package com.example.testandroid51.algorithm.bfs;

import static com.example.testandroid51.algorithm.Utils.printIndent;

import com.example.testandroid51.algorithm.tree.TreeNode;

import org.junit.Test;

import java.util.Arrays;

/**
 * 由前序和中序遍历重建二叉树
 * 题目描述：输入某二叉树的前序遍历和中序遍历的结果，请重建出该二叉树。
 * 假 设输入的前序遍历和中序遍历的结果中都不含重复的数字。
 * 例如输入前序遍历序列 {1,2,4,7,3,5,6,8}和中序遍历序列{4,7,2,1,5,3,8,6}，则重建二叉树并返回。
 * @author Hdq on 2022/12/9.
 */
public class ReCreateBinaryTree {

    @Test
    public void test(){
        int[] preOrder = {1,2,4,7,3,5,6,8};
        int[] inOrder = {4,7,2,1,5,3,8,6};
        TreeNode root = createTree(preOrder,0,preOrder.length,inOrder,0,inOrder.length);
        printPreOrder(root);
        System.out.println("-----");
    }

    private void printPreOrder(TreeNode root) {
        if (root == null) {
            return;
        }
        System.out.print(root.val +", ");
        printPreOrder(root.left);
        printPreOrder(root.right);
    }

    int depth;

    private TreeNode createTree(int[] preOrder,int preStart,int preEnd,int[] inOrder,int inStart,int inEnd) {
        depth ++ ;

        if (preStart >= preEnd){
            return null;
        }
        if (inStart >= inEnd){
            return null;
        }

        int rootValue = preOrder[preStart];//1
        TreeNode node = new TreeNode(rootValue);
        int rootInOrderIndex = findIndex(rootValue,inOrder,inStart,inEnd);//3
        int leftLength = rootInOrderIndex - inStart;//3
        int rightLength = inEnd - rootInOrderIndex - 1;//4

        int leftPreStart = preStart + 1;//1
        int leftPreEnd = leftPreStart + leftLength;//4
        int leftInStart = inStart;//0
        int leftInEnd = leftInStart + leftLength;//3

        printIndent(depth);
        System.out.println("left preOrder = " + Arrays.toString(Arrays.copyOfRange(preOrder,leftPreStart,leftPreEnd)) + ", leftPreStart = " + leftPreStart + ", leftPreEnd = " + leftPreEnd +
                ", left inOrder = " + Arrays.toString(Arrays.copyOfRange(inOrder,leftInStart,leftInEnd)) + ", leftInStart = " + leftInStart + ", leftInEnd = " + leftInEnd);


        int rightPreStart = leftPreEnd;//4
        int rightPreEnd = rightPreStart + rightLength;//8
        int rightInStart = rootInOrderIndex + 1;//4
        int rightInEnd = rightInStart + rightLength;//8

        printIndent(depth);
        System.out.println("right preOrder = " + Arrays.toString(Arrays.copyOfRange(preOrder,rightPreStart,rightPreEnd)) + ", rightPreStart = " + rightPreStart + ", rightPreEnd = " + rightPreEnd +
                ", right inOrder = " + Arrays.toString(Arrays.copyOfRange(inOrder,rightInStart,rightInEnd)) + ", rightInStart = " + rightInStart + ", rightInEnd = " + rightInEnd);

        node.left = createTree(preOrder,leftPreStart,leftPreEnd, inOrder,leftInStart,leftInEnd);
        node.right = createTree(preOrder,rightPreStart,rightPreEnd, inOrder,rightInStart,rightInEnd);

        depth --;
        return node;
    }

    private void printIndent(int depth) {
        for (int i = 0; i < depth; i++) {
            System.out.printf(" - ");
        }
    }

    private int findIndex(int rootValue,int[] inOrder,int inStart,int inEnd) {
        for (int i = inStart; i < inEnd; i++) {
            int inValue = inOrder[i];
            if (rootValue == inValue) {
                return i;
            }
        }
        return -1;
    }


}
