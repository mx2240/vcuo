package com.example.voiceaiiot;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

public class VoiceFragment extends Fragment {

    private TextView txtResult, txtStatus;
    private View btnSpeak; // This should be your Mic button
    private TextToSpeech tts;
    private SharedPreferences prefs;
    private ActivityResultLauncher<Intent> speechLauncher;
    private ActivityResultLauncher<String> permissionLauncher;

    private boolean waitingForCommand = false;
    private final String ESP32_IP = "http://192.168.1.100/";

    public VoiceFragment() {}

    // FIX 1: You must override onCreateView to show the UI
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_voice, container, false);

        // FIX 2: Initialize your views here
        txtResult = view.findViewById(R.id.txtResult);
        txtStatus = view.findViewById(R.id.txtStatus);
        btnSpeak = view.findViewById(R.id.btnSpeak); // Ensure this ID exists in fragment_voice.xml

        btnSpeak.setOnClickListener(v -> checkPermissionAndListen());

        return view;
    }

    // FIX 3: Check permission before opening the mic
    private void checkPermissionAndListen() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {
            startListening();
        } else {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
        }
    }

    private void startListening() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                waitingForCommand ? "Say your command" : "Say 'Hello Alora'");
        speechLauncher.launch(intent);
    }

    private void sendToESP32(String command) {
        new Thread(() -> {
            try {
                URL url = new URL(ESP32_IP + command);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(3000);
                conn.getInputStream().close(); // Just trigger the URL

                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> speak("Command executed"));
                }
            } catch (Exception e) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> speak("ESP32 not reachable"));
                }
            }
        }).start();
    }

    private void processVoice(String command) {
        if (!waitingForCommand) {
            if (command.contains("alora")) {
                waitingForCommand = true;
                speak("Yes, I'm listening");
                startListening();
            }
        } else {
            waitingForCommand = false;
            if (command.contains("light on")) {
                speak("Turning on the light");
                sendToESP32("on");
            } else if (command.contains("light off")) {
                speak("Turning off the light");
                sendToESP32("off");
            } else {
                speak("I didn't recognize that command");
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = requireContext().getSharedPreferences("AssistantPrefs", Context.MODE_PRIVATE);
        setupSpeechLauncher();
        setupPermissionLauncher();
        setupTTS();
    }

    private void setupSpeechLauncher() {
        speechLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        ArrayList<String> res = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        if (res != null && !res.isEmpty()) {
                            String heard = res.get(0).toLowerCase().trim();
                            if(txtResult != null) txtResult.setText(heard);
                            processVoice(heard);
                        }
                    }
                });
    }

    private void setupPermissionLauncher() {
        permissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) startListening();
                    else Toast.makeText(getContext(), "Mic permission required", Toast.LENGTH_SHORT).show();
                });
    }

    private void setupTTS() {
        tts = new TextToSpeech(requireContext(), status -> {
            if (status == TextToSpeech.SUCCESS) tts.setLanguage(Locale.US);
        });
    }

    private void speak(String message) {
        if (tts != null) tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}