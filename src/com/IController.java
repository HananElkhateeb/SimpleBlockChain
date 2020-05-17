package com;

import java.io.IOException;

public interface IController {
    boolean verifyTransaction(Transaction tx);
    void receiveBlock(Block block);
    void mineBlock() throws IOException;
    void getReceivedTransactions(Transaction transaction);
    void broadcastBlock() throws IOException;
}
