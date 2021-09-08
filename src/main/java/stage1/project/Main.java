package stage1.project;

import java.util.Arrays;

class Game {
    String[][] board = new String[10][];
    String[] ROW_KEYS = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};

    Game() {
        for (int i = 0; i < 10; i++) {
            String[] row = new String[10];
            Arrays.fill(row, "~");
            board[i] = row;
        }
    }

    void print() {
        System.out.print(" ");
        for (int i = 1; i < 11; i++) {
            System.out.print(" " + i);
        }
        System.out.println();

        for (int i = 0; i < 10; i++) {
            System.out.print(ROW_KEYS[i]);
            for (int j = 0; j < 10; j++) {
                System.out.print(" " + board[i][j]);
            }
            System.out.println();
        }
    }
}

public class Main {

    public static void main(String[] args) {
        Game game = new Game();
        game.print();
    }
}
