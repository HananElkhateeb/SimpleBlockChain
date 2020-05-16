package parsing.messages;

import parsing.messages.payloads.Payload;

public interface IMessage {
    String getMessageType();
    Object getMessagePayload();
    void setMessageType(String messageType);
    void setMessagePayload(Object messagePayload);
}
