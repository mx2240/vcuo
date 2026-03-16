package com.example.voiceaiiot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

// FIX 1: Removed all the boilerplate comments, newInstance method, and unused parameters.
public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment. This creates the view.
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    // FIX 2: Moved all view-related logic to onViewCreated.
    // This method is called immediately after onCreateView has returned, ensuring the view is not null.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Load the animation from the anim resource folder.
        Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up_fade);

        // FIX 3: Correctly find the views using the 'view' parameter provided by onViewCreated.
        View energyCard = view.findViewById(R.id.energyCard);
        View deviceGrid = view.findViewById(R.id.deviceGrid);

        // Start the animation on the views.
        energyCard.startAnimation(anim);
        deviceGrid.startAnimation(anim);
    }

    // FIX 4: The original onCreate method was trying to do view work, so it has been removed.
    // The default implementation is sufficient if you are not handling arguments.
}
