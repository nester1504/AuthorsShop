package com.nesterenko.authorsshop;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Вертикальная оринтация
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle
                (this, drawer, toolbar,R.string.closed,R.string.open);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.naviView);
        navigationView.setNavigationItemSelectedListener(this);

        MainFragment mainFragment = new MainFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.container, mainFragment).commit();

        //Нижняя навигация

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.active_home:
                        MainFragment mainFragment = new MainFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, mainFragment).addToBackStack(null).commit();
                        break;
                    case R.id.active_favorites:
                        FavoritesFragment favoritesFragment = new FavoritesFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, favoritesFragment).addToBackStack(null).commit();
                        break;
                    case R.id.active_message:
                        MessageFragment messageFragment = new MessageFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, messageFragment).addToBackStack(null).commit();
                        break;
                    case R.id.active_personal_area:
                        PersonFragment personFragment = new PersonFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, personFragment).addToBackStack(null).commit();
                        break;
                    case R.id.active_add:
                        Intent intent = new Intent(MainActivity.this, ToAdvertiseActivity.class);
                        startActivity(intent);
                        break;

                }
                return true;
            }
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


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        setTitle(menuItem.getItemId());
        int  itemId = menuItem.getItemId();


        drawer.closeDrawer(Gravity.START);
        return true;
    }
}
