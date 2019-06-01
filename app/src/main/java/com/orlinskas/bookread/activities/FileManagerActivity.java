package com.orlinskas.bookread.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orlinskas.bookread.Book;
import com.orlinskas.bookread.R;
import com.orlinskas.bookread.ToastBuilder;
import com.orlinskas.bookread.fileManager.FileFormat;
import com.orlinskas.bookread.fileManager.Opener;
import com.orlinskas.bookread.helpers.ActivityOpenHelper;
import com.orlinskas.bookread.helpers.BookCreator;
import com.orlinskas.bookread.helpers.LibraryHelper;
import com.orlinskas.bookread.parsers.ListToArray;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.orlinskas.bookread.fileManager.FileFormat.FB2;
import static com.orlinskas.bookread.fileManager.FileFormat.FOLDER;
import static com.orlinskas.bookread.fileManager.FileFormat.OTHER;
import static com.orlinskas.bookread.fileManager.FileFormat.TXT;

public class FileManagerActivity extends ListActivity {
    private List<String> directoryEntries = new ArrayList<>();
    private File currentDirectory = new File("/");
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_file_manager);
        browseTo(new File("/sdcard/Download/"));

        ImageView rowIconHelp = findViewById(R.id.activity_file_manager_iv_help);
        ImageView rowIconSearch = findViewById(R.id.activity_file_manager_iv_search);
        RelativeLayout rowRelativeLayout = findViewById(R.id.activity_file_manager_rl);
        progressBar = findViewById(R.id.activity_file_manager_pb);

        rowIconHelp.setImageResource(R.drawable.ic_file_manager_help);
        rowIconSearch.setImageResource(R.drawable.ic_file_manager_search);
        rowRelativeLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        progressBar.setVisibility(View.INVISIBLE);

    }

    private void upOneLevel(){
        if(this.currentDirectory.getParent() != null) {
            this.browseTo(this.currentDirectory.getParentFile());
        }
    }

    private void browseTo(final File aDirectory){

        assert aDirectory != null;
        if (aDirectory.isDirectory()){
            try {
                this.currentDirectory = aDirectory;
                fill(aDirectory.listFiles());

                TextView titleManager = findViewById(R.id.titleManager);
                titleManager.setText(aDirectory.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
                ToastBuilder.create(getApplicationContext(), "Невозможно открыть, сейчас будет вылет, не волнуйтесь)");
            }
        }
        else { //обработчик нажатий


            DialogInterface.OnClickListener okButtonListener = new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface arg0, int arg1) {
                    progressBar.setVisibility(View.VISIBLE);

                    if(castFileToBookAndAddToLibrary(aDirectory)){
                        ToastBuilder.create(getApplicationContext(), "Книга добавлена в библиотеку!)");
                    }
                    else {
                        ToastBuilder.create(getApplicationContext(), "Ошибка, не удалось открыть книгу!");
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                }
            };

            DialogInterface.OnClickListener cancelButtonListener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                }
            };

            if (FileFormat.getFormat(aDirectory.getName()).equals(FileFormat.FB2)){
                new AlertDialog.Builder(this)
                        .setTitle("Подтверждение") //title
                        .setMessage("Хотите добавить книгу "+ aDirectory.getName() + "?") //message
                        .setPositiveButton("Да", okButtonListener) //positive button
                        .setNegativeButton("Нет", cancelButtonListener) //negative button
                        .show();
            }
            else {
                ToastBuilder.create(getApplicationContext(), "Не является книгой!");
            }


        }
    }

    private void fill(File[] files) {

        this.directoryEntries.clear();

        if (this.currentDirectory.getParent() != null)
            this.directoryEntries.add("..");

        for (File file : files) {
            this.directoryEntries.add(file.getAbsolutePath());
        }
        //в параметр принимается только массив
        this.setListAdapter(new FileManagerAdapter(this, R.layout.file_manager_row, ListToArray.parse(directoryEntries)));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String selectedFileString = this.directoryEntries.get(position);

        if(selectedFileString.equals("..")){
            this.upOneLevel();
        }
        else {
            try {
                File clickedFile;
                clickedFile = new File(selectedFileString);
                this.browseTo(clickedFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void searchButton(View view) {
        ToastBuilder.create(this, "Поиск");
        //нужен класс поиска в отдельном потоке
    }

    public void helpButton(View view) {
        ActivityOpenHelper.openActivity(getApplicationContext(), HelpActivity.class);
    }

    private class FileManagerAdapter extends ArrayAdapter<String> {

        FileManagerAdapter(Context context, int textViewResourceId, String[] objects) {
            super(context, textViewResourceId, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            @SuppressLint("ViewHolder") View row = inflater.inflate(R.layout.file_manager_row, parent, false);
            TextView rowText = row.findViewById(R.id.file_manager_row_tv);
            String path = directoryEntries.get(position);
            rowText.setText(path);
            ImageView rowIcon = row.findViewById(R.id.file_manager_row_image);

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

            if (path.equals("..")){
                rowIcon.setImageResource(R.drawable.ic_file_manager_row_back);
            }
            else {
                switch(FileFormat.getFormat(path)) {
                    case FB2:
                        rowIcon.setImageResource(R.drawable.ic_book_fb2);
                        break;

                    case TXT:
                        rowIcon.setImageResource(R.drawable.ic_file);
                        break;

                    case OTHER:
                        rowIcon.setImageResource(R.drawable.ic_file);
                        break;

                    case FOLDER:
                        rowIcon.setImageResource(R.drawable.ic_folder);
                        break;

                    default:
                        rowIcon.setImageResource(R.drawable.ic_file);
                        break;
                }
            }
            return row;
        }
    }

    private boolean castFileToBookAndAddToLibrary (File file){
        try {
            Opener opener = new Opener();
            BookCreator bookCreator = new BookCreator();
            Book book = bookCreator.create(getApplicationContext(), opener.open(file));
            LibraryHelper libraryHelper = new LibraryHelper(getApplicationContext());
            if(libraryHelper.addBook(book)){
                ToastBuilder.create(getApplicationContext(), "Книга добавлена в библиотеку!)");
                ActivityOpenHelper.openActivity(getApplicationContext(), LibraryActivity.class);
            }
            else {
                ToastBuilder.create(getApplicationContext(), "Книга УЖЕ в библиотеке!!!");
                return false;
            }

            //нужно исключить добавление одних и тех же книг, используй либрари хелпер
        } catch (Exception e) {
            ToastBuilder.create(getApplicationContext(), "Ошибка, не удалось открыть книгу!");
            e.printStackTrace();
            return false;
        }
        return true;
    }
}

