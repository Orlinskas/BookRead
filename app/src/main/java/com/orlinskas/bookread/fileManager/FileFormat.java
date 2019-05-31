package com.orlinskas.bookread.fileManager;

public class FileFormat {
    public static final String FB2 = ".fb2";
    public static final String TXT = ".txt";
    public static final String FOLDER = "folder";
    public static final char OTHER_FILE = '.';
    public static final String OTHER = ".";

    public static String getFormat(String path) {
        String format;
        try {
            format = path.substring(path.length() - 4);
        } catch (Exception e) {
            e.printStackTrace();
            format = "";
        }

        if (containPoint(path)){
            switch(format) {
                case FB2:
                    return FB2;

                case TXT:
                    return TXT;

                default:
                    return OTHER;
            }
        }
        else {
            return FOLDER;
        }
    }

    private static boolean containPoint(String path) {
        try {
            char[] pathArray = path.toCharArray();

            for (Character ch : pathArray) {
                if (ch == OTHER_FILE) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



}
