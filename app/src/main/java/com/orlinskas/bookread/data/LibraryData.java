package com.orlinskas.bookread.data;

import android.content.Context;

import com.orlinskas.bookread.Library;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class LibraryData {
    private static final String FILE_LIBRARY_NAME = "serializedLibrary" + ".txt";
    private Context context;

    public LibraryData(Context context){
        this.context = context;
    }

    public Library loadLibrary() throws Exception {
        String filePath = context.getFilesDir().getPath() + "/" + FILE_LIBRARY_NAME;
        File file = new File(filePath);

        if(file.exists() && !file.isDirectory()) {
            ObjectInputStream ois =
                    new ObjectInputStream(new FileInputStream(file));

            return (Library) ois.readObject();
        }
        else return new Library();
    }

    public void saveLibrary(Library library) throws Exception {
        String filePath = context.getFilesDir().getPath() + "/" + FILE_LIBRARY_NAME;
        File file = new File(filePath);
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(library);
    }
}
