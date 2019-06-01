package com.orlinskas.bookread.fileManager;

import android.content.res.Resources;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileReader;

public class Opener {

    public XmlPullParser open(File file) {


        XmlPullParser xpp = null;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            xpp = factory.newPullParser();
            xpp.setInput( new FileReader( file) );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xpp;
    }
}
