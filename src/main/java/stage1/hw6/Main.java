package stage1.hw6;

import java.io.BufferedReader;
import java.io.InputStreamReader;

class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String text = reader.readLine().trim();
        reader.close();

        if (text.length() == 0) {
            System.out.println(0);
        } else {
            System.out.println(text.split("\\s+").length);
        }
    }
}
