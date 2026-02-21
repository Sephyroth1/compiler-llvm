package org.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class App {

    public static void main(String[] args) throws IOException {
        try {
            String fileName = args[0];
            Path p = Path.of(fileName);
            if (p == null) {
                throw new IllegalArgumentException("File path cannot be null");
            }
            List<String> lines = Files.readAllLines(p);
            System.out.println("File content:" + lines);
            String input = String.join("\n", lines);
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
            FnDecl fnDecl = new FnDecl(
                "main",
                new BlockStmt(sms),
                new ArrayList<Param>(),
                Type.VOID
            );
            Builder b = new Builder();
            b.callableMap.put("print", new BuiltInFunction());
            Lowerer lowerer = new Lowerer(b);
            Function fn = lowerer.lowerFunction(fnDecl);
            String code = fn.emitter();
            Files.writeString(Path.of("out.ll"), code);
        } catch (IOException e) {
            System.err.println(
                "Error reading file: " + e.getMessage() + " " + e
            );
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String stackTrace = sw.toString();
            System.err.println(
                "Error parsing or type checking: " +
                    e.getMessage() +
                    " " +
                    e +
                    " " +
                    stackTrace
            );
        }
    }
}
