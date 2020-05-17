package com.parsing;

import com.parsing.messages.IMessage;
import com.parsing.messages.payloads.types.*;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import com.parsing.messages.Message;

import java.io.IOException;

import com.parsing.messages.MessagesTypes;


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

    public static void main(String[] args) {
//        Message blockMessage = new Message();
//        blockMessage.setMessageType(MessagesTypes.BLOCK_MESSAGE.toString());
//
//        PayloadFactory payloadFactory = new PayloadFactory();
//        BlockPayload blockPayload = (BlockPayload) payloadFactory.getPayload(PayloadTypes.BLOCK_PAYLOAD);
//
//        blockPayload.setHash("ayhashfeldenya");
//        blockPayload.setMerkleTreeRoot("ayhagaaa");
//        blockPayload.setTimeStamp(234234234);
//        blockPayload.setPrevBlockHash("sfsdkjfsdkjfhs");
//        blockPayload.setTransactions(new ArrayList<Transaction>());
//        blockMessage.setMessagePayload(blockPayload);
//
//        Parser parser = new Parser();
//        String message = parser.serializeMessage(blockMessage);
//        Message messageObj = parser.deSerializeMessage(message);
    }
}
