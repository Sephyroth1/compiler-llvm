package org.example;

class Builder {

    BasicBlock current;
    Function fn;
    int id = 0;
    Environment env;

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

    Function lowerFunction(FnDecl fnDecl) {
        Builder b = new Builder();
        b.fn = new Function(fnDecl.name);
        b.env = new Environment();
        BasicBlock entry = b.newBlock("entry");
        b.positionAtEnd(entry);
        lowerStmt(fnDecl.body, b);
        if (b.current.getTerminator() == null) {
            Register zero = b.constInt(0);
            b.ret(zero);
        } else {
            System.out.println(
                "Block " +
                    b.current.getTerminator().stringify() +
                    " is terminated"
            );
        }
        return b.fn;
    }

    void lowerStmt(Stmt body, Builder b) {
        if (body instanceof BlockStmt) {
            b.pushScope();
            for (Stmt stmt : ((BlockStmt) body).getStmts()) {
                lowerStmt(stmt, b);
            }
            b.popScope();
        }

        if (body instanceof IfStmt i) {
            lowerIfStmt(i, b);
            return;
        }

        if (body instanceof LetStmt l) {
            lowerLetStmt(l, b);
            return;
        }

        if (body instanceof ExprStmt e) {
            e.getExpr().lower(b);
            return;
        }
    }

    void lowerIfStmt(IfStmt i, Builder b) {
        BasicBlock thenBlock = b.newBlock("if.then");
        BasicBlock elseBlock = b.newBlock("if.else");
        BasicBlock mergeBlock = b.newBlock("if.merge");

        // 1. evaluate condition in current block
        Register cond = i.condition.lower(b);

        // 2. branch based on condition
        b.branchInst(cond, thenBlock, elseBlock);

        // ----- THEN -----
        b.positionAtEnd(thenBlock);
        lowerStmt(i.thenBranch, b);
        if (b.current.getTerminator() == null) b.jumpInst(mergeBlock);
        BasicBlock thenEnd = b.current;

        // ----- ELSE -----
        b.positionAtEnd(elseBlock);
        if (i.elseBranch != null) lowerStmt(i.elseBranch, b);
        if (b.current.getTerminator() == null) b.jumpInst(mergeBlock);
        BasicBlock elseEnd = b.current;

        // ----- MERGE -----
        b.positionAtEnd(mergeBlock);

        // (phi insertion happens later when variables differ)
    }

    void lowerLetStmt(LetStmt l, Builder b) {
        Register val = l.getValue().lower(b);
        b.env.define(l.getName(), val);
    }
}
