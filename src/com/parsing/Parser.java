package com.parsing;

import com.Transaction;
import com.TransactionInput;
import com.TransactionOutput;
import com.parsing.messages.IMessage;
import com.parsing.messages.payloads.types.*;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import com.parsing.messages.Message;

import java.io.IOException;
import java.util.ArrayList;

import com.parsing.messages.MessagesTypes;


public class Parser {

    public Transaction parseInputLineTransaction (String inputLine) {
        String[] params = inputLine.split("\\s+");

        Transaction transaction = new Transaction();
        TransactionInput transactionInput = new TransactionInput();
        if(params.length == 4) {
            transaction.setTransactionID(Long.valueOf(params[0]));
            transaction.setInitialTransaction(true);
            transactionInput.setInput(Integer.valueOf(params[1].substring(params[1].indexOf(':')+1).trim()));

            ArrayList<TransactionOutput> transactionOutputs = new ArrayList<>();
            TransactionOutput transactionOutput = new TransactionOutput();
            transactionOutput.setValue(Float.valueOf(params[2].substring(params[2].indexOf(':')+1).trim()));
            transactionOutput.setOutputIndex(Integer.valueOf(params[3].substring(params[3].indexOf(':')+1).trim()));
            transactionOutputs.add(transactionOutput);
            transaction.setInput(transactionInput);
            transaction.setOutputs(transactionOutputs);
        } else if(params.length > 4) {
            transaction.setTransactionID(Long.valueOf(params[0]));
            transaction.setInitialTransaction(false);
            transactionInput.setInput(Integer.valueOf(params[1].substring(params[1].indexOf(':')+1).trim()));
            transactionInput.setPrevTX(Integer.valueOf(params[2].substring(params[2].indexOf(':')+1).trim()));
            transactionInput.setPrevOutputIndex(Short.valueOf(params[3].substring(params[3].indexOf(':')+1).trim()));
            ArrayList<TransactionOutput> transactionOutputs = new ArrayList<>();

            for(int i = 4; i < params.length; i++) {
                if(params[i].contains("value")) {
                    TransactionOutput transactionOutput = new TransactionOutput();
                    transactionOutput.setValue(Float.valueOf(params[i].substring(params[i].indexOf(':')+1).trim()));
                    transactionOutput.setOutputIndex(Integer.valueOf(params[i+1].substring(params[i+1].indexOf(':')+1).trim()));
                    transactionOutputs.add(transactionOutput);
                    i++;
                }

            }
            transaction.setInput(transactionInput);
            transaction.setOutputs(transactionOutputs);
        }
        return transaction;
    }

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

//        Parser parser = new Parser();
//        parser.parseInputLineTransaction("intput 0  value:\n");
//    }
}
