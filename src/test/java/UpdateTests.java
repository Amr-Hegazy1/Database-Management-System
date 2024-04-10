import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.junit.Test;

import com.bplustree.BPlusTree;
import com.db_engine.*;

public class UpdateTests {
    @Test
    public void UpadateWithNonExistentTable() throws DBAppException, IOException{
        try{
            DBApp dbApp = new DBApp();
            dbApp.init();
            String strTableName = "Student";
            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();
            htblColNameType.put("id", "java.lang.Integer");
            htblColNameType.put("name", "java.lang.String");
            htblColNameType.put("gpa", "java.lang.Double");
            dbApp.createTable(strTableName, "id", htblColNameType);
            for(int i = 0; i < 20; i++){
                Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
                htblColNameValue.put("id", i);
                htblColNameValue.put("name", "Student" + i);
                htblColNameValue.put("gpa", 3.0 + i);
                dbApp.insertIntoTable(strTableName, htblColNameValue);
            }

            // update a row
            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
            htblColNameValue.put("name", "Student" + 21);
            assertThrows(DBAppException.class, () ->dbApp.updateTable("strTableName", "0", htblColNameValue));

        }finally{
            cleanUp();
        }
    }
    // first thing to talk about
    // @Test
    // public void UpadateWithNonExistentColumn() throws DBAppException, IOException{
    //     try{
    //         DBApp dbApp = new DBApp();
    //         dbApp.init();
    //         String strTableName = "Student";
    //         Hashtable<String, String> htblColNameType = new Hashtable<String, String>();
    //         htblColNameType.put("id", "java.lang.Integer");
    //         htblColNameType.put("name", "java.lang.String");
    //         htblColNameType.put("gpa", "java.lang.Double");
    //         dbApp.createTable(strTableName, "id", htblColNameType);
    //         for(int i = 0; i < 20; i++){
    //             Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
    //             htblColNameValue.put("id", i);
    //             htblColNameValue.put("name", "Student" + i);
    //             htblColNameValue.put("gpa", 3.0 + i);
    //             dbApp.insertIntoTable(strTableName, htblColNameValue);
    //         }

    //         // update a row
    //         Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
    //         htblColNameValue.put("sampo", "Student" + 21);
    //         assertThrows(DBAppException.class, () ->dbApp.updateTable(strTableName, "0", htblColNameValue));

    //     }finally{
    //         cleanUp();
    //     }
    // }

    @Test 
    public void UpadateWithWrongDataType() throws DBAppException, IOException{
        try{
            DBApp dbApp = new DBApp();
            dbApp.init();
            String strTableName = "Student";
            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();
            htblColNameType.put("id", "java.lang.Integer");
            htblColNameType.put("name", "java.lang.String");
            htblColNameType.put("gpa", "java.lang.Double");
            dbApp.createTable(strTableName, "id", htblColNameType);
            for(int i = 0; i < 20; i++){
                Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
                htblColNameValue.put("id", i);
                htblColNameValue.put("name", "Student" + i);
                htblColNameValue.put("gpa", 3.0 + i);
                dbApp.insertIntoTable(strTableName, htblColNameValue);
            }

            // update a row
            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
            htblColNameValue.put("name",  345.75);
            assertThrows(DBAppException.class, () ->dbApp.updateTable("strTableName", "0", htblColNameValue));

        }finally{
            cleanUp();
        }
    }
    // second to be told
    // @Test
    // public void UpadateWithWrongDataTypeinClus() throws DBAppException, IOException{
    //     try{
    //         DBApp dbApp = new DBApp();
    //         dbApp.init();
    //         String strTableName = "Student";
    //         Hashtable<String, String> htblColNameType = new Hashtable<String, String>();
    //         htblColNameType.put("id", "java.lang.Integer");
    //         htblColNameType.put("name", "java.lang.String");
    //         htblColNameType.put("gpa", "java.lang.Double");
    //         dbApp.createTable(strTableName, "id", htblColNameType);
    //         for(int i = 0; i < 20; i++){
    //             Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
    //             htblColNameValue.put("id", i);
    //             htblColNameValue.put("name", "Student" + i);
    //             htblColNameValue.put("gpa", 3.0 + i);
    //             dbApp.insertIntoTable(strTableName, htblColNameValue);
    //         }

    //         // update a row
    //         Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
    //         htblColNameValue.put("name", "Student" + 21);
    //         assertThrows(DBAppException.class, () ->dbApp.updateTable(strTableName, "asfafaf", htblColNameValue));

    //     }finally{
    //         cleanUp();
    //     }
    // }
    
    @Test
    public void updateWithoutIndex() throws DBAppException, IOException{
        try{
            DBApp dbApp = new DBApp();

            dbApp.init();

            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();

            htblColNameType.put("id", "java.lang.Integer");

            htblColNameType.put("name", "java.lang.String");

            htblColNameType.put("gpa", "java.lang.Double");

            dbApp.createTable(strTableName, "id", htblColNameType);

            // insert 20 rows

            for(int i = 0; i < 20; i++){
                Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
                htblColNameValue.put("id", i);
                htblColNameValue.put("name", "Student" + i);
                htblColNameValue.put("gpa", 3.0 + i);
                dbApp.insertIntoTable(strTableName, htblColNameValue);
            }

            // update a row

            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();

            htblColNameValue.put("name", "Student" + 21);

            dbApp.updateTable(strTableName, "0", htblColNameValue);

            // check that the row is updated and the other rows are not

            String pagesPath = "tables/" + strTableName;

            File pagesDir = new File(pagesPath);

            File[] pages = pagesDir.listFiles();

            for(File page : pages){
                // check if the file name is in the format page_i.class
                if(!page.getName().matches("page_\\d+\\.class")){
                    continue;
                }
                
                Page p = Page.deserialize(page.getPath());
                for(Tuple tuple : p.getTuples()){
                    if(tuple.getColumnValue("id").equals(0)){
                        assert tuple.getColumnValue("name").equals("Student" + 21);
                    }else{
                        assert tuple.getColumnValue("name").equals("Student" + tuple.getColumnValue("id"));
                    }
                }
            }
        }finally{
            cleanUp();
        }
            
    }


    protected static void cleanUp() throws IOException{
        try{
            // delete tables directory

            String tablesPath = "tables/";

            

            Path dir = Paths.get(tablesPath); //path to the directory  
            Files
                .walk(dir) // Traverse the file tree in depth-first order
                .sorted(Comparator.reverseOrder())
                .forEach(path -> {
                    try {
                        
                        Files.delete(path);  //delete each file or directory
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        }catch(Exception e){
        
        }

        try{
            
            // delete Indicies directory

            String indiciesPath = "Indicies/";

            Path dir2 = Paths.get(indiciesPath); //path to the directory
            Files
                .walk(dir2) // Traverse the file tree in depth-first order
                .sorted(Comparator.reverseOrder())
                .forEach(path -> {
                    try {
                       
                        Files.delete(path);  //delete each file or directory
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        }catch(Exception e){
        
        }
        try{
            // delete metadata.csv

            File metadata = new File("metadata.csv");
            metadata.delete();
        }catch(Exception e){
            
        }



    }
}
