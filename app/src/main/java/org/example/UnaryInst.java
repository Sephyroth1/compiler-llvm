package org.example;

class UnaryInst extends Inst {

    Register dst;
    Op op;
    Register src;

    UnaryInst(Register dst, Op op, Register src) {
        this.dst = dst;
        this.op = op;
        this.src = src;
    }

    boolean isTerminator() {
        return false;
    }

    @Override
    public String stringify() {
        return dst + " = " + op + " " + src;
    }
}
