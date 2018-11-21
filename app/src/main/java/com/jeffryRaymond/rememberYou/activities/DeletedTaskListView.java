package com.jeffryRaymond.rememberYou.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.jeffryRaymond.rememberYou.adapters.TaskAdapterDeletedTasks;
import com.jeffryRaymond.rememberYou.data.database.db.DataBaseHelper;
import com.jeffryRaymond.rememberYou.data.database.models.TaskItem;
import com.jeffryRaymond.rememberYou.R;

import java.util.List;

/**
 * This activity deals with displaying the deleted tasks under a category.
 */

public class DeletedTaskListView extends AppCompatActivity {
    private ListView mListView;
    private DataBaseHelper mDataBaseHelper;
    private TextView noTaskAddedTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleted_task_list_view);
        noTaskAddedTextView = findViewById(R.id.noTasksAdded);
        mDataBaseHelper = new DataBaseHelper(DeletedTaskListView.this);
        Toolbar appToolbar = findViewById(R.id.toolBarAppDeletedTasks);
        setSupportActionBar(appToolbar);
        appToolbar.setTitleTextColor(0xFFFFFFFF);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Completed Tasks!");
        mListView = findViewById(R.id.deletedTaskListView);
        registerForContextMenu(mListView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDeletedTasks();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadDeletedTasks();
    }

    @Override
    protected void onPause() {
        super.onPause();
        loadDeletedTasks();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //Function to load the deleted tasks
    private void loadDeletedTasks() {
        Intent intent = getIntent();
        String categoryName = intent.getStringExtra("categoryName");
        int specificID = mDataBaseHelper.getCategoryNumber(categoryName);
        List<TaskItem> deletedTasksList = mDataBaseHelper.getDeletedTasks(specificID);
        if (deletedTasksList.isEmpty()) {
            noTaskAddedTextView.setVisibility(View.VISIBLE);
        } else if (deletedTasksList.size() != 0) {
            noTaskAddedTextView.setVisibility(View.GONE);
            TaskAdapterDeletedTasks mTaskAdapterDeletedTasks = new TaskAdapterDeletedTasks(DeletedTaskListView.this, deletedTasksList);
            mTaskAdapterDeletedTasks.notifyDataSetChanged();
            mListView.setAdapter(mTaskAdapterDeletedTasks);
        }
    }
}
