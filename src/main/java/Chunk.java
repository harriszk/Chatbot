import java.util.List;
import java.util.Queue;

/**
 * This class is a data struture for a chunk. It assumes that the
 * chunks text file is aleady sorted so the getNextElement method
 * can be thought of as getting the smallest element from the chunk.
 */
public class Chunk {
    private Queue<Integer> queue;
    private ChunkProcessor processor;
    private int elementsRemovedCounter = 0;

    public Chunk(Queue<Integer> queue, ChunkProcessor processor)
    {
        this.queue = queue;
        this.processor = processor;
    } // end initializing construtor

    public boolean isEmpty()
    {
        return this.queue.isEmpty();
    } // end isEmpty

    public Integer getNextElement()
    {
        return this.queue.peek();
    } // end getNextElement

    public Integer removeNextElement()
    {
        this.elementsRemovedCounter++;
        return this.queue.poll();
    } // end removeNextElement

    public void loadNewElementsIfNeeded(int MAX) {
        if(this.elementsRemovedCounter >= MAX) 
        {
            List<Integer> newElements = this.processor.loadNElements(MAX);
            if(newElements != null)
            {
                this.queue.addAll(newElements);
            }
            
            this.elementsRemovedCounter = 0;
        } // end if
    } // end loadNewElementsIfNeeded

    public void delete()
    {
        this.processor.deleteChunkFromFile();
    } // end delete
} // end Chunk