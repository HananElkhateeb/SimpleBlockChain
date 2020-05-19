package com.parsing.messages.payloads.types;

import com.Transaction;
import com.parsing.messages.payloads.IPayload;
import com.parsing.messages.payloads.Payload;

import java.util.List;
import java.util.Set;

public class BlockPayload extends Payload implements IPayload {

    private String prevBlockHash = "";
    private String merkleTreeRoot = "";
    private long timeStamp;
    private String hash = "";
    private List<Transaction> transactions;
    private Set<String> spentcoins;
    private int nonce;
    public BlockPayload() {}


    public String getPrevBlockHash() {
        return prevBlockHash;
    }

    public void setPrevBlockHash(String prevBlockHash) {
        this.prevBlockHash = prevBlockHash;
    }

    public String getMerkleTreeRoot() {
        return merkleTreeRoot;
    }

    public void setMerkleTreeRoot(String merkleTreeRoot) {
        this.merkleTreeRoot = merkleTreeRoot;
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

    public Set<String> getSpentcoins() {
        return spentcoins;
    }

    public void setSpentcoins(Set<String> spentcoins) {
        this.spentcoins = spentcoins;
    }

    public int getNonce() {
        return nonce;
    }

    public void setNonce(int nonce) {
        this.nonce = nonce;
    }
}
