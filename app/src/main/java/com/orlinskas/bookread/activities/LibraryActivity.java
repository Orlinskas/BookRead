package com.orlinskas.bookread.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.orlinskas.bookread.Book;
import com.orlinskas.bookread.R;
import com.orlinskas.bookread.ToastBuilder;
import com.orlinskas.bookread.data.SharedPreferencesData;
import com.orlinskas.bookread.fragments.LibraryBookFragment;
import com.orlinskas.bookread.helpers.ActivityOpenHelper;
import com.orlinskas.bookread.helpers.LibraryHelper;
import com.orlinskas.bookread.interfaces.FragmentLibraryBookActions;

import java.util.ArrayList;

public class LibraryActivity extends AppCompatActivity implements FragmentLibraryBookActions {
    static int countFragments = 1;
    LinearLayout linearLayout, backgrounOnOpenMenu;
    ScrollView scrollView;
    RelativeLayout parentMenuAnnotation;
    ImageView menuImageBook;
    TextView authorName, bookTitle, annotation;
    Book clickedBook;
    boolean isOpenMenu = false;
    ProgressBar progressBar;
    private Float bookProgress = 0.0f;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        scrollView = findViewById(R.id.activity_library_ll);
        linearLayout = findViewById(R.id.activity_library_ll_container);

        parentMenuAnnotation = findViewById(R.id.activity_library_rl_annotation_menu);
        menuImageBook = findViewById(R.id.activity_library_rl_annotation_menu_image);
        authorName = findViewById(R.id.activity_library_rl_annotation_menu_author);
        bookTitle = findViewById(R.id.activity_library_rl_annotation_menu_title);
        progressBar = findViewById(R.id.activity_library_rl_annotation_menu_pb);
        annotation = findViewById(R.id.activity_library_rl_annotation_menu_annotation_text);
        backgrounOnOpenMenu = findViewById(R.id.activity_library_pelena);

        progressBar.setIndeterminate(false);
        backgrounOnOpenMenu.setVisibility(View.INVISIBLE);

        try {
            LibraryHelper libraryHelper = new LibraryHelper(getApplicationContext());
            ArrayList<Book> books = libraryHelper.getBooks();
            for(Book book : books){
                if(book==null){
                    libraryHelper.deleteAllBook();
                    ToastBuilder.create(getApplicationContext(), "Произошла ошибка, загрузите книги заново");
                    ActivityOpenHelper.openActivity(getApplicationContext(), LibraryActivity.class);
                }
            }
            showFragments(books);

        } catch (Exception e) {
            e.printStackTrace();
            ToastBuilder.create(getApplicationContext(), "Ошибка в загрузке библиотеки, обратитесь в поддержку");
        }

        setBackGroundColor();
    }

    private void setBackGroundColor() {
        try {
            if(countFragments%2 == 0) {
                scrollView.setBackgroundColor(getResources().getColor(R.color.colorWHITE));
            }
            else {
                scrollView.setBackgroundColor(getResources().getColor(R.color.colorLowGREY));
            }
        } catch (Resources.NotFoundException e) {
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
               //((LibraryBookFragment) fragment).context = getApplicationContext() ;
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
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            ActivityOpenHelper.openActivity(getApplicationContext(), SettingsActivity.class);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override //или показать меню
    public boolean showBookAnnotation(Book book) {
        try {
            bookProgress = SharedPreferencesData.getPreferenceUsingKey(book.getBookTitle(), 0.0f);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (bookProgress > 0.0f){
            openBook(book);
        }
        else {
            try {
                Animation animation;
                animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.open_library_menu);
                parentMenuAnnotation.startAnimation(animation);
                backgrounOnOpenMenu.setVisibility(View.VISIBLE);
                if (book.getCoverImage() == null) {
                    menuImageBook.setImageResource(book.getCoverImagePath());
                }else {
                    menuImageBook.setImageURI(Uri.parse(book.getCoverImage().getAbsolutePath()));
                }
                authorName.setText(book.getAuthorName());
                bookTitle.setText(book.getBookTitle());

                if(book.getAnnotation().length() < 10){
                    annotation.setText("Приятного чтения!");
                }
                else {
                    annotation.setText(book.getAnnotation());
                }

                clickedBook = book;
                parentMenuAnnotation.setVisibility(View.VISIBLE);
                isOpenMenu = true;
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
                openBook(book);
            }
            return true;
        }
        return false;
    }

    public void onClickCloseMenu(View view) {
        closeMenu();
    }

    public void onClickReadBook(View view) {
        progressBar.setIndeterminate(true);
        openBook(clickedBook);
    }

    private void closeMenu(){
        if(isOpenMenu) {
            Animation animation;
            animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.close_library_menu);
            parentMenuAnnotation.startAnimation(animation);
            backgrounOnOpenMenu.setVisibility(View.INVISIBLE);
            parentMenuAnnotation.setVisibility(View.INVISIBLE);
            isOpenMenu = false;
        }
    }

    @Override
    public void openBook(Book book) {
        Intent i = new Intent(getApplicationContext(), ReadActivity.class);
        i.putExtra("book", book);
        startActivity(i);
    }

    @Override
    public boolean deleteBook(final Book book) {

        DialogInterface.OnClickListener okButtonListener = new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface arg0, int arg1) {
                LibraryHelper libraryHelper = new LibraryHelper(getApplicationContext());

                try {
                    if(libraryHelper.deleteBook(book)){
                        ToastBuilder.create(getApplicationContext(), "Удалено");
                        ActivityOpenHelper.openActivity(getApplicationContext(), LibraryActivity.class);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastBuilder.create(getApplicationContext(), "Не удалось выполнить удаление");
                }

            }
        };

        DialogInterface.OnClickListener cancelButtonListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                ToastBuilder.create(getApplicationContext(), "Ну и правильно, много читать не бывает)");
            }
        };

        try {
            new AlertDialog.Builder(this)
                    .setTitle("Подтверждение") //title
                    .setMessage("Хотите удалить книгу из библиотеки "
                            + book.getBookTitle().substring(0, 10) + "...?") //message
                    .setPositiveButton("Да", okButtonListener) //positive button
                    .setNegativeButton("Нет", cancelButtonListener) //negative button
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
            ToastBuilder.create(getApplicationContext(), "Не надо)");
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if(isOpenMenu){
            closeMenu();
        }
        else {
            ActivityOpenHelper.openActivity(this, MainActivity.class);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setIndeterminate(false);
    }
}
