package com.jeffryRaymond.rememberYou.data.database.activities;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jeffryRaymond.rememberYou.R;
import com.jeffryRaymond.rememberYou.activities.DeletedTaskListView;
import com.jeffryRaymond.rememberYou.data.database.adapters.TaskAdapter;
import com.jeffryRaymond.rememberYou.data.database.db.DataBaseHelper;
import com.jeffryRaymond.rememberYou.data.database.models.TaskItem;
import com.jeffryRaymond.rememberYou.interfaces.Communicator;
import com.jeffryRaymond.rememberYou.models.Message;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * This activity deals with a list of tasks under a category.
 */

public class EachTaskCategoryListView extends AppCompatActivity implements Communicator {
    private ListView mListView;
    private String categoryName;
    private TaskAdapter taskAdapter;
    private List<TaskItem> listOfTasksUnderCategory = new ArrayList<>();
    private List<TaskItem> taskItemList = new ArrayList<>();
    private TextView messageTextView;
    private List<TaskItem> getListOfTasksUnderCategory = new ArrayList<>();
    private boolean sameTask = false;
    private DataBaseHelper mDataBaseHelper;
    private String titleName;
    private String selectedTaskTitleForUpdate, selectedTaskDescriptionForUpdate;
    private View mView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_item_list_view);
        mDataBaseHelper = new DataBaseHelper(EachTaskCategoryListView.this);
        messageTextView = findViewById(R.id.msgTextView);
        Intent intent = getIntent();
        categoryName = intent.getStringExtra("categoryName");
        final int categoryId = mDataBaseHelper.getCategoryNumber(categoryName);
        //registerForContextMenu(mListView);
        Toolbar appToolbar = findViewById(R.id.toolBarApp);
        setSupportActionBar(appToolbar);
        appToolbar.setTitleTextColor(0xFFFFFFFF);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mListView = findViewById(R.id.taskCategorylistView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                TaskItem task = (TaskItem) mListView.getItemAtPosition(position);
                String selectedTaskTitle = task.getTaskTitle();
                String selectedTaskDescription = task.getTaskDescription();
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(2500);
                view.startAnimation(animation1);
                AlertDialog.Builder builder = new AlertDialog.Builder(EachTaskCategoryListView.this);
                View newView = getLayoutInflater().inflate(R.layout.pop_up_display_info, null);
                TextView titleTextView = newView.findViewById(R.id.titleTextView);
                TextView descriptionTextView = newView.findViewById(R.id.descriptionTextView);
                titleTextView.setText(selectedTaskTitle);
                descriptionTextView.setText(selectedTaskDescription);
                builder.setView(newView);
                AlertDialog ad = builder.create();
                ad.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
                ad.show();
            }
        });
        android.app.FragmentManager fm = getFragmentManager();
        FragmentTransaction fmTransaction = fm.beginTransaction();
        fmTransaction.commit();
//---------------------------------------------------------------------------------------------------------------------------------------------------------------
        //Dealing with image view click item for Adding a tasks
        ImageView addImageView = findViewById(R.id.imageAddButton);
        addImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getListOfTasksUnderCategory.clear();
                getListOfTasksUnderCategory = mDataBaseHelper.getSpecificData(categoryId);
                final AlertDialog.Builder builder = new AlertDialog.Builder(EachTaskCategoryListView.this);
                mView = getLayoutInflater().inflate(R.layout.pop_up_add_task, null);
                Button insertButton = mView.findViewById(R.id.insertPopUpButton);
                builder.setView(mView);
                final AlertDialog alert = builder.create();
                final EditText titleEditText = mView.findViewById(R.id.titleInsertPopUp);
                final EditText descriptionEditText = mView.findViewById(R.id.descriptionInsertPopUp);
                alert.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
                alert.show();
                insertButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String editTextDescription = descriptionEditText.getText().toString();
                        String editTextTitle = titleEditText.getText().toString();
                        for (int i = 0; i < getListOfTasksUnderCategory.size(); i++) {
                            TaskItem currentTaskItem = getListOfTasksUnderCategory.get(i);
                            String currentTaskTitle = currentTaskItem.getTaskTitle();
                            if (currentTaskTitle.equals(editTextTitle)) {
                                sameTask = true;
                            } else {
                                sameTask = false;
                            }
                        }
                        if (sameTask == false) {
                            long id = mDataBaseHelper.insertTask(editTextTitle, editTextDescription, titleName);
                            if (id < 0) {
                                Message.message(EachTaskCategoryListView.this, "Unsuccessful");
                            } else {
                                alert.dismiss();
                            }
                            loadTasks();
                        } else {
                            Message.message(EachTaskCategoryListView.this, "Task with same title already exists!");
                            loadTasks();
                        }
                    }
                });
            }
        });
//------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //Dealing with image view for Editing a task
        ImageView editImageView = findViewById(R.id.imageEditButton);
        editImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v7.app.AlertDialog.Builder mBuilder = new android.support.v7.app.AlertDialog.Builder(EachTaskCategoryListView.this);
                View mView = getLayoutInflater().inflate(R.layout.pop_up_update, null);
                final EditText titleEditText = mView.findViewById(R.id.titlePopUp);
                titleEditText.setText(selectedTaskTitleForUpdate);
                final EditText descriptionEditText = mView.findViewById(R.id.descriptionPopUp);
                descriptionEditText.setText(selectedTaskDescriptionForUpdate);
                Button update_button = mView.findViewById(R.id.updatePopUpButton);
                mBuilder.setView(mView);
                final android.support.v7.app.AlertDialog alertDialog = mBuilder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
                if (selectedTaskTitleForUpdate == null && selectedTaskDescriptionForUpdate == null) {
                    Message.message(EachTaskCategoryListView.this, "Please select an item first!");
                } else {
                    alertDialog.show();
                    update_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String newTitle = titleEditText.getText().toString();
                            String newDescription = descriptionEditText.getText().toString();
                            boolean updatedTask = mDataBaseHelper.updateData(selectedTaskTitleForUpdate, newTitle, newDescription);
                            if (updatedTask == true) {
                                String allData = mDataBaseHelper.getAllData();
                            }
                            loadTasks();
                            alertDialog.dismiss();
                        }
                    });
                }
            }
        });
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //Dealing with image view for syncing task to calender
        ImageView calenderImageView = findViewById(R.id.imageCalenderButton);
        calenderImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cal = new Intent(Intent.ACTION_EDIT);
                cal.setType("vnd.android.cursor.item/event");
                if (selectedTaskTitleForUpdate != null && selectedTaskDescriptionForUpdate != null) {
                    cal.putExtra("title", selectedTaskTitleForUpdate);
                    cal.putExtra("description", selectedTaskDescriptionForUpdate);
                    startActivity(cal);
                } else {
                    Message.message(EachTaskCategoryListView.this, "Please select an item first!");
                }
            }
        });
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //Dealing with image view for setting alarm/reminder to task
        ImageView alarmImageView = findViewById(R.id.imageAlarmButton);
        alarmImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
                TimeZone torontoTimeZone = TimeZone.getDefault();
                Calendar calendar = Calendar.getInstance(torontoTimeZone);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                if (selectedTaskDescriptionForUpdate != null) {
                    intent.putExtra(AlarmClock.EXTRA_MESSAGE, selectedTaskDescriptionForUpdate);
                    if (hour > 12) {
                        int newHour = hour - 12;
                        intent.putExtra(AlarmClock.EXTRA_HOUR, newHour);
                    } else {
                        intent.putExtra(AlarmClock.EXTRA_HOUR, hour);
                        intent.putExtra(AlarmClock.EXTRA_MINUTES, minute);
                    }
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                } else {
                    Message.message(EachTaskCategoryListView.this, "Please select an item first!");

                }
            }
        });
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //Dealing with image view for sharing a task
        ImageView shareImageView = findViewById(R.id.imageShareButton);
        shareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                if (selectedTaskTitleForUpdate != null && selectedTaskDescriptionForUpdate != null) {
                    intent.putExtra(Intent.EXTRA_TEXT, selectedTaskDescriptionForUpdate);
                    intent.putExtra(Intent.EXTRA_SUBJECT, selectedTaskTitleForUpdate);
                    intent = Intent.createChooser(intent, "SEND");
                    startActivity(intent);
                } else {
                    Message.message(EachTaskCategoryListView.this, "Please select an item first!");
                }
            }
        });
//------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //Dealing with image view for deleting a task
        ImageView deleteImageView = findViewById(R.id.imageDeleteButton);
        deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v7.app.AlertDialog.Builder mBuilderNew = new android.support.v7.app.AlertDialog.Builder(EachTaskCategoryListView.this);
                View mViewNew = getLayoutInflater().inflate(R.layout.pop_up_delete, null);
                Button yesButton = mViewNew.findViewById(R.id.yesButton);
                Button noButton = mViewNew.findViewById(R.id.noButton);
                //Starting alert dialog
                mBuilderNew.setView(mViewNew);
                final android.support.v7.app.AlertDialog alertDialogNew = mBuilderNew.create();
                alertDialogNew.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
                if (selectedTaskTitleForUpdate == null && selectedTaskDescriptionForUpdate == null) {
                    Message.message(EachTaskCategoryListView.this, "Please select an item first!");
                } else {
                    alertDialogNew.show();
                    noButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialogNew.dismiss();
                        }
                    });
                    yesButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (selectedTaskTitleForUpdate != null && selectedTaskDescriptionForUpdate != null) {
                                long idDeletedTasks = mDataBaseHelper.insertDeletedTask(selectedTaskTitleForUpdate, selectedTaskDescriptionForUpdate, categoryName);
                                if (listOfTasksUnderCategory.size() == 1) {
                                    mListView.setAdapter(null);
                                    TaskItem currentTask = listOfTasksUnderCategory.get(0);
                                    String titleToDelete = currentTask.getTaskTitle();
                                    boolean taskDelete = mDataBaseHelper.deleteData(titleToDelete);
                                    if (taskDelete) {
                                        loadTasks();
                                        alertDialogNew.dismiss();
                                    }
                                } else if (listOfTasksUnderCategory.size() > 1) {
                                    boolean taskDelete = mDataBaseHelper.deleteData(selectedTaskTitleForUpdate);
                                    if (idDeletedTasks < 0) {
                                        Message.message(EachTaskCategoryListView.this, "Error: cannot delete task");
                                    } else if (taskDelete) {
                                        loadTasks();
                                        alertDialogNew.dismiss();
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });
//----------------------------------------------------------------------------------------------------------------------------------------------------------------
        //Dealing with history image view for seeing history of deleted tasks
        ImageView historyImageView = findViewById(R.id.imageHistoryButton);
        historyImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(EachTaskCategoryListView.this, DeletedTaskListView.class);
                newIntent.putExtra("categoryName", categoryName);
                startActivity(newIntent);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View v, final ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        TaskItem taskItem = (TaskItem) mListView.getItemAtPosition(info.position);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasks();
        loadTasksFromRecentlyClicked();
        getSupportActionBar().setTitle(categoryName);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadTasks();
    }

    @Override
    protected void onPause() {
        super.onPause();
        loadTasks();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //Function to load tasks into listView
    private void loadTasks() {
        taskItemList.clear();
        Intent intent = getIntent();
        titleName = intent.getExtras().getString("categoryName");
        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        if (titleName != null) {
            int categoryID = dataBaseHelper.getSpinnerId(titleName);
            //List<TaskItem> taskItemList = dataBaseHelper.getSpecificData(categoryID);
            taskItemList = dataBaseHelper.getSpecificData(categoryID);
            if (taskItemList.size() != 0) {
                mListView.setEmptyView(messageTextView);
                mListView.setAdapter(null);
                taskAdapter = new TaskAdapter(EachTaskCategoryListView.this, taskItemList);
                taskAdapter.notifyDataSetChanged();
                mListView.setAdapter(taskAdapter);
            }
        }
    }

    //Function to load tasks from recently clicked
    private void loadTasksFromRecentlyClicked() {
        Intent intent = getIntent();
        int taskClickedId = intent.getIntExtra("relatedTasksList", 0);
        int clicked = intent.getIntExtra("clicked", 0);
        if (clicked == 1) {
            List<TaskItem> restOfTasksList = mDataBaseHelper.getSpecificData(taskClickedId);
            if (restOfTasksList.size() != 0) {
                mListView.setEmptyView(messageTextView);
                taskAdapter = new TaskAdapter(EachTaskCategoryListView.this, restOfTasksList);
                taskAdapter.notifyDataSetChanged();
                mListView.setAdapter(taskAdapter);
            }
        }
    }

    //Function to get value of selected radio button
    @Override
    public void respond(int position) {
        int categoryId = mDataBaseHelper.getCategoryNumber(categoryName);
        listOfTasksUnderCategory = mDataBaseHelper.getSpecificData(categoryId);
        if (listOfTasksUnderCategory.size() > 0) {
            TaskItem selectedCurrentTask = listOfTasksUnderCategory.get(position);
            selectedTaskTitleForUpdate = selectedCurrentTask.getTaskTitle();
            selectedTaskDescriptionForUpdate = selectedCurrentTask.getTaskDescription();
        }
    }
}

