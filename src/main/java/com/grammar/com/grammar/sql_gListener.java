// Generated from C:/Users/gchehata/IdeaProjects/DB2/src/main/java/com/grammar/sql_g.g4 by ANTLR 4.13.1
package com.grammar.com.grammar;
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
	/**
	 * Enter a parse tree produced by {@link sql_gParser#attrname}.
	 * @param ctx the parse tree
	 */
	void enterAttrname(sql_gParser.AttrnameContext ctx);
	/**
	 * Exit a parse tree produced by {@link sql_gParser#attrname}.
	 * @param ctx the parse tree
	 */
	void exitAttrname(sql_gParser.AttrnameContext ctx);
	/**
	 * Enter a parse tree produced by {@link sql_gParser#values}.
	 * @param ctx the parse tree
	 */
	void enterValues(sql_gParser.ValuesContext ctx);
	/**
	 * Exit a parse tree produced by {@link sql_gParser#values}.
	 * @param ctx the parse tree
	 */
	void exitValues(sql_gParser.ValuesContext ctx);
	/**
	 * Enter a parse tree produced by {@link sql_gParser#eq}.
	 * @param ctx the parse tree
	 */
	void enterEq(sql_gParser.EqContext ctx);
	/**
	 * Exit a parse tree produced by {@link sql_gParser#eq}.
	 * @param ctx the parse tree
	 */
	void exitEq(sql_gParser.EqContext ctx);
	/**
	 * Enter a parse tree produced by {@link sql_gParser#cluster}.
	 * @param ctx the parse tree
	 */
	void enterCluster(sql_gParser.ClusterContext ctx);
	/**
	 * Exit a parse tree produced by {@link sql_gParser#cluster}.
	 * @param ctx the parse tree
	 */
	void exitCluster(sql_gParser.ClusterContext ctx);
	/**
	 * Enter a parse tree produced by {@link sql_gParser#name}.
	 * @param ctx the parse tree
	 */
	void enterName(sql_gParser.NameContext ctx);
	/**
	 * Exit a parse tree produced by {@link sql_gParser#name}.
	 * @param ctx the parse tree
	 */
	void exitName(sql_gParser.NameContext ctx);
	/**
	 * Enter a parse tree produced by {@link sql_gParser#closerupdate}.
	 * @param ctx the parse tree
	 */
	void enterCloserupdate(sql_gParser.CloserupdateContext ctx);
	/**
	 * Exit a parse tree produced by {@link sql_gParser#closerupdate}.
	 * @param ctx the parse tree
	 */
	void exitCloserupdate(sql_gParser.CloserupdateContext ctx);
	/**
	 * Enter a parse tree produced by {@link sql_gParser#update}.
	 * @param ctx the parse tree
	 */
	void enterUpdate(sql_gParser.UpdateContext ctx);
	/**
	 * Exit a parse tree produced by {@link sql_gParser#update}.
	 * @param ctx the parse tree
	 */
	void exitUpdate(sql_gParser.UpdateContext ctx);
	/**
	 * Enter a parse tree produced by {@link sql_gParser#columndelete}.
	 * @param ctx the parse tree
	 */
	void enterColumndelete(sql_gParser.ColumndeleteContext ctx);
	/**
	 * Exit a parse tree produced by {@link sql_gParser#columndelete}.
	 * @param ctx the parse tree
	 */
	void exitColumndelete(sql_gParser.ColumndeleteContext ctx);
	/**
	 * Enter a parse tree produced by {@link sql_gParser#and}.
	 * @param ctx the parse tree
	 */
	void enterAnd(sql_gParser.AndContext ctx);
	/**
	 * Exit a parse tree produced by {@link sql_gParser#and}.
	 * @param ctx the parse tree
	 */
	void exitAnd(sql_gParser.AndContext ctx);
	/**
	 * Enter a parse tree produced by {@link sql_gParser#operaone}.
	 * @param ctx the parse tree
	 */
	void enterOperaone(sql_gParser.OperaoneContext ctx);
	/**
	 * Exit a parse tree produced by {@link sql_gParser#operaone}.
	 * @param ctx the parse tree
	 */
	void exitOperaone(sql_gParser.OperaoneContext ctx);
	/**
	 * Enter a parse tree produced by {@link sql_gParser#closerdelete}.
	 * @param ctx the parse tree
	 */
	void enterCloserdelete(sql_gParser.CloserdeleteContext ctx);
	/**
	 * Exit a parse tree produced by {@link sql_gParser#closerdelete}.
	 * @param ctx the parse tree
	 */
	void exitCloserdelete(sql_gParser.CloserdeleteContext ctx);
	/**
	 * Enter a parse tree produced by {@link sql_gParser#delete}.
	 * @param ctx the parse tree
	 */
	void enterDelete(sql_gParser.DeleteContext ctx);
	/**
	 * Exit a parse tree produced by {@link sql_gParser#delete}.
	 * @param ctx the parse tree
	 */
	void exitDelete(sql_gParser.DeleteContext ctx);
	/**
	 * Enter a parse tree produced by {@link sql_gParser#columns}.
	 * @param ctx the parse tree
	 */
	void enterColumns(sql_gParser.ColumnsContext ctx);
	/**
	 * Exit a parse tree produced by {@link sql_gParser#columns}.
	 * @param ctx the parse tree
	 */
	void exitColumns(sql_gParser.ColumnsContext ctx);
	/**
	 * Enter a parse tree produced by {@link sql_gParser#oper}.
	 * @param ctx the parse tree
	 */
	void enterOper(sql_gParser.OperContext ctx);
	/**
	 * Exit a parse tree produced by {@link sql_gParser#oper}.
	 * @param ctx the parse tree
	 */
	void exitOper(sql_gParser.OperContext ctx);
	/**
	 * Enter a parse tree produced by {@link sql_gParser#opera}.
	 * @param ctx the parse tree
	 */
	void enterOpera(sql_gParser.OperaContext ctx);
	/**
	 * Exit a parse tree produced by {@link sql_gParser#opera}.
	 * @param ctx the parse tree
	 */
	void exitOpera(sql_gParser.OperaContext ctx);
	/**
	 * Enter a parse tree produced by {@link sql_gParser#closerselect}.
	 * @param ctx the parse tree
	 */
	void enterCloserselect(sql_gParser.CloserselectContext ctx);
	/**
	 * Exit a parse tree produced by {@link sql_gParser#closerselect}.
	 * @param ctx the parse tree
	 */
	void exitCloserselect(sql_gParser.CloserselectContext ctx);
	/**
	 * Enter a parse tree produced by {@link sql_gParser#select}.
	 * @param ctx the parse tree
	 */
	void enterSelect(sql_gParser.SelectContext ctx);
	/**
	 * Exit a parse tree produced by {@link sql_gParser#select}.
	 * @param ctx the parse tree
	 */
	void exitSelect(sql_gParser.SelectContext ctx);
}