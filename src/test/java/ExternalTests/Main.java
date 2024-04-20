package testing;

import DBMain.DBAppException;

public class Main {
    public static void main(String[] args) throws DBAppException, TestException {
        System.out.println("Test 1 is Starting!");
        EvaluateTest evaluateTest = new EvaluateTest();
        evaluateTest.EvaluateTest1();
    }
}
