package com.parsing.messages.types;

import com.parsing.messages.IMessage;
import com.parsing.messages.payloads.Payload;

public class GetPeersMessage implements IMessage {

    public GetPeersMessage() {}

    private String messageType;
    private Payload payload;

    @Override
    public String getMessageType() {
        return null;
    }

    @Override
    public Object getMessagePayload() {
        return null;
    }

    @Override
    public void setMessageType(String messageType) {

    }

    @Override
    public void setMessagePayload(Object messagePayload) {

    }
}
