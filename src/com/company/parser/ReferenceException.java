package com.company.parser;

// Exception when the entry in the SLR parsing table was referenced invalidly
public class ReferenceException extends Exception {
    public ReferenceException(String errorMessage) {
        super(errorMessage);
    }
}
