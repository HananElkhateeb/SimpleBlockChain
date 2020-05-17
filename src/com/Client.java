package com;

import com.parsing.Parser;
import com.parsing.messages.Message;
import com.parsing.messages.MessagesTypes;
import com.parsing.messages.payloads.PayloadFactory;
import com.parsing.messages.payloads.types.PayloadTypes;
import com.parsing.messages.payloads.types.TransactionPayload;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.*;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class Client implements IClient {
	private PublicKey publicKey;
	private PrivateKey privateKey;
	private Map<Integer, PublicKey> nodes;
	private int clientID;//TODO how to set IDs ?
 
   public Client(int clientID){
   	this.clientID = clientID;
   }
    @Override
    public void getTransactions(String filePath) {
		Parser parser = new Parser();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filePath));
			String line = reader.readLine();
			while (line != null) {
				Transaction transaction = parser.parseInputLineTransaction(line);
				if (this.clientID == transaction.getInput().getInput()) {
					transaction = fillSecurityFields(transaction);
					broadcastTransaction(transaction);
				}
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

	private Transaction fillSecurityFields(Transaction transaction) {
		String hash = transaction.calculateHash();
		transaction.setHash(hash);
		transaction.getInput().setSender(Base64.getEncoder().encodeToString(this.publicKey.getEncoded()));
		transaction.generateSignature(this.privateKey);
		List<TransactionOutput> outputs = transaction.getOutputs();
		for(TransactionOutput txo : outputs) {
			//TODO if client does not exist
			Integer clientID = txo.getOutputIndex();
			txo.setReciever(Base64.getEncoder().encodeToString(nodes.get(clientID).getEncoded()));
		}
    	return transaction;
	}

	@Override
    public void generateKeys() {
    	try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			//ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");

			keyGen.initialize(256, random);   //256 bytes provides an acceptable security level
			KeyPair keyPair = keyGen.generateKeyPair();

			// set the public and private keys from the keyPair 	
			privateKey = keyPair.getPrivate();    	
			publicKey = keyPair.getPublic();
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
    }

    @Override
	//TODO: while loop to send to all nodes
    public void broadcastTransaction(Transaction transaction) throws IOException {
    	NodeSender nodeSender = new NodeSender();
		Message transMessage = new Message();
		PayloadFactory payloadFactory = new PayloadFactory();
		TransactionPayload transactionPayload = (TransactionPayload) payloadFactory.getPayload(PayloadTypes.TRANSACTION_PAYLOAD);

		transactionPayload.setInput(transaction.getInput());
		transactionPayload.setOutputs(transaction.getOutputs());
		transactionPayload.setHash(transaction.getHash());
		transactionPayload.setSignature(transaction.getSignature());
		transactionPayload.setTransactionID(transaction.getTransactionID());
		transactionPayload.setInitialTransaction(transaction.isInitialTransaction());

		transMessage.setMessagePayload(transactionPayload);
		transMessage.setMessageType(MessagesTypes.TRANSACTION_MESSAGE.toString());
		Parser parser = new Parser();
		String message = parser.serializeMessage(transMessage);
		nodeSender.send(message);
    }

	@Override
	public PublicKey getPublicKey() {
		return publicKey;
	}

	@Override
	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	@Override
	public boolean addNewNode(Integer id, PublicKey publicKey) {
		if(nodes.containsKey(id)) {
			if(nodes.get(id) == publicKey)
				return false;
			nodes.replace(id, publicKey);
		}
		nodes.put(id, publicKey);
		return true;
	}
}
