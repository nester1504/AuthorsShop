package com.nesterenko.authorsshop;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class PersonFragment extends Fragment {

    TextView emailText;
    TextView passwordText;
    Button btnEnter;
    TextView btnRegistration;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference users;
    LinearLayout linearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

                return inflater.inflate(R.layout.fragment_person, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        emailText = getView().findViewById(R.id.person_inputEmail);
        passwordText = getView().findViewById(R.id.person_inputPassword);
        btnEnter = getView().findViewById(R.id.person_button_enter);
        btnRegistration = getView().findViewById(R.id.person_button_registration);

        linearLayout = getView().findViewById(R.id.linearLayout);




        // FireBase

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegiserWindow();
            }
        });

        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEnterWindow();
            }
        });

    }

    private void showEnterWindow() {

        auth.signInWithEmailAndPassword(emailText.getText().toString(), passwordText.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        AccountFragment accountFragment = new AccountFragment();
                        assert getFragmentManager() != null;
                        getFragmentManager().beginTransaction().replace(R.id.container, accountFragment).addToBackStack(null).commit();
                        Toast.makeText(getContext(), "Успешная авторизация", Toast.LENGTH_LONG).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Ошибка авторизации. " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void showRegiserWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Зарегестрироваться");
        dialog.setMessage("Введите все данные для регистрации");
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View register_window = inflater.inflate(R.layout.regiter_window, null);
        dialog.setView(register_window);

        final MaterialEditText email = register_window.findViewById(R.id.emailFiend);
        final MaterialEditText password = register_window.findViewById(R.id.passFiend);
        final MaterialEditText name = register_window.findViewById(R.id.nameFiend);
        final MaterialEditText phone = register_window.findViewById(R.id.phoneFiend);

        dialog.setNeutralButton("Отменить", null);
        dialog.setPositiveButton("Готово", null);

        AlertDialog alertDialog = dialog.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(email.getText().toString())) {
                            Snackbar.make(linearLayout, "Введите вашу почту", Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        if (password.getText().length() < 5) {
                            Snackbar.make(linearLayout, "Пароль должен содержать не менее 5 символов", Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(name.getText().toString())) {
                            Snackbar.make(linearLayout, "Введите ваше Имя", Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(phone.getText().toString())) {
                            Snackbar.make(linearLayout, "Введите ваш телефон", Snackbar.LENGTH_SHORT).show();
                            return;
                        }

                        auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        User user = new User();
                                        user.setEmail(email.getText().toString());
                                        user.setName(name.getText().toString());
                                        user.setPassword(password.getText().toString());
                                        user.setPhone(phone.getText().toString());

                                        users.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Snackbar.make(linearLayout, "Мастер добавлен", Snackbar.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });

                        dialog.dismiss();
                    }
                });
            }
        });

    }
}
