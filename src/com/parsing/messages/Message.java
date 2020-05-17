package com.parsing.messages;

public class Message implements IMessage {

    public String messageType;
    public Object payload;

    public String getMessageType() {
        return this.messageType;
    }

    @Override
    public Object getMessagePayload() {
        return this.payload;
    }

    @Override
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }


    @Override
    public void setMessagePayload(Object messagePayload) {
        this.payload = messagePayload;
    }

}
