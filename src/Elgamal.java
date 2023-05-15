import java.math.BigInteger;
import java.security.SecureRandom;
public class Elgamal {
    private BigInteger publicKey;
    private BigInteger privateKey;
    private BigInteger g; // generator
    private BigInteger n; // Z*n -> cyclic group
    private BigInteger ordN;

    public Elgamal(BigInteger n, BigInteger g) {
        this.n = n;
        this.g = g;
        this.ordN = n.subtract(BigInteger.ONE); // assuming n is a prime
    }

    public void createKeypair() {
        SecureRandom random = new SecureRandom(); // for secure random number creation
        privateKey = new BigInteger(ordN.bitLength(), random); // publicKey == b
        publicKey = g.modPow(privateKey, ordN); // not sure about this one
    }

    public String encrypt(String text) {
        StringBuilder encryptedText = new StringBuilder();
        for(int i = 0; i < text.length(); i++) {
            BigInteger character = BigInteger.valueOf(text.charAt(i)); // casting every character from string(builder) to BigInteger
            // TODO: encrypt
        }
        return encryptedText.toString();
    }

    public String decrypt(String text) {
        StringBuilder decryptedText = new StringBuilder();
        for(int i = 0; i < text.length(); i++) {
            BigInteger character = BigInteger.valueOf(text.charAt(i)); // casting every character from string(builder) to BigInteger
            // TODO: decrypt
        }
        return decryptedText.toString();
    }

    public BigInteger getPublicKey() {
        return publicKey;
    }

    public BigInteger getPrivateKey() {
        return privateKey;
    }

    public BigInteger getG() {
        return g;
    }

    public BigInteger getN() {
        return n;
    }

    public BigInteger getOrdN() {
        return ordN;
    }
}
