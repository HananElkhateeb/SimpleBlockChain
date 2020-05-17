package com.parsing.messages.types;

import com.parsing.messages.IMessage;
import com.parsing.messages.payloads.Payload;

public class VoteMessage implements IMessage {

    public VoteMessage() {}

    private String messageType;
    private Payload payload;

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
