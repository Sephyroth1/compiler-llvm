package org.example;

class RetInst extends Inst {

    Register val;

    RetInst(Register val) {
        this.val = val;
    }

    @Override
    boolean isTerminator() {
        return true;
    }

    @Override
    public String stringify() {
        return "ret i32 %" + val.id;
    }
}
