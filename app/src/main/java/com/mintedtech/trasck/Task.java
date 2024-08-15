package com.mintedtech.trasck;

public class Task {
    private String title;
    private String description;
    private long time;

    public Task(String title, String description, long time) {
        this.title = title;
        this.description = description;
        this.time=time;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public long getTime() {
        return time;
    }
}

