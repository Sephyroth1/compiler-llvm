package org.example;

class IdentifierExpr extends Expr {

    private String name;

    public IdentifierExpr(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public Register lower(Builder builder) {
        return builder.env.lookup(name);
    }

    @Override
    public String stringify() {
        return name;
    }
}
