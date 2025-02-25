package com.mycompany.app;

/**
 * Hello world!
 */
public class App {

    private static final String MESSAGE = "Hello World!";

    public App() {}

    public static void main(String[] args) {
        System.out.println(MESSAGE);

        // Keep the application running
        while (true) {
            try {
                Thread.sleep(10000); // Sleep for 10 seconds (prevents 100% CPU usage)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public String getMessage() {
        return MESSAGE;
    }
}
