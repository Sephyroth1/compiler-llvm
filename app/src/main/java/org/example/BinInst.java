package org.example;

class BinInst extends Inst {

    Op op;
    Register dst;
    Register lhs, rhs;

    BinInst(Register dst, Op op, Register lhs, Register rhs) {
        this.dst = dst;
        this.op = op;
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    boolean isTerminator() {
        return false;
    }

    @Override
    String stringify() {
        return (
            "%" + dst.id + " = " + op.llvm + " i32 %" + lhs.id + ", %" + rhs.id
        );
    }
}
