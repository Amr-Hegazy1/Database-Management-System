import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import org.junit.Test;

public class TestCases {

    

    public void cleanUp() throws IOException{

        // delete tables directory

        String tablesPath = "tables/";

        

        Path dir = Paths.get(tablesPath); //path to the directory  
        Files
            .walk(dir) // Traverse the file tree in depth-first order
            .sorted(Comparator.reverseOrder())
            .forEach(path -> {
                try {
                    System.out.println("Deleting: " + path);
                    Files.delete(path);  //delete each file or directory
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        // delete metadata.csv

        File metadata = new File("metadata.csv");
        metadata.delete();

        

    }

}
