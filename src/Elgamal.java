import javax.swing.*;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
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
    private BigInteger ordNm1; //

    public Elgamal(BigInteger n, BigInteger g) {
        this.n = n;
        this.g = g;
        this.ordNm1 = n.subtract(BigInteger.ONE); // assuming n is a prime
    }

    public void createKeypair() {
        SecureRandom secureRandom = new SecureRandom(); // for secure random number creation
        privateKey = new BigInteger(ordNm1.bitLength(), secureRandom); // publicKey == b
        generatePublicKey();
    }
    public void generatePublicKey() {
        publicKey = g.modPow(privateKey, n); // create public key using the private key
    }

    public BigInteger generateTempPublicKey() {
        SecureRandom secureRandom = new SecureRandom(); // for secure random number creation
        return new BigInteger(n.bitLength(), secureRandom); // generate a: between 0 - ord(n)
    }

    public String encrypt(String text) {
        StringBuilder encryptedText = new StringBuilder();

        for(int i = 0; i < text.length(); i++) {
            BigInteger m = BigInteger.valueOf(text.charAt(i)); // casting every character from char to BigInteger
            // encryption
            BigInteger a = generateTempPublicKey(); // temporary public key for encryption
            BigInteger y1 = g.modPow(a, n); // g^a
            BigInteger y2 = m.multiply(publicKey.modPow(a, n)); // y2 == (g^b)^a o x; --> o == multiply; x == m
            encryptedText.append("(").append(y1).append(", ").append(y2).append(");");
        }
        return encryptedText.toString();
    }

    public String decrypt(String chiffre)  {
        StringBuilder decryptedText = new StringBuilder();
        String[] chiffreSplit = chiffre.replaceAll("\\(|\\)|\\s", "").split(";"); // remove brackets and whitespaces also split by comma
        for(int i = 0; i < chiffreSplit.length; i++) {
            String[] chiffreArgs = chiffreSplit[i].split(","); // from String array of "y1,y2" to a nested array with ["y1", "y2"]
            if(chiffreArgs.length == 2) { // skip if it does not have both pairs: y1 and y2
                BigInteger y1 = new BigInteger(chiffreArgs[0]); //
                BigInteger y2 = new BigInteger(chiffreArgs[1]); //
                // decryption
                // for reversal we need n instead of ordN
                BigInteger y1Inverse = y1.modPow(privateKey, n).modInverse(n); // (y1^b)^-1
                BigInteger m = y2.multiply(y1Inverse).mod(n); // y2 o y1Inverse (o == multiply)
                decryptedText.append(new String(m.toByteArray(), StandardCharsets.UTF_8));
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
        if(privateKey.compareTo(BigInteger.ZERO) < 0 || privateKey.compareTo(ordNm1) > 0)
            throw new RuntimeException("private key has to be between 0 and ordN");
        this.privateKey = privateKey;
    }
}
