import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Transaction {
	private long transactionID;
	private TransactionInput input;
	private List<TransactionOutput> outputs;
	private String hash;
	public byte[] signature;


	public Transaction(){
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

	public void generateSignature(PrivateKey privateKey){
		String senderPubKeySTR = Base64.getEncoder().encodeToString(input.getSender().getEncoded());
		StringBuffer outputSTR = new StringBuffer();
		for(TransactionOutput txo : outputs) {
			outputSTR.append(Base64.getEncoder().encodeToString(txo.getReciever().getEncoded()));
			outputSTR.append(Float.toString(txo.getValue()));
		}
		signature = Utils.digialSignature(privateKey, senderPubKeySTR+outputSTR.toString());
	}

	public boolean verifySignature(){
		String senderPubKeySTR = Base64.getEncoder().encodeToString(input.getSender().getEncoded());
		StringBuffer outputSTR = new StringBuffer();
		for(TransactionOutput txo : outputs) {
			outputSTR.append(Base64.getEncoder().encodeToString(txo.getReciever().getEncoded()));
			outputSTR.append(Float.toString(txo.getValue()));
		}
		String data = senderPubKeySTR+outputSTR.toString();
		return Utils.verifyDigitalSign(input.getSender(), data, signature);
	}

	public String calculateHash(){
		String senderPubKeySTR = Base64.getEncoder().encodeToString(input.getSender().getEncoded());
		StringBuffer outputSTR = new StringBuffer();
		for(TransactionOutput txo : outputs) {
			outputSTR.append(Base64.getEncoder().encodeToString(txo.getReciever().getEncoded()));
			outputSTR.append(Float.toString(txo.getValue()));
		}
		String data = senderPubKeySTR+outputSTR.toString();
		//current time to not have same hash value
		return Utils.sha256(data + new Date().getTime());
	}


	//TODO not complete
	public boolean verify() {
		//Check Signature
		if(verifySignature() == false) {
			System.out.println("#Transaction Signature failed to verify");
			return false;
		}

		String prevTX = Long.toString(input.getPrevTX());
		Short prevO = input.getPrevOutputIndex();
		Block currBlock = new Block("prevHash"); //TODO current block
		Transaction prevTx = currBlock.getTransaction(prevTX);
		TransactionOutput txIN = prevTx.getOutputs().get(prevO);

		//TODO check same public key from previous transaction, need to get the transaction list
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

	private float computeTotal() {
		float total = 0;
		for(TransactionOutput txo : outputs)
			total += txo.getValue();
		return total;
	}
}
