package com.orlinskas.bookread;

import java.io.Serializable;

/**
 * @author Orlinskas
 * @version 2
 */
public class Settings implements Serializable {
    private boolean portraitOrientation = true;
    private int themeId = 1;
    private int textSize = 14;
    private int typeface = 0;

    public Settings (boolean portraitOrientation, int themeId, int textSize, int typeface){
       this.portraitOrientation = portraitOrientation;
       this.themeId = themeId;
       this.textSize = textSize;
       this.typeface = typeface;
    }

    public Settings(){
    }

    public boolean isPortraitOrientation() {
        return portraitOrientation;
    }

    public void setPortraitOrientation(boolean portraitOrientation) {
        this.portraitOrientation = portraitOrientation;
    }

    public int getThemeId() {
        return themeId;
    }

    public void setThemeId(int themeId) {
        this.themeId = themeId;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getTypeface() {
        return typeface;
    }

    public void setTypeface(int typeface) {
        this.typeface = typeface;
    }
}
