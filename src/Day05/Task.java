package Day05;

import utils.AdventReadUtils;

import java.util.ArrayList;
import java.util.List;

public class Task {
    public static void main(String[] args) {
        AdventReadUtils adventReadUtils = new AdventReadUtils();
        List<String> input = adventReadUtils.readDataAsString(AdventReadUtils.INPUT_TYPE.test, "05");
        List<Order> orderList = new ArrayList<>();
        List<List<Integer>> inputList = new ArrayList<>();

        for (int i = 0; i < input.size(); i++) {
            if (input.get(i).contains("|")) {
                String[] splitted = input.get(i).split("\\|");
                orderList.add(new Order(Integer.parseInt(splitted[0]), Integer.parseInt(splitted[1])));
            } else if (input.get(i).contains(",")) {
                String[] splitted = input.get(i).split(",");
                List<Integer> oneLine = new ArrayList<>();
                for (String s : splitted) {
                    oneLine.add(Integer.parseInt(s));
                }
                inputList.add(oneLine);
            }

        }
        //taskA(orderList, inputList);
        taskB(orderList, inputList);

    }

    record Order(int left, int right) {
    }

    private static void taskB(List<Order> orders, List<List<Integer>> input) {
        long sum = 0;
        for (List<Integer> oneLine : input) {
            boolean lineIsCorrect = true;
            for (int i = 0; i < oneLine.size() && lineIsCorrect; i++) {
                int number = oneLine.get(i);
                for (Order order : orders) {
                    if (number == order.left) {
                        if (!validateLeftPart(oneLine, i, order)) {
                            //    System.out.println("NO: " + oneLine + " " + order);
                            lineIsCorrect = false;
                            break;
                        }
                    }
                }
            }
            if (!lineIsCorrect) {
                System.out.println("before: " + oneLine);

                for (int i = 0; i < oneLine.size(); i++) {
                    int number = oneLine.get(i);
                    for (Order order : orders) {
                        if (number == order.left) {
                            if (!validateLeftPart(oneLine, i, order)) {
                                List<Integer> newLine = new ArrayList<>();
                                int indexOfRight = oneLine.indexOf(order.right);
                                newLine.addAll(oneLine.subList(0, indexOfRight));
                                newLine.add(order.left);
                                newLine.add(order.right);
                                oneLine = oneLine.stream().filter(e -> e != order.left && e != order.right).toList();
                                newLine.addAll(oneLine.subList(indexOfRight, oneLine.size()));
                                oneLine = newLine;
                                i = 0;
                                break;
                            }
                        }
                    }
                }

                System.out.println("after: " + oneLine);
                System.out.println(oneLine.get((oneLine.size() - 1) / 2));
                sum += oneLine.get((oneLine.size() - 1) / 2);
            }
        }
        System.out.println("result: " + sum);
    }

    private static void taskA(List<Order> orders, List<List<Integer>> input) {
        long sum = 0;
        for (List<Integer> oneLine : input) {
            boolean lineIsCorrect = true;
            for (int i = 0; i < oneLine.size() && lineIsCorrect; i++) {
                int number = oneLine.get(i);
                for (Order order : orders) {
                    if (number == order.left) {
                        if (!validateLeftPart(oneLine, i, order)) {
                            //    System.out.println("NO: " + oneLine + " " + order);
                            lineIsCorrect = false;
                            break;
                        }
                    }
                }
            }
            if (lineIsCorrect) {
                System.out.println(oneLine.get((oneLine.size() - 1) / 2));
                sum += oneLine.get((oneLine.size() - 1) / 2);
            }
        }
        System.out.println("result: " + sum);
    }

    private static boolean validateLeftPart(List<Integer> oneLine, int index, Order order) {
        int rightNumber = order.right;
        for (int i = 0; i < index; i++) {
            if (oneLine.get(i) == rightNumber) {
                return false;
            }
        }
        return true;
    }

}
