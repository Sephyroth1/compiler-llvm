package org.example;

class UnaryExpr extends Expr {

    Op op;
    Expr expr;

    UnaryExpr(Op op, Expr expr) {
        this.op = op;
        this.expr = expr;
    }

    @Override
    public Register lower(Builder builder) {
        Register exprReg = expr.lower(builder);
        switch (op) {
            case NEG:
                return builder.unaryInst(Op.NEG, exprReg);
            case NOT:
                return builder.unaryInst(Op.NOT, exprReg);
            case PLUS:
                return exprReg;
            default:
                throw new IllegalArgumentException(
                    "Unsupported unary operator: " + op
                );
        }
    }

    @Override
    String stringify() {
        return ("(" + op + " " + expr.stringify() + ":" + expr.type + ")");
    }
}
