package com.orlinskas.bookread.presenters;

/**
 * @author Orlinskas
 * @version 2
 */
public class ReadingProgressHandler {
    public int getPageNumber(float percentProgress, int allPages) {
        return (int) ((allPages * percentProgress) / 100);
    }

    public float getPercentProgress(int pageNumber, int allPages) {
        float a = (float) pageNumber;
        float b = (float) allPages;
        return (100 * a) / b;
    }
}
