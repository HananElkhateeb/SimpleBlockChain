package com;

import java.io.IOException;

// Network Server class
public class InitialClient {
    public static void main(String[] args) throws IOException {
        NodeSender nodeSender = new NodeSender();
        nodeSender.send("GetKey", IPsDTO.clientsIPs);
        while (!nodeSender.initializationFinishedState);
        nodeSender.send("StoreKeys", IPsDTO.clientsIPs);
        int clientID = 0;
        Client client = new Client(clientID);
        client.setNodes(nodeSender.networkPublicKeys);
        client.generateKeys();
        client.getTransactions("/home/karim/IdeaProjects/SimpleBlockChain/src/com/resources/txdataset_v7.3.txt");

        //ToDo send Done to clients
        new NodeSender().send("Done", IPsDTO.clientsIPs);

    }
}
