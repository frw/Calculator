package com.frederickw.calculator.token.operation;

import com.frederickw.calculator.token.Operation;

public final class Multiplication extends Operation {

    public Multiplication() {
        super('*');
    }

    @Override
    public int getPrecedence() {
        return 1;
    }

    @Override
    public double evaluate(double d1, double d2) {
        return d1 * d2;
    }

}
