package org.example;

import java.util.List;

class CallInst extends Inst {

    Register dst;
    Register func;
    List<Register> args;

    CallInst(Register dst, Register func, List<Register> args) {
        this.dst = dst;
        this.func = func;
        this.args = args;
    }

    public boolean isTerminator() {
        return false;
    }

    @Override
    public String stringify() {
        StringBuilder sb = new StringBuilder();
        sb.append("call ");
        sb.append(dst);
        sb.append(", ");
        sb.append(func);
        sb.append(", ");
        sb.append(args);
        return sb.toString();
    }
}
