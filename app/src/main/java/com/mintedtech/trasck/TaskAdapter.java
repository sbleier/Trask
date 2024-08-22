package com.mintedtech.trasck;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Locale;

public class TaskAdapter extends ListAdapter<Task, TaskAdapter.TaskViewHolder> {

    private Context context;
    private OnItemClickListener listener;

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
        Log.d("TaskAdapter", "Title: " + task.getTitle());  // Log to check data

        holder.taskTitle.setText(task.getTitle());
        holder.taskDescription.setText(task.getDescription());

        holder.estimatedTime.setText(formatDuration(task.getEstimatedTime()));

        long elapsedTime = task.getElapsedTime();
        if (elapsedTime == 0) {
            holder.elapsedTime.setText("00:00:00");
        } else {
            holder.elapsedTime.setText(formatDuration(elapsedTime));
        }

        long remainingTime = task.getEstimatedTime() - task.getElapsedTime();
        if (remainingTime <= 0) {
            holder.remainingTime.setText("Congratulation! You've reached your goal!");
        } else {
            holder.remainingTime.setText(formatDuration(remainingTime));
        }


        holder.itemView.setOnClickListener(v -> handleClick(task, position));
    }

    private void handleClick(Task task, int position) {
        if (listener !=null) {
            listener.onItemClick(task, position);
        }
    }


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

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskTitle, taskDescription, estimatedTime, elapsedTime, remainingTime;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.taskTitle);
            taskDescription = itemView.findViewById(R.id.taskDescription);
            estimatedTime = itemView.findViewById(R.id.estimatedTime);
            elapsedTime = itemView.findViewById(R.id.tv_seconds_elapsed);
            remainingTime = itemView.findViewById(R.id.tv_remaining_time);
        }
    }
}