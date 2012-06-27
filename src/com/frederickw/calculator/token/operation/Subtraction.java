package com.frederickw.calculator.token.operation;

import com.frederickw.calculator.token.Operation;

public final class Subtraction extends Operation {

    public Subtraction() {
        super('-');
    }

    @Override
    public int getPrecedence() {
        return 2;
    }

    @Override
    public double evaluate(double d1, double d2) {
        return d1 - d2;
    }

}
