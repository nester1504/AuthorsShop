package com.nesterenko.authorsshop;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import gun0912.tedimagepicker.builder.TedImagePicker;
import gun0912.tedimagepicker.builder.listener.OnSelectedListener;

public class ToAdvertiseActivity extends AppCompatActivity {
    private ImageView img;
    private Uri myUri;
    private TextView nameText;
    private TextView priceText;
    private DatabaseReference reference;
    private static final int PICK_IMAGE_REQUESR = 1;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_advertise);


        reference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference("product");

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ImageButton btmTest = findViewById(R.id.imageViewTed);
        ImageButton btm = findViewById(R.id.imageView4);
        img = findViewById(R.id.imageView5);

        nameText = findViewById(R.id.name_text);
        priceText = findViewById(R.id.price_text);

        Button buttonAdd = findViewById(R.id.buttonAdd);
//
//        btm.setOnClickListener(view -> {
//            openFileChooser();
//        });

        btmTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TedImagePicker.with(ToAdvertiseActivity.this).start(new OnSelectedListener() {
                    @Override
                    public void onSelected(Uri uri) {

                    }
                });
            }
        });

        buttonAdd.setOnClickListener(view -> {
            uploudFile();

            Intent intent = new Intent(ToAdvertiseActivity.this, MainActivity.class);
            startActivity(intent);
        });

    }

//    private void openFileChooser() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent, PICK_IMAGE_REQUESR);
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PICK_IMAGE_REQUESR && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            myUri = data.getData();
//            Picasso.get().load(myUri).into(img);
//            img.setImageURI(myUri);
//        }
//    }



    public String getFileExtension(Uri uri) {
        ContentResolver resolver = getContentResolver();
        MimeTypeMap typeMap = MimeTypeMap.getSingleton();
        return typeMap.getExtensionFromMimeType(resolver.getType(uri));
    }

    public void uploudFile() {
        if (myUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(myUri));
            fileReference.putFile(myUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            int priseInt = Integer.parseInt(priceText.getText().toString());
                            reference.child("product").push().setValue(new Product(priseInt, nameText.getText().toString(),uri.toString()));
//
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
