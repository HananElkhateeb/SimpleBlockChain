package parsing.messages.payloads.types;

import parsing.messages.payloads.Payload;

public class PeerAckPayload implements Payload {

    public PeerAckPayload() {}

    private int clientId;
    private String publicKey;

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
