package com.orlinskas.bookread.helpers;

import android.content.Context;
import android.content.Intent;

import static android.support.v4.content.ContextCompat.startActivity;
/**
 * @author Orlinskas
 * @version 1
 */
public class ActivityOpenHelper {

        public static void openActivity(Context contextThisActivity, Class needActivity){
            Intent intent = new Intent(contextThisActivity, needActivity);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(contextThisActivity, intent, null);
        }
}
