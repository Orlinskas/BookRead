package com.orlinskas.bookread;

import com.orlinskas.bookread.constants.LicenceConstant;

import java.io.Serializable;

public class Licence implements Serializable {
    private int countOfBookCreate = 0;
    private int countOfFreeBooks = 10;
    private boolean marketAd = true;
    private String licenceVer = LicenceConstant.FREE_WITH_AD;


    public int getCountOfBookCreate() {
        return countOfBookCreate;
    }

    public void setCountOfBookCreate(int countOfBookCreate) {
        this.countOfBookCreate = countOfBookCreate;
    }

    public  void newBookCreate() {
        countOfBookCreate++;
    }

    public int getCountOfFreeBooks() {
        return countOfFreeBooks;
    }

    public void setCountOfFreeBooks(int countOfFreeBooks) {
        this.countOfFreeBooks = countOfFreeBooks;
    }

    public boolean isMarketAd() {
        return marketAd;
    }

    public void setMarketAd(boolean marketAd) {
        this.marketAd = marketAd;
    }

    public String getLicenceVer() {
        return licenceVer;
    }

    public void setLicenceVer(String licenceVer) {
        this.licenceVer = licenceVer;
    }
}
