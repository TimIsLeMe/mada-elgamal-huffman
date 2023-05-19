import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.format.SignStyle;
import java.util.Iterator;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

//        Elgamal();
        Huffman();
    }

    public static void Elgamal() {
        String filePath = "elgamal_files/";
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
        createFile(filePath + "pk.txt", em.getPublicKey().toString().getBytes());
        createFile(filePath + "sk.txt", em.getPrivateKey().toString().getBytes());
        // 3:
        String text = new String(readFileContent(filePath + "text.txt"));
        BigInteger pk = new BigInteger(new String(readFileContent(filePath + "pk.txt")));
        em.setPublicKey(pk); // same as before
        createFile(filePath + "chiffre.txt", em.encrypt(text).getBytes()); // encrypt and write to file
        // 4:
        String chiffre = String.valueOf(readFileContent(filePath + "chiffre.txt"));
        createFile(filePath + "text-d.txt", em.decrypt(chiffre).getBytes()); // decrypt and write to file
        // 5
        Elgamal gegebenEm = new Elgamal(new BigInteger(hexNum, 16), BigInteger.TWO);
        BigInteger privateKeyGegeben = new BigInteger(new String(readFileContent(filePath + "sk_gegeben.txt")));
        gegebenEm.setPrivateKey(privateKeyGegeben);
        gegebenEm.generatePublicKey();
        String chiffreGegeben = String.valueOf(readFileContent(filePath + "chiffre_gegeben.txt"));
        createFile(filePath + "text-d-5.txt", gegebenEm.decrypt(chiffreGegeben).getBytes()); // decrypt and write to file
    }

    public static void Huffman() {
        String filePath = "huffman_files/";
        // 1:
        String text = new String(readFileContent(filePath + "text1.txt"));
        Huffman huff = new Huffman(text);
        // 2:
        huff.setStrCharFreq();
        // 3:
        huff.createTree();
        huff.createCode();
        // 4:
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<Integer, String>> iterator = huff.huffmanCode.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry<Integer, String> entry = iterator.next();
            sb.append(entry.getKey()).append(":").append(entry.getValue());
            if(iterator.hasNext()) sb.append("-");
        }
        createFile(filePath + "dec_tab.txt", sb.toString().getBytes());
        // 5 - 8:
        createFile(filePath + "output.dat", Huffman.convertBitStringToByteArray(huff.compress(text)));
        // 9 - 10:
        String content = Huffman.convertByteArrayToBitString(readFileBytes(filePath + "output.dat"));
        System.out.println(content);
        System.out.println(huff.decompress(content));
        createFile(filePath + "decompress.txt", huff.decompress(content).getBytes());
        // 11:

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

    public static byte[] readFileBytes(String fileName) {
        try {
            File file = new File(fileName);
            byte[] bFile = new byte[(int) file.length()];
            FileInputStream fis = new FileInputStream(file);
            fis.read(bFile);
            fis.close();
            return bFile;
        }
        catch(IOException exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }
}