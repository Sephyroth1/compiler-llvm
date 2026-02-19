package org.example;

import java.util.List;

class FnDecl {

    String name;
    BlockStmt body;
    List<Param> params;
    Type returnType;

    FnDecl(String name, BlockStmt body, List<Param> params, Type returnType) {
        this.name = name;
        this.body = body;
        this.params = params;
        this.returnType = returnType;
    }
}
