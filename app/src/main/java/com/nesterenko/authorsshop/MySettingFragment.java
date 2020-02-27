package com.nesterenko.authorsshop;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class MySettingFragment extends Fragment {

    Button exit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        return inflater.inflate(R.layout.fragment_my_setting, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        exit = getView().findViewById(R.id.button_exit);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(auth.getCurrentUser() != null) {
                    auth.signOut();
                    AccountFragment accountFragment = new AccountFragment();
                    getFragmentManager().beginTransaction().replace(R.id.container, accountFragment).addToBackStack(null).commit();
                    MainActivity.bottomNavigationView.getMenu().findItem(R.id.active_add).setVisible(false);
                }
            }
        });
    }
}
