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

    @Test
    public void createIndexInserts() throws DBAppException, IOException {
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

            dbApp.createIndex(strTableName, "id", "idIndex");

            // check that index contains correct values

            BPlusTree tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "idIndex.class");

            for(int i = 0; i < 20; i++){
                assert tree.query(i) != null && tree.query(i).size() == 1 && ((Tuple) tree.query(i).get(0)).getColumnValue("id").equals(i);
            }

        }finally{
            cleanUp();
        }

        
    }

    @Test
    public void insertInIndex() throws DBAppException, IOException {
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

            dbApp.createIndex(strTableName, "id", "idIndex");

            // check that index contains correct values

            BPlusTree tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "idIndex.class");

            for(int i = 0; i < 20; i++){
                assert tree.query(i) != null && tree.query(i).size() == 1 && ((Tuple) tree.query(i).get(0)).getColumnValue("id").equals(i);
            }

            // insert a new row

            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
            htblColNameValue.put("id", 21);
            htblColNameValue.put("name", "Student" + 21);
            htblColNameValue.put("gpa", 3.0 + 21);
            dbApp.insertIntoTable(strTableName, htblColNameValue);

            // check that index contains correct values

            tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "idIndex.class");
            
            for(int i = 0; i < 21; i++){
                assert tree.query(i) != null && tree.query(i).size() == 1 && ((Tuple) tree.query(i).get(0)).getColumnValue("id").equals(i);
            }

        }finally{
            cleanUp();
        }
    }

    

    private void cleanUp() throws IOException{
        try{
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
                        System.out.println("Deleting: " + path);
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
