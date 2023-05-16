import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

public class Main {
    public static void main(String[] args) {
        // 1:
        String hexNum = "FFFFFFFFFFFFFFFFC90FDAA22168C234C4C6628B80DC1CD1" +
                "29024E088A67CC74020BBEA63B139B22514A08798E3404DD" +
                "EF9519B3CD3A431B302B0A6DF25F14374FE1356D6D51C245" +
                "E485B576625E7EC6F44C42E9A637ED6B0BFF5CB6F406B7ED" +
                "EE386BFB5A899FA5AE9F24117C4B1FE649286651ECE45B3D" +
                "C2007CB8A163BF0598DA48361C55D39A69163FA8FD24CF5F" +
                "83655D23DCA3AD961C62F356208552BB9ED529077096966D" +
                "670C354E4ABC9804F1746C08CA18217C32905E462E36CE3B" +
                "E39E772C180E86039B2783A2EC07A28FB5C55DF06F4C52C9" +
                "DE2BCBF6955817183995497CEA956AE515D2261898FA0510" +
                "15728E5A8AACAA68FFFFFFFFFFFFFFFF";
        Elgamal em = new Elgamal(new BigInteger(hexNum, 16), BigInteger.TWO);
        em.createKeypair();
        // 2:
        createFile("pk.txt", em.getPublicKey().toString().getBytes());
        createFile("sk.txt", em.getPrivateKey().toString().getBytes());

        // 3:
        String text = new String(readFileContent("text.txt"));
        BigInteger pk = new BigInteger(new String(readFileContent("pk.txt")));
        em.setPublicKey(pk); // same as before
        createFile("chiffre_test.txt", em.encrypt(text).getBytes());
        // 4:
        String chiffre = String.valueOf(readFileContent("chiffre_test.txt"));
        createFile("text-d.txt", em.decrypt(chiffre).getBytes());
    }

    public static void createFile(String fileName, byte[] content) {
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            fos.write(content);
        } catch(IOException exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    public static char[] readFileContent(String fileName) {
        try {
            File file = new File(fileName);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s = "";
            String line;
            while ((line = br.readLine()) != null) {
                s += line;
                if(br.ready()) s += "\n";
            }
            return s.toCharArray();
        }
        catch(IOException exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }
}