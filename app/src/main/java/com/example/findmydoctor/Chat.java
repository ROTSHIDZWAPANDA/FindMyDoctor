package com.example.findmydoctor;

import android.os.Message;

import java.util.HashMap;
import java.util.Map;

public class Chat {
    private String chatId;
    private String participant1; // User ID of participant 1 (e.g., doctor)
    private String participant2; // User ID of participant 2 (e.g., patient)
    private String appointmentId; // ID of the associated appointment
    private Map<String, Message> messages; // A map to store chat messages

    // Default constructor (required for Firebase)
    public Chat() {
        // Default constructor is required for Firebase.
    }

    // Constructor to create a new chat
    public Chat(String chatId, String participant1, String participant2, String appointmentId) {
        this.chatId = chatId;
        this.participant1 = participant1;
        this.participant2 = participant2;
        this.appointmentId = appointmentId;
        this.messages = new HashMap<>();
    }

    // Getters and setters
    public String getChatId() {
        return chatId;
    }

    public String getParticipant1() {
        return participant1;
    }

    public void setParticipant1(String participant1) {
        this.participant1 = participant1;
    }

    public String getParticipant2() {
        return participant2;
    }

    public void setParticipant2(String participant2) {
        this.participant2 = participant2;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Map<String, Message> getMessages() {
        return messages;
    }

    public void setMessages(Map<String, Message> messages) {
        this.messages = messages;
    }
}
