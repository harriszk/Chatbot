import java.io.File;
import java.util.PriorityQueue;
import java.util.Queue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ChunkTest {
    private static final int MAX_SIZE = 3;
    private static final String TEST_CHUNKS_DIRECTORY = "tmp/chunks/testChunks";
    private ChunkProcessor<Integer> processor;
    private Queue<Integer> queue;
    private IntegerConverter converter = new IntegerConverter();
    private Chunk<Integer> chunk;

    @Before
    public void setup() {
        this.processor = new ChunkProcessor<>(TEST_CHUNKS_DIRECTORY + "/testLoadingChunksFile.txt");
        this.queue = new PriorityQueue<>(MAX_SIZE);
        this.queue.addAll(this.processor.loadNElements(MAX_SIZE, this.converter));

        this.chunk = new Chunk<>(this.queue, this.processor, this.converter);
    } // end setUp

    @Test
    public void testIsEmpty() {
        Assert.assertFalse(this.chunk.isEmpty());
        
        for(int i = 0; i < 16; i++) {
            this.chunk.removeNextElement();
        } // end for
        
        Assert.assertTrue(this.chunk.isEmpty());
    } // end testIsEmpty

    @Test
    public void testGetNextElement() {
        Integer expected = 1;
        Integer actual = this.chunk.getNextElement();
        Assert.assertEquals(expected, actual);

        this.chunk.removeNextElement();
        expected = 2;
        actual = this.chunk.getNextElement();
        Assert.assertEquals(expected, actual);
    } // end testGetNextElement

    @Test
    public void testRemoveNextElement() {
        Integer expected = 1;
        Integer actual = this.chunk.removeNextElement();
        Assert.assertEquals(expected, actual);

        expected = 2;
        actual = this.chunk.removeNextElement();
        Assert.assertEquals(expected, actual);

        expected = 3;
        actual = this.chunk.removeNextElement();
        Assert.assertEquals(expected, actual);
    } // end testRemoveNextElement

    @Test
    public void testLoadNewElementsIfNeeded() {
        for(int i = 0; i < MAX_SIZE; i++) {
            this.chunk.removeNextElement();
        } // end for

        this.chunk.loadNewElementsIfNeeded(MAX_SIZE);

        Integer expected = MAX_SIZE + 1;
        Integer actual = this.chunk.removeNextElement();
        Assert.assertEquals(expected, actual);

        expected++;
        actual = this.chunk.removeNextElement();
        Assert.assertEquals(expected, actual);
    } // end testRemoveNextElement

    @Test
    public void testDelete() {
        Integer[] testChunk = {2, 7, 9, 12, 15, 19, 23, 26, 27, 28, 30, 31, 36, 38};
        FileHandler fileHandler = new FileHandler();
        fileHandler.writeChunkToFile(TEST_CHUNKS_DIRECTORY + "/test.txt", testChunk);

        this.processor = new ChunkProcessor<>(TEST_CHUNKS_DIRECTORY + "/test.txt");
        this.queue = new PriorityQueue<>(MAX_SIZE);
        this.queue.addAll(this.processor.loadNElements(MAX_SIZE, this.converter));

        this.chunk = new Chunk<>(this.queue, this.processor, this.converter);

        this.chunk.delete();

        File chunkFile = new File(TEST_CHUNKS_DIRECTORY + "/test.txt");
        Assert.assertFalse(chunkFile.exists());
    } // end testDelete
} // end ChunkTest