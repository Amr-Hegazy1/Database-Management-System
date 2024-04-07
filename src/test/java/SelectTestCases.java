import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.*;

import org.junit.Test;

import com.db_engine.DBApp;
import com.db_engine.DBAppException;
import com.db_engine.SQLTerm;
import com.db_engine.Tuple;

public class SelectTestCases {

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

            // insert 20 rows

            for (int i = 0; i < 20; i++) {
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

            for (int i = 0; i < 20; i++) {
                if (i == 5) {
                    assert iterator.hasNext();
                    Tuple tuple = (Tuple) iterator.next();
                    assert tuple.getColumnValue("id").equals(5);
                    assert tuple.getColumnValue("name").equals("Student5");
                    assert tuple.getColumnValue("gpa").equals(3.0 + 5);
                } else {
                    assert !iterator.hasNext();
                }
            }

        } finally {
            cleanUp();
        }
    }

    @Test
    public void selectExceptionTests() throws IOException, DBAppException {
        // Test Invalid Inputs for Select (Ensure Correct Exception Handling)

        try {
            DBApp dbApp = new DBApp();
            dbApp.init();

            initializeTestTable(20); // Initializes Student Table with Columns Id, name, GPA , with rows count n = 20

            SQLTerm[] arrSQLTerms = new SQLTerm[1];
            String[] strarrOperators = new String[0];

            // select with invalid Table name
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "WrongStudent";
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = 5;

            assertThrows(DBAppException.class, () -> { // select * from WrongStudent where id=5;
                dbApp.selectFromTable(arrSQLTerms, strarrOperators);
            });

            // select with invalid column name
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "iddd";

            assertThrows(DBAppException.class, () -> { // select * from student where iddd=5;
                dbApp.selectFromTable(arrSQLTerms, strarrOperators);
            });

            // select with invalid operator
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = "==";

            assertThrows(DBAppException.class, () -> { // select * from student where id==5;
                dbApp.selectFromTable(arrSQLTerms, strarrOperators);
            });

            // select with invalid datatype on clust key
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = "hello";

            assertThrows(DBAppException.class, () -> { // Select * from student where id="Hello";
                dbApp.selectFromTable(arrSQLTerms, strarrOperators);
            });

            // select with invalid datatype on non-clustering column
            arrSQLTerms[0]._strColumnName = "name";
            arrSQLTerms[0]._objValue = 5;

            assertThrows(DBAppException.class, () -> { // Select * from student where name=5;
                dbApp.selectFromTable(arrSQLTerms, strarrOperators);
            });

            // select with strarrOperators empty
            arrSQLTerms[0]._strColumnName = "id";
            String[] EmptystrarrOperators = new String[1];

            assertThrows(DBAppException.class, () -> { // Select * from student where id=5; (With empty strarrOperators)
                dbApp.selectFromTable(arrSQLTerms, EmptystrarrOperators);
            });

            // select with invalid strarrOperators Value (Test 1: Base Case - 1 Operator)
            SQLTerm[] newArrSQLTerms = new SQLTerm[2];
            String[] newStrarrOperators = new String[1];
            newArrSQLTerms[0] = new SQLTerm();
            newArrSQLTerms[0]._strTableName = "Student";
            newArrSQLTerms[0]._strColumnName = "id";
            newArrSQLTerms[0]._strOperator = "=";
            newArrSQLTerms[0]._objValue = 1;

            newStrarrOperators[0] = "NOT";

            newArrSQLTerms[1] = new SQLTerm();
            newArrSQLTerms[1]._strTableName = "Student";
            newArrSQLTerms[1]._strColumnName = "gpa";
            newArrSQLTerms[1]._strOperator = "=";
            newArrSQLTerms[1]._objValue = 3.2;

            assertThrows(DBAppException.class, () -> { // Select * from student where id=1 NOT gpa=3.2;
                dbApp.selectFromTable(newArrSQLTerms, newStrarrOperators);
            });

            // select with invalid strarrOperators Value (Test 2 : 2 Operators, One Correct
            // and One Invalid)
            SQLTerm[] newArrSQLTerms2 = new SQLTerm[3];
            String[] newStrarrOperators2 = new String[2];

            newArrSQLTerms2[0] = new SQLTerm();
            newArrSQLTerms2[0]._strTableName = "Student";
            newArrSQLTerms2[0]._strColumnName = "id";
            newArrSQLTerms2[0]._strOperator = "=";
            newArrSQLTerms2[0]._objValue = 1;

            newStrarrOperators2[0] = "AND";

            newArrSQLTerms2[1] = new SQLTerm();
            newArrSQLTerms2[1]._strTableName = "Student";
            newArrSQLTerms2[1]._strColumnName = "gpa";
            newArrSQLTerms2[1]._strOperator = "=";
            newArrSQLTerms2[1]._objValue = 3.2;

            newStrarrOperators2[1] = "NOT";

            newArrSQLTerms2[2] = new SQLTerm();
            newArrSQLTerms2[2]._strTableName = "Student";
            newArrSQLTerms2[2]._strColumnName = "name";
            newArrSQLTerms2[2]._strOperator = "=";
            newArrSQLTerms2[2]._objValue = "Student1";

            assertThrows(DBAppException.class, () -> { // Select * from student where id=1 AND gpa=3.2 NOT
                                                       // name="Student1";
                dbApp.selectFromTable(newArrSQLTerms2, newStrarrOperators2);
            });

        }

        finally {
            cleanUp();
        }

    }

    @Test
    public void ExistingTuplesExactValues() throws DBAppException, ClassNotFoundException, IOException {
        // testing the base case: Searching for a tuple that exists using exact values.

        try {

            DBApp dbApp = new DBApp();
            dbApp.init();

            initializeTestTable(20);

            SQLTerm[] arrSQLTerms = new SQLTerm[1];
            String[] strarrOperators = new String[0];

            // Test with Clustering key
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = 5;

            Iterator iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            while (iterator.hasNext()) {
                Tuple tuple = (Tuple) iterator.next();
                assert tuple.getColumnValue("id").equals(5);
                assert tuple.getColumnValue("name").equals("Student5");
                assert tuple.getColumnValue("gpa").equals(3.0 + 5);
            }

            // Test with Non-clustering key (Test 1: String)
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "name";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = "Student2";

            Iterator iterator2 = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            while (iterator2.hasNext()) {
                Tuple tuple = (Tuple) iterator.next();
                assert tuple.getColumnValue("id").equals(2);
                assert tuple.getColumnValue("name").equals("Student2");
                assert tuple.getColumnValue("gpa").equals(3.0 + 2);
            }

            // Test with Non-clustering key (Test 2: Double)
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "gpa";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = 3.6;

            Iterator iterator3 = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            while (iterator3.hasNext()) {
                Tuple tuple = (Tuple) iterator.next();
                assert tuple.getColumnValue("id").equals(6);
                assert tuple.getColumnValue("name").equals("Student6");
                assert tuple.getColumnValue("gpa").equals(3.0 + 6);
            }
        } finally {
            cleanUp();
        }

    }

    @Test
    public void ExistingTuplesRangedValues() throws DBAppException, ClassNotFoundException, IOException {
        // select tuples that exist in my table using ranged values
        try {
            // TODO: Implement this test case

        } finally {
            cleanUp();
        }
    }

    @Test
    public void NonExistantTuplesExactValues() throws DBAppException, ClassNotFoundException, IOException {
        // select tuples/values that dont exist in my table using exact values

        try {
            DBApp dbApp = new DBApp();
            dbApp.init();

            initializeTestTable(20);

            SQLTerm[] arrSQLTerms = new SQLTerm[1];
            String[] strarrOperators = new String[0];

            // Test on Clustering Key (Test 1)
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = 40;

            Iterator iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            assert !iterator.hasNext();

            // Test on Clustering Key (Test 2)
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = -1;

            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            assert !iterator.hasNext();

            // Test on Non-Clustering Key (Test 1 : String)
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "name";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = "Harridy";

            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            assert !iterator.hasNext();

            // Test on Non-Clustering Key (Test 2 : Double)
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "gpa";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = 1.0;

            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            assert !iterator.hasNext();
        } finally {
            cleanUp();
        }
    }

    @Test
    public void NonExistantTuplesRangedValues() throws DBAppException, ClassNotFoundException, IOException {
        // Select values that dont exist using ranges
        try {
            // TODO : Implement this test case
        } finally {
            cleanUp();
        }
    }

    private void cleanUp() throws IOException {
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
