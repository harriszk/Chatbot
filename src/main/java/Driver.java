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

        Integer[] test = generate(50, 1, 1000);
        List<Integer> elementsToSort = new ArrayList<>(Arrays.asList(test));
        System.out.println(elementsToSort);
    } // end main

    public static Integer[] generate(int select, int minVal, int maxVal) {
        if (select <= 0) {
            System.out.println("The number of random numbers desired must be a positive integer.");
            return new Integer[0];
        }
        if (minVal <= 0) {
            System.out.println("The minimum value of the range must be a positive integer.");
            return new Integer[0];
        }
        if (maxVal <= 0) {
            System.out.println("The maximum value of the range must be a positive integer.");
            return new Integer[0];
        }

        int remaining = maxVal - minVal + 1;

        if (remaining <= select) {
            System.out.println("The number of values to select must be less than the size of the range from which to select them.");
            return new Integer[0];
        }

        Integer[] result = new Integer[select];
        int index = 0;
        for (int i = minVal; i <= maxVal; i++) {
            if (Math.random() < (select / (double) remaining)) {
                result[index++] = i;
                select--;
            }
            remaining--;
        }

        return result;
    }

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