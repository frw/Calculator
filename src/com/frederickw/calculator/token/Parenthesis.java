package com.frederickw.calculator.token;

import com.frederickw.calculator.Token;

public final class Parenthesis implements Token {

    public static final Parenthesis[] PARENTHESES = new Parenthesis[] {
            new Parenthesis(true), new Parenthesis(false) };

    private final boolean open;

    private Parenthesis(boolean open) {
        this.open = open;
    }

    public static Parenthesis parseParenthesis(String s) {
        if (s.length() == 1) {
            char symbol = s.charAt(0);
            if (symbol == '(' || symbol == ')') {
                return PARENTHESES[symbol - '('];
            }
        }
        return null;
    }

    public boolean isOpen() {
        return open;
    }

    @Override
    public Type getType() {
        return Type.PARENTHESIS;
    }

    @Override
    public String toString() {
        return open ? "(" : ")";
    }

}
