package com.nesterenko.authorsshop.Fragment;


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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.nesterenko.authorsshop.Activity.MainActivity;
import com.nesterenko.authorsshop.Activity.RegistrationActivity;
import com.nesterenko.authorsshop.Fragment.MyAccountFragment;
import com.nesterenko.authorsshop.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import static com.nesterenko.authorsshop.Activity.MainActivity.navigationView;


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
        CardView account_feedback = getView().findViewById(R.id.account_feedback);
        CardView account_assessment = getView().findViewById(R.id.account_assessment);


        account_assessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Будет доступна после реализации в PlayMarket", Toast.LENGTH_SHORT).show();
            }
        });

        account_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHelpWindow();
            }
        });



        account_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEnterWindow();
            }
        });

    }

    private void showHelpWindow(){
        AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        dialog.setTitle("Написать в редакцию");
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View help_window = inflater.inflate(R.layout.help_window, null);
        dialog.setView(help_window);
        dialog.show();

        final MaterialEditText help_theme = help_window.findViewById(R.id.help_theme);
        final MaterialEditText help_error = help_window.findViewById(R.id.help_error);
        final MaterialEditText help_name = help_window.findViewById(R.id.help_name);
        final MaterialEditText help_email = help_window.findViewById(R.id.help_email);
        final Button help_button_send = help_window.findViewById(R.id.help_button_send);


        help_button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toast.makeText(getContext(), "Благодарим за отзыв, мы обязательно расмотрим Ваше сообщение", Toast.LENGTH_LONG).show();
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
        final TextView authorization_password_rec = register_window.findViewById(R.id.authorization_password_rec);




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
                                       navigationView.getMenu().findItem(R.id.nav_reg_exit).setVisible(true);
                                       navigationView.getMenu().findItem(R.id.nav_reg_settings).setVisible(true);


                                       Intent intent = new Intent(getContext(), MainActivity.class);
                                       intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                       startActivity(intent);


                                     //  getActivity().recreate();

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

        authorization_password_rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Для востановления пароля обратитесь к Аминистратору", Toast.LENGTH_SHORT).show();
            }
        });


    }


}
