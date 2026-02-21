package org.example;

class PrintInst extends Inst {

    Register dst;
    Register value;

    public PrintInst(Register dst, Register value) {
        this.dst = dst;
        this.value = value;
    }

    @Override
    public boolean isTerminator() {
        return false;
    }

    @Override
    public String stringify() {
        return String.format("print %s", value);
    }
}
