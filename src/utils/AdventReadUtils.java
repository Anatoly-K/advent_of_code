package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdventReadUtils {

    public enum INPUT_TYPE {
        data, test
    }

    public List<List<Integer>> readData(INPUT_TYPE inputType, String dayNum) {
        List<List<Integer>> result = new ArrayList<>();

        String filePath = "/Users/anatoly.kondratiev/IdeaProjects/advent-2024/src/Day" + dayNum + "/" + (inputType == INPUT_TYPE.data ? "data.input" : "test.input");

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split("\\s+");
                List<Integer> lineWithInt = Arrays.stream(values).map(Integer::parseInt).toList();
                result.add(lineWithInt);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<String> readDataAsString(INPUT_TYPE inputType, String dayNum) {
        List<String> result = new ArrayList<>();

        String filePath = "/Users/anatoly.kondratiev/IdeaProjects/advent-2024/src/Day" + dayNum + "/" + (inputType == INPUT_TYPE.data ? "data.input" : "test.input");

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                result.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
