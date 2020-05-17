package com;

import com.parsing.Parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class Client implements IClient {
	private PublicKey publicKey;
	private PrivateKey privateKey;
	private Map<Integer, PublicKey> nodes;
	private int clientID;//TODO how to set IDs ?
 
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
					broadcastTransaction();
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
    public void broadcastTransaction() {

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
