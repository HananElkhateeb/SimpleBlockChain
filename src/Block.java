import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Block {
    private int blockThreshold;
    private String prevBlockHash;
    private String merkleTreeRoot;
    private long timeStamp;
    private int nonce = -1;
    private String hash;
    private List<Transaction> transactions;

    public Block (String prevHash){
        this.prevBlockHash = prevBlockHash;
        this.timeStamp = new Date().getTime();
        transactions = new ArrayList<>();
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

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
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
