package com;

import java.util.*;

public class Block {
    private int blockThreshold;
    private String prevBlockHash = "";
    private String merkleTreeRoot = "";
    private long timeStamp;
    private int nonce = -1;
    private String hash = "";
    private List<Transaction> transactions;
    private Set<String> spentcoins;



    public Block (String prevBlockHash){
        this.prevBlockHash = prevBlockHash;
        this.timeStamp = new Date().getTime();
        this.spentcoins = new HashSet<>();
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

    public int getNonce() {
        return nonce;
    }

    public void setNonce(int nonce) {
        this.nonce = nonce;
    }

    public String getMerkleTreeRoot() { return merkleTreeRoot; }

    public void setMerkleTreeRoot(String merkleTreeRoot) { this.merkleTreeRoot = merkleTreeRoot; }

    public void setTransactions(List<Transaction> transactions) { this.transactions = transactions; }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public Transaction getTransaction(String txid) {
        for (Transaction transaction : transactions) {
            String tx = transaction.getTransactionID()+"";
            if (tx.equals(txid))
                return transaction;
        }
        return null;
    }

    public void addTransaction(Transaction tx) { transactions.add(tx); }

    public Set<String> getSpentcoins() {
        return spentcoins;
    }

    public void setSpentcoins(Set<String> spentcoins) {
        this.spentcoins = spentcoins;
    }

    public boolean checkDoubleSpend (String coin){
        return spentcoins.contains(coin);
    }

    public boolean containTransaction(long txid) {
        for (Transaction transaction:transactions){
            if (transaction.getTransactionID() == txid)
                return true;
        }
        return false;
    }
    public String calculateMerkleTreeRoot() {
        return Utils.calculateMerkleTreeRoot(transactions);
    }

    public boolean verifyHash(){
        return hash.equals(calculateBlockHash());
    }

    public String calculateBlockHash(){
        return Utils.sha256(prevBlockHash + merkleTreeRoot + Integer.toString(nonce));
    }

    private String getDificultyString(int difficulty) {
        return new String(new char[difficulty]).replace('\0', '0');
    }

    public void solveBlock (int type, int difficulty){
        if (type == 1){
            proofOfWork(difficulty);
        } else if (type == 2){
            ByzantineFaultTolerance();
        } else {
            System.out.println("Enter Correct Type");
        }
    }

    public void proofOfWork(int difficulty){
        String target = getDificultyString(difficulty);
        do {
            nonce++;
            hash = calculateBlockHash();
        } while (!hash.substring(0,difficulty).equals(target));
    }

    //TODO
    public void ByzantineFaultTolerance(){

    }

    public void printBlock(){
        List<Transaction> transactions = this.getTransactions();
        for (Transaction transaction:transactions){
            System.out.println("Block Content");
            transaction.printTransaction();
            System.out.println("-------------------------");
        }
    }

    public Block copy(){
        Block cloned = new Block(this.prevBlockHash);
        cloned.setNonce(this.nonce);
        cloned.setMerkleTreeRoot(this.merkleTreeRoot);
        cloned.setTimeStamp(this.timeStamp);
        cloned.setHash(this.hash);
        cloned.setTransactions(List.copyOf(this.transactions));
        return cloned;
    }
}
