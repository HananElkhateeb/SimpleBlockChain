package com;

import java.io.IOException;

// Network Server class
public class InitialClient {
    public static void main(String[] args) throws IOException {
        NodeSender nodeSender = new NodeSender();
        nodeSender.send("GetKey");
        while (!nodeSender.initializationFinishedState);
        nodeSender.send("StoreKeys");
        int clientID = 0;
        Client client = new Client(clientID);
        client.setNodes(nodeSender.networkPublicKeys);
        client.generateKeys();
        client.getTransactions("/home/karim/IdeaProjects/SimpleBlockChain/src/com/resources/txdataset_v2.txt");


    }
}
