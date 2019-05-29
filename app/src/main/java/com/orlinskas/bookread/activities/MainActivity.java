package com.orlinskas.bookread.activities;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import com.orlinskas.bookread.AppContext;
import com.orlinskas.bookread.Book;
import com.orlinskas.bookread.Library;
import com.orlinskas.bookread.R;
import com.orlinskas.bookread.ToastBuilder;
import com.orlinskas.bookread.constants.PermissionConstant;
import com.orlinskas.bookread.data.SharedPreferencesData;
import com.orlinskas.bookread.helpers.ActivityOpenHelper;
import com.orlinskas.bookread.helpers.BookBodyFileReader;
import com.orlinskas.bookread.helpers.BookCreator;
import com.orlinskas.bookread.helpers.LibraryHelper;
import com.orlinskas.bookread.helpers.LicenceHelper;
import com.orlinskas.bookread.parsers.ArrayListToString;

import org.xmlpull.v1.XmlPullParser;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    int permissionStatus;
    TextView test;
    //Context context = getApplicationContext();

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

        //parse();


        SharedPreferencesData.setPreferences(getSharedPreferences(SharedPreferencesData.SETTINGS_AND_DATA, MODE_PRIVATE));
        AppContext.setContext(getApplicationContext());

        permissionStatus = ContextCompat.checkSelfPermission
                (this, Manifest.permission.READ_EXTERNAL_STORAGE);
        checkStoragePermission();
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

        if (id == R.id.nav_add_book) {
            permissionStatus = ContextCompat.checkSelfPermission
                    (this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                ActivityOpenHelper.openActivity(getApplicationContext(), OpenBookActivity.class);
            }
            else {
                ToastBuilder.create(this, "Вы не разрешили приложению доступ к памяти телефона!");
            }
        } else if (id == R.id.nav_library) {
            ActivityOpenHelper.openActivity(getApplicationContext(), LibraryActivity.class);
        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send_author) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void checkStoragePermission() {
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            ToastBuilder.create(this, "Привет)");
            //добавить рандомные приветствия
        } else {
            ActivityCompat.requestPermissions
                    (this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                            PermissionConstant.MY_REQUEST_CODE);
        }
    }
   // public void parse() {
   //     XmlPullParser xml = getResources().getXml(R.xml.book2);
   //     BookCreator bookCreator = new BookCreator();
   //     Book book = bookCreator.create(getApplicationContext(), xml);
//
   //     LibraryHelper libraryHelper = new LibraryHelper(getApplicationContext());
   //     libraryHelper.addBook(book);
//
   //     LicenceHelper licenceHelper = new LicenceHelper(getApplicationContext());
   //     licenceHelper.update();
//
   //     go(book);
   // }

   // public void go(Book bookNeed) {
   //     LibraryHelper libraryHelper = new LibraryHelper(getApplicationContext());
   //     Book book = libraryHelper.getBook(bookNeed);
   //     BookBodyFileReader bodyFileReader = new BookBodyFileReader();
//

   // }
}

