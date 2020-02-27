package com.nesterenko.authorsshop;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

public class RegistrationActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        Button registration_button = findViewById(R.id.registration_button_registration);

        registration_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

    }

    private void register() {

        final MaterialEditText email = findViewById(R.id.registration_email_text);
        final MaterialEditText password = findViewById(R.id.registration_password_text);
        final MaterialEditText name = findViewById(R.id.registration_name_text);
        final MaterialEditText phone = findViewById(R.id.registration_phone_text);

        if (TextUtils.isEmpty(email.getText().toString())) {
            Toast.makeText(this, "Введите вашу почту", Toast.LENGTH_SHORT).show();
        } else if (password.getText().toString().length() < 5) {
            Toast.makeText(this, "Ваш пароль должен состоять не менее чем из 5 символов", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(name.getText().toString())) {
            Toast.makeText(this, "Введите ваше имя", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phone.getText().toString())) {
            Toast.makeText(this, "Введите ваш телефон", Toast.LENGTH_SHORT).show();
        } else {

        auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        User user = new User();
                        user.setEmail(email.getText().toString());
                        user.setPassword(password.getText().toString());
                        user.setName(name.getText().toString());
                        user.setPhone(phone.getText().toString());

                        users.push().setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                                startActivity(intent);
                                Toast.makeText(RegistrationActivity.this, "Успешная регистрация", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

    }
    }
}
