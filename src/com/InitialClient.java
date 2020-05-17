package com;

import java.io.IOException;

public class InitialClient {
    public static void main(String[] args) throws IOException {
        int clientID = 0;
        Client client = new Client(clientID);
        client.getTransactions("ay 7aga");
        NodeSender nodeSender = new NodeSender();
        nodeSender.send("Done");
    }
}
