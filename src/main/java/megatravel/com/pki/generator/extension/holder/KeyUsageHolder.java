package megatravel.com.pki.generator.extension.holder;

import megatravel.com.pki.util.ValidationException;
import org.bouncycastle.asn1.x509.KeyUsage;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KeyUsageHolder extends X509ExtensionHolder {

    private boolean digitalSignature;
    private boolean nonRepudiation;
    private boolean keyEncipherment;
    private boolean dataEncipherment;
    private boolean keyAgreement;
    private boolean keyCertSign;
    private boolean cRLSign;
    private boolean encipherOnly;
    private boolean decipherOnly;

    private static final Map<String, Integer> MAPPER = new HashMap<String, Integer>() {{
        put("digitalSignature", KeyUsage.digitalSignature);
        put("nonRepudiation", KeyUsage.nonRepudiation);
        put("keyEncipherment", KeyUsage.keyEncipherment);
        put("dataEncipherment", KeyUsage.dataEncipherment);
        put("keyAgreement", KeyUsage.keyAgreement);
        put("keyCertSign", KeyUsage.keyCertSign);
        put("cRLSign", KeyUsage.cRLSign);
        put("encipherOnly", KeyUsage.encipherOnly);
        put("decipherOnly", KeyUsage.decipherOnly);
    }};

    public KeyUsageHolder() {
        this.name = "Key Usage";
        this.ASN1ObjectIdentifier = "2.5.29.15";
        this.critical = false;
    }

    public KeyUsageHolder(boolean critical, boolean digitalSignature, boolean nonRepudiation, boolean keyEncipherment,
                          boolean dataEncipherment, boolean keyAgreement, boolean keyCertSign, boolean cRLSign,
                          boolean encipherOnly, boolean decipherOnly) {
        this.name = "Key Usage";
        this.ASN1ObjectIdentifier = "2.5.29.15";
        this.critical = critical;
        this.digitalSignature = digitalSignature;
        this.nonRepudiation = nonRepudiation;
        this.keyEncipherment = keyEncipherment;
        this.dataEncipherment = dataEncipherment;
        this.keyAgreement = keyAgreement;
        this.keyCertSign = keyCertSign;
        this.cRLSign = cRLSign;
        this.encipherOnly = encipherOnly;
        this.decipherOnly = decipherOnly;
    }

    public int getKeyUsageBits() {
        List<String> activeFields = Arrays.stream(this.getClass().getDeclaredFields()).filter(field -> {
            try {
                return field.getBoolean(this);
            } catch (IllegalAccessException | IllegalArgumentException e) {
                return false;
            }
        }).map(Field::getName).collect(Collectors.toList());

        if (activeFields.isEmpty()) {
            throw new ValidationException("Key Usage field must be set!");
        } else if (activeFields.size() == 1){
            return MAPPER.get(activeFields.get(0));
        } else {
            int result = MAPPER.get(activeFields.get(0));
            for (int i = 1; i < activeFields.size(); i++) {
                result |= MAPPER.get(activeFields.get(i));
            }
            return result;
        }
    }

    public boolean isDigitalSignature() {
        return digitalSignature;
    }

    public void setDigitalSignature(boolean digitalSignature) {
        this.digitalSignature = digitalSignature;
    }

    public boolean isNonRepudiation() {
        return nonRepudiation;
    }

    public void setNonRepudiation(boolean nonRepudiation) {
        this.nonRepudiation = nonRepudiation;
    }

    public boolean isKeyEncipherment() {
        return keyEncipherment;
    }

    public void setKeyEncipherment(boolean keyEncipherment) {
        this.keyEncipherment = keyEncipherment;
    }

    public boolean isDataEncipherment() {
        return dataEncipherment;
    }

    public void setDataEncipherment(boolean dataEncipherment) {
        this.dataEncipherment = dataEncipherment;
    }

    public boolean isKeyAgreement() {
        return keyAgreement;
    }

    public void setKeyAgreement(boolean keyAgreement) {
        this.keyAgreement = keyAgreement;
    }

    public boolean isKeyCertSign() {
        return keyCertSign;
    }

    public void setKeyCertSign(boolean keyCertSign) {
        this.keyCertSign = keyCertSign;
    }

    public boolean iscRLSign() {
        return cRLSign;
    }

    public void setcRLSign(boolean cRLSign) {
        this.cRLSign = cRLSign;
    }

    public boolean isEncipherOnly() {
        return encipherOnly;
    }

    public void setEncipherOnly(boolean encipherOnly) {
        this.encipherOnly = encipherOnly;
    }

    public boolean isDecipherOnly() {
        return decipherOnly;
    }

    public void setDecipherOnly(boolean decipherOnly) {
        this.decipherOnly = decipherOnly;
    }
}
