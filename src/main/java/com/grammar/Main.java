package com.grammar;

import com.db_engine.DBApp;
import com.db_engine.DBAppException;
import java.util.Hashtable;
import java.util.Vector;

import com.grammar.sql_gBaseVisitor;
import com.grammar.sql_gVisitor;
import com.grammar.sql_gVisitor;
import com.grammar.sql_gParser;
public class Main extends sql_gBaseVisitor {

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
                colTypes.add(i, "java.lang.Integer");
            } else if (datatype.equals("STRING")) {
                colTypes.add(i, "java.lang.String");
            } else if (datatype.equals("DOUBLE")) {
                colTypes.add(i, "java.lang.Double");
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


    @Override
    public Object visitSemicoloncloserinsert(sql_gParser.SemicoloncloserinsertContext ctx) {


        System.out.println("Table Name: " + strTableName);
        System.out.println("Table Name: " + colNames.get(0));
        for (int i = 0; i < colNames.size(); i++) {
            Hashtable<String, Object> htblColNameValue = new Hashtable<>();
            System.out.print("Column Value: " + colValues.get(0).getClass() + "\n");
            htblColNameValue.put(colNames.get(i), colValues.get(i));
            try {
                DBApp db = new DBApp();
                db.init();
                db.insertIntoTable(strTableName, htblColNameValue);
            } catch (DBAppException e) {
                throw new RuntimeException(e);
            }

            System.out.print("Column Name: " + colNames.get(i) + "\n");
            System.out.print("Column Value: " + colValues.get(i) + "\n");

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

}

