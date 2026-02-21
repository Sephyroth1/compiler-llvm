package org.example;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TypeChecker {

    private final Deque<Map<String, Symbol>> scopes = new ArrayDeque<>();

    public TypeChecker() {
        scopes.push(new HashMap<>());

        declare(
            "print",
            new FunctionSymbol("print", List.of(Type.INT), Type.VOID)
        );
    }

    /* ---------------- EXPRESSIONS ---------------- */

    Type visitExpr(Expr e) {
        if (e instanceof CallExpr c) return visitCall(c);
        if (e instanceof LiteralExpr l) return visitLiteral(l);
        if (e instanceof BinaryExpr b) return visitBinary(b);
        if (e instanceof UnaryExpr u) return visitUnary(u);
        if (e instanceof AssignExpr a) return visitAssign(a);
        if (e instanceof LogicalAndExpr l) return visitLogicalAnd(l);
        if (e instanceof LogicalOrExpr l) return visitLogicalOr(l);
        if (e instanceof IdentifierExpr i) return visitIdentifier(i);

        throw new IllegalArgumentException("Unknown expression: " + e);
    }

    private Type visitLiteral(LiteralExpr e) {
        if (e.value instanceof Integer) return e.type = Type.INT;
        if (e.value instanceof Boolean) return e.type = Type.BOOL;
        throw new IllegalArgumentException("Invalid literal");
    }

    private Type visitIdentifier(IdentifierExpr e) {
        Symbol s = lookup(e.getName());

        if (s instanceof FunctionSymbol f) throw new IllegalArgumentException(
            "Function used as value: " + e.getName()
        );
        VarSymbol v = (VarSymbol) s;
        return e.type = v.type;
    }

    private Type visitAssign(AssignExpr e) {
        Symbol s = lookup(e.name);

        if (!(s instanceof VarSymbol v)) throw new IllegalArgumentException(
            "Cannot assign to function: " + e.name
        );

        Type valueType = visitExpr(e.value);

        if (valueType != v.type) throw new IllegalArgumentException(
            "Type mismatch assigning " + valueType + " to " + v.type
        );

        return e.type = v.type;
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
            case ADD:
            case SUB:
            case MUL:
            case DIV:
                if (left == Type.INT && right == Type.INT) return e.type =
                    Type.INT;
                break;
            case EQEQ:
            case NOT_EQ:
                if (left == right) return e.type = Type.BOOL;
                break;
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

    private Type visitCall(CallExpr e) {
        if (
            !(e.callee instanceof IdentifierExpr id)
        ) throw new IllegalArgumentException("Invalid call target");

        // resolve symbol directly
        Symbol s = lookup(id.getName());

        if (
            !(s instanceof FunctionSymbol f)
        ) throw new IllegalArgumentException(id.getName() + " is not callable");

        // check args
        if (
            e.args.size() != f.params.size()
        ) throw new IllegalArgumentException("Wrong number of arguments");

        for (int i = 0; i < e.args.size(); i++) {
            Type argType = visitExpr(e.args.get(i));
            if (argType != f.params.get(i)) throw new IllegalArgumentException(
                "Argument mismatch"
            );
        }

        return e.type = f.returnType;
    }

    /* ---------------- STATEMENTS ---------------- */

    void visitStmt(Stmt s) {
        if (s instanceof ExprStmt e) {
            visitExpr(e.getExpr());
            return;
        }

        if (s instanceof LetStmt l) {
            Type t = visitExpr(l.getValue());
            declare(l.getName(), new VarSymbol(l.getName(), t));
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

        if (s instanceof RetStmt r) {
            visitExpr(r.getExpr());
            return;
        }

        throw new IllegalArgumentException("Unknown statement: " + s);
    }

    private void visitIfStmt(IfStmt i) {
        Type cond = visitExpr(i.condition);
        if (cond != Type.BOOL) throw new IllegalArgumentException(
            "If condition must be boolean"
        );

        visitBlock(i.thenBranch);
        if (i.elseBranch != null) visitBlock(i.elseBranch);
    }

    private void visitBlock(BlockStmt b) {
        scopes.push(new HashMap<>());

        for (Stmt stmt : b.getStmts()) visitStmt(stmt);

        scopes.pop();
    }

    /* ---------------- SYMBOL TABLE ---------------- */

    private void declare(String name, Symbol symbol) {
        Map<String, Symbol> current = scopes.peek();

        if (current.containsKey(name)) throw new IllegalArgumentException(
            "Redeclaration of " + name
        );

        current.put(name, symbol);
    }

    private Symbol lookup(String name) {
        for (Map<String, Symbol> scope : scopes) {
            Symbol s = scope.get(name);
            if (s != null) return s;
        }
        throw new IllegalArgumentException("Undefined symbol: " + name);
    }
}
