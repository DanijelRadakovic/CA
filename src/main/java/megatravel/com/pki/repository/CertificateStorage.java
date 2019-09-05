package megatravel.com.pki.repository;

import megatravel.com.pki.config.AppConfig;
import megatravel.com.pki.domain.cert.CerChanPrivateKey;
import megatravel.com.pki.domain.enums.CerType;
import megatravel.com.pki.generator.helper.IssuerData;
import megatravel.com.pki.util.GeneralException;
import net.schmizz.sshj.DefaultConfig;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.signature.SignatureDSA;
import net.schmizz.sshj.signature.SignatureECDSA;
import net.schmizz.sshj.signature.SignatureRSA;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.xfer.FileSystemFile;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class CertificateStorage {

    @Autowired
    private AppConfig config;

    public void store(X509Certificate[] chain, PrivateKey privateKey) {
        char[] password = config.getKeystorePassword().toCharArray();
        String serialNumber = chain[0].getSerialNumber().toString();
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            try {
                keyStore.load(new FileInputStream(config.getKeystore()), password);
            } catch (IOException e) {
                keyStore.load(null, null);
            }

            keyStore.setKeyEntry(serialNumber, privateKey, serialNumber.toCharArray(), chain);
            keyStore.store(new FileOutputStream(config.getKeystore()), password);
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            throw new GeneralException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void sendToCertificateRepository(String serialNumber, String hostname, String destination, CerType cerType) {
        new Thread(() -> {
            String keystorePath = createTempKeystore(getCertificateChain(serialNumber, true));
            try {
                send(hostname, keystorePath, destination);
                if (cerType == CerType.END_ENTITY) {
                    removeEndEntityCertificate(serialNumber);
                }
            } catch (IOException e) {
                throw new GeneralException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            removeTempKeystore(keystorePath);
        }).start();
    }

    public IssuerData findCAbySerialNumber(String serialNumber) {
        char[] password = config.getKeystorePassword().toCharArray();
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(new FileInputStream(config.getKeystore()), password);

            if (!Collections.list(keyStore.aliases()).contains(serialNumber)) {
                return null;
            }
            Key key = keyStore.getKey(serialNumber, serialNumber.toCharArray());
            if (key instanceof PrivateKey) {
                X509Certificate cert = (X509Certificate) keyStore.getCertificate(serialNumber);
                return new IssuerData((PrivateKey) key, new JcaX509CertificateHolder(cert).getSubject(),
                        cert.getPublicKey(), cert.getSerialNumber());
            } else {
                throw new GeneralException("Error occurred while storing certificate.",
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException |
                UnrecoverableKeyException e) {
            throw new GeneralException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void createTrustStorage(X509Certificate cert) {
        try {
            File directory = new File(config.getTruststore());
            if (!directory.exists()) {
                if (!directory.mkdirs()) throw new IOException("Can not create directory.");
            }
            KeyStore trustStore = KeyStore.getInstance("PKCS12");
            trustStore.load(null, null);

            trustStore.store(new FileOutputStream(config.getTruststore() + File.separator +
                    cert.getSerialNumber() + ".p12"), config.getTruststorePassword().toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            throw new GeneralException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<X509Certificate> getCertificates(List<String> serialNumbers) {
        List<X509Certificate> certs = new ArrayList<>(serialNumbers.size());
        char[] password = config.getKeystorePassword().toCharArray();
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(new FileInputStream(config.getKeystore()), password);

            serialNumbers.forEach(s -> {
                try {
                    certs.add((X509Certificate) keyStore.getCertificate(s));
                } catch (KeyStoreException e) {
                    throw new GeneralException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                }
            });
            return certs;
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException e) {
            e.printStackTrace();
            throw new GeneralException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public CerChanPrivateKey getCertificateChain(String serialNumber, boolean privateKey) {
        char[] password = config.getKeystorePassword().toCharArray();
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(new FileInputStream(config.getKeystore()), password);
            Certificate[] certs = keyStore.getCertificateChain(serialNumber);

            if (!privateKey) return new CerChanPrivateKey(certs, null);

            Key key = keyStore.getKey(serialNumber, serialNumber.toCharArray());
            if (key instanceof PrivateKey) {
                return new CerChanPrivateKey(certs, (PrivateKey) key);
            } else {
                throw new GeneralException("Error occurred while reading certificate.",
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException |
                CertificateException | UnrecoverableKeyException e) {
            throw new GeneralException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String createTempKeystore(CerChanPrivateKey cerKey) {
        char[] password = config.getKeystorePassword().toCharArray();
        try {
            String serialNumber = ((X509Certificate) cerKey.getChain()[0]).getSerialNumber().toString();
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(null, null);

            keyStore.setKeyEntry(serialNumber, cerKey.getPrivateKey(), serialNumber.toCharArray(), cerKey.getChain());
            keyStore.store(new FileOutputStream(serialNumber + ".p12"), password);
            return serialNumber + ".p12";
        } catch (ClassCastException | KeyStoreException | NoSuchAlgorithmException
                | CertificateException | IOException e) {
            throw new GeneralException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void removeTempKeystore(String keystorePath) {
        //noinspection ResultOfMethodCallIgnored
        new File(keystorePath).delete();
    }

    private void removeEndEntityCertificate(String serialNumber) {
        char[] password = config.getKeystorePassword().toCharArray();
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(new FileInputStream(config.getKeystore()), password);

            keyStore.deleteEntry(serialNumber);
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException |
                CertificateException e) {
            throw new GeneralException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void send(String hostname, String source, String destination) throws IOException {
        DefaultConfig sftpConfig = new DefaultConfig();
        sftpConfig.setSignatureFactories(
                new SignatureECDSA.Factory256(),
                new SignatureECDSA.Factory384(),
                new SignatureECDSA.Factory521(),
                new SignatureRSA.Factory(),
                new SignatureDSA.Factory());
        final SSHClient sshClient = new SSHClient(sftpConfig);
        sshClient.addHostKeyVerifier(new PromiscuousVerifier());
        sshClient.loadKnownHosts();
        sshClient.connect(hostname);
        sshClient.authPublickey(config.getSftpUsername());
        try (SFTPClient sftpClient = sshClient.newSFTPClient()) {
            sftpClient.put(new FileSystemFile(source), destination);
        }
        sshClient.close();
    }
}
