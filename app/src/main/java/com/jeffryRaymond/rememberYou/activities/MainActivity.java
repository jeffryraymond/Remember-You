package com.jeffryRaymond.rememberYou.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.style.TypefaceSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.jeffryRaymond.rememberYou.R;
import com.jeffryRaymond.rememberYou.adapters.TaskAdapterRecent;
import com.jeffryRaymond.rememberYou.data.database.activities.EachTaskCategoryListView;
import com.jeffryRaymond.rememberYou.data.database.db.DataBaseHelper;
import com.jeffryRaymond.rememberYou.data.database.models.TaskItem;
import com.jeffryRaymond.rememberYou.models.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeffry Raymond on 2018-05-08.
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DataBaseHelper mDataBaseHelper;
    private NavigationView navigationView;
    private ArrayList<String> categoryNames = new ArrayList<>();
    private String userName;
    private String categoryClicked;
    private boolean categoryExists;
    private ArrayList<String> categoriesList = new ArrayList<>();
    private ListView recentTasksListView;
    private List<TaskItem> listOfRecentTasks = new ArrayList<>();
    private boolean updated = false;
    private boolean sameTasks = false;
    private String newTitleCategory;
    private List<TaskItem> tasksUnderCategory = new ArrayList<>();
    private ImageView rememberYouImageView;
    private ArrayList<String> categoryNamesList = new ArrayList<>();
    private EditText updatedCategoryEditText;
    private List<TaskItem> userNameList = new ArrayList<>();
    private List<TaskItem> deletedTasksList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String Welcome = getResources().getString(R.string.welcome);
        categoryExists = true;
        mDataBaseHelper = new DataBaseHelper(MainActivity.this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View contentMain = findViewById(R.id.content_main);
        recentTasksListView = contentMain.findViewById(R.id.taskCategorylistCM);
        recentTasksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                TaskItem task = (TaskItem) recentTasksListView.getItemAtPosition(position);
                long catId = task.getTaskCategoryId();
                int catid = (int) catId;
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(2500);
                view.startAnimation(animation1);
                categoryNamesList.clear();
                categoryNamesList = mDataBaseHelper.getSpinnerData();
                int idCategory = catid - 1;
                String categoryNameClicked = categoryNamesList.get(idCategory);
                Intent intent = new Intent(MainActivity.this, EachTaskCategoryListView.class);
                intent.putExtra("categoryName", categoryNameClicked);
                startActivity(intent);
            }

        });
        rememberYouImageView = contentMain.findViewById(R.id.rememberYouLogoImage);
        rememberYouImageView.setVisibility(View.VISIBLE);
        View headerView = navigationView.getHeaderView(0);
        List<TaskItem> usersInfo = mDataBaseHelper.getUserNameAndPassWord();
        if (usersInfo != null) {
            for (int i = 0; i < usersInfo.size(); i++) {
                TaskItem currentTask = usersInfo.get(i);
                userName = currentTask.getUserName();
                TextView welcomeMsgTextView = headerView.findViewById(R.id.welcomeMessage);
                welcomeMsgTextView.setText(Welcome + userName);
            }
        }
        Button addNewTaskButton = findViewById(R.id.addCategoryButtonContentMain);
        addNewTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> categoriesArrayList = mDataBaseHelper.getSpinnerData();
                if (categoriesArrayList.size() == 0) {
                    Message.message(MainActivity.this, "No categories added yet.  Click the top left or slide finger from left screen to right to add a new category!");
                } else {
                    android.support.v7.app.AlertDialog.Builder mBuilder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                    View mView = getLayoutInflater().inflate(R.layout.a_d_content_main_add_task, null);
                    final EditText taskTitleEditText = mView.findViewById(R.id.editTextTaskTitleAD);
                    final EditText taskDescriptionEditText = mView.findViewById(R.id.editTextTaskDescriptionAD);
                    final Spinner mSpinner = mView.findViewById(R.id.spinnerDataCategories);
                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(MainActivity.this,
                            android.R.layout.simple_spinner_item, categoriesArrayList);
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mSpinner.setAdapter(spinnerAdapter);
                    mBuilder.setView(mView);
                    final android.support.v7.app.AlertDialog alertDialog = mBuilder.create();
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
                    alertDialog.show();
                    Button saveButton = mView.findViewById(R.id.saveButtonADTask);
                    saveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String categorySelected = mSpinner.getSelectedItem().toString();
                            String taskTitle = taskTitleEditText.getText().toString();
                            String taskDescription = taskDescriptionEditText.getText().toString();
                            int catID = mDataBaseHelper.getCategoryNumber(categorySelected);
                            tasksUnderCategory.clear();
                            tasksUnderCategory = mDataBaseHelper.getSpecificData(catID);
                            for (int i = 0; i < tasksUnderCategory.size(); i++) {
                                TaskItem currentTask = tasksUnderCategory.get(i);
                                String currentTaskTaskTitle = currentTask.getTaskTitle();
                                if (currentTaskTaskTitle.equals(taskTitle)) {
                                    sameTasks = true;
                                }
                            }
                            if (!sameTasks) {
                                if (taskTitle.isEmpty() || taskDescription.isEmpty()) {
                                    Message.message(MainActivity.this, "Please fill out all the information!");
                                } else {
                                    long id = mDataBaseHelper.insertTask(taskTitle, taskDescription, categorySelected);
                                    if (id < 0) {
                                        Message.message(MainActivity.this, "Unsuccessful");
                                    } else {
                                        Intent intent1 = new Intent(MainActivity.this, MainActivity.class);
                                        startActivity(intent1);
                                    }
                                    alertDialog.dismiss();
                                }
                            } else {
                                Message.message(MainActivity.this, "Task with same title already exists");
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        categoriesList.clear();
        categoriesList = mDataBaseHelper.getSpinnerData();
        if (!categoriesList.isEmpty()) {
            if (id == R.id.action_delete_category) {
                createMenu();
                android.support.v7.app.AlertDialog.Builder mBuilderNew = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                View mViewNew = getLayoutInflater().inflate(R.layout.pop_up_delete_category, null);
                Button deleteButton = mViewNew.findViewById(R.id.deleteButtonPopUpDelete);
                final Spinner mSpinnerData = mViewNew.findViewById(R.id.spinnerDataPopUpDelete);
                final ArrayAdapter<String> spinnerAdapterNew = new ArrayAdapter<>(MainActivity.this,
                        android.R.layout.simple_spinner_item, categoriesList);
                spinnerAdapterNew.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinnerData.setAdapter(spinnerAdapterNew);
                mBuilderNew.setView(mViewNew);
                final android.support.v7.app.AlertDialog alertDialogNew = mBuilderNew.create();
                alertDialogNew.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
                alertDialogNew.show();
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String selectedCategory = mSpinnerData.getSelectedItem().toString();
                        int categoryId = mDataBaseHelper.getCategoryNumber(selectedCategory);
                        deletedTasksList = mDataBaseHelper.getSpecificData(categoryId);
                        for (int i = 0; i < deletedTasksList.size(); i++) {
                            TaskItem currentTaskItem = deletedTasksList.get(i);
                            String currentTaskTitle = currentTaskItem.getTaskTitle();
                            String currentTaskDescription = currentTaskItem.getTaskDescription();
                            long idDeletedTasks = mDataBaseHelper.insertDeletedTask(currentTaskTitle, currentTaskDescription, selectedCategory);
                            if (idDeletedTasks > 0) {
                            }
                        }
                        boolean taskDeleted = mDataBaseHelper.deleteCategory(selectedCategory);
                        if (taskDeleted) {
                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        alertDialogNew.dismiss();
                    }
                });
                return true;
            } else if (id == R.id.action_update_category) {
                createMenu();
                android.support.v7.app.AlertDialog.Builder builderNew = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                View viewNew = getLayoutInflater().inflate(R.layout.a_d_update_category, null);
                final Spinner spinner_data = viewNew.findViewById(R.id.categorySpinnerData);
                final ArrayAdapter<String> spinnerAdapterNeww = new ArrayAdapter<>(MainActivity.this,
                        android.R.layout.simple_spinner_item, categoriesList);
                spinnerAdapterNeww.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_data.setAdapter(spinnerAdapterNeww);
                updatedCategoryEditText = viewNew.findViewById(R.id.alertUpdateCategoryEditText);
                Button updateCategoryButton = viewNew.findViewById(R.id.updateButtonCategory);
                builderNew.setView(viewNew);
                final android.support.v7.app.AlertDialog alertDialogNeww = builderNew.create();
                alertDialogNeww.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
                alertDialogNeww.show();
                updateCategoryButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String selectedCategoryTitle = spinner_data.getSelectedItem().toString();
                        String updatedCategoryName = updatedCategoryEditText.getText().toString();
                        updated = mDataBaseHelper.updateCategoryName(selectedCategoryTitle, updatedCategoryName);
                        if (updated) {
                            Message.message(MainActivity.this, "Succesfully updated category name!");
                            createMenu();
                            alertDialogNeww.dismiss();
                        }
                    }
                });
                return true;
            }
        } else {
            createMenu();
            Message.message(MainActivity.this, "Please create a category first!");
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.new_cat:
                android.support.v7.app.AlertDialog.Builder mBuilder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.add_cat_alert_dialog, null);
                categoryNames = mDataBaseHelper.getSpinnerData();
                final EditText categoryNameAD = mView.findViewById(R.id.categoryNameEditTextAlertDialog);
                Button saveButtonAD = mView.findViewById(R.id.saveButtonAlertDialog);
                mBuilder.setView(mView);
                final android.support.v7.app.AlertDialog alertDialog = mBuilder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
                alertDialog.show();
                saveButtonAD.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        newTitleCategory = categoryNameAD.getText().toString();
                        for (int i = 0; i < categoryNames.size(); i++) {
                            String categoryTitle = categoryNames.get(i);
                            if (newTitleCategory.equals(categoryTitle)) {
                                categoryExists = false;
                            } else {
                                categoryExists = true;
                            }
                        }
                        if (newTitleCategory != null && !newTitleCategory.isEmpty()) {
                            if (categoryExists == true) {
                                long id = mDataBaseHelper.insertTaskCategory(newTitleCategory);
                                if (id < 0) {
                                    //Do nothing because it will display Message later
                                } else {
                                    Message.message(MainActivity.this, "Succesfully added new category: " + newTitleCategory);
                                }
                            } else {
                                Message.message(MainActivity.this, "Unsuccesfull, category already exists!");
                            }
                            alertDialog.dismiss();
                        } else {
                            Message.message(MainActivity.this, "Please fill out information!");
                        }
                        createMenu();
                    }
                });
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        createMenu();
        loadRecentTasks();
        displayUserNameMessage();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    //Function to load userName in message
    private void displayUserNameMessage() {
        userNameList.clear();
        userNameList = mDataBaseHelper.getUserNameInfo();

    }

    //Function to load most recent task in main screen
    private void loadRecentTasks() {
        TextView mainWelcomeMsgTextView = findViewById(R.id.welcomeUserContentMain);
        listOfRecentTasks.clear();
        ArrayList<String> listOfCategories = mDataBaseHelper.getSpinnerData();
        if (listOfCategories.size() > 0) {
            for (int i = 0; i < listOfCategories.size(); i++) {
                String categoryName = listOfCategories.get(i);
                int categoryID = mDataBaseHelper.getCategoryNumber(categoryName);
                List<TaskItem> tasksUnderCategory = mDataBaseHelper.getSpecificData(categoryID);
                if (tasksUnderCategory.size() > 0) {
                    for (int j = 0; j < tasksUnderCategory.size(); j++) {
                        int mostRecentTask = tasksUnderCategory.size() - 1;
                        if (j == mostRecentTask) {
                            TaskItem recentTask = tasksUnderCategory.get(j);
                            listOfRecentTasks.add(recentTask);
                        }
                    }
                }
            }
        }
        if (listOfRecentTasks.size() > 0) {
            rememberYouImageView.setVisibility(View.INVISIBLE);
            recentTasksListView.setVisibility(View.VISIBLE);
            mainWelcomeMsgTextView.setText("Welcome " + userName + ", here are some of your tasks:");
            mainWelcomeMsgTextView.setTextSize(18);
            TaskAdapterRecent taskAdapterRecent = new TaskAdapterRecent(MainActivity.this, listOfRecentTasks);
            recentTasksListView.setAdapter(taskAdapterRecent);

        } else {
            rememberYouImageView.setVisibility(View.VISIBLE);
            mainWelcomeMsgTextView = findViewById(R.id.welcomeUserContentMain);
            mainWelcomeMsgTextView.setText("Welcome " + userName + ", you have added no tasks!");
            mainWelcomeMsgTextView.setTextSize(18);

        }
    }

    //Function to create navigation drawer menu
    public void createMenu() {
        ArrayList<String> categoryNamesList = mDataBaseHelper.getSpinnerData();
        if (categoryNamesList.size() > 0) {
            Menu menu = navigationView.getMenu();
            menu.removeGroup(R.id.group2);
            for (int i = 0; i < categoryNamesList.size(); i++) {
                String categoryName = categoryNamesList.get(i);
                int idCounter = mDataBaseHelper.getSpinnerId(categoryName);
                TypefaceSpan span = new TypefaceSpan("cabin_medium");
                SpannableStringBuilder title = new SpannableStringBuilder(categoryName);
                title.setSpan(span, 0, title.length(), 0);
                menu.add(R.id.group2, 0, idCounter, categoryName).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        categoryClicked = menuItem.getTitle().toString();
                        Intent intent = new Intent(MainActivity.this, EachTaskCategoryListView.class);
                        intent.putExtra("categoryName", categoryClicked);
                        startActivity(intent);
                        return true;
                    }
                });
            }
        }
    }
}
