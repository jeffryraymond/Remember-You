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
 * This adapter is for the deleted tasks listView.
 */

public class TaskAdapterDeletedTasks extends BaseAdapter {
    private final List<TaskItem> taskItemList;
    private final Context mContext;

    public TaskAdapterDeletedTasks(Context context, List<TaskItem> taskItems) {
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
        view = inflater.inflate(R.layout.single_list_item_delete, null, false);
        TextView title = view.findViewById(R.id.deletedTaskTitle);
        title.setText(currentTaskItem.getTaskTitle());
        TextView description = view.findViewById(R.id.deletedTaskDescription);
        description.setText(currentTaskItem.getTaskDescription());
        return view;
    }
}
