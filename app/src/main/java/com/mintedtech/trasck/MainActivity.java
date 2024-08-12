package com.mintedtech.trasck;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mintedtech.trasck.databinding.ActivityMainBinding;

import android.view.View;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private ArrayList<Task> taskList;
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
        adapter = new TaskAdapter(this, (ArrayList<Task>) taskList);
        recyclerView.setAdapter(adapter);

        binding.fab.setOnClickListener(view -> handleFABClick(view));

        addDummyTasks();


    }

    private void addDummyTasks() {
        // Adding dummy tasks for demonstration
        taskList.add(new Task("Task 1", "Description for Task 1"));
        taskList.add(new Task("Task 2", "Description for Task 2"));
        adapter.notifyDataSetChanged();
    }

    private void setContentView() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void handleFABClick(View view) {
        Intent intent = new Intent(
                MainActivity.this, TaskActivity.class);
        startActivity(intent);

    }

}
