import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class KWayMerge<T extends Comparable<T>> {
    private static final String CHUNKS_DIRECTORY = "tmp/chunks/";
    private static final int MAX_SIZE = 100;
    private static final int BUFFER_SIZE = 1000;
    private Queue<Chunk<T>> chunksToMerge;
    private ElementConverter<T> converter;

    public KWayMerge(ElementConverter<T> converter) {
        chunksToMerge = new LinkedList<>();
        this.converter = converter;
    } // end default constructor

    public void mergeAllChunks(List<String> chunkLocations, String outputFileName) {
        PriorityQueue<T> chunkQueue;
        ChunkProcessor<T> chunkProcessor;
        Chunk<T> left;
        Chunk<T> right;
        
        // Load in initial chunks into queues 
        for(String chunkLocation : chunkLocations) {
            chunkProcessor = new ChunkProcessor<>(chunkLocation);

            List<T> loadedChunk = chunkProcessor.loadNElements(KWayMerge.MAX_SIZE, converter);
            if(loadedChunk == null) {
                chunkProcessor.deleteChunkFromFile();
                break;
            } // end if

            chunkQueue = new PriorityQueue<>(loadedChunk);

            left = new Chunk<>(chunkQueue, chunkProcessor, converter);
            // Uncomment this if you don't want the initial chunks to be deleted.
            left.setDeleteFlag(false);
            chunksToMerge.add(left);
        } // end for


        // If there was just a single chunk then we basically just rename it
        // although it does go through the formality of doing the merge.
        if(chunksToMerge.size() == 1) {
            // TODO: Change this to just rename the chunk's file name to "finalMergedChunks.txt".

            left = chunksToMerge.poll();
            chunkQueue = new PriorityQueue<>();
            right = new Chunk<>(chunkQueue, null, converter);

            merge(left, right, outputFileName);
            left.delete();
        } // end if

        // Merge chunks until only one chunk remains
        while(chunksToMerge.size() > 1) {
            System.out.println((chunksToMerge.size() - 1) + " merges left...");
            left = chunksToMerge.poll();
            right = chunksToMerge.poll();

            String mergedChunkLocation;

            if(chunksToMerge.size() == 0) {
                mergedChunkLocation = outputFileName;
            } else {
                mergedChunkLocation = KWayMerge.CHUNKS_DIRECTORY + "merged_chunk_" + System.currentTimeMillis() + ".txt";
            } // end if
            merge(left, right, mergedChunkLocation);

            chunkProcessor = new ChunkProcessor<>(mergedChunkLocation);
            chunkQueue = new PriorityQueue<>(chunkProcessor.loadNElements(KWayMerge.MAX_SIZE, converter));
            Chunk<T> mergedChunk = new Chunk<>(chunkQueue, chunkProcessor, converter);
            chunksToMerge.add(mergedChunk);

            left.delete();
            right.delete();
        } // end while
    } // end mergeAllChunks

    private void merge(Chunk<T> left, Chunk<T> right, String outputFile) {
        try {
            FileWriter fileWriter = new FileWriter(outputFile, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter, KWayMerge.BUFFER_SIZE);

            StringBuilder mergedData = new StringBuilder();

            int comparingNumber = 0;

            while(!left.isEmpty() && !right.isEmpty()) {
                comparingNumber = left.getNextElement().compareTo(right.getNextElement());

                if(comparingNumber <= 0) {
                    mergedData.append(left.removeNextElement());
                    mergedData.append(System.lineSeparator());

                    left.loadNewElementsIfNeeded(KWayMerge.MAX_SIZE);
                } else {
                    mergedData.append(right.removeNextElement());
                    mergedData.append(System.lineSeparator());

                    right.loadNewElementsIfNeeded(KWayMerge.MAX_SIZE);
                } // end if

                tryWritingToBuffer(mergedData, bufferedWriter);
            } // end while


            // Write the remaining elements from either the left or the right chunk.
            while(!left.isEmpty()) {
                mergedData.append(left.removeNextElement());
                mergedData.append(System.lineSeparator());

                left.loadNewElementsIfNeeded(KWayMerge.MAX_SIZE);

                tryWritingToBuffer(mergedData, bufferedWriter);
            } // end while

            while(!right.isEmpty()) {
                mergedData.append(right.removeNextElement());
                mergedData.append(System.lineSeparator());

                right.loadNewElementsIfNeeded(KWayMerge.MAX_SIZE);

                tryWritingToBuffer(mergedData, bufferedWriter);
            } // end while

            bufferedWriter.write(mergedData.toString());
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        } // end try/catch
    } // end merge

    private void tryWritingToBuffer(StringBuilder buffer, BufferedWriter bufferedWriter) throws IOException {
        if(buffer.length() >= KWayMerge.BUFFER_SIZE) {
            bufferedWriter.write(buffer.toString());
            buffer.setLength(0); // Clear the buffer
        } // end if
    } // end writeToBuffer
} // end KWayMerge