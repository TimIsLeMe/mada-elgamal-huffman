import com.sun.source.tree.Tree;

import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.Objects;

public class TreeNode implements Comparable {
    private int frequency;
    private int asciiCode;
    private String id;
    private boolean invalidAscii;

    public static int counter = 0;

    public TreeNode(String id, int frequency) {
        this(0, frequency);
        this.id = id;
        this.invalidAscii = true;
    }
    public TreeNode(int asciiCode, int frequency) {
        this.asciiCode = asciiCode;
        this.frequency = frequency;
        counter++;
    }

    public int getAsciiCode() {
        return asciiCode;
    }

    public int getFrequency() {
        return frequency;
    }
    public boolean isInvalidAscii() {
        return invalidAscii;
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) return true;
        if(!(o instanceof TreeNode)) return false;
        TreeNode treeObj = (TreeNode) o;
        return treeObj.asciiCode == asciiCode && treeObj.id == id;
    }

    @Override
    public int hashCode() {
        return invalidAscii ? Objects.hash(id) : Objects.hash(asciiCode);
    }

    @Override
    public int compareTo(Object o) {
        if (o == this) return 0;
        if (!(o instanceof TreeNode)) throw new ClassCastException();
        TreeNode compared = (TreeNode) o;
        int res = Integer.compare(this.frequency, compared.frequency);
        if (res == 0) {
            if(invalidAscii && compared.id != null) {
                return id.compareTo(compared.id);
            } else {
                return Integer.compare(asciiCode, compared.asciiCode);
            }
        }
        return res;
    }
}
