package Day12;

import utils.AdventReadUtils;

import java.util.*;

public class Task {

    private static List<List<Cell>> cellsMap = new ArrayList<>();
    private static Map<String, Region> regionMap = new HashMap<>();


    public static void main(String[] args) {
        AdventReadUtils adventReadUtils = new AdventReadUtils();
        List<List<String>> inputAsString = adventReadUtils.readDataAsCharMap(AdventReadUtils.INPUT_TYPE.data, "12");
        //  printCharMap(inputAsString);

        cellsMap = mapOfStringsIntoMapOfCells(inputAsString);
        System.out.println("Input as cell:");
        printCellMap(cellsMap);
        starB(cellsMap);
    }

    private static void starB(List<List<Cell>> inputAsCell) {
        Cell nextUnvisitedCell = pickNextUnvisitedCell(inputAsCell);
        int regionNum = 0;
        int totalPower = 0;
        while (nextUnvisitedCell != null) {
            Set<Cell> cellsInRegion = coverRegion(nextUnvisitedCell, nextUnvisitedCell, new HashSet<>());

            Region region = new Region();
            region.value = nextUnvisitedCell.value;
            region.cells = new ArrayList<>(cellsInRegion);
            region.perimeter = calculatePerimeterOfTheRegion(region);
            region.sides = calculateSidesOfTheRegion(region);
            regionMap.put(nextUnvisitedCell.value, region);

            System.out.println("Region " + regionNum + "_" + nextUnvisitedCell.value + " size: " + cellsInRegion.size() + " sides: " + region.sides + " power: " + cellsInRegion.size() * region.sides);
            totalPower += cellsInRegion.size() * region.sides;
            regionNum++;
            nextUnvisitedCell = pickNextUnvisitedCell(inputAsCell);
        }
        System.out.println("Total power: " + totalPower);
    }

    private static void starA(List<List<Cell>> inputAsCell) {
        Cell nextUnvisitedCell = pickNextUnvisitedCell(inputAsCell);
        int regionNum = 0;
        int totalPower = 0;
        while (nextUnvisitedCell != null) {
            Set<Cell> cellsInRegion = coverRegion(nextUnvisitedCell, nextUnvisitedCell, new HashSet<>());

            Region region = new Region();
            region.value = nextUnvisitedCell.value;
            region.cells = new ArrayList<>(cellsInRegion);
            region.perimeter = calculatePerimeterOfTheRegion(region);
            regionMap.put(nextUnvisitedCell.value, region);

            calculatePerimeterOfTheRegion(region);
            System.out.println("Region " + regionNum + "_" + nextUnvisitedCell.value + " size: " + cellsInRegion.size() + " perimeter: " + region.perimeter + " power: " + cellsInRegion.size() * region.perimeter);
            totalPower += cellsInRegion.size() * region.perimeter;
            regionNum++;
            nextUnvisitedCell = pickNextUnvisitedCell(inputAsCell);
        }
        System.out.println("Total power: " + totalPower);
    }

    private static int calculateSidesOfTheRegion(Region region) {
        int sidesCount = 0;

        //check by lines, top-down
        for (int y = 0; y < cellsMap.size(); y++) {
            ArrayList<Cell> perimiterCellsInLine = new ArrayList<>();
            for (int x = 0; x < cellsMap.get(0).size(); x++) {
                Cell cell = cellsMap.get(y).get(x);
                if (region.cells.contains(cell) && !isInnerCell(cell, region)) {
                    perimiterCellsInLine.add(cell);
                }
            }
            //check top
            List<Cell> cellWithFreeTop = perimiterCellsInLine.stream()
                    .filter(cell -> {
                        Cell upCell = getCellByDirection(cell, Direction.UP);
                        return upCell == null || !region.cells.contains(upCell);
                    }).toList();
            if (!cellWithFreeTop.isEmpty()) {
                sidesCount += 1;
                Cell prevCell = cellWithFreeTop.get(0);
                for (int i = 1; i < cellWithFreeTop.size(); i++) {
                    Cell cell = cellWithFreeTop.get(i);
                    if (Math.abs(cell.x - prevCell.x) != 1) {
                        sidesCount += 1;
                    }
                    prevCell = cell;
                }
            }

            //check down
            List<Cell> cellWithFreeDown = perimiterCellsInLine.stream()
                    .filter(cell -> {
                        Cell downCell = getCellByDirection(cell, Direction.DOWN);
                        return downCell == null || !region.cells.contains(downCell);
                    }).toList();
            if (!cellWithFreeDown.isEmpty()) {
                sidesCount += 1;
                Cell prevCell = cellWithFreeDown.get(0);
                for (int i = 1; i < cellWithFreeDown.size(); i++) {
                    Cell cell = cellWithFreeDown.get(i);
                    if (Math.abs(cell.x - prevCell.x) != 1) {
                        sidesCount += 1;
                    }
                    prevCell = cell;
                }
            }
        }

        //check by columns, left-right
        for (int x = 0; x < cellsMap.get(0).size(); x++) {
            ArrayList<Cell> perimiterCellsInRow = new ArrayList<>();
            for (int y = 0; y < cellsMap.size(); y++) {
                Cell cell = cellsMap.get(y).get(x);
                if (region.cells.contains(cell) && !isInnerCell(cell, region)) {
                    perimiterCellsInRow.add(cell);
                }
            }
            //check left
            List<Cell> cellWithFreeLeft = perimiterCellsInRow.stream()
                    .filter(cell -> {
                        Cell leftCell = getCellByDirection(cell, Direction.LEFT);
                        return leftCell == null || !region.cells.contains(leftCell);
                    }).toList();
            if (!cellWithFreeLeft.isEmpty()) {
                sidesCount += 1;
                Cell prevCell = cellWithFreeLeft.get(0);
                for (int i = 1; i < cellWithFreeLeft.size(); i++) {
                    Cell cell = cellWithFreeLeft.get(i);
                    if (Math.abs(cell.y - prevCell.y) != 1) {
                        sidesCount += 1;
                    }
                    prevCell = cell;
                }
            }

            //check down
            List<Cell> cellWithFreeRight = perimiterCellsInRow.stream()
                    .filter(cell -> {
                        Cell downCell = getCellByDirection(cell, Direction.RIGHT);
                        return downCell == null || !region.cells.contains(downCell);
                    }).toList();
            if (!cellWithFreeRight.isEmpty()) {
                sidesCount += 1;
                Cell prevCell = cellWithFreeRight.get(0);
                for (int i = 1; i < cellWithFreeRight.size(); i++) {
                    Cell cell = cellWithFreeRight.get(i);
                    if (Math.abs(cell.y - prevCell.y) != 1) {
                        sidesCount += 1;
                    }
                    prevCell = cell;
                }
            }
        }

        return sidesCount;
    }

    private static boolean isSameLine(Cell cell1, Cell cell2, Cell cell3) {
        return (cell1.x == cell2.x && cell2.x == cell3.x) || (cell1.y == cell2.y && cell2.y == cell3.y);
    }

    private static Cell getNextInPerimeter(Cell cell, Region region) {
        List<Direction> directionsList = Arrays.asList(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT);
        Cell prevElementInPerimeter = null;
        for (Direction direction : directionsList) {
            Cell cellByDirection = getCellByDirection(cell, direction);
            if (cellByDirection != null) {
                if (!cellByDirection.markedAsEdge && !isInnerCell(cellByDirection, region)) {
                    return cellByDirection;
                } else if (cellByDirection.markedAsEdge && cellByDirection.numberInPerimeter == cell.numberInPerimeter) {
                    prevElementInPerimeter = cellByDirection;
                }
            }
        }
        if (prevElementInPerimeter == null) {
            return null;
        } else {
            return getNextInPerimeter(prevElementInPerimeter, region);
        }
    }

    private static boolean isInnerCell(Cell cell, Region region) {
        List<Direction> directionsList = Arrays.asList(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT);
        for (Direction direction : directionsList) {
            Cell cellByDirection = getCellByDirection(cell, direction);
            if (!region.cells.contains(cellByDirection)) {
                return false;
            }
        }
        return true;
    }

    private static int calculatePerimeterOfTheRegion(Region region) {
        int perimeter = 0;
        for (Cell cell : region.cells) {
            List<Direction> directionsList = Arrays.asList(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT);
            int numberOfCellsFromTheSameRegion = 0;
            for (Direction direction : directionsList) {
                Cell cellByDirection = getCellByDirection(cell, direction);
                if (region.cells.contains(cellByDirection)) {
                    numberOfCellsFromTheSameRegion++;
                }
            }
            perimeter += 4 - numberOfCellsFromTheSameRegion;
        }

        return perimeter;
    }


    private static Cell getCellByDirection(Cell initialCell, Direction direction) {
        int x = initialCell.x;
        int y = initialCell.y;
        switch (direction) {
            case UP:
                y--;
                break;
            case DOWN:
                y++;
                break;
            case LEFT:
                x--;
                break;
            case RIGHT:
                x++;
                break;
        }
        if (x < 0 || y < 0 || x >= cellsMap.get(0).size() || y >= cellsMap.size()) {
            return null;
        }
        return cellsMap.get(y).get(x);
    }

    private static Set<Cell> coverRegion(Cell initialCell, Cell cellToCheck, Set<Cell> cellsInRegion) {
        List<Direction> directionsList = Arrays.asList(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT);
        if (cellToCheck == null || cellToCheck.visited || !cellToCheck.value.equals(initialCell.value)) {
            return cellsInRegion;
        } else {
            cellToCheck.visited = true;
            cellsInRegion.add(cellToCheck);
            for (Direction newDirection : directionsList) {
                cellsInRegion.addAll(coverRegion(initialCell, getCellByDirection(cellToCheck, newDirection), cellsInRegion));
            }
            return cellsInRegion;
        }
    }

    enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    private static Cell pickNextUnvisitedCell(List<List<Cell>> inputAsCell) {
        for (List<Cell> row : inputAsCell) {
            for (Cell cell : row) {
                if (!cell.visited) {
                    return cell;
                }
            }
        }
        return null;
    }

    private static List<List<Cell>> mapOfStringsIntoMapOfCells(List<List<String>> inputAsString) {
        List<List<Cell>> result = new ArrayList<>();
        for (int y = 0; y < inputAsString.size(); y++) {
            List<String> row = inputAsString.get(y);
            List<Cell> rowCell = new ArrayList<>();
            for (int x = 0; x < row.size(); x++) {
                String value = row.get(x);
                Cell cell = new Cell();
                cell.value = value;
                cell.x = x;
                cell.y = y;
                cell.visited = false;

                rowCell.add(cell);
            }
            result.add(rowCell);
        }
        return result;
    }


    private static class Cell {
        String value;
        boolean visited;
        boolean markedAsEdge;
        int numberInPerimeter;
        int x;
        int y;
    }

    private static class Region {
        String value;
        List<Cell> cells;
        int perimeter;
        int sides;

        public void addCell(Cell cell) {
            cells.add(cell);
        }
    }

    private static void printCharMap(List<List<String>> charMap) {
        for (List<String> row : charMap) {
            for (String c : row) {
                System.out.print(c);
            }
            System.out.println();
        }
    }

    private static void printCellMap(List<List<Cell>> cellMap) {
        for (List<Cell> row : cellMap) {
            for (Cell c : row) {
                System.out.print(c.value);
            }
            System.out.println();
        }
    }
}
