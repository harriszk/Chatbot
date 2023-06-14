import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List; 

public class ChatLog<T extends Comparable<T>> {
    private static String CHUNK_DIRECTORY = "tmp/chunks/";
    private List<String> chunkLocations = new ArrayList<>();
    private FileHandler fileHandler = new FileHandler();
    private MergeSort sorter = new MergeSort();
    private KWayMerge<T> merger;
    private ElementConverter<T> converter;

    public ChatLog(ElementConverter<T> converter)
    {
        this.converter = converter;
        this.merger = new KWayMerge<>(this.converter);
    } // end default constructor

    public void chunkLogs(String path, int chunkSize)
    {
        List<String> filePaths = this.getFileNames(path);

        if(filePaths == null)
        {
            return;
        } // end if

        System.out.println(filePaths);

        List<T> chunk = new ArrayList<>();
        String line, chunkLocation;
        int elementCount = 0;
        T element;

        for(String filePath : filePaths)
        {
            try(BufferedReader reader = new BufferedReader(new FileReader(path + "/" + filePath))) {
                while((line = reader.readLine()) != null) 
                {
                    if(line == "")
                    {
                        continue;
                    } // end if

                    element = converter.convert(line);
                    chunk.add(element);
                    elementCount++;

                    if(elementCount == chunkSize) 
                    {
                        chunk = sorter.sort(chunk);
                        chunkLocation = ChatLog.CHUNK_DIRECTORY + "chunk_"  + System.currentTimeMillis() + ".txt";
                        fileHandler.writeChunkToFile(chunkLocation, chunk.toArray());

                        chunkLocations.add(chunkLocation);
                        chunk.clear();
                        elementCount = 0;
                    } // end if
                } // end while
            } catch (IOException e) {
                e.printStackTrace();
            } // end try/catch
        } // end for

        // Write any remaining entries as the last chunk
        if(!chunk.isEmpty()) 
        {
            chunk = sorter.sort(chunk);
            chunkLocation = ChatLog.CHUNK_DIRECTORY + "chunk_"  + System.currentTimeMillis() + ".txt";
            fileHandler.writeChunkToFile(chunkLocation, chunk.toArray());
            chunkLocations.add(chunkLocation);
            chunk.clear();
        } // end if
    } // end chunkLogs

    public void mergeChunks()
    {
        System.out.println("=================== mergeChunks() ===================");
        System.out.println(chunkLocations);
        System.out.println(chunkLocations.size());

        merger.mergeAllChunks(chunkLocations);
    } // end mergeChunks

    public List<String> getChunkLocations()
    {
        return chunkLocations;
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