package org.example;

import java.util.ArrayList;
import java.util.List;

class BasicBlock {

    String name;
    List<Inst> instructions;
    List<BasicBlock> successors;
    List<BasicBlock> predecessors;

    BasicBlock(String name) {
        this.name = name;
        this.instructions = new ArrayList<>();
        this.successors = new ArrayList<>();
        this.predecessors = new ArrayList<>();
    }

    void addInstruction(Inst inst) {
        instructions.add(inst);
    }

    void addSuccessor(BasicBlock block) {
        successors.add(block);
    }

    void addPredecessor(BasicBlock block) {
        predecessors.add(block);
    }

    void addInstructionFront(Inst inst) {
        instructions.add(0, inst);
    }

    Inst getTerminator() {
        if (instructions.isEmpty()) return null;
        Inst terminator = instructions.get(instructions.size() - 1);
        return (terminator.isTerminator()) ? terminator : null;
    }

    String stringify() {
        StringBuilder sb = new StringBuilder();
        for (Inst inst : instructions) {
            sb.append(inst.stringify());
            sb.append("\n");
            sb.append("   ");
        }

        // for (BasicBlock successor : successors) {
        //     sb.append(successor.name).append(" ");
        //     sb.append("\n");
        // }

        // for (BasicBlock predecessor : predecessors) {
        //     sb.append(predecessor.name).append(" ");
        //     sb.append("\n");
        // }

        return sb.toString();
    }
}
