package com.orlinskas.bookread.fileManager;

import android.content.res.Resources;

import com.orlinskas.bookread.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;

public class Opener {
    private Resources resources;

    public Opener(Resources resources){
        this.resources = resources;
    }

    public XmlPullParser open(File file) {


        XmlPullParser xpp = null;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            xpp = factory.newPullParser();
            xpp.setInput( new FileReader( file) );
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // String rename = file.getAbsolutePath().replaceAll("fb2" , "xml");
       // File xmlFile = new File(rename);


        return xpp;
    }
}
