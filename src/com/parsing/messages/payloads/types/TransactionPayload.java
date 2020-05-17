package com.parsing.messages.payloads.types;

import com.TransactionInput;
import com.TransactionOutput;
import com.parsing.messages.payloads.IPayload;
import com.parsing.messages.payloads.Payload;

import java.util.List;

public class TransactionPayload extends Payload implements IPayload {
    private long transactionID;
    private TransactionInput input;
    private List<TransactionOutput> outputs;
    private String hash;
    public byte[] signature;
    boolean initialTransaction;

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
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public boolean isInitialTransaction() {
        return initialTransaction;
    }

    public void setInitialTransaction(boolean initialTransaction) {
        this.initialTransaction = initialTransaction;
    }
}
