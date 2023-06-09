import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Driver {
    public static void main(String[] args) {
        /*
        Integer[] randomArray = generateRandomArray(100000, 0, 100000);
        List<Integer> elementsToSort = new ArrayList<>(Arrays.asList(randomArray));
        //System.out.println(elementsToSort);

        MergeSort sorter = new MergeSort();
        elementsToSort = sorter.sort(elementsToSort);
        //System.out.println(elementsToSort);
        */

        /*
        Charset charset = Charset.forName("US-ASCII");
        Path file = FileSystems.getDefault().getPath("src/main/java", "test.txt");
        try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        } // end try/catch
        */

        Charset charset = Charset.forName("US-ASCII");
        String s = "test\n";
        Path file = FileSystems.getDefault().getPath("src/main/java", "test.txt");
        try (BufferedWriter writer = Files.newBufferedWriter(file, charset)) {
            for(int i = 0; i < 10; i++)
            {
                writer.write(s, 0, s.length());
            } // end for
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
    } // end main

    public static Integer[] generateRandomArray(int size, int minValue, int maxValue) {
        Integer[] array = new Integer[size];
        Random random = new Random();

        for (int i = 0; i < size; i++) 
        {
            array[i] = random.nextInt(maxValue - minValue + 1) + minValue;
        } // end for

        return array;
    } // end generateRandomArray
} // end Driver