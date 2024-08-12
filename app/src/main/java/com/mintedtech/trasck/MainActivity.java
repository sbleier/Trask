package com.mintedtech.trasck;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.mintedtech.trasck.databinding.ActivityMainBinding;

import android.view.View;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        setSupportActionBar(binding.toolbar);
        binding.fab.setOnClickListener(view -> handleFABClick(view));


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
