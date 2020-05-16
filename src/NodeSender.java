import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

// Client class
public class NodeSender {
    static ArrayList<String> ips = new ArrayList<>(Arrays.asList("5056", "5050"));

    public void send() throws IOException {
        for (String ip : ips) {
            try {
                // getting localhost ip
                InetAddress local_ip = InetAddress.getByName("localhost");

                // establish the connection with server port 5056
                Socket s = new Socket(local_ip, Integer.parseInt(ip));

                // obtaining input and out streams
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                // the following loop performs the exchange of
                // information between client and client handler
                System.out.println(dis.readUTF());
                String tosend = "Time";
                dos.writeUTF(tosend);

                // printing date or time as requested by client
                String received = dis.readUTF();
                System.out.println(received);
                System.out.println("Closing this connection : " + s);
                s.close();
                System.out.println("Connection closed");

                // closing resources
                dis.close();
                dos.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
