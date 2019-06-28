package com.orlinskas.bookread;

import java.util.Objects;

public class Word {
    private int id;
    private String russian;
    private String english;
    private int count = 1;

    public Word(int id, String russian, String english, int count) {
        this.id = id;
        this.russian = russian;
        this.english = english;
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word = (Word) o;
        return Objects.equals(russian, word.russian);
    }

    @Override
    public int hashCode() {
        return Objects.hash(russian);
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
