package Day06;

import utils.AdventReadUtils;

import java.util.*;

public class Task {
    public static void main(String[] args) {
        AdventReadUtils adventReadUtils = new AdventReadUtils();
        List<String> input = adventReadUtils.readDataAsString(AdventReadUtils.INPUT_TYPE.data, "06");
        // taskA(input);
        taskB(input);
    }

    enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    record Coord(int x, int y) {
    }

    private static class Point {
        boolean isObstacle = false;
        boolean isVisited = false;
        Map<Direction, Boolean> visitedDirectionMap = new HashMap<>();

        public Point(boolean isObstacle) {
            this.isObstacle = isObstacle;
        }

        public Point getClone() {
            Point point = new Point(this.isObstacle);
            point.isVisited = this.isVisited;
            point.visitedDirectionMap = new HashMap<>(this.visitedDirectionMap);
            return point;
        }
    }

    private static List<List<Point>> parseMap(List<String> input) {
        List<List<Point>> pointsMap = new ArrayList<>();
        for (String s : input) {
            List<Point> pointLine = new ArrayList<>();
            for (char c : s.toCharArray()) {
                Point point = new Point('#' == c);
                pointLine.add(point);

            }
            pointsMap.add(pointLine);
        }
        return pointsMap;
    }

    private static Coord getStartPoint(List<String> input) {
        int pointerX = 0;
        int pointerY = 0;
        for (int y = 0; y < input.size(); y++) {
            String line = input.get(y);
            for (char c : line.toCharArray()) {
                if (c == '^') {
                    pointerX = line.indexOf('^');
                    pointerY = y;
                    return new Coord(pointerX, pointerY);
                }
            }
        }
        return new Coord(pointerX, pointerY);
    }

    private static void taskA(List<String> input) {
        List<List<Point>> pointsMap = parseMap(input);
        Coord startPoint = getStartPoint(input);
        System.out.println("Original map: ");
        printMap(pointsMap);
        System.out.println();

        Direction currentDirection = Direction.UP;
        int newPointerX = startPoint.x;
        int newPointerY = startPoint.y;
        int pointerX = startPoint.x;
        int pointerY = startPoint.y;
        int visitedCounter = 0;
        Point currentPoint = pointsMap.get(newPointerX).get(newPointerY);
        Point nextPoint;
        try {
            while (true) {
                newPointerY = pointerY;
                newPointerX = pointerX;
                if (!currentPoint.isVisited) {
                    visitedCounter++;
                    currentPoint.isVisited = true;
                }
                switch (currentDirection) {
                    case UP -> newPointerY--;
                    case DOWN -> newPointerY++;
                    case LEFT -> newPointerX--;
                    case RIGHT -> newPointerX++;
                }
                nextPoint = pointsMap.get(newPointerY).get(newPointerX);
                if (nextPoint.isObstacle) {
                    switch (currentDirection) {
                        case UP -> currentDirection = Direction.RIGHT;
                        case RIGHT -> currentDirection = Direction.DOWN;
                        case DOWN -> currentDirection = Direction.LEFT;
                        case LEFT -> currentDirection = Direction.UP;
                    }
                    continue;
                }
                pointerX = newPointerX;
                pointerY = newPointerY;
                currentPoint = pointsMap.get(pointerY).get(pointerX);
                // printMap(pointsMap);
                // System.out.println();
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("visited counter: " + visitedCounter);
        }
    }

    private static boolean checkRay(List<List<Point>> pointMap, Coord position, Direction direction, Coord obstaclePosition) {
        List<List<Point>> mapCopy = new ArrayList<>();
        for (List<Point> line : pointMap) {
            List<Point> lineCopy = new ArrayList<>();
            for (Point point : line) {
                lineCopy.add(point.getClone());
            }
            mapCopy.add(lineCopy);
        }
        if (obstaclePosition != null) {
            mapCopy.get(obstaclePosition.y).get(obstaclePosition.x).isObstacle = true;
        }

        int x = position.x;
        int y = position.y;
        int oldX = x;
        int oldY = y;
        try {
            while (true) {
                oldX = x;
                oldY = y;
                switch (direction) {
                    case UP -> y--;
                    case DOWN -> y++;
                    case LEFT -> x--;
                    case RIGHT -> x++;
                }
                if (mapCopy.get(y).get(x).isObstacle) {
                    return checkRay(mapCopy, new Coord(oldX, oldY), getDirectionAfterObstacle(direction), null);
                } else if (mapCopy.get(y).get(x).visitedDirectionMap.get(direction) == Boolean.TRUE) {
                    return true;
                } else {
                    mapCopy.get(y).get(x).visitedDirectionMap.put(direction, true);
                }
            }
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    //1728 - too high

    private static Coord movePointer(Coord pointer, Direction direction) {
        int x = pointer.x;
        int y = pointer.y;
        switch (direction) {
            case UP -> y--;
            case DOWN -> y++;
            case LEFT -> x--;
            case RIGHT -> x++;
        }
        return new Coord(x, y);
    }

    private static void taskB(List<String> input) {
        List<List<Point>> pointsMap = parseMap(input);
        Coord startPoint = getStartPoint(input);

        Set<Coord> potentialObstacles = new HashSet<>();

        Direction currentDirection = Direction.UP;
        Coord newPosition;
        Coord currentPosition = new Coord(startPoint.x, startPoint.y);
        Point currentPoint;
        Point nextPoint;
        int counter = 0;
        try {
            while (true) {
                //mark point as visited
                currentPoint = pointsMap.get(currentPosition.y).get(currentPosition.x);
                currentPoint.visitedDirectionMap.put(currentDirection, true);

                //check position for potential obstacle
                Coord potentialObstacle = movePointer(currentPosition, currentDirection);
                if (!potentialObstacle.equals(startPoint) && //not start point
                        pointsMap.get(potentialObstacle.y).get(potentialObstacle.x).visitedDirectionMap.isEmpty() && //not visited
                        !pointsMap.get(potentialObstacle.y).get(potentialObstacle.x).isObstacle && //not obstacle
                        !potentialObstacles.contains(potentialObstacle)) { //not already checked
                    if (currentPoint.visitedDirectionMap.get(getDirectionAfterObstacle(currentDirection)) == Boolean.TRUE) {
                        counter++;
                        System.out.println(counter);
                        potentialObstacles.add(potentialObstacle);
                    } else {
                        boolean isCycleForRay = checkRay(pointsMap, currentPosition, getDirectionAfterObstacle(currentDirection), potentialObstacle);
                        if (isCycleForRay) {
                            counter++;
                            potentialObstacles.add(potentialObstacle);
                            System.out.println(counter);
                        }
                    }
                }
                newPosition = movePointer(currentPosition, currentDirection);
                nextPoint = pointsMap.get(newPosition.y).get(newPosition.x);

                if (nextPoint.isObstacle) {
                    currentDirection = getDirectionAfterObstacle(currentDirection);
                } else {
                    currentPosition = newPosition;
                }
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("visited counter: " + counter);
            System.out.println("Potential obstacles: " + potentialObstacles.size());
        }
    }

    private static Direction getDirectionAfterObstacle(Direction oldDirection) {
        switch (oldDirection) {
            case UP -> {
                return Direction.RIGHT;
            }
            case RIGHT -> {
                return Direction.DOWN;
            }
            case DOWN -> {
                return Direction.LEFT;
            }
            case LEFT -> {
                return Direction.UP;
            }
        }
        throw new UnsupportedOperationException();
    }

    private static void printMap(List<List<Point>> map) {
        for (List<Point> line : map) {
            for (Point point : line) {
                if (point.isObstacle) {
                    System.out.print("#");
                } else if (!point.visitedDirectionMap.isEmpty()) {
                    System.out.print("X");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}
