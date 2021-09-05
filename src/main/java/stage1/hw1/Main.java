package stage1.hw1;

public class Main {

    public static void method() {
        throw new RuntimeException();
    }

    public static void main(String[] args) {
        try {
            method();
        } catch (RuntimeException e) {
            System.out.println("RuntimeException");
        } catch (Exception e) {
            System.out.println("Exception");
        }
    }
}
