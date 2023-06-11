import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class KWayMerge {
    private static final int MAX_SIZE = 3;
    private Queue<Chunk> chunksToMerge;
    public KWayMerge()
    {
        this.chunksToMerge = new LinkedList<>();
    } // end default construtor

    public void mergeAllChunks(List<String> chunkLocations)
    {
        // Load in the N chunks from the given chunkLocations list.
        // Each chunk will be given its own list whose size may not exceed MAX_SIZE.
        // Once all chunks have been max loaded into its list we carry on with the merging.
        // After each merge, which chunk the element was taken out of we get another element
        // from its file. If there are no more elements in the file then we will no longer
        // check to add more files to that chunks list.

        

        List<Integer> loadedChunk;

        PriorityQueue<Integer> chunkQueue;
        ChunkProcessor chunkProcessor;
        Chunk chunk;
        
        // Load in initial chunks into queues 
        for(String chunkLocation : chunkLocations)
        {
            chunkProcessor = new ChunkProcessor(chunkLocation);

            loadedChunk = chunkProcessor.loadNElements(MAX_SIZE);
            if(loadedChunk.get(0) == null)
            {
                break;
            } // end if

            System.out.println("Loading chunk " + loadedChunk);

            chunkQueue = new PriorityQueue<>(loadedChunk);
            
            chunk = new Chunk(chunkQueue, chunkProcessor);
            this.chunksToMerge.add(chunk);
        } // end for

        // Merge chunks until only one chunk remains
        while(this.chunksToMerge.size() > 1) 
        {
            Chunk left = this.chunksToMerge.poll();
            Chunk right = this.chunksToMerge.poll();

            String mergedChunkLocation = "tmp/chunks/testChunks" + "/merged_chunk_" + System.currentTimeMillis() + ".txt";
            merge(left, right, mergedChunkLocation);

            chunkProcessor = new ChunkProcessor(mergedChunkLocation);
            chunkQueue = new PriorityQueue<>(chunkProcessor.loadNElements(MAX_SIZE));
            Chunk mergedChunk = new Chunk(chunkQueue, chunkProcessor);
            this.chunksToMerge.add(mergedChunk);
        } // end while

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
            } // end while

            while(!right.getQueue().isEmpty()) 
            {
                fileWriter.write(right.getQueue().poll().toString());
                fileWriter.write(System.lineSeparator());
            } // end while

            //fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        } // end try/catch
    } // end merge
} // end KWayMerge