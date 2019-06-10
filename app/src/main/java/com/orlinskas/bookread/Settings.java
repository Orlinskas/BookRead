package com.orlinskas.bookread;

import java.io.Serializable;

public class Settings implements Serializable {
    private boolean portraitOrientation = true;
    private int theme = 1;
    private int textSize = 14;

    public Settings (boolean portraitOrientation, int theme, int textSize){
       this.portraitOrientation = portraitOrientation;
       this.theme = theme;
       this.textSize = textSize;
    }

    public Settings(){

    }

    public boolean isPortraitOrientation() {
        return portraitOrientation;
    }

    public void setPortraitOrientation(boolean portraitOrientation) {
        this.portraitOrientation = portraitOrientation;
    }

    public int getTheme() {
        return theme;
    }

    public void setTheme(int theme) {
        this.theme = theme;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }
}
