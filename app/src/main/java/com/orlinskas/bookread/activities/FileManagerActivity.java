package com.orlinskas.bookread.activities;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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
import com.orlinskas.bookread.helpers.BookCreator;
import com.orlinskas.bookread.helpers.ActivityOpenHelper;
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
    private String pathRoot = Environment.getExternalStorageDirectory().getAbsolutePath();
    private String pathPrefix = "";
    private File currentDirectory = new File("/");
    private ProgressBar progressBar, progressBarCircle;
    private boolean castComplite = true;
    ImageView downloadImage;
    ImageView phoneImage;


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_file_manager);

        ImageView rowIconHelp = findViewById(R.id.activity_file_manager_iv_help);
        ImageView rowIconSearch = findViewById(R.id.activity_file_manager_iv_search);
        RelativeLayout rowRelativeLayout = findViewById(R.id.activity_file_manager_rl);
        progressBar = findViewById(R.id.activity_file_manager_pb);
        downloadImage = findViewById(R.id.activity_file_manager_iv_download);
        phoneImage = findViewById(R.id.activity_file_manager_iv_phone);
        progressBarCircle = findViewById(R.id.activity_file_manager_pb_circle);

        rowIconHelp.setImageResource(R.drawable.ic_help_2_0);
        rowIconSearch.setImageResource(R.drawable.ic_file_manager_search);
        rowRelativeLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        downloadImage.setImageResource(R.drawable.ic_file_manager_downloads);
        phoneImage.setImageResource(R.drawable.ic_file_manager_phone);
        progressBarCircle.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

        browseTo(new File(pathRoot));
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
        //ToastBuilder.create(this, "Укажите путь самостоятельно");
        //нужен класс поиска в отдельном потоке
    }

    public void helpButton(View view) {
        if (castComplite) {
            ActivityOpenHelper.openActivity(getApplicationContext(), HelpActivity.class);
        }
        else {
            ToastBuilder.create(getApplicationContext(), "Процесс не закончен! Подождите..");
        }
    }

    public void onClickDownload(View view) {
        browseTo(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()));
    }

    public void onClickHome(View view) {
        browseTo(new File(pathRoot));
    }


    private void browseTo(final File aDirectory){

        if(aDirectory.getAbsolutePath().equals(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath())){
            downloadImage.setAlpha(0.5f);
        }else {
            downloadImage.setAlpha(1.0f);
        }
        if(aDirectory.getAbsolutePath().equals(pathRoot)){
            phoneImage.setAlpha(0.5f);
        }else{
            phoneImage.setAlpha(1.0f);
        }

        if (aDirectory.isDirectory()){
            try {
                this.currentDirectory = aDirectory;
                fill(aDirectory.listFiles());

                TextView titleManager = findViewById(R.id.activity_file_manager_titleManager);
                titleManager.setTextColor(getResources().getColor(R.color.colorFileManager));
                titleManager.setText(cutPath(aDirectory.getAbsolutePath()));
                pathPrefix = titleManager.getText().toString();

            } catch (Exception e) {
                e.printStackTrace();
                ToastBuilder.create(getApplicationContext(), "Невозможно открыть, сейчас будет вылет");
            }
        }
        else {
            if(castComplite) {//обработчик нажатий
                DialogInterface.OnClickListener okButtonListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        ToastBuilder.create(getApplicationContext(), "Подождите пожалуйста");
                        CastFileToBookTask castFileToBookTask = new CastFileToBookTask();
                        castFileToBookTask.execute(aDirectory);

                        animationProgress();

                    }
                };

                DialogInterface.OnClickListener cancelButtonListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                };

                if (FileFormat.getFormat(aDirectory.getName()).equals(FileFormat.FB2)) {
                    new AlertDialog.Builder(this)
                            .setTitle("Подтверждение") //title
                            .setMessage("Хотите добавить книгу " + aDirectory.getName() + "?" + " Может занять несколько минут...") //message
                            .setPositiveButton("Да", okButtonListener) //positive button
                            .setNegativeButton("Нет", cancelButtonListener) //negative button
                            .show();
                } else {
                    ToastBuilder.create(getApplicationContext(), "Не является книгой!");
                }
            }
            else {
                ToastBuilder.create(getApplicationContext(), "Процесс не закончен! Подождите..");
            }
        }
    }

    private void upOneLevel(){
        if(this.currentDirectory.getParent() != null) {
            this.browseTo(this.currentDirectory.getParentFile());
        }
    }

    private void fill(File[] files) {

        this.directoryEntries.clear();

        if (!this.currentDirectory.getAbsolutePath().equals(pathRoot))
            this.directoryEntries.add("..");

        for (File file : files) {
            this.directoryEntries.add(file.getAbsolutePath());
        }
        //в параметр принимается только массив
        this.setListAdapter(new FileManagerAdapter(this, R.layout.file_manager_row, ListToArray.parse(directoryEntries)));
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
            rowText.setTextColor(getResources().getColor(R.color.colorFileManager));
            String path = directoryEntries.get(position);
            rowText.setText(cutPath(path));
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

    private String cutPath(String path) {
        String cutPath = path.replaceAll(pathRoot, "");
        return cutPath.replaceAll(pathPrefix, "");
    }

    @SuppressLint("StaticFieldLeak")
    class CastFileToBookTask extends AsyncTask<File, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarCircle.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            castComplite = false;
            ToastBuilder.create(getApplicationContext(), "Подождите...");
        }

        @Override
        protected Boolean doInBackground(File... files) {
            try {
                BookCreator bookCreator = new BookCreator();
                Book book = bookCreator.create(getApplicationContext(), files[0]);
                LibraryHelper libraryHelper = new LibraryHelper(getApplicationContext());
                return libraryHelper.addBook(book);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean root) {
            super.onPostExecute(root);
            if(root) {
                ToastBuilder.create(getApplicationContext(), "Книга добавлена в библиотеку!");
                ActivityOpenHelper.openActivity(getApplicationContext(), LibraryActivity.class);
            }
            else {
                ToastBuilder.create(getApplicationContext(), "Не удалось добавить книгу");
            }
            castComplite = true;
            progressBarCircle.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void animationProgress() {
        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(progressBar, "progress", 100);
        progressAnimator.setDuration(60000);
        progressAnimator.start();
    }

    @Override
    public void onBackPressed() {
        if(castComplite){
            super.onBackPressed();
        }
        else {
            ToastBuilder.create(getApplicationContext(), "Процесс не закончен! Подождите..");
        }
    }
}

