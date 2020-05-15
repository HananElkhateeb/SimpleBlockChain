import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.Map;

public class Client implements IClient {
	private PublicKey publicKey;
	private PrivateKey privateKey;
	private Map<String, PublicKey> nodes;

    @Override
    public void getTransactions(String fileName) {

    }

    @Override
    public void generateKeys() {
    	try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");

			keyGen.initialize(ecSpec, random);   //256 bytes provides an acceptable security level
			KeyPair keyPair = keyGen.generateKeyPair();

			// set the public and private keys from the keyPair 	
			privateKey = keyPair.getPrivate();    	
			publicKey = keyPair.getPublic();
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
    }

    @Override
    public void broadcastTransaction() {

    }

	@Override
	public PublicKey getPublicKey() {
		return publicKey;
	}

	@Override
	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	@Override
	public boolean addNewNode(String id, PublicKey publicKey) {
		if(nodes.containsKey(id)) {
			if(nodes.get(id) == publicKey)
				return false;
			nodes.replace(id, publicKey);
		}
		nodes.put(id, publicKey);
		return true;
	}
}
