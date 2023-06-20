import java.util.List;
import java.util.Queue;

/**
 * This class is a data structure for a chunk. It assumes that the
 * chunks text file is already sorted so the getNextElement method
 * can be thought of as getting the smallest element from the chunk.
 */
public class Chunk<T extends Comparable<T>> {
    private Queue<T> queue;
    private ChunkProcessor<T> processor;
    private ElementConverter<T> converter;
    private int elementsRemovedCounter = 0;
    private boolean deleteFile = true;

    public Chunk(Queue<T> queue, ChunkProcessor<T> processor, ElementConverter<T> converter) {
        this.queue = queue;
        this.processor = processor;
        this.converter = converter;
    } // end initializing constructor

    public boolean isEmpty() {
        return queue.isEmpty();
    } // end isEmpty

    public T getNextElement() {
        return queue.peek();
    } // end getNextElement

    public T removeNextElement() {
        elementsRemovedCounter++;
        return queue.poll();
    } // end removeNextElement

    public void loadNewElementsIfNeeded(int MAX) {
        if(elementsRemovedCounter >= MAX) {
            List<T> newElements = processor.loadNElements(MAX, converter);

            if(newElements != null) {
                queue.addAll(newElements);
            } // end if
            
            elementsRemovedCounter = 0;
        } // end if
    } // end loadNewElementsIfNeeded

    public void setDeleteFlag(boolean flag) {
        deleteFile = flag;
    } // end setDeleteFlag

    public void delete() {
        if(deleteFile) {
            processor.deleteChunkFromFile();
        } // end if

        processor.close();
    } // end delete
} // end Chunk