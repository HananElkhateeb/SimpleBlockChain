public interface IController {
    boolean verifyTransaction(Transaction tx);
    void receiveBlock();
    void mineBlock();
    void getTransactions();
    void broadcastBlock();
}
