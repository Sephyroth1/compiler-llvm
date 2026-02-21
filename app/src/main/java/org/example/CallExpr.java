package org.example;

import java.util.ArrayList;
import java.util.List;

class CallExpr extends Expr {

    Expr callee;
    List<Expr> args;

    CallExpr(Expr callee, List<Expr> args) {
        this.callee = callee;
        this.args = args;
    }

    @Override
    Register lower(Builder b) {
        Callable fn = b.resolveCallable(callee);
        List<Register> argRegs = new ArrayList<>();
        for (Expr arg : args) {
            argRegs.add(arg.lower(b));
        }
        return fn.emitCall(b, argRegs);
    }

    @Override
    public String stringify() {
        StringBuilder sb = new StringBuilder();
        sb.append(callee.stringify()).append("(");
        for (int i = 0; i < args.size(); i++) {
            sb.append(args.get(i).stringify());
            if (i < args.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
