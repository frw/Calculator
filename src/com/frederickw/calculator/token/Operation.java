package com.frederickw.calculator.token;

import java.util.List;

import com.frederickw.calculator.SyntaxException;
import com.frederickw.calculator.Token;
import com.frederickw.calculator.token.operation.Addition;
import com.frederickw.calculator.token.operation.Division;
import com.frederickw.calculator.token.operation.Exponentiation;
import com.frederickw.calculator.token.operation.Modulo;
import com.frederickw.calculator.token.operation.Multiplication;
import com.frederickw.calculator.token.operation.Subtraction;


public abstract class Operation implements Token {

    public static final int HIGHEST_PRECEDENCE = 0;
    public static final int LOWEST_PRECEDENCE = 2;

    public static final Addition ADDITION = new Addition();
    public static final Subtraction SUBTRACTION = new Subtraction();
    public static final Multiplication MULTIPLICATION = new Multiplication();
    public static final Division DIVISION = new Division();
    public static final Exponentiation EXPONENTIATION = new Exponentiation();
    public static final Modulo MODULO = new Modulo();

    public static final Operation[] OPERATIONS = new Operation[] { ADDITION,
            SUBTRACTION, MULTIPLICATION, DIVISION, EXPONENTIATION, MODULO };

    public final char symbol;

    protected Operation(char symbol) {
        this.symbol = symbol;
    }

    public static Operation parseOperator(String s) {
        if (s.length() == 1) {
            char symbol = s.charAt(0);
            for (Operation o : OPERATIONS) {
                if (o.symbol == symbol) {
                    return o;
                }
            }
        }
        return null;
    }

    public abstract int getPrecedence();

    public Number evaluate(int index, List<Token> symbols) {
        final Number n1;
        if (symbol == '+' || symbol == '-') {
            Token before;
            if (index > 0
                    && (before = symbols.get(index - 1)).getType() != Token.Type.OPERATION) {
                n1 = (Number) before;
            } else {
                n1 = null;
            }
        } else {
            if (index > 0) {
                Token before = symbols.get(index - 1);
                if (before.getType() == Token.Type.NUMBER) {
                    n1 = (Number) before;
                } else {
                    throw new SyntaxException();
                }
            } else {
                throw new SyntaxException();
            }
        }
        final Number n2;
        if (index < symbols.size() - 1) {
            int i = index + 1;
            Token after = symbols.get(i);
            if (after.getType() == Token.Type.OPERATION) {
                Operation o = (Operation) after;
                if (o.symbol == '+' || o.symbol == '-') {
                    n2 = o.evaluate(i, symbols);
                    symbols.remove(i);
                } else {
                    throw new SyntaxException();
                }
            } else {
                n2 = (Number) after;
            }
        } else {
            throw new SyntaxException();
        }
        double d1 = n1 != null ? n1.getValue() : 0.0;
        double d2 = n2.getValue();
        return new Number(evaluate(d1, d2));
    }

    protected abstract double evaluate(double d1, double d2);

    @Override
    public final Type getType() {
        return Type.OPERATION;
    }

    @Override
    public String toString() {
        return Character.toString(symbol);
    }
}
