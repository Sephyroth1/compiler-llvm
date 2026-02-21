package org.example;

import java.util.List;

class BuiltInFunction implements Callable {

    String name;

    @Override
    public Register emitCall(Builder b, List<Register> args) {
        return b.emitPrintf(args.get(0));
    }
}
