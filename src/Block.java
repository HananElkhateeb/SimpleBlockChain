import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Block {
    private int blockThreshold;
    private String prevBlockHash;
    private String merkleTreeRoot;
    private long timeStamp;
    private int nonce = -1;
    private String hash;
    private Map<String, Transaction> transactions;
    


    public Block (String prevHash){
        this.prevBlockHash = prevBlockHash;
        this.timeStamp = new Date().getTime();
    }

    public int getBlockThreshold() {
        return blockThreshold;
    }

    public void setBlockThreshold(int blockThreshold) {
        this.blockThreshold = blockThreshold;
    }

    public String getPrevBlockHash() {
        return prevBlockHash;
    }

    public void setPrevBlockHash(String prevBlockHash) {
        this.prevBlockHash = prevBlockHash;
    }

    public String getMerkleTreeRoot() {
        return merkleTreeRoot;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Map<String, Transaction> getTransactions() {
        return transactions;
    }
    public Transaction getTransaction(String txid) {
        return transactions.get(txid);
    }

    public void addTransactions(Transaction tx) {
        this.transactions.put(Long.toString(tx.getTransactionID()), tx);
    }

    public void calculateMerkleTreeRoot() {
    }


    public void calculateBlockHash(){

    }

    public void solveBlock (int type){
        if (type == 1){
            proofOfWork();
        } else if (type == 2){
            ByzantineFaultTolerance();
        } else {
            System.out.println("Enter Correct Type");
        }
    }

    public void proofOfWork(){

    }

    public void ByzantineFaultTolerance(){

    }

    public void addTransaction(Transaction transaction){

    }

}
