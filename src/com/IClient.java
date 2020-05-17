package com;

import java.io.IOException;
import java.security.*;

public interface IClient {
    void getTransactions(String fileName);
    void generateKeys();
    void broadcastTransaction(Transaction transaction) throws IOException;
    boolean addNewNode(Integer id, PublicKey publicKey);

    PublicKey getPublicKey();
    PrivateKey getPrivateKey();
}
