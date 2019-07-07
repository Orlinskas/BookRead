package com.orlinskas.bookread.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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

import com.orlinskas.bookread.AppContext;
import com.orlinskas.bookread.R;
import com.orlinskas.bookread.ToastBuilder;
import com.orlinskas.bookread.constants.PermissionConstant;
import com.orlinskas.bookread.helpers.ActivityOpenHelper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    int permissionStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permissionStatus = ContextCompat.checkSelfPermission
                        (getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
                if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                    ActivityOpenHelper.openActivity(getApplicationContext(), FileManagerActivity.class);
                }
                else {
                    ToastBuilder.create(getApplicationContext(), "Вы не разрешили приложению доступ к памяти телефона!");
                }
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //parse();


        //SharedPreferencesData.setPreferences(getSharedPreferences(SharedPreferencesData.SETTINGS_AND_DATA, MODE_PRIVATE));
        AppContext.setContext(getApplicationContext());

        permissionStatus = ContextCompat.checkSelfPermission
                (this, Manifest.permission.READ_EXTERNAL_STORAGE);
        checkStoragePermission();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
            ActivityOpenHelper.openActivity(getApplicationContext(), SettingsActivity.class);
            return true;
        }
        else if(id == R.id.action_help) {
            ActivityOpenHelper.openActivity(getApplicationContext(), HelpActivity.class);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add_book) {
            permissionStatus = ContextCompat.checkSelfPermission
                    (this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                ActivityOpenHelper.openActivity(getApplicationContext(), FileManagerActivity.class);
            }
            else {
                ToastBuilder.create(this, "Вы не разрешили приложению доступ к памяти телефона!");
            }
        } else if (id == R.id.nav_library) {
            ActivityOpenHelper.openActivity(getApplicationContext(), LibraryActivity.class);
        } else if (id == R.id.nav_vocabulary) {
            ActivityOpenHelper.openActivity(getApplicationContext(), VocabularyActivity.class);
        } else if (id == R.id.nav_settings) {
            ActivityOpenHelper.openActivity(getApplicationContext(), SettingsActivity.class);
        } else if (id == R.id.nav_help) {
            ActivityOpenHelper.openActivity(getApplicationContext(), HelpActivity.class);
        } else if (id == R.id.nav_share) {
            Intent sendIntent = new Intent();
            sendIntent.setType("text/plain");
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "\"BookRead\" - английский в твоих книгах!");
            sendIntent.putExtra(Intent.EXTRA_TEXT,  "\"BookRead\" - английский в твоих книгах! Присоединяйся https://play.google.com/store?hl=ru");
            startActivity(Intent.createChooser(sendIntent,"Поделиться"));
        } else if (id == R.id.group) {
            ActivityOpenHelper.openActivity(getApplicationContext(), AboutAsActivity.class);
        } else if (id == R.id.nav_send_author) {
            ActivityOpenHelper.openActivity(getApplicationContext(), ContactActivity.class);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void checkStoragePermission() {
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            ToastBuilder.create(this, "Welcome!)");
            //добавить рандомные приветствия
        } else {
            ActivityCompat.requestPermissions
                    (this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                            PermissionConstant.MY_REQUEST_CODE);
        }
    }
}

