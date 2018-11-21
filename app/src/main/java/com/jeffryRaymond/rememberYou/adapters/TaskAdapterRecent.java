package com.jeffryRaymond.rememberYou.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.jeffryRaymond.rememberYou.data.database.models.TaskItem;
import com.jeffryRaymond.rememberYou.R;

import java.util.List;

/**
 * The adapter deals with the recent task listView.
 */

public class TaskAdapterRecent extends BaseAdapter {
    private final List<TaskItem> taskItemList;
    private final Context mContext;

    public TaskAdapterRecent(Context context, List<TaskItem> taskItems) {
        this.mContext = context;
        this.taskItemList = taskItems;
    }

    @Override
    public int getCount() {
        return taskItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return taskItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return taskItemList.get(i).getTaskID();
    }

    @Override
    public void notifyDataSetChanged(){
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final TaskItem currentTaskItem = taskItemList.get(i);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.list_item_recent, null, false);
        TextView title = view.findViewById(R.id.taskRecentName);
        title.setText(currentTaskItem.getTaskTitle());
        TextView description = view.findViewById(R.id.taskRecentDescription);
        description.setText(currentTaskItem.getTaskDescription());
        return view;
    }
}
