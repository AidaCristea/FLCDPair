package scanner;

import FA.FiniteAutomata;

import symbolTbl.HashTable;
import symbolTbl.SymbolTable;
import util.Pair;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PIF {

    private ArrayList<String> tokens = new ArrayList<>();

    public PIF() {
        readTokens();
    }

    public void readTokens() {
        //Read tokens from token.in file
        try (BufferedReader reader = new BufferedReader(new FileReader("token.in"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                tokens.add(line);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void readProblem(String readingFile, String pifOut, String stOut) {
        //read the given problem from readingFile
        //for each line, first split it so if there is something that matches this pattern "something" will be kept as a constant
        //then split the line by the specified separators
        //for each token, check if it is lexically correct, and in case of lexical error, print a message
        //if the program is lexically correct, start creating the pif

        ArrayList<String> readLines = new ArrayList<>();
        boolean ok = true;

        try (BufferedReader reader = new BufferedReader(new FileReader(readingFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] lineTokens = line.split("(?=[; ,()\\[\\]])|(?<=[; ,()\\[\\]])");

                List<String> finalList = new ArrayList<>();

                for (String s : lineTokens) {
                    System.out.println(s);
                    String response = stringChecker(s);
                    if (!response.isEmpty()) {
                        String message = "LexicalError for the token " + s + " on the line " + line + " " + response + "\n";
                        //System.out.println(message);
                        Files.writeString(Path.of(pifOut), message, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                        Files.writeString(Path.of(stOut), message, StandardOpenOption.CREATE, StandardOpenOption.APPEND);

                        ok = false;
                        break;
                    }
                    finalList.addAll(Arrays.asList(s));
                }
                readLines.addAll(finalList);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try {
            if (ok) {
                String message = "Lexically correct!\n";
                //System.out.println(message);
                Files.writeString(Path.of(pifOut), message, StandardOpenOption.CREATE, StandardOpenOption.APPEND);

                constructPif(readLines, pifOut, stOut);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }

    public String stringChecker(String s) {
        //we do some lexical checks
        //a variable cannot start with one of these characters ?@$#^
        //a variable cannot start with a capital letter
        //if the given string is equal to "while" or "for" then it is not correct.
        if (s.length() >= 2) {
            if (s.matches("[?@$#^].*")) {
                return "Cannot start with that character";
            }

            if (s.matches("^[A-Z].*")) {
                return "Cannot start with a capital letter";
            }
        }

        if (s.equals("while") || (s.equals("for"))) {
            return "Is not a reserved word. It will not do what it is meant to do";
        }
        return "";
    }

    public String findPositionInST(String key, SymbolTable sb) {
        //return the position of a key from the symbol table
        return sb.find(key).toString();
    }

    public static boolean isInteger(String str) {
        //check if a string is an integer
        try {
            // Try parsing the string as an integer
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            // If an exception occurs, the string is not an integer
            return false;
        }
    }

    public void operation(SymbolTable sb, ArrayList<Pair<String, String>> p, String type, String s) {
        //sb - the symbol table we are working with
        //p - the array list of pairs
        //type - can be "constant" or "identifier"
        //s - the string we need to work with

        //this method is called many times in the constructPif method
        //here we search for the given string in the symbol table. If found, we get the position of it and add the (type, ST_pos) pair
        //If the string cannot be found in the symbol table, we add it in the symbol table, we get its position and then add the (type, ST_pos) pair

        if (sb.search(s)) {
            String ST_pos = findPositionInST(s, sb);
            p.add(new Pair<>(type, ST_pos));
        } else {
            sb.insert(s);
            String ST_pos = findPositionInST(s, sb);
            p.add(new Pair(type, ST_pos));

        }
    }

    public boolean isIntegerConstant(String str) {
        FiniteAutomata faInt = new FiniteAutomata("D:\\FACULTATE\\Materiale facultate 2023-2024\\LFTC\\Labs\\Lab3\\FLCD\\Lab2FLCD\\src\\FA\\IntegerConstant.in");
        faInt.readFile();
        if (!faInt.isDFA())
            System.out.println("Not a valid DFA");

        return faInt.isAccepted(str);

    }

    public boolean isStringConstant(String str) {
        FiniteAutomata faStr = new FiniteAutomata("D:\\FACULTATE\\Materiale facultate 2023-2024\\LFTC\\Labs\\Lab3\\FLCD\\Lab2FLCD\\src\\FA\\StringConstant.in");
        faStr.readFile();
        if (!faStr.isDFA())
            System.out.println("Not a valid DFA");

        return faStr.isAccepted(str);
    }

    public boolean isIdentifier(String str) {
        FiniteAutomata faIdentifier = new FiniteAutomata("D:\\FACULTATE\\Materiale facultate 2023-2024\\LFTC\\Labs\\Lab3\\FLCD\\Lab2FLCD\\src\\FA\\Identifiers.in");
        faIdentifier.readFile();
        if (!faIdentifier.isDFA())
            System.out.println("Not a valid DFA");

        return faIdentifier.isAccepted(str);
    }


    public void constructPif(ArrayList<String> readLines, String pifOut, String stOut) {
        //here the pif is created
        //we take each token from the readLines array list, we check if it is not equal to empty space, in case of which we check if that string exists in the tokens read from the token.in file
        //if it exists, we add the pair appropriately
        //if it doesn't exist, we check if it is an integer to decide if it is a constant. If it is not an integer, we check if the string is surrounded by quotes "".
        //if it is, then that will be a constant, otherwise an identifier
        //then we call the operation method
        //adn lastly we write the pif and the symbol table in the appropriate files

        ArrayList<Pair<String, String>> pif = new ArrayList<>();
        SymbolTable sb = new SymbolTable(new HashTable(5));

        for (String s : readLines) {
            if (!s.equals(" ")) {
                //System.out.println(s);

                if (tokens.contains(s)) {
                    pif.add(new Pair(s, "-1"));
                } else {
                    if (isIntegerConstant(s)) {
                        operation(sb, pif, "constant", s);
                    } else if (isStringConstant(s)) {
                        operation(sb, pif, "constant", s);
                    } else {
                        operation(sb, pif, "identifier", s);

                    }
                }
            }
        }

        try {
            for (Pair<String, String> pair : pif) {
                String pairString = pair.getFirst().toString() + " = " + pair.getSecond().toString() + "\n";
                Files.writeString(Path.of(pifOut), pairString, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            }
            Files.writeString(Path.of(stOut), sb.toString(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }



    public String sequence(String filePath, String seqOut) throws IOException {
        List<String> res = new ArrayList<>();
        String result="";

        try(BufferedReader br = new BufferedReader(new FileReader(filePath)))
        {
            String line;
            br.readLine();

            while((line=br.readLine())!=null)
            {
                String[] splitted = line.split(" ");
                res.add(splitted[0]);
            }
        }


        for(String s : res)
        {
            result += s + " ";
        }

        Files.writeString(Path.of(seqOut), result, StandardOpenOption.CREATE, StandardOpenOption.WRITE);

        return result;
    }

}
