package parsing.messages;

import parsing.messages.types.Message;
import parsing.messages.types.PublicKeyMessage;
import parsing.messages.types.VoteMessage;

public class MessageFactory {

    public Message getShape(String messageType){
        if(messageType == null){
            return null;
        }
        if(messageType.equalsIgnoreCase(MessagesTypes.VOTE_MESSAGE.toString()))
            return new VoteMessage();
        else if(messageType.equalsIgnoreCase(MessagesTypes.PUBLIC_KEY_MESSAGE.toString()))
            return new PublicKeyMessage();
        else
            return null;
    }
}
