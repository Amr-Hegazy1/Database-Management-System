// Generated from C:/Users/gchehata/IdeaProjects/DB2/src/main/java/com/grammar/sql_g.g4 by ANTLR 4.13.1
package com.grammar;
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
	 * Visit a parse tree produced by {@link sql_gParser#insert}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsert(sql_gParser.InsertContext ctx);
}