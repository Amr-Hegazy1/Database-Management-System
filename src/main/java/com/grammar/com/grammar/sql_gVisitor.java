// Generated from C:/Users/gchehata/IdeaProjects/DB2/src/main/java/com/grammar/sql_g.g4 by ANTLR 4.13.1
package com.grammar.com.grammar;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link sql_gParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface sql_gVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link sql_gParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(sql_gParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link sql_gParser#tablename}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTablename(sql_gParser.TablenameContext ctx);
	/**
	 * Visit a parse tree produced by {@link sql_gParser#tablecolumns}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTablecolumns(sql_gParser.TablecolumnsContext ctx);
	/**
	 * Visit a parse tree produced by {@link sql_gParser#attributename}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributename(sql_gParser.AttributenameContext ctx);
	/**
	 * Visit a parse tree produced by {@link sql_gParser#datatype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatatype(sql_gParser.DatatypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link sql_gParser#semicolonclosercreate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSemicolonclosercreate(sql_gParser.SemicolonclosercreateContext ctx);
	/**
	 * Visit a parse tree produced by {@link sql_gParser#semicoloncloserindex}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSemicoloncloserindex(sql_gParser.SemicoloncloserindexContext ctx);
	/**
	 * Visit a parse tree produced by {@link sql_gParser#semicoloncloserinsert}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSemicoloncloserinsert(sql_gParser.SemicoloncloserinsertContext ctx);
	/**
	 * Visit a parse tree produced by {@link sql_gParser#indexname}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexname(sql_gParser.IndexnameContext ctx);
	/**
	 * Visit a parse tree produced by {@link sql_gParser#indexCols}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexCols(sql_gParser.IndexColsContext ctx);
	/**
	 * Visit a parse tree produced by {@link sql_gParser#insertColNames}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsertColNames(sql_gParser.InsertColNamesContext ctx);
	/**
	 * Visit a parse tree produced by {@link sql_gParser#insertColValues}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsertColValues(sql_gParser.InsertColValuesContext ctx);
	/**
	 * Visit a parse tree produced by {@link sql_gParser#createtable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatetable(sql_gParser.CreatetableContext ctx);
	/**
	 * Visit a parse tree produced by {@link sql_gParser#createindex}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateindex(sql_gParser.CreateindexContext ctx);
	/**
	 * Visit a parse tree produced by {@link sql_gParser#insertValuePair}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsertValuePair(sql_gParser.InsertValuePairContext ctx);
	/**
	 * Visit a parse tree produced by {@link sql_gParser#insert}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsert(sql_gParser.InsertContext ctx);
	/**
	 * Visit a parse tree produced by {@link sql_gParser#attrname}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttrname(sql_gParser.AttrnameContext ctx);
	/**
	 * Visit a parse tree produced by {@link sql_gParser#values}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValues(sql_gParser.ValuesContext ctx);
	/**
	 * Visit a parse tree produced by {@link sql_gParser#eq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEq(sql_gParser.EqContext ctx);
	/**
	 * Visit a parse tree produced by {@link sql_gParser#cluster}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCluster(sql_gParser.ClusterContext ctx);
	/**
	 * Visit a parse tree produced by {@link sql_gParser#name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitName(sql_gParser.NameContext ctx);
	/**
	 * Visit a parse tree produced by {@link sql_gParser#closerupdate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCloserupdate(sql_gParser.CloserupdateContext ctx);
	/**
	 * Visit a parse tree produced by {@link sql_gParser#update}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdate(sql_gParser.UpdateContext ctx);
	/**
	 * Visit a parse tree produced by {@link sql_gParser#columndelete}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumndelete(sql_gParser.ColumndeleteContext ctx);
	/**
	 * Visit a parse tree produced by {@link sql_gParser#and}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnd(sql_gParser.AndContext ctx);
	/**
	 * Visit a parse tree produced by {@link sql_gParser#operaone}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperaone(sql_gParser.OperaoneContext ctx);
	/**
	 * Visit a parse tree produced by {@link sql_gParser#closerdelete}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCloserdelete(sql_gParser.CloserdeleteContext ctx);
	/**
	 * Visit a parse tree produced by {@link sql_gParser#delete}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDelete(sql_gParser.DeleteContext ctx);
	/**
	 * Visit a parse tree produced by {@link sql_gParser#columns}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumns(sql_gParser.ColumnsContext ctx);
	/**
	 * Visit a parse tree produced by {@link sql_gParser#oper}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOper(sql_gParser.OperContext ctx);
	/**
	 * Visit a parse tree produced by {@link sql_gParser#opera}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpera(sql_gParser.OperaContext ctx);
	/**
	 * Visit a parse tree produced by {@link sql_gParser#closerselect}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCloserselect(sql_gParser.CloserselectContext ctx);
	/**
	 * Visit a parse tree produced by {@link sql_gParser#select}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect(sql_gParser.SelectContext ctx);
}