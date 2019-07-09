package com.orlinskas.bookread.data;

import android.content.Context;

import com.orlinskas.bookread.Licence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class LicenceData {
    private static final String FILE_LICENCE_NAME = "serializedLicence" + ".txt";
    private Context context;

    public LicenceData(Context context){
        this.context = context;
    }

    public Licence loadLicence() {
        String filePath = context.getFilesDir().getPath() + "/" + FILE_LICENCE_NAME;
        File file = new File(filePath);

        try {
            ObjectInputStream ois =
                    new ObjectInputStream(new FileInputStream(file));

            return (Licence) ois.readObject();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return new Licence();
    }

    public void saveLicence(Licence licence) {
        String filePath = context.getFilesDir().getPath() + "/" + FILE_LICENCE_NAME;
        File file = new File(filePath);

       // try (FileOutputStream fos = new FileOutputStream(file);
       //      ObjectOutputStream oos = new ObjectOutputStream(fos)) {
       //     oos.writeObject(licence);
       // }
       // catch (Exception e){
       //     e.printStackTrace();
       // }
    }
}
