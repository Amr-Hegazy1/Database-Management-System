package SelectTests;

import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

import com.db_engine.DBApp;
import com.db_engine.DBAppException;
import com.db_engine.SQLTerm;
import com.db_engine.Tuple;

public class SelectTestCases {

    // @Test
    // public void selectWithoutIndexWithId() throws DBAppException, IOException,
    // ClassNotFoundException {
    // try {
    // DBApp dbApp = new DBApp();

    // dbApp.init();

    // String strTableName = "Student";

    // Hashtable<String, String> htblColNameType = new Hashtable<String, String>();

    // htblColNameType.put("id", "java.lang.Integer");

    // htblColNameType.put("name", "java.lang.String");

    // htblColNameType.put("gpa", "java.lang.Double");

    // dbApp.createTable(strTableName, "id", htblColNameType);

    // // insert 20 rows

    // for (int i = 0; i < 20; i++) {
    // Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
    // htblColNameValue.put("id", i);
    // htblColNameValue.put("name", "Student" + i);
    // htblColNameValue.put("gpa", 3.0 + i);
    // dbApp.insertIntoTable(strTableName, htblColNameValue);
    // }

    // // select all rows

    // SQLTerm[] arrSQLTerms = new SQLTerm[1];
    // String[] strarrOperators = new String[0];

    // arrSQLTerms[0] = new SQLTerm();
    // arrSQLTerms[0]._strTableName = strTableName;
    // arrSQLTerms[0]._strColumnName = "id";
    // arrSQLTerms[0]._strOperator = "=";
    // arrSQLTerms[0]._objValue = 5;

    // Iterator iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

    // for (int i = 0; i < 20; i++) {
    // if (i == 5) {
    // assert iterator.hasNext();
    // Tuple tuple = (Tuple) iterator.next();
    // assert tuple.getColumnValue("id").equals(5);
    // assert tuple.getColumnValue("name").equals("Student5");
    // assert tuple.getColumnValue("gpa").equals(3.0 + 5);
    // } else {
    // assert !iterator.hasNext();
    // }
    // }

    // } finally {
    // cleanUp();
    // }
    // }

    @Test
    public void invalidTableNameException() throws DBAppException, IOException {
        try {
            DBApp dbApp = new DBApp();
            dbApp.init();

            initializeTestTable(dbApp, 20); // Initializes Student Table with Columns Id, name, GPA , with rows count n
                                            // = 20

            SQLTerm[] arrSQLTerms = new SQLTerm[1];
            String[] strarrOperators = new String[0];

            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "WrongStudent";
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = 5;

            // select with invalid Table name
            // final SQLTerm[] arrSQLTerms = arrSQLTerms;
            // final String[] strarrOperators = strarrOperators;
            assertThrows(DBAppException.class, () -> { // select * from WrongStudent where id=5;
                dbApp.selectFromTable(arrSQLTerms, strarrOperators);
            });
        } finally {
            cleanUp();
        }
    }

    @Test
    public void invalidOperatorException() throws DBAppException, IOException {
        try {
            DBApp dbApp = new DBApp();
            dbApp.init();

            initializeTestTable(dbApp, 20); // Initializes Student Table with Columns Id, name, GPA , with rows count n
                                            // = 20

            SQLTerm[] arrSQLTerms = new SQLTerm[1];
            String[] strarrOperators = new String[0];

            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = "==";
            arrSQLTerms[0]._objValue = 5;

            assertThrows(DBAppException.class, () -> { // select * from student where id==5;
                dbApp.selectFromTable(arrSQLTerms, strarrOperators);
            });
        } finally {
            cleanUp();
        }
    }

    @Test
    public void invalidDataTypeOnClustKeyException() throws DBAppException, IOException {
        try {
            DBApp dbApp = new DBApp();
            dbApp.init();

            initializeTestTable(dbApp, 20); // Initializes Student Table with Columns Id, name, GPA , with rows count n
                                            // = 20

            SQLTerm[] arrSQLTerms = new SQLTerm[1];
            String[] strarrOperators = new String[0];

            // select with invalid datatype on clust key
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = "hello";

            assertThrows(DBAppException.class, () -> { // select * from student where id="hello";
                dbApp.selectFromTable(arrSQLTerms, strarrOperators);
            });

        } finally {
            cleanUp();
        }
    }

    @Test
    public void invalidDataTypeOnNonClustKeyException() throws DBAppException, IOException {
        try {
            DBApp dbApp = new DBApp();
            dbApp.init();

            initializeTestTable(dbApp, 20); // Initializes Student Table with Columns Id, name, GPA , with rows count n
                                            // = 20

            SQLTerm[] arrSQLTerms = new SQLTerm[1];
            String[] strarrOperators = new String[0];

            // select with invalid datatype on non-clust key
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "name";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = 5;

            assertThrows(DBAppException.class, () -> { // select * from student where name=5;
                dbApp.selectFromTable(arrSQLTerms, strarrOperators);
            });

        } finally {
            cleanUp();
        }
    }

    @Test
    public void EmptystrarrOperatorsException() throws DBAppException, IOException {
        try {
            DBApp dbApp = new DBApp();
            dbApp.init();

            initializeTestTable(dbApp, 20); // Initializes Student Table with Columns Id, name, GPA , with rows count n
                                            // = 20

            SQLTerm[] arrSQLTerms = new SQLTerm[2];
            String[] EmptystrarrOperators = new String[1];

            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = 5;

            arrSQLTerms[1] = new SQLTerm();
            arrSQLTerms[1]._strTableName = "Student";
            arrSQLTerms[1]._strColumnName = "name";
            arrSQLTerms[1]._strOperator = "=";
            arrSQLTerms[1]._objValue = "Student5";

            // select with strarrOperators empty
            assertThrows(DBAppException.class, () -> { // Select * from student where id=5 .... name="Student5"; (With
                                                       // empty strarrOperators)
                dbApp.selectFromTable(arrSQLTerms, EmptystrarrOperators);
            });

        } finally {
            cleanUp();
        }
    }

    @Test
    public void invalidArgOperatorException1() throws DBAppException, IOException { // Test 1: Base Case - 1 Operator
                                                                                    // (Arg operators : AND, OR, XOR)
        try {
            DBApp dbApp = new DBApp();
            dbApp.init();

            initializeTestTable(dbApp, 20); // Initializes Student Table with Columns Id, name, GPA , with rows count n
                                            // = 20

            SQLTerm[] arrSQLTerms = new SQLTerm[2];
            String[] strarrOperators = new String[1];

            // select with invalid strarrOperators Value (Test 1: Base Case - 1 Operator)
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = 1;

            strarrOperators[0] = "NOT";

            arrSQLTerms[1] = new SQLTerm();
            arrSQLTerms[1]._strTableName = "Student";
            arrSQLTerms[1]._strColumnName = "gpa";
            arrSQLTerms[1]._strOperator = "=";
            arrSQLTerms[1]._objValue = 5.0;

            assertThrows(DBAppException.class, () -> { // Select * from student where id=1 NOT gpa=5.0;
                dbApp.selectFromTable(arrSQLTerms, strarrOperators);
            });

        } finally {
            cleanUp();
        }
    }

    @Test
    public void invalidArgOperatorException2() throws DBAppException, IOException { // Test 2: 2 Operators, One Valid,
                                                                                    // One Invalid (Arg operators : AND,
                                                                                    // OR, XOR)
        try {
            DBApp dbApp = new DBApp();
            dbApp.init();

            initializeTestTable(dbApp, 20); // Initializes Student Table with Columns Id, name, GPA , with rows count n
                                            // = 20

            SQLTerm[] arrSQLTerms = new SQLTerm[3];
            String[] strarrOperators = new String[2];

            // select with invalid strarrOperators Value (Test 2: 2 Operators, One Valid,
            // One Invalid)

            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = 1;

            strarrOperators[0] = "AND";

            arrSQLTerms[1] = new SQLTerm();
            arrSQLTerms[1]._strTableName = "Student";
            arrSQLTerms[1]._strColumnName = "gpa";
            arrSQLTerms[1]._strOperator = "=";
            arrSQLTerms[1]._objValue = 5.0;

            strarrOperators[1] = "NOT";

            arrSQLTerms[2] = new SQLTerm();
            arrSQLTerms[2]._strTableName = "Student";
            arrSQLTerms[2]._strColumnName = "name";
            arrSQLTerms[2]._strOperator = "=";
            arrSQLTerms[2]._objValue = "Student1";

            assertThrows(DBAppException.class, () -> { // Select * from student where id=1 AND gpa=5.0 NOT
                                                       // name="Student1"
                dbApp.selectFromTable(arrSQLTerms, strarrOperators);
            });

        } finally {
            cleanUp();
        }
    }

    @Test
    public void invalidArgOperatorException3() throws DBAppException, IOException { // Test 3: Invalid Num of
                                                                                    // ArgOperators (Arg operators :
                                                                                    // AND, OR, XOR)
        try {
            DBApp dbApp = new DBApp();
            dbApp.init();

            initializeTestTable(dbApp, 20); // Initializes Student Table with Columns Id, name, GPA , with rows count n
                                            // = 20

            // select with invalid number of strarrOperators Value (Test 3)
            SQLTerm[] arrSQLTerms = new SQLTerm[3];
            String[] strarrOperators = new String[1];

            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = 1;

            strarrOperators[0] = "AND";

            arrSQLTerms[1] = new SQLTerm();
            arrSQLTerms[1]._strTableName = "Student";
            arrSQLTerms[1]._strColumnName = "gpa";
            arrSQLTerms[1]._strOperator = "=";
            arrSQLTerms[1]._objValue = 5.0;

            arrSQLTerms[2] = new SQLTerm();
            arrSQLTerms[2]._strTableName = "Student";
            arrSQLTerms[2]._strColumnName = "name";
            arrSQLTerms[2]._strOperator = "=";
            arrSQLTerms[2]._objValue = "Student1";

            assertThrows(DBAppException.class, () -> { // Select * from student where id =1 AND GPA = 5.0 ...name =
                                                       // Student1;
                dbApp.selectFromTable(arrSQLTerms, strarrOperators);
            });

        } finally {
            cleanUp();
        }
    }

    @Test
    public void invalidArgOperatorException4() throws DBAppException, IOException { // Test 4: Invalid Num of
                                                                                    // ArgOperators (Arg operators :
                                                                                    // AND, OR, XOR)
        try {
            DBApp dbApp = new DBApp();
            dbApp.init();

            initializeTestTable(dbApp, 20); // Initializes Student Table with Columns Id, name, GPA , with rows count n
                                            // = 20
            // select with invalid number of strarrOperators Value (Test 4)
            SQLTerm[] arrSQLTerms = new SQLTerm[3];
            String[] strarrOperators = new String[3];

            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = 1;

            strarrOperators[0] = "AND";

            arrSQLTerms[1] = new SQLTerm();
            arrSQLTerms[1]._strTableName = "Student";
            arrSQLTerms[1]._strColumnName = "gpa";
            arrSQLTerms[1]._strOperator = "=";
            arrSQLTerms[1]._objValue = 5.0;

            strarrOperators[1] = "AND";

            arrSQLTerms[2] = new SQLTerm();
            arrSQLTerms[2]._strTableName = "Student";
            arrSQLTerms[2]._strColumnName = "name";
            arrSQLTerms[2]._strOperator = "=";
            arrSQLTerms[2]._objValue = "Student1";

            strarrOperators[2] = "OR";

            assertThrows(DBAppException.class, () -> { // Select * from student where id=1 AND gpa=5.0 AND
                                                       // name="Student1" OR;
                dbApp.selectFromTable(arrSQLTerms, strarrOperators);
            });
        } finally {
            cleanUp();
        }
    }

    @Test
    public void invalidJoinException() throws DBAppException, IOException { // Joins are not allowed in our system
                                                                            // whatsoever
        try {
            DBApp dbApp = new DBApp();
            dbApp.init();

            initializeTestTable(dbApp, 20); // Initializes Student Table with Columns Id, name, GPA , with rows count n
                                            // = 20

            // select with joining (two different tables)
            SQLTerm[] arrSQLTerms = new SQLTerm[2];
            String[] strarrOperators = new String[1];

            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = 1;

            strarrOperators[0] = "AND";

            arrSQLTerms[1] = new SQLTerm();
            arrSQLTerms[1]._strTableName = "Instructor";
            arrSQLTerms[1]._strColumnName = "id";
            arrSQLTerms[1]._strOperator = "=";
            arrSQLTerms[1]._objValue = 4;

            assertThrows(DBAppException.class, () -> { // Selecting from Student and from Instructor
                dbApp.selectFromTable(arrSQLTerms, strarrOperators);
            });
        } finally {
            cleanUp();
        }
    }

    @Test
    public void ExistingTuplesExactValues() throws DBAppException, ClassNotFoundException, IOException {
        // testing the base case: Searching for a tuple that exists using exact values.

        try {

            DBApp dbApp = new DBApp();
            dbApp.init();

            initializeTestTable(dbApp, 20);

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
                Tuple tuple = (Tuple) iterator2.next();
                assert tuple.getColumnValue("id").equals(2);
                assert tuple.getColumnValue("name").equals("Student2");
                assert tuple.getColumnValue("gpa").equals(3.0 + 2);
            }

            // Test with Non-clustering key (Test 2: Double)
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "gpa";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = 9.0;

            Iterator iterator3 = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            while (iterator3.hasNext()) {
                Tuple tuple = (Tuple) iterator3.next();

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
            DBApp dbApp = new DBApp();
            dbApp.init();

            initializeTestTable(dbApp, 20);

            int count = 0;

            SQLTerm[] arrSQLTerms = new SQLTerm[1];
            String[] strarrOperators = new String[0];

            // Test on Clustering Key (Test 1)
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = "<";
            arrSQLTerms[0]._objValue = 5;

            Iterator iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // Select *from Student where id <
                                                                                     // 5;

            while (iterator.hasNext()) {
                Tuple tuple = (Tuple) iterator.next();
                assert (int) tuple.getColumnValue("id") < 5;
                count++;
            }

            assert count == 5;

            count = 0;

            // Test on Clustering Key (Test 2)
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = "<=";
            arrSQLTerms[0]._objValue = 5;

            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);// Select *from Student where id <= 5;

            while (iterator.hasNext()) {
                Tuple tuple = (Tuple) iterator.next();
                assert (int) tuple.getColumnValue("id") <= 5;
                count++;
            }

            assert count == 6;

            count = 0;

            // Test on Clustering Key (Test 3)
            arrSQLTerms = new SQLTerm[2];
            strarrOperators = new String[1];

            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = "<";
            arrSQLTerms[0]._objValue = 10;

            strarrOperators[0] = "AND";

            arrSQLTerms[1] = new SQLTerm();
            arrSQLTerms[1]._strTableName = "Student";
            arrSQLTerms[1]._strColumnName = "id";
            arrSQLTerms[1]._strOperator = ">";
            arrSQLTerms[1]._objValue = 5;

            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // Select * from Student where id < 10 AND
                                                                            // id > 5;

            while (iterator.hasNext()) {
                Tuple tuple = (Tuple) iterator.next();
                assert (int) tuple.getColumnValue("id") < 10 && (int) tuple.getColumnValue("id") > 5;
                count++;
            }

            assert count == 4;

            count = 0;

            // Test on Clustering Key (Test 4) (Same as Test 3 but change order of range)
            arrSQLTerms = new SQLTerm[2];
            strarrOperators = new String[1];

            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = ">";
            arrSQLTerms[0]._objValue = 5;

            strarrOperators[0] = "AND";

            arrSQLTerms[1] = new SQLTerm();
            arrSQLTerms[1]._strTableName = "Student";
            arrSQLTerms[1]._strColumnName = "id";
            arrSQLTerms[1]._strOperator = "<";
            arrSQLTerms[1]._objValue = 10;

            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // Select * from Student where id > 5 AND
                                                                            // id < 10;

            while (iterator.hasNext()) {
                Tuple tuple = (Tuple) iterator.next();
                assert (int) tuple.getColumnValue("id") < 10 && (int) tuple.getColumnValue("id") > 5;
                count++;
            }

            assert count == 4;

            count = 0;

            // Test on Non-Clustering Key (Test 1 : Double)
            arrSQLTerms = new SQLTerm[1];
            strarrOperators = new String[0];
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "gpa";
            arrSQLTerms[0]._strOperator = "<";
            arrSQLTerms[0]._objValue = 8.0;

            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // Select * from Student where gpa < 8.0;

            while (iterator.hasNext()) {
                Tuple tuple = (Tuple) iterator.next();
                assert (double) tuple.getColumnValue("gpa") < 8.0;
                count++;
            }

            assert count == 5; // 3.0 to 7.0

            count = 0;

            // Test on Non-Clustering Key (Test 2 : Double)
            arrSQLTerms = new SQLTerm[2];
            strarrOperators = new String[1];
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "gpa";
            arrSQLTerms[0]._strOperator = "<";
            arrSQLTerms[0]._objValue = 8.0;

            strarrOperators[0] = "OR";

            arrSQLTerms[1] = new SQLTerm();
            arrSQLTerms[1]._strTableName = "Student";
            arrSQLTerms[1]._strColumnName = "gpa";
            arrSQLTerms[1]._strOperator = ">";
            arrSQLTerms[1]._objValue = 12.0;

            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // Select * from Student where gpa < 8.0 (5)
                                                                            // OR
                                                                            // id > 12.0 (10) --> 5+10 = 15 tuples;

            while (iterator.hasNext()) {
                Tuple tuple = (Tuple) iterator.next();
                assert ((double) tuple.getColumnValue("gpa")) < 8.0 || (double) tuple.getColumnValue("gpa") > 12.0;
                count++;
            }

            assert count == 15;
            assert count == 15;

            count = 0;

            // Test on Non-Clustering Key (Test 3 : Double)
            arrSQLTerms = new SQLTerm[2];
            strarrOperators = new String[1];
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "gpa";
            arrSQLTerms[0]._strOperator = "<=";
            arrSQLTerms[0]._objValue = 8.0;

            strarrOperators[0] = "OR";

            arrSQLTerms[1] = new SQLTerm();
            arrSQLTerms[1]._strTableName = "Student";
            arrSQLTerms[1]._strColumnName = "gpa";
            arrSQLTerms[1]._strOperator = ">=";
            arrSQLTerms[1]._objValue = 12.0;

            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // Select * from Student where gpa <= 8.0
                                                                            // (6)
                                                                            // OR
                                                                            // id >= 12.0 (11) --> 6+11 = 17 tuples;

            while (iterator.hasNext()) {
                Tuple tuple = (Tuple) iterator.next();
                assert (double) tuple.getColumnValue("gpa") <= 8.0 || (double) tuple.getColumnValue("gpa") >= 12.0;
                count++;
            }

            assert count == 17;
            assert count == 17;

            count = 0;

            // Test on Non-Clustering Key (Test 4 : Double)
            arrSQLTerms = new SQLTerm[2];
            strarrOperators = new String[1];
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "gpa";
            arrSQLTerms[0]._strOperator = "<=";
            arrSQLTerms[0]._objValue = 8.0;

            strarrOperators[0] = "OR";

            arrSQLTerms[1] = new SQLTerm();
            arrSQLTerms[1]._strTableName = "Student";
            arrSQLTerms[1]._strColumnName = "gpa";
            arrSQLTerms[1]._strOperator = ">";
            arrSQLTerms[1]._objValue = 12.0;

            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // Select * from Student where gpa <= 8.0
                                                                            // (6)
                                                                            // OR
                                                                            // id > 12.0 (10) --> 6+10 = 16 tuples;

            while (iterator.hasNext()) {
                Tuple tuple = (Tuple) iterator.next();
                assert (double) tuple.getColumnValue("gpa") <= 8.0 || (double) tuple.getColumnValue("gpa") > 12.0;
                count++;
            }

            assert count == 16;
            assert count == 16;

            count = 0;

            // Test on Non-Clustering Key (Test 5 : Double)
            arrSQLTerms = new SQLTerm[2];
            strarrOperators = new String[1];
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "gpa";
            arrSQLTerms[0]._strOperator = "<";
            arrSQLTerms[0]._objValue = 8.0;

            strarrOperators[0] = "OR";

            arrSQLTerms[1] = new SQLTerm();
            arrSQLTerms[1]._strTableName = "Student";
            arrSQLTerms[1]._strColumnName = "gpa";
            arrSQLTerms[1]._strOperator = ">=";
            arrSQLTerms[1]._objValue = 12.0;

            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // Select * from Student where gpa < 8.0
                                                                            // (5)
                                                                            // OR
                                                                            // id >= 12.0 (11) --> 5+11 = 16 tuples;

            while (iterator.hasNext()) {
                Tuple tuple = (Tuple) iterator.next();
                assert (double) tuple.getColumnValue("gpa") < 8.0 || (double) tuple.getColumnValue("gpa") >= 12.0;
                count++;
            }

            assert count == 16;
            assert count == 16;

            count = 0;

            // Test on Non-Clustering Key (Test 1 : String)
            arrSQLTerms = new SQLTerm[1];
            strarrOperators = new String[0];
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "name";
            arrSQLTerms[0]._strOperator = "!=";
            arrSQLTerms[0]._objValue = "Student5";

            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // Select * from Student where name !=
                                                                            // "Student5";

            while (iterator.hasNext()) {
                Tuple tuple = (Tuple) iterator.next();
                assert !tuple.getColumnValue("name").equals("Student5");
                count++;
            }

            assert count == 19;

            count = 0;

            // Test on Non-Clustering Key (Test 2 : String)
            arrSQLTerms = new SQLTerm[1];
            strarrOperators = new String[0];
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "name";
            arrSQLTerms[0]._strOperator = "<";
            arrSQLTerms[0]._objValue = "Student5";

            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // Select * from Student where name <
                                                                            // "Student5";

            while (iterator.hasNext()) {
                Tuple tuple = (Tuple) iterator.next();
                assert ((String) tuple.getColumnValue("name")).compareTo("Student5") < 0;
                count++;
            }

            assert count == 15; // Student0-Student4(5) & Student10-Student19(10) -> (5+10=15 tuples)

            count = 0;

            // Test on Non-Clustering Key (Test 3 : String)
            arrSQLTerms = new SQLTerm[1];
            strarrOperators = new String[0];
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "name";
            arrSQLTerms[0]._strOperator = "<=";
            arrSQLTerms[0]._objValue = "Student5";

            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // Select * from Student where name <=
                                                                            // "Student5";

            while (iterator.hasNext()) {
                Tuple tuple = (Tuple) iterator.next();
                assert ((String) tuple.getColumnValue("name")).compareTo("Student5") <= 0;
                count++;
            }

            assert count == 16; // Student0-Student5(6) & Student10-Student19(10) -> (6+10=16 tuples)

            count = 0;

            // Test on Non-Clustering Key (Test 4 : String)
            arrSQLTerms = new SQLTerm[2];
            strarrOperators = new String[1];
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "name";
            arrSQLTerms[0]._strOperator = "<";
            arrSQLTerms[0]._objValue = "Student5"; // Total 15 Tuples(Student0-Student4(5) & Student10-Student19(10) ->
                                                   // (5+10=15))

            strarrOperators[0] = "OR";

            arrSQLTerms[1] = new SQLTerm();
            arrSQLTerms[1]._strTableName = "Student";
            arrSQLTerms[1]._strColumnName = "name";
            arrSQLTerms[1]._strOperator = ">";
            arrSQLTerms[1]._objValue = "Student15"; // Total 8 tuples (Student2-Student9 (8))

            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // Select * from Student where name <

            while (iterator.hasNext()) {
                Tuple tuple = (Tuple) iterator.next();
                assert ((String) tuple.getColumnValue("name")).compareTo("Student5") < 0
                        || ((String) tuple.getColumnValue("name")).compareTo("Student15") > 0;
                count++;
            }

            assert count == 20;

            count = 0;

            // Test on Non-Clustering Key (Test 5 : String)
            arrSQLTerms = new SQLTerm[2];
            strarrOperators = new String[1];
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "name";
            arrSQLTerms[0]._strOperator = "<=";
            arrSQLTerms[0]._objValue = "Student5";

            strarrOperators[0] = "OR";

            arrSQLTerms[1] = new SQLTerm();
            arrSQLTerms[1]._strTableName = "Student";
            arrSQLTerms[1]._strColumnName = "name";
            arrSQLTerms[1]._strOperator = ">";
            arrSQLTerms[1]._objValue = "Student15"; // Total 8 tuples (Student2-Student9 (8))

            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // Select * from Student where name <=
                                                                            // "Student5"(16) OR name > "Student15" (8)
                                                                            // --> 16+8=24 records;

            while (iterator.hasNext()) {
                Tuple tuple = (Tuple) iterator.next();
                assert ((String) tuple.getColumnValue("name")).compareTo("Student5") <= 0
                        || ((String) tuple.getColumnValue("name")).compareTo("Student15") > 0;
                count++;
            }

            assert count == 20;

            count = 0;

            // Test on Non-Clustering Key (Test 6 : String)
            arrSQLTerms = new SQLTerm[2];
            strarrOperators = new String[1];
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "name";
            arrSQLTerms[0]._strOperator = "<";
            arrSQLTerms[0]._objValue = "Student5"; // Total 15 Tuples(Student0-Student4(5) & Student10-Student19(10) ->
                                                   // (5+10=15))

            strarrOperators[0] = "OR";

            arrSQLTerms[1] = new SQLTerm();
            arrSQLTerms[1]._strTableName = "Student";
            arrSQLTerms[1]._strColumnName = "name";
            arrSQLTerms[1]._strOperator = ">=";
            arrSQLTerms[1]._objValue = "Student15"; // Total 19 tuples (Student1-Student19 (19))

            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // Select * from Student where name <
                                                                            // "Student5"(15) OR name >= "Student15"

            while (iterator.hasNext()) {
                Tuple tuple = (Tuple) iterator.next();
                assert ((String) tuple.getColumnValue("name")).compareTo("Student5") < 0
                        || ((String) tuple.getColumnValue("name")).compareTo("Student15") >= 0;
                count++;
            }

            assert count == 20;
            count = 0;

            // Test on Non-Clustering Key (Test 7 : String) (Same as Test 6 but change order
            // of range)
            arrSQLTerms = new SQLTerm[2];
            strarrOperators = new String[1];
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "name";
            arrSQLTerms[0]._strOperator = ">=";
            arrSQLTerms[0]._objValue = "Student15"; // Total 19 tuples (Student1-Student19 (19))

            strarrOperators[0] = "OR";

            arrSQLTerms[1] = new SQLTerm();
            arrSQLTerms[1]._strTableName = "Student";
            arrSQLTerms[1]._strColumnName = "name";
            arrSQLTerms[1]._strOperator = "<";
            arrSQLTerms[1]._objValue = "Student5";// Total 15 Tuples(Student0-Student4(5) & Student10-Student19(10) ->
                                                  // (5+10=15))

            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // Select * from Student where name >=
                                                                            // "Student15"(19) OR name < "Student5" (15)
                                                                            // --> 19+15= 34 records;

            while (iterator.hasNext()) {
                Tuple tuple = (Tuple) iterator.next();
                assert ((String) tuple.getColumnValue("name")).compareTo("Student5") < 0
                        || ((String) tuple.getColumnValue("name")).compareTo("Student15") >= 0;
                count++;
            }

            assert count == 20;

            count = 0;

        } finally {
            cleanUp();
        }
    }

    @Test
    public void NonExistantTuplesExactValues() throws DBAppException, ClassNotFoundException, IOException {
        // select tuples/values that don't exist in my table using exact values

        try {
            DBApp dbApp = new DBApp();
            dbApp.init();

            initializeTestTable(dbApp, 20);

            SQLTerm[] arrSQLTerms = new SQLTerm[1];
            String[] strarrOperators = new String[0];

            // Test on Clustering Key (Test 1)
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = 40;

            Iterator iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // select * from Student where id =
                                                                                     // 40;

            assert !iterator.hasNext();

            // Test on Clustering Key (Test 2)
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = -1;

            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // select * from Student where id = -1;

            assert !iterator.hasNext();

            // Test on Non-Clustering Key (Test 1 : String)
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "name";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = "Harridy";

            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // select * from Student where name =
                                                                            // "Harridy";

            assert !iterator.hasNext();

            // Test on Non-Clustering Key (Test 2 : String)
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "name";
            arrSQLTerms[0]._strOperator = "<";
            arrSQLTerms[0]._objValue = "Student0";

            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // select * from Student where name <
                                                                            // "Student0";

            assert !iterator.hasNext();

            // Test on Non-Clustering Key (Test 3: String)
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "name";
            arrSQLTerms[0]._strOperator = ">";
            arrSQLTerms[0]._objValue = "Student9";

            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // select * from Student where name >
                                                                            // "Student9";

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

            DBApp dbApp = new DBApp();
            dbApp.init();

            initializeTestTable(dbApp, 20);

            int count = 0;

            SQLTerm[] arrSQLTerms = new SQLTerm[1];
            String[] strarrOperators = new String[0];

            // Test on Non-Clustering Key (Test 1 : String)
            arrSQLTerms = new SQLTerm[2];
            strarrOperators = new String[1];
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "name";
            arrSQLTerms[0]._strOperator = "<";
            arrSQLTerms[0]._objValue = "Student15";

            strarrOperators[0] = "AND";

            arrSQLTerms[1] = new SQLTerm();
            arrSQLTerms[1]._strTableName = "Student";
            arrSQLTerms[1]._strColumnName = "name";
            arrSQLTerms[1]._strOperator = ">";
            arrSQLTerms[1]._objValue = "Student5";

            Iterator iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // Select * from Student where name
                                                                                     // < "Student15" AND name >
                                                                                     // "Student5" --> = 0 records;

            while (iterator.hasNext()) {
                Tuple tuple = (Tuple) iterator.next();
                assert ((String) tuple.getColumnValue("name")).compareTo("Student15") < 0
                        && ((String) tuple.getColumnValue("name")).compareTo("Student5") > 0;
                count++;
            }

            assert count == 0;

            count = 0;
        } finally {
            cleanUp();
        }
    }

    
    @Test
    // Upper case Test
    public void testOperatorCapitilzation1() throws IOException, DBAppException, ClassNotFoundException {
        boolean flag = false;
        try {
            DBApp dbApp = new DBApp();

            dbApp.init();

            initializeTestTable(dbApp, 20);

            SQLTerm[] arrSQLTerms = new SQLTerm[4];
            String[] strarrOperators = new String[3];

            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = 1;

            strarrOperators[0] = "XOR";

            arrSQLTerms[1] = new SQLTerm();
            arrSQLTerms[1]._strTableName = "Student";
            arrSQLTerms[1]._strColumnName = "id";
            arrSQLTerms[1]._strOperator = "=";
            arrSQLTerms[1]._objValue = 2;

            strarrOperators[1] = "OR";

            arrSQLTerms[2] = new SQLTerm();
            arrSQLTerms[2]._strTableName = "Student";
            arrSQLTerms[2]._strColumnName = "id";
            arrSQLTerms[2]._strOperator = "=";
            arrSQLTerms[2]._objValue = 3;

            strarrOperators[2] = "AND";

            arrSQLTerms[3] = new SQLTerm();
            arrSQLTerms[3]._strTableName = "Student";
            arrSQLTerms[3]._strColumnName = "id";
            arrSQLTerms[3]._strOperator = "=";
            arrSQLTerms[3]._objValue = 4;

            Iterator iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // Select * from Student where id =
                                                                                     // 1 XOR id = 2 OR id = 3 AND id =
                                                                                     // 4;

            flag = true;

        } finally {
            cleanUp();
        }

        assert flag;
    }

    @Test
    // Lowercase Test
    public void testOperatorCapitilzation2() throws IOException, DBAppException, ClassNotFoundException {
        boolean flag = false;
        try {
            DBApp dbApp = new DBApp();

            dbApp.init();

            initializeTestTable(dbApp, 20);

            SQLTerm[] arrSQLTerms = new SQLTerm[4];
            String[] strarrOperators = new String[3];

            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = 1;

            strarrOperators[0] = "xor";

            arrSQLTerms[1] = new SQLTerm();
            arrSQLTerms[1]._strTableName = "Student";
            arrSQLTerms[1]._strColumnName = "id";
            arrSQLTerms[1]._strOperator = "=";
            arrSQLTerms[1]._objValue = 2;

            strarrOperators[1] = "or";

            arrSQLTerms[2] = new SQLTerm();
            arrSQLTerms[2]._strTableName = "Student";
            arrSQLTerms[2]._strColumnName = "id";
            arrSQLTerms[2]._strOperator = "=";
            arrSQLTerms[2]._objValue = 3;

            strarrOperators[2] = "and";

            arrSQLTerms[3] = new SQLTerm();
            arrSQLTerms[3]._strTableName = "Student";
            arrSQLTerms[3]._strColumnName = "id";
            arrSQLTerms[3]._strOperator = "=";
            arrSQLTerms[3]._objValue = 4;

            Iterator iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // Select * from Student where id =
                                                                                     // 1 XOR id = 2 OR id = 3 AND id =
                                                                                     // 4;

            flag = true;

        } finally {
            cleanUp();
        }

        assert flag;
    }

    @Test
    public void SingleIndexedQueries() throws DBAppException, ClassNotFoundException, IOException {
        // Compare Select query results with and without index for one indexed column at
        // a time
        try {
            DBApp dbApp = new DBApp();
            dbApp.init();

            initializeTestTable(dbApp, 20);

            int indexCount = 0;
            int noIndexCount = 0;

            SQLTerm[] arrSQLTerms = new SQLTerm[1];
            String[] strarrOperators = new String[0];

            // I. Clustering Index tests
            // Test 1: exact value queries
            arrSQLTerms = new SQLTerm[1];
            strarrOperators = new String[0];
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = 5;

            Iterator iteratorWithoutIndex = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // Select * from
                                                                                                 // Student where id= 5;

            dbApp.createIndex("Student", "id", "idIndex");

            Iterator IteratorWithIndex = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            while (iteratorWithoutIndex.hasNext()) {
                Tuple tuple = (Tuple) iteratorWithoutIndex.next();
                assert tuple.getColumnValue("id").equals(5);
                noIndexCount++;
            }

            while (IteratorWithIndex.hasNext()) {
                Tuple tuple = (Tuple) IteratorWithIndex.next();
                assert tuple.getColumnValue("id").equals(5);
                indexCount++;
            }

            assert noIndexCount == indexCount;
            assert noIndexCount == 1; // if this assert passes that means the number of values is correct and that
                                      // they
                                      // are equal.

            noIndexCount = 0;
            indexCount = 0;

            cleanUp(); // remove index
            dbApp.init();

            initializeTestTable(dbApp, 20);

            // Test 2: Ranged queries 1 (Lower bound only)
            arrSQLTerms = new SQLTerm[1];
            strarrOperators = new String[0];
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = ">";
            arrSQLTerms[0]._objValue = 5;

            iteratorWithoutIndex = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // Select * from Student where
                                                                                        // id > 5;

            dbApp.createIndex("Student", "id", "idIndex");

            IteratorWithIndex = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            while (iteratorWithoutIndex.hasNext()) {
                Tuple tuple = (Tuple) iteratorWithoutIndex.next();
                assert (int) tuple.getColumnValue("id") > 5;
                noIndexCount++;
            }

            while (IteratorWithIndex.hasNext()) {
                Tuple tuple = (Tuple) IteratorWithIndex.next();
                assert (int) tuple.getColumnValue("id") > 5;
                indexCount++;
            }

            assert noIndexCount == indexCount;
            assert noIndexCount == 14; // if this assert passes that means the number of values is correct and that
                                       // they are equal.

            noIndexCount = 0;
            indexCount = 0;

            cleanUp(); // remove index
            dbApp.init();

            initializeTestTable(dbApp, 20);

            // Test 3: Ranged queries 2 (Upper & Lower bound)
            arrSQLTerms = new SQLTerm[2];
            strarrOperators = new String[1];
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = ">";
            arrSQLTerms[0]._objValue = 5;

            strarrOperators[0] = "AND";

            arrSQLTerms[1] = new SQLTerm();
            arrSQLTerms[1]._strTableName = "Student";
            arrSQLTerms[1]._strColumnName = "id";
            arrSQLTerms[1]._strOperator = "<=";
            arrSQLTerms[1]._objValue = 10;

            iteratorWithoutIndex = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // Select * from Student where
                                                                                        // id > 5 AND id<=10;

            dbApp.createIndex("Student", "id", "idIndex");

            IteratorWithIndex = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            while (iteratorWithoutIndex.hasNext()) {
                Tuple tuple = (Tuple) iteratorWithoutIndex.next();
                assert (int) tuple.getColumnValue("id") > 5 && (int) tuple.getColumnValue("id") <= 10;
                noIndexCount++;
            }

            while (IteratorWithIndex.hasNext()) {
                Tuple tuple = (Tuple) IteratorWithIndex.next();
                assert (int) tuple.getColumnValue("id") > 5 && (int) tuple.getColumnValue("id") <= 10;
                indexCount++;
            }

            assert noIndexCount == indexCount;
            assert noIndexCount == 5; // if this assert passes that means the number of values is correct and that
                                      // they are equal.

            noIndexCount = 0;
            indexCount = 0;

            cleanUp(); // remove index
            dbApp.init();
            initializeTestTable(dbApp, 20);

            // II. Non-Clustering Index tests
            // Test 1: Exact Values (double)
            arrSQLTerms = new SQLTerm[1];
            strarrOperators = new String[0];
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "gpa";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = 9.0;

            iteratorWithoutIndex = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // Select * from Student where
                                                                                        // gpa = 9.0;

            dbApp.createIndex("Student", "gpa", "gpaIndex");

            IteratorWithIndex = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            while (iteratorWithoutIndex.hasNext()) {
                Tuple tuple = (Tuple) iteratorWithoutIndex.next();
                assert (double) tuple.getColumnValue("gpa") == 9.0;
                noIndexCount++;
            }

            while (IteratorWithIndex.hasNext()) {
                Tuple tuple = (Tuple) IteratorWithIndex.next();
                assert (double) tuple.getColumnValue("gpa") == 9.0;
                indexCount++;
            }

            assert noIndexCount == indexCount;
            assert noIndexCount == 1; // if this assert passes that means the number of values is correct and that
                                      // they are equal.

            noIndexCount = 0;
            indexCount = 0;

            cleanUp(); // remove index
            dbApp.init();
            initializeTestTable(dbApp, 20);

            // Test 2: Exact Values (String)
            arrSQLTerms = new SQLTerm[1];
            strarrOperators = new String[0];
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "name";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = "Student17";

            iteratorWithoutIndex = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // Select * from Student where
                                                                                        // name = "Student17";

            dbApp.createIndex("Student", "name", "nameIndex");

            IteratorWithIndex = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            while (iteratorWithoutIndex.hasNext()) {
                Tuple tuple = (Tuple) iteratorWithoutIndex.next();
                assert tuple.getColumnValue("name").equals("Student17");
                noIndexCount++;
            }

            while (IteratorWithIndex.hasNext()) {
                Tuple tuple = (Tuple) IteratorWithIndex.next();
                assert tuple.getColumnValue("name").equals("Student17");
                indexCount++;
            }

            assert noIndexCount == indexCount;
            assert noIndexCount == 1; // if this assert passes that means the number of values is correct and that
                                      // they are equal.
            noIndexCount = 0;
            indexCount = 0;

            cleanUp(); // remove index
            dbApp.init();
            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();

            htblColNameType.put("id", "java.lang.Integer");

            htblColNameType.put("name", "java.lang.String");

            htblColNameType.put("gpa", "java.lang.Double");

            dbApp.createTable(strTableName, "name", htblColNameType);

            // insert n rows
            int n = 20;
            for (int i = 0; i < n; i++) {
                Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
                htblColNameValue.put("id", i);
                htblColNameValue.put("name", "Student" + i);
                htblColNameValue.put("gpa", 3.0 + i);
                dbApp.insertIntoTable(strTableName, htblColNameValue);
            }

            // Test 3: Exact Values (Integer) (i changed "name" to be the clust key instead
            // of "id" in this test)
            arrSQLTerms = new SQLTerm[1];
            strarrOperators = new String[0];
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = 3;

            iteratorWithoutIndex = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // Select * from Student where
                                                                                        // id = 3;

            dbApp.createIndex("Student", "id", "idIndex");

            IteratorWithIndex = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            while (iteratorWithoutIndex.hasNext()) {
                Tuple tuple = (Tuple) iteratorWithoutIndex.next();
                assert (int) tuple.getColumnValue("id") == 3;
                noIndexCount++;
            }

            while (IteratorWithIndex.hasNext()) {
                Tuple tuple = (Tuple) IteratorWithIndex.next();
                assert (int) tuple.getColumnValue("id") == 3;
                indexCount++;
            }

            assert noIndexCount == indexCount;
            assert noIndexCount == 1; // if this assert passes that means the number of values is correct and that
                                      // they are equal.

            noIndexCount = 0;
            indexCount = 0;

            cleanUp(); // remove index
            dbApp.init();
            dbApp.createTable(strTableName, "name", htblColNameType);

            // insert n rows (20)
            for (int i = 0; i < n; i++) {
                Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
                htblColNameValue.put("id", i);
                htblColNameValue.put("name", "Student" + i);
                htblColNameValue.put("gpa", 3.0 + i);
                dbApp.insertIntoTable(strTableName, htblColNameValue);
            }

            // Test 4: Ranged Values (int) ("name" is clust key instead of "id") (lower
            // bound)
            arrSQLTerms = new SQLTerm[1];
            strarrOperators = new String[0];
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = ">";
            arrSQLTerms[0]._objValue = 5;

            iteratorWithoutIndex = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // Select * from Student where
                                                                                        // id > 5;

            dbApp.createIndex("Student", "id", "idIndex");

            IteratorWithIndex = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            while (iteratorWithoutIndex.hasNext()) {
                Tuple tuple = (Tuple) iteratorWithoutIndex.next();
                assert (int) tuple.getColumnValue("id") > 5;
                noIndexCount++;
            }

            while (IteratorWithIndex.hasNext()) {
                Tuple tuple = (Tuple) IteratorWithIndex.next();
                assert (int) tuple.getColumnValue("id") > 5;
                indexCount++;
            }

            assert noIndexCount == indexCount;
            assert noIndexCount == 14; // if this assert passes that means the number of values is correct and that
                                       // they are equal.

            noIndexCount = 0;
            indexCount = 0;

            cleanUp(); // remove index
            dbApp.init();
            dbApp.createTable(strTableName, "name", htblColNameType);

            // insert n rows (20)
            for (int i = 0; i < n; i++) {
                Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
                htblColNameValue.put("id", i);
                htblColNameValue.put("name", "Student" + i);
                htblColNameValue.put("gpa", 3.0 + i);
                dbApp.insertIntoTable(strTableName, htblColNameValue);
            }

            // Test 5 Ranged Values (int) (Lower & Upper bound) ("name" is clust key)
            arrSQLTerms = new SQLTerm[2];
            strarrOperators = new String[1];
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = ">";
            arrSQLTerms[0]._objValue = 5;

            strarrOperators[0] = "AND";

            arrSQLTerms[1] = new SQLTerm();
            arrSQLTerms[1]._strTableName = "Student";
            arrSQLTerms[1]._strColumnName = "id";
            arrSQLTerms[1]._strOperator = "<=";
            arrSQLTerms[1]._objValue = 10;

            iteratorWithoutIndex = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // Select * from Student where
                                                                                        // id > 5 AND id<=10;

            dbApp.createIndex("Student", "id", "idIndex");

            IteratorWithIndex = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            while (iteratorWithoutIndex.hasNext()) {
                Tuple tuple = (Tuple) iteratorWithoutIndex.next();
                assert (int) tuple.getColumnValue("id") > 5 && (int) tuple.getColumnValue("id") <= 10;
                noIndexCount++;
            }

            while (IteratorWithIndex.hasNext()) {
                Tuple tuple = (Tuple) IteratorWithIndex.next();
                assert (int) tuple.getColumnValue("id") > 5 && (int) tuple.getColumnValue("id") <= 10;
                indexCount++;
            }

            assert noIndexCount == indexCount;
            assert noIndexCount == 5; // if this assert passes that means the number of values is correct and that
                                      // they are equal.

            noIndexCount = 0;
            indexCount = 0;

            cleanUp(); // remove index
            dbApp.init();
            initializeTestTable(dbApp, 20);

            // Test 6 Ranged Values (double) (lower bound)
            arrSQLTerms = new SQLTerm[1];
            strarrOperators = new String[0];
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "gpa";
            arrSQLTerms[0]._strOperator = ">";
            arrSQLTerms[0]._objValue = 8.0;

            iteratorWithoutIndex = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // Select * from Student where
                                                                                        // gpa > 8.0;

            dbApp.createIndex("Student", "gpa", "gpaIndex");

            IteratorWithIndex = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            while (iteratorWithoutIndex.hasNext()) {
                Tuple tuple = (Tuple) iteratorWithoutIndex.next();
                assert (double) tuple.getColumnValue("gpa") > 8.0;
                noIndexCount++;
            }

            while (IteratorWithIndex.hasNext()) {
                Tuple tuple = (Tuple) IteratorWithIndex.next();
                assert (double) tuple.getColumnValue("gpa") > 8.0;
                indexCount++;
            }

            assert noIndexCount == indexCount;
            assert noIndexCount == 14; // if this assert passes that means the number of values is correct and that
                                       // they are equal.

            noIndexCount = 0;
            indexCount = 0;

            cleanUp(); // remove index
            dbApp.init();
            initializeTestTable(dbApp, 20);

            // Test 7 : Ranged Values (double) (Lower & Upper bound)
            arrSQLTerms = new SQLTerm[2];
            strarrOperators = new String[1];
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "gpa";
            arrSQLTerms[0]._strOperator = ">";
            arrSQLTerms[0]._objValue = 3.0;

            strarrOperators[0] = "AND";

            arrSQLTerms[1] = new SQLTerm();
            arrSQLTerms[1]._strTableName = "Student";
            arrSQLTerms[1]._strColumnName = "gpa";
            arrSQLTerms[1]._strOperator = "<=";
            arrSQLTerms[1]._objValue = 8.0;

            iteratorWithoutIndex = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // Select * from Student where
                                                                                        // gpa > 3.0 AND gpa<=8.0;

            dbApp.createIndex("Student", "gpa", "gpaIndex");

            IteratorWithIndex = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            while (iteratorWithoutIndex.hasNext()) {
                Tuple tuple = (Tuple) iteratorWithoutIndex.next();
                assert (double) tuple.getColumnValue("gpa") > 3.0 && (double) tuple.getColumnValue("gpa") <= 8.0;
                noIndexCount++;
            }

            while (IteratorWithIndex.hasNext()) {
                Tuple tuple = (Tuple) IteratorWithIndex.next();
                assert (double) tuple.getColumnValue("gpa") > 3.0 && (double) tuple.getColumnValue("gpa") <= 8.0;
                indexCount++;
            }

            assert noIndexCount == indexCount;
            assert noIndexCount == 5; // if this assert passes that means the number of values is correct and that
                                      // they are equal.

            noIndexCount = 0;
            indexCount = 0;

            cleanUp(); // remove index
            dbApp.init();
            initializeTestTable(dbApp, 20);

            // Test 8 : Ranged Values (String) (Lower bound)
            arrSQLTerms = new SQLTerm[1];
            strarrOperators = new String[0];
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "name";
            arrSQLTerms[0]._strOperator = ">";
            arrSQLTerms[0]._objValue = "Student5"; // Total tuples = 4 (Student6-Student9)

            iteratorWithoutIndex = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // Select * from Student where
                                                                                        // name > "Student5";

            dbApp.createIndex("Student", "name", "nameIndex");

            IteratorWithIndex = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            while (iteratorWithoutIndex.hasNext()) {
                Tuple tuple = (Tuple) iteratorWithoutIndex.next();
                assert ((String) tuple.getColumnValue("name")).compareTo("Student5") > 0;
                noIndexCount++;
            }

            while (IteratorWithIndex.hasNext()) {
                Tuple tuple = (Tuple) IteratorWithIndex.next();
                assert ((String) tuple.getColumnValue("name")).compareTo("Student5") > 0;
                indexCount++;
            }

            assert noIndexCount == indexCount;
            assert noIndexCount == 4; // if this assert passes that means the number of values is correct and that
                                      // they are equal.

            noIndexCount = 0;
            indexCount = 0;

            cleanUp(); // remove index
            dbApp.init();
            initializeTestTable(dbApp, 20);

            // Test 9 : Ranged Values (String) (Lower & Upper bound)
            arrSQLTerms = new SQLTerm[2];
            strarrOperators = new String[1];
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "name";
            arrSQLTerms[0]._strOperator = ">";
            arrSQLTerms[0]._objValue = "Student5";

            strarrOperators[0] = "AND";

            arrSQLTerms[1] = new SQLTerm();
            arrSQLTerms[1]._strTableName = "Student";
            arrSQLTerms[1]._strColumnName = "name";
            arrSQLTerms[1]._strOperator = "<=";
            arrSQLTerms[1]._objValue = "Student9";

            iteratorWithoutIndex = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // Select * from Student where
                                                                                        // name > "Student5" AND name<=
                                                                                        // "Student10";

            dbApp.createIndex("Student", "name", "nameIndex");

            IteratorWithIndex = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            while (iteratorWithoutIndex.hasNext()) {
                Tuple tuple = (Tuple) iteratorWithoutIndex.next();
                assert ((String) tuple.getColumnValue("name")).compareTo("Student5") > 0
                        && ((String) tuple.getColumnValue("name")).compareTo("Student9") <= 0;
                noIndexCount++;
            }

            while (IteratorWithIndex.hasNext()) {
                Tuple tuple = (Tuple) IteratorWithIndex.next();
                assert ((String) tuple.getColumnValue("name")).compareTo("Student5") > 0
                        && ((String) tuple.getColumnValue("name")).compareTo("Student9") <= 0;
                indexCount++;
            }

            assert noIndexCount == indexCount;
            assert noIndexCount == 4; // if this assert passes that means the number of values is correct and that
                                      // they are equal.

        } finally {
            cleanUp();

        }
    }

    @Test
    public void MultipleIndexedQueries() throws DBAppException, ClassNotFoundException, IOException {
        // Compare Select query results with and without index for more than one indexed
        // column at
        // a time
        try {
            DBApp dbApp = new DBApp();
            dbApp.init();
            initializeTestTable(dbApp, 20);

            int indexCount = 0;
            int noIndexCount = 0;

            // Test 1: Two indexed values (Exact Value) (clust key + non-clust key)
            SQLTerm[] arrSQLTerms = new SQLTerm[2];
            String[] strarrOperators = new String[1];

            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = 5;

            strarrOperators[0] = "OR";

            arrSQLTerms[1] = new SQLTerm();
            arrSQLTerms[1]._strTableName = "Student";
            arrSQLTerms[1]._strColumnName = "gpa";
            arrSQLTerms[1]._strOperator = "=";
            arrSQLTerms[1]._objValue = 3.0;

            Iterator iteratorWithoutIndex = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // Select * from
                                                                                                 // Student where id= 5
                                                                                                 // OR
                                                                                                 // gpa = 3.0;

            dbApp.createIndex("Student", "id", "idIndex");
            dbApp.createIndex("Student", "gpa", "gpaIndex");

            Iterator IteratorWithIndex = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            while (iteratorWithoutIndex.hasNext()) {
                Tuple tuple = (Tuple) iteratorWithoutIndex.next();
                assert (int) tuple.getColumnValue("id") == 5 || (double) tuple.getColumnValue("gpa") == 3.0;
                noIndexCount++;
            }

            while (IteratorWithIndex.hasNext()) {
                Tuple tuple = (Tuple) IteratorWithIndex.next();
                assert (int) tuple.getColumnValue("id") == 5 || (double) tuple.getColumnValue("gpa") == 3.0;
                indexCount++;
            }

            assert noIndexCount == indexCount;
            assert noIndexCount == 2; // if this assert passes that means the number of values is correct and that
                                      // they are equal.

            noIndexCount = 0;
            indexCount = 0;

            cleanUp(); // remove index
            dbApp.init();
            initializeTestTable(dbApp, 20);

            // Test 2: Two indexed values (Ranged Values) (clust key + non-clust key)

            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = ">";
            arrSQLTerms[0]._objValue = 5; // gpas > 8.0

            strarrOperators[0] = "AND";

            arrSQLTerms[1] = new SQLTerm();
            arrSQLTerms[1]._strTableName = "Student";
            arrSQLTerms[1]._strColumnName = "gpa";
            arrSQLTerms[1]._strOperator = "<=";
            arrSQLTerms[1]._objValue = 12.0; // gpa 12 for student who has id=9 , total tuples=10 -> (12.0-3.0)+1 = 10

            iteratorWithoutIndex = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // Select * from Student where
                                                                                        // id= 5 AND gpa <= 12.0;

            dbApp.createIndex("Student", "id", "idIndex");
            dbApp.createIndex("Student", "gpa", "gpaIndex");

            IteratorWithIndex = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            while (iteratorWithoutIndex.hasNext()) {
                Tuple tuple = (Tuple) iteratorWithoutIndex.next();
                assert (int) tuple.getColumnValue("id") > 5 && (double) tuple.getColumnValue("gpa") <= 12.0;
                noIndexCount++;
            }

            while (IteratorWithIndex.hasNext()) {
                Tuple tuple = (Tuple) IteratorWithIndex.next();
                assert (int) tuple.getColumnValue("id") > 5 && (double) tuple.getColumnValue("gpa") <= 12.0;
                indexCount++;
            }

            assert noIndexCount == indexCount;
            assert noIndexCount == 4; // if this assert passes that means the number of values is correct and that
                                      // they are equal.

            noIndexCount = 0;
            indexCount = 0;

            cleanUp(); // remove index
            dbApp.init();
            initializeTestTable(dbApp, 20);

            // Test 3: Two indexed values (Exact Value) (non-clust + non-clust)
            arrSQLTerms = new SQLTerm[2];
            strarrOperators = new String[1];

            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "name";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = "Student5";

            strarrOperators[0] = "OR";

            arrSQLTerms[1] = new SQLTerm();
            arrSQLTerms[1]._strTableName = "Student";
            arrSQLTerms[1]._strColumnName = "gpa";
            arrSQLTerms[1]._strOperator = "=";
            arrSQLTerms[1]._objValue = 3.0;

            iteratorWithoutIndex = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // Select * from
                                                                                        // Student where name=
                                                                                        // "Student5"
                                                                                        // OR
                                                                                        // gpa = 3.0;

            dbApp.createIndex("Student", "name", "nameIndex");
            dbApp.createIndex("Student", "gpa", "gpaIndex");

            IteratorWithIndex = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            while (iteratorWithoutIndex.hasNext()) {
                Tuple tuple = (Tuple) iteratorWithoutIndex.next();
                assert tuple.getColumnValue("name").equals("Student5") || (double) tuple.getColumnValue("gpa") == 3.0;
                noIndexCount++;
            }

            while (IteratorWithIndex.hasNext()) {
                Tuple tuple = (Tuple) IteratorWithIndex.next();
                assert tuple.getColumnValue("name").equals("Student5") || (double) tuple.getColumnValue("gpa") == 3.0;
                indexCount++;
            }

            assert noIndexCount == indexCount;
            assert noIndexCount == 2; // if this assert passes that means the number of values is correct and that
                                      // they are equal.

            noIndexCount = 0;
            indexCount = 0;

            cleanUp(); // remove index
            dbApp.init();
            initializeTestTable(dbApp, 20);

            // Test 4: Two indexed values (Ranged Values) (non-clust + non-clust)
            arrSQLTerms = new SQLTerm[2];
            strarrOperators = new String[1];

            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "name";
            arrSQLTerms[0]._strOperator = ">";
            arrSQLTerms[0]._objValue = "Student5"; // from student6 to student9

            strarrOperators[0] = "AND";

            arrSQLTerms[1] = new SQLTerm();
            arrSQLTerms[1]._strTableName = "Student";
            arrSQLTerms[1]._strColumnName = "gpa";
            arrSQLTerms[1]._strOperator = "<=";
            arrSQLTerms[1]._objValue = 12.0; // from student0 up to student9 inclusive

            iteratorWithoutIndex = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // Select * from
                                                                                        // Student where name>
                                                                                        // "Student5"
                                                                                        // AND
                                                                                        // gpa <= 12.0;

            dbApp.createIndex("Student", "name", "nameIndex");
            dbApp.createIndex("Student", "gpa", "gpaIndex");

            IteratorWithIndex = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            while (iteratorWithoutIndex.hasNext()) {
                Tuple tuple = (Tuple) iteratorWithoutIndex.next();
                assert ((String) tuple.getColumnValue("name")).compareTo("Student5") > 0
                        && (double) tuple.getColumnValue("gpa") <= 12.0;
                noIndexCount++;
            }

            while (IteratorWithIndex.hasNext()) {
                Tuple tuple = (Tuple) IteratorWithIndex.next();
                assert ((String) tuple.getColumnValue("name")).compareTo("Student5") > 0
                        && (double) tuple.getColumnValue("gpa") <= 12.0;
                indexCount++;
            }

            assert noIndexCount == indexCount;
            assert noIndexCount == 4; // if this assert passes that means the number of values is correct and that
                                      // they are equal.

            noIndexCount = 0;
            indexCount = 0;

            cleanUp(); // remove index
            dbApp.init();
            initializeTestTable(dbApp, 20);

            // Test 5: Three indexed values (Exact Value) (clust key + non-clust key +
            // non-clust)
            arrSQLTerms = new SQLTerm[3];
            strarrOperators = new String[2];

            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = 5;

            strarrOperators[0] = "OR";

            arrSQLTerms[1] = new SQLTerm();
            arrSQLTerms[1]._strTableName = "Student";
            arrSQLTerms[1]._strColumnName = "gpa";
            arrSQLTerms[1]._strOperator = "=";
            arrSQLTerms[1]._objValue = 3.0;

            strarrOperators[1] = "OR";

            arrSQLTerms[2] = new SQLTerm();
            arrSQLTerms[2]._strTableName = "Student";
            arrSQLTerms[2]._strColumnName = "name";
            arrSQLTerms[2]._strOperator = "=";
            arrSQLTerms[2]._objValue = "Student10";

            iteratorWithoutIndex = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // Select * from
                                                                                        // Student where id= 5
                                                                                        // OR
                                                                                        // gpa = 3.0
                                                                                        // OR
                                                                                        // name = "Student10";

            dbApp.createIndex("Student", "id", "idIndex");
            dbApp.createIndex("Student", "gpa", "gpaIndex");
            dbApp.createIndex("Student", "name", "nameIndex");

            IteratorWithIndex = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            while (iteratorWithoutIndex.hasNext()) {
                Tuple tuple = (Tuple) iteratorWithoutIndex.next();
                assert (int) tuple.getColumnValue("id") == 5 || (double) tuple.getColumnValue("gpa") == 3.0
                        || tuple.getColumnValue("name").equals("Student10");
                noIndexCount++;
            }

            while (IteratorWithIndex.hasNext()) {
                Tuple tuple = (Tuple) IteratorWithIndex.next();
                assert (int) tuple.getColumnValue("id") == 5 || (double) tuple.getColumnValue("gpa") == 3.0
                        || tuple.getColumnValue("name").equals("Student10");
                indexCount++;
            }

            assert noIndexCount == indexCount;
            assert noIndexCount == 3; // if this assert passes that means the number of values is correct and that
                                      // they are equal.

            noIndexCount = 0;
            indexCount = 0;

            cleanUp(); // remove index
            dbApp.init();
            initializeTestTable(dbApp, 20);

            // Test 6: Three indexed values (Ranged Values) (clust key + non-clust key +
            // non-clust)

            arrSQLTerms = new SQLTerm[3];
            strarrOperators = new String[2];

            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = ">";
            arrSQLTerms[0]._objValue = 5; // from Student6 to Student19

            strarrOperators[0] = "AND";

            arrSQLTerms[1] = new SQLTerm();
            arrSQLTerms[1]._strTableName = "Student";
            arrSQLTerms[1]._strColumnName = "gpa";
            arrSQLTerms[1]._strOperator = ">";
            arrSQLTerms[1]._objValue = 9.0; // from Student7 to Student19

            strarrOperators[1] = "AND";

            arrSQLTerms[2] = new SQLTerm();
            arrSQLTerms[2]._strTableName = "Student";
            arrSQLTerms[2]._strColumnName = "name";
            arrSQLTerms[2]._strOperator = "<=";
            arrSQLTerms[2]._objValue = "Student10"; // Student0,Student1 & from Student10 to Student19

            iteratorWithoutIndex = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // Select * from
                                                                                        // Student where id> 5
                                                                                        // AND
                                                                                        // gpa > 9.0
                                                                                        // AND
                                                                                        // name <= "Student10";

            dbApp.createIndex("Student", "id", "idIndex");
            dbApp.createIndex("Student", "gpa", "gpaIndex");
            dbApp.createIndex("Student", "name", "nameIndex");

            IteratorWithIndex = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            while (iteratorWithoutIndex.hasNext()) {
                Tuple tuple = (Tuple) iteratorWithoutIndex.next();
                assert (int) tuple.getColumnValue("id") > 5 && (double) tuple.getColumnValue("gpa") > 9.0
                        && ((String) tuple.getColumnValue("name")).compareTo("Student10") <= 0;
                noIndexCount++;
            }

            while (IteratorWithIndex.hasNext()) {
                Tuple tuple = (Tuple) IteratorWithIndex.next();
                assert (int) tuple.getColumnValue("id") > 5 && (double) tuple.getColumnValue("gpa") > 9.0
                        && ((String) tuple.getColumnValue("name")).compareTo("Student10") <= 0;
                indexCount++;
            }

            assert noIndexCount == indexCount;
            assert noIndexCount == 1; // if this assert passes that means the number of values is correct and that
                                      // they are equal. Final Tuple range after ANDing -> Student10 to Student19

        } finally {
            cleanUp();
        }
    }

    @Test
    // TODO generate more XOR edge cases
    public void xorTests() throws DBAppException, IOException {
        try {
            DBApp dbApp = new DBApp();
            dbApp.init();

            initializeTestTable(dbApp, 20);

            SQLTerm[] arrSQLTerms = new SQLTerm[2];
            String[] strarrOperators = new String[1];
            int count = 0;

            // Test 1: Exact Value Query (no common values, acts as normal OR)
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = 5;

            strarrOperators[0] = "XOR";

            arrSQLTerms[1] = new SQLTerm();
            arrSQLTerms[1]._strTableName = "Student";
            arrSQLTerms[1]._strColumnName = "id";
            arrSQLTerms[1]._strOperator = "=";
            arrSQLTerms[1]._objValue = 3;

            // select * from Student where id = 5 XOR id = 3
            Iterator iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            while (iterator.hasNext()) {
                Tuple tuple = (Tuple) iterator.next();
                assert (int) tuple.getColumnValue("id") == 5 || (int) tuple.getColumnValue("id") == 3;
                count++;
            }

            assert count == 2;

            count = 0;

            // Test 2: Exact Value Query (common values, should return nothing)
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = 5;

            strarrOperators[0] = "XOR";

            arrSQLTerms[1] = new SQLTerm();
            arrSQLTerms[1]._strTableName = "Student";
            arrSQLTerms[1]._strColumnName = "id";
            arrSQLTerms[1]._strOperator = "=";
            arrSQLTerms[1]._objValue = 5;

            // select * from Student where id = 5 XOR id = 5
            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            assert !iterator.hasNext();

            // Test 3: Ranged Query (common values, should return nothing)
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = ">";
            arrSQLTerms[0]._objValue = 8; // id>8 , 9-19

            strarrOperators[0] = "XOR";

            arrSQLTerms[1] = new SQLTerm();
            arrSQLTerms[1]._strTableName = "Student";
            arrSQLTerms[1]._strColumnName = "id";
            arrSQLTerms[1]._strOperator = ">";
            arrSQLTerms[1]._objValue = 5; // id > 5 , 6-19

            // select * from Student where id > 8 XOR id > 5 (res should be 6-8)
            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            while (iterator.hasNext()) {
                Tuple tuple = (Tuple) iterator.next();
                assert tuple.getColumnValue("id").equals(6) || tuple.getColumnValue("id").equals(7)
                        || tuple.getColumnValue("id").equals(8);
                count++;
            }

            assert count == 3;

        } finally {
            cleanUp();
        }

    }

    @Test
    public void outOfRange() throws IOException, DBAppException {
        try {
            DBApp dbApp = new DBApp();
            dbApp.init();

            initializeTestTable(dbApp, 60);

            SQLTerm[] arrSQLTerms = new SQLTerm[1];
            String[] strarrOperators = new String[0];

            // I. Test Values Bigger than Max of Table
            // Test 1: Exact value out of range (clust key / int)

            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = 200;

            Iterator iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            assert !iterator.hasNext();

            // Test 2: Exact value out of range (non-clust key / double)
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "gpa";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = 120.0; // max gpa = 62.0

            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            assert !iterator.hasNext();

            // Test 3: Exact value out of range (non-clust key / string)
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "name";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = "Student156";

            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            assert !iterator.hasNext();

            // Test 4: Ranged value out of range (clust key / int)
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = ">";
            arrSQLTerms[0]._objValue = 200;

            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            assert !iterator.hasNext();

            // Test 5: Ranged value out of range (clust key / int) (query exactly bigger
            // than biggest value)
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = ">";
            arrSQLTerms[0]._objValue = 59;

            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            assert !iterator.hasNext();

            // Test 6: Ranged value out of range (non-clust key / double)
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "gpa";
            arrSQLTerms[0]._strOperator = ">";
            arrSQLTerms[0]._objValue = 120.0; // max gpa = 62.0

            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            assert !iterator.hasNext();

            // Test 7: Ranged value out of range (non-clust key / double) (query exactly
            // bigger
            // than biggest value)
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "gpa";
            arrSQLTerms[0]._strOperator = ">";
            arrSQLTerms[0]._objValue = 62.0; // max gpa = 62.0

            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            assert !iterator.hasNext();

            // Test 8: Ranged value out of range (non-clust key / string)
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "name";
            arrSQLTerms[0]._strOperator = ">";
            arrSQLTerms[0]._objValue = "Zebra";

            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            assert !iterator.hasNext();

        } finally {
            cleanUp();
        }

    }

    @Test
    public void ExactValueEdgeCaseValues() throws IOException, DBAppException {
        // Testing Queries that yield results on the edge of the Table / Page.
        try {
            DBApp dbApp = new DBApp();
            dbApp.init();

            initializeTestTable(dbApp, 60);

            SQLTerm[] arrSQLTerms = new SQLTerm[1];
            String[] strarrOperators = new String[0];

            int count = 0;

            // Test 1: Exact Value Query (Upper bound) (clust key / int)
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = 59;

            Iterator iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            while (iterator.hasNext()) {
                Tuple tuple = (Tuple) iterator.next();
                assert (int) tuple.getColumnValue("id") == 59;
                count++;
            }

            assert count == 1;

            count = 0;

            // Test 2: Exact Value Query (Lower bound) (clust key / int)
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = 0;

            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            while (iterator.hasNext()) {
                Tuple tuple = (Tuple) iterator.next();
                assert (int) tuple.getColumnValue("id") == 0;
                count++;
            }

            assert count == 1;

            count = 0;

            // Test 3: Exact Value Query (Upper bound) (non-clust key / double)
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "gpa";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = 59.0;

            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            while (iterator.hasNext()) {
                Tuple tuple = (Tuple) iterator.next();
                assert (double) tuple.getColumnValue("gpa") == 59.0;
                count++;
            }

            assert count == 1;

            count = 0;

            // Test 4: Exact Value Query (Lower bound) (non-clust key / double)
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "gpa";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = 3.0;

            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            while (iterator.hasNext()) {
                Tuple tuple = (Tuple) iterator.next();
                assert (double) tuple.getColumnValue("gpa") == 3.0;
                count++;
            }

            assert count == 1;

            count = 0;

            // Test 5: Exact Value Query (Upper bound) (non-clust key / string)
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "name";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = "Student59";

            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            while (iterator.hasNext()) {
                Tuple tuple = (Tuple) iterator.next();
                assert tuple.getColumnValue("name").equals("Student59");
                count++;
            }

            assert count == 1;

            count = 0;

            // Test 6: Exact Value Query (Lower bound) (non-clust key / string)
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "name";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = "Student0";

            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            while (iterator.hasNext()) {
                Tuple tuple = (Tuple) iterator.next();
                assert tuple.getColumnValue("name").equals("Student0");
                count++;
            }

            assert count == 1;

            count = 0;

        } finally {
            cleanUp();
        }
    }

    @Test
    public void rangedValuesEdgeCaseValues() throws IOException, DBAppException {
        // Testing Queries that yield results on the edge of the Table / Page.
        try {
            DBApp dbApp = new DBApp();
            dbApp.init();

            initializeTestTable(dbApp, 60);

            SQLTerm[] arrSQLTerms = new SQLTerm[1];
            String[] strarrOperators = new String[0];

            int count = 0;

            // Test 1: Clust Key/ Int (Lower bound)
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = "<=";
            arrSQLTerms[0]._objValue = 0;

            Iterator iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            while (iterator.hasNext()) {
                Tuple t = (Tuple) iterator.next();
                assert t.getColumnValue("id").equals(0);
                count++;
            }

            assert count == 1;

            count = 0;

            // Test 2: Clust Key/ Int (upper bound)
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = ">=";
            arrSQLTerms[0]._objValue = 59;

            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            while (iterator.hasNext()) {
                Tuple t = (Tuple) iterator.next();
                assert t.getColumnValue("id").equals(59);
                count++;
            }

            assert count == 1;

            count = 0;

            // Test 3: Non-Clust Key/ Double (Lower bound)
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "gpa";
            arrSQLTerms[0]._strOperator = "<=";
            arrSQLTerms[0]._objValue = 3.0;

            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            while (iterator.hasNext()) {
                Tuple t = (Tuple) iterator.next();
                assert t.getColumnValue("gpa").equals(3.0);
                count++;
            }

            assert count == 1;

            count = 0;

            // Test 4: Non-Clust Key/ Double (Upper bound)
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "gpa";
            arrSQLTerms[0]._strOperator = ">=";
            arrSQLTerms[0]._objValue = 62.0;

            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            while (iterator.hasNext()) {
                Tuple t = (Tuple) iterator.next();
                assert t.getColumnValue("gpa").equals(62.0);
                count++;
            }

            assert count == 1;

            count = 0;

            // Test 5: Non-Clust Key/String (Lower bound)
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "name";
            arrSQLTerms[0]._strOperator = "<";
            arrSQLTerms[0]._objValue = "Student1";

            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            while (iterator.hasNext()) {
                Tuple t = (Tuple) iterator.next();
                assert t.getColumnValue("name").equals("Student0");
                count++;
            }

            assert count == 1;

            count = 0;

            // Test 5: Non-Clust Key/String (Upper bound)
            arrSQLTerms = new SQLTerm[2];
            strarrOperators = new String[1];
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "name";
            arrSQLTerms[0]._strOperator = ">";
            arrSQLTerms[0]._objValue = "Student58";

            strarrOperators[0] = "AND";

            arrSQLTerms[1] = new SQLTerm();
            arrSQLTerms[1]._strTableName = "Student";
            arrSQLTerms[1]._strColumnName = "name";
            arrSQLTerms[1]._strOperator = "<";
            arrSQLTerms[1]._objValue = "Student60";
            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            while (iterator.hasNext()) {
                Tuple t = (Tuple) iterator.next();
                if (t.getColumnValue("name").equals("Student6")) {
                    count--;
                }
                count++;
            }

            assert count == 1;

            count = 0;

        } finally {
            cleanUp();
        }

    }

    @Test
    public void noWhereCondition() throws IOException, DBAppException {
        try {
            DBApp dbApp = new DBApp();
            dbApp.init();

            initializeTestTable(dbApp, 20);

            SQLTerm[] arrSQLTerms = new SQLTerm[1];
            String[] strarrOperators = new String[0];

            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";

            Iterator i = dbApp.selectFromTable(arrSQLTerms, strarrOperators);

            int count = 0;
            while (i.hasNext()) {
                Tuple t = (Tuple) i.next();
                assert (int) t.getColumnValue("id") >= 0 && (int) t.getColumnValue("id") <= 19;
                count++;
            }

            assert count == 20;

        } finally {
            cleanUp();
        }

    }

    // @Test
    // public void IndexedQueryTimeClustKey() throws IOException, DBAppException {
    //     try {
    //         cleanUp();
    //         DBApp dbApp = new DBApp();
    //         dbApp.init();

    //         initializeTestTable(dbApp, 1000);

    //         SQLTerm[] arrSQLTerms = new SQLTerm[2];
    //         String[] strarrOperators = new String[1];

    //         // Test 1: Exact Value Query
    //         arrSQLTerms[0] = new SQLTerm();
    //         arrSQLTerms[0]._strTableName = "Student";
    //         arrSQLTerms[0]._strColumnName = "id";
    //         arrSQLTerms[0]._strOperator = "=";
    //         arrSQLTerms[0]._objValue = 5;

    //         strarrOperators[0] = "AND";

    //         arrSQLTerms[1] = new SQLTerm();
    //         arrSQLTerms[1]._strTableName = "Student";
    //         arrSQLTerms[1]._strColumnName = "id";
    //         arrSQLTerms[1]._strOperator = "=";
    //         arrSQLTerms[1]._objValue = 3;

    //         long startTime = System.currentTimeMillis();
    //         Iterator iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);
    //         long endTime = System.currentTimeMillis();

    //         long nonIndexDuration = endTime - startTime;

    //         dbApp.createIndex("Student", "id", "idIndex");

    //         startTime = System.currentTimeMillis();
    //         iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);
    //         endTime = System.currentTimeMillis();

    //         long indexedDuration = endTime - startTime;

    //         assert indexedDuration >= nonIndexDuration;

    //         cleanUp(); // remove index
    //         dbApp.init();
    //         initializeTestTable(dbApp, 1000);

    //         // Test 2: Ranged Query
    //         arrSQLTerms[0] = new SQLTerm();
    //         arrSQLTerms[0]._strTableName = "Student";
    //         arrSQLTerms[0]._strColumnName = "id";
    //         arrSQLTerms[0]._strOperator = "<=";
    //         arrSQLTerms[0]._objValue = 17;

    //         strarrOperators[0] = "AND";

    //         arrSQLTerms[1] = new SQLTerm();
    //         arrSQLTerms[1]._strTableName = "Student";
    //         arrSQLTerms[1]._strColumnName = "id";
    //         arrSQLTerms[1]._strOperator = ">=";
    //         arrSQLTerms[1]._objValue = 3;

    //         startTime = System.currentTimeMillis();
    //         iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);
    //         endTime = System.currentTimeMillis();

    //         nonIndexDuration = endTime - startTime;

    //         dbApp.createIndex("Student", "id", "idIndex");

    //         startTime = System.currentTimeMillis();
    //         iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);
    //         endTime = System.currentTimeMillis();

    //         indexedDuration = endTime - startTime;

    //         assert indexedDuration < nonIndexDuration;

    //     } finally {
    //         cleanUp();
    //     }
    // }

    // @Test // sometimes fails if range is too big (max tuples per page = 20)
    // public void IndexedQueryNonTimeClustKey() throws IOException, DBAppException {
    //     try {
    //         DBApp dbApp = new DBApp();
    //         dbApp.init();

    //         initializeTestTable(dbApp, 1000);

    //         SQLTerm[] arrSQLTerms = new SQLTerm[2];
    //         String[] strarrOperators = new String[1];

    //         // Test 1: Exact Value Query
    //         arrSQLTerms[0] = new SQLTerm();
    //         arrSQLTerms[0]._strTableName = "Student";
    //         arrSQLTerms[0]._strColumnName = "gpa";
    //         arrSQLTerms[0]._strOperator = "=";
    //         arrSQLTerms[0]._objValue = 3.0;

    //         strarrOperators[0] = "AND";

    //         arrSQLTerms[1] = new SQLTerm();
    //         arrSQLTerms[1]._strTableName = "Student";
    //         arrSQLTerms[1]._strColumnName = "name";
    //         arrSQLTerms[1]._strOperator = "=";
    //         arrSQLTerms[1]._objValue = "Student0";

    //         long startTime = System.currentTimeMillis();
    //         Iterator iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);
    //         long endTime = System.currentTimeMillis();

    //         long nonIndexDuration = endTime - startTime;

    //         dbApp.createIndex("Student", "gpa", "gpaIndex");

    //         startTime = System.currentTimeMillis();
    //         iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);
    //         endTime = System.currentTimeMillis();

    //         long indexedDuration = endTime - startTime;

    //         assert indexedDuration < nonIndexDuration;

    //         cleanUp(); // remove index
    //         dbApp.init();
    //         initializeTestTable(dbApp, 1000);

    //         // Test 2: Ranged Query
    //         arrSQLTerms[0] = new SQLTerm();
    //         arrSQLTerms[0]._strTableName = "Student";
    //         arrSQLTerms[0]._strColumnName = "gpa";
    //         arrSQLTerms[0]._strOperator = "<=";
    //         arrSQLTerms[0]._objValue = 400.0;

    //         strarrOperators[0] = "AND";

    //         arrSQLTerms[1] = new SQLTerm();
    //         arrSQLTerms[1]._strTableName = "Student";
    //         arrSQLTerms[1]._strColumnName = "gpa";
    //         arrSQLTerms[1]._strOperator = ">=";
    //         arrSQLTerms[1]._objValue = 8.0;

    //         startTime = System.currentTimeMillis();
    //         iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);
    //         endTime = System.currentTimeMillis();

    //         nonIndexDuration = endTime - startTime;

    //         dbApp.createIndex("Student", "gpa", "gpaIndex");

    //         startTime = System.currentTimeMillis();
    //         iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);
    //         endTime = System.currentTimeMillis();

    //         indexedDuration = endTime - startTime;

    //         assert indexedDuration <= nonIndexDuration;
    //         assert indexedDuration < nonIndexDuration;

    //     } finally {
    //         cleanUp();
    //     }
    // }

    @Test
    public void testPrecedence() throws DBAppException, IOException, ClassNotFoundException {
        try {
            DBApp dbApp = new DBApp();

            dbApp.init();

            initializeTestTable(dbApp, 20);

            SQLTerm[] arrSQLTerms = new SQLTerm[3];
            String[] strarrOperators = new String[2];

            // Testing OR + AND
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "name";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = "Student2";

            strarrOperators[0] = "or";

            arrSQLTerms[1] = new SQLTerm();
            arrSQLTerms[1]._strTableName = "Student";
            arrSQLTerms[1]._strColumnName = "name";
            arrSQLTerms[1]._strOperator = "=";
            arrSQLTerms[1]._objValue = "Student1";

            strarrOperators[1] = "and";

            arrSQLTerms[2] = new SQLTerm();
            arrSQLTerms[2]._strTableName = "Student";
            arrSQLTerms[2]._strColumnName = "name";
            arrSQLTerms[2]._strOperator = "=";
            arrSQLTerms[2]._objValue = "Student1";

            Iterator iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // Select * from Student where name
                                                                                     // = "Student2" OR name =
                                                                                     // "Student1"
                                                                                     // AND name = "Student1";
            int count = 0;

            while (iterator.hasNext()) {
                Tuple tuple = (Tuple) iterator.next();
                assert tuple.getColumnValue("name").equals("Student1")
                        || tuple.getColumnValue("name").equals("Student2");
                count++;
            }

            assert count == 2;

            count = 0;

            // Testing XOR+AND
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "name";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = "Student1";

            strarrOperators[0] = "xor";

            arrSQLTerms[1] = new SQLTerm();
            arrSQLTerms[1]._strTableName = "Student";
            arrSQLTerms[1]._strColumnName = "id";
            arrSQLTerms[1]._strOperator = "=";
            arrSQLTerms[1]._objValue = 2;

            strarrOperators[1] = "and";

            arrSQLTerms[2] = new SQLTerm();
            arrSQLTerms[2]._strTableName = "Student";
            arrSQLTerms[2]._strColumnName = "name";
            arrSQLTerms[2]._strOperator = "=";
            arrSQLTerms[2]._objValue = "Student2";

            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // Select * from Student where name =
                                                                            // "Student1" XOR id = 2 AND name =
                                                                            // "Student2";

            while (iterator.hasNext()) {
                Tuple tuple = (Tuple) iterator.next();
                assert tuple.getColumnValue("name").equals("Student1") || (int) tuple.getColumnValue("id") == 2;
                count++;
            }

            assert count == 2;

            count = 0;

            // Testing XOR+OR
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "name";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = "Student2";

            strarrOperators[0] = "xor";

            arrSQLTerms[1] = new SQLTerm();
            arrSQLTerms[1]._strTableName = "Student";
            arrSQLTerms[1]._strColumnName = "id";
            arrSQLTerms[1]._strOperator = "=";
            arrSQLTerms[1]._objValue = 2;

            strarrOperators[1] = "or";

            arrSQLTerms[2] = new SQLTerm();
            arrSQLTerms[2]._strTableName = "Student";
            arrSQLTerms[2]._strColumnName = "id";
            arrSQLTerms[2]._strOperator = "=";
            arrSQLTerms[2]._objValue = 3;

            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // Select * from Student where name =
                                                                            // "Student1" XOR id = 2 OR id = 3;

            while (iterator.hasNext()) {
                Tuple tuple = (Tuple) iterator.next();
                assert tuple.getColumnValue("name").equals("Student3") && (int) tuple.getColumnValue("id") == 3;
                count++;
            }

            assert count == 1;

            count = 0;

            // Testing XOR+OR+AND (clust key)
            arrSQLTerms = new SQLTerm[4];
            strarrOperators = new String[3];
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = 1;

            strarrOperators[0] = "xor";

            arrSQLTerms[1] = new SQLTerm();
            arrSQLTerms[1]._strTableName = "Student";
            arrSQLTerms[1]._strColumnName = "id";
            arrSQLTerms[1]._strOperator = "=";
            arrSQLTerms[1]._objValue = 2;

            strarrOperators[1] = "or";

            arrSQLTerms[2] = new SQLTerm();
            arrSQLTerms[2]._strTableName = "Student";
            arrSQLTerms[2]._strColumnName = "id";
            arrSQLTerms[2]._strOperator = "=";
            arrSQLTerms[2]._objValue = 3;

            strarrOperators[2] = "and";

            arrSQLTerms[3] = new SQLTerm();
            arrSQLTerms[3]._strTableName = "Student";
            arrSQLTerms[3]._strColumnName = "id";
            arrSQLTerms[3]._strOperator = "=";
            arrSQLTerms[3]._objValue = 4;

            // Final Correct Expression: (id = 1 XOR (id = 2 OR (id = 3 AND id = 4)))
            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // Select * from Student where id =
                                                                            // 1 XOR id = 2 OR id = 3 AND id =
                                                                            // 4;
            while (iterator.hasNext()) {
                Tuple tuple = (Tuple) iterator.next();
                assert (int) tuple.getColumnValue("id") == 1
                        || (int) tuple.getColumnValue("id") == 2;
                count++;
            }

            assert count == 2;

            count = 0;

            // Testing XOR+OR+AND (non-clust key)
            arrSQLTerms = new SQLTerm[4];
            strarrOperators = new String[3];
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "gpa";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = 4.0; // id = 1

            strarrOperators[0] = "xor";

            arrSQLTerms[1] = new SQLTerm();
            arrSQLTerms[1]._strTableName = "Student";
            arrSQLTerms[1]._strColumnName = "gpa";
            arrSQLTerms[1]._strOperator = "=";
            arrSQLTerms[1]._objValue = 5.0; // id=2

            strarrOperators[1] = "or";

            arrSQLTerms[2] = new SQLTerm();
            arrSQLTerms[2]._strTableName = "Student";
            arrSQLTerms[2]._strColumnName = "gpa";
            arrSQLTerms[2]._strOperator = "=";
            arrSQLTerms[2]._objValue = 6.0; // id=3

            strarrOperators[2] = "and";

            arrSQLTerms[3] = new SQLTerm();
            arrSQLTerms[3]._strTableName = "Student";
            arrSQLTerms[3]._strColumnName = "gpa";
            arrSQLTerms[3]._strOperator = "=";
            arrSQLTerms[3]._objValue = 7.0; // id=4

            // Final Correct Expression: (gpa = 4.0 XOR (gpa = 5.0 OR (gpa = 6.0 AND gpa =
            // 7.0)))
            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // Select * from Student where gpa =
                                                                            // 4.0 XOR gpa = 5.0 OR id = 6.0 AND id =
                                                                            // 7.0;
            while (iterator.hasNext()) {
                Tuple tuple = (Tuple) iterator.next();
                assert (int) tuple.getColumnValue("id") == 1
                        || (int) tuple.getColumnValue("id") == 2;
                count++;
            }

            assert count == 2;

            count = 0;

            // Testing AND+OR+XOR (clust key)
            arrSQLTerms = new SQLTerm[4];
            strarrOperators = new String[3];
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = 1;

            strarrOperators[0] = "AND";

            arrSQLTerms[1] = new SQLTerm();
            arrSQLTerms[1]._strTableName = "Student";
            arrSQLTerms[1]._strColumnName = "id";
            arrSQLTerms[1]._strOperator = "=";
            arrSQLTerms[1]._objValue = 2;

            strarrOperators[1] = "OR";

            arrSQLTerms[2] = new SQLTerm();
            arrSQLTerms[2]._strTableName = "Student";
            arrSQLTerms[2]._strColumnName = "id";
            arrSQLTerms[2]._strOperator = "=";
            arrSQLTerms[2]._objValue = 3;

            strarrOperators[2] = "XOR";

            arrSQLTerms[3] = new SQLTerm();
            arrSQLTerms[3]._strTableName = "Student";
            arrSQLTerms[3]._strColumnName = "id";
            arrSQLTerms[3]._strOperator = "=";
            arrSQLTerms[3]._objValue = 4;

            // Final Correct Expression: (((id = 1 AND id = 2) OR id = 3)) XOR id = 4)))
            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // Select * from Student where id =
                                                                            // 1 and id = 2 OR id = 3 xor id =
                                                                            // 4;
            while (iterator.hasNext()) {
                Tuple tuple = (Tuple) iterator.next();
                assert (int) tuple.getColumnValue("id") == 3
                        || (int) tuple.getColumnValue("id") == 4;
                count++;
            }

            assert count == 2;

            count = 0;

            // Testing AND+OR+XOR (non-clust key)
            arrSQLTerms = new SQLTerm[4];
            strarrOperators = new String[3];
            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "gpa";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = 4.0;

            strarrOperators[0] = "AND";

            arrSQLTerms[1] = new SQLTerm();
            arrSQLTerms[1]._strTableName = "Student";
            arrSQLTerms[1]._strColumnName = "gpa";
            arrSQLTerms[1]._strOperator = "=";
            arrSQLTerms[1]._objValue = 5.0;

            strarrOperators[1] = "OR";

            arrSQLTerms[2] = new SQLTerm();
            arrSQLTerms[2]._strTableName = "Student";
            arrSQLTerms[2]._strColumnName = "gpa";
            arrSQLTerms[2]._strOperator = "=";
            arrSQLTerms[2]._objValue = 6.0;

            strarrOperators[2] = "XOR";

            arrSQLTerms[3] = new SQLTerm();
            arrSQLTerms[3]._strTableName = "Student";
            arrSQLTerms[3]._strColumnName = "gpa";
            arrSQLTerms[3]._strOperator = "=";
            arrSQLTerms[3]._objValue = 7.0;

            // Final Correct Expression: (((id = 1 AND id = 2) OR id = 3)) XOR id = 4)))
            iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // Select * from Student where id =
                                                                            // 1 and id = 2 OR id = 3 xor id =
                                                                            // 4;
            while (iterator.hasNext()) {
                Tuple tuple = (Tuple) iterator.next();
                assert (int) tuple.getColumnValue("id") == 3
                        || (int) tuple.getColumnValue("id") == 4;
                count++;
            }

            assert count == 2;

        } finally {
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

    private void initializeTestTable(DBApp dbApp, int n) {
        try {
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
