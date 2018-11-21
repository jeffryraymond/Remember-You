package com.jeffryRaymond.rememberYou.data.database.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;


import com.jeffryRaymond.rememberYou.R;
import com.jeffryRaymond.rememberYou.data.database.models.TaskItem;
import com.jeffryRaymond.rememberYou.interfaces.Communicator;

import java.util.List;

/**
 * This adapter class is for the tasks in a listView.
 */

public class TaskAdapter extends BaseAdapter {
    private final List<TaskItem> mTaskItems;
    private final Context mContext;
    private Communicator mCommunicator;
    private RadioButton selectedRB;
    private int selectedPosition = -1;

    public TaskAdapter(Context context, List<TaskItem> taskItems) {
        this.mContext = context;
        this.mTaskItems = taskItems;
        this.mCommunicator = (Communicator) this.mContext;
    }

    @Override
    public int getCount() {
        return mTaskItems.size();
    }

    @Override
    public Object getItem(int i) {
        return mTaskItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mTaskItems.get(i).getTaskID();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final TaskItem taskItem = mTaskItems.get(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.list_item, null, false);
        TextView title = view.findViewById(R.id.taskTitleName);
        title.setText(taskItem.getTaskTitle());
        TextView description = view.findViewById(R.id.taskDescription);
        description.setText(taskItem.getTaskDescription());
        RadioButton mRadioButton = view.findViewById(R.id.taskSelectedRadioButton);
        mRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position != selectedPosition && selectedRB != null) {
                    selectedRB.setChecked(false);
                }
                selectedPosition = position;
                selectedRB = (RadioButton) view;
                mCommunicator.respond(selectedPosition);
            }
        });
        if (selectedPosition != position) {
            mRadioButton.setChecked(false);
        } else {
            mRadioButton.setChecked(true);
            if (selectedRB != null && mRadioButton != selectedRB) {
                selectedRB = mRadioButton;
            }
        }
        return view;
    }
}
