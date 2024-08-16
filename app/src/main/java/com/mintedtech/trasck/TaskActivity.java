package com.mintedtech.trasck;

import static androidx.core.util.TimeUtils.formatDuration;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Handler;
import android.os.Looper;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mintedtech.trasck.databinding.ActivityTaskBinding;

import java.util.Locale;

public class TaskActivity extends AppCompatActivity {

    private ActivityTaskBinding binding;

    private Handler mHandler;
    private Runnable mRunnable;

    private boolean mTimerPaused;
    private long mSecondsElapsed;
    private long estimatedTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        setupBackArrow();

        binding.fab.setOnClickListener(view -> {
            getTimeFromInput();
            pauseResumeTimer(); // Timer action here
        });
        setupTimer();
        pauseAndResetTimerOnInitialRun(savedInstanceState);

        getTimeFromInput();
        updateTextView();

        Button submitTaskButton = findViewById(R.id.submitTaskButton);
        submitTaskButton.setOnClickListener(view -> submitTask());
    }

    private void submitTask() {
        // Retrieve input values
        String taskTitle = ((TextInputEditText) findViewById(R.id.taskInput)).getText().toString();
        String taskDescription = ((TextInputEditText) findViewById(R.id.taskDescription)).getText().toString();
        String estimatedTime = ((TextInputEditText) findViewById(R.id.estimated_time_input)).getText().toString();

        Intent resultIntent = new Intent();
        resultIntent.putExtra("taskTitle", taskTitle);
        resultIntent.putExtra("taskDescription", taskDescription);
        resultIntent.putExtra("estimatedTime", estimatedTime);

        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void setupBackArrow() {
        FloatingActionButton backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(view -> onBackPressed());
    }

    private void setupTimer() {
        // Create the Handler object
        mHandler = new Handler(Looper.getMainLooper());

        // Create the Runnable that, after being called,
        // calls the on timer tick method and then itself one second later, and on and on...
        mRunnable = new Runnable() {
            @Override
            public void run() {
                onTimerTick();
                mHandler.postDelayed(this, 1000);
            }
        };
    }

    private void onTimerTick() {
        mSecondsElapsed++;
        updateTextView();
    }

    private void getTimeFromInput() {
        TextInputEditText estimatedTimeInput = findViewById(R.id.estimated_time_input);
        String timeInput = estimatedTimeInput.getText().toString();

        // Expected format: HH:MM:SS
        String[] timeParts = timeInput.split(":");
        if (timeParts.length == 3) {
            int hours = Integer.parseInt(timeParts[0]);
            int minutes = Integer.parseInt(timeParts[1]);
            int seconds = Integer.parseInt(timeParts[2]);

            // Convert the provided time to total seconds
            estimatedTime = hours * 3600 + minutes * 60 + seconds;
        }

    }

    private void updateTextView() {
        binding.contentTask.tvSecondsElapsed.setText(formatDuration(mSecondsElapsed));

        long remainingTime = estimatedTime - mSecondsElapsed;


        long hours = remainingTime / 3600;
        long minutes = (remainingTime % 3600) / 60;
        long seconds = remainingTime % 60;

        if (remainingTime >= 0) {
            String formattedTime = String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds);
            binding.contentTask.tvRemainingTime.setText("Time remaining until your goal: " + formattedTime);
        } else {
            binding.contentTask.tvRemainingTime.setText("You've reached your goal!");
        }
    }

    private String formatDuration(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;
        return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, secs);
    }


    private void pauseResumeTimer() {
        if (mTimerPaused) {
            resumeTimer();
        } else {
            pauseTimer();
        }
    }

    private void resumeTimer() {
        mTimerPaused = false;
        mHandler.postDelayed(mRunnable, 1000);

        binding.fab.setImageDrawable(
                ContextCompat.getDrawable(this, android.R.drawable.ic_media_pause));

    }

    private void pauseTimer() {
        binding.fab.setImageDrawable(
                ContextCompat.getDrawable(this, android.R.drawable.ic_media_play));

        mHandler.removeCallbacks(mRunnable);
        mTimerPaused = true;

        long remainingTime = estimatedTime - mSecondsElapsed;

        // Convert remaining time from seconds to hours, minutes, seconds
        long hours = remainingTime / 3600;
        long minutes = (remainingTime % 3600) / 60;
        long seconds = remainingTime % 60;


    }

    @Override
    protected void onResume() {
        super.onResume();

        // If the timer was running then resume it
        if (!mTimerPaused)
            resumeTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // If the timer is running then temporarily pause the timer.
        // But then reset the flag to false so that, when the app resumes,
        // it will also resume the timer automatically
        if (!mTimerPaused) {
            pauseTimer();
            mTimerPaused = false;
        }
    }


    private void pauseAndResetTimerOnInitialRun(Bundle savedInstanceState) {
        // If this is being called on initial run as opposed to due to a device rotation
        if (savedInstanceState == null) {
            mTimerPaused = true;
            resetTimer();
        }
    }

    private void resetTimer() {
        mSecondsElapsed = 0;
        updateTextView();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get saved elapsed seconds and put this into the EditText
        mSecondsElapsed = savedInstanceState.getLong("SECONDS_ELAPSED");
        updateTextView();

        // get saved timer status
        mTimerPaused = savedInstanceState.getBoolean("TIMER_PAUSED");
        estimatedTime = savedInstanceState.getLong("ESTIMATED_TIME");

        // onResume(), called later in the Activity Lifecycle,
        // will resume the timer if it was not paused
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("SECONDS_ELAPSED", mSecondsElapsed);
        outState.putBoolean("TIMER_PAUSED", mTimerPaused);
        outState.putLong("ESTIMATED_TIME", estimatedTime);
    }

}


