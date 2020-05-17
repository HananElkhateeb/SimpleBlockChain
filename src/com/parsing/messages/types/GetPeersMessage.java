package com.parsing.messages.types;

import com.parsing.messages.IMessage;
import com.parsing.messages.Message;
import com.parsing.messages.payloads.IPayload;

public class GetPeersMessage extends Message implements IMessage {

    public GetPeersMessage() {}

    private String messageType;
    private IPayload payload;

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
