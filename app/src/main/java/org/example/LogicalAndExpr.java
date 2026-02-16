package org.example;

class LogicalAndExpr extends Expr {

    private Expr left;
    private Expr right;

    public LogicalAndExpr(Expr left, Expr right) {
        this.left = left;
        this.right = right;
    }

    public Expr getLeft() {
        return left;
    }

    public Expr getRight() {
        return right;
    }

    @Override
    public String stringify() {
        return "(" + left.stringify() + " && " + right.stringify() + ")";
    }
}
