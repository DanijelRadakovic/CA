package megatravel.com.ca.domain.cert;

import java.security.PrivateKey;
import java.security.cert.Certificate;

public class CerChanPrivateKey {
    private Certificate[] chain;
    private PrivateKey privateKey;

    public CerChanPrivateKey() {
    }

    public CerChanPrivateKey(Certificate[] chain, PrivateKey privateKey) {
        this.chain = chain;
        this.privateKey = privateKey;
    }

    public Certificate[] getChain() {
        return chain;
    }

    public void setChain(Certificate[] chain) {
        this.chain = chain;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }
}
