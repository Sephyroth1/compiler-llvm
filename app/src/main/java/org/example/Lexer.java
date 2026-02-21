package org.example;

import java.util.ArrayList;
import java.util.List;

class Lexer {

    String input;
    int pos;
    int line;

    Lexer(String input) {
        this.input = input;
        this.pos = 0;
        this.line = 1;
    }

    char peek() {
        if (pos >= input.length()) return '\0';
        return input.charAt(pos);
    }

    char peekNext() {
        if (pos + 1 >= input.length()) return '\0';
        return input.charAt(pos + 1);
    }

    char next() {
        if (pos >= input.length()) return '\0';
        return input.charAt(pos++);
    }

    List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();

        while (pos < input.length()) {
            char c = peek();

            // -------- NEWLINE ----------
            if (c == '\n') {
                line++;
                next();
                continue;
            }

            // -------- WHITESPACE ----------
            if (Character.isWhitespace(c)) {
                next();
                continue;
            }

            // -------- NUMBER ----------
            if (Character.isDigit(c)) {
                StringBuilder sb = new StringBuilder();
                while (Character.isDigit(peek())) {
                    sb.append(next());
                }
                tokens.add(new Token(TokenType.NUMBER, sb.toString(), line));
                continue;
            }

            // -------- IDENTIFIER / KEYWORD ----------
            if (Character.isLetter(c) || c == '_') {
                StringBuilder sb = new StringBuilder();
                while (Character.isLetterOrDigit(peek()) || peek() == '_') {
                    sb.append(next());
                }

                String word = sb.toString();

                switch (word) {
                    case "true":
                    case "false":
                        tokens.add(new Token(TokenType.BOOLEAN, word, line));
                        break;
                    case "let":
                        tokens.add(new Token(TokenType.LET, word, line));
                        break;
                    case "if":
                        tokens.add(new Token(TokenType.IF, word, line));
                        break;
                    case "else":
                        tokens.add(new Token(TokenType.ELSE, word, line));
                        break;
                    case "return":
                        tokens.add(new Token(TokenType.RETURN, word, line));
                        break;
                    default:
                        tokens.add(new Token(TokenType.IDENTIFIER, word, line));
                }
                continue;
            }

            // -------- OPERATORS ----------
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
                // = and ==
                case '=':
                    if (peekNext() == '=') {
                        next();
                        next();
                        tokens.add(new Token(TokenType.EQEQ, "==", line));
                    } else {
                        next();
                        tokens.add(new Token(TokenType.EQUAL, "=", line));
                    }
                    break;
                case ',':
                    tokens.add(new Token(TokenType.COMMA, ",", line));
                    next();
                    break;
                // ! and !=
                case '!':
                    if (peekNext() == '=') {
                        next();
                        next();
                        tokens.add(new Token(TokenType.NOT_EQ, "!=", line));
                    } else {
                        next();
                        tokens.add(new Token(TokenType.BANG, "!", line));
                    }
                    break;
                // < and <=
                case '<':
                    if (peekNext() == '=') {
                        next();
                        next();
                        tokens.add(new Token(TokenType.LESS_EQ, "<=", line));
                    } else {
                        next();
                        tokens.add(new Token(TokenType.LESS, "<", line));
                    }
                    break;
                // > and >=
                case '>':
                    if (peekNext() == '=') {
                        next();
                        next();
                        tokens.add(new Token(TokenType.GREATER_EQ, ">=", line));
                    } else {
                        next();
                        tokens.add(new Token(TokenType.GREATER, ">", line));
                    }
                    break;
                // &&
                case '&':
                    if (peekNext() == '&') {
                        next();
                        next();
                        tokens.add(new Token(TokenType.AND, "&&", line));
                    } else {
                        throw new IllegalArgumentException(
                            "Unexpected character '&' at line " + line
                        );
                    }
                    break;
                // ||
                case '|':
                    if (peekNext() == '|') {
                        next();
                        next();
                        tokens.add(new Token(TokenType.OR, "||", line));
                    } else {
                        throw new IllegalArgumentException(
                            "Unexpected character '|' at line " + line
                        );
                    }
                    break;
                // BLOCKS
                case '{':
                    tokens.add(new Token(TokenType.LEFT_PAREN, "{", line));
                    next();
                    break;
                case '}':
                    tokens.add(new Token(TokenType.RIGHT_PAREN, "}", line));
                    next();
                    break;
                case '(':
                    tokens.add(new Token(TokenType.LEFT_BRACKET, "(", line));
                    next();
                    break;
                case ')':
                    tokens.add(new Token(TokenType.RIGHT_BRACKET, ")", line));
                    next();
                    break;
                default:
                    throw new IllegalArgumentException(
                        "Unexpected character '" + c + "' at line " + line
                    );
            }
        }

        tokens.add(new Token(TokenType.EOF, "", line));
        return tokens;
    }
}
