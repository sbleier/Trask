package com.mintedtech.trasck;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Handler;
import android.os.Looper;

import android.view.MenuItem;
import android.widget.Button;


import com.google.android.material.textfield.TextInputEditText;
import com.mintedtech.trasck.databinding.ActivityTaskBinding;

import java.util.Locale;

public class TaskActivity extends AppCompatActivity {

    private ActivityTaskBinding binding;

    private Handler mHandler;
    private Runnable mRunnable;

    private boolean mTimerPaused;
    private long mSecondsElapsed;
    private long estimatedTime;
    private boolean isEditMode;
    private int editPosition;

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

        Intent incomingData = getIntent();
        editPosition = incomingData.getIntExtra("edit_task_position", -1);
        isEditMode = editPosition >-1;

        if (isEditMode) {
            Task currentTask = incomingData.getParcelableExtra("current_task");
            binding.contentTask.taskTitle.setText(currentTask.getTitle().toString());
            binding.contentTask.taskDescription.setText(currentTask.getDescription().toString());
            //TODO: Adjust number formats for these two items following
            binding.contentTask.estimatedTime.setText(Long.toString(currentTask.getEstimatedTime()));
            binding.contentTask.tvSecondsElapsed.setText(Long.toString(currentTask.getElapsedTime()));

        }

    }

    private void submitTask() {
        // Retrieve input values
        try {
            TextInputEditText taskTitleInput =  binding.contentTask.taskTitle;
            String taskTitle = taskTitleInput.getText().toString();

            TextInputEditText taskDescriptionInput = binding.contentTask.taskDescription;
            String taskDescription = taskDescriptionInput.getText().toString();

            TextInputEditText estimatedTimeInput = binding.contentTask.estimatedTime;
            String estTimeStr = estimatedTimeInput.getText().toString();

            long estTime = parseTimeFromString(estTimeStr);

            String ellapsedTimeString = binding.contentTask.tvSecondsElapsed.getText().toString();

            long elapsedTime = parseTimeFromString(ellapsedTimeString); //TODO

            Task newTask = new Task(taskTitle, taskDescription, estTime, elapsedTime);
            Intent resultIntent = new Intent();
            resultIntent.putExtra("new_task", newTask);
            resultIntent.putExtra("edit_position", editPosition);
            setResult(RESULT_OK, resultIntent);
            finish();// Convert time string to seconds

        } catch (NullPointerException e) {
            e.getMessage();
        }


    }

    private long parseTimeFromString(String timeInput) {
        // Expected format: HH:MM:SS
        String[] timeParts = timeInput.split(":");
        if (timeParts.length == 3) {
            try {
                int hours = Integer.parseInt(timeParts[0]);
                int minutes = Integer.parseInt(timeParts[1]);
                int seconds = Integer.parseInt(timeParts[2]);
                return hours * 3600 + minutes * 60 + seconds;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return 0; // Default to 0 if parsing fails
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
        TextInputEditText estimatedTimeInput = findViewById(R.id.estimatedTime);
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
            finish();
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


