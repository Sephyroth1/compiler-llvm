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
        StringBuilder sb = new StringBuilder();
        sb.append("%").append(dst.id).append(" = phi i32 ");

        boolean first = true;
        for (Map.Entry<BasicBlock, Register> e : inputs.entrySet()) {
            if (!first) sb.append(", ");
            first = false;

            sb
                .append("[ %")
                .append(e.getValue().id)
                .append(", %")
                .append(e.getKey().name)
                .append(" ]");
        }

        return sb.toString();
    }
}
