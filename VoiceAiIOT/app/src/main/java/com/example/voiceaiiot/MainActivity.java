package com.example.voiceaiiot;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the BottomNavigationView from the layout
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        // --- FIX 1: Set a default selected item and load the initial fragment ---
        // This ensures the app starts on the home screen if it's the first time loading.
        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.nav_home); // Highlight the home icon
            loadFragment(new HomeFragment(), false);    // Load HomeFragment without animation
        }

        // --- FIX 2: A single, correct listener for item selection ---
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            // Mapping menu IDs to their respective fragments
            if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.nav_voice) {
                selectedFragment = new VoiceFragment();
            } else if (itemId == R.id.nav_chat) {
                selectedFragment = new ChatFragment();
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }

            // Load the selected fragment with animation
            if (selectedFragment != null) {
                loadFragment(selectedFragment, true);
                return true; // Return true to visually select the item
            }
            return false;
        });
    }

    /**
     * Replaces the current fragment in the fragment_container.
     * @param fragment The new fragment to display.
     * @param withAnimation True to use a fade animation, false otherwise.
     */
    private void loadFragment(Fragment fragment, boolean withAnimation) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    // Set animations: Use internal android resources for simple fades
                    .setCustomAnimations(
                            withAnimation ? android.R.anim.fade_in : 0,
                            withAnimation ? android.R.anim.fade_out : 0
                    )
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}