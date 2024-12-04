package Day03;

import utils.AdventReadUtils;

import java.util.List;

public class Task {
    public static void main(String[] args) {
        AdventReadUtils adventReadUtils = new AdventReadUtils();
        List<String> input = adventReadUtils.readDataAsString(AdventReadUtils.INPUT_TYPE.data, "03");
        taskA(input);
    }

    private static void taskA(List<String> input) {
        long sum = 0;
        boolean sumEnabled = true;
        for (String str : input) {
            String subStrStartingMul = str;

            while (subStrStartingMul.contains("mul(")) {
                int mulIndex = subStrStartingMul.indexOf("mul(") == -1 ? Integer.MAX_VALUE : subStrStartingMul.indexOf("mul(");
                int doIndex = subStrStartingMul.indexOf("do()") == -1 ? Integer.MAX_VALUE : subStrStartingMul.indexOf("do()");
                int dontIndex = subStrStartingMul.indexOf("don't()") == -1 ? Integer.MAX_VALUE : subStrStartingMul.indexOf("don't()");

                if (doIndex < mulIndex && doIndex < dontIndex) {
                    subStrStartingMul = subStrStartingMul.substring(doIndex + 4);
                    sumEnabled = true;
                    continue;
                }
                if (dontIndex < mulIndex && dontIndex < doIndex) {
                    subStrStartingMul = subStrStartingMul.substring(dontIndex + 7);
                    sumEnabled = false;
                    continue;
                }
                if (mulIndex < doIndex && mulIndex < dontIndex) {
                    subStrStartingMul = subStrStartingMul.substring(mulIndex + 4);
                    if (!sumEnabled) {
                        continue;
                    }
                }

                int firstNum = getNextNumber(subStrStartingMul);
                //   System.out.println("first: " + firstNum);
                if (firstNum == 0) {
                    continue;
                }
                int numLength = (firstNum + "").length();
                subStrStartingMul = subStrStartingMul.substring(numLength);
                //   System.out.println(subStrStartingMul);
                if (subStrStartingMul.charAt(0) != ',') {
                    continue;
                }
                subStrStartingMul = subStrStartingMul.substring(1);
                int secondNum = getNextNumber(subStrStartingMul);
                //  System.out.println("Second: " + secondNum);
                if (secondNum == 0) {
                    continue;
                }
                numLength = (secondNum + "").length();
                subStrStartingMul = subStrStartingMul.substring(numLength);
                if (subStrStartingMul.charAt(0) != ')') {
                    continue;
                }
                System.out.println(firstNum + " * " + secondNum);
                sum += firstNum * secondNum;
            }
        }
        System.out.println("sum:" + sum);
    }

    private static int getNextNumber(String str) {
        String numAsString = "";
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                numAsString += str.charAt(i);
            } else {
                break;
            }
        }
        if (numAsString.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(numAsString);
    }
}
