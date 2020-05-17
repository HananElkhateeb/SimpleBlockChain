package com.parsing;

import com.parsing.messages.IMessage;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import com.parsing.messages.Message;
import com.parsing.messages.payloads.types.BlockPayload;
import com.parsing.messages.payloads.types.GetPeersPayload;
import com.parsing.messages.payloads.types.PeerAckPayload;

import java.io.IOException;

import com.parsing.messages.payloads.types.VotePayload;
import com.parsing.messages.types.MessagesTypes;


public class Parser {

    public String serializeMessage(IMessage message) {
        String jsonMessage  = null;
        try {
            jsonMessage = new ObjectMapper().writeValueAsString(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonMessage;
    }

   public Message deSerializeMessage(String message) {
        Message receivedMessage = null;
        try {
            receivedMessage = new ObjectMapper().readValue(message, Message.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        }  catch (IOException e) {
            e.printStackTrace();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        if(receivedMessage.messageType.equals(MessagesTypes.GET_PEERS_MESSAGE.toString())) {
            GetPeersPayload getPeersPayload = objectMapper.convertValue(receivedMessage.payload, GetPeersPayload.class);
            receivedMessage.payload = getPeersPayload;
        } else if(receivedMessage.messageType.equals(MessagesTypes.BLOCK_MESSAGE.toString())) {
            BlockPayload blockPayload = objectMapper.convertValue(receivedMessage.payload, BlockPayload.class);
            receivedMessage.payload = blockPayload;
        } else if(receivedMessage.messageType.equals(MessagesTypes.PEER_ACK_MESSAGE.toString())) {
            PeerAckPayload peerAckPayload = objectMapper.convertValue(receivedMessage.payload, PeerAckPayload.class);
            receivedMessage.payload = peerAckPayload;
        } else if(receivedMessage.messageType.equals(MessagesTypes.VOTE_MESSAGE.toString())) {
            VotePayload votePayload = objectMapper.convertValue(receivedMessage.payload, VotePayload.class);
            receivedMessage.payload = votePayload;
        }
        return receivedMessage;
    }

//    public static void main(String[] args) {
//        Message message = new Message();
//        message.messageType = MessagesTypes.BLOCK_MESSAGE.toString();
//        BlockPayload payload = new BlockPayload();
//        payload.setInput(new ArrayList<>(Arrays.asList(1, 2, 3)));
//        payload.setOutput(new ArrayList<>(Arrays.asList(4, 5, 6)));
//        message.payload = payload;
//
//        Parser parser = new Parser();
//        String jsonMessage = parser.serializeMessage(message);
//        parser.deSerializeMessage(jsonMessage);
//    }
}
