package com.orlinskas.bookread.presenters;

public class PercentProgress {
    public int getPageNumber(float percentProgress, int allPages) {
        try {
            return (int) ((allPages * percentProgress) / 100);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public float getPercentProgress(int pageNumber, int allPages) {
        try {
            float a = (float) pageNumber;
            float b = (float) allPages;
            return (100 * a) / b;
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0f;
        }
    }
}
