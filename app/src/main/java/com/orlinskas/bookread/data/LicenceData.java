package com.orlinskas.bookread.data;

import com.orlinskas.bookread.constants.LicenceConstant;

public class LicenceData {
    private static int countOfBookCreate = 0;
    private static int countOfFreeBooks = 10;
    private static boolean marketAd = true;
    private static String licenceVer = LicenceConstant.FREE_WITH_AD;

    public static boolean isMarketAd() {
        return marketAd;
    }

    public static void setMarketAd(boolean marketAd) {
        LicenceData.marketAd = marketAd;
    }

    public static String getLicenceVer() {
        return licenceVer;
    }

    public static void setLicenceVer(String licenceVer) {
        LicenceData.licenceVer = licenceVer;
    }

    public static int getCountOfBookCreate() {
        return countOfBookCreate;
    }

    public static void setCountOfBookCreate(int countOfBookCreate) {
        LicenceData.countOfBookCreate = countOfBookCreate;
    }

    public static void newBookCreate(){
        countOfBookCreate++;
    }

    public static int getCountOfFreeBooks() {
        return countOfFreeBooks;
    }

    public static void setCountOfFreeBooks(int countOfFreeBooks) {
        LicenceData.countOfFreeBooks = countOfFreeBooks;
    }
}
