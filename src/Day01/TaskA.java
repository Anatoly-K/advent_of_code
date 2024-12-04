package Day01;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TaskA {

    private enum INPUT_TYPE {
        data, test
    }

    public static void main(String[] args) {
        System.out.println("Hello world!");
        TaskA taskA = new TaskA();
        List<Integer>[] data = taskA.readData(INPUT_TYPE.data);

        List<Integer> firstColumn = data[0].stream().sorted().toList();
        List<Integer> secondColumn = data[1].stream().sorted().toList();

        long sum = 0;
//        for (int i = 0; i < firstColumn.size(); i++) {
//            int diff = Math.abs(firstColumn.get(i) - secondColumn.get(i));
//            sum += diff;
//        }

        for (int i = 0; i < firstColumn.size(); i++) {
            int valuueToLookFor = firstColumn.get(i);
            int multiplier = secondColumn.stream().filter(x -> x == valuueToLookFor).toList().size();
//            int diff = Math.abs(firstColumn.get(i) - secondColumn.get(i));
            sum += valuueToLookFor * multiplier;
        }

        System.out.println(sum);
    }

    public List<Integer>[] readData(INPUT_TYPE inputType) {
        List<Integer> firstColumn = new ArrayList<>();
        List<Integer> secondColumn = new ArrayList<>();

        String filePath = "src/Day01/" + (inputType == INPUT_TYPE.data ? "data.input" : "test.input");

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split("\\s+");
                firstColumn.add(Integer.parseInt(values[0]));
                secondColumn.add(Integer.parseInt(values[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new List[]{firstColumn, secondColumn};
    }
}
