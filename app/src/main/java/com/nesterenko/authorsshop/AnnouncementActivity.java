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
    Product product;
    public LikeButton imageFavorite;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Bundle arguments = getIntent().getExtras();

        auth = FirebaseAuth.getInstance();

        TextView announcement_price_text = findViewById(R.id.announcement_price_text);
        ImageView announcement_icon = findViewById(R.id.announcement_icon);
        TextView announcement_heading_text = findViewById(R.id.announcement_heading_text);
        TextView announcement_description_text = findViewById(R.id.announcement_description_text);

        carouselView = findViewById(R.id.announcement_slider);

        // Кнопка избранное
        imageFavorite = findViewById(R.id.announcement_favorite);
        imageFavorite.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                String login = FirebaseAuth.getInstance().getCurrentUser().getUid();
                if (auth.getCurrentUser() != null) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("favorite/" + login + "/" + product.getKey());
                    product.setFavorite(true);
                    ref.setValue(product);
                } else {
                    Toast.makeText(AnnouncementActivity.this, "Авторизируйтесь", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void unLiked(LikeButton likeButton) {
                if (auth.getCurrentUser() != null) {
                    String login = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("favorite/" + login + "/" + product.getKey());
                    ref.removeValue();
                } else {
                    Toast.makeText(AnnouncementActivity.this, "Авторизируйтесь", Toast.LENGTH_SHORT).show();
                }

            }
        });


        assert arguments != null;
        product = (Product) arguments.getSerializable(Product.class.getSimpleName());
        announcement_price_text.setText(product.getPrice() + " ");
        announcement_heading_text.setText(product.getHeading() + " ");
        announcement_description_text.setText(product.getDescription() + " ");


        //Карусель фото
        carouselView.setPageCount(product.getUrlImageList().size());
        carouselView.setImageListener(imageListener);
        carouselView.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(AnnouncementActivity.this, "Clicked item: " + position, Toast.LENGTH_SHORT).show();
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
