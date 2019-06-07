package com.orlinskas.bookread.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.orlinskas.bookread.Book;
import com.orlinskas.bookread.R;
import com.orlinskas.bookread.ToastBuilder;
import com.orlinskas.bookread.fragments.BookInfoPageFragment;
import com.orlinskas.bookread.helpers.BookBodyFileReader;

import java.io.File;
import java.util.ArrayList;

public class ReadActivity extends AppCompatActivity {
    LinearLayout invisibleLayout, fragmentContainer;
    Button btnBack, btnNext, btnSettings;
    ScrollView scrollView;
    TextView bookText;
    Book book;
    ArrayList<String> words;

    boolean listening = true;
    int screenHeight;
    int scrollHeight;
    int countPages;
    int currentPage = 0;
    ArrayList<Integer> pagePositions;
    int realCountPages;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_test);

        try {
            invisibleLayout = findViewById(R.id.activity_read_test_ll_invisible);
            btnBack = findViewById(R.id.activity_read_test_btn_back_page);
            btnNext = findViewById(R.id.activity_read_test_btn_next_page);
            btnSettings = findViewById(R.id.activity_read_test_btn_settings);
            bookText = findViewById(R.id.activity_read_test_ll_container_text);
            scrollView = findViewById(R.id.activity_read_test_sv);
            fragmentContainer = findViewById(R.id.activity_read_test_ll_container_book_info);

            invisibleLayout.setAlpha(0.0f);
            btnBack.setAlpha(0.0f);
            btnNext.setAlpha(0.0f);
            btnSettings.setAlpha(0.0f);

            book = (Book) getIntent().getSerializableExtra("book");
            words = readBookFile(book.getBookBodyFile());

            showBookText(words);

        } catch (Exception e) {
            e.printStackTrace();
            ToastBuilder.create(this, "Неудалось открыть книгу, попробуйте удалить из библиотеки");
        }
    }

    private ArrayList<String> readBookFile(File file) throws Exception {
        BookBodyFileReader bookBodyFileReader = new BookBodyFileReader();
        return bookBodyFileReader.read(file);
    }

    private void showBookText(ArrayList<String> words) {
        StringBuilder stringBuilder = new StringBuilder();
        for(String word : words) {
            stringBuilder.append(word);
        }
        bookText.setText(stringBuilder);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(listening) {
            invisibleLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (invisibleLayout.getMeasuredHeight() != 0 & bookText.getMeasuredHeight() != 0) {
                        try {
                            final int screenHeightFINAL = invisibleLayout.getMeasuredHeight();
                            final int scrollHeightFINAL = bookText.getMeasuredHeight();
                            screenHeight = screenHeightFINAL;
                            scrollHeight = scrollHeightFINAL;
                            countPages = countPages(screenHeight, scrollHeight);
                            pagePositions = createScrollPositions(screenHeight);
                            realCountPages = pagePositions.size();
                            showPagesInfoFragment(currentPage, realCountPages);
                            listening = false;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        invisibleLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        invisibleLayout.setVisibility(View.GONE);
                    }
                }
            });
        }

    }

    private void showPagesInfoFragment(int pageNumber, int pageCount) {
        try {
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentById(R.id.activity_read_test_ll_container_book_info);

            if (fragment == null) {
                fragment = new BookInfoPageFragment();
                ((BookInfoPageFragment) fragment).pageNumber = pageNumber + 1;
                ((BookInfoPageFragment) fragment).pageCount = pageCount - 1;

                fm.beginTransaction()
                        .add(R.id.activity_read_test_ll_container_book_info, fragment)
                        .commit();
            }
            else {
                fragment = new BookInfoPageFragment();
                ((BookInfoPageFragment) fragment).pageNumber = pageNumber + 1;
                ((BookInfoPageFragment) fragment).pageCount = pageCount - 1;

                fm.beginTransaction()
                        .replace(R.id.activity_read_test_ll_container_book_info, fragment)
                        .commit();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private int countPages(int screenHeight, int scrollHeight) {
        int countPages = 0;
        try {
            countPages = (scrollHeight/screenHeight) + 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 999;
        }
        return countPages;
    }

    public void onClickBackPage(View view) {
        if(scrollToBackPage()){
            currentPage--;
        }
        showPagesInfoFragment(currentPage, realCountPages);
    }

    public void onClickNextPage(View view) {
        if(scrollToNextPage()){
            currentPage++;
        }
        showPagesInfoFragment(currentPage, realCountPages);
    }

    public void onClickSettings(View view) {
        ToastBuilder.create(this, "Настройки");
    }

    private boolean scrollToBackPage() {
        try {
            int testValue = pagePositions.get(currentPage - 1);
            scrollView.scrollTo(0, pagePositions.get(currentPage - 1));
            ToastBuilder.create(this, "back");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean scrollToNextPage() {
        try {
            int testValue = pagePositions.get(currentPage + 1);
            scrollView.scrollTo(0, pagePositions.get(currentPage + 1));
            ToastBuilder.create(this, "next");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private ArrayList<Integer> createScrollPositions(int screenHeight) {
        ArrayList<Integer> pagePositions = new ArrayList<>();
        pagePositions.add(0);
        for (int i = 1; i < countPages; i++){
            pagePositions.add(screenHeight*i);
        }
        if (pagePositions.get(pagePositions.size() - 1) < scrollHeight) {
            pagePositions.add(scrollHeight);
        }
        return pagePositions;
    }
}
