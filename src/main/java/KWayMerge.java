import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class KWayMerge {
    private static final String CHUNKS_DIRECTORY = "tmp/chunks/testChunks/";
    private static final int MAX_SIZE = 3;
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

            loadedChunk = chunkProcessor.loadNElements(MAX_SIZE);
            if(loadedChunk.get(0) == null)
            {
                chunkProcessor.deleteChunk();
                break;
            } // end if

            System.out.println("Loading chunk " + loadedChunk);

            chunkQueue = new PriorityQueue<>(loadedChunk);
            
            left = new Chunk(chunkQueue, chunkProcessor);
            this.chunksToMerge.add(left);
        } // end for

        // Merge chunks until only one chunk remains
        while(this.chunksToMerge.size() > 1) 
        {
            left = this.chunksToMerge.poll();
            right = this.chunksToMerge.poll();

            String mergedChunkLocation;

            if(this.chunksToMerge.size() == 0) {
                mergedChunkLocation = CHUNKS_DIRECTORY + "finalMergedChunks.txt";
            } else {
                mergedChunkLocation = CHUNKS_DIRECTORY + "merged_chunk_" + System.currentTimeMillis() + ".txt";
            } // end if
            merge(left, right, mergedChunkLocation);

            chunkProcessor = new ChunkProcessor(mergedChunkLocation);
            chunkQueue = new PriorityQueue<>(chunkProcessor.loadNElements(MAX_SIZE));
            mergedChunk = new Chunk(chunkQueue, chunkProcessor);
            this.chunksToMerge.add(mergedChunk);

            left.getProcessor().deleteChunk();
            right.getProcessor().deleteChunk();
        } // end while

        /*
        if(this.chunksToMerge.size() == 1)
        {
            left = this.chunksToMerge.poll();
            chunkQueue = new PriorityQueue<>();
            right = new Chunk(chunkQueue, null);

            String mergedChunkLocation = CHUNKS_DIRECTORY + "finalMergedChunks.txt";
            merge(left, right, mergedChunkLocation);
            left.getProcessor().deleteChunk();
        }
        */
    } // end mergeAllChunks

    private void merge(Chunk left, Chunk right, String outputFile) 
    {
        try {
            FileWriter fileWriter = new FileWriter(outputFile, true);

            while(!left.getQueue().isEmpty() && !right.getQueue().isEmpty())
            {
                if(left.getQueue().peek() <= right.getQueue().peek()) {
                    fileWriter.write(left.getQueue().poll().toString());
                    fileWriter.write(System.lineSeparator());

                    Integer nextElement = left.getProcessor().loadNElements(1).get(0);
                    if(nextElement != null)
                    {
                        left.getQueue().add(nextElement);
                    } // end if
                } else {
                    fileWriter.write(right.getQueue().poll().toString());
                    fileWriter.write(System.lineSeparator());

                    Integer nextElement = right.getProcessor().loadNElements(1).get(0);
                    if(nextElement != null)
                    {
                        right.getQueue().add(nextElement);
                    } // end if
                } // end if
            } // end while

            // Write the remaining elements from chunk1 or chunk2
            while(!left.getQueue().isEmpty()) 
            {
                fileWriter.write(left.getQueue().poll().toString());
                fileWriter.write(System.lineSeparator());

                Integer nextElement = left.getProcessor().loadNElements(1).get(0);
                if(nextElement != null)
                {
                    left.getQueue().add(nextElement);
                } // end if
            } // end while

            while(!right.getQueue().isEmpty()) 
            {
                fileWriter.write(right.getQueue().poll().toString());
                fileWriter.write(System.lineSeparator());

                Integer nextElement = right.getProcessor().loadNElements(1).get(0);
                if(nextElement != null)
                {
                    right.getQueue().add(nextElement);
                } // end if
            } // end while

            //fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        } // end try/catch
    } // end merge
} // end KWayMerge