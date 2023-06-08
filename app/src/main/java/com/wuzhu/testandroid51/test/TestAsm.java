package com.wuzhu.testandroid51.test;

public class TestAsm {


    private void test(int a) {
        System.out.println(a);
    }

    private void test(int a,int b) {
        System.out.println(a + b);
    }


    /**
     * 测试插桩
     */
    private void test1() {
        int a = 111;
        System.out.println(a);
    }

    private void test2() {
        int a = 111;
        int b = 222;
        System.out.println(a + b);
    }

    private void test3() {
        int a = 111;
        int b = 222;
        int c = 333;
        System.out.println(a + b + c);
    }

}
