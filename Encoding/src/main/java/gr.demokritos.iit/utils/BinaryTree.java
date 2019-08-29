/** 
* Copyright 2018 Antonia Tsili NCSR Demokritos
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

package gr.demokritos.iit.utils;

import javafx.util.Pair;
import java.util.Vector;

/**
 * A binary tree node
 */
class Node {
    /**
     * Range boundary matched to alphabetical character
     */
    Pair<Character,Double> Space;
    Node left, right, parent;

    /**
     * <p>Initialization of tree node</p>
     *
     * @param p         Match of boundary to character
     * @param parent    Parent of tree element
     */
    Node(Pair<Character,Double> p, Node parent) {
        Space = p;
        this.parent = parent;
        left = right = null;
    }
}

/**
 * <p>A binary tree that distributes the divided spacings and serves for matching the height to a ]n alphabetical character </p>
 */
public abstract class BinaryTree {
    /**
     * Root of binary tree with ranges
     */
    static Node root;

    /**
     * <p>A function that constructs Balanced Binary Search Tree from a sorted array</p>
     *
     * @param V         Vector of matches between heights and alphabetical characters
     * @param start     Element id
     * @param end       Element id
     * @param parent    Parent node on Balanced Binary Tree
     * @return          Return median as root of tree
     */
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

    /**
     * <p>Initialize with result of constructing function</p>
     *
     * @param root
     */
    public void init(Node root) {
        this.root = root;
    }

    /**
     * <p>A utility function to print preorder traversal of BST</p>
     *
     * @param node          Node of tree
     */
    void preOrder(Node node) {
        if (node == null) {
            return;
        }
        System.out.print(node.Space.getValue() + " ");
        preOrder(node.left);
        preOrder(node.right);
    }

    /**
     * <p>Match height to character</p>
     *
     * @param height        Height value
     * @return              Corresponding character
     */
    public Character search(Double height) { return null; }
}