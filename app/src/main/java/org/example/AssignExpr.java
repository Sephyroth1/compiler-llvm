package org.example;

class AssignExpr extends Expr {

    String name;
    Expr value;

    AssignExpr(String name, Expr value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String stringify() {
        return String.format("( = %s %s)", name, value.stringify());
    }
}
