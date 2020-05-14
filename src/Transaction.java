import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Transaction {
    private long transactionID;
    private List<TransactionInput> inputs;
    private List<TransactionOutput> outputs;
    private String hash;

    public Transaction(){
        transactionID = -1;
        inputs = new ArrayList<>();
        outputs = new ArrayList<>();
        hash = "";
    }

    public long getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(long transactionID) {
        this.transactionID = transactionID;
    }

    public List<TransactionInput> getInputs() {
        return inputs;
    }

    public void setInputs(List<TransactionInput> inputs) {
        this.inputs = inputs;
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

    }

    public void verifySignature(){

    }

    public String calculateHash(){
        return null;
    }
}
