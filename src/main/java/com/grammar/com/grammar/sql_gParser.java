// Generated from C:/Users/gchehata/IdeaProjects/DB2/src/main/java/com/grammar/sql_g.g4 by ANTLR 4.13.1
package com.grammar.com.grammar;
//import com.grammar.sql_gListener;
//import com.grammar.sql_gVisitor;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class sql_gParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, STRING=31, 
		INT=32, DOUBLE=33, SPACE=34, COMMA=35;
	public static final int
		RULE_program = 0, RULE_tablename = 1, RULE_tablecolumns = 2, RULE_attributename = 3, 
		RULE_datatype = 4, RULE_semicolonclosercreate = 5, RULE_semicoloncloserindex = 6, 
		RULE_semicoloncloserinsert = 7, RULE_indexname = 8, RULE_indexCols = 9, 
		RULE_insertColNames = 10, RULE_insertColValues = 11, RULE_createtable = 12, 
		RULE_createindex = 13, RULE_insertValuePair = 14, RULE_insert = 15, RULE_attrname = 16, 
		RULE_values = 17, RULE_eq = 18, RULE_cluster = 19, RULE_name = 20, RULE_closerupdate = 21, 
		RULE_update = 22, RULE_columndelete = 23, RULE_and = 24, RULE_operaone = 25, 
		RULE_closerdelete = 26, RULE_delete = 27, RULE_columns = 28, RULE_oper = 29, 
		RULE_opera = 30, RULE_closerselect = 31, RULE_select = 32;
	private static String[] makeRuleNames() {
		return new String[] {
			"program", "tablename", "tablecolumns", "attributename", "datatype", 
			"semicolonclosercreate", "semicoloncloserindex", "semicoloncloserinsert", 
			"indexname", "indexCols", "insertColNames", "insertColValues", "createtable", 
			"createindex", "insertValuePair", "insert", "attrname", "values", "eq", 
			"cluster", "name", "closerupdate", "update", "columndelete", "and", "operaone", 
			"closerdelete", "delete", "columns", "oper", "opera", "closerselect", 
			"select"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'INT'", "'STRING'", "'DOUBLE'", "';'", "'CREATE TABLE'", "'('", 
			"'PRIMARY KEY'", "','", "')'", "'CREATE INDEX'", "'ON'", "'INSERT INTO'", 
			"'VALUES'", "'='", "'UPDATE'", "'SET'", "'WHERE'", "'and'", "'AND'", 
			"'DELETE FROM'", "'!='", "'>'", "'>='", "'<'", "'<='", "'OR'", "'or'", 
			"'XOR'", "'xor'", "'SELECT * FROM'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, "STRING", "INT", "DOUBLE", 
			"SPACE", "COMMA"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "sql_g.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public sql_gParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProgramContext extends ParserRuleContext {
		public CreatetableContext createtable() {
			return getRuleContext(CreatetableContext.class,0);
		}
		public TerminalNode EOF() { return getToken(sql_gParser.EOF, 0); }
		public CreateindexContext createindex() {
			return getRuleContext(CreateindexContext.class,0);
		}
		public InsertContext insert() {
			return getRuleContext(InsertContext.class,0);
		}
		public UpdateContext update() {
			return getRuleContext(UpdateContext.class,0);
		}
		public DeleteContext delete() {
			return getRuleContext(DeleteContext.class,0);
		}
		public SelectContext select() {
			return getRuleContext(SelectContext.class,0);
		}
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener) ((sql_gListener)listener).enterProgram(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).exitProgram(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof sql_gVisitor) return ((sql_gVisitor<? extends T>)visitor).visitProgram(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_program);
		try {
			setState(84);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__4:
				enterOuterAlt(_localctx, 1);
				{
				setState(66);
				createtable();
				setState(67);
				match(EOF);
				}
				break;
			case T__9:
				enterOuterAlt(_localctx, 2);
				{
				setState(69);
				createindex();
				setState(70);
				match(EOF);
				}
				break;
			case T__11:
				enterOuterAlt(_localctx, 3);
				{
				setState(72);
				insert();
				setState(73);
				match(EOF);
				}
				break;
			case T__14:
				enterOuterAlt(_localctx, 4);
				{
				setState(75);
				update();
				setState(76);
				match(EOF);
				}
				break;
			case T__19:
				enterOuterAlt(_localctx, 5);
				{
				setState(78);
				delete();
				setState(79);
				match(EOF);
				}
				break;
			case T__29:
				enterOuterAlt(_localctx, 6);
				{
				setState(81);
				select();
				setState(82);
				match(EOF);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TablenameContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(sql_gParser.STRING, 0); }
		public TablenameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tablename; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).enterTablename(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).exitTablename(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof sql_gVisitor ) return ((sql_gVisitor<? extends T>)visitor).visitTablename(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TablenameContext tablename() throws RecognitionException {
		TablenameContext _localctx = new TablenameContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_tablename);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(86);
			match(STRING);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TablecolumnsContext extends ParserRuleContext {
		public AttributenameContext attributename() {
			return getRuleContext(AttributenameContext.class,0);
		}
		public DatatypeContext datatype() {
			return getRuleContext(DatatypeContext.class,0);
		}
		public TablecolumnsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tablecolumns; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).enterTablecolumns(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).exitTablecolumns(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof sql_gVisitor ) return ((sql_gVisitor<? extends T>)visitor).visitTablecolumns(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TablecolumnsContext tablecolumns() throws RecognitionException {
		TablecolumnsContext _localctx = new TablecolumnsContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_tablecolumns);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(88);
			attributename();
			setState(89);
			datatype();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AttributenameContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(sql_gParser.STRING, 0); }
		public AttributenameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributename; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).enterAttributename(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).exitAttributename(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof sql_gVisitor ) return ((sql_gVisitor<? extends T>)visitor).visitAttributename(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttributenameContext attributename() throws RecognitionException {
		AttributenameContext _localctx = new AttributenameContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_attributename);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(91);
			match(STRING);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DatatypeContext extends ParserRuleContext {
		public DatatypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_datatype; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).enterDatatype(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).exitDatatype(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof sql_gVisitor ) return ((sql_gVisitor<? extends T>)visitor).visitDatatype(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DatatypeContext datatype() throws RecognitionException {
		DatatypeContext _localctx = new DatatypeContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_datatype);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(93);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 14L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SemicolonclosercreateContext extends ParserRuleContext {
		public SemicolonclosercreateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_semicolonclosercreate; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).enterSemicolonclosercreate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).exitSemicolonclosercreate(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof sql_gVisitor ) return ((sql_gVisitor<? extends T>)visitor).visitSemicolonclosercreate(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SemicolonclosercreateContext semicolonclosercreate() throws RecognitionException {
		SemicolonclosercreateContext _localctx = new SemicolonclosercreateContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_semicolonclosercreate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(95);
			match(T__3);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SemicoloncloserindexContext extends ParserRuleContext {
		public SemicoloncloserindexContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_semicoloncloserindex; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).enterSemicoloncloserindex(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).exitSemicoloncloserindex(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof sql_gVisitor ) return ((sql_gVisitor<? extends T>)visitor).visitSemicoloncloserindex(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SemicoloncloserindexContext semicoloncloserindex() throws RecognitionException {
		SemicoloncloserindexContext _localctx = new SemicoloncloserindexContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_semicoloncloserindex);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(97);
			match(T__3);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SemicoloncloserinsertContext extends ParserRuleContext {
		public SemicoloncloserinsertContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_semicoloncloserinsert; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).enterSemicoloncloserinsert(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).exitSemicoloncloserinsert(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof sql_gVisitor ) return ((sql_gVisitor<? extends T>)visitor).visitSemicoloncloserinsert(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SemicoloncloserinsertContext semicoloncloserinsert() throws RecognitionException {
		SemicoloncloserinsertContext _localctx = new SemicoloncloserinsertContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_semicoloncloserinsert);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(99);
			match(T__3);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IndexnameContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(sql_gParser.STRING, 0); }
		public IndexnameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_indexname; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).enterIndexname(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).exitIndexname(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof sql_gVisitor ) return ((sql_gVisitor<? extends T>)visitor).visitIndexname(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IndexnameContext indexname() throws RecognitionException {
		IndexnameContext _localctx = new IndexnameContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_indexname);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(101);
			match(STRING);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IndexColsContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(sql_gParser.STRING, 0); }
		public IndexColsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_indexCols; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).enterIndexCols(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).exitIndexCols(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof sql_gVisitor ) return ((sql_gVisitor<? extends T>)visitor).visitIndexCols(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IndexColsContext indexCols() throws RecognitionException {
		IndexColsContext _localctx = new IndexColsContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_indexCols);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(103);
			match(STRING);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class InsertColNamesContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(sql_gParser.STRING, 0); }
		public InsertColNamesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_insertColNames; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).enterInsertColNames(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).exitInsertColNames(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof sql_gVisitor ) return ((sql_gVisitor<? extends T>)visitor).visitInsertColNames(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InsertColNamesContext insertColNames() throws RecognitionException {
		InsertColNamesContext _localctx = new InsertColNamesContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_insertColNames);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(105);
			match(STRING);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class InsertColValuesContext extends ParserRuleContext {
		public TerminalNode INT() { return getToken(sql_gParser.INT, 0); }
		public TerminalNode STRING() { return getToken(sql_gParser.STRING, 0); }
		public TerminalNode DOUBLE() { return getToken(sql_gParser.DOUBLE, 0); }
		public InsertColValuesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_insertColValues; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).enterInsertColValues(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).exitInsertColValues(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof sql_gVisitor ) return ((sql_gVisitor<? extends T>)visitor).visitInsertColValues(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InsertColValuesContext insertColValues() throws RecognitionException {
		InsertColValuesContext _localctx = new InsertColValuesContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_insertColValues);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(107);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 15032385536L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CreatetableContext extends ParserRuleContext {
		public TablenameContext tablename() {
			return getRuleContext(TablenameContext.class,0);
		}
		public List<TablecolumnsContext> tablecolumns() {
			return getRuleContexts(TablecolumnsContext.class);
		}
		public TablecolumnsContext tablecolumns(int i) {
			return getRuleContext(TablecolumnsContext.class,i);
		}
		public SemicolonclosercreateContext semicolonclosercreate() {
			return getRuleContext(SemicolonclosercreateContext.class,0);
		}
		public CreatetableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_createtable; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).enterCreatetable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).exitCreatetable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof sql_gVisitor ) return ((sql_gVisitor<? extends T>)visitor).visitCreatetable(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreatetableContext createtable() throws RecognitionException {
		CreatetableContext _localctx = new CreatetableContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_createtable);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(109);
			match(T__4);
			setState(110);
			tablename();
			setState(111);
			match(T__5);
			setState(112);
			tablecolumns();
			setState(113);
			match(T__6);
			setState(118);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__7) {
				{
				{
				setState(114);
				match(T__7);
				setState(115);
				tablecolumns();
				}
				}
				setState(120);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(121);
			match(T__8);
			setState(122);
			semicolonclosercreate();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CreateindexContext extends ParserRuleContext {
		public IndexnameContext indexname() {
			return getRuleContext(IndexnameContext.class,0);
		}
		public TablenameContext tablename() {
			return getRuleContext(TablenameContext.class,0);
		}
		public IndexColsContext indexCols() {
			return getRuleContext(IndexColsContext.class,0);
		}
		public SemicoloncloserindexContext semicoloncloserindex() {
			return getRuleContext(SemicoloncloserindexContext.class,0);
		}
		public CreateindexContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_createindex; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).enterCreateindex(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).exitCreateindex(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof sql_gVisitor ) return ((sql_gVisitor<? extends T>)visitor).visitCreateindex(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreateindexContext createindex() throws RecognitionException {
		CreateindexContext _localctx = new CreateindexContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_createindex);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(124);
			match(T__9);
			setState(125);
			indexname();
			setState(126);
			match(T__10);
			setState(127);
			tablename();
			setState(128);
			match(T__5);
			setState(129);
			indexCols();
			setState(130);
			match(T__8);
			setState(131);
			semicoloncloserindex();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class InsertValuePairContext extends ParserRuleContext {
		public List<InsertColValuesContext> insertColValues() {
			return getRuleContexts(InsertColValuesContext.class);
		}
		public InsertColValuesContext insertColValues(int i) {
			return getRuleContext(InsertColValuesContext.class,i);
		}
		public InsertValuePairContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_insertValuePair; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).enterInsertValuePair(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).exitInsertValuePair(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof sql_gVisitor ) return ((sql_gVisitor<? extends T>)visitor).visitInsertValuePair(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InsertValuePairContext insertValuePair() throws RecognitionException {
		InsertValuePairContext _localctx = new InsertValuePairContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_insertValuePair);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(133);
			insertColValues();
			setState(138);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__7) {
				{
				{
				setState(134);
				match(T__7);
				setState(135);
				insertColValues();
				}
				}
				setState(140);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class InsertContext extends ParserRuleContext {
		public TablenameContext tablename() {
			return getRuleContext(TablenameContext.class,0);
		}
		public List<InsertColNamesContext> insertColNames() {
			return getRuleContexts(InsertColNamesContext.class);
		}
		public InsertColNamesContext insertColNames(int i) {
			return getRuleContext(InsertColNamesContext.class,i);
		}
		public List<InsertValuePairContext> insertValuePair() {
			return getRuleContexts(InsertValuePairContext.class);
		}
		public InsertValuePairContext insertValuePair(int i) {
			return getRuleContext(InsertValuePairContext.class,i);
		}
		public SemicoloncloserinsertContext semicoloncloserinsert() {
			return getRuleContext(SemicoloncloserinsertContext.class,0);
		}
		public InsertContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_insert; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).enterInsert(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).exitInsert(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof sql_gVisitor ) return ((sql_gVisitor<? extends T>)visitor).visitInsert(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InsertContext insert() throws RecognitionException {
		InsertContext _localctx = new InsertContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_insert);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(141);
			match(T__11);
			setState(142);
			tablename();
			setState(143);
			match(T__5);
			setState(144);
			insertColNames();
			setState(149);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__7) {
				{
				{
				setState(145);
				match(T__7);
				setState(146);
				insertColNames();
				}
				}
				setState(151);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(152);
			match(T__8);
			setState(153);
			match(T__12);
			setState(154);
			match(T__5);
			setState(155);
			insertValuePair();
			setState(156);
			match(T__8);
			setState(164);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__7) {
				{
				{
				setState(157);
				match(T__7);
				setState(158);
				match(T__5);
				setState(159);
				insertValuePair();
				setState(160);
				match(T__8);
				}
				}
				setState(166);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(167);
			semicoloncloserinsert();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AttrnameContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(sql_gParser.STRING, 0); }
		public AttrnameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attrname; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).enterAttrname(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).exitAttrname(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof sql_gVisitor ) return ((sql_gVisitor<? extends T>)visitor).visitAttrname(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttrnameContext attrname() throws RecognitionException {
		AttrnameContext _localctx = new AttrnameContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_attrname);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(169);
			match(STRING);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ValuesContext extends ParserRuleContext {
		public TerminalNode INT() { return getToken(sql_gParser.INT, 0); }
		public TerminalNode DOUBLE() { return getToken(sql_gParser.DOUBLE, 0); }
		public TerminalNode STRING() { return getToken(sql_gParser.STRING, 0); }
		public ValuesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_values; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).enterValues(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).exitValues(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof sql_gVisitor ) return ((sql_gVisitor<? extends T>)visitor).visitValues(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValuesContext values() throws RecognitionException {
		ValuesContext _localctx = new ValuesContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_values);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(171);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 15032385536L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EqContext extends ParserRuleContext {
		public EqContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_eq; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).enterEq(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).exitEq(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof sql_gVisitor ) return ((sql_gVisitor<? extends T>)visitor).visitEq(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EqContext eq() throws RecognitionException {
		EqContext _localctx = new EqContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_eq);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(173);
			match(T__13);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ClusterContext extends ParserRuleContext {
		public AttrnameContext attrname() {
			return getRuleContext(AttrnameContext.class,0);
		}
		public EqContext eq() {
			return getRuleContext(EqContext.class,0);
		}
		public ValuesContext values() {
			return getRuleContext(ValuesContext.class,0);
		}
		public ClusterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cluster; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).enterCluster(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).exitCluster(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof sql_gVisitor ) return ((sql_gVisitor<? extends T>)visitor).visitCluster(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClusterContext cluster() throws RecognitionException {
		ClusterContext _localctx = new ClusterContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_cluster);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(175);
			attrname();
			setState(176);
			eq();
			setState(177);
			values();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NameContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(sql_gParser.STRING, 0); }
		public NameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).enterName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).exitName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof sql_gVisitor ) return ((sql_gVisitor<? extends T>)visitor).visitName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NameContext name() throws RecognitionException {
		NameContext _localctx = new NameContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(179);
			match(STRING);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CloserupdateContext extends ParserRuleContext {
		public CloserupdateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_closerupdate; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).enterCloserupdate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).exitCloserupdate(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof sql_gVisitor ) return ((sql_gVisitor<? extends T>)visitor).visitCloserupdate(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CloserupdateContext closerupdate() throws RecognitionException {
		CloserupdateContext _localctx = new CloserupdateContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_closerupdate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(181);
			match(T__3);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class UpdateContext extends ParserRuleContext {
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public List<ColumndeleteContext> columndelete() {
			return getRuleContexts(ColumndeleteContext.class);
		}
		public ColumndeleteContext columndelete(int i) {
			return getRuleContext(ColumndeleteContext.class,i);
		}
		public ClusterContext cluster() {
			return getRuleContext(ClusterContext.class,0);
		}
		public CloserupdateContext closerupdate() {
			return getRuleContext(CloserupdateContext.class,0);
		}
		public List<TerminalNode> COMMA() { return getTokens(sql_gParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(sql_gParser.COMMA, i);
		}
		public UpdateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_update; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).enterUpdate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).exitUpdate(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof sql_gVisitor ) return ((sql_gVisitor<? extends T>)visitor).visitUpdate(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UpdateContext update() throws RecognitionException {
		UpdateContext _localctx = new UpdateContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_update);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(183);
			match(T__14);
			setState(184);
			name();
			setState(185);
			match(T__15);
			setState(186);
			columndelete();
			setState(191);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(187);
				match(COMMA);
				setState(188);
				columndelete();
				}
				}
				setState(193);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(194);
			match(T__16);
			setState(195);
			cluster();
			setState(196);
			closerupdate();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ColumndeleteContext extends ParserRuleContext {
		public AttrnameContext attrname() {
			return getRuleContext(AttrnameContext.class,0);
		}
		public EqContext eq() {
			return getRuleContext(EqContext.class,0);
		}
		public ValuesContext values() {
			return getRuleContext(ValuesContext.class,0);
		}
		public ColumndeleteContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_columndelete; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).enterColumndelete(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).exitColumndelete(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof sql_gVisitor ) return ((sql_gVisitor<? extends T>)visitor).visitColumndelete(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ColumndeleteContext columndelete() throws RecognitionException {
		ColumndeleteContext _localctx = new ColumndeleteContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_columndelete);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(198);
			attrname();
			setState(199);
			eq();
			setState(200);
			values();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AndContext extends ParserRuleContext {
		public AndContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_and; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).enterAnd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).exitAnd(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof sql_gVisitor ) return ((sql_gVisitor<? extends T>)visitor).visitAnd(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AndContext and() throws RecognitionException {
		AndContext _localctx = new AndContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_and);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(202);
			_la = _input.LA(1);
			if ( !(_la==T__17 || _la==T__18) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OperaoneContext extends ParserRuleContext {
		public AndContext and() {
			return getRuleContext(AndContext.class,0);
		}
		public OperaoneContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operaone; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).enterOperaone(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).exitOperaone(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof sql_gVisitor ) return ((sql_gVisitor<? extends T>)visitor).visitOperaone(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OperaoneContext operaone() throws RecognitionException {
		OperaoneContext _localctx = new OperaoneContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_operaone);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(204);
			and();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CloserdeleteContext extends ParserRuleContext {
		public CloserdeleteContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_closerdelete; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).enterCloserdelete(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).exitCloserdelete(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof sql_gVisitor ) return ((sql_gVisitor<? extends T>)visitor).visitCloserdelete(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CloserdeleteContext closerdelete() throws RecognitionException {
		CloserdeleteContext _localctx = new CloserdeleteContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_closerdelete);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(206);
			match(T__3);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DeleteContext extends ParserRuleContext {
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public CloserdeleteContext closerdelete() {
			return getRuleContext(CloserdeleteContext.class,0);
		}
		public List<ColumndeleteContext> columndelete() {
			return getRuleContexts(ColumndeleteContext.class);
		}
		public ColumndeleteContext columndelete(int i) {
			return getRuleContext(ColumndeleteContext.class,i);
		}
		public List<OperaoneContext> operaone() {
			return getRuleContexts(OperaoneContext.class);
		}
		public OperaoneContext operaone(int i) {
			return getRuleContext(OperaoneContext.class,i);
		}
		public DeleteContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_delete; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).enterDelete(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).exitDelete(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof sql_gVisitor ) return ((sql_gVisitor<? extends T>)visitor).visitDelete(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeleteContext delete() throws RecognitionException {
		DeleteContext _localctx = new DeleteContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_delete);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(208);
			match(T__19);
			setState(209);
			name();
			setState(222);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__16) {
				{
				{
				setState(210);
				match(T__16);
				setState(211);
				columndelete();
				setState(217);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__17 || _la==T__18) {
					{
					{
					setState(212);
					operaone();
					setState(213);
					columndelete();
					}
					}
					setState(219);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				}
				setState(224);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(225);
			closerdelete();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ColumnsContext extends ParserRuleContext {
		public AttrnameContext attrname() {
			return getRuleContext(AttrnameContext.class,0);
		}
		public OperContext oper() {
			return getRuleContext(OperContext.class,0);
		}
		public ValuesContext values() {
			return getRuleContext(ValuesContext.class,0);
		}
		public ColumnsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_columns; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).enterColumns(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).exitColumns(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof sql_gVisitor ) return ((sql_gVisitor<? extends T>)visitor).visitColumns(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ColumnsContext columns() throws RecognitionException {
		ColumnsContext _localctx = new ColumnsContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_columns);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(227);
			attrname();
			setState(228);
			oper();
			setState(229);
			values();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OperContext extends ParserRuleContext {
		public OperContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_oper; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).enterOper(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).exitOper(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof sql_gVisitor ) return ((sql_gVisitor<? extends T>)visitor).visitOper(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OperContext oper() throws RecognitionException {
		OperContext _localctx = new OperContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_oper);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(231);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 65028096L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OperaContext extends ParserRuleContext {
		public OperaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_opera; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).enterOpera(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).exitOpera(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof sql_gVisitor ) return ((sql_gVisitor<? extends T>)visitor).visitOpera(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OperaContext opera() throws RecognitionException {
		OperaContext _localctx = new OperaContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_opera);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(233);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 1007419392L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CloserselectContext extends ParserRuleContext {
		public CloserselectContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_closerselect; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).enterCloserselect(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).exitCloserselect(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof sql_gVisitor ) return ((sql_gVisitor<? extends T>)visitor).visitCloserselect(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CloserselectContext closerselect() throws RecognitionException {
		CloserselectContext _localctx = new CloserselectContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_closerselect);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(235);
			match(T__3);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SelectContext extends ParserRuleContext {
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public CloserselectContext closerselect() {
			return getRuleContext(CloserselectContext.class,0);
		}
		public List<ColumnsContext> columns() {
			return getRuleContexts(ColumnsContext.class);
		}
		public ColumnsContext columns(int i) {
			return getRuleContext(ColumnsContext.class,i);
		}
		public List<OperaContext> opera() {
			return getRuleContexts(OperaContext.class);
		}
		public OperaContext opera(int i) {
			return getRuleContext(OperaContext.class,i);
		}
		public SelectContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_select; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).enterSelect(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).exitSelect(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof sql_gVisitor ) return ((sql_gVisitor<? extends T>)visitor).visitSelect(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SelectContext select() throws RecognitionException {
		SelectContext _localctx = new SelectContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_select);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(237);
			match(T__29);
			setState(238);
			name();
			setState(249);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__16) {
				{
				setState(239);
				match(T__16);
				setState(240);
				columns();
				setState(246);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1007419392L) != 0)) {
					{
					{
					setState(241);
					opera();
					setState(242);
					columns();
					}
					}
					setState(248);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(251);
			closerselect();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u0001#\u00fe\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018"+
		"\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007\u001b"+
		"\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002\u001e\u0007\u001e"+
		"\u0002\u001f\u0007\u001f\u0002 \u0007 \u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0003\u0000U\b\u0000\u0001\u0001"+
		"\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003"+
		"\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006"+
		"\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\t\u0001\t\u0001\n\u0001"+
		"\n\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0005\fu\b\f\n\f\f\fx\t\f\u0001\f\u0001\f\u0001\f\u0001\r\u0001"+
		"\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0005\u000e\u0089\b\u000e\n\u000e\f\u000e\u008c"+
		"\t\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001"+
		"\u000f\u0005\u000f\u0094\b\u000f\n\u000f\f\u000f\u0097\t\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0005\u000f\u00a3\b\u000f\n\u000f"+
		"\f\u000f\u00a6\t\u000f\u0001\u000f\u0001\u000f\u0001\u0010\u0001\u0010"+
		"\u0001\u0011\u0001\u0011\u0001\u0012\u0001\u0012\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0014\u0001\u0014\u0001\u0015\u0001\u0015"+
		"\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016"+
		"\u0005\u0016\u00be\b\u0016\n\u0016\f\u0016\u00c1\t\u0016\u0001\u0016\u0001"+
		"\u0016\u0001\u0016\u0001\u0016\u0001\u0017\u0001\u0017\u0001\u0017\u0001"+
		"\u0017\u0001\u0018\u0001\u0018\u0001\u0019\u0001\u0019\u0001\u001a\u0001"+
		"\u001a\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001"+
		"\u001b\u0001\u001b\u0005\u001b\u00d8\b\u001b\n\u001b\f\u001b\u00db\t\u001b"+
		"\u0005\u001b\u00dd\b\u001b\n\u001b\f\u001b\u00e0\t\u001b\u0001\u001b\u0001"+
		"\u001b\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001d\u0001"+
		"\u001d\u0001\u001e\u0001\u001e\u0001\u001f\u0001\u001f\u0001 \u0001 \u0001"+
		" \u0001 \u0001 \u0001 \u0001 \u0005 \u00f5\b \n \f \u00f8\t \u0003 \u00fa"+
		"\b \u0001 \u0001 \u0001 \u0000\u0000!\u0000\u0002\u0004\u0006\b\n\f\u000e"+
		"\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*,.02468:<>@\u0000"+
		"\u0005\u0001\u0000\u0001\u0003\u0001\u0000\u001f!\u0001\u0000\u0012\u0013"+
		"\u0002\u0000\u000e\u000e\u0015\u0019\u0002\u0000\u0012\u0013\u001a\u001d"+
		"\u00ea\u0000T\u0001\u0000\u0000\u0000\u0002V\u0001\u0000\u0000\u0000\u0004"+
		"X\u0001\u0000\u0000\u0000\u0006[\u0001\u0000\u0000\u0000\b]\u0001\u0000"+
		"\u0000\u0000\n_\u0001\u0000\u0000\u0000\fa\u0001\u0000\u0000\u0000\u000e"+
		"c\u0001\u0000\u0000\u0000\u0010e\u0001\u0000\u0000\u0000\u0012g\u0001"+
		"\u0000\u0000\u0000\u0014i\u0001\u0000\u0000\u0000\u0016k\u0001\u0000\u0000"+
		"\u0000\u0018m\u0001\u0000\u0000\u0000\u001a|\u0001\u0000\u0000\u0000\u001c"+
		"\u0085\u0001\u0000\u0000\u0000\u001e\u008d\u0001\u0000\u0000\u0000 \u00a9"+
		"\u0001\u0000\u0000\u0000\"\u00ab\u0001\u0000\u0000\u0000$\u00ad\u0001"+
		"\u0000\u0000\u0000&\u00af\u0001\u0000\u0000\u0000(\u00b3\u0001\u0000\u0000"+
		"\u0000*\u00b5\u0001\u0000\u0000\u0000,\u00b7\u0001\u0000\u0000\u0000."+
		"\u00c6\u0001\u0000\u0000\u00000\u00ca\u0001\u0000\u0000\u00002\u00cc\u0001"+
		"\u0000\u0000\u00004\u00ce\u0001\u0000\u0000\u00006\u00d0\u0001\u0000\u0000"+
		"\u00008\u00e3\u0001\u0000\u0000\u0000:\u00e7\u0001\u0000\u0000\u0000<"+
		"\u00e9\u0001\u0000\u0000\u0000>\u00eb\u0001\u0000\u0000\u0000@\u00ed\u0001"+
		"\u0000\u0000\u0000BC\u0003\u0018\f\u0000CD\u0005\u0000\u0000\u0001DU\u0001"+
		"\u0000\u0000\u0000EF\u0003\u001a\r\u0000FG\u0005\u0000\u0000\u0001GU\u0001"+
		"\u0000\u0000\u0000HI\u0003\u001e\u000f\u0000IJ\u0005\u0000\u0000\u0001"+
		"JU\u0001\u0000\u0000\u0000KL\u0003,\u0016\u0000LM\u0005\u0000\u0000\u0001"+
		"MU\u0001\u0000\u0000\u0000NO\u00036\u001b\u0000OP\u0005\u0000\u0000\u0001"+
		"PU\u0001\u0000\u0000\u0000QR\u0003@ \u0000RS\u0005\u0000\u0000\u0001S"+
		"U\u0001\u0000\u0000\u0000TB\u0001\u0000\u0000\u0000TE\u0001\u0000\u0000"+
		"\u0000TH\u0001\u0000\u0000\u0000TK\u0001\u0000\u0000\u0000TN\u0001\u0000"+
		"\u0000\u0000TQ\u0001\u0000\u0000\u0000U\u0001\u0001\u0000\u0000\u0000"+
		"VW\u0005\u001f\u0000\u0000W\u0003\u0001\u0000\u0000\u0000XY\u0003\u0006"+
		"\u0003\u0000YZ\u0003\b\u0004\u0000Z\u0005\u0001\u0000\u0000\u0000[\\\u0005"+
		"\u001f\u0000\u0000\\\u0007\u0001\u0000\u0000\u0000]^\u0007\u0000\u0000"+
		"\u0000^\t\u0001\u0000\u0000\u0000_`\u0005\u0004\u0000\u0000`\u000b\u0001"+
		"\u0000\u0000\u0000ab\u0005\u0004\u0000\u0000b\r\u0001\u0000\u0000\u0000"+
		"cd\u0005\u0004\u0000\u0000d\u000f\u0001\u0000\u0000\u0000ef\u0005\u001f"+
		"\u0000\u0000f\u0011\u0001\u0000\u0000\u0000gh\u0005\u001f\u0000\u0000"+
		"h\u0013\u0001\u0000\u0000\u0000ij\u0005\u001f\u0000\u0000j\u0015\u0001"+
		"\u0000\u0000\u0000kl\u0007\u0001\u0000\u0000l\u0017\u0001\u0000\u0000"+
		"\u0000mn\u0005\u0005\u0000\u0000no\u0003\u0002\u0001\u0000op\u0005\u0006"+
		"\u0000\u0000pq\u0003\u0004\u0002\u0000qv\u0005\u0007\u0000\u0000rs\u0005"+
		"\b\u0000\u0000su\u0003\u0004\u0002\u0000tr\u0001\u0000\u0000\u0000ux\u0001"+
		"\u0000\u0000\u0000vt\u0001\u0000\u0000\u0000vw\u0001\u0000\u0000\u0000"+
		"wy\u0001\u0000\u0000\u0000xv\u0001\u0000\u0000\u0000yz\u0005\t\u0000\u0000"+
		"z{\u0003\n\u0005\u0000{\u0019\u0001\u0000\u0000\u0000|}\u0005\n\u0000"+
		"\u0000}~\u0003\u0010\b\u0000~\u007f\u0005\u000b\u0000\u0000\u007f\u0080"+
		"\u0003\u0002\u0001\u0000\u0080\u0081\u0005\u0006\u0000\u0000\u0081\u0082"+
		"\u0003\u0012\t\u0000\u0082\u0083\u0005\t\u0000\u0000\u0083\u0084\u0003"+
		"\f\u0006\u0000\u0084\u001b\u0001\u0000\u0000\u0000\u0085\u008a\u0003\u0016"+
		"\u000b\u0000\u0086\u0087\u0005\b\u0000\u0000\u0087\u0089\u0003\u0016\u000b"+
		"\u0000\u0088\u0086\u0001\u0000\u0000\u0000\u0089\u008c\u0001\u0000\u0000"+
		"\u0000\u008a\u0088\u0001\u0000\u0000\u0000\u008a\u008b\u0001\u0000\u0000"+
		"\u0000\u008b\u001d\u0001\u0000\u0000\u0000\u008c\u008a\u0001\u0000\u0000"+
		"\u0000\u008d\u008e\u0005\f\u0000\u0000\u008e\u008f\u0003\u0002\u0001\u0000"+
		"\u008f\u0090\u0005\u0006\u0000\u0000\u0090\u0095\u0003\u0014\n\u0000\u0091"+
		"\u0092\u0005\b\u0000\u0000\u0092\u0094\u0003\u0014\n\u0000\u0093\u0091"+
		"\u0001\u0000\u0000\u0000\u0094\u0097\u0001\u0000\u0000\u0000\u0095\u0093"+
		"\u0001\u0000\u0000\u0000\u0095\u0096\u0001\u0000\u0000\u0000\u0096\u0098"+
		"\u0001\u0000\u0000\u0000\u0097\u0095\u0001\u0000\u0000\u0000\u0098\u0099"+
		"\u0005\t\u0000\u0000\u0099\u009a\u0005\r\u0000\u0000\u009a\u009b\u0005"+
		"\u0006\u0000\u0000\u009b\u009c\u0003\u001c\u000e\u0000\u009c\u00a4\u0005"+
		"\t\u0000\u0000\u009d\u009e\u0005\b\u0000\u0000\u009e\u009f\u0005\u0006"+
		"\u0000\u0000\u009f\u00a0\u0003\u001c\u000e\u0000\u00a0\u00a1\u0005\t\u0000"+
		"\u0000\u00a1\u00a3\u0001\u0000\u0000\u0000\u00a2\u009d\u0001\u0000\u0000"+
		"\u0000\u00a3\u00a6\u0001\u0000\u0000\u0000\u00a4\u00a2\u0001\u0000\u0000"+
		"\u0000\u00a4\u00a5\u0001\u0000\u0000\u0000\u00a5\u00a7\u0001\u0000\u0000"+
		"\u0000\u00a6\u00a4\u0001\u0000\u0000\u0000\u00a7\u00a8\u0003\u000e\u0007"+
		"\u0000\u00a8\u001f\u0001\u0000\u0000\u0000\u00a9\u00aa\u0005\u001f\u0000"+
		"\u0000\u00aa!\u0001\u0000\u0000\u0000\u00ab\u00ac\u0007\u0001\u0000\u0000"+
		"\u00ac#\u0001\u0000\u0000\u0000\u00ad\u00ae\u0005\u000e\u0000\u0000\u00ae"+
		"%\u0001\u0000\u0000\u0000\u00af\u00b0\u0003 \u0010\u0000\u00b0\u00b1\u0003"+
		"$\u0012\u0000\u00b1\u00b2\u0003\"\u0011\u0000\u00b2\'\u0001\u0000\u0000"+
		"\u0000\u00b3\u00b4\u0005\u001f\u0000\u0000\u00b4)\u0001\u0000\u0000\u0000"+
		"\u00b5\u00b6\u0005\u0004\u0000\u0000\u00b6+\u0001\u0000\u0000\u0000\u00b7"+
		"\u00b8\u0005\u000f\u0000\u0000\u00b8\u00b9\u0003(\u0014\u0000\u00b9\u00ba"+
		"\u0005\u0010\u0000\u0000\u00ba\u00bf\u0003.\u0017\u0000\u00bb\u00bc\u0005"+
		"#\u0000\u0000\u00bc\u00be\u0003.\u0017\u0000\u00bd\u00bb\u0001\u0000\u0000"+
		"\u0000\u00be\u00c1\u0001\u0000\u0000\u0000\u00bf\u00bd\u0001\u0000\u0000"+
		"\u0000\u00bf\u00c0\u0001\u0000\u0000\u0000\u00c0\u00c2\u0001\u0000\u0000"+
		"\u0000\u00c1\u00bf\u0001\u0000\u0000\u0000\u00c2\u00c3\u0005\u0011\u0000"+
		"\u0000\u00c3\u00c4\u0003&\u0013\u0000\u00c4\u00c5\u0003*\u0015\u0000\u00c5"+
		"-\u0001\u0000\u0000\u0000\u00c6\u00c7\u0003 \u0010\u0000\u00c7\u00c8\u0003"+
		"$\u0012\u0000\u00c8\u00c9\u0003\"\u0011\u0000\u00c9/\u0001\u0000\u0000"+
		"\u0000\u00ca\u00cb\u0007\u0002\u0000\u0000\u00cb1\u0001\u0000\u0000\u0000"+
		"\u00cc\u00cd\u00030\u0018\u0000\u00cd3\u0001\u0000\u0000\u0000\u00ce\u00cf"+
		"\u0005\u0004\u0000\u0000\u00cf5\u0001\u0000\u0000\u0000\u00d0\u00d1\u0005"+
		"\u0014\u0000\u0000\u00d1\u00de\u0003(\u0014\u0000\u00d2\u00d3\u0005\u0011"+
		"\u0000\u0000\u00d3\u00d9\u0003.\u0017\u0000\u00d4\u00d5\u00032\u0019\u0000"+
		"\u00d5\u00d6\u0003.\u0017\u0000\u00d6\u00d8\u0001\u0000\u0000\u0000\u00d7"+
		"\u00d4\u0001\u0000\u0000\u0000\u00d8\u00db\u0001\u0000\u0000\u0000\u00d9"+
		"\u00d7\u0001\u0000\u0000\u0000\u00d9\u00da\u0001\u0000\u0000\u0000\u00da"+
		"\u00dd\u0001\u0000\u0000\u0000\u00db\u00d9\u0001\u0000\u0000\u0000\u00dc"+
		"\u00d2\u0001\u0000\u0000\u0000\u00dd\u00e0\u0001\u0000\u0000\u0000\u00de"+
		"\u00dc\u0001\u0000\u0000\u0000\u00de\u00df\u0001\u0000\u0000\u0000\u00df"+
		"\u00e1\u0001\u0000\u0000\u0000\u00e0\u00de\u0001\u0000\u0000\u0000\u00e1"+
		"\u00e2\u00034\u001a\u0000\u00e27\u0001\u0000\u0000\u0000\u00e3\u00e4\u0003"+
		" \u0010\u0000\u00e4\u00e5\u0003:\u001d\u0000\u00e5\u00e6\u0003\"\u0011"+
		"\u0000\u00e69\u0001\u0000\u0000\u0000\u00e7\u00e8\u0007\u0003\u0000\u0000"+
		"\u00e8;\u0001\u0000\u0000\u0000\u00e9\u00ea\u0007\u0004\u0000\u0000\u00ea"+
		"=\u0001\u0000\u0000\u0000\u00eb\u00ec\u0005\u0004\u0000\u0000\u00ec?\u0001"+
		"\u0000\u0000\u0000\u00ed\u00ee\u0005\u001e\u0000\u0000\u00ee\u00f9\u0003"+
		"(\u0014\u0000\u00ef\u00f0\u0005\u0011\u0000\u0000\u00f0\u00f6\u00038\u001c"+
		"\u0000\u00f1\u00f2\u0003<\u001e\u0000\u00f2\u00f3\u00038\u001c\u0000\u00f3"+
		"\u00f5\u0001\u0000\u0000\u0000\u00f4\u00f1\u0001\u0000\u0000\u0000\u00f5"+
		"\u00f8\u0001\u0000\u0000\u0000\u00f6\u00f4\u0001\u0000\u0000\u0000\u00f6"+
		"\u00f7\u0001\u0000\u0000\u0000\u00f7\u00fa\u0001\u0000\u0000\u0000\u00f8"+
		"\u00f6\u0001\u0000\u0000\u0000\u00f9\u00ef\u0001\u0000\u0000\u0000\u00f9"+
		"\u00fa\u0001\u0000\u0000\u0000\u00fa\u00fb\u0001\u0000\u0000\u0000\u00fb"+
		"\u00fc\u0003>\u001f\u0000\u00fcA\u0001\u0000\u0000\u0000\nTv\u008a\u0095"+
		"\u00a4\u00bf\u00d9\u00de\u00f6\u00f9";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}