package org.example;

enum Op {
    // PLUS,
    // MINUS,
    // DIVIDE,
    // TIMES,
    // EQEQ,
    // NOT_EQ,
    // LESS,
    // LESS_EQ,
    // GREATER,
    // GREATER_EQ,
    ADD("add"),
    SUB("sub"),
    MUL("mul"),
    DIV("div"),
    EQEQ("eq"),
    NOT_EQ("ne"),
    LESS("lt"),
    LESS_EQ("le"),
    GREATER("gt"),
    GREATER_EQ("ge"),
    NEG("neg"),
    NOT("not"),
    PLUS("add");

    String llvm;

    Op(String llvm) {
        this.llvm = llvm;
    }
}
