import java.security.PrivateKey;
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


	//TODO testing
	//Maybe send instance of class as parameter ?
	public boolean verify() {
		//Check Signature
		if(verifySignature() == false) {
			System.out.println("#Transaction Signature failed to verify");
			return false;
		}
		//TODO which list ?		
		Controller controller = new Controller(); //TODO suppose to be singleton
		
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
