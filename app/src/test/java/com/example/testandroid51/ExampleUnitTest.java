package com.example.testandroid51;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.LinkedList;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void traverse() {
        assertEquals(4, 2 + 2);
    }




    @Test
    public void testLinkedList() {
        LinkedList<Integer> stack = new LinkedList<>();
        stack.push(1);
        stack.pop();


        LinkedList<Integer> queue = new LinkedList<>();
        queue.offer(1);
        queue.poll();


    }





}