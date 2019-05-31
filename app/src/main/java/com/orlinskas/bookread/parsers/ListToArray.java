package com.orlinskas.bookread.parsers;

import java.util.List;

public class ListToArray {

    public static String[] parse(List<String> directoryEntries) {
        String[] entries = new String[directoryEntries.size()];

        for(int i = 0; i < entries.length; i++){
            entries[i] = directoryEntries.get(i);
        }
        return entries;
    }
}
