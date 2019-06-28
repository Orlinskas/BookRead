package com.orlinskas.bookread.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class BookBodyFileReader {

    private ArrayList<String> words = new ArrayList<>();
    private StringBuilder word = new StringBuilder();

    public ArrayList<String> read (File file) throws Exception {
        BufferedReader bufferedReader;
        bufferedReader = new BufferedReader(new FileReader(file));

        int c;
        while ((c = bufferedReader.read()) != -1) {
            switch ((char) c){
                case ' ':
                    separateWord(" ");
                    break;
                case '\n':
                    separateWord("\n");
                    break;
                case '.':
                    separateWord(".");
                    break;
                case ',':
                    separateWord(",");
                    break;
                case '!':
                    separateWord("!");
                    break;
                case '&':
                    separateWord("&");
                    break;
                case '?':
                    separateWord("?");
                    break;
                case ':':
                    separateWord(":");
                    break;
                case ';':
                    separateWord(";");
                    break;
                case '@':
                    separateWord("@");
                    break;
                case '#':
                    separateWord("#");
                    break;
                case '$':
                    separateWord("$");
                    break;
                case '%':
                    separateWord("%");
                    break;
                case '*':
                    separateWord("*");
                    break;
                case '(':
                    separateWord("(");
                    break;
                case ')':
                    separateWord(")");
                    break;
                case '-':
                    separateWord("-");
                    break;
                case '+':
                    separateWord("+");
                    break;
                case '«':
                    separateWord("«");
                    break;
                case '»':
                    separateWord("»");
                    break;
                    default:
                        word.append((char) c);
            }
        }
        bufferedReader.close();

        return words;
    }

    private void separateWord(String character) {
        if(word.length() > 0) {
            words.add(word.toString());
            words.add(character);
        }
        else {
            words.add(character);
        }
        this.word = new StringBuilder();
    }

}
