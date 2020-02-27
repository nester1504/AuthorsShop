package com.nesterenko.authorsshop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private FirebaseAuth auth;
    static BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.naviView);
        auth = FirebaseAuth.getInstance();



        // Вертикальная оринтация
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        //Боковая навигация
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle
                (this, drawer, toolbar, R.string.closed, R.string.open);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);



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
                    MainFragment mainFragment1 = new MainFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, mainFragment1).addToBackStack(null).commit();
                    setTitle("Главное");
                    break;
                case R.id.active_favorites:
                    setTitle("Избранное");
                    if (auth.getCurrentUser() != null) {
                        FavoritesFragment favoritesFragment = new FavoritesFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, favoritesFragment).addToBackStack(null).commit();

                    } else {
                        NoFavoriteUserFragment noFavoriteUserFragment = new NoFavoriteUserFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, noFavoriteUserFragment).addToBackStack(null).commit();
                    }

                    break;
                case R.id.active_message:
                    setTitle("Сообщения");
                    MessageFragment messageFragment = new MessageFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, messageFragment).addToBackStack(null).commit();
                    break;
                case R.id.active_personal_area:

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
        switch (menuItem.getItemId()){
            case R.id.nav_reg_exit:
                if(auth.getCurrentUser() != null) {
                    auth.signOut();
                    MainActivity.bottomNavigationView.getMenu().findItem(R.id.active_add).setVisible(false);
                }
                break;
        }
        return true;
    }


}
