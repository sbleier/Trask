package com.mintedtech.trasck;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class Task implements Parcelable {
    private String title;
    private String description;
    private long estimatedTime;
    private long elapsedTime;
    private long remainingTime;

    public Task(String title, String description, long estimatedTime, long elapsedTime) {
        this.title = title;
        this.description = description;
        this.estimatedTime = estimatedTime;
        this.elapsedTime = elapsedTime;
        remainingTime = estimatedTime - elapsedTime;



    }

    protected Task(Parcel in) {
        title = in.readString();
        description = in.readString();
        estimatedTime = in.readLong();
        elapsedTime = in.readLong();
        remainingTime = in.readLong();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeLong(estimatedTime);
        parcel.writeLong(elapsedTime);
        parcel.writeLong(remainingTime);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEstimatedTime(long estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public void setRemainingTime(long remainingTime) {
        this.remainingTime = remainingTime;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public long getEstimatedTime() {
        return estimatedTime;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public long getRemainingTime() {
        return remainingTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return estimatedTime == task.estimatedTime && elapsedTime == task.elapsedTime && Objects.equals(title, task.title) && Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, estimatedTime, elapsedTime);
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", allottedTime=" + estimatedTime +
                ", elapsedTime=" + elapsedTime +
                '}';
    }
}