package com.orlinskas.bookread.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;


import com.orlinskas.bookread.R;
import com.orlinskas.bookread.Settings;
import com.orlinskas.bookread.data.SettingsData;

public class SettingsActivity extends AppCompatActivity {
    Button vintageLine, telegramLine, defaultLine;
    RadioButton album, portair;
    Settings settings;
    Button apply;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        vintageLine = findViewById(R.id.activity_settings_btn_theme_vintage_line);
        telegramLine = findViewById(R.id.activity_settings_btn_theme_telegram_line);
        defaultLine = findViewById(R.id.activity_settings_btn_theme_default_line);
        album = findViewById(R.id.activity_read_test_rb_album);
        portair = findViewById(R.id.activity_read_test_rb_portair);
        apply = findViewById(R.id.activity_settings_btn_apply);

        SettingsData settingsData = new SettingsData(this);
        settings = settingsData.loadSettings();

        if(settings.isPortraitOrientation()) {
            portair.setChecked(true);
            album.setChecked(false);
        }
        else {
            portair.setChecked(false);
            album.setChecked(true);
        }

        switch (settings.getTheme()) {
            case 1:
                changeVisibleElement(defaultLine);
                break;
            case 2:
                changeVisibleElement(telegramLine);
                break;
            case 3:
                changeVisibleElement(vintageLine);
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveSettings();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void changeVisibleElement(View view){
        switch (view.getId()){
            case R.id.activity_settings_btn_theme_default_line:
                vintageLine.setVisibility(View.INVISIBLE);
                telegramLine.setVisibility(View.INVISIBLE);
                defaultLine.setVisibility(View.VISIBLE);
                break;
            case R.id.activity_settings_btn_theme_telegram_line:
                vintageLine.setVisibility(View.INVISIBLE);
                telegramLine.setVisibility(View.VISIBLE);
                defaultLine.setVisibility(View.INVISIBLE);
                break;
            case R.id.activity_settings_btn_theme_vintage_line:
                vintageLine.setVisibility(View.VISIBLE);
                telegramLine.setVisibility(View.INVISIBLE);
                defaultLine.setVisibility(View.INVISIBLE);
                break;

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickVintageTheme(View view) {
       changeVisibleElement(vintageLine);
       settings.setTheme(3);
    }

    public void onClickTelegramTheme(View view) {
       changeVisibleElement(telegramLine);
        settings.setTheme(2);
    }

    public void onClickDefaultTheme(View view) {
        changeVisibleElement(defaultLine);
        settings.setTheme(1);
    }

    public void onClickScreenOrientation(View view) {
        switch (view.getId()) {
            case R.id.activity_read_test_rb_album:
                portair.setChecked(false);
                settings.setPortraitOrientation(false);
                break;
            case R.id.activity_read_test_rb_portair:
                album.setChecked(false);
                settings.setPortraitOrientation(true);
                break;
        }
    }

    public void onClickApply(View view) {
        saveSettings();
    }

    private boolean saveSettings(){
        try {
            SettingsData settingsData = new SettingsData(this);
            settingsData.saveSettings(settings);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
