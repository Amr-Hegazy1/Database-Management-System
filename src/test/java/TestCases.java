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
    public void insertWithClusteringKeyIndex() throws DBAppException, IOException {
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
            htblColNameValue.put("id", 20);
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

    @Test
    public void insertWithNonClusteringKeyIndex() throws DBAppException, IOException {
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

            dbApp.createIndex(strTableName, "name", "nameIndex");

            // check that index contains correct values

            BPlusTree tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "nameIndex.class");

            for(int i = 0; i < 20; i++){
                assert tree.query("Student" + i) != null && tree.query("Student" + i).size() == 1 && ((Tuple) tree.query("Student" + i).get(0)).getColumnValue("name").equals("Student" + i);
            }

            // insert a new row

            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
            htblColNameValue.put("id", 20);
            htblColNameValue.put("name", "Student" + 20);
            htblColNameValue.put("gpa", 3.0 + 20);
            dbApp.insertIntoTable(strTableName, htblColNameValue);

            // check that index contains correct values

            tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "nameIndex.class");
            
            for(int i = 0; i < 21; i++){
                
                assert tree.query("Student" + i) != null && tree.query("Student" + i).size() == 1 && ((Tuple) tree.query("Student" + i).get(0)).getColumnValue("name").equals("Student" + i);
            }

        }finally{
            cleanUp();
        }
    }

    @Test
    public void insertWithMultipleIndexes() throws DBAppException, IOException {
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

            dbApp.createIndex(strTableName, "name", "nameIndex");

            dbApp.createIndex(strTableName, "gpa", "gpaIndex");

            // check that index contains correct values

            BPlusTree tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "nameIndex.class");

            for(int i = 0; i < 20; i++){
                assert tree.query("Student" + i) != null && tree.query("Student" + i).size() == 1 && ((Tuple) tree.query("Student" + i).get(0)).getColumnValue("name").equals("Student" + i);
            }

            tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "gpaIndex.class");

            for(int i = 0; i < 20; i++){
                assert tree.query(3.0 + i) != null && tree.query(3.0 + i).size() == 1 && ((Tuple) tree.query(3.0 + i).get(0)).getColumnValue("gpa").equals(3.0 + i);
            }

            // insert a new row

            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
            htblColNameValue.put("id", 20);
            htblColNameValue.put("name", "Student20");
            htblColNameValue.put("gpa", 3.0 + 20);
            dbApp.insertIntoTable(strTableName, htblColNameValue);

            // check that index contains correct values

            tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "nameIndex.class");

            for(int i = 0; i < 21; i++){
                assert tree.query("Student" + i) != null && tree.query("Student" + i).size() == 1 && ((Tuple) tree.query("Student" + i).get(0)).getColumnValue("name").equals("Student" + i);
            }

            tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "gpaIndex.class");

            for(int i = 0; i < 21; i++){
                assert tree.query(3.0 + i) != null && tree.query(3.0 + i).size() == 1 && ((Tuple) tree.query(3.0 + i).get(0)).getColumnValue("gpa").equals(3.0 + i);
            }

        }finally{

            cleanUp();
        }

    }

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

    @Test
    public void updateWithoutIndexMultiplePages() throws DBAppException, IOException{
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

            for(int i = 0; i < 2000; i++){
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

    @Test
    public void updateWithIndex() throws DBAppException, IOException{
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

            dbApp.createIndex(strTableName, "name", "nameIndex");

            // update a row

            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();

            htblColNameValue.put("name", "Student20");

            dbApp.updateTable(strTableName, "0", htblColNameValue);

            // check that the index is updated

            BPlusTree tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "nameIndex.class");

            for(int i = 0; i < 21; i++){
                if(i == 0){
                    assert tree.query("Student" + i).size() == 0;
                }else{
                    assert tree.query("Student" + i) != null && tree.query("Student" + i).size() == 1 && ((Tuple) tree.query("Student" + i).get(0)).getColumnValue("name").equals("Student" + i);
                }
            }
            
        }finally{
            cleanUp();
        }
            
    }

    @Test
    public void updateWithMultipleIndexes() throws DBAppException, IOException{
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

            dbApp.createIndex(strTableName, "name", "nameIndex");

            dbApp.createIndex(strTableName, "gpa", "gpaIndex");

            // update a row

            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();

            htblColNameValue.put("name", "Student20");

            dbApp.updateTable(strTableName, "0", htblColNameValue);

            // check that the indexes are updated

            BPlusTree tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "nameIndex.class");

            for(int i = 0; i < 21; i++){
                if(i == 0){ 
                    assert tree.query("Student" + i).size() == 0;
                }else{
                    assert tree.query("Student" + i) != null && tree.query("Student" + i).size() == 1 && ((Tuple) tree.query("Student" + i).get(0)).getColumnValue("name").equals("Student" + i);
                }
            }

            tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "gpaIndex.class");

            for(int i = 0; i < 21; i++){
                if(i == 0){
                    System.out.println(tree.query(3.0 + i));
                    assert tree.query(3.0 + i).size() == 0;
                }else{
                    assert tree.query(3.0 + i) != null && tree.query(3.0 + i).size() == 1 && ((Tuple) tree.query(3.0 + i).get(0)).getColumnValue("gpa").equals(3.0 + i);
                }
                
            }

        }finally{
            cleanUp();
        }
    }


    @Test
    public void deleteWithoutIndex() throws DBAppException, IOException{
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

            // delete a row

            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
            
            htblColNameValue.put("id", 0);

            dbApp.deleteFromTable(strTableName, htblColNameValue);

            // check that the row is deleted and the other rows are not

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
                        assert false;
                    }
                }
            }
        }finally{
            cleanUp();
        }
            
    }


    @Test
    public void deleteWithoutIndexMultiplePages() throws DBAppException, IOException{
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

            for(int i = 0; i < 2000; i++){
                Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
                htblColNameValue.put("id", i);
                htblColNameValue.put("name", "Student");
                htblColNameValue.put("gpa", 3.0 + i);
                dbApp.insertIntoTable(strTableName, htblColNameValue);
            }

            // delete a row

            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
            
            htblColNameValue.put("name", "Student");

            dbApp.deleteFromTable(strTableName, htblColNameValue);

            // check that the row is deleted and the other rows are not

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
                        assert false;
                    }
                }
            }
        }finally{
            cleanUp();
        }
            
    }

    @Test
    public void deleteWithClusteringKeyIndex() throws DBAppException, IOException{
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

            // delete a row

            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
            
            htblColNameValue.put("id", 0);

            dbApp.deleteFromTable(strTableName, htblColNameValue);

            // check that the row is deleted and the other rows are not

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
                        assert false;
                    }
                }
            }

            // check that the index is updated

            BPlusTree tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "idIndex.class");

            for(int i = 0; i < 20; i++){
                if(i == 0){
                    assert tree.query(i).size() == 0;
                }else{
                    assert tree.query(i) != null && tree.query(i).size() == 1 && ((Tuple) tree.query(i).get(0)).getColumnValue("id").equals(i);
                }
            }
        }finally{
            cleanUp();
        }
    }

    @Test
    public void deleteWithNonClusteringKeyIndex() throws DBAppException, IOException{
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

            dbApp.createIndex(strTableName, "name", "nameIndex");

            // delete a row

            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
            
            htblColNameValue.put("name", "Student0");

            dbApp.deleteFromTable(strTableName, htblColNameValue);

            // check that the row is deleted and the other rows are not

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
                    if(tuple.getColumnValue("name").equals("Student0")){
                        assert false;
                    }
                }
            }

            // check that the index is updated

            BPlusTree tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "nameIndex.class");

            for(int i = 0; i < 20; i++){
                if(i == 0){
                    assert tree.query("Student0").size() == 0;
                }else{
                    assert tree.query("Student" + i) != null && tree.query("Student" + i).size() == 1 && ((Tuple) tree.query("Student" + i).get(0)).getColumnValue("name").equals("Student" + i);

                }
            }
        }finally{
            cleanUp();
        }
    }

    @Test
    public void deleteWithMultipleIndexes() throws DBAppException, IOException{
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

            dbApp.createIndex(strTableName, "name", "nameIndex");

            dbApp.createIndex(strTableName, "gpa", "gpaIndex");

            // delete a row

            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
            
            htblColNameValue.put("name", "Student0");

            dbApp.deleteFromTable(strTableName, htblColNameValue);

            // check that the row is deleted and the other rows are not

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
                    if(tuple.getColumnValue("name").equals("Student0")){
                        assert false;
                    }
                }
            }

            // check that the indexes are updated

            BPlusTree tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "nameIndex.class");

            for(int i = 0; i < 20; i++){
                if(i == 0){
                    assert tree.query("Student0").size() == 0;
                }else{
                    assert tree.query("Student" + i) != null && tree.query("Student" + i).size() == 1 && ((Tuple) tree.query("Student" + i).get(0)).getColumnValue("name").equals("Student" + i);

                }
            }

            tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "gpaIndex.class");

            for(int i = 0; i < 20; i++){
                assert tree.query(3.0 + i) != null && tree.query(3.0 + i).size() == 1 && ((Tuple) tree.query(3.0 + i).get(0)).getColumnValue("gpa").equals(3.0 + i);
            }
        }finally{
            cleanUp();
        }
    }



    @Test
    public void selectWithoutIndexWithId() throws DBAppException, IOException, ClassNotFoundException{
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

            // select all rows

            SQLTerm[] arrSQLTerms = new SQLTerm[1];
            String[] strarrOperators = new String[0];

            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = strTableName;
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = 5;

            Iterator iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            for(int i = 0; i < 20; i++){
                if(i == 5){
                    assert iterator.hasNext();
                    Tuple tuple = (Tuple) iterator.next();
                    assert tuple.getColumnValue("id").equals(5);
                    assert tuple.getColumnValue("name").equals("Student5");
                    assert tuple.getColumnValue("gpa").equals(3.0 + 5);
                }else{
                    assert !iterator.hasNext();
                }
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

    public static void main(String[] args) {
        TestCases testCases = new TestCases();
        try {
            
            testCases.insertWithClusteringKeyIndex();
           
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                testCases.cleanUp();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
