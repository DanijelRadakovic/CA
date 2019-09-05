package megatravel.com.pki.domain.DTO.cer;

public class CertificateDistributionDTO {
    private String serialNumber;
    private String hostname;
    private String destination;

    public CertificateDistributionDTO() {
    }

    public CertificateDistributionDTO(String serialNumber,
                                      String hostname, String destination) {
        this.serialNumber = serialNumber;
        this.hostname = hostname;
        this.destination = destination;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
