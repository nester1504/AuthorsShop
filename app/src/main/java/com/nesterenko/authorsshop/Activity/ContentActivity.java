package com.nesterenko.authorsshop.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.nesterenko.authorsshop.Product;
import com.nesterenko.authorsshop.R;

public class ContentActivity extends AppCompatActivity {

    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        Bundle arguments = getIntent().getExtras();

        ImageView content_image = findViewById(R.id.content_image);
        TextView content_heading_text = findViewById(R.id.content_heading_text);

        product = (Product) arguments.getSerializable(Product.class.getSimpleName());

        content_heading_text.setText(product.getHeading() +" ");
        //content_image.setImageURI(product.getUrlImageList());
    }
}
