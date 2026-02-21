package org.example;

class WhileStmt extends Stmt {

    Expr cond;
    BlockStmt body;

    WhileStmt(Expr cond, BlockStmt body) {
        this.cond = cond;
        this.body = body;
    }

    @Override
    public String stringify() {
        return "WhileStmt{" + "cond=" + cond + ", body=" + body + '}';
    }
}
