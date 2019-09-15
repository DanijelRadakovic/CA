package megatravel.com.ca.domain.dto.cer;

import megatravel.com.ca.domain.dto.cer.ext.*;

public class CertificateRequestDTO {
    private SubjectDTO subjectDN;
    private String issuerSN;
    private BasicConstraintsDTO basicConstraints;
    private NameConstraintsDTO nameConstraints;
    private AuthorityInfoAccessDTO authorityInfoAccess;
    private CertificatePoliciesDTO certificatePolicies;
    private KeyUsageDTO keyUsage;
    private ExtendedKeyUsageDTO extendedKeyUsage;
    private SubjectAlternativeNameDTO subjectAlternativeNames;


    public CertificateRequestDTO() {
    }

    public CertificateRequestDTO(SubjectDTO subjectDN, String issuerSN, BasicConstraintsDTO basicConstraints,
                                 NameConstraintsDTO nameConstraints, AuthorityInfoAccessDTO authorityInfoAccess, CertificatePoliciesDTO certificatePolicies, KeyUsageDTO keyUsage, ExtendedKeyUsageDTO extendedKeyUsage, SubjectAlternativeNameDTO subjectAlternativeNames) {
        this.subjectDN = subjectDN;
        this.issuerSN = issuerSN;
        this.basicConstraints = basicConstraints;
        this.nameConstraints = nameConstraints;
        this.authorityInfoAccess = authorityInfoAccess;
        this.certificatePolicies = certificatePolicies;
        this.keyUsage = keyUsage;
        this.extendedKeyUsage = extendedKeyUsage;
        this.subjectAlternativeNames = subjectAlternativeNames;
    }

    public SubjectDTO getSubjectDN() {
        return subjectDN;
    }

    public void setSubjectDN(SubjectDTO subjectDN) {
        this.subjectDN = subjectDN;
    }

    public String getIssuerSN() {
        return issuerSN;
    }

    public void setIssuerSN(String issuerSN) {
        this.issuerSN = issuerSN;
    }

    public BasicConstraintsDTO getBasicConstraints() {
        return basicConstraints;
    }

    public void setBasicConstraints(BasicConstraintsDTO basicConstraints) {
        this.basicConstraints = basicConstraints;
    }

    public NameConstraintsDTO getNameConstraints() {
        return nameConstraints;
    }

    public void setNameConstraints(NameConstraintsDTO nameConstraints) {
        this.nameConstraints = nameConstraints;
    }

    public AuthorityInfoAccessDTO getAuthorityInfoAccess() {
        return authorityInfoAccess;
    }

    public void setAuthorityInfoAccess(AuthorityInfoAccessDTO authorityInfoAccess) {
        this.authorityInfoAccess = authorityInfoAccess;
    }

    public CertificatePoliciesDTO getCertificatePolicies() {
        return certificatePolicies;
    }

    public void setCertificatePolicies(CertificatePoliciesDTO certificatePolicies) {
        this.certificatePolicies = certificatePolicies;
    }

    public KeyUsageDTO getKeyUsage() {
        return keyUsage;
    }

    public void setKeyUsage(KeyUsageDTO keyUsage) {
        this.keyUsage = keyUsage;
    }

    public ExtendedKeyUsageDTO getExtendedKeyUsage() {
        return extendedKeyUsage;
    }

    public void setExtendedKeyUsage(ExtendedKeyUsageDTO extendedKeyUsage) {
        this.extendedKeyUsage = extendedKeyUsage;
    }

    public SubjectAlternativeNameDTO getSubjectAlternativeNames() {
        return subjectAlternativeNames;
    }

    public void setSubjectAlternativeNames(SubjectAlternativeNameDTO subjectAlternativeNames) {
        this.subjectAlternativeNames = subjectAlternativeNames;
    }
}
