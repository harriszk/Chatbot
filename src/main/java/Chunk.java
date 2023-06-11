import java.util.Queue;

public class Chunk {
    private Queue<Integer> queue;
    private ChunkProcessor processor;

    public Chunk(Queue<Integer> queue, ChunkProcessor processor)
    {
        this.queue = queue;
        this.processor = processor;
    } // end initializing construtor

    public Queue<Integer> getQueue()
    {
        return this.queue;
    } // end getQueue

    public ChunkProcessor getProcessor()
    {
        return this.processor;
    } // end getProcessor
} // end Chunk