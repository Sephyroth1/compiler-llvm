package org.example;

class UnaryExpr extends Expr {

    Token op;
    Expr expr;

    UnaryExpr(Token op, Expr expr) {
        this.op = op;
        this.expr = expr;
    }

    @Override
    String stringify() {
        return (
            "(" +
            op.getLexeme() +
            " " +
            expr.stringify() +
            ":" +
            expr.type +
            ")"
        );
    }
}
