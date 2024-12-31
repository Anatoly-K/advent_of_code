package Day13;

import utils.AdventReadUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Task {

    public static void main(String[] args) {
        AdventReadUtils adventReadUtils = new AdventReadUtils();
        List<String> inputAsListOfStrings = adventReadUtils.readDataAsString(AdventReadUtils.INPUT_TYPE.data, "13");
        //  printCharMap(inputAsString);
        System.out.println(inputAsListOfStrings);
        List<Equation> equations = convertStringsToEquations(inputAsListOfStrings);
        long tokens = 0;
        for (Equation equation : equations) {
           // System.out.println(equation);
            Solution solution = solveEquation(equation);
            if (solution != null) {
                tokens += solution.x * 3 + solution.y;
            }
           // System.out.println(solution);
            //1228360646084 - too low
            //79352015273424
        }

        System.out.println(tokens);
    }

    private static Solution solveEquation(Equation equation) {
        double y = ((double) (equation.result_Y * equation.buttonA_X - equation.result_X * equation.buttonA_Y)) /
                ((double) (equation.buttonA_X * equation.buttonB_Y - equation.buttonA_Y * equation.buttonB_X));
        if (y % 1 != 0 || y < 0) {
            return null;
        }
        double x = ((double) equation.result_X - equation.buttonB_X * y) / equation.buttonA_X;
        if (x % 1 != 0 || x < 0) {
            return null;
        }
        return new Solution((long) x, (long) y);
    }

    private static List<Equation> convertStringsToEquations(List<String> input) {
        List<Equation> equations = new ArrayList<>();
        Pattern patternA = Pattern.compile("Button A: X\\+(\\d+), Y\\+(\\d+)");
        Pattern patternB = Pattern.compile("Button B: X\\+(\\d+), Y\\+(\\d+)");
        Pattern patternResult = Pattern.compile("Prize: X=(\\d+), Y=(\\d+)");

        for (int i = 0; i < input.size(); ) {
            Equation equation = new Equation();
            String eqA = input.get(i);
            Matcher matcherA = patternA.matcher(eqA);
            if (matcherA.find()) {
                equation.buttonA_X = Integer.parseInt(matcherA.group(1));
                equation.buttonA_Y = Integer.parseInt(matcherA.group(2));
            }
            String eqB = input.get(i + 1);
            Matcher matcherB = patternB.matcher(eqB);
            if (matcherB.find()) {
                equation.buttonB_X = Integer.parseInt(matcherB.group(1));
                equation.buttonB_Y = Integer.parseInt(matcherB.group(2));
            }
            String result = input.get(i + 2);
            Matcher matcherResult = patternResult.matcher(result);
            if (matcherResult.find()) {
                equation.result_X = 10000000000000L + Integer.parseInt(matcherResult.group(1));
                equation.result_Y = 10000000000000L + Integer.parseInt(matcherResult.group(2));
            }
            equations.add(equation);
            i += 4;
        }
        return equations;
    }

    record Solution(long x, long y) {
    }

    private static class Equation {
        long buttonA_X;
        long buttonA_Y;

        long buttonB_X;
        long buttonB_Y;

        long result_X;
        long result_Y;

        @Override
        public String toString() {
            return "Equation{" +
                    "buttonA_X=" + buttonA_X +
                    ", buttonA_Y=" + buttonA_Y +
                    ", buttonB_X=" + buttonB_X +
                    ", buttonB_Y=" + buttonB_Y +
                    ", result_X=" + result_X +
                    ", result_Y=" + result_Y +
                    '}';
        }
    }


}
