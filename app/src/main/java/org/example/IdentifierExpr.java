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
    public String stringify() {
        return name;
    }
}
