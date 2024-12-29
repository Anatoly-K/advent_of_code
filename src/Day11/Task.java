package Day11;

import utils.AdventReadUtils;

import java.util.*;

public class Task {
    public static void main(String[] args) {
        AdventReadUtils adventReadUtils = new AdventReadUtils();
        String inputAsString = adventReadUtils.readDataAsString(AdventReadUtils.INPUT_TYPE.data, "11").get(0);
        String[] inputAsStringArray = inputAsString.split(" ");
        List<Long> input = new ArrayList<>();
        for (String str : inputAsStringArray) {
            input.add(Long.parseLong(str));
        }

        System.out.println(input);
//        taskA(input);
//        for (int i = 1; i <= 40; i++) {
//            System.out.println("iteration..." + i);
//            input = blink(input, i);
//            uniqNumbers.addAll(input);
//            System.out.println("uniqNumbers.size() = " + uniqNumbers.size());
//            System.out.println("input.size() = " + input.size());
//            //   System.out.println(input);
//        }

        long result = 0;
        for (Long number : input) {
            result += blinkRecursive(number, 75);
        }

        System.out.println(result);
    }

    private static Map<NumberAndIteration, Long> numberAndIterationsToNumberOfStonesMap = new HashMap<>();

    record NumberAndIteration(long number, int iteration) {
    }

    static Long blinkRecursive(Long number, int iteration) {
        if (iteration == 0) {
            return 1L;
        }
        if (numberAndIterationsToNumberOfStonesMap.containsKey(new NumberAndIteration(number, iteration))) {
            return numberAndIterationsToNumberOfStonesMap.get(new NumberAndIteration(number, iteration));
        }
        if (number == 0) {
            long numberOfStones = blinkRecursive(1L, iteration - 1);
            numberAndIterationsToNumberOfStonesMap.put(new NumberAndIteration(number, iteration), numberOfStones);
            return numberOfStones;
        } else if ((number + "").length() % 2 == 0) {
            String numAsString = number + "";
            String firstHalf = numAsString.substring(0, numAsString.length() / 2);
            String secondHalf = numAsString.substring(numAsString.length() / 2);

            long numberOfStonesLeftPart = blinkRecursive(Long.parseLong(firstHalf), iteration - 1);
            numberAndIterationsToNumberOfStonesMap.put(new NumberAndIteration(Long.parseLong(firstHalf), iteration - 1), numberOfStonesLeftPart);

            long numberOfStonesRightPart = blinkRecursive(Long.parseLong(secondHalf), iteration - 1);
            numberAndIterationsToNumberOfStonesMap.put(new NumberAndIteration(Long.parseLong(secondHalf), iteration - 1), numberOfStonesRightPart);

            numberAndIterationsToNumberOfStonesMap.put(new NumberAndIteration(number, iteration), numberOfStonesLeftPart + numberOfStonesRightPart);
            return numberOfStonesLeftPart + numberOfStonesRightPart;
        } else {
            long numberOfStones = blinkRecursive(number * 2024, iteration - 1);
            numberAndIterationsToNumberOfStonesMap.put(new NumberAndIteration(number, iteration), numberOfStones);
            return numberOfStones;
        }
    }

    static List<Long> blink(List<Long> input, int iteration) {
        List<Long> newInput = new ArrayList<>();
        for (Long i : input) {
            if (i == 0) {
                newInput.add(1L);
            } else if ((i + "").length() % 2 == 0) {
                String numAsString = i + "";
                String firstHalf = numAsString.substring(0, numAsString.length() / 2);
                String secondHalf = numAsString.substring(numAsString.length() / 2);
                newInput.add(Long.parseLong(firstHalf));
                newInput.add(Long.parseLong(secondHalf));
            } else {
                newInput.add(i * 2024);
            }
        }
        return newInput;
    }

}
