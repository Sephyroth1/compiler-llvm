package org.example;

import java.util.HashMap;
import java.util.Map;

class Environment {

    Environment parent;
    Map<String, Register> vars = new HashMap<>();

    Environment() {
        this.parent = null;
    }

    Environment(Environment parent) {
        this.parent = parent;
    }

    Register lookup(String name) {
        if (vars.containsKey(name)) return vars.get(name);
        if (parent != null) return parent.lookup(name);

        throw new RuntimeException("Undefined variable: " + name);
    }

    void define(String name, Register reg) {
        vars.put(name, reg);
    }

    void assign(String name, Register reg) {
        if (vars.containsKey(name)) {
            vars.put(name, reg);
            return;
        }
        if (parent != null) {
            parent.assign(name, reg);
            return;
        }
        throw new RuntimeException("Undefined variable: " + name);
    }

    public Environment copy() {
        Environment env = new Environment(this);
        env.vars.putAll(this.vars);
        return env;
    }
}
