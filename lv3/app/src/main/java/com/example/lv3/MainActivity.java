package com.example.lv3;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// 1. Removed unused imports (EdgeToEdge, Insets, View) for cleaner code.
// 2. Removed the EdgeToEdge boilerplate as it's often not needed for simple layouts.

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // The call to EdgeToEdge.enable(this) has been removed for simplicity.
        // It can be added back if edge-to-edge display is a specific requirement.
        setContentView(R.layout.activity_main);

        // This listener handles system UI padding, which is good practice.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            // Using android.R.id.content is a robust way to get the root view.
            v.setPadding(insets.getSystemWindowInsetLeft(), insets.getSystemWindowInsetTop(),
                    insets.getSystemWindowInsetRight(), insets.getSystemWindowInsetBottom());
            return insets;
        });

        // Find the button by its ID
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button myButton = findViewById(R.id.myButton);

        // Set a click listener on the button using a more concise lambda expression.
        myButton.setOnClickListener(v -> {
            // Show a toast message when the button is clicked
            Toast.makeText(MainActivity.this, "Button Clicked!", Toast.LENGTH_SHORT).show();
        });
    }
}
