package com.company.parser;

import java.io.File;

public class Main {

    public static void main(String[] args) {

        String fileName = "ErrorTrueCode.ser";

        // Syntactically analyze the input file
        SyntaxAnalyzer parser = new SyntaxAnalyzer(new File(fileName));
        boolean isAccepted = parser.parse();
    }
}
