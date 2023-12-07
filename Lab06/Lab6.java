import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Lab6 {
    private static InputReader in;
    private static PrintWriter out;
    static AVLTree tree = new AVLTree();

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N = in.nextInt();
        for (int i = 0; i < N; i++) {
            // TODO: process inputs
            int key = in.nextInt();
            tree.insert(key);
        }

        int Q = in.nextInt();
        for (int i = 0; i < Q; i++) {
            char query = in.nextChar();

            if (query == 'G') { grow(); }
            else if (query == 'P') { pick(); }
            else if (query == 'F') { fall(); }
            else { height(); }
        }

        out.close();
    }

    static void grow() {
        int key = in.nextInt();
        tree.insert(key);
        // printTree(tree.root, "", true);
        // TODO: implement this method
    }

    static void pick() {
        int key = in.nextInt();
        if (tree.find(key) == false){
            out.println(-1);
            return;
        }
        tree.delete(key);
        out.println(key);
        // printTree(tree.root, "", true);
        // TODO: implement this method
    }

    static void fall() {
        if (tree.root == null){
            out.println(-1);
            return;
        }
        Node current = tree.root;
        while (current.right != null){
            current = current.right;
        }
        tree.delete(current.key);
        out.println(current.key);
        // printTree(tree.root, "", true);


        // TODO: implement this method
    }

    static void height() {
        if (tree.root == null){
            out.println(0);
            return;
        }
        out.println(tree.height() + 1);

        // TODO: implement this method
    }

    // taken from https://www.programiz.com/dsa/avl-tree
    // a method to print the contents of a Tree data structure in a readable
    // format. it is encouraged to use this method for debugging purposes.
    // to use, simply copy and paste this line of code:
    // printTree(tree.root, "", true);
    static void printTree(Node currPtr, String indent, boolean last) {
        if (currPtr != null) {
            out.print(indent);
            if (last) {
                out.print("R----");
                indent += "   ";
            } else {
                out.print("L----");
                indent += "|  ";
            }
            out.println(currPtr.key);
            printTree(currPtr.left, indent, false);
            printTree(currPtr.right, indent, true);
        }
    }

    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the
    // usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit
    // Exceeded caused by slow input-output (IO)
    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public char nextChar() {
            return next().charAt(0);
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

    }
}

class Node {
    // TODO: modify attributes as needed
    int key;
    int height;
    Node left, right;

    Node(int key) {
        this.key = key;
    }
}

class AVLTree {
    // TODO: modify attributes as needed
    Node root;

    int height() {
        return root == null ? -1 : root.height;
    }

    void insert(int key) {
        root = insert(root, key);
    }

    void delete(int key) {
        root = delete(root, key);
    }
    boolean find(int key) {
        Node current = root;
        while (current != null) {
            if (current.key == key) {
               return true;
            }
            current = current.key < key ? current.right : current.left;
        }
        return false;
    }

    Node insert(Node root, int key) {
        if (root == null) {
            return new Node(key);
        } else if (root.key > key) {
            root.left = insert(root.left, key);
        } else if (root.key < key) {
            root.right = insert(root.right, key);
        } else {
            throw new RuntimeException("duplicate Key!");
        }
        return balancing(root);
    }

    Node delete(Node node, int key) {
        // TODO: implement this method
        if (node == null){
            return node;
        }else if (key < node.key){
            node.left = delete(node.left, key);
        }else if (key > node.key){
            node.right = delete(node.right, key);
        }else{
            if (node.left == null || node.right == null) {
                node = (node.left == null) ? node.right : node.left;
            } else {
                Node anakTerkecil = anakTerkecil(node.left);
                node.key = anakTerkecil.key;
                node.left = delete(node.left, node.key);
            }
        }
        if (node != null) {
            node = balancing(node);
        }
        return node;
    }

    Node balancing(Node node) {
        // TODO: implement this method
        updateHeight(node);
        int balance = getBalance(node);
        if (balance > 1) {
            if (height(node.right.right) > height(node.right.left)) {
                node = singleLeftRotate(node);
            } else {
                node.right = singleRightRotate(node.right);
                node = singleLeftRotate(node);
            }
        } else if (balance < -1) {
            if (height(node.left.left) > height(node.left.right)) {
                node = singleRightRotate(node);
            } else {
                node.left = singleLeftRotate(node.left);
                node = singleRightRotate(node);
            }
        }
        return node;
    }

    Node singleLeftRotate(Node node) {
        // TODO: implement this method
        Node x = node.right;
        Node z = x.left;
        x.left = node;
        node.right = z;
        updateHeight(node);
        updateHeight(x);
        return x;
    }

    Node singleRightRotate(Node node) {
        // TODO: implement this method
        Node x = node.left;
        Node z = x.right;
        x.right = node;
        node.left = z;
        updateHeight(node);
        updateHeight(x);
        return x;
    }

    Node anakTerkecil(Node node){
        Node current = node;
        while (current.right != null){
            current = current.right;
        }
        return current;
    }

    void updateHeight(Node node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    int height(Node n) {
        return n == null ? -1 : n.height;
    }

    int getBalance(Node n) {
        return (n == null) ? 0 : height(n.right) - height(n.left);
    }
}