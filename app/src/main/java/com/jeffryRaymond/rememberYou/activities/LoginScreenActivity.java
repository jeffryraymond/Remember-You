package com.jeffryRaymond.rememberYou.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jeffryRaymond.rememberYou.data.database.db.DataBaseHelper;
import com.jeffryRaymond.rememberYou.models.Message;
import com.jeffryRaymond.rememberYou.R;

/**
 * This activity deals with the login screen.
 */

public class LoginScreenActivity extends AppCompatActivity {
    private EditText userName, pinCode;
    private String mUserName, mUserPin;
    private DataBaseHelper mDataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        userName = findViewById(R.id.userNameTextView);
        pinCode = findViewById(R.id.pinCode);
        mDataBaseHelper = new DataBaseHelper(LoginScreenActivity.this);
        Button continueButton = findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUserName = userName.getText().toString();
                mUserPin = pinCode.getText().toString();
                if (!mUserName.isEmpty() && !mUserPin.isEmpty()) {
                    long id = mDataBaseHelper.insertUserNameAndPin(mUserName, mUserPin, 0);
                    if (id > 0) {
                        Intent intent = new Intent(LoginScreenActivity.this, MainActivity.class);
                        intent.putExtra("userName", mUserName);
                        startActivity(intent);
                    }
                } else {
                    Message.message(LoginScreenActivity.this, "Please fill out all the information!");
                }
            }
        });
    }
}
