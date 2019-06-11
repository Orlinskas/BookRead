package com.orlinskas.bookread.activities;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.orlinskas.bookread.Book;
import com.orlinskas.bookread.R;
import com.orlinskas.bookread.Settings;
import com.orlinskas.bookread.ToastBuilder;
import com.orlinskas.bookread.data.SettingsData;
import com.orlinskas.bookread.fragments.BookInfoPageFragment;
import com.orlinskas.bookread.helpers.ActivityOpenHelper;
import com.orlinskas.bookread.helpers.BookBodyFileReader;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class ReadActivity extends AppCompatActivity {
    LinearLayout fragmentContainer;
    RelativeLayout relativeLayout;
    Button btnBack, btnNext, btnSettings;
    ScrollView scrollView; //вылазит строка прокрутки сбоку, убрать
    TextView bookText;
    Book book;
    ArrayList<String> words;
    ProgressBar progressBar;

    int screenHeight;
    int scrollHeight;
    int fragmentHeight;
    int countPages;
    int realCountPages;
    int currentPage = 0;
    ArrayList<Integer> pagePositions;
    Settings settings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_test);

        Objects.requireNonNull(getSupportActionBar()).hide();

        //нужен отдельный поток на загрузке
        btnBack = findViewById(R.id.activity_read_test_btn_back_page);
        btnNext = findViewById(R.id.activity_read_test_btn_next_page);
        btnSettings = findViewById(R.id.activity_read_test_btn_settings);
        bookText = findViewById(R.id.activity_read_test_ll_container_text);
        scrollView = findViewById(R.id.activity_read_test_sv);
        fragmentContainer = findViewById(R.id.activity_read_test_ll_container_book_info);
        progressBar = findViewById(R.id.activity_read_test_pb);
        relativeLayout = findViewById(R.id.activity_read_test_fl);

        SettingsData settingsData = new SettingsData(this);
        settings = settingsData.loadSettings();

        switch (settings.getTheme()){
            case 1:
                relativeLayout.setBackgroundColor(getResources().getColor(R.color.colorLowGREY));
                break;
            case 2:
                relativeLayout.setBackgroundColor(getResources().getColor(R.color.colorThemeTelegramBackground));
                break;
            case 3:
                relativeLayout.setBackgroundColor(getResources().getColor(R.color.colorThemeVintageBackground));
                break;
        }

        if(settings.isPortraitOrientation()){
            setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        LoadBook loadBook = new LoadBook();
        loadBook.execute();

        btnBack.setAlpha(0.0f);
        btnNext.setAlpha(0.0f);
        btnSettings.setAlpha(0.0f);

        btnSettings.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Objects.requireNonNull(getSupportActionBar()).show();
                return true;
            }
        });

    }

    private void openPage(int pageNumber) {
        try {
            currentPage = pageNumber;
            scrollView.scrollTo(0, pagePositions.get(currentPage));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getPageNumber(int percentProgress, int allPages) {
        return (allPages * percentProgress) / 100;
    }

    private int getPersentProgress(int pageNumber, int allPages) {
        return (100 * pageNumber) / allPages;
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
        setExampleParams();
        bookText.setText(stringBuilder);
    }

    @Override
    protected void onStart() {
        super.onStart();

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
                            fragmentHeight = fragmentContainer.getHeight();

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
        return screenHeight - screenHeight % lineHeight;
    }

    private int countPages(int screenHeight, int scrollHeight) {
        int countPages;
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
        openPage(150);
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

    @SuppressLint("StaticFieldLeak")
    private class LoadBook extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            fragmentContainer.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Void doInBackground(Void... parameter) {
            try {
                book = (Book) getIntent().getSerializableExtra("book");
                words = readBookFile(book.getBookBodyFile());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showBookText(words);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            fragmentContainer.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);

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
            ActivityOpenHelper.openActivity(this, SettingsActivity.class);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Typeface getTypeFaceCode(int number) {
        switch (number) {
            case 0:
                return Typeface.DEFAULT;
            case 1:
                return Typeface.DEFAULT_BOLD;
            case 2:
                return Typeface.SERIF;
            case 3:
                return Typeface.SANS_SERIF;
            case 4:
                return Typeface.MONOSPACE;
            default:return Typeface.DEFAULT;
        }
    }

    private void setExampleParams() {
        bookText.setTextSize(settings.getTextSize());
        bookText.setShadowLayer(1.0f, 0.0f, 1.0f, getResources().getColor(R.color.colorGREY));
        bookText.setTypeface(getTypeFaceCode(settings.getTypeface()));

        switch (settings.getTheme()){
            case 1:
                scrollView.setBackgroundColor(getResources().getColor(R.color.colorLowGREY));
                bookText.setTextColor(getResources().getColor(R.color.colorLowBlack));
                relativeLayout.setBackgroundColor(getResources().getColor(R.color.colorLowGREY));
                break;
            case 2:
                scrollView.setBackgroundColor(getResources().getColor(R.color.colorThemeTelegramBackground));
                bookText.setTextColor(getResources().getColor(R.color.colorWHITE));
                relativeLayout.setBackgroundColor(getResources().getColor(R.color.colorThemeTelegramBackground));
                break;
            case 3:
                scrollView.setBackgroundColor(getResources().getColor(R.color.colorThemeVintageBackground));
                bookText.setTextColor(getResources().getColor(R.color.colorLowBlack));
                relativeLayout.setBackgroundColor(getResources().getColor(R.color.colorThemeVintageBackground));
                break;
        }

        if(settings.isPortraitOrientation()){
            setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SettingsData settingsData = new SettingsData(this);
        Settings currentSettings = settingsData.loadSettings();
        if(settings.isPortraitOrientation() != currentSettings.isPortraitOrientation()
                | settings.getTheme() != currentSettings.getTheme()
                | settings.getTextSize() != currentSettings.getTextSize()
                | settings.getTypeface() != currentSettings.getTypeface()){
            try {
                settings = currentSettings;
                setExampleParams();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
