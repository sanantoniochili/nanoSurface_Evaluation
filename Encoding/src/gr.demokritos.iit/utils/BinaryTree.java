/** Copyright 2018 Antonia Tsili
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

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