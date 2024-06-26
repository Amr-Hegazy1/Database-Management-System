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

public class TestCases {

    /**
     * The `createTable` function in Java tests the creation of a table in a
     * database, including
     * checking metadata updates and column properties.
     */
    @Test
    public void createTable() throws DBAppException, IOException {

        try {
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

        } finally {
            cleanUp();
        }

    }
    

    @Test
    public void createTableInvalidPrimaryKey() throws DBAppException, IOException {
        try {
            DBApp dbApp = new DBApp();
            dbApp.init();

            String strTableName = "Student";
            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();
            htblColNameType.put("id", "java.lang.Integer");
            htblColNameType.put("name", "java.lang.String");
            htblColNameType.put("gpa", "java.lang.Double");

            assertThrows(DBAppException.class, () -> {
                dbApp.createTable(strTableName, "amr", htblColNameType);
            });

        } finally {
            cleanUp();
        }
    }

    /**
     * The function tests creating a table with invalid data types and expects a
     * DBAppException to be
     * thrown.
     */
    @Test
    public void createTableWithInvalidTypes() throws DBAppException, IOException {

        try {

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

        } finally {
            cleanUp();
        }

    }

    /**
     * The function tests creating a duplicate table in a database application and
     * expects it to throw
     * a DBAppException.
     */
    @Test
    public void createDuplicateTable() throws DBAppException, IOException {

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
                dbApp.createTable(strTableName, "id", htblColNameType);
            });

        } finally {
            cleanUp();
        }

    }

    /**
     * The `createIndexInserts` function in Java creates a table, inserts 20 rows of
     * data, creates an
     * index on the "id" column, and then checks the correctness of the index
     * values.
     */
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

            BPlusTree tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "idIndex.class");

            for (int i = 0; i < 20; i++) {
                assert tree.query(i) != null && tree.query(i).size() == 1;

                Pair pair = (Pair) tree.query(i).get(0);

                Integer clusteringKey = (Integer) pair.getKey();

                String pageName = (String) pair.getValue();

                Page page = Page.deserialize("tables/" + strTableName + "/" + pageName + ".class");

                int tupleIndex = TestCases.searchTuplesByClusteringKey("id", clusteringKey, page);

                assert tupleIndex != -1;

                Tuple tuple = page.getTupleWithIndex(tupleIndex);

                assert tuple.getColumnValue("id").equals(i);

            }

        } finally {
            cleanUp();
        }

    }

    @Test
    public void createIndexOnEmptyTable() throws DBAppException, IOException {
        try {
            DBApp dbApp = new DBApp();

            dbApp.init();

            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();

            htblColNameType.put("id", "java.lang.Integer");

            htblColNameType.put("name", "java.lang.String");

            htblColNameType.put("gpa", "java.lang.Double");

            dbApp.createTable(strTableName, "id", htblColNameType);

            dbApp.createIndex(strTableName, "id", "idIndex");

            // check that index contains correct values

            BPlusTree tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "idIndex.class");

            assert tree.getRoot() == null;

        } finally {
            cleanUp();
        }
    }

    @Test
    public void createIndexOnMultiplePages() throws DBAppException, IOException {
        try {
            DBApp dbApp = new DBApp();

            dbApp.init();

            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();
            htblColNameType.put("id", "java.lang.Integer");
            htblColNameType.put("name", "java.lang.String");
            htblColNameType.put("gpa", "java.lang.Double");

            dbApp.createTable(strTableName, "id", htblColNameType);

            for (int i = 0; i < 1000; i++) {
                Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
                htblColNameValue.put("id", i);
                htblColNameValue.put("name", "Student" + i);
                htblColNameValue.put("gpa", 3.0 + i);
                dbApp.insertIntoTable(strTableName, htblColNameValue);
            }

            dbApp.createIndex(strTableName, "id", "idIndex");

            // check that index contains correct values

            BPlusTree tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "idIndex.class");

            for (int i = 0; i < 1000; i++) {
                assert tree.query(i) != null && tree.query(i).size() == 1;

                Pair pair = (Pair) tree.query(i).get(0);

                Comparable clusteringKey = (Comparable) pair.getKey();

                String pageName = (String) pair.getValue();

                Page page = Page.deserialize("tables/" + strTableName + "/" + pageName + ".class");

                int tupleIndex = TestCases.searchTuplesByClusteringKey("id", clusteringKey, page);

                assert tupleIndex != -1;

                Tuple tuple = page.getTupleWithIndex(tupleIndex);

                assert tuple.getColumnValue("id").equals(i);

            }

        } finally {
            cleanUp();
        }
    }

    @Test
    public void createIndexWithDuplicates() throws DBAppException, IOException {
        try {
            DBApp dbApp = new DBApp();

            dbApp.init();

            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();
            htblColNameType.put("id", "java.lang.Integer");
            htblColNameType.put("name", "java.lang.String");
            htblColNameType.put("gpa", "java.lang.Double");

            dbApp.createTable(strTableName, "id", htblColNameType);

            for (int i = 0; i < 1000; i++) {
                Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
                htblColNameValue.put("id", i);
                htblColNameValue.put("name", "Student");
                htblColNameValue.put("gpa", 3.0 + i);
                dbApp.insertIntoTable(strTableName, htblColNameValue);
            }

            dbApp.createIndex(strTableName, "name", "nameIndex");

            // check that index contains correct values

            BPlusTree tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "nameIndex.class");

            assert tree.query("Student") != null && tree.query("Student").size() == 1000;

        } finally {
            cleanUp();
        }
    }

    /**
     * The `insertWithClusteringKeyIndex` function tests inserting records into a
     * table with a
     * clustering key index in a database application.
     */
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

            BPlusTree tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "idIndex.class");

            for (int i = 0; i < 20; i++) {

                assert tree.query(i) != null && tree.query(i).size() == 1;

                assert ((Pair) tree.query(i).get(0)).getKey().equals(i);
            }

            // insert a new row

            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
            htblColNameValue.put("id", 20);
            htblColNameValue.put("name", "Student" + 21);
            htblColNameValue.put("gpa", 3.0 + 21);
            dbApp.insertIntoTable(strTableName, htblColNameValue);

            // check that index contains correct values

            tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "idIndex.class");

            for (int i = 0; i < 21; i++) {
                assert tree.query(i).size() == 1;
                assert tree.query(i) != null && tree.query(i).size() == 1
                        && ((Pair) tree.query(i).get(0)).getKey().equals(i);
            }

        } finally {
            cleanUp();
        }
    }

    /**
     * The function tests inserting data into a table with a non-clustering key
     * index in a database
     * application.
     */
    @Test
    public void insertWithNonClusteringKeyIndex() throws DBAppException, IOException {
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

            BPlusTree tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "nameIndex.class");

            for (int i = 0; i < 20; i++) {
                assert tree.query("Student" + i) != null && tree.query("Student" + i).size() == 1;

                Pair pair = (Pair) tree.query("Student" + i).get(0);

                Comparable clusteringKey = (Comparable) pair.getKey();
                String pageName = (String) pair.getValue();

                Page page = Page.deserialize("tables/" + strTableName + "/" + pageName + ".class");

                int tupleIndex = TestCases.searchTuplesByClusteringKey("id", clusteringKey, page);

                assert tupleIndex != -1;

                Tuple tuple = page.getTupleWithIndex(tupleIndex);

                assert tuple.getColumnValue("name").equals("Student" + i);
            }

            // insert a new row

            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
            htblColNameValue.put("id", 20);
            htblColNameValue.put("name", "Student" + 20);
            htblColNameValue.put("gpa", 3.0 + 20);
            dbApp.insertIntoTable(strTableName, htblColNameValue);

            // check that index contains correct values

            tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "nameIndex.class");

            for (int i = 0; i < 21; i++) {

                assert tree.query("Student" + i) != null && tree.query("Student" + i).size() == 1;

                Pair pair = (Pair) tree.query("Student" + i).get(0);

                Comparable clusteringKey = (Comparable) pair.getKey();
                String pageName = (String) pair.getValue();

                Page page = Page.deserialize("tables/" + strTableName + "/" + pageName + ".class");

                int tupleIndex = TestCases.searchTuplesByClusteringKey("id", clusteringKey, page);

                assert tupleIndex != -1;

                Tuple tuple = page.getTupleWithIndex(tupleIndex);

                assert tuple.getColumnValue("name").equals("Student" + i);

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

            BPlusTree tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "nameIndex.class");

            for (int i = 0; i < 20; i++) {
                assert tree.query("Student" + i) != null && tree.query("Student" + i).size() == 1;

                Pair pair = (Pair) tree.query("Student" + i).get(0);

                Comparable clusteringKey = (Comparable) pair.getKey();

                String pageName = (String) pair.getValue();

                Page page = Page.deserialize("tables/" + strTableName + "/" + pageName + ".class");

                int tupleIndex = TestCases.searchTuplesByClusteringKey("id", clusteringKey, page);

                assert tupleIndex != -1;

                Tuple tuple = page.getTupleWithIndex(tupleIndex);

                assert tuple.getColumnValue("name").equals("Student" + i);

            }

            tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "gpaIndex.class");

            for (int i = 0; i < 20; i++) {
                assert tree.query(3.0 + i) != null && tree.query(3.0 + i).size() == 1;

                Pair pair = (Pair) tree.query(3.0 + i).get(0);

                Comparable clusteringKey = (Comparable) pair.getKey();

                String pageName = (String) pair.getValue();

                Page page = Page.deserialize("tables/" + strTableName + "/" + pageName + ".class");

                int tupleIndex = TestCases.searchTuplesByClusteringKey("id", clusteringKey, page);

                assert tupleIndex != -1;

                Tuple tuple = page.getTupleWithIndex(tupleIndex);

                assert tuple.getColumnValue("gpa").equals(3.0 + i);

            }

            // insert a new row

            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
            htblColNameValue.put("id", 20);
            htblColNameValue.put("name", "Student20");
            htblColNameValue.put("gpa", 3.0 + 20);
            dbApp.insertIntoTable(strTableName, htblColNameValue);

            // check that index contains correct values

            tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "nameIndex.class");

            for (int i = 0; i < 21; i++) {
                assert tree.query("Student" + i) != null && tree.query("Student" + i).size() == 1;

                Pair pair = (Pair) tree.query("Student" + i).get(0);

                Comparable clusteringKey = (Comparable) pair.getKey();

                String pageName = (String) pair.getValue();

                Page page = Page.deserialize("tables/" + strTableName + "/" + pageName + ".class");

                int tupleIndex = TestCases.searchTuplesByClusteringKey("id", clusteringKey, page);

                assert tupleIndex != -1;

                Tuple tuple = page.getTupleWithIndex(tupleIndex);

                assert tuple.getColumnValue("name").equals("Student" + i);

            }

            tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "gpaIndex.class");

            for (int i = 0; i < 21; i++) {
                assert tree.query(3.0 + i) != null && tree.query(3.0 + i).size() == 1;

                Pair pair = (Pair) tree.query(3.0 + i).get(0);

                Comparable clusteringKey = (Comparable) pair.getKey();

                String pageName = (String) pair.getValue();

                Page page = Page.deserialize("tables/" + strTableName + "/" + pageName + ".class");

                int tupleIndex = TestCases.searchTuplesByClusteringKey("id", clusteringKey, page);

                assert tupleIndex != -1;

                Tuple tuple = page.getTupleWithIndex(tupleIndex);

                assert tuple.getColumnValue("gpa").equals(3.0 + i);

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
    @Test(timeout = 2000)
    public void insertOccursInLogN() throws DBAppException, IOException {
        try {
            DBApp dbApp = new DBApp();

            dbApp.init();

            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();

            htblColNameType.put("id", "java.lang.Integer");

            htblColNameType.put("name", "java.lang.String");

            htblColNameType.put("gpa", "java.lang.Double");

            dbApp.createTable(strTableName, "id", htblColNameType);

            for (int i = 0; i < 1e3; i++) {
                System.out.println(i);
                Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
                htblColNameValue.put("id", i);
                htblColNameValue.put("name", "Student" + i);
                htblColNameValue.put("gpa", 3.0 + i);
                dbApp.insertIntoTable(strTableName, htblColNameValue);
            }

        } finally {
            cleanUp();
        }
    }

    /**
     * The function `updateWithoutIndex` in Java updates a row in a table and
     * verifies that the updated
     * row is correct while other rows remain unchanged.
     */
    @Test
    public void updateWithoutIndex() throws DBAppException, IOException {
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

            // update a row

            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();

            htblColNameValue.put("name", "Student" + 21);

            dbApp.updateTable(strTableName, "0", htblColNameValue);

            // check that the row is updated and the other rows are not

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
                    if (tuple.getColumnValue("id").equals(0)) {
                        assert tuple.getColumnValue("name").equals("Student" + 21);
                    } else {
                        assert tuple.getColumnValue("name").equals("Student" + tuple.getColumnValue("id"));
                    }
                }
            }
        } finally {
            cleanUp();
        }

    }

    /**
     * The function `updateWithoutIndexMultiplePages` in Java updates a row in a
     * table and checks that
     * the updated row is correct while ensuring other rows remain unchanged.
     */
    @Test
    public void updateWithoutIndexMultiplePages() throws DBAppException, IOException {
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

            for (int i = 0; i < 2000; i++) {
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

            for (File page : pages) {
                // check if the file name is in the format page_i.class
                if (!page.getName().matches("Student_\\d+\\.class")) {
                    continue;
                }

                Page p = Page.deserialize(page.getPath());
                for (Tuple tuple : p.getTuples()) {
                    if (tuple.getColumnValue("id").equals(0)) {
                        assert tuple.getColumnValue("name").equals("Student" + 21);
                    } else {
                        assert tuple.getColumnValue("name").equals("Student" + tuple.getColumnValue("id"));
                    }
                }
            }
        } finally {
            cleanUp();
        }

    }

    /**
     * The `updateWithIndex` function in Java tests updating a row in a table and
     * checking the
     * corresponding index update.
     */
    @Test
    public void updateWithIndex() throws DBAppException, IOException {
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

            // update a row

            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();

            htblColNameValue.put("name", "Student20");

            dbApp.updateTable(strTableName, "0", htblColNameValue);

            // check that the index is updated

            BPlusTree tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "nameIndex.class");

            for (int i = 0; i < 21; i++) {
                if (i == 0) {
                    assert tree.query("Student" + i).size() == 0;
                } else {
                    assert tree.query("Student" + i) != null && tree.query("Student" + i).size() == 1;

                    Pair pair = (Pair) tree.query("Student" + i).get(0);

                    Comparable clusteringKey = (Comparable) pair.getKey();

                    String pageName = (String) pair.getValue();

                    Page page = Page.deserialize("tables/" + strTableName + "/" + pageName + ".class");

                    int tupleIndex = TestCases.searchTuplesByClusteringKey("id", clusteringKey, page);

                    assert tupleIndex != -1;

                    Tuple tuple = page.getTupleWithIndex(tupleIndex);

                    assert tuple.getColumnValue("name").equals("Student" + i);

                }

            }

        } finally {
            cleanUp();
        }

    }

    /**
     * The function `updateWithMultipleIndexes` tests updating a row in a table with
     * multiple indexes
     * in a Java application.
     */
    @Test
    public void updateWithMultipleIndexes() throws DBAppException, IOException {
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

            // update a row

            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();

            htblColNameValue.put("name", "Student20");

            dbApp.updateTable(strTableName, "0", htblColNameValue);

            // check that the indexes are updated

            BPlusTree tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "nameIndex.class");

            for (int i = 0; i < 21; i++) {
                if (i == 0) {
                    assert tree.query("Student" + i).size() == 0;
                } else {
                    assert tree.query("Student" + i) != null && tree.query("Student" + i).size() == 1;

                    Pair pair = (Pair) tree.query("Student" + i).get(0);

                    Comparable clusteringKey = (Comparable) pair.getKey();

                    String pageName = (String) pair.getValue();

                    Page page = Page.deserialize("tables/" + strTableName + "/" + pageName + ".class");

                    int tupleIndex = TestCases.searchTuplesByClusteringKey("id", clusteringKey, page);

                    assert tupleIndex != -1;

                    Tuple tuple = page.getTupleWithIndex(tupleIndex);

                    assert tuple.getColumnValue("name").equals("Student" + i);

                }
            }

            tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "gpaIndex.class");

            for (int i = 0; i < 20; i++) {
                if (i == 0) {
                    System.out.println(tree.query(3.0 + i));
                    assert tree.query(3.0 + i).size() == 1;
                } else {
                    System.out.println(tree.query(3.0 + i));
                    assert tree.query(3.0 + i) != null && tree.query(3.0 + i).size() == 1;

                    Pair pair = (Pair) tree.query(3.0 + i).get(0);

                    Comparable clusteringKey = (Comparable) pair.getKey();

                    String pageName = (String) pair.getValue();

                    Page page = Page.deserialize("tables/" + strTableName + "/" + pageName + ".class");

                    int tupleIndex = TestCases.searchTuplesByClusteringKey("id", clusteringKey, page);

                    assert tupleIndex != -1;

                    Tuple tuple = page.getTupleWithIndex(tupleIndex);

                    assert tuple.getColumnValue("gpa").equals(3.0 + i);

                }
                System.out.println(tree.query(3.0 + i));
            }

        } finally {
            cleanUp();
        }
    }

    @Test
    public void updateClusteringKey() throws DBAppException, IOException {
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

            // update a row

            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();

            htblColNameValue.put("id", 21);

            assertThrows(DBAppException.class, () -> dbApp.updateTable(strTableName, "0", htblColNameValue));

        } finally {
            cleanUp();
        }

    }

    @Test
    public void updateWithExtraColumn() throws DBAppException, IOException {
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

            // update a row

            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();

            htblColNameValue.put("name", "Student20");
            htblColNameValue.put("extra", "extra");

            assertThrows(DBAppException.class, () -> dbApp.updateTable(strTableName, "0", htblColNameValue));

        } finally {
            cleanUp();
        }

    }

    @Test
    public void updateInvalidTableName() throws DBAppException, IOException {
        try {
            DBApp dbApp = new DBApp();

            dbApp.init();

            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();

            htblColNameValue.put("name", "Student20");

            assertThrows(DBAppException.class, () -> dbApp.updateTable("Student", "0", htblColNameValue));

        } finally {
            cleanUp();
        }

    }

    /**
     * The `deleteWithoutIndex` function in Java tests deleting a specific row from
     * a table and
     * verifies that the row is deleted while other rows remain intact.
     */
    @Test
    public void deleteWithoutIndex() throws DBAppException, IOException {
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

            // delete a row

            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();

            htblColNameValue.put("id", 0);

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
                    if (tuple.getColumnValue("id").equals(0)) {
                        assert false;
                    }
                }
            }
        } finally {
            cleanUp();
        }

    }

    @Test
    public void deleteWithoutIndexMultiplePages() throws DBAppException, IOException {
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

            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();

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
                    if (tuple.getColumnValue("id").equals(0)) {
                        assert false;
                    }
                }
            }
        } finally {
            cleanUp();
        }

    }

    @Test
    public void deleteWithClusteringKeyIndex() throws DBAppException, IOException {
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

            // delete a row

            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();

            htblColNameValue.put("id", 0);

            dbApp.deleteFromTable(strTableName, htblColNameValue);

            // check that the row is deleted and the other rows are not

            String pagesPath = "tables/" + strTableName;

            File pagesDir = new File(pagesPath);

            File[] pages = pagesDir.listFiles();

            for (File page : pages) {
                // check if the file name is in the format Student_i.class
                if (!page.getName().matches("Student_\\d+\\.class")) {
                    continue;
                }

                Page p = Page.deserialize(page.getPath());
                for (Tuple tuple : p.getTuples()) {
                    if (tuple.getColumnValue("id").equals(0)) {
                        assert false;
                    }
                }
            }

            // check that the index is updated

            BPlusTree tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "idIndex.class");

            for (int i = 0; i < 20; i++) {
                if (i == 0) {

                    assert tree.query(i).size() == 0;
                } else {
                    assert tree.query(i) != null && tree.query(i).size() == 1;

                    Pair pair = (Pair) tree.query(i).get(0);

                    Comparable clusteringKey = (Comparable) pair.getKey();

                    String pageName = (String) pair.getValue();

                    Page page = Page.deserialize("tables/" + strTableName + "/" + pageName + ".class");

                    int tupleIndex = TestCases.searchTuplesByClusteringKey("id", clusteringKey, page);

                    assert tupleIndex != -1;

                    Tuple tuple = page.getTupleWithIndex(tupleIndex);

                    assert tuple.getColumnValue("id").equals(i);

                }
            }
        } finally {
            cleanUp();
        }
    }

    @Test
    public void deleteWithNonClusteringKeyIndex() throws DBAppException, IOException {
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

            // delete a row

            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();

            htblColNameValue.put("name", "Student0");

            dbApp.deleteFromTable(strTableName, htblColNameValue);

            // check that the row is deleted and the other rows are not

            String pagesPath = "tables/" + strTableName;

            File pagesDir = new File(pagesPath);

            File[] pages = pagesDir.listFiles();

            for (File page : pages) {
                // check if the file name is in the format Student_i.class
                if (!page.getName().matches("Student_\\d+\\.class")) {
                    continue;
                }

                Page p = Page.deserialize(page.getPath());
                for (Tuple tuple : p.getTuples()) {
                    if (tuple.getColumnValue("name").equals("Student0")) {
                        assert false;
                    }
                }
            }

            // check that the index is updated

            BPlusTree tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "nameIndex.class");

            for (int i = 0; i < 20; i++) {
                if (i == 0) {
                    assert tree.query("Student0").size() == 0;
                } else {
                    assert tree.query("Student" + i) != null && tree.query("Student" + i).size() == 1;

                    Pair pair = (Pair) tree.query("Student" + i).get(0);

                    Comparable clusteringKey = (Comparable) pair.getKey();

                    String pageName = (String) pair.getValue();

                    Page page = Page.deserialize("tables/" + strTableName + "/" + pageName + ".class");

                    int tupleIndex = TestCases.searchTuplesByClusteringKey("id", clusteringKey, page);

                    assert tupleIndex != -1;

                    Tuple tuple = page.getTupleWithIndex(tupleIndex);

                    assert tuple.getColumnValue("name").equals("Student" + i);

                }
            }
        } finally {
            cleanUp();
        }
    }

    @Test
    public void deleteWithMultipleIndexes() throws DBAppException, IOException {
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

            // delete a row

            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();

            htblColNameValue.put("name", "Student0");

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
                    if (tuple.getColumnValue("name").equals("Student0")) {
                        assert false;
                    }
                }
            }

            // check that the indexes are updated

            BPlusTree tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "nameIndex.class");

            for (int i = 0; i < 20; i++) {
                if (i == 0) {
                    assert tree.query("Student0").size() == 0;
                } else {
                    assert tree.query("Student" + i) != null && tree.query("Student" + i).size() == 1;

                    Pair pair = (Pair) tree.query("Student" + i).get(0);

                    Comparable clusteringKey = (Comparable) pair.getKey();

                    String pageName = (String) pair.getValue();

                    Page page = Page.deserialize("tables/" + strTableName + "/" + pageName + ".class");

                    int tupleIndex = TestCases.searchTuplesByClusteringKey("id", clusteringKey, page);

                    assert tupleIndex != -1;

                    Tuple tuple = page.getTupleWithIndex(tupleIndex);

                    assert tuple.getColumnValue("name").equals("Student" + i);

                }
            }

            tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "gpaIndex.class");

            for (int i = 0; i < 20; i++) {
                if (i == 0) {
                    assert tree.query(3.0 + i).size() == 0;
                } else {
                    assert tree.query(3.0 + i) != null && tree.query(3.0 + i).size() == 1;

                    Pair pair = (Pair) tree.query(3.0 + i).get(0);

                    Comparable clusteringKey = (Comparable) pair.getKey();

                    String pageName = (String) pair.getValue();

                    Page page = Page.deserialize("tables/" + strTableName + "/" + pageName + ".class");

                    int tupleIndex = TestCases.searchTuplesByClusteringKey("id", clusteringKey, page);

                    assert tupleIndex != -1;

                    Tuple tuple = page.getTupleWithIndex(tupleIndex);

                    assert tuple.getColumnValue("gpa").equals(3.0 + i);

                }

            }
        } finally {
            cleanUp();
        }
    }

    @Test
    public void deleteWithIndexedAndNonIndexedColumns() throws DBAppException, IOException {
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
                htblColNameValue.put("name", "Student0");
                htblColNameValue.put("gpa", 3.0 + i);
                dbApp.insertIntoTable(strTableName, htblColNameValue);
            }

            dbApp.createIndex(strTableName, "name", "nameIndex");

            // delete a row
            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
            htblColNameValue.put("name", "Student0");
            htblColNameValue.put("gpa", 3.0);

            dbApp.deleteFromTable(strTableName, htblColNameValue);

            // check that the row is deleted and the other rows are not
            String pagesPath = "tables/" + strTableName;
            File pagesDir = new File(pagesPath);
            File[] pages = pagesDir.listFiles();

            for (File page : pages) {
                // check if the file name is in the format Student_i.class
                if (!page.getName().matches("Student_\\d+\\.class")) {
                    continue;
                }

                Page p = Page.deserialize(page.getPath());
                for (Tuple tuple : p.getTuples()) {
                    if (tuple.getColumnValue("name").equals("Student0") && tuple.getColumnValue("gpa").equals(3.0)) {
                        System.out.println(tuple);
                        assert false;
                    }
                }
            }

            // check that the indexes are updated
            BPlusTree tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "nameIndex.class");
            System.out.println(tree.query("Student0").size());
            assert tree.query("Student0").size() == 19;

        } finally {
            cleanUp();
        }
    }

    @Test
    public void deleteWrongDataTypes() throws DBAppException, IOException {
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
                htblColNameValue.put("name", "Student0");
                htblColNameValue.put("gpa", 3.0 + i);
                dbApp.insertIntoTable(strTableName, htblColNameValue);
            }

            // delete a row
            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
            htblColNameValue.put("name", 0);

            assertThrows(DBAppException.class, () -> {
                dbApp.deleteFromTable(strTableName, htblColNameValue);
            });

        } finally {
            cleanUp();
        }
    }

    @Test
    public void deleteAllRows() throws DBAppException, IOException {
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
                htblColNameValue.put("name", "Student0");
                htblColNameValue.put("gpa", 3.0 + i);
                dbApp.insertIntoTable(strTableName, htblColNameValue);
            }

            // delete all rows
            dbApp.deleteFromTable(strTableName, new Hashtable<String, Object>());

            // check that all rows are deleted
            String pagesPath = "tables/" + strTableName;
            File pagesDir = new File(pagesPath);
            File[] pages = pagesDir.listFiles();

            for (File page : pages) {
                // check if the file name is in the format Student_i.class
                if (!page.getName().matches("Student_\\d+\\.class")) {
                    continue;
                }

                Page p = Page.deserialize(page.getPath());
                assert p.getTuples().size() == 0;
            }

        } finally {
            cleanUp();
        }
    }

    @Test
    public void deleteWithExtraColumns() throws DBAppException, IOException {
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
                htblColNameValue.put("name", "Student0");
                htblColNameValue.put("gpa", 3.0 + i);
                dbApp.insertIntoTable(strTableName, htblColNameValue);
            }

            // delete a row
            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
            htblColNameValue.put("name", "Student0");
            htblColNameValue.put("extra", "extra");

            assertThrows(DBAppException.class, () -> {
                dbApp.deleteFromTable(strTableName, htblColNameValue);
            });

        } finally {
            cleanUp();
        }
    }

    @Test
    public void deleteWithWrongTableName() throws DBAppException, IOException {
        try {
            DBApp dbApp = new DBApp();

            dbApp.init();

            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
            htblColNameValue.put("name", "Student0");

            assertThrows(DBAppException.class, () -> {
                dbApp.deleteFromTable("Student", htblColNameValue);
            });

        } finally {
            cleanUp();
        }
    }

    @Test
    public void selectWithoutIndexWithId() throws DBAppException, IOException, ClassNotFoundException {
        try {
            DBApp dbApp = new DBApp();

            dbApp.init();

            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();

            htblColNameType.put("id", "java.lang.Integer");

            htblColNameType.put("name", "java.lang.String");

            htblColNameType.put("gpa", "java.lang.Double");

            dbApp.createTable(strTableName, "id", htblColNameType);

            dbApp.createIndex(strTableName, "name", "nameIndex");

            dbApp.createIndex(strTableName, "gpa", "gpaIndex");

            // insert 20 rows

            for (int i = 0; i < 20; i++) {
                Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
                htblColNameValue.put("id", i);
                htblColNameValue.put("name", "Student" + i);
                htblColNameValue.put("gpa", 3.0 + i);
                dbApp.insertIntoTable(strTableName, htblColNameValue);
            }

            // update a row

            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();

            htblColNameValue.put("name", "Student20");

            dbApp.updateTable(strTableName, "0", htblColNameValue);

            // check that the indexes are updated

            BPlusTree tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "nameIndex.class");

            for (int i = 0; i < 21; i++) {
                if (i == 0) {
                    assert tree.query("Student" + i).size() == 0;
                } else {
                    assert tree.query("Student" + i) != null && tree.query("Student" + i).size() == 1;

                    Pair pair = (Pair) tree.query("Student" + i).get(0);

                    Comparable clusteringKey = (Comparable) pair.getKey();

                    String pageName = (String) pair.getValue();

                    Page page = Page.deserialize("tables/" + strTableName + "/" + pageName + ".class");

                    int tupleIndex = TestCases.searchTuplesByClusteringKey("id", clusteringKey, page);

                    assert tupleIndex != -1;

                    Tuple tuple = page.getTupleWithIndex(tupleIndex);

                    assert tuple.getColumnValue("name").equals("Student" + i);

                }
            }
        } finally {
            cleanUp();
        }
    }

    // INTEGRATION TESTS

    @Test
    public void insertAndUpdateWithIndex() throws DBAppException, IOException, ClassNotFoundException {
        try {
            DBApp dbApp = new DBApp();

            dbApp.init();

            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();

            htblColNameType.put("id", "java.lang.Integer");

            htblColNameType.put("name", "java.lang.String");

            htblColNameType.put("gpa", "java.lang.Double");

            dbApp.createTable(strTableName, "id", htblColNameType);

            dbApp.createIndex(strTableName, "name", "nameIndex");

            // insert 20 rows

            for (int i = 0; i < 20; i++) {
                Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
                htblColNameValue.put("id", i);
                htblColNameValue.put("name", "Student" + i);
                htblColNameValue.put("gpa", 3.0 + i);
                dbApp.insertIntoTable(strTableName, htblColNameValue);
            }

            // update a row

            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();

            htblColNameValue.put("name", "Student20");

            dbApp.updateTable(strTableName, "0", htblColNameValue);

            // check that the index is updated

            BPlusTree tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "nameIndex.class");

            for (int i = 0; i < 21; i++) {
                if (i == 0) {
                    assert tree.query("Student" + i).size() == 0;
                } else {
                    assert tree.query("Student" + i) != null && tree.query("Student" + i).size() == 1;

                    Pair pair = (Pair) tree.query("Student" + i).get(0);

                    Comparable clusteringKey = (Comparable) pair.getKey();

                    String pageName = (String) pair.getValue();

                    Page page = Page.deserialize("tables/" + strTableName + "/" + pageName + ".class");

                    int tupleIndex = TestCases.searchTuplesByClusteringKey("id", clusteringKey, page);

                    assert tupleIndex != -1;

                    Tuple tuple = page.getTupleWithIndex(tupleIndex);

                    assert tuple.getColumnValue("name").equals("Student" + i);

                }
            }

        } finally {
            cleanUp();
        }
    }

    @Test
    public void insertAndUpdateWithMultipleIndexes() throws DBAppException, IOException, ClassNotFoundException {
        try {
            DBApp dbApp = new DBApp();

            dbApp.init();

            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();

            htblColNameType.put("id", "java.lang.Integer");

            htblColNameType.put("name", "java.lang.String");

            htblColNameType.put("gpa", "java.lang.Double");

            dbApp.createTable(strTableName, "id", htblColNameType);

            dbApp.createIndex(strTableName, "name", "nameIndex");

            dbApp.createIndex(strTableName, "gpa", "gpaIndex");

            // insert 20 rows

            for (int i = 0; i < 20; i++) {
                Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
                htblColNameValue.put("id", i);
                htblColNameValue.put("name", "Student" + i);
                htblColNameValue.put("gpa", 3.0 + i);
                dbApp.insertIntoTable(strTableName, htblColNameValue);
            }

            // update a row

            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();

            htblColNameValue.put("name", "Student20");

            dbApp.updateTable(strTableName, "0", htblColNameValue);

            // check that the indexes are updated

            BPlusTree tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "nameIndex.class");

            for (int i = 0; i < 21; i++) {
                if (i == 0) {
                    assert tree.query("Student" + i).size() == 0;
                } else {
                    assert tree.query("Student" + i) != null && tree.query("Student" + i).size() == 1;

                    Pair pair = (Pair) tree.query("Student" + i).get(0);

                    Comparable clusteringKey = (Comparable) pair.getKey();

                    String pageName = (String) pair.getValue();

                    Page page = Page.deserialize("tables/" + strTableName + "/" + pageName + ".class");

                    int tupleIndex = TestCases.searchTuplesByClusteringKey("id", clusteringKey, page);

                    assert tupleIndex != -1;

                    Tuple tuple = page.getTupleWithIndex(tupleIndex);

                    assert tuple.getColumnValue("name").equals("Student" + i);

                }
            }
        } finally {
            cleanUp();
        }
    }

    @Test
    public void deleteAndUpdateWithoutIndex() throws DBAppException, IOException, ClassNotFoundException {
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

            // delete a row

            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();

            htblColNameValue.put("id", 0);

            dbApp.deleteFromTable(strTableName, htblColNameValue);

            // update a row

            Hashtable<String, Object> htblColNameValue2 = new Hashtable<String, Object>();

            htblColNameValue2.put("name", "Student20");

            dbApp.updateTable(strTableName, "1", htblColNameValue2);

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
                    if (tuple.getColumnValue("id").equals(0)) {
                        assert false;
                    }
                    if (tuple.getColumnValue("id").equals(1)) {
                        assert tuple.getColumnValue("name").equals("Student20");
                    }
                }
            }

            // check that the row is updated

            for (int i = 0; i < 20; i++) {
                if (i == 1) {
                    Page page = Page.deserialize("tables/" + strTableName + "/Student_0.class");
                    int tupleIndex = TestCases.searchTuplesByClusteringKey("id", 1, page);
                    Tuple tuple = page.getTupleWithIndex(tupleIndex);
                    System.out.println(tuple.getColumnValue("name"));
                    assert tuple.getColumnValue("name").equals("Student20");
                }
            }

        } finally {
            cleanUp();
        }
    }

    public void deleteAndUpdateWithIndex() throws DBAppException, IOException, ClassNotFoundException {
        try {
            DBApp dbApp = new DBApp();

            dbApp.init();

            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();

            htblColNameType.put("id", "java.lang.Integer");

            htblColNameType.put("name", "java.lang.String");

            htblColNameType.put("gpa", "java.lang.Double");

            dbApp.createTable(strTableName, "id", htblColNameType);

            dbApp.createIndex(strTableName, "name", "nameIndex");

            // insert 20 rows

            for (int i = 0; i < 20; i++) {
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

            // update a row

            Hashtable<String, Object> htblColNameValue2 = new Hashtable<String, Object>();

            htblColNameValue2.put("name", "Student20");

            dbApp.updateTable(strTableName, "1", htblColNameValue2);

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
                    if (tuple.getColumnValue("id").equals(0)) {
                        assert false;
                    }
                }
            }

            // check that the row is updated

            BPlusTree tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "nameIndex.class");

            for (int i = 0; i < 20; i++) {
                if (i == 0) {
                    assert tree.query("Student" + i).size() == 0;
                } else {
                    assert tree.query("Student" + i) != null && tree.query("Student" + i).size() == 1;

                    Pair pair = (Pair) tree.query("Student" + i).get(0);

                    Comparable clusteringKey = (Comparable) pair.getKey();

                    String pageName = (String) pair.getValue();

                    Page page = Page.deserialize("tables/" + strTableName + "/" + pageName + ".class");

                    int tupleIndex = TestCases.searchTuplesByClusteringKey("id", clusteringKey, page);

                    assert tupleIndex != -1;

                    Tuple tuple = page.getTupleWithIndex(tupleIndex);

                    assert tuple.getColumnValue("name").equals("Student" + i);

                }
            }

        } finally {
            cleanUp();
        }
    }

    @Test
    public void deletAndUpdateWithIndexAndNonIndexedColumns()
            throws DBAppException, IOException, ClassNotFoundException {
        try {
            DBApp dbApp = new DBApp();

            dbApp.init();

            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();

            htblColNameType.put("id", "java.lang.Integer");

            htblColNameType.put("name", "java.lang.String");

            htblColNameType.put("gpa", "java.lang.Double");

            dbApp.createTable(strTableName, "id", htblColNameType);

            dbApp.createIndex(strTableName, "name", "nameIndex");

            // insert 20 rows

            for (int i = 0; i < 20; i++) {
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

            // update a row

            Hashtable<String, Object> htblColNameValue2 = new Hashtable<String, Object>();

            htblColNameValue2.put("name", "Student20");

            dbApp.updateTable(strTableName, "1", htblColNameValue2);

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
                    if (tuple.getColumnValue("id").equals(0)) {
                        assert false;
                    }
                }
            }

            // check that the row is updated

            BPlusTree tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "nameIndex.class");

            for (int i = 0; i < 20; i++) {
                if (i == 0) {
                    assert tree.query("Student" + i).size() == 0;

                } else if (i == 1){
                    assert tree.query("Student" + i).size() == 0;
                    assert tree.query("Student20").size() == 1;
                } else {
                    assert tree.query("Student" + i) != null && tree.query("Student" + i).size() == 1;

                    Pair pair = (Pair) tree.query("Student" + i).get(0);

                    Comparable clusteringKey = (Comparable) pair.getKey();

                    String pageName = (String) pair.getValue();

                    Page page = Page.deserialize("tables/" + strTableName + "/" + pageName + ".class");

                    int tupleIndex = TestCases.searchTuplesByClusteringKey("id", clusteringKey, page);

                    assert tupleIndex != -1;

                    Tuple tuple = page.getTupleWithIndex(tupleIndex);

                    assert tuple.getColumnValue("name").equals("Student" + i);

                }
            }

        } finally {
            cleanUp();
        }

    }

    @Test
    public void deleteAndUpdateWithMultipleIndexes() throws DBAppException, IOException, ClassNotFoundException {
        try {
            DBApp dbApp = new DBApp();

            dbApp.init();

            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();

            htblColNameType.put("id", "java.lang.Integer");

            htblColNameType.put("name", "java.lang.String");

            htblColNameType.put("gpa", "java.lang.Double");

            dbApp.createTable(strTableName, "id", htblColNameType);

            dbApp.createIndex(strTableName, "name", "nameIndex");

            dbApp.createIndex(strTableName, "gpa", "gpaIndex");

            // insert 20 rows

            for (int i = 0; i < 20; i++) {
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

            // update a row

            Hashtable<String, Object> htblColNameValue2 = new Hashtable<String, Object>();

            htblColNameValue2.put("name", "Student20");

            dbApp.updateTable(strTableName, "1", htblColNameValue2);

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
                    if (tuple.getColumnValue("id").equals(0)) {
                        assert false;
                    }
                    if (tuple.getColumnValue("id").equals(1)) {
                        assert tuple.getColumnValue("name").equals("Student20");
                    }
                }
            }

            // check that the indexes are updated

            BPlusTree tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "nameIndex.class");

            for (int i = 0; i < 20; i++) {
                if (i == 0) {
                    assert tree.query("Student" + i).size() == 0;
                }else if (i == 1){
                    assert tree.query("Student" + i).size() == 0;
                    assert tree.query("Student20").size() == 1;
                } else {
                    assert tree.query("Student" + i) != null && tree.query("Student" + i).size() == 1;

                    Pair pair = (Pair) tree.query("Student" + i).get(0);

                    Comparable clusteringKey = (Comparable) pair.getKey();

                    String pageName = (String) pair.getValue();

                    Page page = Page.deserialize("tables/" + strTableName + "/" + pageName + ".class");

                    int tupleIndex = TestCases.searchTuplesByClusteringKey("id", clusteringKey, page);

                    assert tupleIndex != -1;

                    Tuple tuple = page.getTupleWithIndex(tupleIndex);

                    assert tuple.getColumnValue("name").equals("Student" + i);

                }
            }

            tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "gpaIndex.class");

            for (int i = 0; i < 20; i++) {
                if (i == 0) {
                    assert tree.query(3.0 + i).size() == 0;
                } else {
                    assert tree.query(3.0 + i) != null && tree.query(3.0 + i).size() == 1;

                    Pair pair = (Pair) tree.query(3.0 + i).get(0);

                    Comparable clusteringKey = (Comparable) pair.getKey();

                    String pageName = (String) pair.getValue();

                    Page page = Page.deserialize("tables/" + strTableName + "/" + pageName + ".class");

                    int tupleIndex = TestCases.searchTuplesByClusteringKey("id", clusteringKey, page);

                    assert tupleIndex != -1;

                    Tuple tuple = page.getTupleWithIndex(tupleIndex);

                    assert tuple.getColumnValue("gpa").equals(3.0 + i);

                }
            }

        } finally {
            cleanUp();
        }

    }

    @Test
    public void deleteAndUpdateWithIndexedAndNonIndexedColumns()
            throws DBAppException, IOException, ClassNotFoundException {
        try {
            DBApp dbApp = new DBApp();

            dbApp.init();

            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();

            htblColNameType.put("id", "java.lang.Integer");

            htblColNameType.put("name", "java.lang.String");

            htblColNameType.put("gpa", "java.lang.Double");

            dbApp.createTable(strTableName, "id", htblColNameType);

            dbApp.createIndex(strTableName, "name", "nameIndex");

            // insert 20 rows

            for (int i = 0; i < 20; i++) {
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

            // update a row

            Hashtable<String, Object> htblColNameValue2 = new Hashtable<String, Object>();

            htblColNameValue2.put("name", "Student20");

            dbApp.updateTable(strTableName, "1", htblColNameValue2);

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
                    if (tuple.getColumnValue("id").equals(0)) {
                        assert false;
                    }
                    if (tuple.getColumnValue("id").equals(1)) {
                        assert tuple.getColumnValue("name").equals("Student20");
                    }
                }
            }

            // check that the indexes are updated

            BPlusTree tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "nameIndex.class");

            for (int i = 0; i < 20; i++) {
                if (i == 0) {
                    assert tree.query("Student" + i).size() == 0;
                } else if (i == 1) {

                    assert tree.query("Student" + i).size() == 0;
                    assert tree.query("Student20").size() == 1;
                } else {
                    assert tree.query("Student" + i) != null && tree.query("Student" + i).size() == 1;

                    Pair pair = (Pair) tree.query("Student" + i).get(0);

                    Comparable clusteringKey = (Comparable) pair.getKey();

                    String pageName = (String) pair.getValue();

                    Page page = Page.deserialize("tables/" + strTableName + "/" + pageName + ".class");

                    int tupleIndex = TestCases.searchTuplesByClusteringKey("id", clusteringKey, page);

                    assert tupleIndex != -1;

                    Tuple tuple = page.getTupleWithIndex(tupleIndex);

                    assert tuple.getColumnValue("name").equals("Student" + i);

                }
            }

        } finally {
            cleanUp();
        }

    }

    @Test
    public void deleteAndSelectWithoutIndex() throws DBAppException, IOException, ClassNotFoundException {
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

            // delete a row

            Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();

            htblColNameValue.put("id", 0);

            dbApp.deleteFromTable(strTableName, htblColNameValue);

            // select all rows

            SQLTerm[] arrSQLTerms = new SQLTerm[1];
            String[] strarrOperators = new String[0];

            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = strTableName;
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = "<";
            arrSQLTerms[0]._objValue = 100;

            Iterator iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);
            while(iterator.hasNext()){
                Tuple tuple = (Tuple) iterator.next();
                assert ((Integer) tuple.getColumnValue("id")) != 0;
                assert ((String) tuple.getColumnValue("name")).equals("Student" + tuple.getColumnValue("id"));
            }
            

        } finally {
            cleanUp();
        }
    }

    @Test
    public void deleteAndSelectWithIndex() throws DBAppException, IOException, ClassNotFoundException {
        try {
            DBApp dbApp = new DBApp();

            dbApp.init();

            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();

            htblColNameType.put("id", "java.lang.Integer");

            htblColNameType.put("name", "java.lang.String");

            htblColNameType.put("gpa", "java.lang.Double");

            dbApp.createTable(strTableName, "id", htblColNameType);

            dbApp.createIndex(strTableName, "name", "nameIndex");

            // insert 20 rows

            for (int i = 0; i < 20; i++) {
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

            // check that the index is updated

            BPlusTree tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "nameIndex.class");

            for (int i = 0; i < 20; i++) {
                if (i == 0) {
                    assert tree.query("Student" + i).size() == 0;
                } else {
                    assert tree.query("Student" + i) != null && tree.query("Student" + i).size() == 1;

                    Pair pair = (Pair) tree.query("Student" + i).get(0);

                    Comparable clusteringKey = (Comparable) pair.getKey();

                    String pageName = (String) pair.getValue();

                    Page page = Page.deserialize("tables/" + strTableName + "/" + pageName + ".class");

                    int tupleIndex = TestCases.searchTuplesByClusteringKey("id", clusteringKey, page);

                    assert tupleIndex != -1;

                    Tuple tuple = page.getTupleWithIndex(tupleIndex);

                    assert tuple.getColumnValue("name").equals("Student" + i);

                }
            }

            // select all rows

            SQLTerm[] arrSQLTerms = new SQLTerm[1];
            String[] strarrOperators = new String[0];

            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = strTableName;
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = "<";
            arrSQLTerms[0]._objValue = 100;

            Iterator iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            while(iterator.hasNext()){
                Tuple tuple = (Tuple) iterator.next();
                assert ((Integer) tuple.getColumnValue("id")) != 0;
                assert ((String) tuple.getColumnValue("name")).equals("Student" + tuple.getColumnValue("id"));
            }

            // check that index is maintained

            tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "nameIndex.class");

            for (int i = 0; i < 20; i++) {
                if (i == 0) {
                    assert tree.query("Student" + i).size() == 0;
                } else {
                    assert tree.query("Student" + i) != null && tree.query("Student" + i).size() == 1;

                    Pair pair = (Pair) tree.query("Student" + i).get(0);

                    Comparable clusteringKey = (Comparable) pair.getKey();

                    String pageName = (String) pair.getValue();

                    Page page = Page.deserialize("tables/" + strTableName + "/" + pageName + ".class");

                    int tupleIndex = TestCases.searchTuplesByClusteringKey("id", clusteringKey, page);

                    assert tupleIndex != -1;

                    Tuple tuple = page.getTupleWithIndex(tupleIndex);

                    assert tuple.getColumnValue("name").equals("Student" + i);

                }
            }


            
                
            

        } finally {
            cleanUp();
        }
    }

    @Test
    public void deleteAndSelectWithMultipleIndexes() throws DBAppException, IOException, ClassNotFoundException {
        try {
            DBApp dbApp = new DBApp();

            dbApp.init();

            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();

            htblColNameType.put("id", "java.lang.Integer");

            htblColNameType.put("name", "java.lang.String");

            htblColNameType.put("gpa", "java.lang.Double");

            dbApp.createTable(strTableName, "id", htblColNameType);

            dbApp.createIndex(strTableName, "name", "nameIndex");

            dbApp.createIndex(strTableName, "gpa", "gpaIndex");

            // insert 20 rows

            for (int i = 0; i < 20; i++) {
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

            // check that the indexes are updated

            BPlusTree tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "nameIndex.class");

            for (int i = 0; i < 20; i++) {
                if (i == 0) {
                    assert tree.query("Student" + i).size() == 0;
                } else {
                    assert tree.query("Student" + i) != null && tree.query("Student" + i).size() == 1;

                    Pair pair = (Pair) tree.query("Student" + i).get(0);

                    Comparable clusteringKey = (Comparable) pair.getKey();

                    String pageName = (String) pair.getValue();

                    Page page = Page.deserialize("tables/" + strTableName + "/" + pageName + ".class");

                    int tupleIndex = TestCases.searchTuplesByClusteringKey("id", clusteringKey, page);

                    assert tupleIndex != -1;

                    Tuple tuple = page.getTupleWithIndex(tupleIndex);

                    assert tuple.getColumnValue("name").equals("Student" + i);

                }
            }

            tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "gpaIndex.class");

            for (int i = 0; i < 20; i++) {
                if (i == 0) {
                    assert tree.query(3.0 + i).size() == 0;
                } else {
                    assert tree.query(3.0 + i) != null && tree.query(3.0 + i).size() == 1;

                    Pair pair = (Pair) tree.query(3.0 + i).get(0);

                    Comparable clusteringKey = (Comparable) pair.getKey();

                    String pageName = (String) pair.getValue();

                    Page page = Page.deserialize("tables/" + strTableName + "/" + pageName + ".class");

                    int tupleIndex = TestCases.searchTuplesByClusteringKey("id", clusteringKey, page);

                    assert tupleIndex != -1;

                    Tuple tuple = page.getTupleWithIndex(tupleIndex);

                    assert tuple.getColumnValue("gpa").equals(3.0 + i);

                }
            }



            // select all rows

            SQLTerm[] arrSQLTerms = new SQLTerm[1];
            String[] strarrOperators = new String[0];

            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = strTableName;
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = "<";
            arrSQLTerms[0]._objValue = 100;

            Iterator iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            while(iterator.hasNext()){
                Tuple tuple = (Tuple) iterator.next();
                assert ((Integer) tuple.getColumnValue("id")) != 0;
                assert ((String) tuple.getColumnValue("name")).equals("Student" + tuple.getColumnValue("id"));
            }

            // make sure the indexes are maintained and not tampered with

            tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "nameIndex.class");

            for (int i = 0; i < 20; i++) {
                if (i == 0) {
                    assert tree.query("Student" + i).size() == 0;
                } else {
                    assert tree.query("Student" + i) != null && tree.query("Student" + i).size() == 1;

                    Pair pair = (Pair) tree.query("Student" + i).get(0);

                    Comparable clusteringKey = (Comparable) pair.getKey();

                    String pageName = (String) pair.getValue();

                    Page page = Page.deserialize("tables/" + strTableName + "/" + pageName + ".class");

                    int tupleIndex = TestCases.searchTuplesByClusteringKey("id", clusteringKey, page);

                    assert tupleIndex != -1;

                    Tuple tuple = page.getTupleWithIndex(tupleIndex);

                    assert tuple.getColumnValue("name").equals("Student" + i);

                }
            }


        } finally {
            cleanUp();
        }

    }

    @Test
    public void deleteAndSelectWithIndexedAndNonIndexedColumns()
            throws DBAppException, IOException, ClassNotFoundException {
        try {
            DBApp dbApp = new DBApp();

            dbApp.init();

            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();

            htblColNameType.put("id", "java.lang.Integer");

            htblColNameType.put("name", "java.lang.String");

            htblColNameType.put("gpa", "java.lang.Double");

            dbApp.createTable(strTableName, "id", htblColNameType);

            dbApp.createIndex(strTableName, "name", "nameIndex");

            // insert 20 rows

            for (int i = 0; i < 20; i++) {
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

            // check that the indexes are updated

            BPlusTree tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "nameIndex.class");

            for (int i = 0; i < 20; i++) {
                if (i == 0) {
                    assert tree.query("Student" + i).size() == 0;
                } else {
                    assert tree.query("Student" + i) != null && tree.query("Student" + i).size() == 1;

                    Pair pair = (Pair) tree.query("Student" + i).get(0);

                    Comparable clusteringKey = (Comparable) pair.getKey();

                    String pageName = (String) pair.getValue();

                    Page page = Page.deserialize("tables/" + strTableName + "/" + pageName + ".class");

                    int tupleIndex = TestCases.searchTuplesByClusteringKey("id", clusteringKey, page);

                    assert tupleIndex != -1;

                    Tuple tuple = page.getTupleWithIndex(tupleIndex);

                    assert tuple.getColumnValue("name").equals("Student" + i);

                }
            }



            // select all rows

            SQLTerm[] arrSQLTerms = new SQLTerm[1];
            String[] strarrOperators = new String[0];

            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = strTableName;
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = "<";
            arrSQLTerms[0]._objValue = 100;

            Iterator iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            while(iterator.hasNext()){
                Tuple tuple = (Tuple) iterator.next();
                assert ((Integer) tuple.getColumnValue("id")) != 0;
                assert ((String) tuple.getColumnValue("name")).equals("Student" + tuple.getColumnValue("id"));
            }

            // make sure the indexes are maintained and not tampered with

            tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "nameIndex.class");

            for (int i = 0; i < 20; i++) {
                if (i == 0) {
                    assert tree.query("Student" + i).size() == 0;
                } else {
                    assert tree.query("Student" + i) != null && tree.query("Student" + i).size() == 1;

                    Pair pair = (Pair) tree.query("Student" + i).get(0);

                    Comparable clusteringKey = (Comparable) pair.getKey();

                    String pageName = (String) pair.getValue();

                    Page page = Page.deserialize("tables/" + strTableName + "/" + pageName + ".class");

                    int tupleIndex = TestCases.searchTuplesByClusteringKey("id", clusteringKey, page);

                    assert tupleIndex != -1;

                    Tuple tuple = page.getTupleWithIndex(tupleIndex);

                    assert tuple.getColumnValue("name").equals("Student" + i);

                }
            }

        } finally {
            cleanUp();
        }

    }

    @Test
    public void deleteAndSelectWithMultipleIndexesAndNonIndexedColumns()
            throws DBAppException, IOException, ClassNotFoundException {
        try {
            DBApp dbApp = new DBApp();

            dbApp.init();

            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();

            htblColNameType.put("id", "java.lang.Integer");

            htblColNameType.put("name", "java.lang.String");

            htblColNameType.put("gpa", "java.lang.Double");

            dbApp.createTable(strTableName, "id", htblColNameType);

            dbApp.createIndex(strTableName, "name", "nameIndex");

            dbApp.createIndex(strTableName, "gpa", "gpaIndex");

            // insert 20 rows

            for (int i = 0; i < 20; i++) {
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

            // make sure the indexes are updated

            BPlusTree tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "nameIndex.class");

            for (int i = 0; i < 20; i++) {
                if (i == 0) {
                    assert tree.query("Student" + i).size() == 0;
                } else {
                    assert tree.query("Student" + i) != null && tree.query("Student" + i).size() == 1;

                    Pair pair = (Pair) tree.query("Student" + i).get(0);

                    Comparable clusteringKey = (Comparable) pair.getKey();

                    String pageName = (String) pair.getValue();

                    Page page = Page.deserialize("tables/" + strTableName + "/" + pageName + ".class");

                    int tupleIndex = TestCases.searchTuplesByClusteringKey("id", clusteringKey, page);

                    assert tupleIndex != -1;

                    Tuple tuple = page.getTupleWithIndex(tupleIndex);

                    assert tuple.getColumnValue("name").equals("Student" + i);

                }
            }

            tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "gpaIndex.class");

            for (int i = 0; i < 20; i++) {
                if (i == 0) {
                    assert tree.query(3.0 + i).size() == 0;
                } else {
                    assert tree.query(3.0 + i) != null && tree.query(3.0 + i).size() == 1;

                    Pair pair = (Pair) tree.query(3.0 + i).get(0);

                    Comparable clusteringKey = (Comparable) pair.getKey();

                    String pageName = (String) pair.getValue();

                    Page page = Page.deserialize("tables/" + strTableName + "/" + pageName + ".class");

                    int tupleIndex = TestCases.searchTuplesByClusteringKey("id", clusteringKey, page);

                    assert tupleIndex != -1;

                    Tuple tuple = page.getTupleWithIndex(tupleIndex);

                    assert tuple.getColumnValue("gpa").equals(3.0 + i);

                }
            }

            // select all rows

            SQLTerm[] arrSQLTerms = new SQLTerm[1];
            String[] strarrOperators = new String[0];

            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = strTableName;
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = "<";
            arrSQLTerms[0]._objValue = 100;

            Iterator iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            while (iterator.hasNext()) {
                Tuple tuple = (Tuple) iterator.next();
                assert ((Integer) tuple.getColumnValue("id")) != 0;
                assert ((String) tuple.getColumnValue("name")).equals("Student" + tuple.getColumnValue("id"));
            }

            // make sure the indexes are maintained and not tampered with

            tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "nameIndex.class");

            for (int i = 0; i < 20; i++) {
                if (i == 0) {
                    assert tree.query("Student" + i).size() == 0;
                } else {
                    assert tree.query("Student" + i) != null && tree.query("Student" + i).size() == 1;

                    Pair pair = (Pair) tree.query("Student" + i).get(0);

                    Comparable clusteringKey = (Comparable) pair.getKey();

                    String pageName = (String) pair.getValue();

                    Page page = Page.deserialize("tables/" + strTableName + "/" + pageName + ".class");

                    int tupleIndex = TestCases.searchTuplesByClusteringKey("id", clusteringKey, page);

                    assert tupleIndex != -1;

                    Tuple tuple = page.getTupleWithIndex(tupleIndex);

                    assert tuple.getColumnValue("name").equals("Student" + i);

                }
            }

            tree = BPlusTree.deserialize("tables/" + strTableName + "/" + "gpaIndex.class");

            for (int i = 0; i < 20; i++) {
                if (i == 0) {
                    assert tree.query(3.0 + i).size() == 0;
                } else {
                    assert tree.query(3.0 + i) != null && tree.query(3.0 + i).size() == 1;

                    Pair pair = (Pair) tree.query(3.0 + i).get(0);

                    Comparable clusteringKey = (Comparable) pair.getKey();

                    String pageName = (String) pair.getValue();

                    Page page = Page.deserialize("tables/" + strTableName + "/" + pageName + ".class");

                    int tupleIndex = TestCases.searchTuplesByClusteringKey("id", clusteringKey, page);

                    assert tupleIndex != -1;

                    Tuple tuple = page.getTupleWithIndex(tupleIndex);

                    assert tuple.getColumnValue("gpa").equals(3.0 + i);

                }
            }


        } finally {
            cleanUp();
        }

    }

    @Test
    public void checkRightOrder () throws DBAppException, IOException{
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
            

            Hashtable<String , Object> ht = new Hashtable<>();
            ht.put("id" , 2);
            dbApp.deleteFromTable(strTableName , ht);
            ht.put("id" , 4);
            dbApp.deleteFromTable(strTableName , ht);

            // check that two pages are half full
            Table table = Table.deserialize("tables/" + strTableName + "/" + strTableName
                    + ".class");
            for(int i = 0 ; i < table.getNumberOfPages() ; i ++){
                Page page = Page.deserialize("tables/" + strTableName + "/" +
                        table.getPages().get(i) + ".class");
                Vector<Tuple> vec = page.getTuples();
                System.out.println("AMR " + vec);
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
    


    /**
     * This Java function searches for tuples by a given clustering key name and
     * value using binary
     * search.
     * 
     * @param strClusteringKeyName  The `strClusteringKeyName` parameter is the name
     *                              of the clustering
     *                              key that you want to search for in the list of
     *                              tuples. It is used to identify the specific
     *                              attribute or column in the tuple that serves as
     *                              the clustering key for the data structure.
     * @param objClusteringKeyValue The `objClusteringKeyValue` parameter represents
     *                              the value of the
     *                              clustering key that you are searching for within
     *                              the list of tuples. The method
     *                              `searchTuplesByClusteringKey` is designed to
     *                              search for a specific tuple within the list of
     *                              tuples based on the provided clustering key name
     *                              and value.
     * @return The method is returning the index of the tuple in the `vecTuples`
     *         list that matches the
     *         provided clustering key name and value. If a match is found, the
     *         method returns the index of
     *         that tuple. If no match is found after the binary search, it throws a
     *         `DBAppException` with the
     *         message "Column Value doesn't exist".
     */
    public static int searchTuplesByClusteringKey(String strClusteringKeyName, Object objClusteringKeyValue, Page page)
            throws DBAppException {

        Vector<Tuple> vecTuples = page.getTuples();

        int n = vecTuples.size();

        int left = 0, right = n - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            Comparable midClusteringKeyValue = (Comparable) vecTuples.get(mid).getColumnValue(strClusteringKeyName);

            Comparable compClusteringKeyValue = (Comparable) objClusteringKeyValue;

            if (midClusteringKeyValue.compareTo(compClusteringKeyValue) == 0) {
                return mid;
            } else if (midClusteringKeyValue.compareTo(compClusteringKeyValue) < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }

        }

        return -1;

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

    private void initializeTestTable(int n) {
        try {
            DBApp dbApp = new DBApp();

            dbApp.init();

            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();

            htblColNameType.put("id", "java.lang.Integer");

            htblColNameType.put("name", "java.lang.String");

            htblColNameType.put("gpa", "java.lang.Double");

            dbApp.createTable(strTableName, "id", htblColNameType);

            // insert n rows

            for (int i = 0; i < n; i++) {
                Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
                htblColNameValue.put("id", i);
                htblColNameValue.put("name", "Student" + i);
                htblColNameValue.put("gpa", 3.0 + i);
                dbApp.insertIntoTable(strTableName, htblColNameValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
