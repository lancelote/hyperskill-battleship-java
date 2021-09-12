package stage1.project;

import java.util.Arrays;
import java.util.Scanner;

class WrongLengthException extends Exception {
}

class WrongLocationException extends Exception {
}

class TooCloseException extends Exception {
}

class Shift {
    final int x;
    final int y;

    Shift(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

class Ship {
    final String name;
    final int size;

    Ship(String name, int size) {
        this.name = name;
        this.size = size;
    }
}

class Coordinate {
    final int x;
    final int y;

    Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

class Position {
    final Coordinate start;
    final Coordinate stop;

    Position(String start, String stop) throws WrongLocationException {
        Coordinate firstCoordinate = parsePosition(start);
        Coordinate secondCoordinate = parsePosition(stop);

        if (firstCoordinate.x < secondCoordinate.x || firstCoordinate.y < secondCoordinate.y) {
            this.start = firstCoordinate;
            this.stop = secondCoordinate;
        } else {
            this.start = secondCoordinate;
            this.stop = firstCoordinate;
        }
    }

    private Coordinate parsePosition(String position) throws WrongLocationException {
        int x = position.endsWith("10") ? 9 : position.charAt(1) - 49;
        int y = position.charAt(0) - 65;

        if (x > 9 || y < 0 || y > 9) {
            throw new WrongLocationException();
        }

        return new Coordinate(x, y);
    }
}

class Game {
    final String[][] board = new String[10][];
    final String[] ROW_KEYS = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
    final Ship[] SHIPS = {
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
            return size == position.stop.y - position.start.y + 1;
        } else if (position.start.y == position.stop.y) {
            return size == position.stop.x - position.start.x + 1;
        } else {
            return false;
        }
    }

    private boolean areCollisions(Position position, int size) {
        int start_x = position.start.x;
        int start_y = position.start.y;
        int stop_x = position.stop.x;
        int stop_y = position.stop.y;

        assert start_x == stop_x || start_y == stop_y;
        assert stop_x - start_x + 1 == size || stop_y - start_y + 1 == size;

        if (start_x == stop_x) {
            for (int y = start_y; y != stop_y + 1; y++) {
                if (areNeighbors(start_x, y)) {
                    return false;
                }
            }
        } else {
            for (int x = start_x; x != stop_x + 1; x++) {
                if (areNeighbors(x, start_y)) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean areNeighbors(int x, int y) {
        Shift[] shifts = {
                new Shift(-1, 1),
                new Shift(0, 1),
                new Shift(1, 1),
                new Shift(1, 0),
                new Shift(1, -1),
                new Shift(0, -1),
                new Shift(-1, -1),
                new Shift(-1, 0)
        };

        for (Shift shift : shifts) {
            try {
                if (!board[y + shift.y][x + shift.x].equals("~")) {
                    return true;
                }
            } catch (ArrayIndexOutOfBoundsException ignored) {}
        }

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

        int start_x = position.start.x;
        int start_y = position.start.y;
        int stop_x = position.stop.x;
        int stop_y = position.stop.y;

        assert start_x == stop_x || start_y == stop_y;
        assert stop_x - start_x + 1 == size || stop_y - start_y + 1 == size;

        if (start_x == stop_x) {
            for (int y = start_y; y != stop_y + 1; y++) {
                board[y][start_x] = "O";
            }
        } else {
            for (int x = start_x; x != stop_x + 1; x++) {
                board[start_y][x] = "O";
            }
        }
    }

    void placeShips() {
        for (Ship ship : SHIPS) {
            System.out.printf("Enter the coordinates of the %s (%d cells):", ship.name, ship.size);
            System.out.println();
            System.out.println();
            System.out.print("> ");

            while (true) {
                try {
                    placeShip(ship.size);
                    break;
                } catch (WrongLengthException e) {
                    System.out.println();
                    System.out.printf("Error! Wrong length of the %s! Try again:", ship.name);
                    System.out.println();
                } catch (WrongLocationException e) {
                    System.out.println();
                    System.out.println("Error! Wrong ship location! Try again:");
                } catch (TooCloseException e) {
                    System.out.println();
                    System.out.println("Error! You placed it too close to another one. Try again:");
                }
                System.out.println();
                System.out.print("> ");
            }

            System.out.println();
            printBoard();
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
