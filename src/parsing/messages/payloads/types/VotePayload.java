package parsing.messages.payloads.types;

import parsing.messages.payloads.Payload;

public class VotePayload implements Payload {
    private int clientId;
    private boolean voting;

    public boolean isVoting() {
        return voting;
    }

    public void setVoting(boolean voting) {
        this.voting = voting;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }
}
