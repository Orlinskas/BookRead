package com.orlinskas.bookread.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.orlinskas.bookread.Book;
import com.orlinskas.bookread.CustomPageAdapter;
import com.orlinskas.bookread.R;
import com.orlinskas.bookread.fragments.BookInfoPageFragment;
import com.orlinskas.bookread.fragments.LibraryBookFragment;
import com.orlinskas.bookread.helpers.BookBodyFileReader;

import java.io.File;
import java.util.ArrayList;

public class ReadActivity extends AppCompatActivity {
    LinearLayout containerText, containerBookInfo;
    TextView bookText;
    private int pageNumber = 0;
    private  int pageCount;
    private int sayCount;
    Book book;
    ArrayList<String> words;
    ArrayList<String> wordsOnFirstPage = new ArrayList<>();
    ScrollView scrollView;
    boolean scrollGo = false;
    boolean stopCollectFirstPage = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_test);

        try {
            containerBookInfo = findViewById(R.id.activity_read_test_ll_container_book_info);
            containerText = findViewById(R.id.activity_read_test_ll_container_text);
            //bookText = findViewById(R.id.activity_read_test_tv_book_text);

            book = (Book) getIntent().getSerializableExtra("book");
            words = readBookFile(book.getBookBodyFile());

            //scrollView.scrollTo(0, scrollView.getBottom());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //ViewPager pager = findViewById(R.id.pager);
        //pager.setAdapter(new CustomPageAdapter(getSupportFragmentManager()));
    }

    @Override
    protected void onStart() {
        super.onStart();
        scrollView = findViewById(R.id.activity_read_test_sv);
        scrollView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                scrollGo = true;
                return true;
            }
        });
        sayCount = countWordsOnPage(words);
        //pageCount = countPages(wordsOnFirstPage, words);
    }

    private int countWordsOnPage(ArrayList<String> words) {
        int countSay = 0;
        try {
            while (!scrollGo){
                    TextView say = new TextView(this);
                    say.setText(words.get(0));
                    containerText.addView(say);
                    countSay++;
                    //if(!stopCollectFirstPage) {
                    //    wordsOnFirstPage.add(words.get(0));
                    //    words.remove(0);
                    //}
                    scrollView.scrollTo(0, scrollView.getBottom());
            }
            pageNumber++;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return countSay;
    }

    private void showPagesInfoFragment(int pageNumber, int pageCount) {
        try {
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentById(R.id.activity_read_test_ll_container_book_info);

            if (fragment == null) {
                fragment = new BookInfoPageFragment();
                ((BookInfoPageFragment) fragment).pageNumber = pageNumber;
                ((BookInfoPageFragment) fragment).pageCount = pageCount;

                fm.beginTransaction()
                        .add(R.id.activity_read_test_ll_container_book_info, fragment)
                        .commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private ArrayList<String> readBookFile(File file) {
        BookBodyFileReader bookBodyFileReader = new BookBodyFileReader();
        return bookBodyFileReader.read(file);
    }

    private int countPages(ArrayList<String> pageWords, ArrayList<String> allWords) {
        int countPages = 0;
        try {
            int sumAllChar = allWords.toString().toCharArray().length;
            int sumPageChar = pageWords.toString().toCharArray().length;
            countPages = (sumAllChar/sumPageChar) + 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return countPages;
    }
}
