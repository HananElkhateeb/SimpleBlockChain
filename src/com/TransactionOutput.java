package com;

import java.security.PublicKey;

public class TransactionOutput {
    private int outputIndex;
    private float value;
    private String reciever;

    public int getOutputIndex() {
    	
        return outputIndex;
    }

    public void setOutputIndex(int outputIndex) {
        this.outputIndex = outputIndex;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }
}
