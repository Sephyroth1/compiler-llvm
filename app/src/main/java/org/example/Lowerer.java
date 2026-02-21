package org.example;

import java.util.HashSet;
import java.util.Set;

class Lowerer {

    Builder b;

    Lowerer(Builder b) {
        this.b = b;
    }

    Function lowerFunction(FnDecl fnDecl) {
        b.fn = new Function(fnDecl.name);
        b.env = new Environment();
        BasicBlock entry = b.newBlock("entry");
        b.positionAtEnd(entry);
        lowerStmt(fnDecl.body);
        if (b.current.getTerminator() == null) {
            Register zero = b.constInt(0);
            b.ret(zero);
        }
        return b.fn;
    }

    void lowerStmt(Stmt body) {
        if (body instanceof BlockStmt) {
            b.pushScope();
            for (Stmt stmt : ((BlockStmt) body).getStmts()) {
                lowerStmt(stmt);
            }
            b.popScope();
        }

        if (body instanceof IfStmt i) {
            lowerIfStmt(i);
            return;
        }

        if (body instanceof LetStmt l) {
            lowerLetStmt(l);
            return;
        }

        if (body instanceof ExprStmt e) {
            e.getExpr().lower(b);
            return;
        }

        if (body instanceof RetStmt r) {
            Register val = r.getExpr().lower(b);
            b.ret(val);
            return;
        }
    }

    void lowerIfStmt(IfStmt i) {
        BasicBlock thenBlock = b.newBlock("then");
        BasicBlock elseBlock = b.newBlock("else");
        BasicBlock mergeBlock = b.newBlock("merge");
        Environment before = b.env.copy();

        // 1. evaluate condition in current block
        Register cond = i.condition.lower(b);
        Register condBool = toBool(cond);
        b.branchInst(condBool, thenBlock, elseBlock);

        // ----- THEN -----
        b.positionAtEnd(thenBlock);
        b.env = before.copy();
        lowerStmt(i.thenBranch);

        if (b.current.getTerminator() == null) b.jumpInst(mergeBlock);

        Environment thenEnv = b.env.copy();
        BasicBlock thenEnd = b.current;

        // ----- ELSE -----
        b.positionAtEnd(elseBlock);
        b.env = before.copy();

        if (i.elseBranch != null) lowerStmt(i.elseBranch);
        if (b.current.getTerminator() == null) b.jumpInst(mergeBlock);
        Environment elseEnv = b.env.copy();
        BasicBlock elseEnd = b.current;

        // ----- MERGE -----
        b.positionAtEnd(mergeBlock);
        b.env = new Environment();

        Set<String> names = new HashSet<>();
        names.addAll(before.vars.keySet());
        names.addAll(thenEnv.vars.keySet());
        names.addAll(elseEnv.vars.keySet());

        for (String name : before.vars.keySet()) {
            Register t = thenEnv.lookup(name);
            Register e = elseEnv.lookup(name);

            if (t != e) {
                PhiInst phi = b.phiInst(t.type);
                phi.addIncoming(t, thenEnd);
                phi.addIncoming(e, elseEnd);
                b.env.define(name, phi.dst);
            } else {
                b.env.define(name, t);
            }
        }
    }

    void lowerLetStmt(LetStmt l) {
        Register val = l.getValue().lower(b);
        b.env.define(l.getName(), val);
    }

    Register toBool(Register cond) {
        if (cond.type == Type.BOOL) {
            return cond;
        }
        Register condBool = b.constInt(0);
        return b.icmpInst(cond, condBool, Op.NOT_EQ);
    }
}
