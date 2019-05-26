package com.orlinskas.bookread;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.orlinskas.bookread.constants.BookConstant;
import com.orlinskas.bookread.constants.XML_TAG;
import com.orlinskas.bookread.data.LicenceData;
import com.orlinskas.bookread.data.SharedPreferencesData;

import org.xmlpull.v1.XmlPullParser;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        test = findViewById(R.id.tv_test);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        parse();

        SharedPreferencesData.setPreferences(getSharedPreferences(SharedPreferencesData.SETTINGS_AND_DATA, MODE_PRIVATE));

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getLicenceData (){
        SharedPreferencesData.getPreferenceUsingKey(SharedPreferencesData.KEY_COUNT_OF_BOOK_CREATE, LicenceData.getCountOfBookCreate());
        SharedPreferencesData.getPreferenceUsingKey(SharedPreferencesData.KEY_COUNT_OF_FREE_BOOK, LicenceData.getCountOfFreeBooks());
        SharedPreferencesData.getPreferenceUsingKey(SharedPreferencesData.KEY_MARKET_AD, LicenceData.isMarketAd());
        SharedPreferencesData.getPreferenceUsingKey(SharedPreferencesData.KEY_LICENCE_VERSION, LicenceData.getLicenceVer());
    }

    public void parse () {
        //XmlPullParser xpp = getResources().getXml(R.xml.book2);

        Book book = new Book();

        book.setAuthorName(findAuthorName(getResources().getXml(R.xml.xmlbook)));
        book.setBookTitle(findBookTitle(getResources().getXml(R.xml.xmlbook)));
        book.setAnnotation(findAnnotation(getResources().getXml(R.xml.xmlbook)));
        book.setDate();

        test.setText(String.format("%s%s%s%s", book.getAuthorName(), book.getBookTitle(), book.getDate(), book.getAnnotation()));


    }

    public String findAuthorName (XmlPullParser parser){
        //нужно исключить неправильные имена авторов!
        StringBuilder authorName = new StringBuilder();

        int eventType;
        String tag;
        boolean stop = false;

        try {
            do {
                eventType = parser.next();
                tag = parser.getName();

                if (tag != null && tag.equals(XML_TAG.AUTHOR) && !stop) {
                    try {
                        authorName.append(parser.nextText()).append(" ");
                    } catch (Exception ignored) {}
                    while (checkEndTag(XML_TAG.AUTHOR, parser.getName(), parser.getEventType()) && !stop) {
                        try {
                            authorName.append(parser.nextText()).append(" ");
                        } catch (Exception ignored) {}
                        if (!checkEndTag(XML_TAG.LAST_NAME, parser.getName(), parser.getEventType())){
                            stop = true;
                        }
                        parser.next();
                    }
                }

            } while (eventType != parser.END_DOCUMENT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return String.valueOf(authorName);
    }

    public String findBookTitle (XmlPullParser parser){
        //не останавливается в конце тега, доходит до конца документа
        String bookTitle = BookConstant.NA_BOOK_TITLE;
        String tag;
        int eventType;
        boolean stop = false;

        try {
            do {
                eventType = parser.next();
                tag = parser.getName();

                if (tag != null && tag.equals(XML_TAG.BOOK_TITLE) && !stop) {
                    try {
                        bookTitle = parser.nextText();
                    } catch (Exception ignored) {}
                    while (checkEndTag(XML_TAG.BOOK_TITLE, parser.getName(), parser.getEventType()) && !stop) {
                        try {
                            bookTitle = parser.nextText();
                        } catch (Exception ignored) {}
                        if (!checkEndTag(XML_TAG.BOOK_TITLE, parser.getName(), parser.getEventType())){
                            stop = true;
                        }
                        parser.next();
                    }
                }

            } while (eventType != parser.END_DOCUMENT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookTitle;
    }

    public String findAnnotation(XmlPullParser parser){
        //не останавливается в конце тега, доходит до конца документа
        StringBuilder annotation = new StringBuilder();

        int eventType;
        String tag;
        boolean stop = false;

        try {
            do {
                eventType = parser.next();
                tag = parser.getName();

                if (tag != null && tag.equals(XML_TAG.ANNOTATION) && !stop) {
                    try {
                        annotation.append(parser.nextText()).append(" ");
                    } catch (Exception ignored) {}
                    while (checkEndTag(XML_TAG.ANNOTATION, parser.getName(), parser.getEventType()) && !stop) {
                        try {
                            annotation.append(parser.nextText()).append(" ");
                        } catch (Exception ignored) {}
                        if (!checkEndTag(XML_TAG.ANNOTATION, parser.getName(), parser.getEventType())){
                            stop = true;
                        }
                        parser.next();
                    }
                }

            } while (eventType != parser.END_DOCUMENT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return String.valueOf(annotation);
    }

    /*public String findBody(XmlPullParser parser){
        //не останавливается в конце тега, доходит до конца документа
        StringBuilder body = new StringBuilder();

        int eventType;
        String tag;
        boolean stop = false;

        try {
            do {
                eventType = parser.next();
                tag = parser.getName();

                if (tag != null && tag.equals(XML_TAG.ANNOTATION) && !stop) {
                    try {
                        annotation.append(parser.nextText()).append(" ");
                    } catch (Exception ignored) {}
                    while (checkEndTag(XML_TAG.ANNOTATION, parser.getName(), parser.getEventType()) && !stop) {
                        try {
                            annotation.append(parser.nextText()).append(" ");
                        } catch (Exception ignored) {}
                        if (!checkEndTag(XML_TAG.ANNOTATION, parser.getName(), parser.getEventType())){
                            stop = true;
                        }
                        parser.next();
                    }
                }

            } while (eventType != parser.END_DOCUMENT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return String.valueOf(annotation);
    }
    */

    public boolean checkEndTag (String needTag, String nowTag, int eventType){
        if (nowTag == null){
            return true;
        }
        if (nowTag.equals(needTag)){
            return eventType != XmlPullParser.END_TAG;
        }
        return true;
    }
}

