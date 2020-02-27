package com.nesterenko.authorsshop;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.rengwuxian.materialedittext.MaterialEditText;


public class AccountFragment extends Fragment {

    private FirebaseAuth auth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        auth = FirebaseAuth.getInstance();

        CardView account_enter = getView().findViewById(R.id.account_enter);


        account_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEnterWindow();
            }
        });

    }

    private void showEnterWindow() {

        AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        dialog.setTitle("Вход в личный кабинет");
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View register_window = inflater.inflate(R.layout.authorization_window, null);
        dialog.setView(register_window);
        dialog.show();
        final MaterialEditText email = register_window.findViewById(R.id.authorization_inputEmail);
        final MaterialEditText password = register_window.findViewById(R.id.authorization_inputPassword);
        final Button enter = register_window.findViewById(R.id.authorization_button_enter);
        final Button registartion = register_window.findViewById(R.id.authorization_button_registration);

       enter.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (email.getText().length() == 0) {
                   Toast.makeText(getContext(), "Введите Ваш Email", Toast.LENGTH_LONG).show();
               } else if (password.getText().length() == 0) {
                   Toast.makeText(getContext(), "Введите Ваш пароль", Toast.LENGTH_LONG).show();
               } else if (email.getText().length() == 0 && password.getText().length() == 0) {
                   Toast.makeText(getContext(), "Введите Ваши данные", Toast.LENGTH_LONG).show();
               } else {
                       auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                               .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                   @Override
                                   public void onSuccess(AuthResult authResult) {
                                       dialog.dismiss();
                                       MyAccountFragment myAccountFragment = new MyAccountFragment();
                                       assert getFragmentManager() != null;
                                       getFragmentManager().beginTransaction().replace(R.id.container, myAccountFragment).addToBackStack(null).commit();
                                       Toast.makeText(getContext(), "Успешная авторизация", Toast.LENGTH_LONG).show();
                                       MainActivity.bottomNavigationView.getMenu().findItem(R.id.active_add).setVisible(true);


                                   }
                               }).addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               if (e instanceof FirebaseAuthInvalidUserException) {
                                   Toast.makeText(getContext(), "Этот пользователь не найден", Toast.LENGTH_LONG).show();
                               } else if (e instanceof FirebaseAuthWeakPasswordException) {
                                   Toast.makeText(getContext(), "Электронная почта в неправильном формате", Toast.LENGTH_LONG).show();
                               } else if (e instanceof FirebaseNetworkException) {
                                   Toast.makeText(getContext(), "Проверьте соединение с интернетом", Toast.LENGTH_LONG).show();
                               }  else {
                                   Toast.makeText(getContext(), "Ошибка авторизации. " + e.getMessage(), Toast.LENGTH_LONG).show();
                               }


                           }
                       });

                   }
               }




       });

       registartion.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(getContext(), RegistrationActivity.class);
               startActivity(intent);
           }
       });


    }


}
