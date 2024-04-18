package com.grammar;

import com.db_engine.DBApp;
import com.db_engine.DBAppException;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import com.db_engine.SQLTerm;
import com.db_engine.Tuple;
import com.grammar.com.grammar.sql_gBaseVisitor;
import com.grammar.com.grammar.sql_gParser;
//import com.grammar.sql_gBaseVisitor;

public class Main extends sql_gBaseVisitor {

    public static Iterator iterator;
    Vector<String> colNames = new Vector<String>();
    Vector<String> colTypes = new Vector<String>();
    Vector<Object> colValues = new Vector<Object>();
    String strTableName = "";
    String strIndexName = "";
    String strIndexColName="";


    @Override
    public Object visitTablename(sql_gParser.TablenameContext ctx) {

        strTableName = ctx.getText();

        //remove first and last character (quotations)
        strTableName = removeQuotations(strTableName);


        return visitChildren(ctx);
    }

    @Override
    public Object visitAttributename(sql_gParser.AttributenameContext ctx) {

        for (int i = 0; i < ctx.getChildCount(); i++) {
            String name = ctx.getChild(i).getText();
            name = removeQuotations(name);
            colNames.add(name);
        }

        return visitChildren(ctx);

    }

    @Override
    public Object visitDatatype(sql_gParser.DatatypeContext ctx) {


        for (int i = 0; i < ctx.getChildCount(); i++) {
            String datatype = ctx.getChild(i).getText();
            if (datatype.equals("INT")) {
                colTypes.add("java.lang.Integer");
            } else if (datatype.equals("STRING")) {
                colTypes.add("java.lang.String");
            } else if (datatype.equals("DOUBLE")) {
                colTypes.add("java.lang.Double");
            }
        }

        return visitChildren(ctx);

    }


    @Override
    public Object visitSemicolonclosercreate(sql_gParser.SemicolonclosercreateContext ctx) {
        Hashtable<String, String> htblColNameValue = new Hashtable<String, String>();
        String strClusteringKeyColumn = "";

        for (int i = 0; i < colNames.size(); i++) {
            if (i == 0) {
                strClusteringKeyColumn = colNames.get(i);
            }

            htblColNameValue.put(colNames.get(i), colTypes.get(i));
        }

        System.out.print("Table Name: " + strTableName + "\n");
        System.out.print("Clustering Key Column: " + strClusteringKeyColumn + "\n");
        System.out.print("Columns: " + htblColNameValue + "\n");

        try {
            DBApp db = new DBApp();
            db.init();
            db.createTable(strTableName, strClusteringKeyColumn, htblColNameValue);
        } catch (DBAppException e) {
            throw new RuntimeException(e.getMessage());
        }

        return visitChildren(ctx);

    }

    @Override
    public Object visitIndexname(sql_gParser.IndexnameContext ctx) {

        strIndexName = ctx.getText();

        //remove first and last character (quotations)
        strIndexName=removeQuotations(strIndexName);

        return visitChildren(ctx);

    }


    @Override
    public Object visitSemicoloncloserindex(sql_gParser.SemicoloncloserindexContext ctx) {

        try {
            DBApp db = new DBApp();
            db.init();
            db.createIndex(strTableName, strIndexColName, strIndexName);
        } catch (DBAppException e) {
            throw new RuntimeException(e);
        }

        System.out.print("Table Name: " + strTableName + "\n");
        System.out.print("Index Name: " + strIndexName + "\n");
        System.out.print("Column Name: " + strIndexColName + "\n");


        return visitChildren(ctx);

    }

    @Override
    public Object visitInsertColNames(sql_gParser.InsertColNamesContext ctx) {

        for (int i = 0; i < ctx.getChildCount(); i++) {
            String name = ctx.getChild(i).getText();
            name = removeQuotations(name);
            colNames.add(name);
        }
        return visitChildren(ctx);
    }


    @Override
    public Object visitInsertColValues(sql_gParser.InsertColValuesContext ctx) {

        for (int i = 0; i < ctx.getChildCount(); i++) {
            Object value = ctx.getChild(i).getText();
            if (tryParseInt((String) value) != null) {
                value = tryParseInt((String) value);
            } else if (tryParseDouble((String) value) != null) {
                value = tryParseDouble((String) value);
            }
            colValues.add(value);
        }

        return visitChildren(ctx);

    }

    @Override public Object visitInsertValuePair(sql_gParser.InsertValuePairContext ctx) {

        int colnamesSize = colNames.size();

//        for (int i = 0; i < ctx.getChildCount(); i++) {
//
//        int pairsize = ctx.getChild(i).getChildCount();
//
//
//        }

        if((( ctx.getChildCount() / 2 ) + 1 ) != colnamesSize){
            throw new RuntimeException("Number of columns and values do not match: "+ ctx.getText());
        }
        return visitChildren(ctx);
    }


    @Override
    public Object visitSemicoloncloserinsert(sql_gParser.SemicoloncloserinsertContext ctx) {


        System.out.println("colname size: " + colNames.size());
        System.out.println("colValues size: " + colValues.size());
        Hashtable<String, Object> htblColNameValue = new Hashtable<>();
        for (int i = 0; i < colNames.size(); i++) {
            htblColNameValue.put(colNames.get(i), colValues.get(i));
        }
        try {
            DBApp db = new DBApp();
            db.init();
            db.insertIntoTable(strTableName, htblColNameValue);
        } catch (DBAppException e) {
            throw new RuntimeException(e);
        }


        return visitChildren(ctx);

    }

    @Override public Object visitIndexCols(sql_gParser.IndexColsContext ctx) {

        strIndexColName = ctx.getText();

        strIndexColName = removeQuotations(strIndexColName);

        return visitChildren(ctx);

    }

    public static Integer tryParseInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return null; // Parsing failed
        }
    }

    public static Double tryParseDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return null; // Parsing failed
        }
    }

    public static String removeQuotations(String str) {
        return  str.substring(1, str.length() - 1);
    }

    //HARRIDY CODE

    String tablename;
    Vector<String> colnames = new Vector<>();
    Vector<String> operators = new Vector<>();
    Vector<Object> values = new Vector<>();
    Vector<String> opera = new Vector<>();


    @Override
    public Object visitName(sql_gParser.NameContext ctx) {
        tablename = ctx.getText();
        tablename = removeQuotations(tablename);
        System.out.println(tablename);
        return visitChildren(ctx);
    }

    @Override
    public Object visitAttrname(sql_gParser.AttrnameContext ctx) {

        for (Object val : ctx.children) {
            colnames.add(Main.removeQuotations(val.toString()));
        }
        return visitChildren(ctx);
    }

    @Override
    public Object visitOper(sql_gParser.OperContext ctx) {
        for (Object val : ctx.children) {
//            operators.add(Main.removeQuotations(val.toString()));
            operators.add(val.toString());
        }
        return visitChildren(ctx);
    }

    @Override
    public Object visitValues(sql_gParser.ValuesContext ctx) {
        for (int i = 0; i < ctx.getChildCount(); i++) {
            Object val = ctx.getChild(i).getText();
            if (Main.tryParseInt((String) val) != null) {
                val = (int) Main.tryParseInt((String) val);
            } else if (Main.tryParseDouble((String) val) != null) {
                val = (Double) Main.tryParseDouble((String) val);
            }
            values.add(val);
        }

        return visitChildren(ctx);
    }
    @Override public Object visitOpera(sql_gParser.OperaContext ctx) {
        for (int i = 0; i < ctx.getChildCount(); i++) {
            Object val = ctx.getChild(i).getText();
            opera.add((String) val);
        }

        return visitChildren(ctx); }

    @Override public Iterator<Tuple> visitCloserselect(sql_gParser.CloserselectContext ctx) {
        SQLTerm [] sql  = new SQLTerm[colnames.size()];
        for(int i=0;i<colnames.size();i++){
            sql[i]= new SQLTerm();
            sql[i]._strTableName=tablename;
            sql[i]._strColumnName=colnames.get(i);
            sql[i]._strOperator= operators.get(i);
            sql[i]._objValue= values.get(i);
        }
        String[] strarrOperators = new String[opera.size()];
        for(int i=0;i<opera.size();i++){
            strarrOperators[i]=opera.get(i);
        }

        try{
            DBApp dbApp = new DBApp();
            dbApp.init();
           iterator = dbApp.selectFromTable(sql, strarrOperators);
            while(iterator.hasNext()){
                Tuple t = (Tuple) iterator.next();
                System.out.println(t);
            }
            return iterator;

        }
        catch(DBAppException | ClassNotFoundException | IOException e){
            e.printStackTrace();
        }

        return null;


    }
    @Override public Object visitCloserdelete(sql_gParser.CloserdeleteContext ctx) {

        Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
        for(int i=0;i<colnames.size();i++){
            htblColNameValue.put(colnames.get(i), values.get(i));
        }
        try {
            DBApp dbApp = new DBApp();
            dbApp.init();
            dbApp.deleteFromTable(tablename, htblColNameValue);
        }
        catch(DBAppException e){
            e.printStackTrace();
        }

        return visitChildren(ctx); }

    @Override public Object visitCloserupdate(sql_gParser.CloserupdateContext ctx) {
        Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
        for(int i=0;i<colnames.size()-1;i++){
            htblColNameValue.put(colnames.get(i), values.get(i));
        }
        String strkey="" + values.getLast();
//        if(values.getLast() instanceof String){
//             strkey =(String) values.getLast();
//        }

        try {
            DBApp dbApp = new DBApp();
            dbApp.init();
            dbApp.updateTable(tablename, strkey, htblColNameValue);
        }
        catch(DBAppException  e){
            e.printStackTrace();
        }
        return visitChildren(ctx); }


}