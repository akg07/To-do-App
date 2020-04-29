package com.example.mynotes;

import com.google.firebase.Timestamp;

public class Note {

    private String text;
    private boolean completed;
    private Timestamp created;
    private String UserID;

    public Note() {
    }

    public Note(String text, boolean completed, Timestamp created, String userID) {
        this.text = text;
        this.completed = completed;
        this.created = created;
        UserID = userID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean getCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    @Override
    public String toString() {
        return "Note{" +
                "text='" + text + '\'' +
                ", completed=" + completed +
                ", created=" + created +
                ", UserID='" + UserID + '\'' +
                '}';
    }
}
