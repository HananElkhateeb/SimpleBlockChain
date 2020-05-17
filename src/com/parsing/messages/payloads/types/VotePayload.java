package com.parsing.messages.payloads.types;

import com.parsing.messages.payloads.IPayload;
import com.parsing.messages.payloads.Payload;

public class VotePayload extends Payload implements IPayload {

    public VotePayload() {}

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
