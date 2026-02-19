package org.example;

import java.util.HashMap;
import java.util.Map;

class PhiInst extends Inst {

    Register dst;
    Map<BasicBlock, Register> inputs;

    PhiInst(Register dst) {
        this.dst = dst;
        this.inputs = new HashMap<>();
    }

    void addIncoming(Register src, BasicBlock bb) {
        inputs.put(bb, src);
    }

    @Override
    boolean isTerminator() {
        return false;
    }

    @Override
    public String stringify() {
        return "PhiInst " + dst.stringify() + " " + inputs;
    }
}
