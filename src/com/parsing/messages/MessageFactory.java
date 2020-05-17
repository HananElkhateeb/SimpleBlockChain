package com.parsing.messages;

import com.parsing.messages.types.*;

public class MessageFactory {

    public IMessage getMessage(MessagesTypes messageType){
        if(messageType == null){
            return null;
        }
        switch (messageType) {
            case VOTE_MESSAGE:
                return new VoteMessage();
            case GET_PEERS_MESSAGE:
                return new GetPeersMessage();
            case PEER_ACK_MESSAGE:
                return new PeerAckMessage();
            case BLOCK_MESSAGE:
                return new BlockMessage();
            default:
                return null;
        }
    }
}
