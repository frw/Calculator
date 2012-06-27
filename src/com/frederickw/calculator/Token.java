package com.frederickw.calculator;

public interface Token {

	public enum Type {
		NUMBER, CONSTANT, OPERATION, FUNCTION, PARENTHESIS
	}

	public Type getType();

}
