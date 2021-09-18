package stage4.project;

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

    Coordinate(String input) throws WrongLocationException {
        int x = input.endsWith("10") ? 9 : input.charAt(1) - 49;
        int y = input.charAt(0) - 65;

        if (x > 9 || y < 0 || y > 9 || (input.length() == 3 && !input.endsWith("10"))) {
            throw new WrongLocationException();
        }

        this.x = x;
        this.y = y;
    }

    static Coordinate readCoordinate() throws WrongLocationException {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();
        return new Coordinate(input);
    }
}

class Position {
    final Coordinate start;
    final Coordinate stop;

    Position(String start, String stop) throws WrongLocationException {
        Coordinate firstCoordinate = new Coordinate(start);
        Coordinate secondCoordinate = new Coordinate(stop);

        if (firstCoordinate.x < secondCoordinate.x || firstCoordinate.y < secondCoordinate.y) {
            this.start = firstCoordinate;
            this.stop = secondCoordinate;
        } else {
            this.start = secondCoordinate;
            this.stop = firstCoordinate;
        }
    }

    static Position readPosition() throws WrongLocationException {
        Scanner scanner = new Scanner(System.in);
        String start = scanner.next();
        String stop = scanner.next();
        return new Position(start, stop);
    }
}

class Player {
    final String[][] board = new String[10][];
    final String[] ROW_KEYS = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
    final Ship[] SHIPS = {
            new Ship("Aircraft Carrier", 5),
            new Ship("Battleship", 4),
            new Ship("Submarine", 3),
            new Ship("Cruiser", 3),
            new Ship("Destroyer", 2)
    };

    Player() {
        for (int y = 0; y < 10; y++) {
            String[] row = new String[10];
            Arrays.fill(row, "~");
            board[y] = row;
        }
    }

    void printBoard() {
        printBoard(false);
    }

    void printBoard(Boolean fog) {
        System.out.print(" ");
        for (int x = 1; x < 11; x++) {
            System.out.print(" " + x);
        }
        System.out.println();

        for (int y = 0; y < 10; y++) {
            System.out.print(ROW_KEYS[y]);
            for (int x = 0; x < 10; x++) {
                String cell = board[y][x];
                if (fog && cell.equals("O")) {
                    System.out.print(" ~");
                } else {
                    System.out.print(" " + cell);
                }
            }
            System.out.println();
        }
        System.out.println();
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
                if (hasNeighbors(start_x, y)) {
                    return false;
                }
            }
        } else {
            for (int x = start_x; x != stop_x + 1; x++) {
                if (hasNeighbors(x, start_y)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Check if there is "O" near the given point.
     */
    private boolean hasNeighbors(int x, int y) {
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
                if (board[y + shift.y][x + shift.x].equals("O")) {
                    return true;
                }
            } catch (ArrayIndexOutOfBoundsException ignored) {}
        }

        return false;
    }

    void placeShip(int size) throws WrongLocationException, TooCloseException, WrongLengthException {
        Position position = Position.readPosition();

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
        printBoard();

        for (Ship ship : SHIPS) {
            System.out.printf("Enter the coordinates of the %s (%d cells):", ship.name, ship.size);
            System.out.println();
            System.out.println();

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
            }

            System.out.println();
            printBoard();
        }
    }

    Coordinate askForCoordinate() {
        Coordinate coordinate;

        while (true) {
            try {
                coordinate = Coordinate.readCoordinate();
                break;
            } catch (WrongLocationException e) {
                System.out.println();
                System.out.println("Error! You entered the wrong coordinates! Try again:");
                System.out.println();
            }
        }

        System.out.println();
        return coordinate;
    }

    void fire() {
        Coordinate coordinate = askForCoordinate();

        if (board[coordinate.y][coordinate.x].equals("O")) {
            board[coordinate.y][coordinate.x] = "X";
            printBoard(true);

            if (shipIsStillAfloat(coordinate)) {
                System.out.println("You hit a ship! Try again:");
            } else if (hasShips()) {
                System.out.println("You sank a ship! Specify a new target:");
            } else {
                System.out.print("You sank the last ship. You won. Congratulations!");
            }
        } else {
            board[coordinate.y][coordinate.x] = "M";
            printBoard(true);
            System.out.println("You missed! Try again:");
        }

        System.out.println();
    }

    private boolean shipIsStillAfloat(Coordinate coordinate) {
        return hasNeighbors(coordinate.x, coordinate.y);
    }

    public boolean hasShips() {
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                if (board[y][x].equals("O")) {
                    return true;
                }
            }
        }
        return false;
    }
}

class Game {
    Player player;

    Game() {
        player = new Player();
    }

    void placeShips() {
        player.placeShips();
    }

    void play() {
        System.out.println("The game starts!");
        System.out.println();
        player.printBoard(true);
        System.out.println("Take a shot!");
        System.out.println();

        while (player.hasShips()) {
            player.fire();
        }
    }
}

public class Main {

    public static void main(String[] args) {
        Game game = new Game();
        game.placeShips();
        game.play();
    }
}
