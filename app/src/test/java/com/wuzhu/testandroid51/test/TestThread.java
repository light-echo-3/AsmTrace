package com.wuzhu.testandroid51.test;

import org.junit.Test;

/**
 * @author Hdq on 2022/12/5.
 */
public class TestThread {

    @Test
    public void test(){
        Thread thread = new Thread(){
            private Object object = new Object();
            @Override
            public void run() {
                super.run();

                try {
                    sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                interrupt();
                try {
                    object.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                object.notify();
                try {
                    object.wait(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        };
        thread.start();
    }

}
