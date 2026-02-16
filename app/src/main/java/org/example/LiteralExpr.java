package org.example;

class LiteralExpr extends Expr {

    Object value;

    LiteralExpr(Object value) {
        this.value = value;
    }

    @Override
    String stringify() {
        return value.toString();
    }
}
