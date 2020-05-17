package com;

import com.parsing.Parser;
import com.parsing.messages.Message;
import com.parsing.messages.MessagesTypes;
import com.parsing.messages.payloads.types.TransactionPayload;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

// Network Server class
public class ClientReceiver {
    public static void main(String[] args) throws IOException {
        // server is listening on this port
        ServerSocket ss = new ServerSocket(6060);

        // running infinite loop for getting
        // client request
        while (true) {
            Socket s = null;

            try {
                // socket object to receive incoming client requests
                s = ss.accept();

                System.out.println("A new client is connected : " + s);

                // obtaining input and out streams
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                System.out.println("Assigning new thread for this client");

                // create a new thread object
                Thread t = new ClientReceiverHandler(s, dis, dos);

                // Invoking the start() method
                t.start();

            } catch (Exception e) {
                s.close();
                e.printStackTrace();
            }
        }
    }
}

// Network ClientHandler class
class ClientReceiverHandler extends Thread {
    DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd");
    DateFormat fortime = new SimpleDateFormat("hh:mm:ss");
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;


    // Constructor
    public ClientReceiverHandler(Socket s, DataInputStream dis, DataOutputStream dos) {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

    @Override
    public void run() {
        String received;
        String toreturn;
        Client client = new Client();
        while (true) {
            try {

                client.getTransactions("ay 7aga");

                // receive the answer from client
                received = dis.readUTF();

                Message receivedMsg = new Parser().deSerializeMessage(received);

                if (receivedMsg.getMessageType().equals(MessagesTypes.TRANSACTION_MESSAGE.toString())){
                    TransactionPayload transactionPayload = (TransactionPayload) receivedMsg.getMessagePayload();

                    Transaction t = new Transaction();
                    t.setInput(transactionPayload.getInput());
                    t.setOutputs(transactionPayload.getOutputs());
                    t.setTransactionID(transactionPayload.getTransactionID());
                    t.setHash(transactionPayload.getHash());
                    t.setInitialTransaction(transactionPayload.isInitialTransaction());
                    t.setSignature(transactionPayload.getSignature());

                    //TODO: ID/Port/PublicKey/privateKey hardcoded??
                    //TODO:Node0 configuration

                } else {
                    dos.writeUTF("Invalid Message Type!");
                }

                System.out.println("com.Client " + this.s + " sends exit...");
                System.out.println("Closing this connection.");
                this.s.close();
                System.out.println("Connection closed");
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            // closing resources
            this.dis.close();
            this.dos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
