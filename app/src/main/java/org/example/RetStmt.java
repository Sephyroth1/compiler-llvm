package org.example;

class RetStmt extends Stmt {

    private Expr expr;

    public RetStmt(Expr expr) {
        this.expr = expr;
    }

    public Expr getExpr() {
        return expr;
    }

    @Override
    public String stringify() {
        return "ret " + expr.stringify();
    }
}
