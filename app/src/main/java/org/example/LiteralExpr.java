package org.example;

class LiteralExpr extends Expr {

    Object value;

    LiteralExpr(Object value) {
        this.value = value;
    }

    @Override
    public Register lower(Builder builder) {
        if (value instanceof Integer i) {
            return builder.constInt(i);
        } else if (value instanceof Boolean i) {
            return builder.constBool(i);
        }
        throw new IllegalArgumentException(
            "Unsupported literal type: " + value.getClass().getName()
        );
    }

    @Override
    String stringify() {
        return value.toString();
    }
}
