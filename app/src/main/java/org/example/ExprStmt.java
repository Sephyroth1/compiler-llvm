package org.example;

class ExprStmt extends Stmt {

    private Expr expr;

    public ExprStmt(Expr expr) {
        this.expr = expr;
    }

    public Expr getExpr() {
        return expr;
    }

    @Override
    public String stringify() {
        return expr.stringify();
    }
}
