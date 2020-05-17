package com.parsing.messages;

public interface IMessage {
    String getMessageType();
    Object getMessagePayload();
    void setMessageType(String messageType);
    void setMessagePayload(Object messagePayload);
}
