import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class KWayMerge {
    private static final String CHUNKS_DIRECTORY = "tmp/chunks/testChunks/";
    private static final int MAX_SIZE = 10;
    private static final int BUFFER_SIZE = 1000;
    private Queue<Chunk> chunksToMerge;

    public KWayMerge()
    {
        this.chunksToMerge = new LinkedList<>();
    } // end default construtor

    public void mergeAllChunks(List<String> chunkLocations)
    {
        List<Integer> loadedChunk;
        PriorityQueue<Integer> chunkQueue;
        ChunkProcessor chunkProcessor;
        Chunk left;
        Chunk right;
        Chunk mergedChunk;
        
        // Load in initial chunks into queues 
        for(String chunkLocation : chunkLocations)
        {
            chunkProcessor = new ChunkProcessor(chunkLocation);

            loadedChunk = chunkProcessor.loadNElements(KWayMerge.MAX_SIZE);
            if(loadedChunk == null)
            {
                chunkProcessor.deleteChunkFromFile();
                break;
            } // end if

            System.out.println("Loading chunk " + loadedChunk);

            chunkQueue = new PriorityQueue<>(loadedChunk);
            
            System.out.println(chunkQueue);

            left = new Chunk(chunkQueue, chunkProcessor);
            this.chunksToMerge.add(left);
        } // end for

        // If there was just a single chunk then we basically just rename it
        // although it does go through the formality of doing the merge.
        if(this.chunksToMerge.size() == 1)
        {
            // TODO: Change this to just rename the chunk's file name to "finalMergedChunks.txt".

            left = this.chunksToMerge.poll();
            chunkQueue = new PriorityQueue<>();
            right = new Chunk(chunkQueue, null);

            String mergedChunkLocation = KWayMerge.CHUNKS_DIRECTORY + "finalMergedChunks.txt";
            merge(left, right, mergedChunkLocation);
            left.delete();
        } // end if

        // Merge chunks until only one chunk remains
        while(this.chunksToMerge.size() > 1) 
        {
            left = this.chunksToMerge.poll();
            right = this.chunksToMerge.poll();

            String mergedChunkLocation;

            if(this.chunksToMerge.size() == 0) {
                mergedChunkLocation = KWayMerge.CHUNKS_DIRECTORY + "finalMergedChunks.txt";
            } else {
                mergedChunkLocation = KWayMerge.CHUNKS_DIRECTORY + "merged_chunk_" + System.currentTimeMillis() + ".txt";
            } // end if
            merge(left, right, mergedChunkLocation);

            chunkProcessor = new ChunkProcessor(mergedChunkLocation);
            chunkQueue = new PriorityQueue<>(chunkProcessor.loadNElements(KWayMerge.MAX_SIZE));
            mergedChunk = new Chunk(chunkQueue, chunkProcessor);
            this.chunksToMerge.add(mergedChunk);

            left.delete();
            right.delete();
        } // end while
    } // end mergeAllChunks

    private void merge(Chunk left, Chunk right, String outputFile) 
    {
        try {
            FileWriter fileWriter = new FileWriter(outputFile, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter, KWayMerge.BUFFER_SIZE);

            StringBuilder mergedData = new StringBuilder();

            while(!left.isEmpty() && !right.isEmpty())
            {
                if(left.getNextElement() <= right.getNextElement()) {
                    mergedData.append(left.removeNextElement());
                    mergedData.append(System.lineSeparator());

                    left.loadNewElementsIfNeeded(KWayMerge.MAX_SIZE);
                } else {
                    mergedData.append(right.removeNextElement());
                    mergedData.append(System.lineSeparator());

                    right.loadNewElementsIfNeeded(KWayMerge.MAX_SIZE);
                } // end if

                this.tryWritingToBuffer(mergedData, bufferedWriter);
            } // end while

            // Write the remaining elements from either the left or the right chunk.
            while(!left.isEmpty()) 
            {
                mergedData.append(left.removeNextElement());
                mergedData.append(System.lineSeparator());

                left.loadNewElementsIfNeeded(KWayMerge.MAX_SIZE);

                this.tryWritingToBuffer(mergedData, bufferedWriter);
            } // end while

            while(!right.isEmpty()) 
            {
                mergedData.append(right.removeNextElement());
                mergedData.append(System.lineSeparator());

                right.loadNewElementsIfNeeded(KWayMerge.MAX_SIZE);

                this.tryWritingToBuffer(mergedData, bufferedWriter);
            } // end while

            bufferedWriter.write(mergedData.toString());
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        } // end try/catch
    } // end merge

    private void tryWritingToBuffer(StringBuilder buffer, BufferedWriter bufferedWriter) throws IOException 
    {
        if(buffer.length() >= KWayMerge.BUFFER_SIZE) 
        {
            bufferedWriter.write(buffer.toString());
            buffer.setLength(0); // Clear the buffer
        } // end if
    } // end writeToBuffer
} // end KWayMerge