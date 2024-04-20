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

public class Main {
    public static void main(String[] args) throws DBAppException, TestException {
        System.out.println("Test 1 is Starting!");
        EvaluateTest evaluateTest = new EvaluateTest();
        evaluateTest.EvaluateTest1();
    }
}
