package com.grammar;

import com.grammar.com.grammar.sql_gParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;


//import com.grammar.sql_gParser;
import com.grammar.com.grammar.sql_gLexer;

import java.io.IOException;

import static org.antlr.v4.runtime.CharStreams.fromFileName;

public class main2 {
    public static void main(String[] args) {
        try{
            String source = "C:\\Users\\gchehata\\IdeaProjects\\DB2\\src\\main\\java\\com\\grammar\\test.txt";
            CharStream cs = fromFileName(source);
            System.out.println(cs);
            sql_gLexer lexer = new sql_gLexer(cs);
            CommonTokenStream token = new CommonTokenStream(lexer);
            sql_gParser parser = new sql_gParser(token);
            ParseTree tree = parser.program();

            Main visitor = new Main();
            visitor.visit(tree);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}