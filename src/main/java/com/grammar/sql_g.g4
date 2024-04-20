grammar sql_g;

program :
createtable EOF
|createindex EOF
|insert EOF
|update EOF
|delete EOF
|select EOF
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

//INSERT INTO "Student" ("ID","NAME","AGE") VALUES (1,"Ibra",53);
insert:
'INSERT INTO' tablename '(' insertColNames (',' insertColNames)* ')' 'VALUES' '(' insertValuePair ')' (',' '(' insertValuePair ')')* semicoloncloserinsert
;

//HARRIDY GRAMMAR
attrname
 :STRING
 ;
values
: INT|DOUBLE|STRING
;
  eq
  : '='
  ;
   cluster
   : attrname eq values
   ;

name
 : STRING
 ;


   closerupdate
   :';'
   ;
   // UPDATE Student SET gpa=0.69 WHERE id=3;
  update
  : 'UPDATE' name 'SET' columndelete(COMMA columndelete)* 'WHERE' cluster closerupdate
  ;


  //DELETE FROM STUDENT WHERE id= 7 and name ="hamada"
    columndelete
    : attrname eq values
    ;
    and
    : 'and'|'AND'
    ;

    operaone
    : and
    ;
    closerdelete
    :';'
    ;
    delete
    : 'DELETE FROM' name('WHERE'columndelete (operaone columndelete)*)*closerdelete
    ;


columns
: attrname oper values
;

oper
: '='|'!='|'>'|'>='|'<'|'<='
;



opera
: 'AND'|'and'|'OR'|'or'|'XOR'|'xor'
;

closerselect
: ';'
;
 //SELECT * FROM Student WHERE id = 78 AND name = "hamada" Or name="Batates";
 select
  : 'SELECT * FROM' name( 'WHERE' columns (opera columns)* )?closerselect
  ;






