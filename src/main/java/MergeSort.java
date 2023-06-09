import java.util.ArrayList;
import java.util.List;

public class MergeSort {
    public MergeSort()
    {

    } // end default constructor

    public <T extends Comparable<T>> List<T> sort(List<T> listToSort)
    {
        if(listToSort.size() <= 1)
        {
            return listToSort;
        } // end if

        int middleIndex = listToSort.size() / 2;

        List<T> left = new ArrayList<>(listToSort.subList(0, middleIndex));
        List<T> right = new ArrayList<>(listToSort.subList(middleIndex, listToSort.size()));

        left = sort(left);
        right = sort(right);

        return merge(left, right);
    } // end sort

    public <T extends Comparable<T>> List<T> merge(List<T> left, List<T> right)
    {
        List<T> result = new ArrayList<>();
        int comparingNumber = 0;

        while(true)
        {
            if(left.isEmpty())
            {
                result.addAll(right);
                break;
            } // end if

            if(right.isEmpty())
            {
                result.addAll(left);
                break;
            } // end if

            comparingNumber = left.get(0).compareTo(right.get(0));

            if(comparingNumber < 0 || comparingNumber == 0) {
                result.add(left.get(0));
                left.remove(0);
            } else {
                result.add(right.get(0));
                right.remove(0);
            } // end if
        } // end while

        return result;
    } // end merge
} // end MergeSort