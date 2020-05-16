import java.awt.*;
import java.util.*;
import java.util.List;

public class Controller implements IController {

    private BlockChain blockChain;
    private List<Transaction> receivedTransactions;
    private Block currentBlock;
    private int difficulty = 7;
    private int type = 1;
    private Set<String> coins;
    private Thread miningThread;

    public Controller(){
        blockChain = new BlockChain(blockChain.getGenesisBlock());
        receivedTransactions = new ArrayList<>();
        coins = new HashSet<>();
        initiateThread();
    }

    @Override
    public boolean verifyTransaction(Transaction tx) {
        return tx.verify() && !checkDoubleSpends(tx);
    }

    @Override
    public void receiveBlock() {
        //TODO: handle received blocks from network and parse it to Block object.
        //TODO: create threads for mining and interrupt the other miners when block is found.
        Block block = null;
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
            while (true)
                mineBlock();
        });
        miningThread.start();
    }

    @Override
    public void mineBlock() {
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
    public void getReceivedTransactions() {
        //TODO: received transactions from client and deserialized it to Transaction
        Transaction transaction = null;
        if (verifyTransaction(transaction)){
            receivedTransactions.add(transaction);
        }
    }

    @Override
    public void broadcastBlock() {
        //TODO: .......
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
    //TODO: handling the 50 initial transactions
    private boolean checkDoubleSpends (Transaction transaction){
        TransactionInput input = transaction.getInput();
        String coin = input.getPrevTX() + " " + input.getPrevOutputIndex();
        if (!coins.contains(coin))
            return true;
        return false;
    }

    private Transaction getTransaction(long txid) {
        Block block = blockChain.traverseChain(txid).block;
        if (block != null)
            return block.getTransaction(txid+"");
        return null;
    }
}
