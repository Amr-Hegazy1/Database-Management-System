import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.junit.Test;

import com.db_engine.*;

public class TestCases {

    @Test
    public void createTable() throws DBAppException, IOException {

        try{
            DBApp dbApp = new DBApp();
            dbApp.init();
            String strTableName = "Student";
            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();
            htblColNameType.put("id", "java.lang.Integer");
            htblColNameType.put("name", "java.lang.String");
            htblColNameType.put("gpa", "java.lang.Double");
            dbApp.createTable(strTableName, "id", htblColNameType);

            // check if the table is created

            File file = new File("tables/" + strTableName + "/" + strTableName + ".class");
            assert file.exists();

            // check if the metadata is updated

            Metadata metadata = new Metadata();
            
            // check if the table is in the metadata
            assert metadata.checkTableName(strTableName);

            // check if the columns are in the metadata

            assert metadata.checkColumnName(strTableName, "id");

            assert metadata.checkColumnName(strTableName, "name");

            assert metadata.checkColumnName(strTableName, "gpa");

            // check if the column types are in the metadata

            assert metadata.getColumnType(strTableName, "id").equals("java.lang.Integer");

            assert metadata.getColumnType(strTableName, "name").equals("java.lang.String");

            assert metadata.getColumnType(strTableName, "gpa").equals("java.lang.Double");

            // check if the primary key is in the metadata

            assert metadata.getClusteringKey(strTableName).equals("id");

            // check that no columns are indexed

            assert metadata.isColumnIndexed(strTableName, "id") == false;

            assert metadata.isColumnIndexed(strTableName, "name") == false;

            assert metadata.isColumnIndexed(strTableName, "gpa") == false;

            

        


        }finally{
            cleanUp();
        }
        
        

        
    }

    @Test
    public void createTableWithInvalidTypes() throws DBAppException, IOException {

        try{

            DBApp dbApp = new DBApp();
            dbApp.init();
            String strTableName = "Student";
            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();
            htblColNameType.put("id", "java.lang.Integer");
            htblColNameType.put("name", "java.lang.String");
            htblColNameType.put("gpa", "java.lang.Double");
            htblColNameType.put("age", "java.lang.Integer");
            htblColNameType.put("dob", "java.util.Date");
            
            
            assertThrows(DBAppException.class, () -> {
                dbApp.createTable(strTableName, "id", htblColNameType);
            });



            


           

            
            
        }finally{
            cleanUp();
        }
        
        

        
    }

    @Test
    public void createDuplicateTable() throws DBAppException, IOException {

        try{

            DBApp dbApp = new DBApp();
            dbApp.init();
            String strTableName = "Student";
            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();
            htblColNameType.put("id", "java.lang.Integer");
            htblColNameType.put("name", "java.lang.String");
            htblColNameType.put("gpa", "java.lang.Double");
            dbApp.createTable(strTableName, "id", htblColNameType);

            

            assertThrows(DBAppException.class, () -> {
                dbApp.createTable(strTableName, "id", htblColNameType);
            });

        }finally{
            cleanUp();
        }
        
        

        
    }

    

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
