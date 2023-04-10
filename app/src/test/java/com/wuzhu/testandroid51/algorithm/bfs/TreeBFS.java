package com.wuzhu.testandroid51.algorithm.bfs;

import com.wuzhu.testandroid51.algorithm.tree.TreeNode;

import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Hdq on 2022/12/8.
 */
public class TreeBFS {

    @Test
    public void test(){
        int[] array = {1,2,3,4,5,6,7,8,9,10,11};
        TreeNode root = createTree(array);
        bfs(root);

        System.out.println("------------");
        System.out.println(minDepth(new TreeNode(0)));
    }

    private TreeNode createTree(int[] array) {
        TreeNode root = new TreeNode(0);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        for (int i = 0; i < array.length; i = i + 2) {
            TreeNode node = queue.poll();
            TreeNode left = new TreeNode(array[i]);
            TreeNode right = null;
            if (i + 1 < array.length) {
                right = new TreeNode(array[i + 1]);
            }
            node.left = left;
            queue.offer(left);
            node.right = right;
            queue.offer(right);
        }
        return root;
    }

    private void bfs(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            System.out.println(node.val);
            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
        }
    }


    int minDepth(TreeNode root) {
        if (root == null) return 0;
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);
        // root 本身就是一层，depth 初始化为 1
        int depth = 1;

        while (!q.isEmpty()) {
            int sz = q.size();
            /* 将当前队列中的所有节点向四周扩散 */
            for (int i = 0; i < sz; i++) {
                TreeNode cur = q.poll();
                /* 判断是否到达终点 */
                if (cur.left == null && cur.right == null)
                    return depth;
                /* 将 cur 的相邻节点加入队列 */
                if (cur.left != null)
                    q.offer(cur.left);
                if (cur.right != null)
                    q.offer(cur.right);
            }
            /* 这里增加步数 */
            depth++;
        }
        return depth;
    }

}
