package com.company.lexer;

import java.io.File;

public class Main {

    public static void main(String[] args) {

        // file name
        String fileName = "ErrorTrueCode.txt";

        // Lexically analyze the input file
        LexicalAnalyzer.lex(new File(fileName));
    }

}

