package Day09;

import utils.AdventReadUtils;

import java.util.*;
import java.util.stream.Collectors;

public class Task {
    public static void main(String[] args) {
        AdventReadUtils adventReadUtils = new AdventReadUtils();
        String input = adventReadUtils.readDataAsString(AdventReadUtils.INPUT_TYPE.data, "09").get(0);
        System.out.println(input);
        //taskA(input);
        taskB(input);
    }

    private static class PlaceInMemory {
        int startIndex;
        int finishIndex;

        public int length() {
            return finishIndex - startIndex + 1;
        }

        public PlaceInMemory(int startIndex, int finishIndex) {
            this.startIndex = startIndex;
            this.finishIndex = finishIndex;
        }
    }

    private static void taskB(String input) {
        List<Long> numbers = new ArrayList<>();
        List<PlaceInMemory> memorySpaces = new ArrayList<>();
        List<PlaceInMemory> memoryNumbers = new ArrayList<>();
        long idNumber = 0;
        for (int i = 0; i < input.length(); i++) {
            int originalStringNumber = Integer.parseInt(input.charAt(i) + "");
            if (i % 2 != 0) {
                for (int j = 0; j < originalStringNumber; j++) {
                    numbers.add(null);
                }
                if (originalStringNumber > 0) {
                    memorySpaces.add(new PlaceInMemory(numbers.size() - originalStringNumber, numbers.size() - 1));
                }
            } else {
                for (int j = 0; j < originalStringNumber; j++) {
                    numbers.add(idNumber);
                }
                memoryNumbers.add(new PlaceInMemory(numbers.size() - originalStringNumber, numbers.size() - 1));
                idNumber++;
            }

        }
        // printListWithDot(numbers);

        for (int j = memoryNumbers.size() - 1; j >= 0; j--) {
            PlaceInMemory memoryNumber = memoryNumbers.get(j);
            int memoryNumberLength = memoryNumber.length();
            for (PlaceInMemory memorySpace : memorySpaces) {
                if (memorySpace.length() >= memoryNumberLength && memorySpace.startIndex < memoryNumber.startIndex) {
                    for (int i = 0; i < memoryNumberLength; i++) {
                        numbers.set(memorySpace.startIndex + i, numbers.get(memoryNumber.startIndex + i));
                        numbers.set(memoryNumber.startIndex + i, null);
                    }
                    memorySpace.startIndex += memoryNumberLength;
                    break;
                }
            }
        }

        long sum = 0;
        for (int i = 0; i < numbers.size(); i++) {
            if (numbers.get(i) != null) {
                sum += numbers.get(i) * i;
            }
        }
        System.out.println(sum);
    }

    private static void taskA(String input) {
        //convert String into String with numbers and dots
        List<Long> numbers = new ArrayList<>();
        long idNumber = 0;
        for (int i = 0; i < input.length(); i++) {
            int originalStringNumber = Integer.parseInt(input.charAt(i) + "");
            if (i % 2 != 0) {
                for (int j = 0; j < originalStringNumber; j++) {
                    numbers.add(null);
                }
            } else {
                for (int j = 0; j < originalStringNumber; j++) {
                    numbers.add(idNumber);
                }
                idNumber++;
            }
        }
        printListWithDot(numbers);

        int startIndex = 0;
        int finishIndex = numbers.size() - 1;
        while (startIndex < finishIndex) {
            while (numbers.get(startIndex) != null && startIndex < finishIndex) {
                startIndex++;
            }
            while (numbers.get(finishIndex) == null && startIndex < finishIndex) {
                finishIndex--;
            }
            numbers.set(startIndex, numbers.get(finishIndex));
            numbers.set(finishIndex, null);
            //    printListWithDot(numbers);
        }

        long sum = 0;
        for (int i = 0; i < numbers.size(); i++) {
            if (numbers.get(i) == null) {
                break;
            } else {
                sum += numbers.get(i) * i;
            }
        }
        System.out.println(sum);
    }

    private static void printListWithDot(List<Long> numbers) {
        for (int i = 0; i < numbers.size(); i++) {
            if (numbers.get(i) == null) {
                System.out.print(".");
            } else {
                System.out.print(numbers.get(i));
            }
        }
        System.out.println();
    }

    private static String listWithDotToString(List<Long> numbers) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numbers.size(); i++) {
            if (numbers.get(i) == null) {
                sb.append(".");
            } else {
                sb.append(numbers.get(i));
            }
        }
        return sb.toString();
    }

}
