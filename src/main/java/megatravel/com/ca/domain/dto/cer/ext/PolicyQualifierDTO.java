package megatravel.com.ca.domain.dto.cer.ext;

public class PolicyQualifierDTO {
    private PolicyQualifier qualifierId;
    private String qualifier;

    public PolicyQualifierDTO() {
    }

    public PolicyQualifierDTO(PolicyQualifier qualifierId, String qualifier) {
        this.qualifierId = qualifierId;
        this.qualifier = qualifier;
    }

    public PolicyQualifier getQualifierId() {
        return qualifierId;
    }

    public void setQualifierId(PolicyQualifier qualifierId) {
        this.qualifierId = qualifierId;
    }

    public String getQualifier() {
        return qualifier;
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }
}
