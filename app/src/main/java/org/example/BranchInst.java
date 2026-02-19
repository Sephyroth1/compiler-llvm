package org.example;

class BranchInst extends Inst {

    Register cond;
    BasicBlock thenB, elseB;

    BranchInst(Register cond, BasicBlock thenB, BasicBlock elseB) {
        this.cond = cond;
        this.thenB = thenB;
        this.elseB = elseB;
    }

    @Override
    boolean isTerminator() {
        return true;
    }

    @Override
    String stringify() {
        return (
            "Register: " +
            cond.stringify() +
            " Then: " +
            thenB.stringify() +
            " Else: " +
            elseB.stringify()
        );
    }
}
