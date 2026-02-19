package org.example;

class LogicalOrExpr extends Expr {

    private Expr left;
    private Expr right;

    public LogicalOrExpr(Expr left, Expr right) {
        this.left = left;
        this.right = right;
    }

    public Expr getLeft() {
        return left;
    }

    public Expr getRight() {
        return right;
    }

    @Override
    public Register lower(Builder b) {
        BasicBlock rhs = b.newBlock("or.rhs");
        BasicBlock trueBlock = b.newBlock("or.true");
        BasicBlock merge = b.newBlock("or.merge");

        Register l = left.lower(b);

        b.branchInst(l, trueBlock, rhs);

        // RHS
        b.positionAtEnd(rhs);
        Register r = right.lower(b);
        b.jumpInst(merge);
        BasicBlock rhsEnd = b.current;

        // TRUE path
        b.positionAtEnd(trueBlock);
        Register t = b.constBool(true);
        b.jumpInst(merge);
        BasicBlock trueEnd = b.current;

        // MERGE
        b.positionAtEnd(merge);
        PhiInst result = b.phiInst(Type.BOOL);
        result.addIncoming(r, rhsEnd);
        result.addIncoming(t, trueEnd);
        return result.dst;
    }

    @Override
    public String stringify() {
        return "(" + left.stringify() + " || " + right.stringify() + ")";
    }
}
