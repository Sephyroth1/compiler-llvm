package org.example;

import java.util.ArrayList;
import java.util.List;

class Parser {

    List<Token> tokens;
    int ind;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.ind = 0;
    }

    public Token peek() {
        if (ind >= tokens.size()) {
            return tokens.get(tokens.size() - 1);
        }
        return tokens.get(ind);
    }

    public Token next() {
        if (ind >= tokens.size()) {
            return tokens.get(tokens.size() - 1);
        }
        return tokens.get(ind++);
    }

    public Token peekNext() {
        if (ind + 1 >= tokens.size()) {
            return tokens.get(tokens.size() - 1);
        }
        return tokens.get(ind + 1);
    }

    public int infixBinding(Token t) {
        switch (t.getType()) {
            case PLUS:
            case MINUS:
                return 10;
            case TIMES:
            case DIVIDE:
                return 20;
            case EQUAL:
                return 5;
            default:
                return 0;
        }
    }

    public boolean isAtEnd() {
        return peek().getType() == TokenType.EOF;
    }

    public Expr parsePrefix() {
        Token t = peek();
        switch (t.getType()) {
            case MINUS:
                next();
                return new UnaryExpr(t, parseExpr(30));
            case PLUS:
                next();
                return new UnaryExpr(t, parseExpr(30));
            case NUMBER:
                next();
                return new LiteralExpr(Integer.parseInt(t.getLexeme()));
            case BOOLEAN:
                next();
                return new LiteralExpr(Boolean.parseBoolean(t.getLexeme()));
            case IDENTIFIER:
                next();
                return new IdentifierExpr(t.getLexeme());
            default:
                throw new IllegalArgumentException("Unexpected token: " + t);
        }
    }

    public Expr parseExpr(int minBind) {
        Expr left = parsePrefix();

        while (true) {
            Token t = peek();
            int binding = infixBinding(t);

            if (binding == 0 || binding <= minBind) break;

            next();

            if (t.getType() == TokenType.EQUAL) {
                if (
                    !(left instanceof IdentifierExpr id)
                ) throw new IllegalArgumentException(
                    "Invalid assignment target"
                );

                Expr right = parseExpr(binding - 1);
                left = new AssignExpr(id.getName(), right);
                continue;
            }

            Expr right = parseExpr(binding + 1);
            left = new BinaryExpr(left, t, right);
        }

        return left;
    }

    public List<Stmt> parse() {
        List<Stmt> stmts = new ArrayList<>();
        while (!isAtEnd()) {
            stmts.add(parseStmt());
        }
        return stmts;
    }

    public Stmt parseStmt() {
        Token t = peek();
        switch (t.getType()) {
            case LET:
                next();
                Token name = peek();
                if (
                    name.getType() != TokenType.IDENTIFIER
                ) throw new IllegalArgumentException(
                    "Expected identifier after 'let'"
                );
                next(); // consume identifier

                if (
                    peek().getType() != TokenType.EQUAL
                ) throw new IllegalArgumentException(
                    "Expected '=' after identifier"
                );
                next(); // consume '='

                Expr init = parseExpr(0);
                return new LetStmt(name.getLexeme(), init);
            case LEFT_PAREN:
                return parseBlockStmt();
            default:
                return new ExprStmt(parseExpr(0));
        }
    }

    public BlockStmt parseBlockStmt() {
        List<Stmt> stmts = new ArrayList<>();
        if (peek().getType() != TokenType.LEFT_PAREN) {
            throw new IllegalArgumentException(
                "Expected '{' at start of block"
            );
        }
        next(); // consume '('
        while (!isAtEnd() && peek().getType() != TokenType.RIGHT_PAREN) {
            stmts.add(parseStmt());
        }
        if (!isAtEnd()) next(); // consume '}'
        return new BlockStmt(stmts);
    }
}
