package org.example;

import java.util.List;

public class App {

    public static void main(String[] args) {
        String input = "let x = 4\n{ x = 4\ny = 4 }";
        Lexer lexer = new Lexer(input);
        List<Token> tokens = lexer.tokenize();
        tokens.stream().forEach(System.out::println);
        Parser parser = new Parser(tokens);
        List<Stmt> sms = parser.parse();
        TypeChecker tc = new TypeChecker();
        for (Stmt stmt : sms) {
            tc.visitStmt(stmt);
        }
        for (Stmt stmt : sms) {
            System.out.println(stmt.stringify());
        }
    }
}
