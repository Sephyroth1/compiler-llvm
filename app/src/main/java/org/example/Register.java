package org.example;

class Register {

    int id;
    Type type;

    Register(int id, Type type) {
        this.id = id;
        this.type = type;
    }

    String stringify() {
        return id + " " + type;
    }
}
