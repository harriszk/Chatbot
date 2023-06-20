import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FileHandlerTest {
    private static final String TEST_DIRECTORY = "tmp/chunks/testChunks";
    private FileHandler fileHandler;
    private ElementConverter<Integer> converter = new IntegerConverter();

    private Integer[] intChunk1 = {2, 4, 6, 8, 10};
    private Integer[] intChunk2 = {1, 3, 5, 7, 9};

    @Before
    public void setUp() {
        this.fileHandler = new FileHandler();
    } // end setUp

    @Test
    public void testLoadEntriesFromFile() {
        List<Integer> expected = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16));

        List<Integer> actual = this.fileHandler.loadChunkFromFile(TEST_DIRECTORY + "/testLoadingChunksFile.txt", this.converter);

        Assert.assertEquals(expected, actual);
    } // end testLoadEntriesFromFile

    @Test
    public void testWriteEntriesToFile() {
        List<Integer> expected = new ArrayList<>(Arrays.asList(2, 4, 6, 8, 10));

        this.fileHandler.writeChunkToFile(TEST_DIRECTORY + "/test.txt", intChunk1);
        List<Integer> actual = this.fileHandler.loadChunkFromFile(TEST_DIRECTORY + "/test.txt", this.converter);
        
        Assert.assertEquals(expected, actual);

        this.fileHandler.deleteChunkFile(TEST_DIRECTORY + "/test.txt");
    } // end testWriteEntriesToFile

    @Test
    public void testDeleteFile() {
        this.fileHandler.writeChunkToFile(TEST_DIRECTORY + "/test.txt", intChunk2);
        this.fileHandler.deleteChunkFile(TEST_DIRECTORY + "/test.txt");
        List<Integer> actual = this.fileHandler.loadChunkFromFile(TEST_DIRECTORY + "/test.txt", this.converter);

        Assert.assertNull(actual);
    } // end testDeleteFile
} // end FileHandlerTest
