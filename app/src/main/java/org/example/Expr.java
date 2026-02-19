package org.example;

abstract class Expr {

    Type type;

    abstract String stringify();

    abstract Register lower(Builder builder);
}
