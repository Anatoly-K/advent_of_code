package Day14;

import utils.AdventReadUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Task {

    static int width = 101;
    static int height = 103;

    public static void main(String[] args) throws InterruptedException {
        AdventReadUtils adventReadUtils = new AdventReadUtils();
        List<String> inputAsListOfStrings = adventReadUtils.readDataAsString(AdventReadUtils.INPUT_TYPE.data, "14");
        System.out.println(inputAsListOfStrings);
        int stepsToMove = 10000;
        List<Robot> robotList = new ArrayList<>();
        long topLeft = 0;
        long topRight = 0;
        long bottomLeft = 0;
        long bottomRight = 0;
        for (String string : inputAsListOfStrings) {
            //  System.out.println(string);
            Robot robot = fromStringToRobot(string);
            // robot = moveRobot(robot, stepsToMove);
            robotList.add(robot);
            // printRobotMap(robotList);

            // System.out.println("width / 2 = " + width / 2 + " height / 2 = " + height / 2);

            if (robot.currentX < width / 2 && robot.currentY < height / 2) {
                topLeft++;
            } else if (robot.currentX > width / 2 && robot.currentY < height / 2) {
                topRight++;
            } else if (robot.currentX < width / 2 && robot.currentY > height / 2) {
                bottomLeft++;
            } else if (robot.currentX > width / 2 && robot.currentY > height / 2) {
                bottomRight++;
            }
            //  System.out.println(robot);
        }
        System.out.println(topLeft + " " + topRight + " " + bottomLeft + " " + bottomRight);
        printRobotSeveralIterations(robotList, stepsToMove);
        //  printRobotMap(robotList);
        //91201968 - too low
        //228690000
        long mult = topLeft * topRight * bottomLeft * bottomRight;
        System.out.println(mult);
    }

    private static void printRobotMap(List<Robot> robotList) {
        List<List<Integer>> map = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < width; j++) {
                row.add(0);
            }
            map.add(row);
        }
        boolean shouldPrint = true;
        for (Robot robot : robotList) {
            int currentValue = map.get(robot.currentY).get(robot.currentX);
            if (currentValue == 1) {
                shouldPrint = false;
            }
            map.get(robot.currentY).set(robot.currentX, map.get(robot.currentY).get(robot.currentX) + 1);
        }
        if (shouldPrint) {
            for (List<Integer> row : map) {
                for (Integer i : row) {
                    System.out.print(i == 0 ? " " : i);
                }
                System.out.println();
            }
        }
    }

    private static void printRobotSeveralIterations(List<Robot> robotList, int stepsToPrint) throws InterruptedException {
        printRobotMap(robotList);

        for (int i = 1; i <= stepsToPrint; i++) {
            System.out.println("Step " + i);

            List<Robot> robotListAfterMove = new ArrayList<>();
            for (Robot robot : robotList) {
                robotListAfterMove.add(moveRobot(robot, i));
            }
            // if (i >= stepsToPrint - 50) {
            printRobotMap(robotListAfterMove);
            //        TimeUnit.SECONDS.sleep(1);
            // }
            // printRobotMap(robotListAfterMove);
        }
        System.out.println();

    }

    private static Robot moveRobot(Robot robot, int steps) {
        robot.currentX = (robot.initialX + robot.vectorX * steps) % width;
        if (robot.currentX < 0) {
            robot.currentX = width + robot.currentX;
        }
        robot.currentY = (robot.initialY + robot.vectorY * steps) % height;
        if (robot.currentY < 0) {
            robot.currentY = height + robot.currentY;
        }
        return robot;
    }

    private static Robot fromStringToRobot(String string) {
        Robot robot = new Robot();
        Pattern pattern = Pattern.compile("p=(-?\\d+),(-?\\d+) v=(-?\\d+),(-?\\d+)");
        Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            robot.initialX = Integer.parseInt(matcher.group(1));
            robot.initialY = Integer.parseInt(matcher.group(2));
            robot.currentX = robot.initialX;
            robot.currentY = robot.initialY;
            robot.vectorX = Integer.parseInt(matcher.group(3));
            robot.vectorY = Integer.parseInt(matcher.group(4));
        }
        return robot;
    }

    private static class Robot {
        int initialX;
        int initialY;

        int currentX;
        int currentY;

        int vectorX;
        int vectorY;

        @Override
        public String toString() {
            return "Robot{" +
                    "initialX=" + initialX +
                    ", initialY=" + initialY +
                    ", currentX=" + currentX +
                    ", currentY=" + currentY +
                    ", vectorX=" + vectorX +
                    ", vectorY=" + vectorY +
                    '}';
        }
    }
}
