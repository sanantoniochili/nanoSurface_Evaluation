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

package utils;

import java.util.Vector;

// used for spaces that dont spread evenly around 0
public class ContinuousSplitBT extends BinaryTree {
    public Character search(Double height) { // match <height> to the character corresponding to space H
        // where <height> belongs to H
        Character tempVal = root.Space.getKey();
        Node temp = root;

        if (root == null) {
            return null;
        }
        if( Double.compare(height,temp.Space.getValue())>=0 ) {
            temp = root.right;
            while (temp != null) {
                if ( Double.compare(height, temp.Space.getValue())<0 ) {
                    temp = temp.left;
                } else {
                    tempVal = temp.Space.getKey();

                    temp = temp.right;
                }
            }
            return tempVal;
        } else {
            temp = root.left;
            while ( temp!=null ) {
                if ( Double.compare(height, temp.Space.getValue())>0 ) {
                    temp = temp.right;
                } else {
                    tempVal = temp.Space.getKey();

                    temp = temp.left;
                }
            }

        }
        return tempVal;
    }

    public void print(Vector V) {
        BinaryTree tree = new ContinuousSplitBT();
        int n = V.size();
        root = tree.sortedArrayToBST(V, 0, n - 1, null);
        System.out.println("Preorder traversal of constructed BST");
        tree.preOrder(root);
    }
}
