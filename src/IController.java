public interface IController {
    boolean verifyTransaction(Transaction tx);
    void mineBlock();
    void broadcastBlock();
}
