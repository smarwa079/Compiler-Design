package com.company.lexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;

// Lexical analyzer that lexically analyzes an input file and creates a symbol table

public class LexicalAnalyzer {

    /**
     * Lexically analyzes an input file line by line and character by character to
     * tokenize it into a symbol table.
     *
     * @param inputFile the input file to read
     */
    public static void lex(File inputFile) {

        // Symbol Table
        SymbolTable symtab = new SymbolTable();
        int lineCount = 0;
        int charCount = 0;

        BufferedReader reader = null;
        try {
            // Create input stream
            reader = new BufferedReader(new FileReader(inputFile));

            // String of the one whole line
            String line;

            // Read the input file line by line
            while ((line = reader.readLine()) != null) {
                // Initialize variables for reading the line
                State currentState = State.START;
                String workingString = "";
                char ch = 0;

                // Read character by character in the line and transition the state
                for (charCount = 0; charCount < line.length() + 1; charCount++) {

                    // Check if it is the last character
                    ch = (charCount == line.length()) ? ' ' : line.charAt(charCount);

                    // Transition
                    currentState = currentState.transition(ch);

                    // If accepted
                    if (currentState.isAccepted()) {
                        symtab.put(new Token(currentState.getTokenType(), workingString, lineCount));

                        currentState = State.START;
                        workingString = Character.toString(ch);
                        currentState = currentState.transition(ch);
                    } else {
                        // If not yet accepted
                        workingString += ch;
                    }
                }

                // Check if the token still has not been made at the end
                if (workingString.length() > 0 && !workingString.equals(" ")
                        && (currentState.getTokenType() == TokenType.NOT_YET_A_TOKEN
                        || currentState.getTokenType() == TokenType.NOT_ACCEPTED)) {
                    throw new NullTokenException("Input \"" + workingString + "\" not accepted");
                }

                // Prepare for next line
                lineCount = lineCount + 1;
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
            System.exit(1);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        } catch (NullTokenException e) {
            System.out.println(
                    e + " at character " + charCount + " in line " + (lineCount + 1) + " in " + inputFile.getName());
            System.exit(1);
        } finally {
            // Close the reader
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("\nRead " + lineCount + " line(s) from the file \"" + inputFile.getPath() + "\".");

        // Print information in symbol table
        symtab.printTable();

        // Export the instance information of the token list in the symbol table as .ser
        // file to read it in the parser
        try {
            String inputFilePath = inputFile.getPath();
            int pos = inputFilePath.lastIndexOf(".");
            if (pos > 0 && pos < (inputFilePath.length() - 1)) { // If '.' is not the first or last character.
                inputFilePath = inputFilePath.substring(0, pos);
            }

            File outputFile = new File(inputFilePath + ".ser");

            // Write ArrayList<Token> class information as .ser file
            writeInstanceToFile(outputFile, symtab.getTokens());

            System.out.println("Output file is generated as \"" + outputFile.getPath() + "\".");
        } catch (FileNotFoundException e) {
            System.out.println(e);
            System.exit(1);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }
    }

    /***
     * Saves class instance information as a file
     *
     * @param file the file to save the instance information
     * @param obj  the object to save information
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static void writeInstanceToFile(File file, Object obj) throws FileNotFoundException, IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
        out.writeObject(obj);
        out.flush();
        out.close();
    }
}
