// Generated from C:/Users/gchehata/IdeaProjects/DB2/src/main/java/com/grammar/sql_g.g4 by ANTLR 4.13.1
package com.grammar;
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
		T__9=10, T__10=11, T__11=12, T__12=13, STRING=14, INT=15, DOUBLE=16, SPACE=17;
	public static final int
		RULE_program = 0, RULE_tablename = 1, RULE_tablecolumns = 2, RULE_attributename = 3, 
		RULE_datatype = 4, RULE_semicolonclosercreate = 5, RULE_semicoloncloserindex = 6, 
		RULE_semicoloncloserinsert = 7, RULE_indexname = 8, RULE_indexCols = 9, 
		RULE_insertColNames = 10, RULE_insertColValues = 11, RULE_createtable = 12, 
		RULE_createindex = 13, RULE_insert = 14;
	private static String[] makeRuleNames() {
		return new String[] {
			"program", "tablename", "tablecolumns", "attributename", "datatype", 
			"semicolonclosercreate", "semicoloncloserindex", "semicoloncloserinsert", 
			"indexname", "indexCols", "insertColNames", "insertColValues", "createtable", 
			"createindex", "insert"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'INT'", "'STRING'", "'DOUBLE'", "';'", "'CREATE TABLE'", "'('", 
			"'PRIMARY KEY'", "','", "')'", "'CREATE INDEX'", "'ON'", "'INSERT INTO'", 
			"'VALUES'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, "STRING", "INT", "DOUBLE", "SPACE"
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
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).enterProgram(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof sql_gListener ) ((sql_gListener)listener).exitProgram(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof sql_gVisitor ) return ((sql_gVisitor<? extends T>)visitor).visitProgram(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_program);
		try {
			setState(39);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__4:
				enterOuterAlt(_localctx, 1);
				{
				setState(30);
				createtable();
				setState(31);
				match(EOF);
				}
				break;
			case T__9:
				enterOuterAlt(_localctx, 2);
				{
				setState(33);
				createindex();
				setState(34);
				match(EOF);
				}
				break;
			case T__11:
				enterOuterAlt(_localctx, 3);
				{
				setState(36);
				insert();
				setState(37);
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
			setState(41);
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
			setState(43);
			attributename();
			setState(44);
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
			setState(46);
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
			setState(48);
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
			setState(50);
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
			setState(52);
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
			setState(54);
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
			setState(56);
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
			setState(58);
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
			setState(60);
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
			setState(62);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 114688L) != 0)) ) {
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
			setState(64);
			match(T__4);
			setState(65);
			tablename();
			setState(66);
			match(T__5);
			setState(67);
			tablecolumns();
			setState(68);
			match(T__6);
			setState(73);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__7) {
				{
				{
				setState(69);
				match(T__7);
				setState(70);
				tablecolumns();
				}
				}
				setState(75);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(76);
			match(T__8);
			setState(77);
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
			setState(79);
			match(T__9);
			setState(80);
			indexname();
			setState(81);
			match(T__10);
			setState(82);
			tablename();
			setState(83);
			match(T__5);
			setState(84);
			indexCols();
			setState(85);
			match(T__8);
			setState(86);
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
		public List<InsertColValuesContext> insertColValues() {
			return getRuleContexts(InsertColValuesContext.class);
		}
		public InsertColValuesContext insertColValues(int i) {
			return getRuleContext(InsertColValuesContext.class,i);
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
		enterRule(_localctx, 28, RULE_insert);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(88);
			match(T__11);
			setState(89);
			tablename();
			setState(90);
			match(T__5);
			setState(91);
			insertColNames();
			setState(96);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__7) {
				{
				{
				setState(92);
				match(T__7);
				setState(93);
				insertColNames();
				}
				}
				setState(98);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(99);
			match(T__8);
			setState(100);
			match(T__12);
			setState(101);
			match(T__5);
			setState(102);
			insertColValues();
			setState(107);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__7) {
				{
				{
				setState(103);
				match(T__7);
				setState(104);
				insertColValues();
				}
				}
				setState(109);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(110);
			match(T__8);
			setState(111);
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

	public static final String _serializedATN =
		"\u0004\u0001\u0011r\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0003\u0000(\b\u0000\u0001\u0001\u0001\u0001\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004"+
		"\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007"+
		"\u0001\b\u0001\b\u0001\t\u0001\t\u0001\n\u0001\n\u0001\u000b\u0001\u000b"+
		"\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0005\fH\b\f"+
		"\n\f\f\fK\t\f\u0001\f\u0001\f\u0001\f\u0001\r\u0001\r\u0001\r\u0001\r"+
		"\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\u000e\u0001\u000e\u0001"+
		"\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0005\u000e_\b\u000e\n\u000e"+
		"\f\u000eb\t\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001"+
		"\u000e\u0001\u000e\u0005\u000ej\b\u000e\n\u000e\f\u000em\t\u000e\u0001"+
		"\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0000\u0000\u000f\u0000\u0002"+
		"\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u0000"+
		"\u0002\u0001\u0000\u0001\u0003\u0001\u0000\u000e\u0010g\u0000\'\u0001"+
		"\u0000\u0000\u0000\u0002)\u0001\u0000\u0000\u0000\u0004+\u0001\u0000\u0000"+
		"\u0000\u0006.\u0001\u0000\u0000\u0000\b0\u0001\u0000\u0000\u0000\n2\u0001"+
		"\u0000\u0000\u0000\f4\u0001\u0000\u0000\u0000\u000e6\u0001\u0000\u0000"+
		"\u0000\u00108\u0001\u0000\u0000\u0000\u0012:\u0001\u0000\u0000\u0000\u0014"+
		"<\u0001\u0000\u0000\u0000\u0016>\u0001\u0000\u0000\u0000\u0018@\u0001"+
		"\u0000\u0000\u0000\u001aO\u0001\u0000\u0000\u0000\u001cX\u0001\u0000\u0000"+
		"\u0000\u001e\u001f\u0003\u0018\f\u0000\u001f \u0005\u0000\u0000\u0001"+
		" (\u0001\u0000\u0000\u0000!\"\u0003\u001a\r\u0000\"#\u0005\u0000\u0000"+
		"\u0001#(\u0001\u0000\u0000\u0000$%\u0003\u001c\u000e\u0000%&\u0005\u0000"+
		"\u0000\u0001&(\u0001\u0000\u0000\u0000\'\u001e\u0001\u0000\u0000\u0000"+
		"\'!\u0001\u0000\u0000\u0000\'$\u0001\u0000\u0000\u0000(\u0001\u0001\u0000"+
		"\u0000\u0000)*\u0005\u000e\u0000\u0000*\u0003\u0001\u0000\u0000\u0000"+
		"+,\u0003\u0006\u0003\u0000,-\u0003\b\u0004\u0000-\u0005\u0001\u0000\u0000"+
		"\u0000./\u0005\u000e\u0000\u0000/\u0007\u0001\u0000\u0000\u000001\u0007"+
		"\u0000\u0000\u00001\t\u0001\u0000\u0000\u000023\u0005\u0004\u0000\u0000"+
		"3\u000b\u0001\u0000\u0000\u000045\u0005\u0004\u0000\u00005\r\u0001\u0000"+
		"\u0000\u000067\u0005\u0004\u0000\u00007\u000f\u0001\u0000\u0000\u0000"+
		"89\u0005\u000e\u0000\u00009\u0011\u0001\u0000\u0000\u0000:;\u0005\u000e"+
		"\u0000\u0000;\u0013\u0001\u0000\u0000\u0000<=\u0005\u000e\u0000\u0000"+
		"=\u0015\u0001\u0000\u0000\u0000>?\u0007\u0001\u0000\u0000?\u0017\u0001"+
		"\u0000\u0000\u0000@A\u0005\u0005\u0000\u0000AB\u0003\u0002\u0001\u0000"+
		"BC\u0005\u0006\u0000\u0000CD\u0003\u0004\u0002\u0000DI\u0005\u0007\u0000"+
		"\u0000EF\u0005\b\u0000\u0000FH\u0003\u0004\u0002\u0000GE\u0001\u0000\u0000"+
		"\u0000HK\u0001\u0000\u0000\u0000IG\u0001\u0000\u0000\u0000IJ\u0001\u0000"+
		"\u0000\u0000JL\u0001\u0000\u0000\u0000KI\u0001\u0000\u0000\u0000LM\u0005"+
		"\t\u0000\u0000MN\u0003\n\u0005\u0000N\u0019\u0001\u0000\u0000\u0000OP"+
		"\u0005\n\u0000\u0000PQ\u0003\u0010\b\u0000QR\u0005\u000b\u0000\u0000R"+
		"S\u0003\u0002\u0001\u0000ST\u0005\u0006\u0000\u0000TU\u0003\u0012\t\u0000"+
		"UV\u0005\t\u0000\u0000VW\u0003\f\u0006\u0000W\u001b\u0001\u0000\u0000"+
		"\u0000XY\u0005\f\u0000\u0000YZ\u0003\u0002\u0001\u0000Z[\u0005\u0006\u0000"+
		"\u0000[`\u0003\u0014\n\u0000\\]\u0005\b\u0000\u0000]_\u0003\u0014\n\u0000"+
		"^\\\u0001\u0000\u0000\u0000_b\u0001\u0000\u0000\u0000`^\u0001\u0000\u0000"+
		"\u0000`a\u0001\u0000\u0000\u0000ac\u0001\u0000\u0000\u0000b`\u0001\u0000"+
		"\u0000\u0000cd\u0005\t\u0000\u0000de\u0005\r\u0000\u0000ef\u0005\u0006"+
		"\u0000\u0000fk\u0003\u0016\u000b\u0000gh\u0005\b\u0000\u0000hj\u0003\u0016"+
		"\u000b\u0000ig\u0001\u0000\u0000\u0000jm\u0001\u0000\u0000\u0000ki\u0001"+
		"\u0000\u0000\u0000kl\u0001\u0000\u0000\u0000ln\u0001\u0000\u0000\u0000"+
		"mk\u0001\u0000\u0000\u0000no\u0005\t\u0000\u0000op\u0003\u000e\u0007\u0000"+
		"p\u001d\u0001\u0000\u0000\u0000\u0004\'I`k";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}