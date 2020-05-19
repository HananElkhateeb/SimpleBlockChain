package com;

import java.security.PublicKey;

public class TransactionInput {
    private long prevTX;
    private short prevOutputIndex;
    private String sender;
    private int input;

    public String getSender() {
    	
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
    public long getPrevTX() {
        return prevTX;
    }

    public void setPrevTX(long prevTX) {
        this.prevTX = prevTX;
    }

    public short getPrevOutputIndex() {
        return prevOutputIndex;
    }

    public void setPrevOutputIndex(short prevOutputIndex) {
        this.prevOutputIndex = prevOutputIndex;
    }


    public int getInput() {
        return input;
    }

    public void setInput(int input) {
        this.input = input;
    }
}
