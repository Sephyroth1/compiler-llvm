package org.example;

class LogicalAndExpr extends Expr {

    private Expr left;
    private Expr right;

    public LogicalAndExpr(Expr left, Expr right) {
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
        BasicBlock rhs = b.newBlock("and.rhs");
        BasicBlock falseBlock = b.newBlock("and.false");
        BasicBlock merge = b.newBlock("and.merge");

        Register l = left.lower(b);

        // if l true → evaluate rhs
        // if l false → result false
        b.branchInst(l, rhs, falseBlock);

        // RHS
        b.positionAtEnd(rhs);
        Register r = right.lower(b);
        b.jumpInst(merge);
        BasicBlock rhsEnd = b.current;

        // FALSE path
        b.positionAtEnd(falseBlock);
        Register f = b.constBool(false);
        b.jumpInst(merge);
        BasicBlock falseEnd = b.current;

        // MERGE
        b.positionAtEnd(merge);
        PhiInst result = b.phiInst(Type.BOOL);
        result.addIncoming(r, rhsEnd);
        result.addIncoming(f, falseEnd);
        return result.dst;
    }

    @Override
    public String stringify() {
        return "(" + left.stringify() + " && " + right.stringify() + ")";
    }
}
