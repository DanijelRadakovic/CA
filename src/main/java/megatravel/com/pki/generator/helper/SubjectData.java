package megatravel.com.pki.generator.helper;

import org.bouncycastle.asn1.x500.X500Name;

import java.math.BigInteger;
import java.security.PublicKey;
import java.util.Date;

public class SubjectData {

    private PublicKey publicKey;
    private X500Name x500name;
    private BigInteger serialNumber;
    private Date startDate;
    private Date endDate;

    public SubjectData() {

    }

    public SubjectData(PublicKey publicKey, X500Name x500name, BigInteger serialNumber, Date startDate, Date endDate) {
        this.publicKey = publicKey;
        this.x500name = x500name;
        this.serialNumber = serialNumber;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public X500Name getX500name() {
        return x500name;
    }

    public void setX500name(X500Name x500name) {
        this.x500name = x500name;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
