package com.parsing.messages.types;

import com.parsing.messages.IMessage;
import com.parsing.messages.Message;
import com.parsing.messages.payloads.IPayload;

public class PeerAckMessage extends Message implements IMessage {

    public PeerAckMessage() {}

    private String messageType;
    private IPayload payload;

    @Override
    public String getMessageType() {
        return this.messageType;
    }

    @Override
    public Object getMessagePayload() {
        return this.payload;
    }

    @Override
    public void setMessageType(String messageType) {

    }

    @Override
    public void setMessagePayload(Object messagePayload) {

    }
}
