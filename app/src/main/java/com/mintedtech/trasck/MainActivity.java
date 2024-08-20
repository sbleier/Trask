package com.mintedtech.trasck;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mintedtech.trasck.databinding.ActivityMainBinding;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private ArrayList<Task> taskList;
    private ActivityMainBinding binding;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("LIST", taskList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        setSupportActionBar(binding.toolbar);
        // Initialize the task list
        taskList = savedInstanceState == null ? new ArrayList<>()
                : savedInstanceState.getParcelableArrayList("LIST");

        //Toast.makeText(this, "List Size: " + taskList.size(),Toast.LENGTH_LONG).show();


        // Set up the RecyclerView
        recyclerView = binding.taskRecyclerView; // findViewById(R.id.taskRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TaskAdapter(this);
        recyclerView.setAdapter(adapter);

        // Set item click listener
        adapter.setOnItemClickListener((task, position) -> handleEditClick(task, position));
//        binding.fab.setOnClickListener(view -> handleFABClick(view));

        binding.fab.setOnClickListener(view -> handleFabClick());

//        addDummyTasks();


    }

    private void handleFabClick() {
        Intent intent = new Intent(this, TaskActivity.class);
        startActivityForResult(intent, 1);
    }

    private void handleEditClick(Task task, int position) {
        Intent intent = new Intent(MainActivity.this, TaskActivity.class);
        intent.putExtra("edit_task_position", position);
        intent.putExtra("current_task", task);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            // Retrieve the task data from the intent
            Task newTask = data.getParcelableExtra("new_task");
            if (newTask != null) {

                if (requestCode == 1) { //add
                    taskList.add(newTask);

                    adapter.submitList(taskList); // Notify adapter of new data
                    adapter.notifyItemInserted(taskList.size() - 1);
                } else if (requestCode == 2) { //edit
                    int position = data.getIntExtra("edit_position", -1);
                    if (position !=-1) {
                        taskList.remove(position);
                        taskList.add(position, newTask);
                        adapter.submitList(taskList); // Notify adapter of new data
                        adapter.notifyItemChanged(position);
                    }
                }
            }

        }
    }

    private long convertTimeToSeconds(String timeInput) {
        // Expected format: HH:MM:SS
        if (timeInput == null || timeInput.isEmpty()) {
            return 0; // Or handle empty input differently
        }

        String[] timeParts = timeInput.split(":");
        if (timeParts.length == 3) {
            try {
                int hours = Integer.parseInt(timeParts[0]);
                int minutes = Integer.parseInt(timeParts[1]);
                int seconds = Integer.parseInt(timeParts[2]);
                return hours * 3600 + minutes * 60 + seconds;
            } catch (NumberFormatException e) {
                // Handle invalid number format
                return 0; // Or handle differently
            }
        } else {
            // Handle invalid time format
            return 0; // Or handle differently
        }
    }

//    private void addDummyTasks() {
//        // Adding dummy tasks for demonstration
//        taskList.add(new Task("Task 1", "Description for Task 1", 3));
//        taskList.add(new Task("Task 2", "Description for Task 2", 3));
//        adapter.notifyDataSetChanged();
//    }

    private void setContentView() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

//    private void handleFABClick(View view) {
//        Intent intent = new Intent(
//                MainActivity.this, TaskActivity.class);
//        startActivity(intent);
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
