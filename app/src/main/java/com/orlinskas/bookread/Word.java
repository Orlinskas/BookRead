package com.orlinskas.bookread;

public class Word {
    private int id;
    private String russian;
    private String english;
    private int count;

    public Word(int id, String russian, String english, int count) {
        this.id = id;
        this.russian = russian;
        this.english = english;
        this.count = count;
    }


    public Word() {
    }

    public Word(int id, String russian, int count) {
        this.id = id;
        this.russian = russian;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRussian() {
        return russian;
    }

    public void setRussian(String russian) {
        this.russian = russian;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
