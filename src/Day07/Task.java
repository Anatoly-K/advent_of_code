package Day07;

import utils.AdventReadUtils;

import java.util.*;

public class Task {
    public static void main(String[] args) {
        AdventReadUtils adventReadUtils = new AdventReadUtils();
        List<String> input = adventReadUtils.readDataAsString(AdventReadUtils.INPUT_TYPE.data, "07");
        // taskA(input);
        taskB(input);
    }

    private static void taskA(List<String> input) {
        long resultsum = 0;
        for (String line : input) {
            long sum = Long.parseLong(line.substring(0, line.indexOf(':')));
            String[] numbersAsStr = line.substring(line.indexOf(':') + 2).split(" ");
            List<Long> numbers = Arrays.stream(numbersAsStr).map(Long::parseLong).toList();
            System.out.println(sum + " = " + numbers);
            long currentLineSum = numbers.get(0);
            long differentMath = tryDifferentMath(numbers.subList(1, numbers.size()), currentLineSum, sum);
            if (differentMath == sum) {
                resultsum += sum;
            }
        }
        System.out.println(resultsum);
    }

    private static long tryDifferentMath(List<Long> numbers, long currentValue, long goalSum) {
        if (numbers.isEmpty()) {
            return currentValue;
        } else {
            long currentLineSum = numbers.get(0);
            if (currentValue > goalSum) {
                return -1;
            }
            long sumWay = tryDifferentMath(numbers.subList(1, numbers.size()), currentLineSum + currentValue, goalSum);
            if (sumWay == goalSum) {
                return sumWay;
            }
            long multWay = tryDifferentMath(numbers.subList(1, numbers.size()), currentLineSum * currentValue, goalSum);
            if (multWay == goalSum) {
                return multWay;
            }
        }
        return -1;
    }

    private static void taskB(List<String> input) {
        long resultsum = 0;
        for (String line : input) {
            long sum = Long.parseLong(line.substring(0, line.indexOf(':')));
            String[] numbersAsStr = line.substring(line.indexOf(':') + 2).split(" ");
            List<Long> numbers = Arrays.stream(numbersAsStr).map(Long::parseLong).toList();
            System.out.print(sum + " = " + numbers);
            long currentLineSum = numbers.get(0);
            long differentMath = tryDifferentComplexMath(numbers.subList(1, numbers.size()), currentLineSum, sum);
            if (differentMath == sum) {
                resultsum += sum;
                System.out.print(" correct ");
            }
            System.out.println();
        }
        System.out.println(resultsum);
    }

    private static long tryDifferentComplexMath(List<Long> numbers, long currentValue, long goalSum) {
        if (numbers.isEmpty()) {
            return currentValue;
        } else {
            long nextNumber = numbers.get(0);
            if (currentValue > goalSum) {
                return -1;
            }
            long sumWay = tryDifferentComplexMath(numbers.subList(1, numbers.size()), nextNumber + currentValue, goalSum);
            if (sumWay == goalSum) {
                return sumWay;
            }
            long multWay = tryDifferentComplexMath(numbers.subList(1, numbers.size()), nextNumber * currentValue, goalSum);
            if (multWay == goalSum) {
                return multWay;
            }
            String concat = currentValue + String.valueOf(nextNumber);
            long concatWay = tryDifferentComplexMath(numbers.subList(1, numbers.size()), Long.parseLong(concat), goalSum);
            if (concatWay == goalSum) {
                return concatWay;
            }
        }
        return -1;
    }

}
