package com.example.voiceaiiot;

// FIX: Removed the first empty "public class ChatMessage { }" block.
// There should only be one class definition per file (for public classes).

public class ChatMessage {
    private String message;
    private boolean isUser;

    public ChatMessage(String message, boolean isUser) {
        this.message = message;
        this.isUser = isUser;
    }

    public String getMessage() {
        return message;
    }

    public boolean isUser() {
        return isUser;
    }
}
