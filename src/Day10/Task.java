package Day10;

import utils.AdventReadUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Task {
    public static void main(String[] args) {
        AdventReadUtils adventReadUtils = new AdventReadUtils();
        List<List<Integer>> input = adventReadUtils.readDataOneDigitOneInt(AdventReadUtils.INPUT_TYPE.data, "10");
        printMap(input);
        taskA(input);
        // taskB(input);
    }

    public static List<List<Coord>> coordMap = new ArrayList<>();
    public static int counter = 0;

    private static void taskA(List<List<Integer>> input) {
        List<Coord> coordsOfZeros = new ArrayList<>();
        for (int y = 0; y < input.size(); y++) {
            List<Coord> line = new ArrayList<>();
            for (int x = 0; x < input.get(y).size(); x++) {
                line.add(new Coord(x, y, input.get(y).get(x)));
                if (input.get(y).get(x) == 0) {
                    coordsOfZeros.add(new Coord(x, y, 0));
                    System.out.println("x=" + x + " y=" + y);
                }
            }
            coordMap.add(line);
        }

        for (int y = 0; y < input.size(); y++) {
            for (int x = 0; x < input.get(y).size(); x++) {
                if (input.get(y).get(x) == 0) {
                    getCoordOf9(coordMap.get(y).get(x), 0);
                    refreshVisitedMap();
                    System.out.println(counter);
                }
            }
        }

        System.out.println("total counter: " + counter);

    }

    private static void refreshVisitedMap() {
        for (List<Coord> coordList : coordMap) {
            for (Coord coord : coordList) {
                coord.visited = false;
            }
        }

    }

    public static class Coord {
        int x;
        int y;
        int value;
        boolean visited = false;

        public Coord(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Coord(int x, int y, int value) {
            this.x = x;
            this.y = y;
            this.value = value;
        }
    }

    private static Optional<Coord> getValueOptional(Coord point) {
        if (point.y < 0 || point.y >= coordMap.size()) {
            return Optional.empty();
        }
        if (point.x < 0 || point.x >= coordMap.get(point.y).size()) {
            return Optional.empty();
        }
        return Optional.of(coordMap.get(point.y).get(point.x));
    }


    private static void getCoordOf9(Coord start, int currentValue) {
        Optional<Coord> up = getValueOptional(new Coord(start.x, start.y - 1));
        Optional<Coord> down = getValueOptional(new Coord(start.x, start.y + 1));
        Optional<Coord> right = getValueOptional(new Coord(start.x - 1, start.y));
        Optional<Coord> left = getValueOptional(new Coord(start.x + 1, start.y));

        List<Optional<Coord>> valuesToCheck = new ArrayList<>();
        valuesToCheck.add(up);
        valuesToCheck.add(down);
        valuesToCheck.add(left);
        valuesToCheck.add(right);

        for (Coord coord : valuesToCheck.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(c -> !c.visited)
                .toList()) {
            if (coord.value == currentValue + 1) {
              //  coord.visited = true;
                if (coord.value == 9) {
                    counter++;
                } else {
                    getCoordOf9(coord, coord.value);
                }
            }
        }
    }


    private static void printMap(List<List<Integer>> map) {
        for (List<Integer> line : map) {
            for (Integer value : line) {
                System.out.print(value + "");
            }
            System.out.println();
        }
        System.out.println();
    }


}
