// Generated from C:/Users/gchehata/IdeaProjects/DB2/src/main/java/com/grammar/sql_g.g4 by ANTLR 4.13.1
package com.grammar;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link sql_gParser}.
 */
public interface sql_gListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link sql_gParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(sql_gParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link sql_gParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(sql_gParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link sql_gParser#tablename}.
	 * @param ctx the parse tree
	 */
	void enterTablename(sql_gParser.TablenameContext ctx);
	/**
	 * Exit a parse tree produced by {@link sql_gParser#tablename}.
	 * @param ctx the parse tree
	 */
	void exitTablename(sql_gParser.TablenameContext ctx);
	/**
	 * Enter a parse tree produced by {@link sql_gParser#tablecolumns}.
	 * @param ctx the parse tree
	 */
	void enterTablecolumns(sql_gParser.TablecolumnsContext ctx);
	/**
	 * Exit a parse tree produced by {@link sql_gParser#tablecolumns}.
	 * @param ctx the parse tree
	 */
	void exitTablecolumns(sql_gParser.TablecolumnsContext ctx);
	/**
	 * Enter a parse tree produced by {@link sql_gParser#attributename}.
	 * @param ctx the parse tree
	 */
	void enterAttributename(sql_gParser.AttributenameContext ctx);
	/**
	 * Exit a parse tree produced by {@link sql_gParser#attributename}.
	 * @param ctx the parse tree
	 */
	void exitAttributename(sql_gParser.AttributenameContext ctx);
	/**
	 * Enter a parse tree produced by {@link sql_gParser#datatype}.
	 * @param ctx the parse tree
	 */
	void enterDatatype(sql_gParser.DatatypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link sql_gParser#datatype}.
	 * @param ctx the parse tree
	 */
	void exitDatatype(sql_gParser.DatatypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link sql_gParser#semicolonclosercreate}.
	 * @param ctx the parse tree
	 */
	void enterSemicolonclosercreate(sql_gParser.SemicolonclosercreateContext ctx);
	/**
	 * Exit a parse tree produced by {@link sql_gParser#semicolonclosercreate}.
	 * @param ctx the parse tree
	 */
	void exitSemicolonclosercreate(sql_gParser.SemicolonclosercreateContext ctx);
	/**
	 * Enter a parse tree produced by {@link sql_gParser#semicoloncloserindex}.
	 * @param ctx the parse tree
	 */
	void enterSemicoloncloserindex(sql_gParser.SemicoloncloserindexContext ctx);
	/**
	 * Exit a parse tree produced by {@link sql_gParser#semicoloncloserindex}.
	 * @param ctx the parse tree
	 */
	void exitSemicoloncloserindex(sql_gParser.SemicoloncloserindexContext ctx);
	/**
	 * Enter a parse tree produced by {@link sql_gParser#semicoloncloserinsert}.
	 * @param ctx the parse tree
	 */
	void enterSemicoloncloserinsert(sql_gParser.SemicoloncloserinsertContext ctx);
	/**
	 * Exit a parse tree produced by {@link sql_gParser#semicoloncloserinsert}.
	 * @param ctx the parse tree
	 */
	void exitSemicoloncloserinsert(sql_gParser.SemicoloncloserinsertContext ctx);
	/**
	 * Enter a parse tree produced by {@link sql_gParser#indexname}.
	 * @param ctx the parse tree
	 */
	void enterIndexname(sql_gParser.IndexnameContext ctx);
	/**
	 * Exit a parse tree produced by {@link sql_gParser#indexname}.
	 * @param ctx the parse tree
	 */
	void exitIndexname(sql_gParser.IndexnameContext ctx);
	/**
	 * Enter a parse tree produced by {@link sql_gParser#indexCols}.
	 * @param ctx the parse tree
	 */
	void enterIndexCols(sql_gParser.IndexColsContext ctx);
	/**
	 * Exit a parse tree produced by {@link sql_gParser#indexCols}.
	 * @param ctx the parse tree
	 */
	void exitIndexCols(sql_gParser.IndexColsContext ctx);
	/**
	 * Enter a parse tree produced by {@link sql_gParser#insertColNames}.
	 * @param ctx the parse tree
	 */
	void enterInsertColNames(sql_gParser.InsertColNamesContext ctx);
	/**
	 * Exit a parse tree produced by {@link sql_gParser#insertColNames}.
	 * @param ctx the parse tree
	 */
	void exitInsertColNames(sql_gParser.InsertColNamesContext ctx);
	/**
	 * Enter a parse tree produced by {@link sql_gParser#insertColValues}.
	 * @param ctx the parse tree
	 */
	void enterInsertColValues(sql_gParser.InsertColValuesContext ctx);
	/**
	 * Exit a parse tree produced by {@link sql_gParser#insertColValues}.
	 * @param ctx the parse tree
	 */
	void exitInsertColValues(sql_gParser.InsertColValuesContext ctx);
	/**
	 * Enter a parse tree produced by {@link sql_gParser#createtable}.
	 * @param ctx the parse tree
	 */
	void enterCreatetable(sql_gParser.CreatetableContext ctx);
	/**
	 * Exit a parse tree produced by {@link sql_gParser#createtable}.
	 * @param ctx the parse tree
	 */
	void exitCreatetable(sql_gParser.CreatetableContext ctx);
	/**
	 * Enter a parse tree produced by {@link sql_gParser#createindex}.
	 * @param ctx the parse tree
	 */
	void enterCreateindex(sql_gParser.CreateindexContext ctx);
	/**
	 * Exit a parse tree produced by {@link sql_gParser#createindex}.
	 * @param ctx the parse tree
	 */
	void exitCreateindex(sql_gParser.CreateindexContext ctx);
	/**
	 * Enter a parse tree produced by {@link sql_gParser#insertValuePair}.
	 * @param ctx the parse tree
	 */
	void enterInsertValuePair(sql_gParser.InsertValuePairContext ctx);
	/**
	 * Exit a parse tree produced by {@link sql_gParser#insertValuePair}.
	 * @param ctx the parse tree
	 */
	void exitInsertValuePair(sql_gParser.InsertValuePairContext ctx);
	/**
	 * Enter a parse tree produced by {@link sql_gParser#insert}.
	 * @param ctx the parse tree
	 */
	void enterInsert(sql_gParser.InsertContext ctx);
	/**
	 * Exit a parse tree produced by {@link sql_gParser#insert}.
	 * @param ctx the parse tree
	 */
	void exitInsert(sql_gParser.InsertContext ctx);
}