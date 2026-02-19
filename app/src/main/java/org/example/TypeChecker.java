package org.example;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

class TypeChecker {

    private final Deque<Map<String, Type>> scopes = new ArrayDeque<>();

    public TypeChecker() {
        scopes.push(new HashMap<>()); // global scope
    }

    /* -------------------- EXPRESSIONS -------------------- */

    Type visitExpr(Expr e) {
        if (e instanceof LiteralExpr l) return visitLiteral(l);
        if (e instanceof BinaryExpr b) return visitBinary(b);
        if (e instanceof UnaryExpr u) return visitUnary(u);
        if (e instanceof IdentifierExpr i) return visitIdentifier(i);
        if (e instanceof AssignExpr a) return visitAssign(a);
        if (e instanceof LogicalAndExpr l) return visitLogicalAnd(l);
        if (e instanceof LogicalOrExpr l) return visitLogicalOr(l);

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
        Type existing = lookup(e.name);
        Type valueType = visitExpr(e.value);

        if (existing != valueType) throw new IllegalArgumentException(
            "Type mismatch assigning " + valueType + " to " + existing
        );

        return e.type = existing;
    }

    private Type visitUnary(UnaryExpr e) {
        Type operand = visitExpr(e.expr);

        switch (e.op) {
            case NEG:
                if (operand == Type.INT) return e.type = Type.INT;
                break;
            case NOT:
                if (operand == Type.BOOL) return e.type = Type.BOOL;
                break;
        }

        throw new IllegalArgumentException("Invalid unary operator");
    }

    private Type visitBinary(BinaryExpr e) {
        Type left = visitExpr(e.left);
        Type right = visitExpr(e.right);

        switch (e.operator) {
            // arithmetic
            case ADD:
            case SUB:
            case MUL:
            case DIV:
                if (left == Type.INT && right == Type.INT) return e.type =
                    Type.INT;
                break;
            // equality
            case EQEQ:
            case NOT_EQ:
                if (left == right) return e.type = Type.BOOL;
                break;
            // comparisons
            case LESS:
            case LESS_EQ:
            case GREATER:
            case GREATER_EQ:
                if (left == Type.INT && right == Type.INT) return e.type =
                    Type.BOOL;
                break;
        }

        throw new IllegalArgumentException("Invalid binary expression");
    }

    private Type visitLogicalAnd(LogicalAndExpr e) {
        Type left = visitExpr(e.getLeft());
        Type right = visitExpr(e.getRight());

        if (
            left != Type.BOOL || right != Type.BOOL
        ) throw new IllegalArgumentException("Type mismatch in logical AND");

        return e.type = Type.BOOL;
    }

    private Type visitLogicalOr(LogicalOrExpr e) {
        Type left = visitExpr(e.getLeft());
        Type right = visitExpr(e.getRight());

        if (
            left != Type.BOOL || right != Type.BOOL
        ) throw new IllegalArgumentException("Type mismatch in logical OR");

        return e.type = Type.BOOL;
    }

    /* -------------------- STATEMENTS -------------------- */

    void visitStmt(Stmt s) {
        if (s instanceof ExprStmt e) {
            visitExpr(e.getExpr());
            return;
        }

        if (s instanceof LetStmt l) {
            Type t = visitExpr(l.getValue());
            declare(l.getName(), t);
            return;
        }

        if (s instanceof BlockStmt b) {
            visitBlock(b);
            return;
        }

        if (s instanceof IfStmt i) {
            visitIfStmt(i);
            return;
        }
        throw new IllegalArgumentException("Unknown statement: " + s);
    }

    private void visitIfStmt(IfStmt i) {
        visitExpr(i.condition);
        visitBlock(i.thenBranch);
        if (i.elseBranch != null) visitBlock(i.elseBranch);
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
