package com.mintedtech.trasck;



import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class TaskAdapter extends ListAdapter<Task, TaskAdapter.TaskViewHolder> {

    private Context context;


    public TaskAdapter(Context context) {
        super(new TaskDiffCallback());
        this.context = context;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = getItem(position); // Use getItem() instead of taskList.get()holder.taskTitle.setText(task.getTitle());
        holder.taskTitle.setText(task.getTitle());
        holder.taskDescription.setText(task.getDescription());
        // Assuming your Task class has a getEstimatedTime() method
        holder.estimatedTime.setText(formatDuration(task.getTime()));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, TaskActivity.class);
            context.startActivity(intent);
        });
    }

        // Add this method to your TaskAdapter
        private String formatDuration(long seconds) {
            long hours = seconds / 3600;
            long minutes = (seconds % 3600) / 60;
            long secs = seconds % 60;
            return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, secs);
        }


    // Define a DiffUtil.ItemCallback for Task objects
    private static class TaskDiffCallback extends DiffUtil.ItemCallback<Task> {
        @Override
        public boolean areItemsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.equals(newItem);
        }
    }

    // ... TaskViewHolder remains the same ...
    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskTitle, taskDescription, estimatedTime;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.taskTitle);
            taskDescription = itemView.findViewById(R.id.taskDescription);
            estimatedTime = itemView.findViewById(R.id.estimatedTime);
        }
    }
}