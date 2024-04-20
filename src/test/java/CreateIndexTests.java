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


public class CreateIndexTests {


    @Test
    public void createDuplicateIndexName() throws DBAppException, IOException {
        try{
            DBApp dbApp = new DBApp();

            dbApp.init();

            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();
            htblColNameType.put("id", "java.lang.Integer");
            htblColNameType.put("name", "java.lang.String");
            htblColNameType.put("gpa", "java.lang.Double");

            dbApp.createTable(strTableName, "id", htblColNameType);

            

            dbApp.createIndex(strTableName, "name", "nameIndex");

            assertThrows(DBAppException.class, () -> dbApp.createIndex(strTableName, "name", "nameIndex"));



            



        }finally{
            cleanUp();
        }
    }

    @Test
    public void createIndexOnNonExistingTable() throws DBAppException, IOException {
        try{
            DBApp dbApp = new DBApp();

            dbApp.init();

            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();
            htblColNameType.put("id", "java.lang.Integer");
            htblColNameType.put("name", "java.lang.String");
            htblColNameType.put("gpa", "java.lang.Double");

            dbApp.createTable(strTableName, "id", htblColNameType);

            

            assertThrows(DBAppException.class, () -> dbApp.createIndex("Student2", "name", "nameIndex"));
        }finally{
            cleanUp();
        }
    }

    @Test
    public void createIndexOnNonExistingColumn() throws DBAppException, IOException {
        try{
            DBApp dbApp = new DBApp();

            dbApp.init();

            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();
            htblColNameType.put("id", "java.lang.Integer");
            htblColNameType.put("name", "java.lang.String");
            htblColNameType.put("gpa", "java.lang.Double");

            dbApp.createTable(strTableName, "id", htblColNameType);

            

            assertThrows(DBAppException.class, () -> dbApp.createIndex(strTableName, "name2", "nameIndex"));
        }finally{
            cleanUp();
        }
    }

    @Test
    public void createDuplicateIndexCol() throws DBAppException, IOException {
        try{
            DBApp dbApp = new DBApp();

            dbApp.init();

            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();
            htblColNameType.put("id", "java.lang.Integer");
            htblColNameType.put("name", "java.lang.String");
            htblColNameType.put("gpa", "java.lang.Double");

            dbApp.createTable(strTableName, "id", htblColNameType);

            

            dbApp.createIndex(strTableName, "name", "nameIndex");

            assertThrows(DBAppException.class, () -> dbApp.createIndex(strTableName, "name", "nameIndex2"));
        }finally{
            cleanUp();
        }
    }

    @Test
    public void createIndexOnMultipleTables() throws DBAppException, IOException {
        try{
            DBApp dbApp = new DBApp();

            dbApp.init();

            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();
            htblColNameType.put("id", "java.lang.Integer");
            htblColNameType.put("name", "java.lang.String");
            htblColNameType.put("gpa", "java.lang.Double");

            dbApp.createTable(strTableName, "id", htblColNameType);

            // insert into table

            for(int i = 0; i < 100; i++){
                Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
                htblColNameValue.put("id", i);
                htblColNameValue.put("name", "name" + i);
                htblColNameValue.put("gpa", 0.7 + i);
                dbApp.insertIntoTable(strTableName, htblColNameValue);
            }

            String strTableName2 = "Student2";

            Hashtable<String, String> htblColNameType2 = new Hashtable<String, String>();
            htblColNameType2.put("id", "java.lang.Integer");
            htblColNameType2.put("name", "java.lang.String");
            htblColNameType2.put("gpa", "java.lang.Double");

            dbApp.createTable(strTableName2, "id", htblColNameType2);

            // insert into table

            for(int i = 0; i < 100; i++){
                Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
                htblColNameValue.put("id", i);
                htblColNameValue.put("name", "name200" + i);
                htblColNameValue.put("gpa", 0.7 + i + 1);
                dbApp.insertIntoTable(strTableName2, htblColNameValue);
            }

            dbApp.createIndex(strTableName, "name", "nameIndex");

            dbApp.createIndex(strTableName2, "name", "nameIndex");

            // check if the index is created

            BPlusTree tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "nameIndex.class");

            assertNotNull(tree);

            BPlusTree tree2 = BPlusTree.deserialize("tables/" + strTableName2 + "/" + "nameIndex.class");

            assertNotNull(tree2);

            // check data inside the index

            for(int i = 0; i < 100; i++){
                assert tree.query("name200" + i).size() == 0;
                assert tree.query("name" + i) != null && tree.query("name" + i).size() == 1;

                Pair pair = (Pair) tree.query("name" + i).get(0);

                Comparable clusteringKey = (Comparable) pair.getKey();

                String pageName = (String) pair.getValue();

                Page page = Page.deserialize("tables/" + strTableName + "/" + pageName + ".class");

                int tupleIndex = TestCases.searchTuplesByClusteringKey("id", clusteringKey, page);

                assert tupleIndex != -1;

                Tuple tuple = page.getTupleWithIndex(tupleIndex);
                
                assert tuple.getColumnValue("name").equals("name" + i);
            }

            for(int i = 0; i < 100; i++){
                assert tree2.query("name" + i).size() == 0;
                assert tree2.query("name200" + i) != null && tree2.query("name200" + i).size() == 1;

                Pair pair = (Pair) tree2.query("name200" + i).get(0);

                Comparable clusteringKey = (Comparable) pair.getKey();

                String pageName = (String) pair.getValue();

                Page page = Page.deserialize("tables/" + strTableName2 + "/" + pageName + ".class");

                int tupleIndex = TestCases.searchTuplesByClusteringKey("id", clusteringKey, page);

                assert tupleIndex != -1;

                Tuple tuple = page.getTupleWithIndex(tupleIndex);

                assert tuple.getColumnValue("name").equals("name200" + i);

                
            }



        }finally{
            cleanUp();
        }
    }

    @Test
    public void createDuplicateIndexNameOnDifferentCols() throws DBAppException, IOException {
        try{
            DBApp dbApp = new DBApp();

            dbApp.init();

            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();
            htblColNameType.put("id", "java.lang.Integer");
            htblColNameType.put("name", "java.lang.String");
            htblColNameType.put("gpa", "java.lang.Double");

            dbApp.createTable(strTableName, "id", htblColNameType);

            

            dbApp.createIndex(strTableName, "name", "nameIndex");

            assertThrows(DBAppException.class, () -> dbApp.createIndex(strTableName, "gpa", "nameIndex"));

        }finally{
            cleanUp();
        }
    }

    protected void cleanUp() throws IOException{
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
