package utils;

import javafx.util.Pair;
import java.util.Vector;

// A binary tree node
class Node {
    Pair<Character,Double> Space;
    Node left, right, parent;

    Node(Pair<Character,Double> p, Node parent) {
        Space = p;
        this.parent = parent;
        left = right = null;
    }
}

public abstract class BinaryTree {
    static Node root;

    /* A function that constructs Balanced Binary Search Tree
     from a sorted array */
    public Node sortedArrayToBST(Vector<Pair<Character, Double>> V, int start, int end, Node parent) {

        /* Base Case */
        if (start > end) {
            return null;
        }

        /* Get the middle element and make it root */
        int mid = (start + end) / 2;
        Node node = new Node(V.get(mid), parent);

        /* Recursively construct the left subtree and make it
         left child of root */
        node.left = sortedArrayToBST(V, start, mid - 1, node);

        /* Recursively construct the right subtree and make it
         right child of root */
        node.right = sortedArrayToBST(V, mid + 1, end, node);

        return node;
    }

    // initialize with result of previous function
    public void init(Node root) {
        this.root = root;
    }

    /* A utility function to print preorder traversal of BST */
    void preOrder(Node node) {
        if (node == null) {
            return;
        }
        System.out.print(node.Space.getValue() + " ");
        preOrder(node.left);
        preOrder(node.right);
    }

    public Character search(Double height) { return null; }
}