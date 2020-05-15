package parsing.messages;

import parsing.messages.types.MessagesTypes;
import parsing.messages.payloads.Payload;

public interface Message {
    String getMessageType();
    Payload getMessagePayload();
    void setMessageType(MessagesTypes messageType);
    void setMessagePayload(Payload messagePayload);
}
