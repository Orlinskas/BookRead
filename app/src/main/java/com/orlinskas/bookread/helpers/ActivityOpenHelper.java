package com.orlinskas.bookread.helpers;

import android.content.Context;
import android.content.Intent;

import static android.support.v4.content.ContextCompat.startActivity;

public class ActivityOpenHelper {

        public static void openActivity(Context contextThisActivity, Class needActivity){
            Intent intent = new Intent(contextThisActivity, needActivity);
            startActivity(contextThisActivity, intent, null);
        }
}
