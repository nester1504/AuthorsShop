package com.nesterenko.authorsshop.Fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nesterenko.authorsshop.R;

public class MyArchiveFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
  
        return inflater.inflate(R.layout.fragment_my_archive, container, false);
    }

}
