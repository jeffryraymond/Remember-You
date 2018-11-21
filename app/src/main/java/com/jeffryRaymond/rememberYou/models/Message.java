package com.jeffryRaymond.rememberYou.models;

import android.content.Context;
import android.widget.Toast;

/**
 * This java class is for displaying a message pop up.
 */

public class Message {
    public static void message(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
