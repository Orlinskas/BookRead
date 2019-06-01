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
import com.orlinskas.bookread.ToastBuilder;
import com.orlinskas.bookread.data.LibraryData;
import com.orlinskas.bookread.fragments.LibraryBookFragment;
import com.orlinskas.bookread.helpers.LibraryHelper;

import java.util.ArrayList;

public class LibraryActivity extends AppCompatActivity {
    static int countFragments = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        try {
            LibraryHelper libraryHelper = new LibraryHelper(getApplicationContext());
            ArrayList<Book> books = libraryHelper.getBooks();
            showFragments(books);
        } catch (Exception e) {
            e.printStackTrace();
            ToastBuilder.create(getApplicationContext(), "Ошибка в загрузке библиотеки, обратитесь в поддержку");
        }
    }

    public void showFragments(ArrayList<Book> books) {
        FragmentManager fm = getSupportFragmentManager();

        for (Book book : books){
            Fragment fragment = fm.findFragmentById(R.id.activity_library_ll_container);
            if (fragment == null) {
                fragment = new LibraryBookFragment();
                ((LibraryBookFragment) fragment).book = book;
                ((LibraryBookFragment) fragment).countFragments = countFragments;
                fm.beginTransaction()
                        .add(R.id.activity_library_ll_container, fragment)
                        .commit();
                countFragments++;
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
