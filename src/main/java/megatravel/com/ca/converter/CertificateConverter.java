package megatravel.com.ca.converter;

import megatravel.com.ca.domain.cert.CertificateDistribution;
import megatravel.com.ca.domain.dto.cer.CertificateDistributionDTO;
import megatravel.com.ca.domain.dto.cer.CertificateRequestDTO;
import megatravel.com.ca.domain.dto.cer.SubjectDTO;
import megatravel.com.ca.domain.dto.cer.ext.AccessDescriptionDTO;
import megatravel.com.ca.domain.dto.cer.ext.KeyUsageDTO;
import megatravel.com.ca.domain.enums.KeyPurposeType;
import megatravel.com.ca.generator.extension.holder.*;
import megatravel.com.ca.generator.helper.PolicyDescriber;
import megatravel.com.ca.generator.helper.PolicyQualifierDescriber;
import megatravel.com.ca.util.exception.GeneralException;
import org.apache.commons.beanutils.PropertyUtils;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.AccessDescription;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CertificateConverter extends AbstractConverter {

    private static final Map<String, ASN1ObjectIdentifier> X500NAME_MAPPER = Collections.unmodifiableMap(
            new HashMap<String, ASN1ObjectIdentifier>() {{
                put("commonName", BCStyle.CN);
                put("surname", BCStyle.SURNAME);
                put("givenName", BCStyle.GIVENNAME);
                put("gender", BCStyle.GENDER);
                put("country", BCStyle.C);
                put("email", BCStyle.E);
                put("organization", BCStyle.O);
                put("organizationUnit", BCStyle.OU);
                put("placeOfBirth", BCStyle.PLACE_OF_BIRTH);
                put("street", BCStyle.STREET);
                put("localityName", BCStyle.L);
                put("postalCode", BCStyle.POSTAL_CODE);
                put("countryOfCitizenship", BCStyle.COUNTRY_OF_CITIZENSHIP);
                put("countryOfResidence", BCStyle.COUNTRY_OF_RESIDENCE);
                put("dn_qualifier", BCStyle.DN_QUALIFIER);
                put("initials", BCStyle.INITIALS);
                put("title", BCStyle.T);
                put("pseudonym", BCStyle.PSEUDONYM);
            }});

    public static X500Name toX500Name(SubjectDTO subject, boolean isCA) {
        String value;
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);

        builder.addRDN(BCStyle.DN_QUALIFIER, System.currentTimeMillis() + "");
        for (Field field : subject.getClass().getDeclaredFields()) {
            try {
                value = (String) PropertyUtils.getProperty(subject, field.getName());
                if (value != null && !value.equals("")) {
                    builder.addRDN(X500NAME_MAPPER.get(field.getName()), value);
                }
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                throw new GeneralException(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }

        if (!isCA) {
            builder.addRDN(BCStyle.UID, System.currentTimeMillis() + "");
        }
        return builder.build();
    }

    public static CertificateDistribution toEntity(CertificateDistributionDTO distribution) {
        return new CertificateDistribution(distribution.getSerialNumber(),
                distribution.getHostname(),
                distribution.getDestination());
    }

    public static ExtensionHolders toExtensionHolders(CertificateRequestDTO certificateRequest) {
        Map<String, X509ExtensionHolder> holders = new HashMap<>();
        holders.put(BasicConstraintsHolder.class.getCanonicalName(), new BasicConstraintsHolder(
                certificateRequest.getBasicConstraints().isCritical(),
                certificateRequest.getBasicConstraints().iscA(),
                certificateRequest.getBasicConstraints().getPathLenConstraint())
        );
        if (certificateRequest.getAuthorityInfoAccess() != null) {
            holders.put(AuthorityInfoAccessHolder.class.getCanonicalName(), new AuthorityInfoAccessHolder(
                    certificateRequest.getAuthorityInfoAccess().isCritical(),
                    certificateRequest.getAuthorityInfoAccess().getAccessDescriptions()
                            .stream()
                            .map(CertificateConverter::toAccessDescription)
                            .collect(Collectors.toList()))
            );
        }
        if (certificateRequest.getCertificatePolicies() != null) {
            holders.put(CertificatePoliciesHolder.class.getCanonicalName(), new CertificatePoliciesHolder(
                    certificateRequest.getCertificatePolicies().isCritical(),
                    certificateRequest.getCertificatePolicies().getPolicies()
                            .stream()
                            .map(certificatePolicy ->
                                    new PolicyDescriber(certificatePolicy.getPolicyId(),
                                            certificatePolicy.getQualifiers()
                                                    .stream()
                                                    .map(policyQualifier ->
                                                            new PolicyQualifierDescriber(policyQualifier
                                                                    .getQualifierId().getQualifierId(),
                                                                    policyQualifier.getQualifier()))
                                                    .collect(Collectors.toList())))
                            .collect(Collectors.toList()))
            );
        }
        if (certificateRequest.getKeyUsage() != null) {
            holders.put(KeyUsageHolder.class.getCanonicalName(), toKeyUsageHolder(certificateRequest.getKeyUsage()));
        }

        if (certificateRequest.getExtendedKeyUsage() != null) {
            holders.put(ExtendedKeyUsageHolder.class.getCanonicalName(), new ExtendedKeyUsageHolder(
                    certificateRequest.getExtendedKeyUsage().isCritical(),
                    certificateRequest.getExtendedKeyUsage().getKeyPurpose()
                            .stream().map(KeyPurposeType::getPurpose).toArray(KeyPurposeId[]::new)
            ));
        }

        if (certificateRequest.getNameConstraints() != null) {
            holders.put(NameConstraintsHolder.class.getCanonicalName(), new NameConstraintsHolder(
                    certificateRequest.getNameConstraints().isCritical(),
                    certificateRequest.getNameConstraints().getPermitted(),
                    certificateRequest.getNameConstraints().getExcluded()
            ));
        }

        if (certificateRequest.getSubjectAlternativeNames() != null) {
            holders.put(SubjectAlternativeNameHolder.class.getCanonicalName(), new SubjectAlternativeNameHolder(
                    certificateRequest.getSubjectAlternativeNames().isCritical(),
                    certificateRequest.getSubjectAlternativeNames().getAltNames()
            ));
        }

        return new ExtensionHolders(holders);
    }

    private static AccessDescription toAccessDescription(AccessDescriptionDTO accessDescriptionDTO) {
        return new AccessDescription(accessDescriptionDTO.getAccessMethod().getType(),
                new GeneralName(GeneralName.uniformResourceIdentifier, accessDescriptionDTO.getLocation()));
    }

    private static KeyUsageHolder toKeyUsageHolder(KeyUsageDTO keyUsageDTO) {
        return new KeyUsageHolder(keyUsageDTO.isCritical(),
                keyUsageDTO.isDigitalSignature(),
                keyUsageDTO.isNonRepudiation(),
                keyUsageDTO.isKeyEncipherment(),
                keyUsageDTO.isDataEncipherment(),
                keyUsageDTO.isKeyAgreement(),
                keyUsageDTO.isKeyCertSign(),
                keyUsageDTO.iscRLSign(),
                keyUsageDTO.isEncipherOnly(),
                keyUsageDTO.isDecipherOnly());
    }
}
