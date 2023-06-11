import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
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
    public static void main(String[] args) throws IOException {
        /*
        Integer[] randomArray = generateRandomArray(100000, 0, 100000);
        List<Integer> elementsToSort = new ArrayList<>(Arrays.asList(randomArray));
        //System.out.println(elementsToSort);

        MergeSort sorter = new MergeSort();
        elementsToSort = sorter.sort(elementsToSort);
        //System.out.println(elementsToSort);
        */

        int numChunks = 5;
        int chunkSize = 500;

        List<String> chunkFiles = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < numChunks; i++) {
            String fileName = "Chunk_" + i + ".txt";
            chunkFiles.add(fileName);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                int overlap = chunkSize / 5; // 10% overlap between chunks

                // Generate the chunk data
                int start = i * chunkSize - overlap;
                int end = (i + 1) * chunkSize + overlap;
                for (int j = start; j < end; j++) {
                    writer.write(String.valueOf(j + random.nextInt(10))); // Add random variation to each integer
                    writer.newLine();
                }
            }
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