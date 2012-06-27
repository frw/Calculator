package com.frederickw.calculator.token;

import com.frederickw.calculator.Token;

public final class Number implements Token {

    public static final Number ZERO = new Number(0.0);

    private final double value;

    public Number(double value) {
        this.value = value;
    }

    public static Number parseNumber(String s) {
        return new Number(Double.parseDouble(s));
    }

    public double getValue() {
        return value;
    }

    @Override
    public Type getType() {
        return Type.NUMBER;
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }

}
