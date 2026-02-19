package org.example;

public class ConstInst extends Inst {

    Register dst;
    int value;

    ConstInst(Register dst, int value) {
        this.dst = dst;
        this.value = value;
    }

    @Override
    boolean isTerminator() {
        return false;
    }

    @Override
    public String stringify() {
        return "%" + dst.id + " = " + "add i32 0, " + value;
    }
}
