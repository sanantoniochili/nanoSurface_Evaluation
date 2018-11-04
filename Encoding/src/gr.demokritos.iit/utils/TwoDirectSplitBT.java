package utils;

import java.util.Vector;

public class TwoDirectSplitBT extends BinaryTree {
    public Character search(Double height) { // match <height> to the character corresponding to space H
        // where <height> belongs to H
        Character tempVal = 'A';
        Node temp;

        if (root == null) {
            return null;
        }
        if( Double.compare(height,0)>=0 ) {
            temp = root.right;

            while ( temp!=null ) {
                if ( Double.compare(height, temp.Space.getValue())>0 ) {
                    temp = temp.right;
                } else {
                    tempVal = temp.Space.getKey();
                    temp = temp.left;

                }
            }
            return tempVal;
        } else {
            temp = root.left;

            while (temp != null) {
                if ( Double.compare(height, temp.Space.getValue())<0 ) {
                    temp = temp.left;
                } else {
                    tempVal = temp.Space.getKey();
                    temp = temp.right;

                }
            }

        }
        return tempVal;
    }

    public void print(Vector V) {
        BinaryTree tree = new TwoDirectSplitBT();
        int n = V.size();
        root = tree.sortedArrayToBST(V, 0, n - 1, null);
        System.out.println("Preorder traversal of constructed BST");
        tree.preOrder(root);
    }
}
