package com.orlinskas.bookread.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.orlinskas.bookread.Book;
import com.orlinskas.bookread.R;
import com.orlinskas.bookread.Settings;
import com.orlinskas.bookread.ToastBuilder;
import com.orlinskas.bookread.Word;
import com.orlinskas.bookread.bookReadAlgorithm.WordReplacer;
import com.orlinskas.bookread.data.DatabaseAdapter;
import com.orlinskas.bookread.data.SettingsData;
import com.orlinskas.bookread.data.SharedPreferencesData;
import com.orlinskas.bookread.fileManager.FileFormat;
import com.orlinskas.bookread.fragments.BookInfoPageFragment;
import com.orlinskas.bookread.helpers.ActivityOpenHelper;
import com.orlinskas.bookread.helpers.BookBodyFileReader;
import com.orlinskas.bookread.helpers.LibraryHelper;
import com.orlinskas.bookread.presenters.PercentProgress;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

//баг с открытием нужной страницы. видно первую, но открыта нужная
public class ReadActivity extends AppCompatActivity {
    private LinearLayout fragmentContainer;
    private RelativeLayout relativeLayout, settingsLayout, settingsTopLayout;
    private Button btnBack, btnNext, btnSettings, btnCloseSettings, btnCancelSettingsScroll;
    private ScrollView scrollView;
    private TextView bookText, currentPageSettings, bookInfoSettings;
    private Book book;
    private ArrayList<String> words;
    private ProgressBar progressBar, pageScrollProgress;
    private SeekBar seekBar;

    private int screenHeight;
    private int scrollHeight;
    public int fragmentHeight;
    private int countPages;
    private int realCountPages;
    private int currentPage = 0;
    private int oldCurrentPage;
    private ArrayList<Integer> pagePositions;
    private Settings settings;
    private boolean settingsOpen = false;
    private boolean needSaveProgress = false;
    private float bookProgress;
    private PercentProgress percentProgress = new PercentProgress();
    private boolean isOpenNeedProgressPage = false;

    //первая часть отображения
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_test);

        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        //нужен отдельный поток на загрузке
        btnBack = findViewById(R.id.activity_read_test_btn_back_page);
        btnNext = findViewById(R.id.activity_read_test_btn_next_page);
        btnSettings = findViewById(R.id.activity_read_test_btn_settings);
        bookText = findViewById(R.id.activity_read_test_ll_container_text);
        scrollView = findViewById(R.id.activity_read_test_sv);
        fragmentContainer = findViewById(R.id.activity_read_test_ll_container_book_info);
        progressBar = findViewById(R.id.activity_read_test_pb);
        relativeLayout = findViewById(R.id.activity_read_test_fl);
        seekBar = findViewById(R.id.activity_read_test_sb_page_scroller);
        currentPageSettings = findViewById(R.id.activity_read_test_tv_current_page);
        settingsLayout = findViewById(R.id.activity_read_test_ll_settings);
        settingsTopLayout = findViewById(R.id.activity_read_test_ll_settings_top);
        bookInfoSettings = findViewById(R.id.activity_read_test_tv_book_info);
        btnCloseSettings = findViewById(R.id.activity_read_test_btn_close_settings);
        btnCancelSettingsScroll = findViewById(R.id.activity_read_test_btn_cancel_scroll_settings);
        pageScrollProgress = findViewById(R.id.activity_read_test_pb_page_scroll);

        //registerForContextMenu(bookText);

        SettingsData settingsData = new SettingsData(this);
        settings = settingsData.loadSettings();

        SharedPreferencesData.setPreferences(getSharedPreferences(SharedPreferencesData.SETTINGS_AND_DATA, MODE_PRIVATE));

        switch (settings.getTheme()) {
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

        if (settings.isPortraitOrientation()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        LoadBook loadBook = new LoadBook();
        loadBook.execute();

        btnBack.setAlpha(0.0f);
        btnNext.setAlpha(0.0f);
        btnSettings.setAlpha(0.0f);
        seekBar.setVisibility(View.INVISIBLE);
        currentPageSettings.setVisibility(View.INVISIBLE);
        settingsLayout.setVisibility(View.INVISIBLE);
        settingsLayout.setAlpha(0.9f);
        settingsTopLayout.setVisibility(View.INVISIBLE);
        settingsTopLayout.setAlpha(0.9f);
        bookInfoSettings.setMaxLines(1);
        btnCloseSettings.setVisibility(View.INVISIBLE);
        scrollView.setClickable(false);
        scrollView.setFocusable(false);
        scrollView.setVerticalScrollBarEnabled(false);
        settingsTopLayout.setClickable(true);
        settingsLayout.setClickable(true);

        btnSettings.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                SendBookToVocabulary sendBookToVocabulary = new SendBookToVocabulary();
                sendBookToVocabulary.execute();
            return true;
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (settingsOpen) {
                    try {
                        btnCancelSettingsScroll.setVisibility(View.VISIBLE);
                        seekBar.setMax(realCountPages - 1);
                        seekBar.setProgress(currentPage + 1);
                        currentPageSettings.setText(String.format("%d/%d", progress + 1, realCountPages - 1));
                        openPage(progress);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class SendBookToVocabulary extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ToastBuilder.create(getApplicationContext(), "Открываем словарь");
        }

        @Override
        protected Void doInBackground(Void... parameter) {
            try {
                Intent i = new Intent(getApplicationContext(), VocabularyActivity.class);
                i.putExtra("book", book);
                startActivity(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
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

                bookProgress = SharedPreferencesData.getPreferenceUsingKey(book.getBookTitle(), 0.0f);

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

    private ArrayList<String> readBookFile(File file) throws Exception {
        BookBodyFileReader bookBodyFileReader = new BookBodyFileReader();
        return bookBodyFileReader.read(file);
    }

    private void showBookText(ArrayList<String> words) {
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<Word> firstEntriesWords = new ArrayList<>();

        if(book.isTrainingMode()) {
            DatabaseAdapter databaseAdapter = new DatabaseAdapter(this, book.getDataTableName());
            databaseAdapter.openWithTransaction();
            ArrayList<Word> englishWords = databaseAdapter.getWords(book.getDataTableName());
            databaseAdapter.closeWithTransaction();

            for(String word : words) {
                Word actualWord = new Word(word);
                WordReplacer wordReplacer = new WordReplacer();

                if(englishWords.contains(actualWord)) {
                    if(firstEntriesWords.contains(actualWord)) {
                        stringBuilder.append(wordReplacer.replace(actualWord, englishWords));
                    }
                    else {
                        stringBuilder.append(wordReplacer.firstReplace(actualWord, englishWords));
                        firstEntriesWords.add(actualWord);
                    }
                }
                else {
                    stringBuilder.append(word);
                }
            }
        }
        else {
            for(String word : words) {
                stringBuilder.append(word);
            }
        }
        setExampleParams();
        bookText.setText(stringBuilder);
    }

    private void setExampleParams() {
        bookText.setTextSize(settings.getTextSize());
        //bookText.setShadowLayer(1.0f, 0.0f, 1.0f, getResources().getColor(R.color.colorGREY));
        bookText.setTypeface(getTypeFaceCode(settings.getTypeface()));

        switch (settings.getTheme()){
            case 1:
                scrollView.setBackgroundColor(getResources().getColor(R.color.colorLowGREY));
                bookText.setTextColor(getResources().getColor(R.color.colorLowBlack));
                relativeLayout.setBackgroundColor(getResources().getColor(R.color.colorLowGREY));
                break;
            case 2:
                scrollView.setBackgroundColor(getResources().getColor(R.color.colorThemeTelegramBackground));
                bookText.setTextColor(getResources().getColor(R.color.colorLowGREY));
                relativeLayout.setBackgroundColor(getResources().getColor(R.color.colorThemeTelegramBackground));
                break;
            case 3:
                scrollView.setBackgroundColor(getResources().getColor(R.color.colorThemeVintageBackground));
                bookText.setTextColor(getResources().getColor(R.color.colorLowBlack));
                relativeLayout.setBackgroundColor(getResources().getColor(R.color.colorThemeVintageBackground));
                break;
        }

        if(settings.isPortraitOrientation()){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
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

    private void openPage(int pageNumber) {
        try {
            currentPage = pageNumber;
            scrollView.scrollTo(0, pagePositions.get(currentPage));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //вторая часть отображения
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

                            if (!isOpenNeedProgressPage & progressBar.getVisibility() == View.INVISIBLE & realCountPages > 50) {
                                try {
                                    OpenPageWithProgress openPageWithProgress = new OpenPageWithProgress();
                                    openPageWithProgress.execute();
                                    //openPageWithProgress(bookProgress);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
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
            pagePositions.add((screenHeight * i) + 7); //КОСТЫЛЬ С ЦИФРОЙ, так буквы не обрываются (почти)
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

    @SuppressLint("StaticFieldLeak")
    private class OpenPageWithProgress extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... parameter) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    openPageWithProgress(bookProgress = SharedPreferencesData.getPreferenceUsingKey(book.getBookTitle(), 0.0f));
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

    }

    private void openPageWithProgress (float bookProgress) {
         try {
             int needPage = percentProgress.getPageNumber(bookProgress, realCountPages);
             if(needPage != 0){
                 openPage(needPage);
                 if(currentPage == needPage){
                     isOpenNeedProgressPage = true;
                 }
             }
         } catch (Exception e) {
             e.printStackTrace();
         }

        showFirstMessageDialog();
     }

    //активная фаза чтения
    @SuppressLint("DefaultLocale")
    public void onClickBackPage(View view) {
        if (currentPage >= 1) {
            OpenPreviosPage openPreviosPage = new OpenPreviosPage();
            openPreviosPage.execute();
        }
        showPagesInfoFragment(currentPage, realCountPages);
    }

    public void onClickNextPage(View view) {
        if (currentPage < realCountPages) {
            OpenNextPage openNextPage = new OpenNextPage();
            openNextPage.execute();

        }
        showPagesInfoFragment(currentPage, realCountPages);
    }

    public void onClickSettings(View view) {
        if(settingsOpen){
            closeSettings();
        }
        else {
            openSettings();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class OpenNextPage extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pageScrollProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... parameter) {
            try {
                scrollView.scrollTo(0, pagePositions.get(currentPage + 1));
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            pageScrollProgress.setVisibility(View.INVISIBLE);
            if(result){
                currentPage++;
                needSaveProgress = true;
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class OpenPreviosPage extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pageScrollProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... parameter) {
            try {
                scrollView.scrollTo(0, pagePositions.get(currentPage - 1));
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            pageScrollProgress.setVisibility(View.INVISIBLE);
            if(result){
                currentPage--;
                needSaveProgress = true;
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private void openSettings() {
        try {
            btnCancelSettingsScroll.setVisibility(View.INVISIBLE);
            oldCurrentPage = currentPage;
            btnCloseSettings.setVisibility(View.VISIBLE);
            settingsTopLayout.setVisibility(View.VISIBLE);
            bookInfoSettings.setText(book.getBookTitle());
            currentPageSettings.setText(String.format("%d/%d", currentPage + 1, realCountPages - 1));
            seekBar.setVisibility(View.VISIBLE);
            seekBar.setMax(realCountPages - 1);
            seekBar.setProgress(currentPage + 1);
            currentPageSettings.setVisibility(View.VISIBLE);
            settingsLayout.setVisibility(View.VISIBLE);
            btnBack .setVisibility(View.INVISIBLE);
            btnNext .setVisibility(View.INVISIBLE);
            btnSettings.setVisibility(View.INVISIBLE);

            Animation animationTop = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.open_settings_top);
            settingsTopLayout.startAnimation(animationTop);
            Animation animationBot = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.open_settings_bottom);
            settingsLayout.startAnimation(animationBot);

            settingsOpen = true;
        } catch (Exception e) {
            e.printStackTrace();
            settingsOpen = false;
        }

    }

    private void closeSettings() {
        CloseSettings closeSettings = new CloseSettings();
        closeSettings.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class CloseSettings extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            settingsOpen = false;
            Animation animationTop = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.close_settings_top);
            settingsTopLayout.startAnimation(animationTop);
            Animation animationBot = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.close_settigs_bottom);
            settingsLayout.startAnimation(animationBot);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... parameter) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        settingsTopLayout.setVisibility(View.INVISIBLE);
                        btnCloseSettings.setVisibility(View.INVISIBLE);
                        seekBar.setVisibility(View.INVISIBLE);
                        currentPageSettings.setVisibility(View.INVISIBLE);
                        settingsLayout.setVisibility(View.INVISIBLE);
                        btnBack .setVisibility(View.VISIBLE);
                        btnNext .setVisibility(View.VISIBLE);
                        btnSettings.setVisibility(View.VISIBLE);
                        settingsOpen = false;
                    } catch (Exception e) {
                        e.printStackTrace();
                        settingsOpen = false;
                    }
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

    }

    //активная фаза открытого меню настроек
    @SuppressLint("DefaultLocale")
    public void onClickBackBtnSettings(View view) {
        if (settingsOpen) {
            try {
                seekBar.setMax(realCountPages - 1);
                seekBar.setProgress(oldCurrentPage);
                currentPageSettings.setText(String.format("%d/%d", oldCurrentPage, realCountPages - 1));
                openPage(oldCurrentPage);
                seekBar.setProgress(currentPage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onClickGoToSettings(View view) {
        try {
            SharedPreferencesData.savePreferenceUsingKey(book.getBookTitle(), percentProgress.getPercentProgress(currentPage, realCountPages));
            ActivityOpenHelper.openActivity(this, SettingsActivity.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClickBtnCloseSettings(View view) {
        closeSettings();
    }

    //фаза закрытия
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
                try {
                    openPageWithProgress(bookProgress = SharedPreferencesData.getPreferenceUsingKey(book.getBookTitle(), 0.0f));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (needSaveProgress) {
            SharedPreferencesData.savePreferenceUsingKey(book.getBookTitle(), percentProgress.getPercentProgress(currentPage, realCountPages));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // if (needSaveProgress) {
        //     SharedPreferencesData.savePreferenceUsingKey(book.getBookTitle(), percentProgress.getPercentProgress(currentPage, realCountPages));
        // }
    }

    @Override
    public void onBackPressed() {
        if (settingsOpen){
            closeSettings();
        }
        else {
            super.onBackPressed();
        }
    }

    public void showFirstMessageDialog() {
        if(!checkFirstRun()) {
            DialogInterface.OnClickListener okButtonListener = new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface arg0, int arg1) {
                }
            };

            DialogInterface.OnClickListener cancelButtonListener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    ActivityOpenHelper.openActivity(getApplicationContext(), HelpActivity.class);
                }
            };

            try {
                new AlertDialog.Builder(this)
                        .setTitle("Подсказка:") //title
                        .setMessage("Тап по краям экрана - перелистывание. Тап по центру - открыть меню. Зажать по центру - открыть словарь.") //message
                        .setPositiveButton("Ясно", okButtonListener) //positive button
                        .setNegativeButton("Помощь", cancelButtonListener) //negative button
                        .show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            SharedPreferencesData.setTrueFirstRun();
        }
    }

    public boolean checkFirstRun() {
        return SharedPreferencesData.getFirstRun();
    }
}
