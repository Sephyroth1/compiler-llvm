package org.example;

import java.util.List;

public class App {

    public static void main(String[] args) {
        String input = """
            let a = 3
            let b = 4
            let c = a + b
            let d = a < b
            let e = d && true
            let f = !a == b
            a = 5
            """;
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
