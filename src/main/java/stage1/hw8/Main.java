package stage1.hw8;

import java.io.BufferedReader;
import java.io.InputStreamReader;

class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder builder = new StringBuilder();
        int current = reader.read();

        while (current != -1) {
            builder.append((char) current);
            current = reader.read();
        }

        reader.close();
        System.out.println(builder.reverse());
    }
}
