package org.example;

import java.util.List;

class IfStmt extends Stmt {

    Expr condition;
    BlockStmt thenBranch;
    BlockStmt elseBranch;

    public IfStmt(Expr condition, BlockStmt thenBranch, BlockStmt elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    @Override
    public String stringify() {
        return (
            "IfStmt{" +
            "condition=" +
            condition.stringify() +
            ", thenBranch=" +
            thenBranch.stringify() +
            ", elseBranch=" +
            elseBranch.stringify() +
            '}'
        );
    }
}
