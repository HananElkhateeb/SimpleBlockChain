package com;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class Transaction {
	private long transactionID;
	private TransactionInput input;
	private List<TransactionOutput> outputs;
	private String hash;
	private float leftover;
	public byte[] signature;
	boolean initialTransaction;


	public Transaction(){
		leftover = -1;
		transactionID = -1;
		input = null;
		outputs = new ArrayList<>();
		hash = "";
	}
	

	public long getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(long transactionID) {
		this.transactionID = transactionID;
	}


	public TransactionInput getInput() {
		return input;
	}

	public void setInput(TransactionInput input) {
		this.input = input;
	}

	public List<TransactionOutput> getOutputs() {
		return outputs;
	}

	public void setOutputs(List<TransactionOutput> outputs) {
		this.outputs = outputs;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}
	
	public byte[] getSignature() {
		return this.signature;
	}

	public boolean isInitialTransaction() {
		return initialTransaction;
	}

	public void setInitialTransaction(boolean initialTransaction) {
		this.initialTransaction = initialTransaction;
	}

	public void generateSignature(PrivateKey privateKey){
		//String senderPubKeySTR = Base64.getEncoder().encodeToString(input.getSender().getEncoded());
		String senderPubKeySTR = input.getSender();
		StringBuffer outputSTR = new StringBuffer();
		for(TransactionOutput txo : outputs) {
			//outputSTR.append(Base64.getEncoder().encodeToString(txo.getReciever().getEncoded()));
			outputSTR.append(txo.getReciever());

			outputSTR.append(Float.toString(txo.getValue()));
		}
		signature = Utils.digialSignature(privateKey, senderPubKeySTR+outputSTR.toString());
	}

	public boolean verifySignature(){
		try {
		//String senderPubKeySTR = Base64.getEncoder().encodeToString(input.getSender().getEncoded());
		String senderPubKeySTR = input.getSender();
		StringBuffer outputSTR = new StringBuffer();
		for(TransactionOutput txo : outputs) {
			//outputSTR.append(Base64.getEncoder().encodeToString(txo.getReciever().getEncoded()));
			outputSTR.append(txo.getReciever());

			outputSTR.append(Float.toString(txo.getValue()));
		}
		String data = senderPubKeySTR+outputSTR.toString();
		KeyFactory factory;
		factory = KeyFactory.getInstance("EC");
		PublicKey public_key = (PublicKey) factory.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(input.getSender())));
		return Utils.verifyDigitalSign(public_key, data, signature);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return false;
	}

	public String calculateHash(){
		String senderPubKeySTR = input.getSender();
		StringBuffer outputSTR = new StringBuffer();
		for(TransactionOutput txo : outputs) {
			outputSTR.append(txo.getReciever());
			outputSTR.append(Float.toString(txo.getValue()));
		}
		String data = senderPubKeySTR+outputSTR.toString();
		//current time to not have same hash value
		return Utils.sha256(data + new Date().getTime());
	}


	//TODO testing
	public boolean verify(Controller controller) {
		//Check Signature
		if(verifySignature() == false) {
			System.out.println("#com.Transaction Signature failed to verify");
			return false;
		}
		long prevTX = input.getPrevTX();
		Short prevO = input.getPrevOutputIndex();
	
		Transaction prevTx = controller.getTransaction(prevTX);
		TransactionOutput txIN = prevTx.getOutputs().get(prevO);

		//check same public key from previous transaction
	
		if(txIN.getReciever() != input.getSender()) {
			System.out.println("#Transactions public keys don't match");
			return false;
		}
		
		//check credit is enough
		float totalVal = this.computeTotal();
		if(totalVal > txIN.getValue()) {
			System.out.println("#Transactions credit is not enough");
			return false;
		}

		return true;
	}
	
	public float getLeftOver() {
		if(leftover != -1)
			return leftover;
		float total = this.computeTotal();
		long prevTX = input.getPrevTX();
		Short prevO = input.getPrevOutputIndex();
		Controller controller = new Controller(); //TODO which list?

		Transaction prevTx = controller.getTransaction(prevTX);
		TransactionOutput txIN = prevTx.getOutputs().get(prevO);
				
		float wallet = txIN.getValue();
		this.leftover = wallet - total;
		return leftover;
	}

	private float computeTotal() {
		float total = 0;
		for(TransactionOutput txo : outputs)
			total += txo.getValue();
		return total;
	}
}
