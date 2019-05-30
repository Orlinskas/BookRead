package com.orlinskas.bookread.activities;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.orlinskas.bookread.R;
import com.orlinskas.bookread.constants.FormatConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileManagerActivity extends ListActivity {
    private List<String> directoryEntries = new ArrayList<>();
    private File currentDirectory = new File("/");

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_file_manager);
        browseTo(new File("/sdcard/Download/"));
    }

    private void upOneLevel(){
        if(this.currentDirectory.getParent() != null) {
            this.browseTo(this.currentDirectory.getParentFile());
        }
    }

    private void browseTo(final File aDirectory){

        if (aDirectory.isDirectory()){
            this.currentDirectory = aDirectory;
            fill(aDirectory.listFiles());

            TextView titleManager = (TextView) findViewById(R.id.titleManager);
            titleManager.setText(aDirectory.getAbsolutePath());
        }
        else {
            DialogInterface.OnClickListener okButtonListener = new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface arg0, int arg1) {

                    Intent i = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("file://" + aDirectory.getAbsolutePath()));

                    startActivity(i);
                }
            };

            DialogInterface.OnClickListener cancelButtonListener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                }
            };

            new AlertDialog.Builder(this)
                    .setTitle("Подтверждение") //title
                    .setMessage("Хотите открыть файл "+ aDirectory.getName() + "?") //message
                    .setPositiveButton("Да", okButtonListener) //positive button
                    .setNegativeButton("Нет", cancelButtonListener) //negative button
                    .show(); //show dialog
        }
    }

    private void fill(File[] files) {

        this.directoryEntries.clear();

        if (this.currentDirectory.getParent() != null)
            this.directoryEntries.add("..");

        for (File file : files) {
            this.directoryEntries.add(file.getAbsolutePath());
        }

        //create array adapter to show everything
        //ArrayAdapter<String> directoryList = new ArrayAdapter<>(this, R.layout.file_manager_row, R.id.file_manager_row_tv, this.directoryEntries);

        //в параметр принимается только массив
        this.setListAdapter(new FileManagerAdapter(this, R.layout.file_manager_row, parseToArray(directoryEntries)));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        int selectionRowID = position;
        String selectedFileString = this.directoryEntries.get(selectionRowID);

        if(selectedFileString.equals("..")){
            this.upOneLevel();
        }
        else {
            File clickedFile = null;
            clickedFile = new File(selectedFileString);
            if (clickedFile != null)
                this.browseTo(clickedFile);
        }
    }

    private class FileManagerAdapter extends ArrayAdapter<String> {

        FileManagerAdapter(Context context, int textViewResourceId, String[] objects) {
            super(context, textViewResourceId, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.file_manager_row, parent, false);
            TextView rowText = (TextView) row.findViewById(R.id.file_manager_row_tv);
            rowText.setText(directoryEntries.get(position));
            ImageView rowIcon = (ImageView) row.findViewById(R.id.file_manager_row_image);

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

            String rowName = directoryEntries.get(position);
            String format;
            try {
                format = rowName.substring(rowName.length() - 4);
            } catch (Exception e) {
                e.printStackTrace();
                format = "";
            }

            if (directoryEntries.get(position).equals("..")){
                rowIcon.setImageResource(R.drawable.ic_file_manager_row_back);
            }
            else {
                if (format.equals(FormatConstants.FB2)) {
                    rowIcon.setImageResource(R.drawable.ic_book_fb2);
                }
                else {
                    rowIcon.setImageResource(R.drawable.ic_folder);
                }
            }
            return row;
        }
    }

    private String[] parseToArray(List<String> directoryEntries) {
        String[] entries = new String[directoryEntries.size()];

        for(int i = 0; i < entries.length; i++){
            entries[i] = directoryEntries.get(i);
        }
        return entries;
    }
}

