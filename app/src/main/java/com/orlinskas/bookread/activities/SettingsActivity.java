package com.orlinskas.bookread.activities;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;


import com.orlinskas.bookread.R;
import com.orlinskas.bookread.Settings;
import com.orlinskas.bookread.ToastBuilder;
import com.orlinskas.bookread.data.SettingsData;

public class SettingsActivity extends AppCompatActivity {
    Button vintageLine, telegramLine, defaultLine;
    RadioButton album, portair;
    Settings settings;
    Button apply;
    SeekBar seekBar;
    TextView example, textSizeNumber;
    Spinner spinner;

    @SuppressLint("SetTextI18n")
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
        seekBar = findViewById(R.id.activity_settings_sb_text_size);
        example = findViewById(R.id.activity_settings_tv_example);
        textSizeNumber = findViewById(R.id.activity_settings_tv_text_size_number);
        spinner = findViewById(R.id.activity_settings_spn_fonts);

        SettingsData settingsData = new SettingsData(this);
        settings = settingsData.loadSettings();

        setExampleParams();

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


        spinner.setSelection(settings.getTypeface());
        textSizeNumber.setText(Integer.toString(settings.getTextSize()));
        seekBar.setProgress(settings.getTextSize() - 8);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               String valueText;
               int value = progress + 8;
               valueText = Integer.toString(value);
               textSizeNumber.setText(valueText);
               settings.setTextSize(value);
               setExampleParams();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                switch (pos){
                    case 0:
                        settings.setTypeface(0);
                        setExampleParams();
                        settings.setTypeface(0);
                        break;
                    case 1:
                        settings.setTypeface(1);
                        setExampleParams();
                        settings.setTypeface(1);
                        break;
                    case 2:
                        settings.setTypeface(2);
                        setExampleParams();
                        settings.setTypeface(2);
                        break;
                    case 3:
                        settings.setTypeface(3);
                        setExampleParams();
                        settings.setTypeface(3);
                        break;
                    case 4:
                        settings.setTypeface(4);
                        setExampleParams();
                        settings.setTypeface(4);
                        break;
                }

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
       // try {
       //     saveSettings();
       //     ToastBuilder.create(this, "Настройки приняты");
       // } catch (Exception e) {
       //     e.printStackTrace();
       //     ToastBuilder.create(this, "Ошибка");
       // }
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
        setExampleParams();
    }

    public void onClickTelegramTheme(View view) {
       changeVisibleElement(telegramLine);
        settings.setTheme(2);
        setExampleParams();
    }

    public void onClickDefaultTheme(View view) {
        changeVisibleElement(defaultLine);
        settings.setTheme(1);
        setExampleParams();
    }

    public void onClickScreenOrientation(View view) {
        switch (view.getId()) {
            case R.id.activity_read_test_rb_album:
                portair.setChecked(false);
                album.setChecked(true);
                settings.setPortraitOrientation(false);
                break;
            case R.id.activity_read_test_rb_portair:
                album.setChecked(false);
                portair.setChecked(true);
                settings.setPortraitOrientation(true);
                break;
        }
    }

    public void onClickApply(View view) {
        try {
            saveSettings();
            ToastBuilder.create(this, "Настройки приняты");
        } catch (Exception e) {
            e.printStackTrace();
            ToastBuilder.create(this, "Ошибка");
        }
    }

    private boolean saveSettings() throws Exception{
        SettingsData settingsData = new SettingsData(this);
        settingsData.saveSettings(settings);
        return true;
    }

    private Typeface getTypeFaceCode(int number) {
        switch (number) {
            case 0:
                return Typeface.DEFAULT;
            case 1:
                return Typeface.DEFAULT_BOLD;
            case 2:
                return Typeface.SERIF;
            case 3:
                return Typeface.SANS_SERIF;
            case 4:
                return Typeface.MONOSPACE;
            default:return Typeface.DEFAULT;
        }
    }

    private void setExampleParams() {
        example.setTextSize(settings.getTextSize());
        example.setTypeface(getTypeFaceCode(settings.getTypeface()));

        switch (settings.getTheme()){
            case 1:
                example.setBackgroundColor(getResources().getColor(R.color.colorLowGREY));
                example.setTextColor(getResources().getColor(R.color.colorLowBlack));
                break;
            case 2:
                example.setBackgroundColor(getResources().getColor(R.color.colorThemeTelegramBackground));
                example.setTextColor(getResources().getColor(R.color.colorWHITE));
                break;
            case 3:
                example.setBackgroundColor(getResources().getColor(R.color.colorThemeVintageBackground));
                example.setTextColor(getResources().getColor(R.color.colorLowBlack));
                break;
        }

    }
}
