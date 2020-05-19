package com;

import com.parsing.Parser;
import com.parsing.messages.IMessage;
import com.parsing.messages.Message;
import com.parsing.messages.payloads.types.BlockPayload;
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
public class MinerReceiver {
    public static void main(String[] args) throws IOException {

        Controller controller = new Controller(); //moved from while loop
        Thread t;
        long[] currDate = new long[1];
        // server is listening this port
        ServerSocket ss = new ServerSocket(5051);

        Thread watcher = new MineHandler(controller, currDate);
        watcher.start();
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
                 t = new MinerReceiverHandler(s, dis, dos, controller, currDate);

                // Invoking the start() method
                t.start();

            } catch (Exception e) {
                s.close();
                e.printStackTrace();
            }
        }
    }
}

class MineHandler extends Thread{
    Controller controller;
    long[] currDate;
    public MineHandler(Controller controller, long[] currDate){
        this.controller = controller;
        this.currDate = currDate;
    }
    @Override
    public void run(){
        while (true){
            if (new Date().getTime()-currDate[0] > 3000 && !controller.receivedTransactions.isEmpty()){
                try {
                    controller.mineBlock();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
// Network ClientHandler class
class MinerReceiverHandler extends Thread {
    DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd");
    DateFormat fortime = new SimpleDateFormat("hh:mm:ss");
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;
    Controller controller;
    long[] currDate;

    // Constructor
    public MinerReceiverHandler(Socket s, DataInputStream dis, DataOutputStream dos, Controller controller, long[] currDate) {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
        this.controller = controller;
        this.currDate = currDate;
    }

    @Override
    public void run() {
        String received;
        String toreturn;
        while (true) {
            try {

                // receive the answer from client
                received = dis.readUTF();

                System.out.println("Miner received: " + received);

                // write on output stream based on the
                // answer from the client
                if (!received.equals("Done")){
                    Message receivedMsg = new Parser().deSerializeMessage(received);
                    if (receivedMsg.getMessageType().equals(MessagesTypes.BLOCK_MESSAGE.toString())){
                        BlockPayload blockPayload = (BlockPayload) receivedMsg.getMessagePayload();

                        Block b = new Block(blockPayload.getPrevBlockHash());
                        b.setHash(blockPayload.getHash());
                        b.setMerkleTreeRoot(blockPayload.getMerkleTreeRoot());
                        b.setTimeStamp(blockPayload.getTimeStamp());
                        b.setPrevBlockHash(blockPayload.getPrevBlockHash());
                        b.setTransactions(blockPayload.getTransactions());
                        b.setSpentcoins(blockPayload.getSpentcoins());
                        controller.receiveBlock(b);
                        currDate[0] = new Date().getTime();
                    } else if (receivedMsg.getMessageType().equals(MessagesTypes.TRANSACTION_MESSAGE.toString())){
                        TransactionPayload transactionPayload = (TransactionPayload) receivedMsg.getMessagePayload();

                        Transaction t = new Transaction();
                        t.setInput(transactionPayload.getInput());
                        t.setOutputs(transactionPayload.getOutputs());
                        t.setTransactionID(transactionPayload.getTransactionID());
                        t.setHash(transactionPayload.getHash());
                        t.setInitialTransaction(transactionPayload.isInitialTransaction());
                        t.setSignature(transactionPayload.getSignature());

                        controller.getReceivedTransactions(t);
                        if (controller.receivedTransactions.size() >= 4){
                            controller.mineBlock();
                            currDate[0] = new Date().getTime();
                        }

                    } else {
                        dos.writeUTF("Invalid Message Type!");
                    }
                    dos.writeUTF("OK");
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
            // closing com.resources
            this.dis.close();
            this.dos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
