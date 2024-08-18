package com.mintedtech.trasck;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mintedtech.trasck.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private List<Task> taskList;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        setSupportActionBar(binding.toolbar);
        // Initialize the task list
        taskList = new ArrayList<>();

        // Set up the RecyclerView
        recyclerView = findViewById(R.id.taskRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TaskAdapter(this, taskList);
        recyclerView.setAdapter(adapter);

//        binding.fab.setOnClickListener(view -> handleFABClick(view));

        binding.fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, TaskActivity.class);
            startActivityForResult(intent, 1);
        });


//        addDummyTasks();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Retrieve the task data from the intent
            String taskTitle = data.getStringExtra("taskTitle");
            String taskDescription = data.getStringExtra("taskDescription");
            long estimatedTime = convertTimeToSeconds(data.getStringExtra("estimatedTime"));

            // Create a new Task object and add it to the list
            Task newTask = new Task(taskTitle, taskDescription, estimatedTime);
            taskList.add(newTask);

            // Notify the adapter that the data has changed
            adapter.notifyDataSetChanged();
        }
    }

    private long convertTimeToSeconds(String timeInput) {
        // Expected format: HH:MM:SS
        String[] timeParts = timeInput.split(":");
        int hours = Integer.parseInt(timeParts[0]);
        int minutes = Integer.parseInt(timeParts[1]);
        int seconds = Integer.parseInt(timeParts[2]);

        // Convert the provided time to total seconds
        return hours * 3600 + minutes * 60 + seconds;
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
