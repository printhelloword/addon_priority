package com.biller.addonpriority;

public class TestReloading {

    public static void main(String[] args) {

        while (true) {

            try {
                Thread.sleep(2000);
                System.out.println(MyApplicationProperties.getProperty("test"));
            } catch (InterruptedException e) {
                Application.log.debug("Thread failure" + e.getMessage());
            }

        }

    }
}