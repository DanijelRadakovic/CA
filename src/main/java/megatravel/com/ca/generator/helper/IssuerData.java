package megatravel.com.ca.generator.helper;

import org.bouncycastle.asn1.x500.X500Name;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;

public class IssuerData {

    private X500Name x500name;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private BigInteger serialNumber;

    public IssuerData() {
    }

    public IssuerData(PrivateKey privateKey, X500Name x500name, PublicKey publicKey, BigInteger serialNumber) {
        this.privateKey = privateKey;
        this.x500name = x500name;
        this.publicKey = publicKey;
        this.serialNumber = serialNumber;
    }

    public X500Name getX500name() {
        return x500name;
    }

    public void setX500name(X500Name x500name) {
        this.x500name = x500name;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public BigInteger getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(BigInteger serialNumber) {
        this.serialNumber = serialNumber;
    }
}