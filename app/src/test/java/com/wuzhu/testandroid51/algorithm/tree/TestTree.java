package com.wuzhu.testandroid51.algorithm.tree;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Hdq on 2022/11/30.
 */
public class TestTree {


    @Test
    public void testTree() {
        int[] array = {0,1,2,3,4,5,6,7,8,9};
        TreeNode root = createBinaryTree(array,0);
        System.out.println("-- begin --------------");

        preOrderTraverse1(root);
        System.out.println();
        System.out.println(preorder(root));

        inOrderTraverse1(root);
        System.out.println();

        System.out.println("-- end --------------");

    }

    public void printChild(TreeNode root) {
        if (root != null) {
            if (root.left != null) {
                System.out.print(root.left.val+"  ");
            }
            if (root.right != null) {
                System.out.print(root.right.val+"  ");
            }
            printChild(root.left);
            printChild(root.right);
        }
    }


    public void preOrderTraverse1(TreeNode root) {
        if (root != null) {
            System.out.print(root.val+"  ");
            preOrderTraverse1(root.left);
            preOrderTraverse1(root.right);
        }
    }

    public void preOrderTraverse2(TreeNode root) {
        LinkedList<TreeNode> stack = new LinkedList<>();
        TreeNode pNode = root;
        while (pNode != null || !stack.isEmpty()) {
            if (pNode != null) {
                System.out.print(pNode.val+"  ");
                stack.push(pNode);
                pNode = pNode.left;
            } else { //pNode == null && !stack.isEmpty()
                TreeNode node = stack.pop();
                pNode = node.right;
            }
        }
    }


    // 创建二叉树

    private TreeNode createBinaryTree(int[] array, int index) {

        TreeNode treeNode = null;
        if (index < array.length) {
            treeNode = new TreeNode(array[index]);
            // 对于顺序存储的完全二叉树，如果某个节点的索引为index，其对应的左子树的索引为2*index+1，右子树为2*index+1
            treeNode.left = createBinaryTree(array, 2 * index + 1);
            treeNode.right = createBinaryTree(array, 2 * index + 2);
        }
        return treeNode;

    }


    public void inOrderTraverse1(TreeNode root) {
        if (root != null) {
            inOrderTraverse1(root.left);
            System.out.print(root.val+"  ");
            inOrderTraverse1(root.right);
        }
    }

    public void postOrderTraverse1(TreeNode root) {
        if (root != null) {
            postOrderTraverse1(root.left);
            postOrderTraverse1(root.right);
            System.out.print(root.val+"  ");
        }
    }

    // 定义：输入一棵二叉树的根节点，返回这棵树的前序遍历结果
    List<Integer> preorder(TreeNode root) {
        List<Integer> res = new LinkedList<>();
        if (root == null) {
            return res;
        }
        // 前序遍历的结果，root.val 在第一个
        res.add(root.val);
        // 后面接着左子树的前序遍历结果
        res.addAll(preorder(root.left));
        // 最后接着右子树的前序遍历结果
        res.addAll(preorder(root.right));
        return res;
    }

}
