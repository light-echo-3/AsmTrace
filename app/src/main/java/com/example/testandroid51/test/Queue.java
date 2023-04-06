package com.example.testandroid51.test;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Hdq on 2022/11/29.
 */
// 队列封装类
class Queue {
    private int[] arr = new int[5];
    int size = 0;

    // 初始化锁和两个Condition
    private ReentrantLock lock = new ReentrantLock();
    public Condition pCondition = lock.newCondition();
    public Condition cCondition = lock.newCondition();
    public void lock() {
        lock.lock();
    }

    public void unLock() {
        lock.unlock();
    }

    public boolean isEmpty() {
        return size==0;
    }

    public boolean isFull() {
        return size==5;
    }

    public void put(Integer value,String name) throws Exception {

        try {
            lock.lock();
            if (isFull()){
                // 队列满了让生产者等待
                pCondition.await();
            }
            arr[size % 5] = value;
            size++;
            // 生产完唤醒消费者
            cCondition.signalAll();
        } finally {
            System.out.println(name +"-put-" + Arrays.toString(arr));
            lock.unlock();
        }
    }

    public int take() throws Exception {
        try {
            lock.lock();
            // 队列空了就让生产者等待
            if (isEmpty()){
                cCondition.await();
            }
            int value = arr[size % 5];
            size--;
            // 消费完通知生产者
            pCondition.signalAll();
            return value;
        } finally {
            System.out.println("take-" + Arrays.toString(arr));
            lock.unlock();
        }
    }
}
