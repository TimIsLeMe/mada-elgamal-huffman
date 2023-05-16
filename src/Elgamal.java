import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.regex.Pattern;

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
        SecureRandom secureRandom = new SecureRandom(); // for secure random number creation
        privateKey = new BigInteger(ordN.bitLength(), secureRandom); // publicKey == b
        publicKey =  g.modPow(privateKey, ordN); // create public key using the private key
    }

    public BigInteger generateTempPublicKey() {
        SecureRandom secureRandom = new SecureRandom(); // for secure random number creation
        return new BigInteger(ordN.bitLength(), secureRandom);
    }

    public String encrypt(String text) {
        StringBuilder encryptedText = new StringBuilder();

        for(int i = 0; i < text.length(); i++) {
            BigInteger m = BigInteger.valueOf(text.charAt(i)); // casting every character from char to BigInteger
            // encryption
            BigInteger r = generateTempPublicKey(); // temporary public key for encryption
            BigInteger y1 = g.modPow(r, n);
            BigInteger y2 = m.multiply(publicKey.modPow(r, n)).mod(n);
            encryptedText.append("(").append(y1).append(", ").append(y2).append("); ");
        }
        return encryptedText.toString();
    }

    public String decrypt(String chiffre)  {
        StringBuilder decryptedText = new StringBuilder();
        String[] chiffreSplit = chiffre.replaceAll("\\(|\\)|\\s", "").split(";"); // remove brackets and whitespaces also split by comma
        for(int i = 0; i < chiffreSplit.length; i++) {
            String[] chiffreArgs = chiffreSplit[i].split(","); // from "y1,y2" to an array
            if(chiffreArgs.length == 2) { // skip if it does not have both pairs: y1 and y2
                BigInteger y1 = new BigInteger(chiffreArgs[0]); //
                BigInteger y2 = new BigInteger(chiffreArgs[1]); //
                // decryption
                BigInteger s = y1.modPow(privateKey, n);
                BigInteger sInv = s.modInverse(n);
                BigInteger m = y2.multiply(sInv).mod(n);
                try {
                    decryptedText.append(new String(m.toByteArray(), "UTF-8"));
                } catch (UnsupportedEncodingException exception) {
                    throw new RuntimeException(exception);
                }
            }
        }
        return decryptedText.toString();
    }

    public BigInteger getPublicKey() {
        return publicKey;
    }

    public BigInteger getPrivateKey() {
        return privateKey;
    }

    public void setPublicKey(BigInteger publicKey) {
        this.publicKey = publicKey;
    }

    public void setPrivateKey(BigInteger privateKey) {
        this.privateKey = privateKey;
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
