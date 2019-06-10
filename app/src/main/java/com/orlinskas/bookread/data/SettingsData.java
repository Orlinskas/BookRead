package com.orlinskas.bookread.data;

import android.content.Context;

import com.orlinskas.bookread.Settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SettingsData {
    private static final String FILE_LICENCE_NAME = "serializedData" + ".txt";
    private Context context;

    public SettingsData(Context context){
        this.context = context;
    }

    public Settings loadSettings() {
        String filePath = context.getFilesDir().getPath() + "/" + FILE_LICENCE_NAME;
        File file = new File(filePath);

        try {
            ObjectInputStream ois =
                    new ObjectInputStream(new FileInputStream(file));

            return (Settings) ois.readObject();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return new Settings();
    }

    public void saveSettings(Settings settings) throws Exception {
        String filePath = context.getFilesDir().getPath() + "/" + FILE_LICENCE_NAME;
        File file = new File(filePath);

        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(settings);
    }
}
