package com.nesterenko.authorsshop.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.nesterenko.authorsshop.Product;
import com.nesterenko.authorsshop.R;
import com.nesterenko.authorsshop.User;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.util.HashMap;

import io.github.kobakei.materialfabspeeddial.FabSpeedDial;
import io.github.kobakei.materialfabspeeddial.FabSpeedDialMenu;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class AnnouncementActivity extends AppCompatActivity {
    private CarouselView carouselView;
    private Product product;
    public LikeButton imageFavorite;
    private FirebaseAuth auth;
    private ImageView share;
    private String login;
    private DatabaseReference reference;
    private StorageReference storageReference;
    private FirebaseUser firebaseUser;
    private Context context;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ScrollView scrollView = (ScrollView) findViewById(R.id.scroll_view);
        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Ready, move up
                scrollView.fullScroll(View.FOCUS_UP);
            }
        });

        auth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (auth.getCurrentUser() == null) {

        } else {
            login = FirebaseAuth.getInstance().getCurrentUser().getUid();

        }

        Toolbar toolbar = findViewById(R.id.toolbar_my);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.hideOverflowMenu();
        toolbar.isOverflowMenuShowing();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();// возврат на предыдущий activity
            }
        });

        Bundle arguments = getIntent().getExtras();


        TextView announcement_price_text = findViewById(R.id.announcement_price_text);
        ImageView announcement_icon = findViewById(R.id.announcement_icon);
        TextView announcement_heading_text = findViewById(R.id.announcement_heading_text);
        TextView announcement_description_text = findViewById(R.id.announcement_description_text);
        TextView announcement_account_text = findViewById(R.id.announcement_account_text);

        carouselView = findViewById(R.id.announcement_slider);

        FabSpeedDial fabSpeedDial = findViewById(R.id.fab);

        fabSpeedDial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        fabSpeedDial.addOnStateChangeListener(new Function1<Boolean, Unit>() {
            @Override
            public Unit invoke(Boolean aBoolean) {

                return null;
            }
        });

        //Поделиться

        share = findViewById(R.id.announcement_share);
        share.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Приложение name, скачивай от сюда - ссылка");
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Поделиться"));
        });

        // Кнопка избранное
        imageFavorite = findViewById(R.id.announcement_favorite);

        if (auth.getCurrentUser() == null) {

            imageFavorite.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    showEnterWindow();
                    likeButton.setLiked(false);
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    showEnterWindow();
                    likeButton.setLiked(false);
                }
            });
        } else {

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
        }

        assert arguments != null;
        product = (Product) arguments.getSerializable(Product.class.getSimpleName());
        announcement_price_text.setText(product.getPrice() + " ");
        announcement_heading_text.setText(product.getHeading() + " ");
        announcement_description_text.setText(product.getDescription() + " ");
        announcement_account_text.setText(product.getUserName() + " ");


        String userid = product.getUserid();

        DatabaseReference referenceUser = FirebaseDatabase.getInstance().getReference("Users");
        referenceUser.child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                if(user.getImageurl().equals("standard")){
                    announcement_icon.setImageResource(R.drawable.handmade);
                } else {
                    Uri myimgUser = Uri.parse(user.getImageurl());
                    Picasso.get().load(myimgUser).into(announcement_icon);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        reference = FirebaseDatabase.getInstance().getReference().child("favorite/" + login + "/" + product.getKey());
        Boolean on = product.isFavorite();
        if (on == true) {
            imageFavorite.setLiked(true);
        } else {
            imageFavorite.setLiked(false);
        }

        Menu menu = new FabSpeedDialMenu(this);
        menu.add(0, 100, 0, "+7" + product.getPhone() + "").setIcon(R.drawable.ic_call_black);
        menu.add(0, 200, 0, "Сообщение").setIcon(R.drawable.ic_chat_bubble_outline_black);

        fabSpeedDial.setMenu((FabSpeedDialMenu) menu);


        fabSpeedDial.addOnMenuItemClickListener((floatingActionButton, textView, itemId) -> {

            switch (itemId) {
                case 100:
                    String dial = "+7" + product.getPhone();
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + dial)));
                    break;
                case 200:
                    if (product.getUserid().toString().equals(auth.getCurrentUser().getUid())) {
                        Toast.makeText(this, "Невозможно написать сообщение самому себе", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(AnnouncementActivity.this, MessageActivity.class);
                        intent.putExtra("userid", product.getUserid());
                        startActivity(intent);
                    }

            }
            return null;
        });


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

    private void status(String status) {
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (firebaseUser != null) {
            status("online");
        } else {

        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (firebaseUser != null) {
            status("offline");
        } else {

        }

    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            Glide.with(AnnouncementActivity.this).load(product.getUrlImageList().get(position)).into(imageView);
           // Picasso.get().load(product.getUrlImageList().get(position)).into(imageView);

        }
    };

    private void showEnterWindow() {

        AlertDialog dialog = new AlertDialog.Builder(AnnouncementActivity.this).create();
        dialog.setTitle("Вход в личный кабинет");
        LayoutInflater inflater = LayoutInflater.from(AnnouncementActivity.this);
        View register_window = inflater.inflate(R.layout.authorization_window, null);
        dialog.setView(register_window);
        dialog.show();
        final MaterialEditText email = register_window.findViewById(R.id.authorization_inputEmail);
        final MaterialEditText password = register_window.findViewById(R.id.authorization_inputPassword);
        final Button enter = register_window.findViewById(R.id.authorization_button_enter);
        final Button registartion = register_window.findViewById(R.id.authorization_button_registration);
        final TextView authorization_password_rec = register_window.findViewById(R.id.authorization_password_rec);


        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().length() == 0) {
                    Toast.makeText(AnnouncementActivity.this, "Введите Ваш Email", Toast.LENGTH_LONG).show();
                } else if (password.getText().length() == 0) {
                    Toast.makeText(AnnouncementActivity.this, "Введите Ваш пароль", Toast.LENGTH_LONG).show();
                } else if (email.getText().length() == 0 && password.getText().length() == 0) {
                    Toast.makeText(AnnouncementActivity.this, "Введите Ваши данные", Toast.LENGTH_LONG).show();
                } else {
                    auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(AnnouncementActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(AnnouncementActivity.this, "Успешная авторизация", Toast.LENGTH_LONG).show();
                                    MainActivity.bottomNavigationView.getMenu().findItem(R.id.active_add).setVisible(true);
                                    MainActivity.navigationView.getMenu().findItem(R.id.nav_reg_exit).setVisible(true);


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (e instanceof FirebaseAuthInvalidUserException) {
                                Toast.makeText(AnnouncementActivity.this, "Этот пользователь не найден", Toast.LENGTH_LONG).show();
                            } else if (e instanceof FirebaseAuthWeakPasswordException) {
                                Toast.makeText(AnnouncementActivity.this, "Электронная почта в неправильном формате", Toast.LENGTH_LONG).show();
                            } else if (e instanceof FirebaseNetworkException) {
                                Toast.makeText(AnnouncementActivity.this, "Проверьте соединение с интернетом", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(AnnouncementActivity.this, "Ошибка авторизации. " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }


                        }
                    });

                }
            }


        });

        registartion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnnouncementActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        authorization_password_rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AnnouncementActivity.this, "Для востановления пароля обратитесь к Аминистратору", Toast.LENGTH_SHORT).show();
            }
        });


    }

}
