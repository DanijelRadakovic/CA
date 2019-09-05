package megatravel.com.pki.generator.extension.holder;

import java.util.HashMap;
import java.util.Map;

public class ExtensionHolders {
    private Map<String, X509ExtensionHolder> holders;

    public ExtensionHolders() {
        this.holders = new HashMap<>();
    }

    public ExtensionHolders(Map<String, X509ExtensionHolder> holders) {
        this.holders = holders;
    }

    public X509ExtensionHolder getHolder(String canonicalName) {
        return holders.get(canonicalName);
    }

    public Map<String, X509ExtensionHolder> getHolders() {
        return holders;
    }

    public void setHolders(Map<String, X509ExtensionHolder> holders) {
        this.holders = holders;
    }
}
