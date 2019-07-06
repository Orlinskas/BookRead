package com.orlinskas.bookread.activities;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.orlinskas.bookread.Book;
import com.orlinskas.bookread.R;
import com.orlinskas.bookread.ToastBuilder;
import com.orlinskas.bookread.Word;
import com.orlinskas.bookread.data.DatabaseAdapter;
import com.orlinskas.bookread.fileManager.FileFormat;
import com.orlinskas.bookread.helpers.LibraryHelper;
import com.orlinskas.bookread.parsers.ListToArray;

import java.util.ArrayList;
import java.util.List;

import static com.orlinskas.bookread.fileManager.FileFormat.FB2;
import static com.orlinskas.bookread.fileManager.FileFormat.FOLDER;
import static com.orlinskas.bookread.fileManager.FileFormat.OTHER;
import static com.orlinskas.bookread.fileManager.FileFormat.TXT;

public class VocabularyActivity extends ListActivity {
    private ArrayList<Book> books;
    private ArrayList<Word> words = new ArrayList<>();
    private static final String FIRST_TITLE = "Выберете книгу...";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary);

        ImageView rowIconHelp = findViewById(R.id.activity_vocabulary_iv_help);
        ImageView rowIconSearch = findViewById(R.id.activity_vocabulary_iv_search);
        RelativeLayout rowRelativeLayout = findViewById(R.id.activity_vocabulary_rl);
        ProgressBar progressBar = findViewById(R.id.activity_vocabulary_pb);
        Spinner spinner = findViewById(R.id.activity_vocabulary_sp);

        rowIconHelp.setImageResource(R.drawable.ic_help_2_0);
        rowIconSearch.setImageResource(R.drawable.ic_file_manager_search);
        rowRelativeLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        progressBar.setVisibility(View.INVISIBLE);

        LibraryHelper libraryHelper = new LibraryHelper(this);
        try {
            books = libraryHelper.getBooks();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> adapterBookTitles = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, getBookTitles());
        spinner.setAdapter(adapterBookTitles);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String bookTitle = getBookTitles()[position];
                for(Book book : books) {
                    if(book.getBookTitle().equals(bookTitle)){
                        fillList(book);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private String[] getBookTitles() {
        String[] titles = new String[0];
        try {
            titles = new String[books.size() + 1];
            titles[0] = FIRST_TITLE;
            for(int i = 0; i < books.size(); i++) {
                titles[i + 1] = books.get(i).getBookTitle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return titles;
    }

    private boolean findWords(Book book) {
        try {
            DatabaseAdapter databaseAdapter = new DatabaseAdapter(this, book.getDataTableName());
            databaseAdapter.openWithTransaction();
            words = databaseAdapter.getWords(book.getDataTableName());
            databaseAdapter.closeWithTransaction();
            return words.size() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String[] getWordsRus(@NonNull ArrayList<Word> words) {
        String[] wordsRus = new String[words.size()];
        for(int i = 0; i < words.size(); i++)  {
            wordsRus[i] = words.get(i).getRussian();
        }
        return wordsRus;
    }

    private void fillList (Book book) {
        if(findWords(book)){
            this.setListAdapter(new VocabularyAdapter(this, R.layout.file_manager_row, getWordsRus(words)));
        }
        else {
            ToastBuilder.create(this,"Ошибка");
        }
    }

    private class VocabularyAdapter extends ArrayAdapter<String> {
        String[] wordsOnlyRussian;

        VocabularyAdapter(Context context, int textViewResourceId, String[] objects) {
            super(context, textViewResourceId, objects);
            wordsOnlyRussian = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            @SuppressLint("ViewHolder") View row = inflater.inflate(R.layout.vocabulary_row, parent, false);
            TextView rowTextEng = row.findViewById(R.id.vocabulary_row_eng_tv);
            TextView rowTextRus = row.findViewById(R.id.vocabulary_row_rus_tv);
            rowTextEng.setTextColor(getResources().getColor(R.color.colorAccent));
            rowTextRus.setTextColor(getResources().getColor(R.color.colorAccent));
            String russian = wordsOnlyRussian[position];
            String english = getEnglish(russian);
            rowTextRus.setText(russian);
            rowTextEng.setText(english);

            if (position%2 == 0) {
                try {
                    row.setBackgroundColor(getResources().getColor(R.color.colorLowGREY));
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
            }
            else{
                row.setBackgroundColor(getResources().getColor(R.color.colorWHITE));
            }
            return row;
        }
    }

    private String getEnglish(String russian) {
        for(Word word : words) {
            if(word.getRussian().equals(russian)){
                return word.getEnglish();
            }
        }
        return "Не найденно";
    }

}
