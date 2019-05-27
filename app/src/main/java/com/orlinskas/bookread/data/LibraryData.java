package com.orlinskas.bookread.data;

import android.content.Context;

import com.orlinskas.bookread.Library;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class LibraryData {
    private static final String FILE_LIBRARY_NAME = "serializedLibrary" + ".txt";
    private Context context;

    public LibraryData(Context context){
        this.context = context;
    }

    public Library loadLibrary() {
        String filePath = context.getFilesDir().getPath() + "/" + FILE_LIBRARY_NAME;
        File file = new File(filePath);

        try {
            ObjectInputStream ois =
                    new ObjectInputStream(new FileInputStream(file));

            return (Library) ois.readObject();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return new Library();
    }

    public void saveLibrary(Library library) {
        String filePath = context.getFilesDir().getPath() + "/" + FILE_LIBRARY_NAME;
        File file = new File(filePath);

        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(library);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
