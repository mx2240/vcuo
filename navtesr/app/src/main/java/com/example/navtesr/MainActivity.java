package com.example.navtesr;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Enable Edge-to-Edge before setContentView
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // 2. Reference UI elements
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        // 3. FIX: Handle Window Insets correctly
        // Instead of padding the whole screen (which pushes the bottom nav up),
        // we only pad the top for the status bar. The BottomNav usually handles its own bottom inset.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Apply top padding for status bar, but 0 for bottom so BottomNav stays at the very edge
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        // 4. Set initial fragment only on first creation
        if (savedInstanceState == null) {
            // Make sure R.id.nav_Album matches the ID in your res/menu/bottom_menu.xml
            bottomNav.setSelectedItemId(R.id.nav_Album);
            loadFragment(new AlbumFragment(), false);
        }

        // 5. Navigation Listener
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_Album) {
                selectedFragment = new AlbumFragment();
            } else if (itemId == R.id.nav_song) {
                selectedFragment = new SongFragment();
            } else if (itemId == R.id.nav_Artist) {
                selectedFragment = new ArtistFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment, true);
                return true;
            }
            return false;
        });
    }

    /**
     * Helper method to switch fragments
     */
    private void loadFragment(Fragment fragment, boolean withAnimation) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            withAnimation ? android.R.anim.fade_in : 0,
                            withAnimation ? android.R.anim.fade_out : 0
                    )
                    // Ensure R.id.fragment_container exists in activity_main.xml
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}