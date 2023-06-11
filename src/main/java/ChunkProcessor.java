import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChunkProcessor {
    private String chunkFilePath;
    private RandomAccessFile randomAccessFile;
    private long previousPosition;

    public ChunkProcessor(String chunkFilePath)
    {
        this.chunkFilePath = chunkFilePath;
        try {
            this.randomAccessFile = new RandomAccessFile(chunkFilePath, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } // end try/catch
    } // end initializing constructor

    public List<Integer> loadNElements(int N) {
        List<Integer> elements = new ArrayList<>();

        try {
            String line;
            int element;
            int count = 0;

            while(true) 
            {
                this.previousPosition = this.randomAccessFile.getFilePointer();

                line = this.randomAccessFile.readLine();

                if(count == N)
                {
                    break;
                } // end if

                if(line == null)
                {
                    break;
                } // end if
                
                element = Integer.parseInt(line);
                elements.add(element);
                count++;
            } // end while
            
            this.randomAccessFile.seek(previousPosition);
        } catch (IOException e) {
            e.printStackTrace();
        } // end try/catch

        if(elements.size() == 0)
        {
            return null;
        } // end if

        return elements;
    } // end loadNElements

    public void deleteChunkFromFile()
    {
        this.close();

        File fileToDelete = new File(this.chunkFilePath);
        boolean isDeleted = fileToDelete.delete();
        
        if (isDeleted) {
            System.out.println("File deleted successfully. " + fileToDelete.getAbsolutePath());
        } else {
            System.out.println("Failed to delete the file." + fileToDelete.getAbsolutePath());
        }
    } // end deleteChunk

    public void close() 
    {
        try {
            if(this.randomAccessFile != null) 
            {
                this.randomAccessFile.close();
            } // end if
        } catch (IOException e) {
            e.printStackTrace();
        } // end try/catch
    } // end close

    @Override
    public boolean equals(Object obj) {
        if(this == obj) 
        {
            return true;
        } // end if 

        if(obj == null || getClass() != obj.getClass()) 
        {
            return false;
        } // end if

        ChunkProcessor other = (ChunkProcessor) obj;
        return Objects.equals(randomAccessFile, other.randomAccessFile);
    } // end equals
} // end ChunkProcessor