package com.wuzhu.testandroid51.algorithm.linkedlist;

import org.junit.Test;

/**
 * @author Hdq on 2022/12/1.
 */
public class TestLinkedList {


    @Test
    public void createLink(){
        ListNode header1 = new ListNode(1);
        ListNode p1 = header1;
        p1.next = new ListNode(2);
        p1 = p1.next;
        p1.next = new ListNode(4);

        ListNode header2 = new ListNode(1);
        ListNode p2 = header2;
        p2.next = new ListNode(3);
        p2 = p2.next;
        p2.next = new ListNode(4);

        ListNode merged = mergeTwoLists(header1,header2);
        printList(merged);

    }

    private void printList(ListNode merged) {
        System.out.println("-----------");
        while (merged != null){
            System.out.print(merged.val);
            merged = merged.next;
        }
        System.out.println();
    }

    ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(-1);
        ListNode p1 = l1,p2 = l2,p = dummy;
        while (p1 != null && p2 != null){
            if (p1.val < p2.val){
                p.next = p1;
                p = p.next;
                p1 = p1.next;
            } else {
                p.next = p2;
                p = p.next;
                p2 = p2.next;
            }
        }
        if (p1 != null){
            p.next = p1;
        }
        if (p2 != null){
            p.next = p2;
        }
        return dummy.next;
    }

}
