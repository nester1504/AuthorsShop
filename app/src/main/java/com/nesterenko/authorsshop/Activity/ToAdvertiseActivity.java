package com.nesterenko.authorsshop.Activity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nesterenko.authorsshop.Product;
import com.nesterenko.authorsshop.R;
import com.nesterenko.authorsshop.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.sapereaude.maskedEditText.MaskedEditText;
import gun0912.tedimagepicker.builder.TedImagePicker;
import gun0912.tedimagepicker.builder.listener.OnMultiSelectedListener;

public class ToAdvertiseActivity extends AppCompatActivity {
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ImageView img4;
    private ImageView img5;
    private List<Uri> myUriList = new ArrayList<>();
    private ArrayList<String> listUrl = new ArrayList<>();
    private TextView nameText;
    private TextView priceText;
    private TextView contextText;
    private MaskedEditText phoneTextInput;
    private DatabaseReference reference;
    private StorageReference storageReference;
    private String login;
    private String userName;


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_advertise);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        CheckBox checkBoxFoto = findViewById(R.id.materialCheckBoxFoto);


        reference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference("product");


        login = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        DatabaseReference referenceUser = FirebaseDatabase.getInstance().getReference("Users");
        referenceUser.child(login).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                userName = user.getName();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        ImageButton btm = findViewById(R.id.imageButtonFoto);

        img1 = findViewById(R.id.image_1);
        img2 = findViewById(R.id.image_2);
        img3 = findViewById(R.id.image_3);
        img4 = findViewById(R.id.image_4);
        img5 = findViewById(R.id.image_5);

        nameText = findViewById(R.id.name_text);
        priceText = findViewById(R.id.price_text);
        contextText = findViewById(R.id.context_text);
        phoneTextInput = findViewById(R.id.phone_text);




        Button buttonAdd = findViewById(R.id.buttonAdd);

        btm.setOnClickListener(v -> TedImagePicker.with(ToAdvertiseActivity.this).max(5, "Нельзя добавлять более 5 фото").
                title("Выбирите изображение")
                .startMultiImage(uriList -> {
                    myUriList.clear();
                    myUriList.addAll(uriList);
                    if (myUriList.size() == 1) {
                        img1.setImageURI(uriList.get(0));
                        img1.setVisibility(View.VISIBLE);
                    } else if (myUriList.size() == 2) {
                        img1.setImageURI(uriList.get(0));
                        img1.setVisibility(View.VISIBLE);
                        img2.setImageURI(uriList.get(1));
                        img2.setVisibility(View.VISIBLE);
                    } else if (myUriList.size() == 3) {
                        img1.setImageURI(uriList.get(0));
                        img1.setVisibility(View.VISIBLE);
                        img2.setImageURI(uriList.get(1));
                        img2.setVisibility(View.VISIBLE);
                        img3.setImageURI(uriList.get(2));
                        img3.setVisibility(View.VISIBLE);
                    } else if (myUriList.size() == 4) {
                        img1.setImageURI(uriList.get(0));
                        img1.setVisibility(View.VISIBLE);
                        img2.setImageURI(uriList.get(1));
                        img2.setVisibility(View.VISIBLE);
                        img3.setImageURI(uriList.get(2));
                        img3.setVisibility(View.VISIBLE);
                        img4.setImageURI(uriList.get(3));
                        img4.setVisibility(View.VISIBLE);
                    } else if (myUriList.size() == 5) {
                        img1.setImageURI(uriList.get(0));
                        img1.setVisibility(View.VISIBLE);
                        img2.setImageURI(uriList.get(1));
                        img2.setVisibility(View.VISIBLE);
                        img3.setImageURI(uriList.get(2));
                        img3.setVisibility(View.VISIBLE);
                        img4.setImageURI(uriList.get(3));
                        img4.setVisibility(View.VISIBLE);
                        img5.setImageURI(uriList.get(4));
                        img5.setVisibility(View.VISIBLE);
                    }

                    if (img1 != null) {
                        checkBoxFoto.setChecked(true);
                    }
                }));


        buttonAdd.setOnClickListener(view -> {
            if(myUriList.size()==0){
                Toast.makeText(this, "Добавьте фото", Toast.LENGTH_SHORT).show();
            } else {
                uploadFile(1);
                Intent intent = new Intent(ToAdvertiseActivity.this, MainActivity.class);
                startActivity(intent);
            }

        });

    }

    public String getFileExtension(Uri uri) {
        ContentResolver resolver = getContentResolver();
        MimeTypeMap typeMap = MimeTypeMap.getSingleton();
        return typeMap.getExtensionFromMimeType(resolver.getType(uri));
    }

    public void uploadFile(int index) {
        if (myUriList.get(index - 1) != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(myUriList.get(index - 1)));
            fileReference.putFile(myUriList.get(index - 1)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            if (index == myUriList.size()) {
                                listUrl.add(uri.toString());
                                int priseInt = Integer.parseInt(priceText.getText().toString());
                                reference.child("product")
                                        .push()
                                        .setValue(new Product(priseInt, nameText.getText().toString(), listUrl, false, contextText.getText().toString(),userName, phoneTextInput.getRawText(),login));
                                reference.child("Users/" + login + "/product")
                                        .push()
                                        .setValue(new Product(priseInt, nameText.getText().toString(), listUrl, false, contextText.getText().toString(),userName,phoneTextInput.getRawText(),login));
                            } else {
                                listUrl.add(uri.toString());
                                uploadFile(index + 1);
                            }

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ToAdvertiseActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            Toast.makeText(this, "Фото не выбрано", Toast.LENGTH_SHORT).show();
        }

    }

}


