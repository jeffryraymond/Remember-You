package com.jeffryRaymond.rememberYou.data.database.models;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Jeffry Raymond on 2018-05-08.
 */

public class Message {
    public static void message(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
