package org.example;

class BinaryExpr extends Expr {

    Expr left;
    Token operator;
    Expr right;

    public BinaryExpr(Expr left, Token operator, Expr right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    String stringify() {
        return (
            "(" +
            operator.getLexeme() +
            " " +
            left.stringify() +
            ":" +
            left.type +
            " " +
            right.stringify() +
            ":" +
            right.type +
            ")"
        );
    }
}
