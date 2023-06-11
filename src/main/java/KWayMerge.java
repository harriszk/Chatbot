import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class KWayMerge {
    private static final int MAX_SIZE = 3;
    private List<Queue<Integer>> chunks;
    private List<ChunkProcessor> processors;

    private List<Chunk> tempName;

    public KWayMerge()
    {
        this.chunks = new ArrayList<>();
        this.processors = new ArrayList<>();
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
        ChunkProcessor processor;
        PriorityQueue<Integer> chunkQueue;

        // Load in initial chunks into queues 
        for(String chunk : chunkLocations)
        {
            processor = new ChunkProcessor(chunk);
            this.processors.add(processor);

            loadedChunk = processor.loadNElements(MAX_SIZE);
            if(loadedChunk.get(0) == null)
            {
                break;
            } // end if

            System.out.println("Loading chunk " + loadedChunk);

            chunkQueue = new PriorityQueue<>(MAX_SIZE);
            for(Integer element : loadedChunk)
            {
                chunkQueue.add(element);
            } // end for
            this.chunks.add(chunkQueue);
        } // end for
    } // end mergeAllChunks

    private void merge(int chunkIndex1, int chunkIndex2, String outputFile) 
    {
        try(FileWriter fileWriter = new FileWriter(outputFile, true)) {
            while(!this.chunks.get(chunkIndex1).isEmpty() && !this.chunks.get(chunkIndex2).isEmpty()) 
            {
                System.out.println(this.chunks.get(chunkIndex1));
                System.out.println(this.chunks.get(chunkIndex2));

                if(this.chunks.get(chunkIndex1).peek() <= this.chunks.get(chunkIndex2).peek()) {
                    fileWriter.write(this.chunks.get(chunkIndex1).poll().toString());
                    fileWriter.write(System.lineSeparator());

                    Integer nextElement = this.processors.get(chunkIndex1).loadNElements(1).get(0);

                    //System.out.println(nextElement);
                    if(nextElement != null)
                    {
                        this.chunks.get(chunkIndex1).add(nextElement);
                    }
                } else {
                    fileWriter.write(this.chunks.get(chunkIndex2).poll().toString());
                    fileWriter.write(System.lineSeparator());

                    Integer nextElement = this.processors.get(chunkIndex2).loadNElements(1).get(0);

                    //System.out.println(nextElement);
                    if(nextElement != null)
                    {
                        this.chunks.get(chunkIndex2).add(nextElement);
                    }
                } // end if
            } // end while

            System.out.println("Out of while");
            System.out.println(this.chunks.get(chunkIndex1));
            System.out.println(this.chunks.get(chunkIndex2));

            // Write the remaining elements from chunk1 or chunk2
            while(!this.chunks.get(chunkIndex1).isEmpty()) 
            {
                fileWriter.write(this.chunks.get(chunkIndex1).poll().toString());
                fileWriter.write(System.lineSeparator());
            } // end while

            while(!this.chunks.get(chunkIndex2).isEmpty()) 
            {
                fileWriter.write(this.chunks.get(chunkIndex2).poll().toString());
                fileWriter.write(System.lineSeparator());
            } // end while

            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } // end try/catch
    } // end merge

} // end KWayMerge
