package com.orlinskas.bookread.data;

public class SharedPreferencesData {

        private static android.content.SharedPreferences preferences;

        public static final String SETTINGS_AND_DATA = "settingsAndData";
        public static final String KEY_FIRST_RUN = "firstRun";
        public static final String KEY_COUNT_OF_BOOK_CREATE = "countOfBookCreate";
        public static final String KEY_COUNT_OF_FREE_BOOK = "countOfFreeBook";
        public static final String KEY_MARKET_AD = "marketAd";
        public static final String KEY_LICENCE_VERSION = "licenceVersion";

        public static void setPreferences(android.content.SharedPreferences preferences) {
            SharedPreferencesData.preferences = preferences;
        }

        public static android.content.SharedPreferences getPreferences() {
            return preferences;
        }

        public static void savePreferenceUsingKey(String key, String value){
            android.content.SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, value);
            editor.apply();
        }

        public static void savePreferenceUsingKey(String key, int value){
            android.content.SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(key, value);
            editor.apply();
        }

        public static void savePreferenceUsingKey(String key, boolean value){
            android.content.SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(key, value);
            editor.apply();
        }

        public static String getPreferenceUsingKey(String key, String defValue){
            return preferences.getString(key, defValue);
        }

        public static int getPreferenceUsingKey(String key, int defValue){
            return preferences.getInt(key, defValue);
        }

        public static boolean getPreferenceUsingKey(String key, boolean defValue){
            return preferences.getBoolean(key, defValue);
        }

        public static void setTrueFirstRun(){
            android.content.SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(KEY_FIRST_RUN, true);
            editor.apply();
        }

        public static boolean getFirstRun(){
            return preferences.getBoolean(KEY_FIRST_RUN, false);
        }
}

