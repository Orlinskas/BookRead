package com.orlinskas.bookread.activities;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.orlinskas.bookread.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OpenBookActivity extends ListActivity {
    private List<String> directoryEntries = new ArrayList<String>();
    private File currentDirectory = new File("/");
    //when application started
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        //set main layout
        setContentView(R.layout.activity_open_book);
        //browse to root directory
        browseTo(new File("/"));
    }

    //browse to parent directory
    private void upOneLevel(){
        if(this.currentDirectory.getParent() != null) {
            this.browseTo(this.currentDirectory.getParentFile());
        }
    }

    //browse to file or directory
    private void browseTo(final File aDirectory){
        //if we want to browse directory
        if (aDirectory.isDirectory()){
            //fill list with files from this directory
            this.currentDirectory = aDirectory;
            fill(aDirectory.listFiles());

            //set titleManager text
            TextView titleManager = (TextView) findViewById(R.id.titleManager);
            titleManager.setText(aDirectory.getAbsolutePath());
        } else {
            //if we want to open file, show this dialog:
            //listener when YES button clicked
            DialogInterface.OnClickListener okButtonListener = new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface arg0, int arg1) {
                    //intent to navigate file
                    Intent i = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("file://" + aDirectory.getAbsolutePath()));
                    //start this activity
                    startActivity(i);
                }
            };
            //listener when NO button clicked
            DialogInterface.OnClickListener cancelButtonListener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    //do nothing
                    //or add something you want
                }
            };

            //create dialog
            new AlertDialog.Builder(this)
                    .setTitle("Подтверждение") //title
                    .setMessage("Хотите открыть файл "+ aDirectory.getName() + "?") //message
                    .setPositiveButton("Да", okButtonListener) //positive button
                    .setNegativeButton("Нет", cancelButtonListener) //negative button
                    .show(); //show dialog
        }
    }
    //fill list
    private void fill(File[] files) {
        //clear list
        this.directoryEntries.clear();

        if (this.currentDirectory.getParent() != null)
            this.directoryEntries.add("..");

        //add every file into list
        for (File file : files) {
            this.directoryEntries.add(file.getAbsolutePath());
        }

        //create array adapter to show everything
        ArrayAdapter<String> directoryList = new ArrayAdapter<String>(this, R.layout.file_manager_row, this.directoryEntries);
        this.setListAdapter(directoryList);
    }
    //when you clicked onto item
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        //get selected file name
        int selectionRowID = position;
        String selectedFileString = this.directoryEntries.get(selectionRowID);

        //if we select ".." then go upper
        if(selectedFileString.equals("..")){
            this.upOneLevel();
        } else {
            //browse to clicked file or directory using browseTo()
            File clickedFile = null;
            clickedFile = new File(selectedFileString);
            if (clickedFile != null)
                this.browseTo(clickedFile);
        }
    }
}

