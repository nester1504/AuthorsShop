package com.nesterenko.authorsshop.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.nesterenko.authorsshop.Fragment.AccountFragment;
import com.nesterenko.authorsshop.Fragment.FavoritesFragment;
import com.nesterenko.authorsshop.Fragment.MainFragment;
import com.nesterenko.authorsshop.Fragment.MessageFragment;
import com.nesterenko.authorsshop.Fragment.MyAccountFragment;
import com.nesterenko.authorsshop.Fragment.NoFavoriteUserFragment;
import com.nesterenko.authorsshop.Fragment.NoMessageFragment;
import com.nesterenko.authorsshop.R;
import com.nesterenko.authorsshop.User;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    public static NavigationView navigationView;
    private FirebaseAuth auth;
    public static BottomNavigationView bottomNavigationView;
    private String userName;
    private MaterialSearchBar searchBar;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private Uri myimgUser;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        searchBar = (MaterialSearchBar) findViewById(R.id.searchBar);

        searchBar.inflateMenu(R.menu.menu_search);
        searchBar.setMenuIcon(R.drawable.ic_playlist_add_check_black);
        searchBar.getMenu().setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.search_click:
                        Toast.makeText(MainActivity.this, "Расширинный поиск в разроботке", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });



        drawer = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.naviView);
        auth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        View heard = navigationView.getHeaderView(0);
        TextView navHeaderText = heard.findViewById(R.id.nav_header_name);
        ImageView navHeaderImg = heard.findViewById(R.id.nav_header_img);


        // Вертикальная оринтация
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);


        //Боковая навигация
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle
                (this, drawer, toolbar, R.string.closed, R.string.open);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        navigationView.setNavigationItemSelectedListener(this);
        if (auth.getCurrentUser() == null) {
            navigationView.getMenu().findItem(R.id.nav_reg_exit).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_reg_settings).setVisible(false);
            navHeaderText.setText("Уважаемые гости");
            navHeaderImg.setImageResource(R.drawable.handmade);
        } else {
            navigationView.getMenu().findItem(R.id.nav_reg_exit).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_reg_settings).setVisible(true);

            String login = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference referenceUser = FirebaseDatabase.getInstance().getReference("Users");

            referenceUser.child(login).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    assert user != null;
                    userName = user.getName();
                    navHeaderText.setText(userName);
                    if(user.getImageurl().equals("standard")){
                        navHeaderImg.setImageResource(R.drawable.handmade);
                    } else {
                        myimgUser = Uri.parse(user.getImageurl());
                        Picasso.get().load(myimgUser).into(navHeaderImg);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        }


        MainFragment mainFragment = new MainFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.container, mainFragment).commit();


        //Нижняя навигация
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        if (auth.getCurrentUser() == null) {
            bottomNavigationView.getMenu().findItem(R.id.active_add).setVisible(false);
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.active_home:
                    searchBar.setVisibility(View.VISIBLE);
                    MainFragment mainFragment1 = new MainFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, mainFragment1).addToBackStack(null).commit();
                    setTitle("Главное");
                    break;
                case R.id.active_favorites:
                    setTitle("Избранное");
                    searchBar.setVisibility(View.GONE);
                    if (auth.getCurrentUser() != null) {
                        FavoritesFragment favoritesFragment = new FavoritesFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, favoritesFragment).addToBackStack(null).commit();

                    } else {
                        NoFavoriteUserFragment noFavoriteUserFragment = new NoFavoriteUserFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, noFavoriteUserFragment).addToBackStack(null).commit();
                    }

                    break;
                case R.id.active_message:
                    searchBar.setVisibility(View.GONE);
                    if (auth.getCurrentUser() != null) {
                        setTitle("Сообщения");
                        MessageFragment messageFragment = new MessageFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, messageFragment).addToBackStack(null).commit();
                    } else {
                        setTitle("Author`s Shop");
                        NoMessageFragment noMessageFragment = new NoMessageFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, noMessageFragment).addToBackStack(null).commit();

                    }

                    break;
                case R.id.active_personal_area:
                    searchBar.setVisibility(View.GONE);
                    if (auth.getCurrentUser() != null) {
                        setTitle("Личный кабинет");
                        MyAccountFragment myAccountFragment = new MyAccountFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, myAccountFragment).addToBackStack(null).commit();
                    } else {
                        setTitle("Author`s Shop");
                        AccountFragment accountFragment = new AccountFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, accountFragment).addToBackStack(null).commit();

                    }

                    break;
                case R.id.active_add:
                    Intent intent = new Intent(MainActivity.this, ToAdvertiseActivity.class);
                    startActivity(intent);
                    break;

            }
            return true;
        });



    }

    private void status (String status){
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String , Object> hashMap = new HashMap<>();
        hashMap.put("status",status);

        reference.updateChildren(hashMap);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(firebaseUser!=null){
            status("online");
        } else {

        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if(firebaseUser!=null){
            status("offline");
        } else {

        }

    }




    // Выход из приложения

    private long previusBackTime = 0;

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count > 0) {
            super.onBackPressed();
        } else {
            long currentTime = System.currentTimeMillis();
            if (currentTime - 2500 > previusBackTime) {
                Toast.makeText(this, "Для выхода нажмите ещё раз", Toast.LENGTH_SHORT).show();
                previusBackTime = System.currentTimeMillis();
            } else {
                super.onBackPressed();
            }
        }
    }


    @SuppressLint("WrongConstant")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        setTitle(menuItem.getItemId());
        int itemId = menuItem.getItemId();
        drawer.closeDrawer(Gravity.START);
        switch (menuItem.getItemId()) {
            case R.id.nav_reg_exit:
                if (auth.getCurrentUser() != null) {
                    auth.signOut();
                    MainActivity.bottomNavigationView.getMenu().findItem(R.id.active_add).setVisible(false);
                    MainActivity.navigationView.getMenu().findItem(R.id.nav_reg_exit).setVisible(false);
                    MainActivity.navigationView.getMenu().findItem(R.id.nav_reg_settings).setVisible(false);

                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                break;
            case R.id.nav_reg_settings:
                setTitle("Настройки");
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }


}

