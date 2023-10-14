package com.example.findmydoctor;

import java.util.Map;

public class ChatMessage {
    private String sender; // User ID of the message sender
    private String text; // The message text
    private Map<String, String> timestamp; // Timestamp of the message

    // Default constructor (required for Firebase)
    public ChatMessage() {
        // Default constructor is required for Firebase.
    }

    // Constructor to create a new message
    public ChatMessage(String sender, String text, Map<String, String> timestamp) {
        this.sender = sender;
        this.text = text;
        this.timestamp = timestamp;
    }

    // Getters and setters (if needed)
    public String getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTimestamp(Map<String, String> timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, String> getTimestamp() {
        return timestamp;
    }
}
