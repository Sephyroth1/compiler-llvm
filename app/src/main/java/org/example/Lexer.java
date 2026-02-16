package org.example;

import java.util.ArrayList;
import java.util.List;

class Lexer {

    String input;
    int pos;

    Lexer(String input) {
        this.input = input;
        this.pos = 0;
    }

    char peek() {
        if (pos >= input.length()) return '\0';
        return input.charAt(pos);
    }

    char next() {
        if (pos >= input.length()) return '\0';
        return input.charAt(pos++);
    }

    List<Token> tokenize() {
        int line = 0;
        List<Token> tokens = new ArrayList<>();
        while (pos < input.length()) {
            char c = peek();
            StringBuilder sb = new StringBuilder();
            if (Character.isDigit(c)) {
                while (Character.isDigit(peek())) {
                    sb.append(next());
                }
                tokens.add(new Token(TokenType.NUMBER, sb.toString(), line));
            } else if (Character.isAlphabetic(c)) {
                while (Character.isAlphabetic(peek())) {
                    sb.append(next());
                }
                if (
                    sb.toString().equals("true") ||
                    sb.toString().equals("false")
                ) {
                    tokens.add(
                        new Token(TokenType.BOOLEAN, sb.toString(), line)
                    );
                } else if (sb.toString().equals("let")) {
                    tokens.add(new Token(TokenType.LET, sb.toString(), line));
                } else {
                    tokens.add(
                        new Token(TokenType.IDENTIFIER, sb.toString(), line)
                    );
                }
            } else if (Character.isWhitespace(c)) {
                next();
            } else if (
                !Character.isAlphabetic(c) && !Character.isWhitespace(c)
            ) {
                switch (c) {
                    case '+':
                        tokens.add(new Token(TokenType.PLUS, "+", line));
                        next();
                        break;
                    case '-':
                        tokens.add(new Token(TokenType.MINUS, "-", line));
                        next();
                        break;
                    case '*':
                        tokens.add(new Token(TokenType.TIMES, "*", line));
                        next();
                        break;
                    case '/':
                        tokens.add(new Token(TokenType.DIVIDE, "/", line));
                        next();
                        break;
                    case '=':
                        tokens.add(new Token(TokenType.EQUAL, "=", line));
                        next();
                        break;
                    case '{':
                        tokens.add(new Token(TokenType.LEFT_PAREN, "{", line));
                        next();
                        break;
                    case '}':
                        tokens.add(new Token(TokenType.RIGHT_PAREN, "}", line));
                        next();
                        break;
                    default:
                        throw new IllegalArgumentException(
                            "Unexpected character: " + c
                        );
                }
            } else if (Character.isWhitespace(c)) {
                if (c == '\n') line++;
                next();
                continue;
            } else if (c == '\n') {
                line++;
            }
        }
        tokens.add(new Token(TokenType.EOF, "", line));
        return tokens;
    }
}
