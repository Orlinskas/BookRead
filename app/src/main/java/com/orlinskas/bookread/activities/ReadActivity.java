package com.orlinskas.bookread.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    LinearLayout fragmentContainer;
    Button btnBack, btnNext, btnSettings;
    ScrollView scrollView;
    TextView bookText;
    Book book;
    ArrayList<String> words;

    int screenHeight;
    int scrollHeight;
    int fragmentHeight;
    int countPages;
    int realCountPages;
    int currentPage = 0;
    ArrayList<Integer> pagePositions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_test);

        //нужен отдельный поток на загрузке

        try {
            btnBack = findViewById(R.id.activity_read_test_btn_back_page);
            btnNext = findViewById(R.id.activity_read_test_btn_next_page);
            btnSettings = findViewById(R.id.activity_read_test_btn_settings);
            bookText = findViewById(R.id.activity_read_test_ll_container_text);
            scrollView = findViewById(R.id.activity_read_test_sv);
            fragmentContainer = findViewById(R.id.activity_read_test_ll_container_book_info);

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

        setTextParams();

            //ждет отображения, все самое важное происходит тут
        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (scrollView.getMeasuredHeight() != 0 & bookText.getMeasuredHeight() != 0) {
                    try {
                        final int screenHeightFINAL = scrollView.getMeasuredHeight();
                        final int scrollHeightFINAL = bookText.getMeasuredHeight();
                        screenHeight = correctedScreenHeight(screenHeightFINAL);
                        scrollHeight = scrollHeightFINAL;

                        countPages = countPages(screenHeight, scrollHeight);
                        pagePositions = createScrollPositions(screenHeight);
                        realCountPages = pagePositions.size();

                        showPagesInfoFragment(currentPage, realCountPages);
                        final int fragmentHeightFinal = fragmentContainer.getHeight();
                        fragmentHeight = fragmentHeightFinal;

                        //делаю красиво тут
                        RelativeLayout.LayoutParams scrollParams = (RelativeLayout.LayoutParams) scrollView.getLayoutParams();
                        scrollParams.height = screenHeight;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private int correctedScreenHeight(int screenHeight) {
        int lineHeight = bookText.getLineHeight();
        int correctScreenHeight = screenHeight - screenHeight % lineHeight;
        return correctScreenHeight;
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

    private ArrayList<Integer> createScrollPositions(int screenHeight) {
        ArrayList<Integer> pagePositions = new ArrayList<>();
        pagePositions.add(0);
        for (int i = 1; i < countPages; i++){
            pagePositions.add((screenHeight * i) + 7); //КОСТЫЛЬ С ЦИФРОЙ, так буквы не обрываются
        }
        if (pagePositions.get(pagePositions.size() - 1) < scrollHeight) {
            pagePositions.add(scrollHeight);
        }
        return pagePositions;
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
            scrollView.scrollTo(0, pagePositions.get(currentPage - 1));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean scrollToNextPage() {
        try {
            scrollView.scrollTo(0, pagePositions.get(currentPage + 1));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void setTextParams() {
        bookText.setTextSize(16.0f);
        bookText.setShadowLayer(1.0f, 0.0f, 1.0f, getResources().getColor(R.color.colorGREY));
    }

}
