import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KWayMerge {
    private static final int MAX_SIZE = 3;
    private List<List<Integer>> chunks;

    public KWayMerge()
    {
        this.chunks = new ArrayList<>();
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

        for(String chunk : chunkLocations)
        {
            /*
            loadedChunk = loadChunkFromFile(chunk);

            if(loadedChunk != null)
            {
                this.chunks.add(loadedChunk);
            } // end if
            */
        } // end for

    } // end mergeAllChunks

    private void merge(List<Integer> chunk1, List<Integer> chunk2)
    {

    } // end merge
} // end KWayMerge
