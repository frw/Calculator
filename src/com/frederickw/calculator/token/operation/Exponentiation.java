package com.frederickw.calculator.token.operation;

import com.frederickw.calculator.token.Operation;

public final class Exponentiation extends Operation {

    public Exponentiation() {
        super('^');
    }

    @Override
    public int getPrecedence() {
        return 0;
    }

    @Override
    public double evaluate(double d1, double d2) {
        return Math.pow(d1, d2);
    }

}
