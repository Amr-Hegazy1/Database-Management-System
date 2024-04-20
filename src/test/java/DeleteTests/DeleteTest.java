package DeleteTests;

import com.db_engine.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class DeleteTest {
    @Test
    public void deleteAndCheckMinAndMaxDeleted() throws DBAppException {
        try {
            DBApp dbApp = new DBApp();

            dbApp.init();

            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();

            htblColNameType.put("id", "java.lang.Integer");

            htblColNameType.put("name", "java.lang.String");

            htblColNameType.put("gpa", "java.lang.Double");

            dbApp.createTable(strTableName, "id", htblColNameType);

            // insert 2000 rows

            for (int i = 0; i < 2000; i++) {
                Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
                htblColNameValue.put("id", i);
                htblColNameValue.put("name", "Student");
                htblColNameValue.put("gpa", 3.0 + i);
                dbApp.insertIntoTable(strTableName, htblColNameValue);
            }

            // delete a row

            Hashtable<String, Object> htblColNameValue = new Hashtable<>();

            htblColNameValue.put("name", "Student");

            dbApp.deleteFromTable(strTableName, htblColNameValue);

            // check that the row is deleted and the other rows are not

            String pagesPath = "tables/" + strTableName;

            File pagesDir = new File(pagesPath);

            File[] pages = pagesDir.listFiles();

            for (File page : pages) {
                // check if the file name is in the format page_i.class
                if (!page.getName().matches("Student_\\d+\\.class")) {
                    continue;
                }

                Page p = Page.deserialize(page.getPath());
                for (Tuple tuple : p.getTuples()) {
                    assert false;
                }
            }
            Table table = Table.deserialize("tables/" + strTableName + "/" + strTableName + ".class");
            assert table.getMaxVec().isEmpty() && table.getMinVec().isEmpty();
        } finally {
            cleanUp();
        }

    }

    @Test
    public void deleteAndCheckDeletedPage () throws DBAppException{
        try {
            DBApp dbApp = new DBApp();

            dbApp.init();

            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();

            htblColNameType.put("id", "java.lang.Integer");

            htblColNameType.put("name", "java.lang.String");

            htblColNameType.put("gpa", "java.lang.Double");

            dbApp.createTable(strTableName, "id", htblColNameType);

            // insert 3 rows

            for (int i = 1; i <= 3; i++) {
                Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
                htblColNameValue.put("id", i);
                htblColNameValue.put("name", "Student");
                htblColNameValue.put("gpa", 3.0 + i);
                dbApp.insertIntoTable(strTableName, htblColNameValue);
            }

            // delete a row

            Hashtable<String, Object> htblColNameValue = new Hashtable<>();

            htblColNameValue.put("id", 3);

            boolean oneWasFound = false , ThreeWasNotFound = true;
            dbApp.deleteFromTable(strTableName , htblColNameValue);
            Table table = Table.deserialize("tables/" + strTableName + "/" +
                    strTableName + ".class");
            for(String str : table.getPages()){
                Page page = Page.deserialize("tables/" + strTableName + "/" +
                        str+".class");
                for(Tuple tuple : page.getTuples()){
                    oneWasFound |= ((Integer) tuple.getColumnValue("id") == 1);
                    ThreeWasNotFound &= ((Integer) tuple.getColumnValue("id") != 3);
                }
            }

            assert oneWasFound;
            assert ThreeWasNotFound;
        } finally {
            cleanUp();
        }
    }

    @Test
    public void deleteAndCheckMax () throws DBAppException{
        try {
            DBApp dbApp = new DBApp();

            dbApp.init();

            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();

            htblColNameType.put("id", "java.lang.Integer");

            htblColNameType.put("name", "java.lang.String");

            htblColNameType.put("gpa", "java.lang.Double");

            dbApp.createTable(strTableName, "id", htblColNameType);

            // insert 200

            ArrayList<Hashtable<String,Object>> hashtables = new ArrayList<>();

            for (int i = 1; i <= 200; i++) {
                Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
                htblColNameValue.put("id", i);
                htblColNameValue.put("name", "Student");
                htblColNameValue.put("gpa", 3.0 + i);
                dbApp.insertIntoTable(strTableName, htblColNameValue);
                if(i % 20 == 0){
                    htblColNameValue = new Hashtable<>();
                    htblColNameValue.put("id" , i);
                    hashtables.add(htblColNameValue);
                }
            }

            // delete all rows

            for(var h : hashtables){
                dbApp.deleteFromTable(strTableName , h);
            }

            Table table = Table.deserialize("tables/" + strTableName + "/" + strTableName + ".class");
            int lastMax = 19;
            for(String pageName : table.getPages()){
                Page page = Page.deserialize("tables/" + strTableName + "/" + pageName
                + ".class");
                assert lastMax == (Integer) page.Max("id");
                lastMax += 20;
            }
        } finally {
            cleanUp();
        }
    }

    @Test
    public void deleteMultipleRecords() throws DBAppException{
        try {
            DBApp dbApp = new DBApp();

            dbApp.init();

            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<>();

            htblColNameType.put("id", "java.lang.Integer");

            htblColNameType.put("name", "java.lang.String");

            htblColNameType.put("gpa", "java.lang.Double");

            dbApp.createTable(strTableName, "id", htblColNameType);

            for (int i = 1; i <= 200; i++) {
                Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
                htblColNameValue.put("id", i);
                htblColNameValue.put("gpa", 3.0 + i);
                if(i % 20 == 0){
                    htblColNameValue.put("name", "Student");
                }else htblColNameValue.put("name", "Student" + i);
                dbApp.insertIntoTable(strTableName, htblColNameValue);
            }


            Table table = Table.
                    deserialize("tables/" + strTableName + "/" + strTableName + ".class");
            int numOfPagesBefore = table.getPages().size();
            Hashtable<String,Object> ht = new Hashtable<>();
            ht.put("name" , "Student");
            dbApp.deleteFromTable(strTableName , ht);
            table = Table.
                    deserialize("tables/" + strTableName + "/" + strTableName + ".class");
            int numOfPagesAfter = table.getPages().size();
//            assert numOfPagesAfter < numOfPagesBefore;
            dbApp.createIndex("Student" , "name" , "saba7o");
            SQLTerm [] sqlTerms = new SQLTerm[1];
            sqlTerms[0] = new SQLTerm();
            sqlTerms[0]._strTableName = "Student";
            sqlTerms[0]._strColumnName = "name";
            sqlTerms[0]._strOperator = "=";
            sqlTerms[0]._objValue = "Student";
            String [] strArr = new String[0];
            Iterator it = dbApp.selectFromTable(sqlTerms , strArr);
            HashSet<Tuple> hs = new HashSet<>();
            while (it.hasNext()){
                hs.add((Tuple) it.next());
            }
            assert hs.isEmpty();
        } finally {
            cleanUp();
        }
    }

    @Test
    public void checkRightOrder () throws DBAppException{
        try {
            DBApp dbApp = new DBApp();

            dbApp.init();

            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<>();

            htblColNameType.put("id", "java.lang.Integer");

            htblColNameType.put("name", "java.lang.String");


            dbApp.createTable(strTableName, "id", htblColNameType);

            for(int i = 0 ; i < 4 ; i ++){
                Hashtable<String , Object> ht = new Hashtable<>();
                ht.put("id" , 2*i);
                ht.put("name" , "ankdk"+i);
                dbApp.insertIntoTable(strTableName , ht);
            }

            Table table = Table.deserialize("tables/" + strTableName + "/" + strTableName
                    + ".class");

            Hashtable<String , Object> ht = new Hashtable<>();
            ht.put("id" , 2);
            dbApp.deleteFromTable(strTableName , ht);

            table = Table.deserialize("tables/" + strTableName + "/" + strTableName
                    + ".class");
            ht.put("id" , 4);
            dbApp.deleteFromTable(strTableName , ht);

            // check that two pages are half full
            table = Table.deserialize("tables/" + strTableName + "/" + strTableName
                    + ".class");
            for(int i = 0 ; i < table.getNumberOfPages() ; i ++){
                Page page = Page.deserialize("tables/" + strTableName + "/" +
                        table.getPages().get(i) + ".class");
                Vector<Tuple> vec = page.getTuples();
                assert vec.size() == 1;
            }

            // insert in the first page
            ht.put("id" , 8);
            ht.put("name" , "sasa");
            dbApp.insertIntoTable(strTableName , ht);

            // check sasa is in the second page
            table = Table.deserialize("tables/" + strTableName + "/" + strTableName
                    + ".class");
            Page page = Page.deserialize("tables/" + strTableName + "/" +
                    table.getPages().get(1) + ".class");
            Vector<Tuple> vec = page.getTuples();
            for(Tuple tup : vec){
                if((Integer) tup.getColumnValue("id") == 8
                        && ((String) tup.getColumnValue("name")).equals("sasa")){
                    return;
                }
            }
            assert false;
        } finally {
            cleanUp();
        }
    }

    @Test
    public void checkRightOrderAndShifting () throws DBAppException{
        try {
            DBApp dbApp = new DBApp();

            dbApp.init();

            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<>();

            htblColNameType.put("id", "java.lang.Integer");

            htblColNameType.put("name", "java.lang.String");


            dbApp.createTable(strTableName, "id", htblColNameType);

            for(int i = 0 ; i < 4 ; i ++){
                Hashtable<String , Object> ht = new Hashtable<>();
                ht.put("id" , 2*i);
                ht.put("name" , "ahmed"+i);
                dbApp.insertIntoTable(strTableName , ht);
            }

            Hashtable<String , Object> ht = new Hashtable<>();
            ht.put("id" , 1);
            ht.put("name" , "ali");
            dbApp.insertIntoTable(strTableName , ht);
            dbApp.createIndex(strTableName , "name" , "nameIndex");

            Table table = Table.deserialize("tables/" + strTableName + "/" + strTableName
                    + ".class");
            Page page = Page.deserialize("tables/" + strTableName + "/" +
                    table.getPages().get(2) + ".class");
            Vector<Tuple> vec = page.getTuples();
            assert vec.size() == 1;

            ht = new Hashtable<>();
            ht.put("name" , "ali");
            dbApp.updateTable(strTableName , "4" , ht);

            table = Table.deserialize("tables/" + strTableName + "/" + strTableName
                    + ".class");
            page = Page.deserialize("tables/" + strTableName + "/" +
                    table.getPages().get(0) + ".class");
            vec = page.getTuples();
            for(Tuple tup : vec){
                if((Integer) tup.getColumnValue("id") == 1
                        && ((String) tup.getColumnValue("name")).equals("ali")){
                    return;
                }
            }
            assert false;
        } finally {
            cleanUp();
        }
    }

    @Test
    public void checkProperShifting () throws DBAppException{
        try {
            DBApp dbApp = new DBApp();

            dbApp.init();

            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<>();

            htblColNameType.put("id", "java.lang.Integer");

            htblColNameType.put("name", "java.lang.String");


            dbApp.createTable(strTableName, "id", htblColNameType);

            for(int i = 0 ; i < 6 ; i ++){
                Hashtable<String , Object> ht = new Hashtable<>();
                ht.put("id" , 2*i);
                if(i >= 2 && i <= 3)
                    ht.put("name" , "ahmed");
                else
                    ht.put("name" , "ahmed"+i);
                dbApp.insertIntoTable(strTableName , ht);
            }

            dbApp.createIndex(strTableName , "name" , "nameIndex");

            Table table = Table.deserialize("tables/" + strTableName + "/" + strTableName
                    + ".class");

            int before = table.getNumberOfPages();

            Hashtable<String , Object> ht = new Hashtable<>();
            ht.put("name" , "ahmed");
            dbApp.deleteFromTable(strTableName , ht);

            table = Table.deserialize("tables/" + strTableName + "/" + strTableName
                    + ".class");

            assert table.getNumberOfPages() == 2;
        } finally {
            cleanUp();
        }
    }

    @Test
    public void deleteNonExistingTuple () throws DBAppException{
        try {
            DBApp dbApp = new DBApp();

            dbApp.init();

            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<>();

            htblColNameType.put("id", "java.lang.Integer");

            htblColNameType.put("name", "java.lang.String");


            dbApp.createTable(strTableName, "id", htblColNameType);

            for(int i = 0 ; i < 200 ; i ++){
                Hashtable<String , Object> ht = new Hashtable<>();
                ht.put("id" , 2*i);
                if(i >= 2 && i <= 3)
                    ht.put("name" , "ahmed");
                else
                    ht.put("name" , "ahmed"+i);
                dbApp.insertIntoTable(strTableName , ht);
            }

            dbApp.createIndex(strTableName , "name" , "nameIndex");

            Table table = Table.deserialize("tables/" + strTableName + "/" + strTableName
                    + ".class");

            int numOfTuplesBefore = 0;

            for(String strPageName : table.getPages()){
                Page page = Page.deserialize("tables/" + strTableName + "/" +
                        strPageName + ".class");
                numOfTuplesBefore += page.getTuples().size();
            }

            Hashtable<String , Object> ht = new Hashtable<>();
            ht.put("name" , "ali");
            dbApp.deleteFromTable(strTableName , ht);

            table = Table.deserialize("tables/" + strTableName + "/" + strTableName
                    + ".class");

            int numOfTuplesAfter = 0;

            for(String strPageName : table.getPages()){
                Page page = Page.deserialize("tables/" + strTableName + "/" +
                        strPageName + ".class");
                numOfTuplesAfter += page.getTuples().size();
            }

            assert numOfTuplesAfter == numOfTuplesBefore;
        } finally {
            cleanUp();
        }
    }

    @Before
    public void cleanUp() {
        try {
            // delete tables directory

            String tablesPath = "tables/";

            Path dir = Paths.get(tablesPath); // path to the directory
            Files
                    .walk(dir) // Traverse the file tree in depth-first order
                    .sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {

                            Files.delete(path); // delete each file or directory
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (Exception e) {

        }

        try {

            // delete Indicies directory

            String indiciesPath = "Indicies/";

            Path dir2 = Paths.get(indiciesPath); // path to the directory
            Files
                    .walk(dir2) // Traverse the file tree in depth-first order
                    .sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {

                            Files.delete(path); // delete each file or directory
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (Exception e) {

        }
        try {
            // delete metadata.csv

            File metadata = new File("metadata.csv");
            metadata.delete();
        } catch (Exception e) {

        }

    }
}

