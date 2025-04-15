package com.wuzhu.testandroid51.test;

public class TestAsm {


    private void test(int a) {
        System.out.println(a);
    }


    private void testThrow(int a) {
        System.out.println(a);
        if (a==0) {
            throw new RuntimeException("test throw");
        }
    }

    private int testTryCatchThrow1() {
        String str = null;
        try {
            int l = str.length();
            return 1;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private int testTryCatchThrow3() {
        String str = null;
        try {
            int l = str.length();
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 2;
        }
    }

    private int testTryCatchThrow2() {
        String str = null;
        try {
            int l = str.length();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            return 111;
        }
    }


    private int test111(int a) {
        System.out.println(a);
        return 111;
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
