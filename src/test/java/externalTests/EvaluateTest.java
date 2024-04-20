package externalTests;

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

import java.util.Vector;

public class EvaluateTest {
    public void EvaluateTest1() throws TestException, DBAppException {
        DBApp dbApp = new DBApp();
        GenerateTests generateTests = new GenerateTests(dbApp);
        Vector<Integer> chosenIds = generateTests.InitializeTest1(); // Check for Table Insertions : A. Check Clustered
                                                                     // , B. Check Existence and Index Creation
        Table testingTable;
        try {
            testingTable = Table.deserialize("tables/" + "students/" + "students.class");
        } catch (Exception e) {
            throw new TestException("Table students not found/created");
        }
        if (chosenIds.size() != 500) {
            throw new TestException("Not all tuples were inserted, first check!");
        }
        Integer prevId = -1;
        for (String pageName : testingTable.getPageVector()) {
            Page page = Page.deserialize("tables/" + pageName + "/" + pageName + ".class");
            for (Tuple tuple : page.getTuples()) {
                chosenIds.remove(tuple.getColumnValue("id"));
                if (prevId.compareTo((Integer) tuple.getColumnValue("id")) > 0) {
                    System.out.println(testingTable);
                    throw new TestException("Tuples are not sorted");
                }
                prevId = (Integer) tuple.getColumnValue("id");
            }
        }
        if (chosenIds.size() != 0) {
            System.out.println(testingTable);
            throw new TestException("Not all tuples were inserted and a tuple could be duplicated , 2nd Check!");
        }

        System.out.println("All Insertion Tests Passed Successfully");

        generateTests.deletionsAgeTest1();
        for (String pageName : testingTable.getPageVector()) {
            Page page = Page.deserialize("tables/" + pageName + "/" + pageName + ".class");
            for (Tuple tuple : page.getTuples()) {
                if ((Integer) tuple.getColumnValue("age") == 27) {
                    throw new TestException("Age Table Deletion failed");
                }
            }
        }

        System.out.println("All Deletion Tests without Index Passed Successfully");

        generateTests.deletionsGpaTest1();
        for (String pageName : testingTable.getPageVector()) {
            Page page = Page.deserialize("tables/" + pageName + "/" + pageName + ".class");
            for (Tuple tuple : page.getTuples()) {
                if ((Double) tuple.getColumnValue("gpa") == 0.7) {
                    throw new TestException("Gpa Table Deletion failed");
                }
            }
        }

        System.out.println("All Deletion Tests with Index Passed Successfully");
        BPlusTree gpaIndex = BPlusTree
                .deserialize("tables/" + "studentsGpaIndex" + "/" + "studentsGpaIndex" + ".class");
        Vector<String> values = gpaIndex.query(0.7);
        if (values != null) {
            System.out.println(values);
            throw new TestException("Gpa Index Deletion failed , Not Null or Empty");
        }

        generateTests.updateTest1();
        for (String pageName : testingTable.getPageNames()) {
            Page page = (Page) deserialize(pageName);
            for (Tuple tuple : page.getTuples()) {
                if ((Integer) tuple.getValue("id") <= 50) {
                    if ((Double) tuple.getValue("gpa") != 0.5) {
                        throw new TestException("Update Test 1 failed");
                    }
                }
            }
        }
        generateTests.selectOnlyoneconditon();
        System.out.println("All Tests 1 Passed Successfully");

    }
}
