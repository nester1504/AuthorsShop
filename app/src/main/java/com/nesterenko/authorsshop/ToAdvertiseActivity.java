package com.nesterenko.authorsshop;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import gun0912.tedimagepicker.builder.TedImagePicker;
import gun0912.tedimagepicker.builder.listener.OnMultiSelectedListener;

public class ToAdvertiseActivity extends AppCompatActivity {
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ImageView img4;
    private ImageView img5;
    private List<Uri> myUriList = new ArrayList<>();
    private List<Uri> urlString = new ArrayList<>();
    private ArrayList<String> listUrl = new ArrayList<>();
    private String[] str = new String[5];
    private TextView nameText;
    private TextView priceText;
    private TextView nameSupport;
    private DatabaseReference reference;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_advertise);

        CheckBox checkBox = findViewById(R.id.materialCheckBox2);
        CheckBox checkBox1 = findViewById(R.id.materialCheckBox);
        CheckBox checkBox2 = findViewById(R.id.materialCheckBox3);



        reference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference("product");

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        ImageButton btm = findViewById(R.id.imageView4);
        img1 = findViewById(R.id.image_1);
        img2 = findViewById(R.id.image_2);
        img3 = findViewById(R.id.image_3);
        img4 = findViewById(R.id.image_4);
        img5 = findViewById(R.id.image_5);

        nameText = findViewById(R.id.name_text);
        priceText = findViewById(R.id.price_text);
        nameSupport = findViewById(R.id.nameSupport);


        priceText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(priceText.getText().toString().length()<1){
                    checkBox.setChecked(false);
                } else {
                    checkBox.setChecked(true);
                }
            }
        });

        nameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(nameText.getText().toString().length()>=1){
                    nameSupport.setVisibility(View.VISIBLE);
                } else {
                    nameSupport.setVisibility(View.GONE);
                }

                if(nameText.getText().toString().length()<3){
                    checkBox1.setChecked(false);
                } else {
                    checkBox1.setChecked(true);
                }
            }
        });

        Button buttonAdd = findViewById(R.id.buttonAdd);

        btm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TedImagePicker.with(ToAdvertiseActivity.this)
                        .startMultiImage(new OnMultiSelectedListener() {
                            @Override
                            public void onSelected(@org.jetbrains.annotations.NotNull @NotNull List<? extends Uri> uriList) {
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
                                myUriList.addAll(uriList);
                                if(img1!=null){
                                    checkBox2.setChecked(true);
                                }
                            }
                        });
            }
        });


        buttonAdd.setOnClickListener(view -> {
            uploadFile(1);
            Intent intent = new Intent(ToAdvertiseActivity.this, MainActivity.class);
            startActivity(intent);
        });

    }

    public String getFileExtension(Uri uri) {
        ContentResolver resolver = getContentResolver();
        MimeTypeMap typeMap = MimeTypeMap.getSingleton();
        return typeMap.getExtensionFromMimeType(resolver.getType(uri));
    }

    public void uploadFile(int index) {
        if (myUriList.get(index-1) != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(myUriList.get(index-1)));
            fileReference.putFile(myUriList.get(index-1)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            if (index == myUriList.size()) {
                                listUrl.add(uri.toString());
                                int priseInt = Integer.parseInt(priceText.getText().toString());
                                reference.child("product").push().setValue(new Product(priseInt, nameText.getText().toString(), listUrl));
                            } else {
                                listUrl.add(uri.toString());
                                uploadFile(index+1);
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

//    public void uploadFile(){
//
//        StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(myUriList.get(0)));
//        fileReference.putFile(myUriList.get(0)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl();
//                Uri result = downloadUrl.getResult();
//                myUriList2.add(0,result);
//                String resultStr = myUriList2.get(0).toString();
//                int priseInt = Integer.parseInt(priceText.getText().toString());
//                reference.child("product").push().setValue(new Product(priseInt, nameText.getText().toString(), resultStr));
//            }
//        });


}


