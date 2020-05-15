package parsing.messages.types;

import parsing.messages.Message;
import parsing.messages.payloads.Payload;

public class TransactionMessage implements Message {

    public TransactionMessage() {}

    private String messageType;
    private Payload payload;

    @Override
    public String getMessageType() {
        return this.messageType;
    }

    @Override
    public Payload getMessagePayload() {
        return this.payload;
    }

    @Override
    public void setMessageType(MessagesTypes messageType) {

    }

    @Override
    public void setMessagePayload(Payload messagePayload) {

    }
}
