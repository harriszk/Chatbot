import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class ChunkProcessor {
    //private String chunkFilePath;
    private RandomAccessFile randomAccessFile;
    private long previousPosition;

    public ChunkProcessor(String chunkFilePath)
    {
        //this.chunkFilePath = chunkFilePath;
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
                if(line == null || count == N)
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

        System.out.println(elements);

        return elements;
    } // end loadNElements

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
} // end ChunkProcessor