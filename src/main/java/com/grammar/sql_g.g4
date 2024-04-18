grammar sql_g;

program :
createtable EOF
|createindex EOF
|insert EOF
;

STRING
 : '"' (~["\r\n] | '""')* '"'
 ;

 INT
  : [0-9]+
  ;

 DOUBLE
  : [0-9]+ '.' [0-9]*
  | '.' [0-9]+
  ;

tablename :
STRING
;

tablecolumns :
attributename datatype
;

attributename :
STRING
;

datatype :
'INT' | 'STRING' | 'DOUBLE'
;

SPACE
 : [ \t\r\n] -> skip
 ;

semicolonclosercreate :
    ';'
;

 semicoloncloserindex :
    ';'
  ;

  semicoloncloserinsert:
    ';'
    ;
 indexname:
 STRING
 ;

  indexCols:
  STRING
  ;

 insertColNames:
 STRING
 ;

 insertColValues:
 INT | STRING | DOUBLE
 ;



createtable :
//CREATE TABLE ID '(' column_def ')' ';'
//CREATE TABLE STUDENT(ID INT PRIMARY KEY, NAME STRING , AGE INT);
'CREATE TABLE' tablename '('  tablecolumns 'PRIMARY KEY' (',' tablecolumns)* ')' semicolonclosercreate
;

createindex :
'CREATE INDEX' indexname 'ON' tablename '(' indexCols')' semicoloncloserindex
;

insertValuePair:
insertColValues (',' insertColValues)*
;

insert:
'INSERT INTO' tablename '(' insertColNames (',' insertColNames)* ')' 'VALUES' '(' insertValuePair ')' (',' '(' insertValuePair ')')* semicoloncloserinsert
;



