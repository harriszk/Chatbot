import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ChatLogTest {
    private ChatLog<Integer> chatLog;
    private FileHandler fileHandler = new FileHandler();
    private static int CHUNK_SIZE = 5000;

    @Before
    public void setUp() {
        this.chatLog = new ChatLog<>(new IntegerConverter());
    } // end setUp

    @Test
    public void testChunkingEmptyFile() {
        // Test when the chat log directory is empty, and ensure that no chunks are created
        String directoryPath = "tmp/emptyFolder";

        File directory = new File(directoryPath);
        if(directory.exists()) {
            directory.delete();
        } // end if

        boolean created = directory.mkdirs();
        Assert.assertTrue("Failed to create the empty folder.", created);

        this.chatLog.chunkLogs(directoryPath, ChatLogTest.CHUNK_SIZE);

        Assert.assertTrue(this.chatLog.getChunkLocations().isEmpty());

        this.deleteDirectory(directory);
    } // end testChunkingEmptyFile

    @Test
    public void testChunkingSingleEntry() {
        // Test when the chat log directory contains only one entry, and verify that the chunk(s) 
        // are correctly sorted and in the tmp/chunks folder.
        String directoryPath = "tmp/unsortedFiles";

        File directory = new File(directoryPath);
        if(directory.exists()) {
            this.deleteDirectory(directory);
        } // end if

        boolean created = directory.mkdirs();
        Assert.assertTrue("Failed to create the empty folder.", created);

        Integer[] unsortedTestArray = this.generateRandomArray(20, 0, 100);

        String testFilePath = directoryPath + "/testUnsorted.txt";
        this.fileHandler.writeChunkToFile(testFilePath, unsortedTestArray);

        this.chatLog.chunkLogs(directoryPath, ChatLogTest.CHUNK_SIZE);

        List<String> chunkLocations = this.chatLog.getChunkLocations();
        Assert.assertFalse("No chunks were created.", chunkLocations.isEmpty());

        List<Integer[]> files = new ArrayList<>();
        files.add(unsortedTestArray);
        int expectedChunkCount = calculateExpectedChunkCount(files);

        Assert.assertEquals("The number of chunks isn't as expected.", expectedChunkCount, chunkLocations.size());

        this.deleteFile(testFilePath);
        this.deleteChunks(chunkLocations);
        this.deleteDirectory(directory);
    } // end testChunkingSingleEntry

    @Test
    public void testChunkingMultipleEntries() {
        // Test when the chat log directory contains multiple entries, and 
        // verify that they are correctly sorted and saved as chunks
        String directoryPath = "tmp/unsortedFiles";

        File directory = new File(directoryPath);
        if(directory.exists()) {
            this.deleteDirectory(directory);
        } // end if

        boolean created = directory.mkdirs();
        Assert.assertTrue("Failed to create the empty folder.", created);

        Integer[] unsortedTestArray1 = this.generateRandomArray(50, 0, 200);
        Integer[] unsortedTestArray2 = this.generateRandomArray(50, 10, 60);
        Integer[] unsortedTestArray3 = this.generateRandomArray(50, 50, 350);
        Integer[] unsortedTestArray4 = this.generateRandomArray(50, 25, 150);

        List<Integer[]> unsortedArrays = new ArrayList<>();
        unsortedArrays.add(unsortedTestArray1);
        unsortedArrays.add(unsortedTestArray2);
        unsortedArrays.add(unsortedTestArray3);
        unsortedArrays.add(unsortedTestArray4);

        String testFilePath;

        for(int i = 0; i < unsortedArrays.size(); i++) {
            testFilePath = directoryPath + "/testUnsorted" + (i + 1) + ".txt";
            this.fileHandler.writeChunkToFile(testFilePath, unsortedArrays.get(i));
        } // end for

        this.chatLog.chunkLogs(directoryPath, ChatLogTest.CHUNK_SIZE);

        List<String> chunkLocations = this.chatLog.getChunkLocations();
        Assert.assertFalse("No chunks were created.", chunkLocations.isEmpty());

        int expectedChunkCount = calculateExpectedChunkCount(unsortedArrays);
        Assert.assertEquals("The number of chunks isn't as expected.", expectedChunkCount, chunkLocations.size());

        this.deleteChunks(chunkLocations);
        this.deleteDirectory(directory);
    } // end testChunkingMultipleEntries

    @Test
    public void testChunkingLargeFile() {
        // Test when the chat log directory has a very large file, exceeding the available memory.
        // Ensure that the chunking process can handle the large file and produce correctly sorted chunks
        String directoryPath = "tmp/unsortedFiles";

        File directory = new File(directoryPath);
        if(directory.exists()) {
            this.deleteDirectory(directory);
        } // end if

        boolean created = directory.mkdirs();
        Assert.assertTrue("Failed to create the empty folder.", created);

        Integer[] unsortedTestArray1 = this.generateRandomArray(10000, 0, 10000);
        Integer[] unsortedTestArray2 = this.generateRandomArray(10000, 0, 10000);
        Integer[] unsortedTestArray3 = this.generateRandomArray(10000, 0, 10000);
        Integer[] unsortedTestArray4 = this.generateRandomArray(10000, 0, 10000);
        Integer[] unsortedTestArray5 = this.generateRandomArray(10000, 0, 10000);

        List<Integer[]> unsortedArrays = new ArrayList<>();
        unsortedArrays.add(unsortedTestArray1);
        unsortedArrays.add(unsortedTestArray2);
        unsortedArrays.add(unsortedTestArray3);
        unsortedArrays.add(unsortedTestArray4);
        unsortedArrays.add(unsortedTestArray5);

        String testFilePath;

        for(int i = 0; i < unsortedArrays.size(); i++) {
            testFilePath = directoryPath + "/testUnsorted" + (i + 1) + ".txt";
            this.fileHandler.writeChunkToFile(testFilePath, unsortedArrays.get(i));
        } // end for

        this.chatLog.chunkLogs(directoryPath, ChatLogTest.CHUNK_SIZE);

        List<String> chunkLocations = this.chatLog.getChunkLocations();
        Assert.assertFalse("No chunks were created.", chunkLocations.isEmpty());

        int expectedChunkCount = calculateExpectedChunkCount(unsortedArrays);
        Assert.assertEquals("The number of chunks isn't as expected.", expectedChunkCount, chunkLocations.size());

        this.deleteChunks(chunkLocations);
        this.deleteDirectory(directory);
    } // end testChunkingLargeFile

    @Test
    public void testChunkingWithDifferentN() {
        // Test the chunking process with different values of N
        // Verify that the chunks are still correctly sorted regardless of the value of N
        String directoryPath = "tmp/unsortedFiles";

        File directory = new File(directoryPath);
        if(directory.exists()) {
            this.deleteDirectory(directory);
        } // end if

        boolean created = directory.mkdirs();
        Assert.assertTrue("Failed to create the empty folder.", created);

        Integer[] unsortedTestArray1 = this.generateRandomArray(150, 0, 1000);
        Integer[] unsortedTestArray2 = this.generateRandomArray(25, 0, 1000);
        Integer[] unsortedTestArray3 = this.generateRandomArray(215, 0, 1000);
        Integer[] unsortedTestArray4 = this.generateRandomArray(80, 0, 1000);
        Integer[] unsortedTestArray5 = this.generateRandomArray(105, 0, 1000);

        List<Integer[]> unsortedArrays = new ArrayList<>();
        unsortedArrays.add(unsortedTestArray1);
        unsortedArrays.add(unsortedTestArray2);
        unsortedArrays.add(unsortedTestArray3);
        unsortedArrays.add(unsortedTestArray4);
        unsortedArrays.add(unsortedTestArray5);

        String testFilePath;

        for(int i = 0; i < unsortedArrays.size(); i++) {
            testFilePath = directoryPath + "/testUnsorted" + (i + 1) + ".txt";
            this.fileHandler.writeChunkToFile(testFilePath, unsortedArrays.get(i));
        } // end for

        this.chatLog.chunkLogs(directoryPath, ChatLogTest.CHUNK_SIZE);

        List<String> chunkLocations = this.chatLog.getChunkLocations();
        Assert.assertFalse("No chunks were created.", chunkLocations.isEmpty());

        int expectedChunkCount = calculateExpectedChunkCount(unsortedArrays);
        Assert.assertEquals("The number of chunks isn't as expected.", expectedChunkCount, chunkLocations.size());

        this.deleteChunks(chunkLocations);
        this.deleteDirectory(directory);
    } // end testChunkingWithDifferentN

    @Test
    public void testMergeSortingSingleChunk() {
        // Test if a single chunk can be correctly merge sorted and written to a final output file
        String directoryPath = "tmp/unsortedFiles";

        File directory = new File(directoryPath);
        if(directory.exists()) {
            this.deleteDirectory(directory);
        } // end if

        boolean created = directory.mkdirs();
        Assert.assertTrue("Failed to create the empty folder.", created);

        Integer[] unsortedTestArray1 = this.generateRandomArray(50000, 10000, 1000000);
        Integer[] unsortedTestArray2 = this.generateRandomArray(50000, 10000, 1000000);
        Integer[] unsortedTestArray3 = this.generateRandomArray(50000, 10000, 1000000);
        Integer[] unsortedTestArray4 = this.generateRandomArray(50000, 10000, 1000000);
        Integer[] unsortedTestArray5 = this.generateRandomArray(50000, 10000, 1000000);

        List<Integer[]> unsortedArrays = new ArrayList<>();
        unsortedArrays.add(unsortedTestArray1);
        unsortedArrays.add(unsortedTestArray2);
        unsortedArrays.add(unsortedTestArray3);
        unsortedArrays.add(unsortedTestArray4);
        unsortedArrays.add(unsortedTestArray5);

        String testFilePath;

        for(int i = 0; i < unsortedArrays.size(); i++) {
            testFilePath = directoryPath + "/testUnsorted" + (i + 1) + ".txt";
            this.fileHandler.writeChunkToFile(testFilePath, unsortedArrays.get(i));
        } // end for

        this.chatLog.chunkLogs(directoryPath, ChatLogTest.CHUNK_SIZE);

        this.chatLog.mergeChunks("finalMergedChunks.txt");

        this.deleteDirectory(directory);

        //Assert.fail("TODO: Add test implementation");
    } // end testMergeSortingSingleChunk

    @Test
    public void testMergeSortingMultipleChunks() {
        // Test if multiple chunks can be efficiently merge sorted and written to a final output file
        Assert.fail("TODO: Add test implementation");
    } // end testMergeSortingMultipleChunks

    @Test
    public void testMergeSortingWithLargeChunks() {
        // Test if the merge sorting process can handle large chunks efficiently without causing memory issues
        Assert.fail("TODO: Add test implementation");
    } // end testMergeSortingWithLargeChunks

    @Test
    public void testMergeSortingWithDifferentK() {
        // Test the k-way merging process with different values of K
        // Verify that the final output file is correctly sorted regardless of the value of K
        Assert.fail("TODO: Add test implementation");
    } // end testMergeSortingWithDifferentK

    @Test
    public void testMergeSortingWithDifferentChunkSizes() {
        // Test the k-way merging process with chunks of different sizes
        // Ensure that the final output file is correctly sorted regardless of the chunk sizes
        Assert.fail("TODO: Add test implementation");
    } // end testMergeSortingWithDifferentChunkSizes

    @Test
    public void testMergeSortingWithMultipleUsers() {
        // Test the entire chat log management process by simulating multiple chat log files from different users
        // Verify that all the chat logs are correctly chunked, merge sorted, and written to the final output file
        Assert.fail("TODO: Add test implementation");
    } // end testMergeSortingWithMultipleUsers

    // ------------------------------ HELPER METHODS ------------------------------
    private Integer[] generateRandomArray(int size, int minValue, int maxValue) {
        Integer[] array = new Integer[size];
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(maxValue - minValue + 1) + minValue;
        } // end for

        return array;
    } // end generateRandomArray

    private <T> int calculateExpectedChunkCount(List<T[]> unsortedArrays) {
        double totalCount = 0;

        for(T[] array : unsortedArrays) {
            int arraySize = array.length;
            double chunkCount = (double) arraySize / CHUNK_SIZE;
            totalCount += chunkCount;
        } // end for

        return (int) Math.ceil(totalCount);
    } // end calculateExpectedChunkCount

    private void deleteFile(String filePath) {
        File file = new File(filePath);
        if(file.exists()) {
            file.delete();
        } // end if
    } // end deleteFile

    private void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if(files != null) {
            for(File file : files) {
                this.deleteFile(file.getAbsolutePath());
            } // end for
        } // end if
        directory.delete();
    } // end deleteDirectory

    private void deleteChunks(List<String> chunkLocations) {
        for(String chunkLocation : chunkLocations) {
            this.deleteFile(chunkLocation);
        } // end for
    } // end deleteChunks
} // end ChatLogTest