package com.orlinskas.bookread.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.orlinskas.bookread.Book;
import com.orlinskas.bookread.Library;
import com.orlinskas.bookread.R;
import com.orlinskas.bookread.data.LibraryData;
import com.orlinskas.bookread.fragments.LibraryBookFragment;

import java.util.ArrayList;

public class LibraryActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        ArrayList<Book> books = new ArrayList<>();
        Library library;
        LibraryData libraryData = new LibraryData(getApplicationContext());
        try {
            library = libraryData.loadLibrary();
            books = library.getBooks();
            showFragments(books);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showFragments(ArrayList<Book> books) {
        FragmentManager fm = getSupportFragmentManager();

        for (Book book : books){
            Fragment fragment = fm.findFragmentById(R.id.activity_library_ll_container);
            if (fragment == null) {
                fragment = new LibraryBookFragment();
                ((LibraryBookFragment) fragment).book = book;
                fm.beginTransaction()
                        .add(R.id.activity_library_ll_container, fragment)
                        .commit();
            }
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
}
