package org.example;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

class TypeChecker {

    private final Deque<Map<String, Type>> scopes = new ArrayDeque<>();

    public TypeChecker() {
        // global scope
        scopes.push(new HashMap<>());
    }

    /* -------------------- EXPRESSIONS -------------------- */

    Type visitExpr(Expr e) {
        if (e instanceof LiteralExpr l) return visitLiteral(l);
        if (e instanceof BinaryExpr b) return visitBinary(b);
        if (e instanceof UnaryExpr u) return visitUnary(u);
        if (e instanceof IdentifierExpr i) return visitIdentifier(i);
        if (e instanceof AssignExpr a) return visitAssign(a);

        throw new IllegalArgumentException("Unknown expression: " + e);
    }

    private Type visitLiteral(LiteralExpr e) {
        if (e.value instanceof Integer) return e.type = Type.INT;
        if (e.value instanceof Boolean) return e.type = Type.BOOL;
        throw new IllegalArgumentException("Invalid literal");
    }

    private Type visitIdentifier(IdentifierExpr e) {
        Type t = lookup(e.getName());
        e.type = t;
        return t;
    }

    private Type visitAssign(AssignExpr e) {
        Type existing = lookup(e.name); // must already exist
        Type valueType = visitExpr(e.value);

        if (existing != valueType) throw new IllegalArgumentException(
            "Type mismatch assigning " + valueType + " to " + existing
        );

        return e.type = existing;
    }

    private Type visitUnary(UnaryExpr e) {
        Type operand = visitExpr(e.expr);

        switch (e.op.getType()) {
            case MINUS:
                if (operand == Type.INT) return e.type = Type.INT;
        }

        throw new IllegalArgumentException("Invalid unary operator");
    }

    private Type visitBinary(BinaryExpr e) {
        Type left = visitExpr(e.left);
        Type right = visitExpr(e.right);

        switch (e.operator.getType()) {
            case PLUS:
            case MINUS:
            case TIMES:
            case DIVIDE:
                if (left == Type.INT && right == Type.INT) return e.type =
                    Type.INT;
        }

        throw new IllegalArgumentException("Invalid binary expression");
    }

    /* -------------------- STATEMENTS -------------------- */

    void visitStmt(Stmt s) {
        if (s instanceof ExprStmt e) {
            visitExpr(e.getExpr());
            return;
        }

        if (s instanceof LetStmt l) {
            declare(l.getName(), visitExpr(l.getValue()));
            return;
        }

        if (s instanceof BlockStmt b) {
            visitBlock(b);
            return;
        }

        throw new IllegalArgumentException("Unknown statement: " + s);
    }

    private void visitBlock(BlockStmt b) {
        scopes.push(new HashMap<>());

        for (Stmt stmt : b.getStmts()) visitStmt(stmt);

        scopes.pop();
    }

    /* -------------------- SYMBOL LOGIC -------------------- */

    private void declare(String name, Type type) {
        Map<String, Type> current = scopes.peek();

        if (current.containsKey(name)) throw new IllegalArgumentException(
            "Redeclaration of " + name
        );

        current.put(name, type);
    }

    private Type lookup(String name) {
        for (Map<String, Type> scope : scopes) {
            Type t = scope.get(name);
            if (t != null) return t;
        }
        throw new IllegalArgumentException("Undefined variable: " + name);
    }
}
