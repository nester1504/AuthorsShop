package com.nesterenko.authorsshop;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

public class AnnouncementActivity extends AppCompatActivity {

    CarouselView carouselView;
   //int[] sampleImages = {R.drawable.bracer_1, R.drawable.bracer_3, R.drawable.brooch_1, R.drawable.bracer_2, R.drawable.bracer_1};
   Product product;
   public LikeButton imageFavorite;

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

        imageFavorite = findViewById(R.id.announcement_favorite);



        imageFavorite.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                String login = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("favorite/" + login + "/" + product.getKey());
                product.setFavorite(true);
                ref.setValue(product);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                String login = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("favorite/" + login + "/" + product.getKey());
                ref.removeValue();
            }
        });

        assert arguments != null;
        product = (Product) arguments.getSerializable(Product.class.getSimpleName());
        announcement_price_text.setText(product.getPrice() + " ");
        announcement_heading_text.setText(product.getHeading() +" ");

        if(product.isFavorite() == true){
            imageFavorite.setLiked(true);
        } else {
            imageFavorite.setLiked(false);
        }

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
