public class Driver {
    public static void main(String[] args) {
        
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
} // end Driver