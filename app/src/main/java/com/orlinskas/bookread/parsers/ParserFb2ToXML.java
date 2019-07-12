package com.orlinskas.bookread.parsers;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileReader;
/**
 * @author Orlinskas
 * @version 1
 */
public class ParserFb2ToXML {

    public XmlPullParser parse(File file) {

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
