package testing;

import DBMain.DBApp;
import DBMain.DBAppException;
import DBMain.SQLTerm;
import Structures.Tuple;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.Random;

public class GenerateTests {

    public DBApp dbApp;

    public GenerateTests(DBApp dbApp) {
        this.dbApp = dbApp;
    }

    public Vector<Integer> InitializeTest1() throws DBAppException {

        Hashtable<String,String> htblColNameType = new Hashtable<>();
        htblColNameType.put("id","java.lang.Integer");
        htblColNameType.put("name","java.lang.String");
        htblColNameType.put("gpa","java.lang.Double");
        htblColNameType.put("age","java.lang.Integer");
        dbApp.createTable("students","id" , htblColNameType);

        Vector<Integer> ids = new Vector<>();
        Vector<Integer> chosenIds = new Vector<>();
        Vector<String> names = new Vector<>();
        names.add("Ahmed");
        names.add("Mohamed");
        names.add("Ali");
        names.add("Omar");
        names.add("Amr");
        names.add("Khaled");
        names.add("Mahmoud");
        names.add("Hassan");
        names.add("Hussein");
        names.add("Othman");

        for (int i = 0; i < 1000; i++){
            ids.add(i);
        }

        Random rm = new Random();

        for (int i = 0; i < 500 ; i++){
            Hashtable<String,Object> htblColNameValue = new Hashtable<>();
            int randomIndex = rm.nextInt(ids.size());
            int id = ids.remove(randomIndex);
            String name = names.get(rm.nextInt(names.size()));
            double gpa = 0.7 + (5.0 - 0.7) * rm.nextDouble();
            int age = rm.nextInt(10) + 17;
            htblColNameValue.put("id",id);
            htblColNameValue.put("name",name);
            htblColNameValue.put("gpa",gpa);
            htblColNameValue.put("age",age);
            dbApp.insertIntoTable("students",htblColNameValue);
            chosenIds.add(id);
        }

        dbApp.createIndex("students","gpa","studentsGpaIndex");


        return chosenIds;
    }

    public void deletionsGpaTest1() throws DBAppException {
        Hashtable<String,Object> htblColNameValue = new Hashtable<>();
        htblColNameValue.put("gpa",0.7);
        dbApp.deleteFromTable("students",htblColNameValue);
    }

    public void deletionsAgeTest1() throws DBAppException {
        Hashtable<String,Object> htblColNameValue = new Hashtable<>();
        htblColNameValue.put("age",27);
        dbApp.deleteFromTable("students",htblColNameValue);
    }

    public void updateTest1() throws DBAppException {
        Hashtable<String,Object> htblColNameValue = new Hashtable<>();
        for (int i = 0 ; i < 50 ; i++){
            htblColNameValue.clear();
            htblColNameValue.put("gpa",0.5);
            dbApp.updateTable("students",i+"",htblColNameValue);
        }
    }
   public void selectOnlyoneconditon() throws  DBAppException{
       Hashtable<String,Object> htblColNameValue = new Hashtable<>();
       SQLTerm[] arrSQLTerms;
       arrSQLTerms= new SQLTerm[1];
       arrSQLTerms[0]=new SQLTerm();
       arrSQLTerms[0]._strTableName="First_Test";
       arrSQLTerms[0]._strOperator="";
       arrSQLTerms[0]._strColumnName="gpa";
       arrSQLTerms[0]._objValue=0;
       Iterator<Tuple> result =dbApp.selectFromTable( arrSQLTerms,null);
       Iterator<Tuple> iterator= dbApp.selectFromTable(arrSQLTerms,null);
       while (iterator.hasNext()) {
           System.out.println("Result Found="+iterator.next());
       }

   }
}
