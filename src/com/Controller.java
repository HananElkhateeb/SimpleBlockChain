package com;

import com.parsing.Parser;
import com.parsing.messages.Message;
import com.parsing.messages.payloads.PayloadFactory;
import com.parsing.messages.payloads.types.BlockPayload;
import com.parsing.messages.payloads.types.PayloadTypes;
import com.parsing.messages.MessagesTypes;

import java.io.IOException;
import java.util.*;
import java.util.List;

public class Controller implements IController {

    private BlockChain blockChain;
    private List<Transaction> receivedTransactions;
    private Block currentBlock;
    private int difficulty = 7;
    private int type = 1;
    private Set<String> coins;//hash set that has prev transaction with its output indes, can be spent
    private Thread miningThread;

    public Controller(){
        blockChain = new BlockChain(blockChain.getGenesisBlock());
        receivedTransactions = new ArrayList<>();
        coins = new HashSet<>();
        initiateThread();
    }

    @Override
    public boolean verifyTransaction(Transaction tx) {
        return tx.verify(this) && !checkDoubleSpends(tx);
    }

    @Override
    public void receiveBlock(Block b) {
        Block block = b;
        if(!block.verifyHash() && !blockChain.addBlock(block))
            return;
        miningThread.interrupt();
        handleCoins(block);
        List<Transaction> transactions = block.getTransactions();
        for (Transaction transaction:transactions){
            if (currentBlock.getTransactions().contains(transaction))
                currentBlock.getTransactions().remove(transaction);
        }
        initiateThread();

    }

    private void initiateThread (){
        miningThread = new Thread(() -> {
            while (true) {
                try {
                    mineBlock();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        miningThread.start();
    }

    @Override
    public void mineBlock() throws IOException {
        if (currentBlock == null)
            currentBlock = new Block(blockChain.getChainHead().block.getHash());
        List<Transaction> pendingTransactions = new ArrayList<>();
        int txCount = 0;
        while ((new Date().getTime() - currentBlock.getTimeStamp() < 7000) && (txCount < currentBlock.getBlockThreshold())){
            if (!receivedTransactions.isEmpty()) {
                pendingTransactions.add(receivedTransactions.remove(0));
                txCount++;
            } else {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        currentBlock.setTransactions(pendingTransactions);
        currentBlock.setMerkleTreeRoot(currentBlock.calculateMerkleTreeRoot());
        currentBlock.setHash(currentBlock.calculateBlockHash());
        currentBlock.solveBlock(type, difficulty);
        handleCoins(currentBlock);
        broadcastBlock();
    }

    @Override
    public void getReceivedTransactions(Transaction t) {
        Transaction transaction = t;
        if(t.isInitialTransaction()){
            receivedTransactions.add(transaction);
        } else {
            if (verifyTransaction(transaction)){
                receivedTransactions.add(transaction);
            }
        }
    }

    @Override
    public void broadcastBlock() throws IOException {
        NodeSender nodeSender = new NodeSender();
        Message blockMessage = new Message();
        PayloadFactory payloadFactory = new PayloadFactory();
        BlockPayload blockPayload = (BlockPayload) payloadFactory.getPayload(PayloadTypes.BLOCK_PAYLOAD);

        blockPayload.setHash(currentBlock.getHash());
        blockPayload.setMerkleTreeRoot(currentBlock.getMerkleTreeRoot());
        blockPayload.setTimeStamp(currentBlock.getTimeStamp());
        blockPayload.setPrevBlockHash(currentBlock.getPrevBlockHash());
        blockPayload.setTransactions(currentBlock.getTransactions());
        blockMessage.setMessagePayload(blockPayload);

        blockMessage.setMessageType(MessagesTypes.BLOCK_MESSAGE.toString());
        Parser parser = new Parser();
        String message = parser.serializeMessage(blockMessage);
        nodeSender.send(message);
        currentBlock = null;
    }

    private void handleCoins (Block block){
        List<Transaction> transactions = block.getTransactions();
        for (Transaction transaction: transactions) {
            TransactionInput input = transaction.getInput();
            List<TransactionOutput> outputs = transaction.getOutputs();

            String coin = input.getPrevTX() + " " + input.getPrevOutputIndex();
            if (coins.contains(coin))
                coins.remove(coin);

            for (TransactionOutput output: outputs){
                String addedCoin = transaction.getTransactionID() + " " + output.getOutputIndex();
                coins.add(addedCoin);
            }
        }
    }

    private boolean checkDoubleSpends (Transaction transaction){
        TransactionInput input = transaction.getInput();
        String coin = input.getPrevTX() + " " + input.getPrevOutputIndex();
        if (!coins.contains(coin))
            return true;
        return false;
    }

    public Transaction getTransaction(long txid) {
        Block block = blockChain.traverseChain(txid).block;
        if (block != null)
            return block.getTransaction(txid+"");
        return null;
    }
}
