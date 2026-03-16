package com.example.voiceaiiot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ChatFragment extends Fragment {

    private RecyclerView recyclerView;
    private EditText etMessage;
    private ImageButton btnSend, btnVoice;
    private ChatAdapter adapter;
    private List<ChatMessage> messageList;

    private TextToSpeech tts;
    private StringBuilder conversationMemory = new StringBuilder();
    private static final int SPEECH_REQUEST_CODE = 100;

    public ChatFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView = view.findViewById(R.id.recyclerChat);
        etMessage = view.findViewById(R.id.etMessage);
        btnSend = view.findViewById(R.id.btnSend);
        btnVoice = view.findViewById(R.id.btnVoice);

        tts = new TextToSpeech(getContext(), status -> {
            if (status != TextToSpeech.ERROR) {
                tts.setLanguage(Locale.US);
            }
        });

        loadChatHistory();
        adapter = new ChatAdapter(messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        btnSend.setOnClickListener(v -> sendMessage());
        btnVoice.setOnClickListener(v -> startVoiceInput());

        return view;
    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...");
        try {
            startActivityForResult(intent, SPEECH_REQUEST_CODE);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Speech not supported", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                etMessage.setText(result.get(0));
                sendMessage();
            }
        }
    }

    private void sendMessage() {
        String messageText = etMessage.getText().toString().trim();
        if (!messageText.isEmpty()) {
            addUserMessage(messageText);
            etMessage.setText("");
            conversationMemory.append("User: ").append(messageText).append("\n");
            simulateAIResponse(messageText);
        }
    }

    private void simulateAIResponse(String userText) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            String aiResponse = "You said: " + userText + ". I am remembering this conversation.";
            addAiMessage(aiResponse);
            if (tts != null) {
                tts.speak(aiResponse, TextToSpeech.QUEUE_FLUSH, null, null);
            }
            conversationMemory.append("AI: ").append(aiResponse).append("\n");
        }, 1000);
    }

    private void addUserMessage(String message) {
        messageList.add(new ChatMessage(message, true));
        updateChat();
    }

    private void addAiMessage(String message) {
        messageList.add(new ChatMessage(message, false));
        updateChat();
    }

    private void updateChat() {
        adapter.notifyItemInserted(messageList.size() - 1);
        recyclerView.scrollToPosition(messageList.size() - 1);
        saveChatHistory();
    }

    private void saveChatHistory() {
        if (getContext() == null) return;
        SharedPreferences prefs = getContext().getSharedPreferences("ChatPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(messageList);
        editor.putString("history", json);
        editor.apply();
    }

    private void loadChatHistory() {
        if (getContext() == null) {
            messageList = new ArrayList<>();
            return;
        }
        SharedPreferences prefs = getContext().getSharedPreferences("ChatPrefs", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("history", null);

        if (json == null) {
            messageList = new ArrayList<>();
        } else {
            // FIXED: Using correct TypeToken from com.google.gson.reflect
            Type type = new com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken<ArrayList<ChatMessage>>() {}.getType();
            messageList = gson.fromJson(json, type);
        }

        if (messageList == null) {
            messageList = new ArrayList<>();
        }
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}