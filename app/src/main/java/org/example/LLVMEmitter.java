package org.example;

class LLVMEmitter {

    String emit(Function fn) {
        StringBuilder sb = new StringBuilder();

        sb.append("define i32 @main() {\n");

        for (BasicBlock bb : fn.blocks) {
            sb.append(bb.name).append(":\n");

            for (Inst inst : bb.instructions)
                sb.append("  ").append(emitInst(inst)).append("\n");
        }

        sb.append("}\n");
        return sb.toString();
    }

    String emitInst(Inst i) {
        // big instanceof switch later
        return "; TODO";
    }
}
