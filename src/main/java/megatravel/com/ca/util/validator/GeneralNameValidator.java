package megatravel.com.ca.util.validator;


import org.apache.commons.validator.routines.DomainValidator;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.util.IPAddress;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class GeneralNameValidator {

    private static final String RFC_822_REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])";

    public boolean isValidDN(String name) {
        try {
            new GeneralName(GeneralName.directoryName, name);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isValidEmail(String name) {
        return name.matches(RFC_822_REGEX);
    }

    public boolean isValidDNS(String name) {
        return DomainValidator.getInstance().isValid(name);
    }

    public boolean isValidIpAddress(String name) {
        return IPAddress.isValid(name);
    }

    public boolean isValidURI(String name) {
        try {
            new URL(name).toURI();
            return true;
        } catch (URISyntaxException | MalformedURLException e) {
            return false;
        }
    }
}
