package com.jeffryRaymond.rememberYou.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jeffryRaymond.rememberYou.R;
import com.jeffryRaymond.rememberYou.activities.MainActivity;
import com.jeffryRaymond.rememberYou.data.database.db.DataBaseHelper;
import com.jeffryRaymond.rememberYou.interfaces.Communicator;
import com.jeffryRaymond.rememberYou.models.Message;

/**
 * This fragment deals with adding a new .
 */

public class addCategoryToListFragment extends android.support.v4.app.Fragment implements Communicator {
    private EditText categoryEditText;
    private String newTitleCategory;
    private DataBaseHelper mDataBaseHelper;
    private int menuCounter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_add_category_to_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView titleTextView = getView().findViewById(R.id.enterCategoryTitleMsg);
        titleTextView.setVisibility(View.GONE);
        categoryEditText = getView().findViewById(R.id.newCategoryName);
        mDataBaseHelper = new DataBaseHelper(getActivity());
        Button saveButton = getView().findViewById(R.id.saveNewCategoryButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newTitleCategory = categoryEditText.getText().toString();
                if (!newTitleCategory.isEmpty()) {
                    long id = mDataBaseHelper.insertTaskCategory(newTitleCategory);
                    if (id < 0) {
                        Message.message(getActivity(), "Unsuccesfull");
                    } else {
                        menuCounter++;
                        Log.i("SAVE", "counter value: " + menuCounter);
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.putExtra("KEY", newTitleCategory);
                        intent.putExtra("COUNTER", menuCounter);
                        startActivity(intent);
                    }
                } else {
                    Message.message(getActivity(), "Please fill out information!");
                }
            }
        });
    }

    @Override
    public void respond(int position) {
    }
}
