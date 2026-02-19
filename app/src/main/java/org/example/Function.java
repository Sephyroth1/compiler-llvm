package org.example;

import java.util.ArrayList;
import java.util.List;

class Function {

    BasicBlock entry;
    List<BasicBlock> blocks;

    public Function(String name) {
        this.entry = new BasicBlock(name);
        this.blocks = new ArrayList<>();
    }

    void print() {
        System.out.println("define i32 @main {");
        System.out.println("entry:");
        for (BasicBlock block : blocks) {
            System.out.println("   " + block.stringify());
        }
        System.out.println("}");
    }

    String emitter() {
        StringBuilder sb = new StringBuilder();
        sb.append("define i32 @main() {");
        sb.append("\nentry:");
        for (BasicBlock block : blocks) {
            sb.append("\n   ").append(block.stringify());
        }
        sb.append("\n}");
        return sb.toString();
    }
}
