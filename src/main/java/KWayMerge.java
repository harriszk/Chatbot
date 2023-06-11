import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class KWayMerge {
    private static final String CHUNKS_DIRECTORY = "tmp/chunks/testChunks/";
    private static final int MAX_SIZE = 3;
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
                chunkProcessor.deleteChunk();
                break;
            } // end if

            System.out.println("Loading chunk " + loadedChunk);

            chunkQueue = new PriorityQueue<>(loadedChunk);
            
            left = new Chunk(chunkQueue, chunkProcessor);
            this.chunksToMerge.add(left);
        } // end for

        // If there was just a single chunk then we basically just rename it
        // although it does go through the formality of doing the merge.
        if(this.chunksToMerge.size() == 1)
        {
            left = this.chunksToMerge.poll();
            chunkQueue = new PriorityQueue<>();
            right = new Chunk(chunkQueue, null);

            String mergedChunkLocation = KWayMerge.CHUNKS_DIRECTORY + "finalMergedChunks.txt";
            merge(left, right, mergedChunkLocation);
            left.getProcessor().deleteChunk();
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

            left.getProcessor().deleteChunk();
            right.getProcessor().deleteChunk();
        } // end while
    } // end mergeAllChunks

    private void merge(Chunk left, Chunk right, String outputFile) 
    {
        int leftCounter = 0;
        int rightCounter = 0;
        List<Integer> newElements;

        try {
            FileWriter fileWriter = new FileWriter(outputFile, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter, KWayMerge.BUFFER_SIZE);

            StringBuilder mergedData = new StringBuilder();

            while(!left.getQueue().isEmpty() && !right.getQueue().isEmpty())
            {
                if(left.getQueue().peek() <= right.getQueue().peek()) {
                    mergedData.append(left.getQueue().poll());
                    mergedData.append(System.lineSeparator());

                    leftCounter++;
                    if(leftCounter == KWayMerge.MAX_SIZE)
                    {
                        newElements = left.getProcessor().loadNElements(KWayMerge.MAX_SIZE);
                        left.getQueue().addAll(newElements);
                        leftCounter = 0;
                    } // end if
                } else {
                    mergedData.append(right.getQueue().poll().toString());
                    mergedData.append(System.lineSeparator());

                    rightCounter++;
                    if(rightCounter == KWayMerge.MAX_SIZE)
                    {
                        newElements = right.getProcessor().loadNElements(KWayMerge.MAX_SIZE);
                        right.getQueue().addAll(newElements);
                        rightCounter = 0;
                    } // end if
                } // end if

                if(mergedData.length() >= KWayMerge.BUFFER_SIZE) 
                {
                    bufferedWriter.write(mergedData.toString());
                    mergedData.setLength(0); // Clear the buffer
                } // end if
            } // end while

            // Write the remaining elements from either the left or the right chunk.
            while(!left.getQueue().isEmpty()) 
            {
                mergedData.append(left.getQueue().poll());
                mergedData.append(System.lineSeparator());

                leftCounter++;
                if(leftCounter == KWayMerge.MAX_SIZE)
                {
                    newElements = left.getProcessor().loadNElements(KWayMerge.MAX_SIZE);
                    left.getQueue().addAll(newElements);
                    leftCounter = 0;
                } // end if

                if(mergedData.length() >= KWayMerge.BUFFER_SIZE) 
                {
                    bufferedWriter.write(mergedData.toString());
                    mergedData.setLength(0); // Clear the buffer
                } // end if
            } // end while

            while(!right.getQueue().isEmpty()) 
            {
                mergedData.append(right.getQueue().poll().toString());
                mergedData.append(System.lineSeparator());

                rightCounter++;
                if(rightCounter == KWayMerge.MAX_SIZE)
                {
                    newElements = right.getProcessor().loadNElements(KWayMerge.MAX_SIZE);
                    right.getQueue().addAll(newElements);
                    rightCounter = 0;
                } // end if 

                if(mergedData.length() >= KWayMerge.BUFFER_SIZE) 
                {
                    bufferedWriter.write(mergedData.toString());
                    mergedData.setLength(0); // Clear the buffer
                } // end if
            } // end while

            bufferedWriter.write(mergedData.toString());
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        } // end try/catch
    } // end merge
} // end KWayMerge