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
            case EQUAL:
                return 5; // assignment
            case OR:
                return 6; // ||
            case AND:
                return 7; // &&
            case EQEQ:
            case NOT_EQ:
                return 8; // == !=
            case LESS:
            case GREATER:
            case LESS_EQ:
            case GREATER_EQ:
                return 9; // comparisons
            case PLUS:
            case MINUS:
                return 10;
            case TIMES:
            case DIVIDE:
                return 20;
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
            case PLUS:
            case BANG:
                next();
                return new UnaryExpr(fromToken(t.getType()), parseExpr(30));
            case NUMBER:
                next();
                return new LiteralExpr(Integer.parseInt(t.getLexeme()));
            case BOOLEAN:
                next();
                return new LiteralExpr(Boolean.parseBoolean(t.getLexeme()));
            case IDENTIFIER:
                next();
                return new IdentifierExpr(t.getLexeme());
            case LEFT_BRACKET:
                return GroupedExpr();
            default:
                throw new IllegalArgumentException("Unexpected token: " + t);
        }
    }

    public Expr GroupedExpr() {
        next();
        Expr expr = parseExpr(0);
        if (
            peek().getType() != TokenType.RIGHT_BRACKET
        ) throw new IllegalArgumentException("Expected right bracket");
        next();
        return expr;
    }

    public Expr parseExpr(int minBind) {
        Expr left = parsePrefix();

        while (true) {
            Token t = peek();
            if (t.getType() == TokenType.LEFT_BRACKET) {
                left = new CallExpr(left, parseArguments());
                continue;
            }
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
            switch (t.getType()) {
                case AND:
                    left = new LogicalAndExpr(left, right);
                    break;
                case OR:
                    left = new LogicalOrExpr(left, right);
                    break;
                default:
                    left = new BinaryExpr(left, fromToken(t.getType()), right);
            }
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
            case IF:
                next(); // consume 'if'

                if (
                    peek().getType() != TokenType.LEFT_BRACKET
                ) throw new IllegalArgumentException("Expected '(' after 'if'");
                next(); // consume '('

                Expr condition = parseExpr(0);

                if (
                    peek().getType() != TokenType.RIGHT_BRACKET
                ) throw new IllegalArgumentException(
                    "Expected ')' after condition"
                );
                next(); // consume ')'

                BlockStmt thenBranch = parseBlockStmt();

                BlockStmt elseBranch = new BlockStmt(new ArrayList<>());
                if (peek().getType() == TokenType.ELSE) {
                    next();
                    elseBranch = parseBlockStmt();
                }

                return new IfStmt(condition, thenBranch, elseBranch);
            case RETURN:
                next();
                Expr value = parseExpr(0);
                return new RetStmt(value);
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

    Op fromToken(TokenType type) {
        switch (type) {
            case PLUS:
                return Op.ADD;
            case MINUS:
                return Op.SUB;
            case TIMES:
                return Op.MUL;
            case DIVIDE:
                return Op.DIV;
            case EQEQ:
                return Op.EQEQ;
            default:
                throw new IllegalArgumentException("Invalid token type" + type);
        }
    }

    public List<Expr> parseArguments() {
        List<Expr> args = new ArrayList<>();
        if (peek().getType() != TokenType.LEFT_BRACKET) {
            throw new IllegalArgumentException(
                "Expected '(' at start of arguments"
            );
        }
        next(); // consume '['
        while (!isAtEnd() && peek().getType() != TokenType.RIGHT_BRACKET) {
            args.add(parseExpr(0));
            if (peek().getType() == TokenType.COMMA) {
                next(); // consume ','
            }
        }
        if (!isAtEnd()) next(); // consume ')'
        return args;
    }

    public CallExpr parseCallExpr(Expr callee) {
        List<Expr> args = parseArguments();
        return new CallExpr(callee, args);
    }
}
