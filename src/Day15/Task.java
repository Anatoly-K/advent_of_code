package Day15;

import utils.AdventReadUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Task {

    static List<List<Cell>> cellMap = new ArrayList<>();
    static int robotX = 0;
    static int robotY = 0;

    public static void main(String[] args) throws InterruptedException {
        AdventReadUtils adventReadUtils = new AdventReadUtils();
        List<String> inputAsListOfStrings = adventReadUtils.readDataAsString(AdventReadUtils.INPUT_TYPE.data, "15");
        cellMap = convertStringsToMap(inputAsListOfStrings);
        printMap(cellMap);
        List<Direction> directions = convertStringsToDirections(inputAsListOfStrings);
        System.out.println(directions);

        for (int i = 0; i < directions.size(); i++) {
            System.out.println("Direction: " + directions.get(i));
            boolean robotWasMoved = tryToMove(robotX, robotY, directions.get(i));
            if (robotWasMoved) {
                switch (directions.get(i)) {
                    case UP:
                        robotY--;
                        break;
                    case DOWN:
                        robotY++;
                        break;
                    case LEFT:
                        robotX--;
                        break;
                    case RIGHT:
                        robotX++;
                        break;
                }
            }
            //  printMap(cellMap);
            //  System.out.println();
        }
        System.out.println(calculatePower());
    }

    private static long calculatePower() {
        long power = 0;
        for (int y = 0; y < cellMap.size(); y++) {
            for (int x = 0; x < cellMap.get(y).size(); x++) {
                if (cellMap.get(y).get(x).type == CellType.BOX) {
                    power += (long) y * 100 + x;
                }
            }
        }
        return power;
    }

    private static boolean tryToMove(int x, int y, Direction direction) {
        Cell nextCell = switch (direction) {
            case UP -> cellMap.get(y - 1).get(x);
            case DOWN -> cellMap.get(y + 1).get(x);
            case LEFT -> cellMap.get(y).get(x - 1);
            case RIGHT -> cellMap.get(y).get(x + 1);
        };
        if (nextCell.type == CellType.EMPTY) {
            nextCell.type = cellMap.get(y).get(x).type;
            cellMap.get(y).get(x).type = CellType.EMPTY;
            return true;
        } else if (nextCell.type == CellType.WALL) {
            return false;
        } else if (nextCell.type == CellType.BOX) {
            if (tryToMove(nextCell.x, nextCell.y, direction)) {
                nextCell.type = cellMap.get(y).get(x).type;
                cellMap.get(y).get(x).type = CellType.EMPTY;
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    private static List<Direction> convertStringsToDirections(List<String> inputStrings) {
        List<Direction> directions = new ArrayList<>();
        for (String inputString : inputStrings) {
            if (!inputString.isEmpty() && !inputString.contains("#")) {
                for (int i = 0; i < inputString.length(); i++) {
                    if (inputString.charAt(i) == '<') {
                        directions.add(Direction.LEFT);
                    } else if (inputString.charAt(i) == '>') {
                        directions.add(Direction.RIGHT);
                    } else if (inputString.charAt(i) == '^') {
                        directions.add(Direction.UP);
                    } else if (inputString.charAt(i) == 'v') {
                        directions.add(Direction.DOWN);
                    }
                }
            }

        }
        return directions;
    }

    private static List<List<Cell>> convertStringsToMap(List<String> input) {
        List<List<Cell>> map = new ArrayList<>();
        for (int i = 0; i < input.size(); i++) {
            String line = input.get(i);
            if (line.isEmpty()) {
                break;
            }
            List<Cell> row = new ArrayList<>();
            for (int j = 0; j < line.length(); j++) {
                Cell cell = new Cell();
                cell.x = j;
                cell.y = i;
                switch (line.charAt(j)) {
                    case '#':
                        cell.type = CellType.WALL;
                        break;
                    case '.':
                        cell.type = CellType.EMPTY;
                        break;
                    case '@':
                        cell.type = CellType.ROBOT;
                        robotX = j;
                        robotY = i;
                        break;
                    case 'O':
                        cell.type = CellType.BOX;
                        break;
                }
                row.add(cell);
            }
            map.add(row);
        }
        return map;
    }

    private static void printMap(List<List<Cell>> cellMap) {
        for (List<Cell> row : cellMap) {
            for (Cell cell : row) {
                switch (cell.type) {
                    case WALL:
                        System.out.print("#");
                        break;
                    case EMPTY:
                        System.out.print(".");
                        break;
                    case ROBOT:
                        System.out.print("@");
                        break;
                    case BOX:
                        System.out.print("O");
                        break;
                }
            }
            System.out.println();
        }
    }

    enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    static class Cell {
        CellType type;
        int x;
        int y;
    }

    enum CellType {
        WALL, EMPTY, BOX, ROBOT
    }
}
