package com.nesterenko.authorsshop;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

public class AnnouncementActivity extends AppCompatActivity {

    CarouselView carouselView;
   //int[] sampleImages = {R.drawable.bracer_1, R.drawable.bracer_3, R.drawable.brooch_1, R.drawable.bracer_2, R.drawable.bracer_1};
   Product product;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Bundle arguments = getIntent().getExtras();

        TextView announcement_price_text = findViewById(R.id.announcement_price_text);
        ImageView announcement_icon = findViewById(R.id.announcement_icon);
        TextView announcement_heading_text = findViewById(R.id.announcement_heading_text);

        carouselView = (CarouselView) findViewById(R.id.announcement_slider);


        assert arguments != null;
        product = (Product) arguments.getSerializable(Product.class.getSimpleName());
        announcement_price_text.setText(product.getPrice() + " ");
        announcement_heading_text.setText(product.getHeading() +" ");
        //announcement_icon.setImageURI(Uri.parse(product.getUrlImageList().get(0)));
       // Picasso.get().load(product.getUrlImageList().get(0)).resize(600,600).into(announcement_icon);

        carouselView.setPageCount(product.getUrlImageList().size());
        carouselView.setImageListener(imageListener);

        carouselView.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(AnnouncementActivity.this, "Clicked item: "+ position, Toast.LENGTH_SHORT).show();
            }
        });
    }


    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            Picasso.get().load(product.getUrlImageList().get(position)).into(imageView);

        }
    };


}
