package com.nesterenko.authorsshop;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.List;

public class ToAdvertiseActivity extends AppCompatActivity {
    ImageButton btm;
    ImageView img;
    Uri myUri;
    private TextView nameText;
    private TextView priceText;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_advertise);

        reference = FirebaseDatabase.getInstance().getReference();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btm = findViewById(R.id.imageView4);
        img = findViewById(R.id.imageView5);

        nameText = findViewById(R.id.name_text);
        priceText = findViewById(R.id.price_text);

        Button buttonAdd = findViewById(R.id.buttonAdd);


        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int priseInt = Integer.parseInt( priceText.getText().toString());
                reference.child("product").push().setValue(new Product(priseInt,nameText.getText().toString(),2,""));
                Intent intent = new Intent(ToAdvertiseActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    public void onChooseaFile(View v) {
        CropImage.activity().start(ToAdvertiseActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                myUri =result.getUri();
                img.setImageURI(myUri);
            } else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception e = result.getError();
                Toast.makeText(this,"Ошибка загрузки" + e,Toast.LENGTH_LONG).show();
            }
        }
    }
}
