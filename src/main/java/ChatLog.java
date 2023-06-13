import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChatLog {
    private MergeSort sorter = new MergeSort();
    private List<String> chunkLocations = new ArrayList<>();
    private IntegerConverter converter = new IntegerConverter();
    private FileHandler fileHandler = new FileHandler();
    private static String CHUNK_DIRECTORY = "tmp/chunks/";

    public ChatLog()
    {

    } // end default constructor

    public void chunkLogs(String path, int chunkSize)
    {
        List<String> filePaths = this.getFileNames(path);

        if(filePaths == null)
        {
            return;
        } // end if

        System.out.println(filePaths);

        List<Integer> chunk = new ArrayList<>();
        String line, chunkLocation;
        int numberOfChunks = 1;
        int elementCount = 0;
        int element;

        for(String filePath : filePaths)
        {
            try(BufferedReader reader = new BufferedReader(new FileReader(path + "/" + filePath))) {
                while((line = reader.readLine()) != null) 
                {
                    if(line == "")
                    {
                        continue;
                    } // end if

                    // Process the line and extract the entry
                    element = converter.convert(line);
                    chunk.add(element);
                    elementCount++;

                    if(elementCount == chunkSize) 
                    {
                        chunk = this.sorter.sort(chunk);
                        // Write the chunk to a chunk file
                        chunkLocation = ChatLog.CHUNK_DIRECTORY + "chunk"  + numberOfChunks + ".txt";
                        this.fileHandler.writeChunkToFile(chunkLocation, chunk.toArray());

                        this.chunkLocations.add(chunkLocation);
                        chunk.clear();
                        elementCount = 0;
                        numberOfChunks++;
                    } // end if
                } // end while
            } catch (IOException e) {
                e.printStackTrace();
            } // end try/catch
        } // end for

        // Write any remaining entries as the last chunk
        if(!chunk.isEmpty()) 
        {
            chunk = this.sorter.sort(chunk);
            chunkLocation = ChatLog.CHUNK_DIRECTORY + "chunk"  + numberOfChunks + ".txt";
            this.fileHandler.writeChunkToFile("tmp/chunks/chunk" + numberOfChunks + ".txt", chunk.toArray());
            this.chunkLocations.add(chunkLocation);
            chunk.clear();
        } // end if
    } // end chunkLogs

    public List<String> getChunkLocations()
    {
        return this.chunkLocations;
    } // end getChunkLocations

    private List<String> getFileNames(String directoryPath) 
    {
        List<String> fileNames = new ArrayList<>();

        File directory = new File(directoryPath);

        if(!directory.isDirectory()) 
        {
            return null;
        } // end if

        File[] files = directory.listFiles();
        if(files == null) 
        {
            return null;
        } // end if

        for(File file : files) 
        {
            String fileName = file.getName();
            if(fileName.endsWith(".txt"))
            {
                fileNames.add(fileName);
            } // end if
        } // end for

        return fileNames;
    } // end getFileNames
} // end ChatLog