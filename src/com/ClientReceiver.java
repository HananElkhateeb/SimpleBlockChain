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
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// Network Server class
public class ClientReceiver {
    public static void main(String[] args) throws IOException {
        // server is listening on this port
        ServerSocket ss = new ServerSocket(6666);

        int clientID = 1;
        Client client = new Client(clientID);
        client.generateKeys();

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
                Thread t = new ClientReceiverHandler(s, dis, dos, client);

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
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;
    final Client client;

    // Constructor
    public ClientReceiverHandler(Socket s, DataInputStream dis, DataOutputStream dos, Client client ) {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
        this.client = client;
    }

    @Override
    public void run() {
        String received;
        String toreturn;
        while (true) {
            try {

                // receive the answer from client
                received = dis.readUTF();

                if (received.equals("Done")){
                    client.getTransactions("/home/karim/IdeaProjects/SimpleBlockChain/src/com/resources/txdataset_v7.3.txt");
                    dos.writeUTF("OK");
//                } else {
//                    dos.writeUTF("Client "+ clientID+ "Failed to get transactions");
                } else if (received.equals("GetKey")){
                    //System.out.println("kkkkkkkkkkkkkk> "+client.getPublicKey());
                    toreturn = ""+client.getClientID()+"#&"+Base64.getEncoder().encodeToString(client.getPublicKey().getEncoded());
                    dos.writeUTF(toreturn);
                } else {
                    Map<Integer, PublicKey> nodes = new HashMap<>();
                    String[] clientsInfo = received.split("!");
                    for(String clientInfoStr: clientsInfo){
                        String[] clientInfo = clientInfoStr.split("#&");
                        KeyFactory factory = KeyFactory.getInstance("EC");
                        int id = Integer.parseInt(clientInfo[0]);
                       // System.out.println("+++++++++++++++Pub "+ clientInfo[1]);
                        PublicKey pub = (PublicKey) factory.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(clientInfo[1])));
                        nodes.put(id,pub);
                        client.setNodes(nodes);
                    }
                    dos.writeUTF("OK");
                }
                this.s.close();
                System.out.println("Connection closed");
                break;
            } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
                e.printStackTrace();
            }
        }

        try {
            // closing com.resources
            this.dis.close();
            this.dos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
