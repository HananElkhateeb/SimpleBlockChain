public interface IController {
    boolean verifyTransaction(Transaction tx);
    void receiveBlock();
    void mineBlock();
    void getReceivedTransactions();
    void broadcastBlock();
}
