import java.util.HashMap;
import java.util.Map;

class Node {
    int val;
    Node left;
    Node right;
    Node(int x) { val = x; }
}

public class Codec {

    // Encodes a tree to a single string.
    private Map<Node, Integer> mp = new HashMap<>();
    public String serialize(Node root) {
        if(root == null) return "null,";

        if(mp.containsKey(root)) throw new RuntimeException("cyclic tree");
        mp.put(root, 1);

        String ss = "";
        ss = ss + String.valueOf(root.val) + ",";

        ss = ss + serialize(root.left);
        ss = ss + serialize(root.right);

        return ss;
    }

    Integer cnt = 0;
    public Node convert(String[] arr) {
        if(arr[cnt].equals("null")) {
            cnt++;
            return null;
        }

        Node r = new Node(Integer.parseInt(arr[cnt]));
        cnt++;

        r.left = convert(arr);
        r.right = convert(arr);

        return r;
    }

    // Decodes your encoded data to tree.
    public Node deserialize(String data) {
        String[] arrOfStr = data.split(",");

        Node r = convert(arrOfStr);
        return r;
    }

    public static void main(String[] args) {
        Node root1 = new Node(1);
        Node root2 = new Node(2);
        Node root3 = new Node(3);
        Node root4 = new Node(4);
        Node root5 = new Node(5);

        root1.left = root2;
        root1.right = root3;
        root3.left = root4;
        root3.right = root5;


        Codec ser = new Codec();
        Codec deser = new Codec();
        Node ans = deser.deserialize(ser.serialize(root1));
        System.out.println(ans);
    }
}

