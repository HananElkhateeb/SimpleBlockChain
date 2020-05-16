package parsing.messages.types;

import parsing.messages.IMessage;
import parsing.messages.payloads.Payload;

public class BlockMessage implements IMessage {

    public BlockMessage() {}

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
