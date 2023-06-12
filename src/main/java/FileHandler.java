import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    public FileHandler()
    {

    } // end default constructor

    public <T> List<T> loadChunkFromFile(String filePathName, ElementConverter<T> converter) {
        List<T> chunk = new ArrayList<>();
        String line;
        T element;

        File file = new File(filePathName);
        if(!file.exists()) 
        {
            return null;
        } // end if

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            while((line = reader.readLine()) != null) 
            {
                if(line.equals(""))
                {
                    continue;
                } // end if

                element = converter.convert(line);
                chunk.add(element);
            } // end while

            reader.close();
        } catch(IOException e) {
            e.printStackTrace();
        } // end try/catch

        return chunk;
    } // end loadChunkFromFile

    public <T> void writeChunkToFile(String filePathName, T[] chunk)
    {
        //String chunkFileName = "chunk_" + chunk.toString() + ".txt";

        File chunkFile = new File(filePathName);
        if(chunkFile.exists()) 
        {
            chunkFile.delete();
        } // end if

        try {
            FileWriter writer = new FileWriter(chunkFile);

            for(int i = 0; i < chunk.length; i++) 
            {
                writer.write(chunk[i].toString() + "\n");
            } // end for

            writer.close();
            System.out.println("Chunk file created: " + filePathName);
        } catch(IOException e) {
            e.printStackTrace();
        } // end try/catch
    } // end writeChunksToFiles

    public void deleteChunkFile(String filePathName) {
        File chunkFile = new File(filePathName);

        if(!chunkFile.exists()) 
        {
            System.out.println("Chunk file does not exist: " + filePathName);
            return;
        } // end if

        if(chunkFile.delete()) {
            System.out.println("Deleted chunk file: " + filePathName);
        } else {
            System.out.println("Failed to delete chunk file: " + filePathName);
        } // end if
    } // end deleteChunkFile
} // end FileHandler
