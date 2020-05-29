package com.nesterenko.authorsshop.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nesterenko.authorsshop.Product;
import com.nesterenko.authorsshop.R;
import com.nesterenko.authorsshop.User;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import gun0912.tedimagepicker.builder.TedImagePicker;
import gun0912.tedimagepicker.builder.listener.OnSelectedListener;

public class ProfileActivity extends AppCompatActivity {

    private ImageView profile_img;
    private Button profile_button_save;
    private String login;
    private User user;
    private Uri uriProfile;
    private StorageReference storageReference;
    private DatabaseReference reference;
    private ImageView profile_create_name;
    private TextView profile_name;
    private EditText profile_name_edit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profile_img = findViewById(R.id.profile_img);
        profile_button_save = findViewById(R.id.profile_button_save);
        profile_create_name = findViewById(R.id.profile_create_name);
        profile_name = findViewById(R.id.profile_name);
        profile_name_edit = findViewById(R.id.profile_name_edit);

        login = FirebaseAuth.getInstance().getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference("Users").child(login);
        reference = FirebaseDatabase.getInstance().getReference("Users").child(login);

        String login = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference referenceUser = FirebaseDatabase.getInstance().getReference("Users");
        referenceUser.child(login).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                assert user != null;
                profile_name.setText(user.getName());
                if(user.getImageurl().equals("standard")){
                    profile_img.setImageResource(R.drawable.handmade);
                } else {
                    Uri myimgUser = Uri.parse(user.getImageurl());
                    Picasso.get().load(myimgUser).into(profile_img);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            //
            }
        });


        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TedImagePicker.with(ProfileActivity.this).start(new OnSelectedListener() {
                    @Override
                    public void onSelected(@NotNull Uri uri) {
                        profile_img.setImageURI(uri);
                        uriProfile = uri;
                    }
                });
            }
        });


        profile_create_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile_name.setVisibility(View.GONE);
                profile_name_edit.setVisibility(View.VISIBLE);
                profile_name_edit.setText(user.getName());
                profile_name_edit.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(profile_name_edit, InputMethodManager.SHOW_IMPLICIT);
            }
        });


        profile_button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uriProfile));
                fileReference.putFile(uriProfile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                HashMap<String , Object> hashMap = new HashMap<>();
                                hashMap.put("imageurl", uri.toString());
                                reference.updateChildren(hashMap);
                                //reference.setValue()
                                //
                                }

                            });
                        }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    public String getFileExtension(Uri uri) {
        ContentResolver resolver = getContentResolver();
        MimeTypeMap typeMap = MimeTypeMap.getSingleton();
        return typeMap.getExtensionFromMimeType(resolver.getType(uri));
    }

}

