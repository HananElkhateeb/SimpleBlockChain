package com;

import java.security.*;

public interface IClient {
    void getTransactions(String fileName);
    void generateKeys();
    void broadcastTransaction();
    boolean addNewNode(String id, PublicKey publicKey);

    PublicKey getPublicKey();
    PrivateKey getPrivateKey();
}
