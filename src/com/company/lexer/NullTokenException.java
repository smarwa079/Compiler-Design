package com.company.lexer;

/**
 * Exception when an input string to be put in symbol table does not belong to
 * any token type
 */
public class NullTokenException extends Exception {
    public NullTokenException(String errorMessage) {
        super(errorMessage);
    }
}
