package stage1.project;

import java.util.Arrays;
import java.util.Scanner;

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

class Coordinate {
    int x;
    int y;

    Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

class Position {
    Coordinate start;
    Coordinate stop;

    Position(String start, String stop) throws WrongLocationException {
        this.start = parsePosition(start);
        this.stop = parsePosition(stop);
    }

    private Coordinate parsePosition(String position) throws WrongLocationException {
        int x = Character.getNumericValue(position.charAt(1));
        int y = Character.getNumericValue(position.charAt(0)) - 65;

        if (x < 0 || x > 9 || y < 0 || y > 9) {
            throw new WrongLocationException();
        }

        return new Coordinate(x, y);
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

    Position readPosition() throws WrongLocationException {
        Scanner scanner = new Scanner(System.in);
        String start = scanner.next();
        String stop = scanner.next();
        return new Position(start, stop);
    }

    private boolean isValidSize(Position position, int size) {
        if (position.start.x == position.stop.x) {
            return size == Math.abs(position.stop.y - position.start.y);
        } else if (position.start.y == position.stop.y) {
            return size == Math.abs(position.stop.x - position.start.x);
        } else {
            return false;
        }
    }

    private boolean areCollisions(Position position, int size) {
        // ToDo: implement
        return false;
    }

    void placeShip(int size) throws WrongLocationException, TooCloseException, WrongLengthException {
        Position position = readPosition();

        if (!isValidSize(position, size)) {
            throw new WrongLengthException();
        }

        if (!areCollisions(position, size)) {
            throw new TooCloseException();
        }

        // ToDo: implement
    }

    void placeShips() {
        for (Ship ship : SHIPS) {
            System.out.printf("Enter the coordinates of the %s (%d cells):", ship.name, ship.size);
            System.out.println();

            while (true) {
                try {
                    placeShip(ship.size);
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
