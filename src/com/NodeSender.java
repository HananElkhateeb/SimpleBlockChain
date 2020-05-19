package com;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

// com.Client class
public class NodeSender {
    public HashMap<Integer, PublicKey> networkPublicKeys = new HashMap<>();
    public boolean initializationFinishedState = false;
//TODO: send message parameter or it is just received.
    public void send(String meassage, ArrayList<String> ips ) throws IOException {
        for (String ip : ips) {
            try {
                System.out.println("Sending Started");
                // getting localhost ip
                InetAddress local_ip = InetAddress.getByName("localhost");

                // establish the connection with server port 5056
                Socket s = new Socket(local_ip, Integer.parseInt(ip));

                // obtaining input and out streams
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                if (meassage.equals("StoreKeys")){
                    StringBuilder networkPublicKeysStr = new StringBuilder();
                    for (Map.Entry<Integer, PublicKey> entry : networkPublicKeys.entrySet()){
                        networkPublicKeysStr.append(entry.getKey());
                        networkPublicKeysStr.append("#&");
                        networkPublicKeysStr.append(Base64.getEncoder().encodeToString(entry.getValue().getEncoded()));
                        networkPublicKeysStr.append("!");
                    }
                    meassage = networkPublicKeysStr.toString();
                }

                // the following loop performs the exchange of
                // information between client and client handler
//                System.out.println(dis.readUTF());
                String tosend = meassage;
                dos.writeUTF(meassage);

                // printing date or time as requested by client
                String received = dis.readUTF();
                if (tosend.equals("GetKey") && received!= null && !received.isEmpty()){
                    initializationFinishedState = false;
                    String[] receivedClientInfo = received.split("#&");
                    System.out.println("Recieved info size : " + receivedClientInfo.length);
                    KeyFactory factory = KeyFactory.getInstance("EC");
                    int clientID = Integer.parseInt(receivedClientInfo[0]);
                    System.out.println("*************************Pub  "+ receivedClientInfo[1]);
                    PublicKey pub = (PublicKey) factory.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(receivedClientInfo[1])));
                    networkPublicKeys.put(clientID, pub);
                }
                System.out.println(received);
                System.out.println("Closing this connection : " + s);
                s.close();
                System.out.println("Connection closed");

                // closing com.resources
                dis.close();
                dos.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        initializationFinishedState = true;
    }
}
