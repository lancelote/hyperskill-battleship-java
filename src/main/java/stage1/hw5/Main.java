package stage1.hw5;

import java.io.IOException;

public class Main {

    public static void method() throws IOException {
        throw new IOException();
    }

    public static void main(String[] args) {
        try {
            method();
        } catch (Exception e) {
            System.out.println(e.getClass());
        }
    }
}
