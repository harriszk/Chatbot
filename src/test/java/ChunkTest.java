import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import org.junit.Assert;
import org.junit.Test;

public class ChunkTest {
    private static final int MAX_SIZE = 3;
    private Chunk chunk;

    @Test
    public void testGetChunkQueue()
    {
        ChunkProcessor processor = new ChunkProcessor("testLoadingChunksFile.txt");

        List<Integer> loadedChunk = processor.loadNElements(MAX_SIZE);

        Queue<Integer> chunkQueue = new PriorityQueue<>(MAX_SIZE);
        for(Integer element : loadedChunk)
        {
            chunkQueue.add(element);
        } // end for

        this.chunk = new Chunk(chunkQueue, processor);

        Queue<Integer> expected = new PriorityQueue<>(MAX_SIZE);
        expected.add(1);
        expected.add(2);
        expected.add(3);

        List<Integer> expectedList = new ArrayList<>(expected);
        List<Integer> actualList = new ArrayList<>(this.chunk.getQueue());

        // Compare the lists
        Assert.assertEquals(expectedList, actualList);
    } // end testGetChunkQueue

    @Test
    public void testGetChunkProcessor()
    {
        ChunkProcessor expected = new ChunkProcessor("testLoadingChunksFile.txt");

        this.chunk = new Chunk(null, expected);

        ChunkProcessor actual = this.chunk.getProcessor();

        Assert.assertEquals(actual, expected);
    } // end testGetChunkProcessor
} // end ChunkTest