package org.example;

import java.util.List;

class FunctionSymbol extends Symbol {

    List<Type> params;
    Type returnType;

    FunctionSymbol(String name, List<Type> params, Type returnType) {
        super(name);
        this.params = params;
        this.returnType = returnType;
    }
}
