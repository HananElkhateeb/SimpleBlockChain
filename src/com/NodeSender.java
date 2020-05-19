package com;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
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
    public void send(String meassage, ArrayList<String> IPs , ArrayList<String> ports ) throws IOException {
        for (String port : ports) {
            for (String IP: IPs){
                try {
                    System.out.println("Sending Started");
                    // getting localhost ip
//                InetAddress local_ip = InetAddress.getByName("41.43.121.36");
//                Socket s = new Socket("41.43.121.36", 6666);

                    // establish the connection with server port 5056
//                Socket s = new Socket(local_ip, 6666);
                    InetSocketAddress socketAddress = new InetSocketAddress(IP, Integer.parseInt(port));
                    Socket s = new Socket();
                    s.connect(socketAddress);
                    // obtaining input and out streams
                    DataInputStream dis = new DataInputStream(s.getInputStream());
                    DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                    if (meassage.equals("StoreKeys")){
                        System.out.println("Storing Key");
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
                        System.out.println("Getting Key");
                        initializationFinishedState = false;
                        String[] receivedClientInfo = received.split("#&");
                        KeyFactory factory = KeyFactory.getInstance("EC");
                        int clientID = Integer.parseInt(receivedClientInfo[0]);
//                    System.out.println("*************************Pub  "+ receivedClientInfo[1]);
                        PublicKey pub = (PublicKey) factory.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(receivedClientInfo[1])));
                        networkPublicKeys.put(clientID, pub);
                    }
//                System.out.println(received);
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
        }
        initializationFinishedState = true;
    }
}
