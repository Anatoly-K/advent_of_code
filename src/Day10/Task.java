package Day10;

import utils.AdventReadUtils;

import java.util.ArrayList;
import java.util.List;

public class Task {
    public static void main(String[] args) {
        AdventReadUtils adventReadUtils = new AdventReadUtils();
        List<List<Integer>> input = adventReadUtils.readDataOneDigitOneInt(AdventReadUtils.INPUT_TYPE.test, "10");
        printMap(input);
      //  taskA(input);
       // taskB(input);
    }

    private static void printMap(List<List<Integer>> map) {
        for (List<Integer> line : map) {
            for (Integer value : line) {
                System.out.print(value+"");
            }
            System.out.println();
        }
        System.out.println();
    }


}
