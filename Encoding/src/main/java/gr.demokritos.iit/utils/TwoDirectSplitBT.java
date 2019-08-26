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

import java.util.Vector;

/**
 * <p>Split space range into zones directly</p>
 */
public class TwoDirectSplitBT extends BinaryTree {
    /**
     * <p>Matches <i>height</i> to the character corresponding to space H</p>
     *
     * @param height        Value to be placed in binary range tree
     * @return              Corresponding character
     */
    public Character search(Double height) {
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

    /**
     * <p>Print elements of tree with preorder traversal</p>
     *
     * @param V     Vector to be sorted and traversed
     */
    public void print(Vector V) {
        BinaryTree tree = new TwoDirectSplitBT();
        int n = V.size();
        root = tree.sortedArrayToBST(V, 0, n - 1, null);
        System.out.println("Preorder traversal of constructed BST");
        tree.preOrder(root);
    }
}
