package com.example.pelorusbv.pelorus;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.maps.android.SphericalUtil;

public class FragmentDisplay extends Fragment {

    public FragmentDisplay() {
    }

    public static FragmentDisplay newInstance() {
        FragmentDisplay displayFragment = new FragmentDisplay();
        return displayFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_display, container, false);
    }



}
