package org.example;

class LetStmt extends Stmt {

    private String name;
    private Expr value;

    public LetStmt(String name, Expr value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Expr getValue() {
        return value;
    }

    @Override
    public String stringify() {
        return String.format("(let %s %s);", name, value.stringify());
    }
}
