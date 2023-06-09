import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Driver {
    public static void main(String[] args) {
        Integer[] randomArray = generateRandomArray(100000, 0, 100000);
        List<Integer> elementsToSort = new ArrayList<>(Arrays.asList(randomArray));
        //System.out.println(elementsToSort);

        MergeSort sorter = new MergeSort();
        elementsToSort = sorter.sort(elementsToSort);
        //System.out.println(elementsToSort);
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