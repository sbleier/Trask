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
