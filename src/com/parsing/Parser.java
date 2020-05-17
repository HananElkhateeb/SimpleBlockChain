package com.parsing;

import com.Client;
import com.Transaction;
import com.TransactionInput;
import com.TransactionOutput;
import com.parsing.messages.IMessage;
import com.parsing.messages.payloads.PayloadFactory;
import com.parsing.messages.payloads.types.*;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import com.parsing.messages.Message;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

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
        } else if(receivedMessage.messageType.equals(MessagesTypes.TRANSACTION_MESSAGE.toString())) {
            TransactionPayload transPayload = objectMapper.convertValue(receivedMessage.payload, TransactionPayload.class);
            receivedMessage.payload = transPayload;
        }
        return receivedMessage;
    }

//    public static void main(String[] args) {
//    	 Message transMessage = new Message();
//    	 transMessage.setMessageType(MessagesTypes.TRANSACTION_MESSAGE.toString());
//
//         PayloadFactory payloadFactory = new PayloadFactory();
//         TransactionPayload blockPayload = (TransactionPayload) payloadFactory.getPayload(PayloadTypes.TRANSACTION_PAYLOAD);
//         Client client = new Client();
//         Client client2 = new Client();
//
//         client.generateKeys();
//         client2.generateKeys();
//         String pub_1 = Base64.getEncoder().encodeToString(client.getPublicKey().getEncoded());
//         String pub_2 = Base64.getEncoder().encodeToString(client2.getPublicKey().getEncoded());
//
//         Transaction t = new Transaction();
//         TransactionInput input = new TransactionInput();
//         input.setInput(7);
//         input.setSender(pub_1);
//         input.setPrevTX(1);
//         input.setPrevOutputIndex((short) 3);
//         TransactionOutput output = new TransactionOutput();
//         output.setReciever(pub_2);
//         output.setValue((float) 30.5);
//         output.setOutputIndex(1);
//         t.setInput(input);
//         List<TransactionOutput> list = new ArrayList<>();
//         list.add(output);
//         t.setOutputs(list);
//         t.generateSignature(client.getPrivateKey());
//         t.setHash(t.calculateHash());
//         t.setTransactionID(1);
//         blockPayload.setHash(t.getHash());
//         blockPayload.setOutputs(t.getOutputs());
//         blockPayload.setSignature(t.getSignature());
//         blockPayload.setTransactionID(1);
//         blockPayload.setInput(t.getInput());
//         transMessage.setMessagePayload(blockPayload);
//
//         Parser parser = new Parser();
//         String message = parser.serializeMessage(transMessage);
//         Message messageObj = parser.deSerializeMessage(message);
//         if(messageObj.getMessageType().equals(MessagesTypes.TRANSACTION_MESSAGE.toString())) {
//        	 TransactionPayload tx = (TransactionPayload) messageObj.getMessagePayload();
//        	 //Transaction tOut = new Transaction();
//        	 System.out.println(tx.getInput().getSender());
//        	 System.out.println(tx.getOutputs().get(0).getReciever());
//        	 System.out.println(tx.getTransactionID());
//        	 System.out.println(tx.getHash());
//        	 System.out.println(t.getHash());
//        	 KeyFactory factory;
//     		try {
//     			factory = KeyFactory.getInstance("EC");
//     			PublicKey public_key_1 = (PublicKey) factory.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(tx.getInput().getSender())));
//     			PublicKey public_key_2 = (PublicKey) factory.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(tx.getOutputs().get(0).getReciever())));
//     			System.out.println(public_key_1.equals(client.getPublicKey()));
//     			System.out.println(public_key_2.equals(client2.getPublicKey()));
//     		}catch(Exception e) {
//   
//     			e.printStackTrace();
//     		}
//
//
//         }
       
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

//       Parser parser = new Parser();
//        parser.parseInputLineTransaction("intput 0  value:\n");
//    }
}
