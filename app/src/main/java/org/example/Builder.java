package org.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Builder {

    BasicBlock current;
    Function fn;
    int id = 0;
    Environment env;
    Map<String, Callable> callableMap = new HashMap<>();

    Register newRegister(Type type) {
        return new Register(id++, type);
    }

    void pushScope() {
        env = new Environment(env);
    }

    void popScope() {
        env = env.parent;
    }

    // Register constInst(int v) {
    //     Register dst = newRegister(Type.INT);
    //     current.addInstruction(new ConstInst(dst, v));
    //     return dst;
    // }

    Register constInt(int v) {
        Register dst = newRegister(Type.INT);
        current.addInstruction(new ConstInst(dst, v));
        return dst;
    }

    Register constBool(boolean v) {
        Register dst = newRegister(Type.BOOL);
        current.addInstruction(new ConstInst(dst, v ? 1 : 0));
        return dst;
    }

    Register binInst(Op op, Register r1, Register r2) {
        Register dst = newRegister(Type.INT);
        current.addInstruction(new BinInst(dst, op, r1, r2));
        return dst;
    }

    void jumpInst(BasicBlock target) {
        current.addInstruction(new JmpInst(target));
        current.addSuccessor(target);
        target.addPredecessor(current);
    }

    void branchInst(
        Register cond,
        BasicBlock trueBlock,
        BasicBlock falseBlock
    ) {
        current.addInstruction(new BranchInst(cond, trueBlock, falseBlock));
        current.addSuccessor(trueBlock);
        current.addSuccessor(falseBlock);
        trueBlock.addPredecessor(current);
        falseBlock.addPredecessor(current);
    }

    void ret(Register val) {
        current.addInstruction(new RetInst(val));
    }

    BasicBlock newBlock(String name) {
        BasicBlock bk = new BasicBlock(name);
        fn.blocks.add(bk);
        return bk;
    }

    void positionAtEnd(BasicBlock block) {
        current = block;
    }

    boolean terminated() {
        return current.getTerminator() != null;
    }

    PhiInst phiInst(Type type) {
        Register dst = newRegister(type);
        PhiInst phi = new PhiInst(dst);
        current.addInstructionFront(phi);
        return phi;
    }

    Register unaryInst(Op op, Register src) {
        Register dst = newRegister(src.type);
        UnaryInst inst = new UnaryInst(dst, op, src);
        current.addInstruction(inst);
        return dst;
    }

    void verify() {
        for (BasicBlock bb : fn.blocks) {
            if (bb.getTerminator() == null) {
                throw new IllegalStateException(
                    "Block " + bb + " is not terminated"
                );
            }

            boolean seenNonPhi = false;
            for (Inst inst : bb.instructions) {
                if (inst instanceof PhiInst && !seenNonPhi) {
                    throw new IllegalStateException(
                        "Block " +
                            bb +
                            " has phi instruction before non-phi instruction"
                    );
                }
                if (!(inst instanceof PhiInst)) {
                    seenNonPhi = true;
                }
            }
        }
    }

    public Register icmpInst(Register lhs, Register rhs, Op op) {
        Register dst = newRegister(Type.BOOL);
        ICmpInst inst = new ICmpInst(dst, lhs, rhs, op);
        current.addInstruction(inst);
        return dst;
    }

    public Register callInst(Register func, List<Register> args) {
        Register dst = newRegister(func.type);
        CallInst inst = new CallInst(dst, func, args);
        current.addInstruction(inst);
        return dst;
    }

    public Register emitPrintf(Register value) {
        Register dst = newRegister(Type.VOID);
        PrintInst inst = new PrintInst(dst, value);
        current.addInstruction(inst);
        return dst;
    }

    Callable resolveCallable(Expr c) {
        if (c instanceof IdentifierExpr i) {
            String name = i.getName();
            Callable callable = callableMap.get(name);
            if (callable != null) {
                if (name.equals("print")) {
                    return new BuiltInFunction();
                }
                return callable;
            }
        }
        throw new IllegalArgumentException("Unknown callable: " + c);
    }
}
