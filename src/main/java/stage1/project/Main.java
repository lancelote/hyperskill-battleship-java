package stage1.project;

import java.util.Arrays;

class WrongLengthException extends Exception {
}

class WrongLocationException extends Exception {
}

class TooCloseException extends Exception {
}

class Ship {
    String name;
    int size;

    Ship(String name, int size) {
        this.name = name;
        this.size = size;
    }
}

class Coordinates {
    String start;
    String stop;

    Coordinates(String start, String stop) {
        this.start = start;
        this.stop = stop;
    }
}

class Game {
    String[][] board = new String[10][];
    String[] ROW_KEYS = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
    Ship[] SHIPS = {
            new Ship("Aircraft Carrier", 5),
            new Ship("Battleship", 4),
            new Ship("Submarine", 3),
            new Ship("Cruiser", 3),
            new Ship("Destroyer", 2)
    };

    Game() {
        for (int i = 0; i < 10; i++) {
            String[] row = new String[10];
            Arrays.fill(row, "~");
            board[i] = row;
        }
    }

    void printBoard() {
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
        System.out.println();
    }

    Coordinates readCoordinates(int size) throws WrongLocationException {
        return null;
    }

    void placeShip(String name, int size) throws WrongLocationException, TooCloseException, WrongLengthException {
        Coordinates coordinates = readCoordinates(size);
    }

    void placeShips() {
        for (Ship ship : SHIPS) {
            System.out.printf("Enter the coordinates of the %s (%d cells):", ship.name, ship.size);
            System.out.println();

            while (true) {
                try {
                    placeShip(ship.name, ship.size);
                    break;
                } catch (WrongLengthException e) {
                    System.out.printf("Error! Wrong length of the %s! Try again:", ship.name);
                    System.out.println();
                } catch (WrongLocationException e) {
                    System.out.println("Error! Wrong ship location! Try again:");
                } catch (TooCloseException e) {
                    System.out.println("Error! You placed it too close to another one. Try again:");
                }
            }
        }
    }
}

public class Main {

    public static void main(String[] args) {
        Game game = new Game();
        game.printBoard();
        game.placeShips();
    }
}
