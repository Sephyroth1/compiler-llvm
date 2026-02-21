package org.example;

class VarSymbol extends Symbol {

    Type type;

    VarSymbol(String name, Type type) {
        super(name);
        this.type = type;
    }
}
