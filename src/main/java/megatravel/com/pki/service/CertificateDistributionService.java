package megatravel.com.pki.service;

import megatravel.com.pki.config.AppConfig;
import megatravel.com.pki.domain.cert.CerChanPrivateKey;
import megatravel.com.pki.domain.cert.CertificateDistribution;
import megatravel.com.pki.repository.CertificateStorage;
import megatravel.com.pki.util.GeneralException;
import megatravel.com.pki.util.ZipUtils;
import net.schmizz.sshj.DefaultConfig;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.signature.SignatureDSA;
import net.schmizz.sshj.signature.SignatureECDSA;
import net.schmizz.sshj.signature.SignatureRSA;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.xfer.FileSystemFile;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.bouncycastle.openssl.PKCS8Generator;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.openssl.jcajce.JcaPKCS8Generator;
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8EncryptorBuilder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.OutputEncryptor;
import org.bouncycastle.util.io.pem.PemObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class CertificateDistributionService {

    @Autowired
    private CertificateStorage certificateStorage;

    @Autowired
    private TrustStorageService trustStorageService;

    @Autowired
    private AppConfig appConfig;


    public void distribute(CertificateDistribution distribution) {
        String dir = "." + distribution.getSerialNumber() + "_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss"));
        Path path = Paths.get(dir);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new GeneralException("Can not create temporary distribution directory.",
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        CerChanPrivateKey cert = createCertificates(path, distribution.getSerialNumber(), distribution.isPrivateKey());
        boolean isCA =((X509Certificate) cert.getChain()[0]).getBasicConstraints() != -1;
        if (!isCA && distribution.isKeystore()) createKeystore(path, distribution.getSerialNumber(), cert);
        if (!isCA && distribution.isTruststore()) createTruststore(distribution.getSerialNumber(), path);

        try {
            ZipUtils zipUtils = new ZipUtils();
            zipUtils.generateFileList(path.toFile());
            zipUtils.zipIt(path.toString() + ".zip");
            upload(distribution.getHostname(), path.toAbsolutePath().toString() + ".zip", distribution.getDestination());
        } finally {
            try {
                FileUtils.deleteDirectory(path.toFile());
                //noinspection ResultOfMethodCallIgnored
                new File(path.toString() + ".zip").delete();
            } catch (IOException ignored) {
            }
        }

    }

    private CerChanPrivateKey createCertificates(Path path, String serialNumber, boolean privateKey) {
        CerChanPrivateKey certs = certificateStorage.getCertificateChain(serialNumber, privateKey);
        JcaPEMWriter pemWrt = null;
        try {
            for (int i = 0; i < certs.getChain().length; i++) {
                if (i == 0) {
                    pemWrt = new JcaPEMWriter(new FileWriter(path.toString() + File.separator +
                            "client.pem"));
                } else if (i < certs.getChain().length - 1 && certs.getChain().length > 3) {
                    pemWrt = new JcaPEMWriter(new FileWriter(path.toString() + File.separator +
                            "intermediate" + i + ".pem"));
                } else if (i < certs.getChain().length - 1) {
                    pemWrt = new JcaPEMWriter(new FileWriter(path.toString() + File.separator +
                            "intermediate.pem"));
                } else {
                    pemWrt = new JcaPEMWriter(new FileWriter(path.toString() + File.separator +
                            "root.pem"));
                }
                pemWrt.writeObject(certs.getChain()[i]);
                pemWrt.flush();
            }
            if (pemWrt != null) pemWrt.close();
            if (privateKey) createPrivateKey(path, serialNumber, certs.getPrivateKey());
            return certs;
        } catch (IOException e) {
            try {
                FileUtils.deleteDirectory(path.toFile());
            } catch (IOException ignored) {
            }
            throw new GeneralException("Could not create certificate chain files.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void createPrivateKey(Path path, String serialNumber, PrivateKey privateKey) {
        JceOpenSSLPKCS8EncryptorBuilder encryptorBuilder =
                new JceOpenSSLPKCS8EncryptorBuilder(PKCS8Generator.DES3_CBC);
        encryptorBuilder.setRandom(new SecureRandom());
        encryptorBuilder.setPasssword(serialNumber.toCharArray());
        try {
            OutputEncryptor encryptor = encryptorBuilder.build();

            // construct object to create the PKCS8 object from the private key and encryptor
            JcaPKCS8Generator pkcsGenerator = new JcaPKCS8Generator(privateKey, encryptor);
            PemObject pemObj = pkcsGenerator.generate();
            StringWriter stringWriter = new StringWriter();
            try (JcaPEMWriter pemWriter = new JcaPEMWriter(stringWriter)) {
                pemWriter.writeObject(pemObj);
            }

            // write PKCS8 to file
            String pkcs8Key = stringWriter.toString();
            FileOutputStream fos = new FileOutputStream(path.toString() + File.separator + "key.pem");
            fos.write(pkcs8Key.getBytes(StandardCharsets.UTF_8));
            fos.flush();
            fos.close();
        } catch (OperatorCreationException | IOException e) {
            try {
                FileUtils.deleteDirectory(path.toFile());
            } catch (IOException ignored) {
            }
            throw new GeneralException("Could not store private key.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void createKeystore(Path path, String serialNumber, CerChanPrivateKey certs) {
        char[] password = appConfig.getKeystorePassword().toCharArray();
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(null, null);

            keyStore.setKeyEntry(serialNumber, certs.getPrivateKey(), password, certs.getChain());
            keyStore.store(new FileOutputStream(path.toString() + File.separator + "keystore.p12"), password);
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            try {
                FileUtils.deleteDirectory(path.toFile());
            } catch (IOException ignored) {
            }
            throw new GeneralException("Could not create keystore.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void createTruststore(String serialNumber, Path to) {
        Path from = Paths.get(appConfig.getTruststore() + File.separator + serialNumber + ".p12");
        to = Paths.get(to.toString(), "truststore.p12");
        try {
            Files.copy(from, to);
        } catch (IOException e) {
            try {
                FileUtils.deleteDirectory(to.toFile());
            } catch (IOException ignored) {
            }
            throw new GeneralException("Could not create truststore.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void upload(String hostname, String source, String destination) {
        DefaultConfig config = new DefaultConfig();
        config.setSignatureFactories(
                new SignatureECDSA.Factory256(),
                new SignatureECDSA.Factory384(),
                new SignatureECDSA.Factory521(),
                new SignatureRSA.Factory(),
                new SignatureDSA.Factory());
        final SSHClient sshClient = new SSHClient(config);
        try {
            sshClient.addHostKeyVerifier(new PromiscuousVerifier());
            sshClient.loadKnownHosts();
            sshClient.connect(hostname);
            sshClient.authPublickey(appConfig.getSftpUsername());
            try (SFTPClient sftpClient = sshClient.newSFTPClient()) {
                sftpClient.put(new FileSystemFile(source), destination);
            }
            sshClient.close();
        } catch (IOException e) {
            throw new GeneralException("Could not send resource.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
