package com.wuzhu.libasmtrack;

import androidx.annotation.NonNull;

import java.util.LinkedList;

@Deprecated()//"改用try catch方案"
@NotTrace
public class Stack<T> {

    private final LinkedList<T> storage = new LinkedList<>();

    /**
     * 入栈
     */
    public void push(T v) {
        storage.push(v);
    }

    /**
     * 出栈，但不删除
     */
    public T peek() {
        return storage.peek();
    }

    /**
     * 出栈
     */
    public T pop() {
        return storage.pop();
    }

    /**
     * 栈是否为空
     */
    public boolean isEmpty() {
        return storage.isEmpty();
    }

    /**
     * 打印栈元素
     */
    @NonNull
    public String toString() {
        return storage.toString();
    }

}

