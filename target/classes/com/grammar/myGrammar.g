grammar DBApp;

insertIntoTable:
	'INSERT' 'INTO' tableName 'VALUES' '(' valuesList ')';

tableName: Identifier;

valuesList: value (',' value)*;

value:
	StringLiteral
	| IntegerLiteral
	| FloatLiteral
	| BooleanLiteral;

Identifier: [a-zA-Z_] [a-zA-Z_0-9]*;

StringLiteral: '\'' ~('\'' | '\r' | '\n')* '\'';

IntegerLiteral: [0-9]+;

FloatLiteral: [0-9]+ '.' [0-9]+;

BooleanLiteral: 'true' | 'false';

WS: [ \t\r\n]+ -> skip;