package org.example;

class BinaryExpr extends Expr {

    Expr left;
    Op operator;
    Expr right;

    public BinaryExpr(Expr left, Op operator, Expr right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public Register lower(Builder builder) {
        Register leftReg = left.lower(builder);
        Register rightReg = right.lower(builder);
        return builder.binInst(operator, leftReg, rightReg);
    }

    @Override
    String stringify() {
        return (
            "(" +
            operator +
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
