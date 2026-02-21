package org.example;

class JmpInst extends Inst {

    BasicBlock target;

    JmpInst(BasicBlock target) {
        this.target = target;
    }

    @Override
    boolean isTerminator() {
        return true;
    }

    @Override
    public String stringify() {
        return "br label %" + target.name;
    }
}
