package Day08;

import utils.AdventReadUtils;

import java.util.*;
import java.util.stream.Collectors;

public class Task {
    public static void main(String[] args) {
        AdventReadUtils adventReadUtils = new AdventReadUtils();
        List<String> input = adventReadUtils.readDataAsString(AdventReadUtils.INPUT_TYPE.data, "08");
        List<List<Point>> inputMap = convertToMap(input);
        printMap(inputMap);
        System.out.println();
        // taskA(inputMap);
        taskB(inputMap);
    }

    private static void printMap(List<List<Point>> pointMap) {
        for (List<Point> pointLine : pointMap) {
            for (Point point : pointLine) {
//                if (!point.isEmpty && point.isAntiNode) {
//                    System.out.print("!");
//                } else
                    if (!point.isEmpty) {
                    System.out.print(point.label);
                } else if (point.isAntiNode) {
                    System.out.print("#");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private static List<List<Point>> convertToMap(List<String> input) {
        List<List<Point>> map = new ArrayList<>();
        for (int y = 0; y < input.size(); y++) {
            String s = input.get(y);
            List<Point> pointLine = new ArrayList<>();
            for (int x = 0; x < s.length(); x++) {
                char c = s.charAt(x);
                Point point = new Point(new Coord(x, y), c == '.', String.valueOf(c));
                pointLine.add(point);
            }
            map.add(pointLine);
        }
        return map;
    }

    record Coord(int x, int y) {
    }

    static class Point {
        Coord coord;
        boolean isEmpty;
        boolean isAntiNode;
        String label;

        public Point(Coord coord, boolean isEmpty, String label) {
            this.coord = coord;
            this.isEmpty = isEmpty;
            this.label = label;
        }
    }

    private static void taskA(List<List<Point>> input) {
        Map<String, List<Point>> byLabels = input.stream()
                .flatMap(List::stream)
                .filter(x -> !x.isEmpty)
                .collect(Collectors.groupingBy(x -> x.label));
        Set<Coord> potentialAntiNodes = new HashSet<>();
        for (Map.Entry<String, List<Point>> entry : byLabels.entrySet()) {
            for (int i = 0; i < entry.getValue().size() - 1; i++) {
                for (int j = i + 1; j < entry.getValue().size(); j++) {
                    Point point1 = entry.getValue().get(i);
                    Point point2 = entry.getValue().get(j);
                    int xDiff = point1.coord.x - point2.coord.x;
                    int yDiff = point1.coord.y - point2.coord.y;

                    int antinode1X = point1.coord.x + xDiff;
                    int antinode1Y = point1.coord.y + yDiff;
                    int antinode2X = point2.coord.x - xDiff;
                    int antinode2Y = point2.coord.y - yDiff;

                    if (antinode1X >= 0 && antinode1Y >= 0 && antinode1X < input.get(0).size() && antinode1Y < input.size()) {
                        potentialAntiNodes.add(new Coord(antinode1X, antinode1Y));
                        input.get(antinode1Y).get(antinode1X).isAntiNode = true;
                    }
                    if (antinode2X >= 0 && antinode2Y >= 0 && antinode2X < input.get(0).size() && antinode2Y < input.size()) {
                        potentialAntiNodes.add(new Coord(antinode2X, antinode2Y));
                        input.get(antinode2Y).get(antinode2X).isAntiNode = true;
                    }

                    printMap(input);
                }

            }

        }
        System.out.println(potentialAntiNodes.size());
    }

    private static void taskB(List<List<Point>> input) {
        Map<String, List<Point>> byLabels = input.stream()
                .flatMap(List::stream)
                .filter(x -> !x.isEmpty)
                .collect(Collectors.groupingBy(x -> x.label));
        Set<Coord> potentialAntiNodes = new HashSet<>();
        for (Map.Entry<String, List<Point>> entry : byLabels.entrySet()) {
            for (int i = 0; i < entry.getValue().size() - 1; i++) {
                for (int j = i + 1; j < entry.getValue().size(); j++) {
                    Point point1 = entry.getValue().get(i);
                    Point point2 = entry.getValue().get(j);
                    int xDiff = point1.coord.x - point2.coord.x;
                    int yDiff = point1.coord.y - point2.coord.y;

                    int antinode1X = point1.coord.x;
                    int antinode1Y = point1.coord.y;
                    int antinode2X = point2.coord.x;
                    int antinode2Y = point2.coord.y;

                    potentialAntiNodes.add(new Coord(antinode1X, antinode1Y));
                    potentialAntiNodes.add(new Coord(antinode2X, antinode2Y));


                    while (antinode1X >= 0 && antinode1Y >= 0 && antinode1X < input.get(0).size() && antinode1Y < input.size()) {
                        antinode1X += xDiff;
                        antinode1Y += yDiff;
                        if (antinode1X >= 0 && antinode1Y >= 0 && antinode1X < input.get(0).size() && antinode1Y < input.size()) {
                            potentialAntiNodes.add(new Coord(antinode1X, antinode1Y));
                            input.get(antinode1Y).get(antinode1X).isAntiNode = true;
                        }
                    }

                    while (antinode2X >= 0 && antinode2Y >= 0 && antinode2X < input.get(0).size() && antinode2Y < input.size()) {
                        antinode2X -= xDiff;
                        antinode2Y -= yDiff;
                        if (antinode2X >= 0 && antinode2Y >= 0 && antinode2X < input.get(0).size() && antinode2Y < input.size()) {
                            potentialAntiNodes.add(new Coord(antinode2X, antinode2Y));
                            input.get(antinode2Y).get(antinode2X).isAntiNode = true;
                        }
                    }

                    //    printMap(input);
                }

            }
        }
     //   printMap(input);
        System.out.println(potentialAntiNodes.size());
    }


}
