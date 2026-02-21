package org.example;

class ICmpInst extends Inst {

    Register lhs, rhs, dst;
    Op op;

    ICmpInst(Register dst, Register lhs, Register rhs, Op op) {
        this.dst = dst;
        this.lhs = lhs;
        this.rhs = rhs;
        this.op = op;
    }

    @Override
    public boolean isTerminator() {
        return false;
    }

    @Override
    public String stringify() {
        return String.format(
            " %%%s = icmp %s i32 %%%s, %%%s",
            dst.id,
            op.llvm,
            lhs.id,
            rhs.id
        );
    }
}
