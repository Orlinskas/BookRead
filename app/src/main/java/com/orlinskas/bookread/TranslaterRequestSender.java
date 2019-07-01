package com.orlinskas.bookread;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class TranslaterRequestSender {

    //https://api.multillect.com/translate/json/1.0/2711024?method=translate/api/translate&from=en&to=ru&text=test&sig=38f4c080a05411e6a0328c89a5a1fd3
    private static final String MULTILLECT_COM = "https://api.multillect.com/translate/json/1.0/";
    private static final String USER_ID = "1153";
    private static final String KEY = "b103a44375cb9bf62864160733214511";
    private static final String EN = "en";
    private static final String RU = "ru";

    public String request(String text) {
        HttpURLConnection httpURLConnection = null;

        try {
            httpURLConnection = (HttpURLConnection) generateURL(text).openConnection();
            InputStream in = httpURLConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();

            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpURLConnection.disconnect();
        }

        return null;
    }

    private static URL generateURL(String text){
       // Uri buildRequest = Uri.parse(MULTILLECT_COM + USER_ID);
                //.buildUpon()
                //.appendQueryParameter("method", "translate/api/translate")
                //.appendQueryParameter("from", RU)
                //.appendQueryParameter("to", EN)
                //.appendQueryParameter("text", text)
                //.appendQueryParameter("sig", KEY)
                //.build();
        String request = MULTILLECT_COM + USER_ID + "?method=translate/api/translate&from=ru&to=en&text=" + text + "&sig=" + KEY;
        //https://api.multillect.com/translate/json/1.0/2711024?method=translate/api/translate&from=en&to=ru&text=test&sig=38f4c080a05411e6a0328c89a5a1fd3

        URL url = null;
        try {
            //url = new URL(buildRequest.toString());
            url = new URL(request);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }
}

