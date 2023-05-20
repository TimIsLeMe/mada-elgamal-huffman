import com.sun.jdi.request.StepRequest;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.util.*;
import java.util.concurrent.SynchronousQueue;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

public class Huffman {
    private final int[] asciiArray = new int[128];
    private String originalStr;
    Map<TreeNode, Pair<TreeNode, TreeNode>> treeMap = new TreeMap<>(Collections.reverseOrder());
    Map<Integer, String> huffmanCode;

    public Huffman(String originalStr) {
        this.originalStr = originalStr;
    }
    public Huffman() {
    }
    public void init() {
        setStrCharFreq();
        createTree();
        createCode();
    }

    public void setStrCharFreq() {
        setCharFrequency(originalStr.toCharArray(), asciiArray);
    }

    public void setCharFrequency(char[] characters, int[] arr) {
        for (int i = 0; i < characters.length; i++) {
            arr[characters[i]]++;
        }
    }

    public void createTree() {
        Queue<TreeNode> treeQueue = new PriorityQueue<>();
        treeMap = new TreeMap<>(Collections.reverseOrder());
        for (int i = 0; i < asciiArray.length; i++) {
            if (asciiArray[i] != 0) {
                TreeNode tn = new TreeNode(i, asciiArray[i]);
                treeQueue.add(tn);
                treeMap.put(tn, new Pair<>());
            }
        }
        while (treeQueue.size() > 1) {
            TreeNode nodeA = treeQueue.poll();
            TreeNode nodeB = treeQueue.poll();
            TreeNode newNode = new TreeNode(nodeA.getAsciiCode() + "" + nodeB.getAsciiCode(), nodeA.getFrequency() + nodeB.getFrequency());
            treeMap.put(newNode, new Pair<>(nodeA, nodeB));
            treeQueue.add(newNode);
        }
    }

    public void createCode() {
        huffmanCode = new HashMap<>();
        Iterator<Map.Entry<TreeNode, Pair<TreeNode, TreeNode>>> it = treeMap.entrySet().iterator();
        createCodes(it.next().getKey(), "", huffmanCode);
    }

    private void createCodes(TreeNode node, String str, Map<Integer, String> huffmanCode) {
        if (treeMap.get(node).isEmpty()) {
            huffmanCode.put(node.getAsciiCode(), str);
        } else {
            createCodes(treeMap.get(node).getFirst(), str + "0", huffmanCode);
            createCodes(treeMap.get(node).getSecond(), str + "1", huffmanCode);
        }
    }

    public String getDecTab() {
        StringBuilder decTab = new StringBuilder();
        Iterator<Map.Entry<Integer, String>> iterator = huffmanCode.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, String> entry = iterator.next();
            decTab.append(entry.getKey()).append(":").append(entry.getValue());
            if (iterator.hasNext()) decTab.append("-");
        }
        return decTab.toString();
    }

    public static Map<Integer, String> parseStringDecTab(String decTab) {
        Map<Integer, String> readHuffCodes = new HashMap<>();
        String[] decTabArr = decTab.split("-");
        for(int i = 0; i < decTabArr.length; i++) {
            String[] pair = decTabArr[i].split(":");
            int charCode = Integer.parseInt(pair[0]);
            String huffCode = pair[1];
            readHuffCodes.put(charCode, huffCode);
        }
        return readHuffCodes;
    }

    public String compress(String content) {
        StringBuilder compressed = new StringBuilder();
        for (int i = 0; i < content.length(); i++) {
            int charCode = content.charAt(i);
            compressed.append(huffmanCode.get(charCode));
        }
        compressed.append('1');
        while (compressed.length() % 8 != 0) {
            compressed.append('0');
        }
        return compressed.toString();
    }

    public String decompress(String compressed) {
        int k = compressed.length();
        while (compressed.charAt(--k) != '1') {
        }
        compressed = compressed.substring(0, k); // also removes 1
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < compressed.length() - 1; i++) {
            int j = i - 1;
            String str = null;
            while (str == null && j <= compressed.length()) {
                String searchString = compressed.substring(i, ++j);
                str = getCodeByString(searchString);
            }
            content.append(str);
            i = j - 1;
        }
        return content.toString();
    }

    public String getCodeByString(String str) {
        String res = null;
        Set<Map.Entry<Integer, String>> find = huffmanCode.entrySet().stream()
                .filter(entry -> str.equals(entry.getValue()))
                .collect(Collectors.toSet());
        if (find.size() == 1) {
            int asciiCode = find.stream().findFirst().get().getKey();
            res = String.valueOf((char) asciiCode);
        }
        return res;
    }

    public static byte[] convertBitStringToByteArray(String bits) {
        byte[] bytes = new byte[bits.length() / 8];
        for (int i = 0; i < bytes.length; i++) {
            int start = i * 8;
            int end = start + 8;
            bytes[i] = (byte) Integer.parseInt(bits.substring(start, end), 2);
        }
        return bytes;
    }

    public static String convertByteArrayToBitString(byte[] bytes) {
        StringBuilder bits = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            int bInt = (bytes[i] & 0xFF); // exclude the leading 24 bits
            bInt += 0x100; // together with substring -> ensures leading 0
            bits.append(Integer.toBinaryString(bInt).substring(1));
        }
        return bits.toString();
    }
}
