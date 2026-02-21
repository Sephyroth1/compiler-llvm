package org.example;

import java.util.List;

interface Callable {
    Register emitCall(Builder b, List<Register> args);
}
