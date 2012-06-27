package com.frederickw.calculator;

public interface Token {

    public enum Type {
        NUMBER, CONSTANT, VARIABLE, OPERATION, FUNCTION, PARENTHESIS
    }

    public Type getType();

}
