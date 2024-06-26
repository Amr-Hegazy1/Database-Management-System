
// It looks like the code snippet you provided is not complete. It seems to be missing the actual code
// logic. The characters "i" and "
package InsertTests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

import com.bplustree.BPlusTree;
import com.db_engine.*;

public class InsertTest {

    @Test
    public void insertWrongTableName() throws DBAppException, IOException {
        try {
            DBApp dbApp = new DBApp();

            dbApp.init();

            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();

            htblColNameType.put("id", "java.lang.Integer");

            htblColNameType.put("name", "java.lang.String");

            htblColNameType.put("gpa", "java.lang.Double");

            dbApp.createTable(strTableName, "id", htblColNameType);

            assertThrows(DBAppException.class, () -> {
                dbApp.insertIntoTable("Student2", new Hashtable<String, Object>());
            });

        } finally {
            cleanUp();
        }
    }

    @Test
    public void insertExtraColumn() throws DBAppException, IOException {
        try {
            DBApp dbApp = new DBApp();

            dbApp.init();

            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();

            htblColNameType.put("id", "java.lang.Integer");

            htblColNameType.put("name", "java.lang.String");

            htblColNameType.put("gpa", "java.lang.Double");

            dbApp.createTable(strTableName, "id", htblColNameType);

            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
            htblColNameValue.put("id", 1);
            htblColNameValue.put("name", "Student");
            htblColNameValue.put("gpa", 3.0);
            htblColNameValue.put("extra", "extra");

            assertThrows(DBAppException.class, () -> {
                dbApp.insertIntoTable(strTableName, htblColNameValue);
            });

        } finally {
            cleanUp();
        }
    }

    @Test
    public void insertWrongDataType() throws DBAppException, IOException {
        try {
            DBApp dbApp = new DBApp();

            dbApp.init();

            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();

            htblColNameType.put("id", "java.lang.Integer");

            htblColNameType.put("name", "java.lang.String");

            htblColNameType.put("gpa", "java.lang.Double");

            dbApp.createTable(strTableName, "id", htblColNameType);

            // insert 20 rows

            // for(int i = 0; i < 20; i++){
            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
            htblColNameValue.put("id", 2 / 3 / 2020);
            htblColNameValue.put("name", "Student");
            htblColNameValue.put("gpa", 3.0);
            dbApp.insertIntoTable(strTableName, htblColNameValue);
            // }

            assertThrows(DBAppException.class, () -> {
                dbApp.insertIntoTable(strTableName, htblColNameValue);
            });

        } finally {
            cleanUp();
        }
    }

    @Test
    public void createIndexInserts() throws DBAppException, IOException {
        try {
            DBApp dbApp = new DBApp();

            dbApp.init();

            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();

            htblColNameType.put("id", "java.lang.Integer");

            htblColNameType.put("name", "java.lang.String");

            htblColNameType.put("gpa", "java.lang.Double");

            dbApp.createTable(strTableName, "id", htblColNameType);

            // insert 20 rows

            for (int i = 0; i < 20; i++) {
                Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
                htblColNameValue.put("id", i);
                htblColNameValue.put("name", "Student" + i);
                htblColNameValue.put("gpa", 3.0 + i);
                dbApp.insertIntoTable(strTableName, htblColNameValue);
            }

            dbApp.createIndex(strTableName, "id", "idIndex");

            // check that index contains correct values

            BPlusTree tree = BPlusTree.deserialize("tables/" + strTableName + "/" +
                    "idIndex.class");

            for (int i = 0; i < 20; i++) {
                assert tree.query(i) != null && tree.query(i).size() == 1
                        && ((Pair) tree.query(i).get(0)).getKey().equals(i);
            }

        } finally {
            cleanUp();
        }

    }

    @Test
    public void insertWithClusteringKeyIndex() throws DBAppException, IOException {
        try {
            DBApp dbApp = new DBApp();

            dbApp.init();

            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();

            htblColNameType.put("id", "java.lang.Integer");

            htblColNameType.put("name", "java.lang.String");

            htblColNameType.put("gpa", "java.lang.Double");

            dbApp.createTable(strTableName, "id", htblColNameType);

            // insert 20 rows

            for (int i = 0; i < 20; i++) {
                Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
                htblColNameValue.put("id", i);
                htblColNameValue.put("name", "Student" + i);
                htblColNameValue.put("gpa", 3.0 + i);
                dbApp.insertIntoTable(strTableName, htblColNameValue);
            }

            dbApp.createIndex(strTableName, "id", "idIndex");

            // check that index contains correct values

            BPlusTree tree = BPlusTree.deserialize("tables/" + strTableName + "/" +
                    "idIndex.class");

            for (int i = 0; i < 20; i++) {
                assert tree.query(i) != null && tree.query(i).size() == 1
                        && ((Pair) tree.query(i).get(0)).getKey().equals(i);
            }

            // insert a new row

            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
            htblColNameValue.put("id", 20);
            htblColNameValue.put("name", "Student" + 21);
            htblColNameValue.put("gpa", 3.0 + 21);
            dbApp.insertIntoTable(strTableName, htblColNameValue);

            // check that index contains correct values

            tree = BPlusTree.deserialize("tables/" + strTableName + "/" +
                    "idIndex.class");

            for (int i = 0; i < 21; i++) {

                assert tree.query(i) != null && tree.query(i).size() == 1
                        && ((Pair) tree.query(i).get(0)).getKey().equals(i);
            }

        } finally {
            cleanUp();
        }
    }

    @Test
    public void insertWithNonClusteringKeyIndex() throws DBAppException,
            IOException {
        try {
            DBApp dbApp = new DBApp();

            dbApp.init();

            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();

            htblColNameType.put("id", "java.lang.Integer");

            htblColNameType.put("name", "java.lang.String");

            htblColNameType.put("gpa", "java.lang.Double");

            dbApp.createTable(strTableName, "id", htblColNameType);

            // insert 20 rows

            for (int i = 0; i < 20; i++) {
                Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
                htblColNameValue.put("id", i);
                htblColNameValue.put("name", "Student" + i);
                htblColNameValue.put("gpa", 3.0 + i);
                dbApp.insertIntoTable(strTableName, htblColNameValue);
            }

            dbApp.createIndex(strTableName, "name", "nameIndex");

            // check that index contains correct values

            BPlusTree tree = BPlusTree.deserialize("tables/" + strTableName + "/" +
                    "nameIndex.class");

            for (int i = 0; i < 20; i++) {
                assert tree.query("Student" + i) != null && tree.query("Student" + i).size() == 1
                        && ((Pair) tree.query("Student" +
                                i).get(0)).getKey().equals(i);
            }

            // insert a new row

            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
            htblColNameValue.put("id", 20);
            htblColNameValue.put("name", "Student" + 20);
            htblColNameValue.put("gpa", 3.0 + 20);
            dbApp.insertIntoTable(strTableName, htblColNameValue);

            // check that index contains correct values

            tree = BPlusTree.deserialize("tables/" + strTableName + "/" +
                    "nameIndex.class");

            for (int i = 0; i < 21; i++) {

                assert tree.query("Student" + i) != null && tree.query("Student" + i).size() == 1
                        && ((Pair) tree.query("Student" +
                                i).get(0)).getKey().equals(i);
            }

        } finally {
            cleanUp();
        }
    }

    /**
     * The function `insertWithMultipleIndexes` tests inserting rows into a table
     * with multiple indexes
     * and verifies the correctness of the indexes.
     */
    @Test
    public void insertWithMultipleIndexes() throws DBAppException, IOException {
        try {
            DBApp dbApp = new DBApp();

            dbApp.init();

            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();

            htblColNameType.put("id", "java.lang.Integer");

            htblColNameType.put("name", "java.lang.String");

            htblColNameType.put("gpa", "java.lang.Double");

            dbApp.createTable(strTableName, "id", htblColNameType);

            // insert 20 rows

            for (int i = 0; i < 20; i++) {
                Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
                htblColNameValue.put("id", i);
                htblColNameValue.put("name", "Student" + i);
                htblColNameValue.put("gpa", 3.0 + i);
                dbApp.insertIntoTable(strTableName, htblColNameValue);
            }

            dbApp.createIndex(strTableName, "name", "nameIndex");

            dbApp.createIndex(strTableName, "gpa", "gpaIndex");

            // check that index contains correct values

            BPlusTree tree = BPlusTree.deserialize("tables/" + strTableName + "/" +
                    "nameIndex.class");

            for (int i = 0; i < 20; i++) {
                assert tree.query("Student" + i) != null && tree.query("Student" + i).size() == 1
                        && ((Pair) tree.query("Student" +
                                i).get(0)).getKey().equals(i);
            }

            tree = BPlusTree.deserialize("tables/" + strTableName + "/" +
                    "gpaIndex.class");

            for (int i = 0; i < 20; i++) {
                assert tree.query(3.0 + i) != null && tree.query(3.0 + i).size() == 1 &&
                        ((Pair) tree.query(3.0 + i).get(0)).getKey().equals(i);
            }

            // insert a new row

            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
            htblColNameValue.put("id", 20);
            htblColNameValue.put("name", "Student20");
            htblColNameValue.put("gpa", 3.0 + 20);
            dbApp.insertIntoTable(strTableName, htblColNameValue);

            // check that index contains correct values

            tree = BPlusTree.deserialize("tables/" + strTableName + "/" +
                    "nameIndex.class");

            for (int i = 0; i < 21; i++) {
                assert tree.query("Student" + i) != null && tree.query("Student" + i).size() == 1
                        && ((Pair) tree.query("Student" +
                                i).get(0)).getKey().equals(i);
            }

            tree = BPlusTree.deserialize("tables/" + strTableName + "/" +
                    "gpaIndex.class");

            for (int i = 0; i < 21; i++) {
                assert tree.query(3.0 + i) != null && tree.query(3.0 + i).size() == 1 &&
                        ((Pair) tree.query(3.0 + i).get(0)).getKey().equals(i);
            }

        } finally {

            cleanUp();
        }

    }

    /**
     * The function tests the efficiency of inserting records into a table in
     * logarithmic time
     * complexity.
     */
    // @Test
    // public void insertOccursInLogN() throws DBAppException, IOException {
    // try {
    // DBApp dbApp = new DBApp();

    // dbApp.init();

    // String strTableName = "Student";

    // Hashtable<String, String> htblColNameType = new Hashtable<String, String>();

    // htblColNameType.put("id", "java.lang.Integer");

    // htblColNameType.put("name", "java.lang.String");

    // htblColNameType.put("gpa", "java.lang.Double");

    // dbApp.createTable(strTableName, "id", htblColNameType);

    // long startTime = System.currentTimeMillis();
    // for (int i = 0; i < 100000; i++) {
    // Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
    // htblColNameValue.put("id", i);
    // htblColNameValue.put("name", "Student" + i);
    // htblColNameValue.put("gpa", 3.0 + i);
    // dbApp.insertIntoTable(strTableName, htblColNameValue);
    // }

    // long endTime = System.currentTimeMillis();

    // long duration = endTime - startTime;

    // System.out.println("Duration: " + duration);

    // assert duration < 1600;

    // } finally {
    // cleanUp();
    // }
    // }

    @Test
    public void insertNewPage() throws DBAppException, IOException {
        try {
            DBApp dbApp = new DBApp();

            dbApp.init();

            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();

            htblColNameType.put("id", "java.lang.Integer");

            htblColNameType.put("name", "java.lang.String");

            htblColNameType.put("gpa", "java.lang.Double");

            dbApp.createTable(strTableName, "id", htblColNameType);

            // insert 20 rows
            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();

            for (int i = 0; i < 21; i++) {
                htblColNameValue.put("id", i);
                htblColNameValue.put("name", "Student" + i);
                htblColNameValue.put("gpa", 3.0 + i);
                dbApp.insertIntoTable(strTableName, htblColNameValue);
            }

            Table tblTable = Table.deserialize("tables/" + strTableName + "/" + strTableName + ".class");
            String pgName = tblTable.getPageAtIndex(1);
            // Page pgLastPage = Page.deserialize("tables/" + strTableName + "/" + pgName +
            // ".class");
            assert pgName.equals("Student_1");
        } finally {
            cleanUp();
        }
    }

    @Test
    public void insertMultipleOverflow() throws DBAppException, IOException {
        try {
            DBApp dbApp = new DBApp();

            dbApp.init();

            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();

            htblColNameType.put("id", "java.lang.Integer");

            htblColNameType.put("name", "java.lang.String");

            htblColNameType.put("gpa", "java.lang.Double");

            dbApp.createTable(strTableName, "id", htblColNameType);

            // insert 20 rows
            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();

            for (int i = 0; i < 80; i++) {
                htblColNameValue.put("id", i + 1);
                htblColNameValue.put("name", "Student" + i);
                htblColNameValue.put("gpa", 3.0 + i);
                dbApp.insertIntoTable(strTableName, htblColNameValue);
            }

            htblColNameValue.put("id", 0);
            htblColNameValue.put("name", "Student" + 0);
            htblColNameValue.put("gpa", 2.0);
            dbApp.insertIntoTable(strTableName, htblColNameValue);

            Table tblTable = Table.deserialize("tables/" + strTableName + "/" + strTableName + ".class");
            String pgNameFirst = tblTable.getPageAtIndex(0);
            Page pgFirstPage = Page.deserialize("tables/" + strTableName + "/" + pgNameFirst + ".class");
            Tuple t = pgFirstPage.getFirstTuple();
            String pgNameLast = tblTable.getPageAtIndex(4);
            Page pgLastPage = Page.deserialize("tables/" + strTableName + "/" + pgNameLast + ".class");
            Tuple tFirsTuple = pgLastPage.getFirstTuple();
            Tuple tLastTuple = pgLastPage.getLastTuple();
            assert tFirsTuple.equals(tLastTuple) && tFirsTuple.getColumnValue("id").equals(80)
                    && t.getColumnValue("id").equals(0);
        } finally {
            cleanUp();
        }
    }

    @Test
    public void checkOverFlowMultipleIndices() throws DBAppException, IOException {
        try {
            DBApp dbApp = new DBApp();

            dbApp.init();

            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();

            htblColNameType.put("id", "java.lang.Integer");

            htblColNameType.put("name", "java.lang.String");

            htblColNameType.put("gpa", "java.lang.Double");

            dbApp.createTable(strTableName, "id", htblColNameType);

            // insert 40 rows

            for (int i = 0; i < 40; i++) {
                Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
                htblColNameValue.put("id", i + 1);
                htblColNameValue.put("name", "Student" + (i + 1));
                htblColNameValue.put("gpa", 3.0 + i + 1);
                dbApp.insertIntoTable(strTableName, htblColNameValue);
            }

            dbApp.createIndex(strTableName, "name", "nameIndex");

            dbApp.createIndex(strTableName, "gpa", "gpaIndex");

            // check that index contains correct values

            BPlusTree tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "nameIndex.class");
            Table tblTable = Table.deserialize("tables/" + strTableName + "/" + strTableName + ".class");

            for (int i = 0; i < 40; i++) {
                String strPageName = tblTable.getPageAtIndex(i / 20);
                assert tree.query("Student" + (i + 1)) != null && tree.query("Student" + (i + 1)).size() == 1
                        && ((Pair) tree.query("Student" + (i + 1)).get(0)).getKey().equals((i + 1))
                        && ((Pair) tree.query("Student" + (i + 1)).get(0)).getValue().equals(strPageName);
            }
            // insert a new row

            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
            htblColNameValue.put("id", 0);
            htblColNameValue.put("name", "Student0");
            htblColNameValue.put("gpa", 3.0);
            dbApp.insertIntoTable(strTableName, htblColNameValue);
            tblTable = Table.deserialize("tables/" + strTableName + "/" + strTableName + ".class");

            String newPage = strTableName + "_" + 0;

            Page pgPage = Page.deserialize("tables/" + strTableName + "/" + newPage + ".class");
            for (int i = 0; i < 41; i++) {
                if (i % 20 == 0 && i > 0) {
                    newPage = strTableName + "_" + (i / 20);
                    pgPage = Page.deserialize("tables/" + strTableName + "/" + newPage + ".class");
                }
                if (pgPage.getTupleWithIndex(i).getColumnValue("id").equals(0)) {
                    break;
                }
            }

            Pair pairIndexPair = new Pair(0, newPage);

            // tree.insert("Student0", pairIndexPair);
            tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "nameIndex.class");

            for (int i = 0; i < 41; i++) {
                String strPageName = tblTable.getPageAtIndex(i / 20);

                // assert tree.query("Student" + i).size() == 1;
                assert tree.query("Student" + i) != null
                        && ((Pair) tree.query("Student" + i).get(0)).getKey().equals(i)
                        && ((Pair) tree.query("Student" + i).get(0)).getValue().equals(strPageName);

            } // error was here
            tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "gpaIndex.class");

            for (int i = 0; i < 41; i++) {
                String strPageName = tblTable.getPageAtIndex(i / 20);
                assert tree.query(3.0 + i) != null
                        && ((Pair) tree.query(3.0 + i).get(0)).getKey().equals(i)
                        && ((Pair) tree.query(3.0 + i).get(0)).getValue().equals(strPageName);
            }
            String pgNameFirst = tblTable.getPageAtIndex(0);
            Page pgFirstPage = Page.deserialize("tables/" + strTableName + "/" + pgNameFirst + ".class");
            Tuple t = pgFirstPage.getFirstTuple();
            String pgNameLast = tblTable.getPageAtIndex(2);
            Page pgLastPage = Page.deserialize("tables/" + strTableName + "/" + pgNameLast + ".class");
            Tuple tFirsTuple = pgLastPage.getFirstTuple();
            Tuple tLastTuple = pgLastPage.getLastTuple();
            assert tFirsTuple.equals(tLastTuple) && tFirsTuple.getColumnValue("id").equals(40)
                    && t.getColumnValue("id").equals(0);

        } finally {

            cleanUp();
        }

    }

    @Test
    public void checkMinMaxUpdate() throws DBAppException, IOException {
        try {
            DBApp dbApp = new DBApp();

            dbApp.init();

            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();

            htblColNameType.put("id", "java.lang.Integer");

            htblColNameType.put("name", "java.lang.String");

            htblColNameType.put("gpa", "java.lang.Double");

            dbApp.createTable(strTableName, "id", htblColNameType);

            // insert 80 rows

            for (int i = 0; i < 80; i++) {
                Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
                htblColNameValue.put("id", i + 1);
                htblColNameValue.put("name", "Student" + (i + 1));
                htblColNameValue.put("gpa", 3.0 + i + 1);
                dbApp.insertIntoTable(strTableName, htblColNameValue);
            }
            Table tblTable = Table.deserialize("tables/" + strTableName + "/" + strTableName + ".class");

            String newPage = "";
            Page pgPage = null;
            int max;
            int min;
            for (int i = 0; i < 80; i++) {

                if (i % 20 == 0) {
                    newPage = strTableName + "_" + (i / 20);
                    pgPage = Page.deserialize("tables/" + strTableName + "/" + newPage + ".class");
                    min = (int) pgPage.getFirstTuple().getColumnValue("id");
                    max = (int) pgPage.getLastTuple().getColumnValue("id");

                    assert min == (int) tblTable.getMin(newPage) && max == (int) tblTable.getMax(newPage);
                }
                // if (pgPage.getTupleWithIndex(i).getColumnValue("id").equals(0)) {
                // assert false;
                // }
            }

            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
            htblColNameValue.put("id", 0);
            htblColNameValue.put("name", "Student0");
            htblColNameValue.put("gpa", 3.0);
            dbApp.insertIntoTable(strTableName, htblColNameValue);
            tblTable = Table.deserialize("tables/" + strTableName + "/" + strTableName + ".class");

            for (int i = 0; i < 81; i++) {

                if (i % 20 == 0) {
                    newPage = strTableName + "_" + (i / 20);
                    pgPage = Page.deserialize("tables/" + strTableName + "/" + newPage + ".class");
                    min = (int) pgPage.getFirstTuple().getColumnValue("id");
                    max = (int) pgPage.getLastTuple().getColumnValue("id");

                    assert min == (int) tblTable.getMin(newPage) && max == (int) tblTable.getMax(newPage);
                }

            }
        } finally {

            cleanUp();
        }
    }

    public Vector<Tuple> getAllTuples (Table table) throws DBAppException {
        Vector<Tuple> vec = new Vector<>();
        for(String strPageName : table.getPages()){
            Page page = Page.deserialize("tables/" + table.getTableName() + "/" +
                    strPageName + ".class");
            vec.addAll(page.getTuples());
        }
        return vec;
    }

    @Test
    public void test_insert_edgeCase() throws DBAppException , IOException {
        try {
            cleanUp();
            DBApp dbApp = new DBApp();

            dbApp.init();

            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();

            htblColNameType.put("id", "java.lang.Integer");

            htblColNameType.put("name", "java.lang.String");

            htblColNameType.put("gpa", "java.lang.Double");

            dbApp.createTable(strTableName, "id", htblColNameType);

            // insert 100 rows
            //page1:1-20 ; page2:41-60 ; page3:81-100 .....

            for (int i = 0; i < 200; i++) {
                if(i == 180) break;
                if(i%20==0 && i!=0){
                    i+=20;
                }
                Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
                htblColNameValue.put("id", (i + 1));
                htblColNameValue.put("name", "Student" + (i + 1));
                htblColNameValue.put("gpa", 3.0 + (i + 1));
                dbApp.insertIntoTable(strTableName, htblColNameValue);
            }

            Table table = Table.deserialize("tables/" + strTableName + "/" +
                    strTableName + ".class");

            Vector<Tuple> allTuples = getAllTuples(table);

            //insert 2 intermediate rows in each page except the 1st
            //25,26,66,67,107,108,148,149,189,190
            //page1:1-20 ; page2:25,26--58 ; page3:59,60-- ; page4:
            int count =0;
            for (int i = 25; i < 200; i++) {

                Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
                htblColNameValue.put("id", i);
                htblColNameValue.put("name", "Student" + i);
                htblColNameValue.put("gpa", 3.0 + i);
                dbApp.insertIntoTable(strTableName, htblColNameValue);

                // i+=10;
                count++;
                if(count==2){
                    i+=39;
                    count=0;
                }


            }

            table = Table.deserialize("tables/" + strTableName + "/" +
                    strTableName + ".class");

            allTuples = getAllTuples(table);

            //

            String newPage = "";
            Page pgPage = null;
            int max;
            int min;

            Table tblTable = Table.deserialize("tables/" + strTableName + "/" + strTableName + ".class");
            for (int i = 0; i < tblTable.getNumberOfPages() ; i++) {

                newPage = tblTable.getPageAtIndex(i);
                pgPage = Page.deserialize("tables/" + strTableName + "/" + newPage + ".class");
                min = (int) pgPage.getFirstTuple().getColumnValue("id");
                max = (int) pgPage.getLastTuple().getColumnValue("id");

                System.out.println("min=" + min + "   " + "max=" + max);

                assert min == (int) tblTable.getMin(newPage) && max == (int) tblTable.getMax(newPage);
            }
        }
        finally{
            cleanUp();
        }
    }

    @Before
    public void cleanUp() throws IOException {
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
