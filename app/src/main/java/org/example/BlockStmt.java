package org.example;

import java.util.List;

class BlockStmt extends Stmt {

    private List<Stmt> stmts;

    public BlockStmt(List<Stmt> stmts) {
        this.stmts = stmts;
    }

    public List<Stmt> getStmts() {
        return stmts;
    }

    @Override
    public String stringify() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        for (Stmt stmt : stmts) {
            sb.append("    ").append(stmt.stringify()).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
}
