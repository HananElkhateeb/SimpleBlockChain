public class Controller implements IController {
    @Override
    public boolean verifyTransaction(Transaction tx) {
    	return tx.verify();
    }

    @Override
    public void mineBlock() {

    }

    @Override
    public void broadcastBlock() {

    }
}
