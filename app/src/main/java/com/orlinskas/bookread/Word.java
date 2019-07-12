package com.orlinskas.bookread;

import java.util.Objects;
/**
 * @author Orlinskas
 * @version 2
 */
public class Word {
    private int id;
    private String original;
    private String translate;
    private int count = 1;

    public Word() {
    }

    public Word(String original) {
        this.original = original;
    }

    public Word(int id, String original, int count) {
        this.id = id;
        this.original = original;
        this.count = count;
    }

    public Word(int id, String original, String translate, int count) {
        this.id = id;
        this.original = original;
        this.translate = translate;
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word = (Word) o;
        return Objects.equals(original, word.original);
    }

    @Override
    public int hashCode() {
        return Objects.hash(original);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
