import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.Assert;
import org.junit.Before;

public class ChunkProcessorTest {
    private static final String TEST_CHUNK_LOCATION = "tmp/chunks/testChunks/test.txt";
    private ChunkProcessor processor;
    private FileHanlder fileHanlder = new FileHanlder();

    private int[] testChunk = {2, 7, 9, 12, 15, 19, 23, 26, 27, 28, 30, 31, 36, 38};

    @Before
    public void setUp()
    {
        this.fileHanlder.writeChunkToFile(TEST_CHUNK_LOCATION, testChunk);
        this.processor = new ChunkProcessor(TEST_CHUNK_LOCATION);
    } // end setUp

    @Test
    public void testLoadNElements()
    {
        List<Integer> expected = new ArrayList<>(Arrays.asList(2, 7, 9, 12, 15, 19));

        List<Integer> actual = this.processor.loadNElements(6);

        Assert.assertEquals(expected, actual);

        this.processor.close();
        this.fileHanlder.deleteChunkFile(TEST_CHUNK_LOCATION);
    } // end testLoadNElements

    @Test
    public void testLoadNextNElements()
    {
        this.processor.loadNElements(6);

        List<Integer> expected = new ArrayList<>(Arrays.asList(23, 26, 27));

        List<Integer> actual = this.processor.loadNElements(3);

        Assert.assertEquals(expected, actual);

        this.processor.close();
        this.fileHanlder.deleteChunkFile(TEST_CHUNK_LOCATION);
    } // end testLoadNextElements
} // end ChunkProcessorTest