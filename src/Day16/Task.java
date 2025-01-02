package Day16;

import utils.AdventReadUtils;

import java.util.*;

public class Task {

    static List<List<Cell>> cellMap = new ArrayList<>();
    static int priceForStep = 1;
    static int priceForRotate = 1000;

    static int startX = 0;
    static int startY = 0;

    static int finishX = 0;
    static int finishY = 0;

    public static void main(String[] args) throws InterruptedException {
        AdventReadUtils adventReadUtils = new AdventReadUtils();
        List<String> inputAsListOfStrings = adventReadUtils.readDataAsString(AdventReadUtils.INPUT_TYPE.data, "16");
        cellMap = convertStringsToMap(inputAsListOfStrings);
        //  printMap(cellMap);
        lookForExit_Deisktra();
    }

    private static boolean lookForExit_Deisktra() {
        Set<Cell> toVisit = new HashSet<>();
        toVisit.add(cellMap.get(startY).get(startX));
        int iteration = 0;
        while (!toVisit.isEmpty()) {
            //  System.out.println("Iteration: " + iteration);
            iteration++;
            Cell currentCell = toVisit.iterator().next();
            toVisit.remove(currentCell);
            if (iteration % 10000 == 0) {
                System.out.println("Iteration: " + iteration);
                System.out.println("elements in queue: " + toVisit.size());
            }


            for (Path currentPath : currentCell.optimalPaths) {

                Direction currentDirection = currentPath.lastDirection;
                for (Direction direction : Direction.values()) {
                    if (isOpposite(currentDirection, direction)) {
                        continue;
                    }
                    Cell cellToVisit = cellMap.get(currentCell.y + direction.shiftY).get(currentCell.x + direction.shiftX);

                    if (cellToVisit.type == CellType.WALL) {
                        continue;
                    }

                    int priceForNewPath = 0;
                    if (direction == currentDirection) {
                        priceForNewPath = currentPath.price + priceForStep;
                    } else {
                        priceForNewPath = currentPath.price + priceForStep + priceForRotate;
                    }

                    if (priceForNewPath <= cellToVisit.getMinimalPrice(direction)) {//Проверять, что я могу встать в такое же положение с учетом 1000
                        Path pathForTheCellToVisit = currentPath.makeCopy();
                        pathForTheCellToVisit.price = priceForNewPath;
                        pathForTheCellToVisit.lastDirection = direction;
                        pathForTheCellToVisit.visitedCells.add(currentCell);
                        if (!cellToVisit.optimalPaths.contains(pathForTheCellToVisit)) {
                            cellToVisit.addOrReplaceOptimalPath(pathForTheCellToVisit, direction);
                            toVisit.add(cellToVisit);
                        }
                    }

                }
            }
        }
        //     printMap(cellMap);
        //   System.out.println();

        printMap(cellMap);

        Cell exitCell = cellMap.get(finishY).get(finishX);
        Set<Cell> uniqCells = new HashSet<>();
        exitCell.removeExpensivePaths();
        printMap(cellMap);

        for (Path path : exitCell.optimalPaths) {
            uniqCells.addAll(path.visitedCells);
        }

        System.out.println("Amount of cells: " + uniqCells.size());

        return false;
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

                switch (line.charAt(j)) {
                    case '#':
                        cell1.type = CellType.WALL;
                        break;
                    case '.':
                        cell1.type = CellType.EMPTY;
                        break;
                    case 'S':
                        cell1.type = CellType.EMPTY;
                        startX = cell1.x;
                        startY = cell1.y;
                        Path path = new Path();
                        path.lastDirection = Direction.RIGHT;
                        path.price = 0;
                        cell1.optimalPaths.add(path);
                        break;
                    case 'E':
                        cell1.type = CellType.EMPTY;
                        finishX = cell1.x;
                        finishY = cell1.y;
                        break;
                }
                row.add(cell1);

            }
            map.add(row);
        }
        return map;
    }

    private static void printMap(List<List<Cell>> cellMap) {
        printMap(cellMap, cellMap.get(finishY).get(finishX));
    }

    private static void printMap(List<List<Cell>> cellMap, Cell currentCell) {

        // Cell exitCell = cellMap.get(finishY).get(finishX);
        Set<Cell> uniqCells = new HashSet<>();
        for (Path path : currentCell.optimalPaths) {
            uniqCells.addAll(path.visitedCells);
        }


        for (List<Cell> row : cellMap) {
            for (Cell cell : row) {
                switch (cell.type) {
                    case WALL:
                        System.out.print("#");
                        break;
                    case EMPTY:
//                        if (cell.path.price == Integer.MAX_VALUE) {
//                            System.out.print(" ");
//                        } else if (cell.path.price<10) {
//                            System.out.print(cell.path.price);
//                        } else {
//                            System.out.print("X");
//                        }
//                        break;
                        if (uniqCells.contains(cell)) {
                            System.out.print("O");
                        } else {
                            System.out.print(" ");
                        }
                        break;

                    case Start:
                        System.out.print("S");
                        break;
                    case Exit:
                        System.out.print("E");
                        break;
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    enum Direction {

        UP(0, -1),
        DOWN(0, 1),
        LEFT(-1, 0),
        RIGHT(1, 0);
        private final int shiftX;
        private final int shiftY;

        Direction(int shiftX, int shiftY) {
            this.shiftX = shiftX;
            this.shiftY = shiftY;
        }
    }

    private static boolean isOpposite(Direction direction, Direction direction1) {
        return direction == Direction.UP && direction1 == Direction.DOWN ||
                direction == Direction.DOWN && direction1 == Direction.UP ||
                direction == Direction.LEFT && direction1 == Direction.RIGHT ||
                direction == Direction.RIGHT && direction1 == Direction.LEFT;
    }

    static class Cell {
        CellType type;
        Set<Path> optimalPaths = new HashSet<>();
        // Path path = new Path();
        int x;
        int y;

        public int getMinimalPrice(Direction direction) {
            int minimalPrice = Integer.MAX_VALUE;
            for (Path path : optimalPaths) {
                if (path.lastDirection == direction && path.price < minimalPrice) {
                    minimalPrice = path.price;
                }
            }
            return minimalPrice;
        }

        public void addOrReplaceOptimalPath(Path path, Direction direction) {
            Set<Path> pathsToRemove = new HashSet<>();
            for (Path currentPath : optimalPaths) {
                if (currentPath.lastDirection == direction) {
                    if (currentPath.price > path.price) {
                        pathsToRemove.add(currentPath);
                    }
                }
            }
            for (Path pathToRemove : pathsToRemove) {
                optimalPaths.remove(pathToRemove);
            }
            optimalPaths.add(path);
        }

        public void removeExpensivePaths() {
            int lowPrice = Integer.MAX_VALUE;
            Set<Path> pathsToRemove = new HashSet<>();
            for (Path path : optimalPaths) {
                if (path.price < lowPrice) {
                    lowPrice = path.price;
                }
            }
            System.out.println("Low price: " + lowPrice);
            for (Path path : optimalPaths) {
                if (path.price > lowPrice) {
                    pathsToRemove.add(path);
                }
            }
            for (Path path : pathsToRemove) {
                optimalPaths.remove(path);
            }
        }

        public int getMinimalLength() {
            int minimalLength = Integer.MAX_VALUE;
            for (Path path : optimalPaths) {
                if (path.visitedCells.size() < minimalLength) {
                    minimalLength = path.visitedCells.size();
                }
            }
            return minimalLength;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Cell cell = (Cell) o;
            return x == cell.x && y == cell.y && type == cell.type;
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, x, y);
        }

        @Override
        public String toString() {
            return
                    "x=" + x +
                            ", y=" + y;
        }
    }

    static class Path {
        List<Cell> visitedCells = new ArrayList<>();
        Direction lastDirection;
        int price = Integer.MAX_VALUE;

        public boolean isEmpty() {
            return visitedCells.isEmpty();
        }

        public Path makeCopy() {
            Path pathCopy = new Path();
            pathCopy.visitedCells = new ArrayList<>(visitedCells);
            pathCopy.lastDirection = lastDirection;
            pathCopy.price = price;
            return pathCopy;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Path path = (Path) o;
            return price == path.price && Objects.equals(visitedCells, path.visitedCells) && lastDirection == path.lastDirection;
        }

        @Override
        public int hashCode() {
            return Objects.hash(visitedCells, lastDirection, price);
        }
    }

    enum CellType {
        WALL, EMPTY, Start, Exit
    }
}
