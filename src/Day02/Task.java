package Day02;

import utils.AdventReadUtils;

import java.util.ArrayList;
import java.util.List;

public class Task {
    public static void main(String[] args) {
        AdventReadUtils adventReadUtils = new AdventReadUtils();
        List<List<Integer>> input = adventReadUtils.readData(AdventReadUtils.INPUT_TYPE.data, "02");
        taskB(input);
    }

    private static void taskA(List<List<Integer>> input) {
        int validRowsCounter = 0;

        for (List<Integer> row : input) {
            int first = row.get(0);
            int second = row.get(2);
            boolean isIncreasing = second - first > 0;

            boolean validLine = true;
            for (int i = 1; i < row.size(); i++) {
                int previous = row.get(i - 1);
                int current = row.get(i);
                int diff = current - previous;
                if (!isIncreasing) {
                    diff = diff * -1;
                }
                if (diff < 1 || diff > 3) {
                    validLine = false;
                    break;
                }
            }
            if (validLine) {
                validRowsCounter++;
            }

        }
        System.out.println(validRowsCounter);
    }

    private static void taskB(List<List<Integer>> input) {
        int validRowsCounter = 0;

        for (List<Integer> row : input) {
            if (checkRow(row)) {
                validRowsCounter++;
            } else {
                for (int i = 0; i < row.size(); i++) {
                    List<Integer> allExceptI = new ArrayList<>();
                    allExceptI.addAll(row);
                    allExceptI.remove(i);
                    if (checkRow(allExceptI)) {
                        validRowsCounter++;
                        break;
                    }
                }
            }
        }
        System.out.println(validRowsCounter);
    }

    private static boolean checkRow(List<Integer> row) {
        int first = row.get(0);
        int second = row.get(2);
        boolean isIncreasing = second - first > 0;

        boolean validLine = true;
        for (int i = 1; i < row.size(); i++) {
            int previous = row.get(i - 1);
            int current = row.get(i);
            int diff = current - previous;
            if (!isIncreasing) {
                diff = diff * -1;
            }
            if (diff < 1 || diff > 3) {
                validLine = false;
                break;
            }
        }

        return validLine;
    }
}
