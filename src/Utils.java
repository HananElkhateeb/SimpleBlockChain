import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.ArrayList;
import java.util.List;

public class Utils {
	/*
	 * Applying sha256 encryption to a string input
	 * Used in :
	 * 		Transaction Hash
	 * 		Block Hash
	 *
	 * @param input -> string to apply sha256 on
	 * @return hex value of the sha256 encryption
	 * */
	public static String sha256(String input){
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(input.getBytes("UTF-8"));

			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if(hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	/*
	 * generate digital signature for data using private key of sender
	 * @param privateKey -> private key of sender
	 * @param data -> input data
	 * @return the digital signature for the sender
	 * */
	public static byte[] digialSignature(PrivateKey privateKey, String data) {
		Signature dsa;
		byte[] output = new byte[0];
		try {
			//ecdsa signature is used with bitcoin transactions
			dsa = Signature.getInstance("ECDSA", "BC");
			dsa.initSign(privateKey);
			byte[] strByte = data.getBytes();
			dsa.update(strByte);
			byte[] digitSign = dsa.sign();
			output = digitSign;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return output;
	}
	/*
	 * verify digital signature for data using public key of sender
	 * @param publicKey -> public key of sender
	 * @param data -> input data
	 * @param signature => digital signature to be verified
	 * @return true if valid, false o.w.
	 * */
	public static boolean verifyDigitalSign(PublicKey publicKey, String data, byte[] signature) {
		try {
			Signature dsVerify = Signature.getInstance("ECDSA", "BC");
			dsVerify.initVerify(publicKey);
			dsVerify.update(data.getBytes());
			return dsVerify.verify(signature);
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String calculateMerkleTreeRoot(List<Transaction> transactions) {
		int treeSize = transactions.size();
		ArrayList<String> prevTreeLayer = new ArrayList<>();

		for(Transaction transaction: transactions){
			prevTreeLayer.add(transaction.calculateHash());
		}
		ArrayList<String> treeLayer = prevTreeLayer;

		while (treeSize > 1) {
			treeLayer = new ArrayList<>();
			for (int i = 1; i < prevTreeLayer.size(); i+=2) {
				treeLayer.add(sha256(prevTreeLayer.get(i-1) + prevTreeLayer.get(i)));
			}
			treeSize = treeLayer.size();
			prevTreeLayer = treeLayer;
		}

		return (treeLayer.size() == 1) ? treeLayer.get(0) : "";
	}
}
