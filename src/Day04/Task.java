package Day04;

import utils.AdventReadUtils;

import java.util.ArrayList;
import java.util.List;

public class Task {
    public static void main(String[] args) {
        AdventReadUtils adventReadUtils = new AdventReadUtils();
        List<String> input = adventReadUtils.readDataAsString(AdventReadUtils.INPUT_TYPE.data, "04");
      // taskA(input);
        taskB(input);

    }

    record coord(int x, int y) {
    }

    private enum wayToWrite {
        horizontalStartLeft(new coord(0, 0), new coord(1, 0), new coord(2, 0), new coord(3, 0)),
        horizontalStartRight(new coord(0, 0), new coord(-1, 0), new coord(-2, 0), new coord(-3, 0)),
        verticalTopDown(new coord(0, 0), new coord(0, 1), new coord(0, 2), new coord(0, 3)),
        verticalDownTop(new coord(0, 0), new coord(0, -1), new coord(0, -2), new coord(0, -3)),
        diagonalStartLeftTop(new coord(0, 0), new coord(1, 1), new coord(2, 2), new coord(3, 3)),
        diagonalStartRightTop(new coord(0, 0), new coord(-1, 1), new coord(-2, 2), new coord(-3, 3)),
        DiagonalStartDownLeft(new coord(0, 0), new coord(1, -1), new coord(2, -2), new coord(3, -3)),
        DiagonalStartDownRight(new coord(0, 0), new coord(-1, -1), new coord(-2, -2), new coord(-3, -3));

        final coord letterX;
        final coord letterM;
        final coord letterA;
        final coord letterS;

        wayToWrite(coord letterX, coord letterM, coord letterA, coord letterS) {
            this.letterX = letterX;
            this.letterM = letterM;
            this.letterA = letterA;
            this.letterS = letterS;
        }
    }

    /**
     * M.M
     * .A.
     * S.S
     *
     * S.S
     * .A.
     * M.M
     *
     * M.S
     * .A.
     * M.S
     *
     * S.M
     * .A.
     * S.M
     */


    private enum wayToWriteMASMAS {
        topDown(new coord(0, 0), new coord(-1, -1), new coord(1, -1), new coord(-1, 1), new coord(1, 1)),
        downTop(new coord(0, 0), new coord(-1, 1), new coord(1, 1), new coord(-1, -1), new coord(1, -1)),
        leftToRight(new coord(0, 0), new coord(-1, -1), new coord(-1, 1), new coord(1, 1), new coord(1, -1)),
        rightToLeft(new coord(0, 0), new coord(1, -1), new coord(1, 1), new coord(-1, -1), new coord(-1, 1));

        final coord letterM1;
        final coord letterM2;
        final coord letterA;
        final coord letterS1;
        final coord letterS2;

        wayToWriteMASMAS(coord letterA, coord letterM1, coord letterM2,coord letterS1, coord letterS2) {
            this.letterM1 = letterM1;
            this.letterM2 = letterM2;
            this.letterA = letterA;
            this.letterS1 = letterS1;
            this.letterS2 = letterS2;
        }
    }

    private static void taskA(List<String> input) {
        int counter = 0;
        List<String> onlyResults = new ArrayList<>();
        for (int line = 0; line < input.size(); line++) {
            String oneLine = "";
            for (int column = 0; column < input.get(line).length(); column++) {
                boolean isElement = false;
                System.out.print(input.get(line).charAt(column));
                if (input.get(line).charAt(column) == 'X') {
                    for (wayToWrite way : wayToWrite.values()) {
                        if (checkWord(input, way, line, column)) {
                            counter++;
                            isElement = true;
                        //    System.out.println("Found word");
                        }
                    }
                }
                oneLine += isElement ? "X" : ".";
            }
            onlyResults.add(oneLine);
            System.out.println();

        }
        System.out.println();
        System.out.println("Counter: " + counter);
        System.out.println();
        for (String str : onlyResults) {
            System.out.println(str);
        }
    }

    private static void taskB(List<String> input) {
        int counter = 0;
        List<String> onlyResults = new ArrayList<>();
        for (int line = 0; line < input.size(); line++) {
            String oneLine = "";
            for (int column = 0; column < input.get(line).length(); column++) {
                boolean isElement = false;
                System.out.print(input.get(line).charAt(column));
                if (input.get(line).charAt(column) == 'A') {
                    for (wayToWriteMASMAS way : wayToWriteMASMAS.values()) {
                        if (checkMas(input, way, line, column)) {
                            counter++;
                            isElement = true;
                            //    System.out.println("Found word");
                        }
                    }
                }
                oneLine += isElement ? "A" : ".";
            }
            onlyResults.add(oneLine);
            System.out.println();

        }
        System.out.println();
        System.out.println("Counter: " + counter);
        System.out.println();
        for (String str : onlyResults) {
            System.out.println(str);
        }
    }

    private static boolean checkWord(List<String> input, wayToWrite way, int line, int column) {
        coord expectedM = new coord(column + way.letterM.x, line + way.letterM.y);
        coord expectedA = new coord(column + way.letterA.x, line + way.letterA.y);
        coord expectedS = new coord(column + way.letterS.x, line + way.letterS.y);

        try {
            if (input.get(expectedM.y).charAt(expectedM.x) == 'M' &&
                    input.get(expectedA.y).charAt(expectedA.x) == 'A' &&
                    input.get(expectedS.y).charAt(expectedS.x) == 'S') {
                return true;
            }
        } catch (Exception e) {
            return false;
        }

        return false;
    }

    private static boolean checkMas(List<String> input, wayToWriteMASMAS way, int line, int column) {
        coord expectedM1 = new coord(column + way.letterM1.x, line + way.letterM1.y);
        coord expectedM2 = new coord(column + way.letterM2.x, line + way.letterM2.y);
        coord expectedS = new coord(column + way.letterS1.x, line + way.letterS1.y);
        coord expectedS2 = new coord(column + way.letterS2.x, line + way.letterS2.y);


        try {
            if (input.get(expectedM1.y).charAt(expectedM1.x) == 'M' &&
                    input.get(expectedM2.y).charAt(expectedM2.x) == 'M' &&
                    input.get(expectedS.y).charAt(expectedS.x) == 'S' &&
                    input.get(expectedS2.y).charAt(expectedS2.x) == 'S') {
                return true;
            }
        } catch (Exception e) {
            return false;
        }

        return false;
    }


}
