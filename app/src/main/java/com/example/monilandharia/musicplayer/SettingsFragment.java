package com.example.monilandharia.musicplayer;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    private Switch switchSms, switchGestureControl;
    private TextView textViewPersonalize, textViewEqualizer, textViewTimer;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        intiViews(view);

        switchSms.setChecked(getActivity().getSharedPreferences("com.example.monilandharia.musicplayer", Context.MODE_PRIVATE).getBoolean("isSms", false));
        switchGestureControl.setChecked(getActivity().getSharedPreferences("com.example.monilandharia.musicplayer", Context.MODE_PRIVATE).getBoolean("isGestureControl", false));

        switchSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchSms.isChecked()) {
                    MainActivity.isSmsEnabled = true;
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("com.example.monilandharia.musicplayer", Context.MODE_PRIVATE).edit();
                    editor.putBoolean("isSms", true);
                    editor.commit();
                } else {
                    MainActivity.isSmsEnabled = false;
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("com.example.monilandharia.musicplayer", Context.MODE_PRIVATE).edit();
                    editor.putBoolean("isSms", false);
                    editor.commit();
                }
            }
        });

        switchGestureControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchGestureControl.isChecked()) {
                    MainActivity.isGestureControlEnabled = true;
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("com.example.monilandharia.musicplayer", Context.MODE_PRIVATE).edit();
                    editor.putBoolean("isGestureControl", true);
                    editor.commit();
                } else {
                    MainActivity.isGestureControlEnabled = false;
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("com.example.monilandharia.musicplayer", Context.MODE_PRIVATE).edit();
                    editor.putBoolean("isGestureControl", false);
                    editor.commit();
                }
            }
        });

        return view;
    }

    private void intiViews(View view) {
        switchSms = view.findViewById(R.id.switchSms);
        switchGestureControl = view.findViewById(R.id.switchGestureControl);
        textViewPersonalize = view.findViewById(R.id.personalizeTextView);
        textViewEqualizer = view.findViewById(R.id.equalizerTextView);
        textViewTimer = view.findViewById(R.id.timerTextView);

        Typeface face = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Raleway-Bold.ttf");
        switchGestureControl.setTypeface(face);
        switchSms.setTypeface(face);
    }

}
