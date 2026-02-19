package org.example;

class AssignExpr extends Expr {

    String name;
    Expr value;

    AssignExpr(String name, Expr value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public Register lower(Builder builder) {
        Register reg = value.lower(builder);
        builder.env.define(name, reg);
        return reg;
    }

    @Override
    public String stringify() {
        return String.format("%s = %s)", name, value.stringify());
    }
}
