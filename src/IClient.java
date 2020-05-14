public interface IClient {
    void getTransactions(String fileName);
    void generateKeys();
    void broadcastTransaction();
}
