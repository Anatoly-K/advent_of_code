package Day15;

import utils.AdventReadUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Task {

    static List<List<Cell>> cellMap = new ArrayList<>();
    static List<List<Cell>> cellMapCopy = new ArrayList<>();

    public static List<List<Cell>> copyMap(List<List<Cell>> cellMap) {
        List<List<Cell>> cellMapCopy = new ArrayList<>();
        for (int i = 0; i < cellMap.size(); i++) {
            List<Cell> row = new ArrayList<>();
            for (int j = 0; j < cellMap.get(i).size(); j++) {
                Cell cell = new Cell();
                cell.type = cellMap.get(i).get(j).type;
                cell.x = cellMap.get(i).get(j).x;
                cell.y = cellMap.get(i).get(j).y;
                row.add(cell);
            }
            cellMapCopy.add(row);
        }
        return cellMapCopy;
    }

    static int robotX = 0;
    static int robotY = 0;

    public static void main(String[] args) throws InterruptedException {
        AdventReadUtils adventReadUtils = new AdventReadUtils();
        List<String> inputAsListOfStrings = adventReadUtils.readDataAsString(AdventReadUtils.INPUT_TYPE.data, "15");
        cellMap = convertStringsToMap(inputAsListOfStrings);
        printMap(cellMap);
        List<Direction> directions = convertStringsToDirections(inputAsListOfStrings);
        System.out.println(directions);
        cellMapCopy = copyMap(cellMap);
        for (Direction direction : directions) {
            System.out.println("Direction: " + direction);
            boolean robotWasMoved = tryToMoveInitial(robotX, robotY, direction);
            if (robotWasMoved) {
                switch (direction) {
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
                cellMapCopy = copyMap(cellMap);
            } else {
                cellMap = copyMap(cellMapCopy);
            }
            //  printMap(cellMap);
            System.out.println();
        }
        System.out.println(calculatePower());

        //1449265 too high
        //1448458
    }

    private static long calculatePower() {
        long power = 0;
        for (int y = 0; y < cellMap.size(); y++) {
            for (int x = 0; x < cellMap.get(y).size(); x++) {
                if (cellMap.get(y).get(x).type == CellType.BOX_L) {
                    power += (long) y * 100 + x;
                }
            }
        }
        return power;
    }

    enum ObjectToMove {
        ROBOT, BOX
    }

//    private static boolean tryToMove(int x, int y, Integer x2, Integer y2, Direction direction, ObjectToMove objectToMove) {
//        Cell nextCell = switch (direction) {
//            case UP -> cellMap.get(y - 1).get(x);
//            case DOWN -> cellMap.get(y + 1).get(x);
//            case LEFT -> cellMap.get(y).get(x - 1);
//            case RIGHT -> cellMap.get(y).get(x + 1);
//        };
//
//        //if horizontal move
//        if (direction == Direction.LEFT || direction == Direction.RIGHT) {
//            if (nextCell.type == CellType.EMPTY) {
//                nextCell.type = cellMap.get(y).get(x).type;
//                cellMap.get(y).get(x).type = CellType.EMPTY;
//                return true;
//            } else if (nextCell.type == CellType.WALL) {
//                return false;
//            } else if (nextCell.type == CellType.BOX_L) {
//                if (tryToMove(nextCell.x, nextCell.y, null, null, direction, ObjectToMove.BOX)) {
//                    nextCell.type = cellMap.get(y).get(x).type;
//                    cellMap.get(y).get(x).type = CellType.EMPTY;
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        }
//        //vertical move
//        else {
//            if (nextCell.type == CellType.WALL) {
//                return false;
//            } else if (nextCell.type == CellType.EMPTY) {
//                if (x2 != null || y2 != null) {
////                    if (objectToMove == ObjectToMove.BOX) {
////                        cellMap.get(y2).get(x2).type = CellType.BOX_L;
////                    }
//
//                    Cell cellNExtToEmptyThatMightBeOccupied = cellMap.get(y2).get(x2).type = CellType.BOX_L;
//                }
//                nextCell.type = cellMap.get(y).get(x).type;
//                cellMap.get(y).get(x).type = CellType.EMPTY;
//                return true;
//            } else if (nextCell.type == CellType.BOX_R) {
//                if (tryToMove(nextCell.x, nextCell.y, direction, ObjectToMove.BOX)) {
//                    nextCell.type = cellMap.get(y).get(x).type;
//                    cellMap.get(y).get(x).type = CellType.EMPTY;
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        }
//
//        return true;
//    }

    private static boolean tryToMoveHorizontally(int x, int y, Direction direction) {
        Cell nextCell = switch (direction) {
            case UP -> cellMap.get(y - 1).get(x);
            case DOWN -> cellMap.get(y + 1).get(x);
            case LEFT -> cellMap.get(y).get(x - 1);
            case RIGHT -> cellMap.get(y).get(x + 1);
        };

        //if horizontal move
        if (nextCell.type == CellType.EMPTY) {
            nextCell.type = cellMap.get(y).get(x).type;
            cellMap.get(y).get(x).type = CellType.EMPTY;
            return true;
        } else if (nextCell.type == CellType.WALL) {
            return false;
        } else if (nextCell.type == CellType.BOX_L || nextCell.type == CellType.BOX_R) {
            if (tryToMoveHorizontally(nextCell.x, nextCell.y, direction)) {
                nextCell.type = cellMap.get(y).get(x).type;
                cellMap.get(y).get(x).type = CellType.EMPTY;
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    private static boolean tryToMoveInitial(int x, int y, Direction direction) {
        if (direction == Direction.LEFT || direction == Direction.RIGHT) {
            return tryToMoveHorizontally(x, y, direction);
        } else {
            return (tryToMoveVertically(x, y, null, null, direction));
        }
    }

    private static boolean tryToMoveVertically(int x, int y, Integer x2, Integer y2, Direction direction) {
        Cell nextCell_L = switch (direction) {
            case UP -> cellMap.get(y - 1).get(x);
            case DOWN -> cellMap.get(y + 1).get(x);
            case LEFT -> cellMap.get(y).get(x - 1);
            case RIGHT -> cellMap.get(y).get(x + 1);
        };

        Cell nextCell_R = null;
        if (x2 != null) {
            nextCell_R = switch (direction) {
                case UP -> cellMap.get(y2 - 1).get(x2);
                case DOWN -> cellMap.get(y2 + 1).get(x2);
                case LEFT -> cellMap.get(y2).get(x2 - 1);
                case RIGHT -> cellMap.get(y2).get(x2 + 1);
            };
        }

        //is wall
        if (bothAreWalls(nextCell_L, nextCell_R)) {
            return false;
        } else if (x2 != null && anyIsWall(nextCell_L, nextCell_R)) {
            return false;
        } else if (x2 == null && nextCell_L.type == CellType.WALL) {
            return false;
        }
        //is empty
        else if (bothAreEmpty(nextCell_L, nextCell_R)) {
            if (x2 != null) {
                nextCell_R.type = cellMap.get(y2).get(x2).type;
                nextCell_L.type = cellMap.get(y).get(x).type;
                cellMap.get(y2).get(x2).type = CellType.EMPTY;
                cellMap.get(y).get(x).type = CellType.EMPTY;
            } else {
                nextCell_L.type = cellMap.get(y).get(x).type;
                cellMap.get(y).get(x).type = CellType.EMPTY;
            }
            return true;
        } else if (x2 == null && nextCell_L.type == CellType.EMPTY) {
            nextCell_L.type = cellMap.get(y).get(x).type;
            cellMap.get(y).get(x).type = CellType.EMPTY;
        }
        //I'm pushing box already
        else if (x2 != null && nextCell_L.type == CellType.BOX_L && nextCell_R.type == CellType.BOX_R) {
            if (tryToMoveVertically(nextCell_L.x, nextCell_L.y, nextCell_R.x, nextCell_R.y, direction)) {
                nextCell_L.type = cellMap.get(y).get(x).type;
                cellMap.get(y).get(x).type = CellType.EMPTY;
                nextCell_R.type = cellMap.get(y2).get(x2).type;
                cellMap.get(y2).get(x2).type = CellType.EMPTY;
                return true;
            } else {
                return false;
            }
        } else if (x2 == null && nextCell_L.type == CellType.BOX_L) {
            if (tryToMoveVertically(nextCell_L.x, nextCell_L.y, nextCell_L.x + 1, nextCell_L.y, direction)) {
                nextCell_L.type = cellMap.get(y).get(x).type;
                cellMap.get(y).get(x).type = CellType.EMPTY;
                return true;
            } else {
                return false;
            }
        } else if (x2 == null && nextCell_L.type == CellType.BOX_R) {
            if (tryToMoveVertically(nextCell_L.x - 1, nextCell_L.y, nextCell_L.x, nextCell_L.y, direction)) {
                nextCell_L.type = cellMap.get(y).get(x).type;
                cellMap.get(y).get(x).type = CellType.EMPTY;
                return true;
            } else {
                return false;
            }
        } else if (x2 != null && nextCell_L.type == CellType.BOX_R && nextCell_R.type == CellType.BOX_L) {
            if (tryToMoveVertically(nextCell_L.x - 1, nextCell_L.y, nextCell_L.x, nextCell_L.y, direction) &&
                    tryToMoveVertically(nextCell_R.x, nextCell_R.y, nextCell_R.x + 1, nextCell_R.y, direction)) {
                nextCell_R.type = cellMap.get(y2).get(x2).type;
                cellMap.get(y2).get(x2).type = CellType.EMPTY;
                nextCell_L.type = cellMap.get(y).get(x).type;
                cellMap.get(y).get(x).type = CellType.EMPTY;
                return true;
            } else {
                return false;
            }
        } else if (x2 != null && nextCell_L.type == CellType.BOX_R && nextCell_R.type == CellType.EMPTY) {
            if (tryToMoveVertically(nextCell_L.x - 1, nextCell_L.y, nextCell_L.x, nextCell_L.y, direction)) {
                nextCell_L.type = cellMap.get(y).get(x).type;
                cellMap.get(y).get(x).type = CellType.EMPTY;
                nextCell_R.type = cellMap.get(y2).get(x2).type;
                cellMap.get(y2).get(x2).type = CellType.EMPTY;
                return true;
            } else {
                return false;
            }
        } else if (x2 != null && nextCell_R.type == CellType.BOX_L && nextCell_L.type == CellType.EMPTY) {
            if (tryToMoveVertically(nextCell_R.x, nextCell_L.y, nextCell_R.x + 1, nextCell_L.y, direction)) {
                nextCell_L.type = cellMap.get(y).get(x).type;
                cellMap.get(y).get(x).type = CellType.EMPTY;
                nextCell_R.type = cellMap.get(y2).get(x2).type;
                cellMap.get(y2).get(x2).type = CellType.EMPTY;
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    private static boolean bothAreEmpty(Cell cell1, Cell cell2) {
        return (cell1 == null || cell1.type == CellType.EMPTY) && (cell2 == null || cell2.type == CellType.EMPTY);
    }

    private static boolean anyIsWall(Cell cell1, Cell cell2) {
        return (cell1 != null && cell1.type == CellType.WALL) || (cell2 != null && cell2.type == CellType.WALL);
    }

    private static boolean bothAreWalls(Cell cell1, Cell cell2) {
        return (cell1 != null && cell1.type == CellType.WALL) && (cell2 != null && cell2.type == CellType.WALL);
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
            int currentX = 0;
            for (int j = 0; j < line.length(); j++) {
                Cell cell1 = new Cell();
                cell1.y = i;
                cell1.x = currentX;
                currentX++;

                Cell cell2 = new Cell();
                cell2.y = i;
                cell2.x = currentX;
                currentX++;

                switch (line.charAt(j)) {
                    case '#':
                        cell1.type = CellType.WALL;
                        row.add(cell1);
                        cell2.type = CellType.WALL;
                        row.add(cell2);
                        break;
                    case '.':
                        cell1.type = CellType.EMPTY;
                        row.add(cell1);
                        cell2.type = CellType.EMPTY;
                        row.add(cell2);
                        break;
                    case '@':
                        cell1.type = CellType.ROBOT;
                        row.add(cell1);
                        robotX = cell1.x;
                        robotY = cell1.y;
                        cell2.type = CellType.EMPTY;
                        row.add(cell2);
                        break;
                    case 'O':
                        cell1.type = CellType.BOX_L;
                        row.add(cell1);
                        cell2.type = CellType.BOX_R;
                        row.add(cell2);
                        break;
                }
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
                    case BOX_L:
                        System.out.print("[");
                        break;
                    case BOX_R:
                        System.out.print("]");
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
        WALL, EMPTY, BOX_L, BOX_R, ROBOT
    }
}
