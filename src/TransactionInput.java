import java.security.PublicKey;

public class TransactionInput {
    private long prevTX;
    private short prevOutputIndex;
    public PublicKey sender;

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

    public PublicKey getSender() {
        return sender;
    }

    public void setSender(PublicKey sender) {
        this.sender = sender;
    }
}
