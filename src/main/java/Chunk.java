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
    private int elementsRemovedCounter = 0;
    private ElementConverter<T> converter;
    private boolean deleteFile = true;

    public Chunk(Queue<T> queue, ChunkProcessor<T> processor, ElementConverter<T> converter)
    {
        this.queue = queue;
        this.processor = processor;
        this.converter = converter;
    } // end initializing constructor

    public boolean isEmpty()
    {
        return this.queue.isEmpty();
    } // end isEmpty

    public T getNextElement()
    {
        return this.queue.peek();
    } // end getNextElement

    public T removeNextElement()
    {
        this.elementsRemovedCounter++;
        return this.queue.poll();
    } // end removeNextElement

    public void loadNewElementsIfNeeded(int MAX) {
        if(this.elementsRemovedCounter >= MAX) 
        {
            List<T> newElements = this.processor.loadNElements(MAX, this.converter);

            if(newElements != null)
            {
                this.queue.addAll(newElements);
            } // end if
            
            this.elementsRemovedCounter = 0;
        } // end if
    } // end loadNewElementsIfNeeded

    public void setDeleteFlag(boolean flag)
    {
        this.deleteFile = flag;
    } // end setDeleteFlag

    public void delete()
    {
        if(this.deleteFile)
        {
            this.processor.deleteChunkFromFile();
        } // end if
    } // end delete
} // end Chunk