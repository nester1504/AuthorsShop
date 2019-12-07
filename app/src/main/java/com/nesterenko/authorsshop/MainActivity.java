package com.nesterenko.authorsshop;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ImageView impHome;
    ImageView impPerson;
    ImageView impMessage;
    private ListView list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = findViewById(R.id.list);

        final ArrayList<Map<String, Object>> adapterValues = new ArrayList<>();

        Map<String, Object> itemValues = new HashMap<>();
        itemValues.put("price", "10000");
        itemValues.put("product", "Браслет из бисера");
        itemValues.put("id", 1);
        itemValues.put("image", R.drawable.bracer_3);

        adapterValues.add(itemValues);

        itemValues = new HashMap<>();
        itemValues.put("price", "8000");
        itemValues.put("product", "Браслет из бисера");
        itemValues.put("id", 2);
        itemValues.put("image", R.drawable.bracer_2);

        adapterValues.add(itemValues);

        itemValues = new HashMap<>();
        itemValues.put("price", "12000");
        itemValues.put("product", "Брошка из бисера");
        itemValues.put("id", 3);
        itemValues.put("image", R.drawable.brooch_1);

        adapterValues.add(itemValues);

        itemValues = new HashMap<>();
        itemValues.put("price", "5000");
        itemValues.put("product", "Браслет из чашуи жопя дракона");
        itemValues.put("id", 4);
        itemValues.put("image", R.drawable.bracer_1);

        adapterValues.add(itemValues);

        itemValues = new HashMap<>();
        itemValues.put("price", "7000");
        itemValues.put("product", "СЕрьги победителя");
        itemValues.put("id", 5);
        itemValues.put("image", R.drawable.earring_1);

        adapterValues.add(itemValues);


        String[] key = new String[]{"price", "product", "image"};

        int ids[] = new int[]{R.id.price, R.id.product, R.id.imageViewHero};

        final SimpleAdapter adapter = new SimpleAdapter(this,
                adapterValues,
                R.layout.listitem_new_contacts,
                key,
                ids);

        list.setAdapter(adapter);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.active_home:

                        break;
                    case R.id.active_stars:

                        break;
                    case R.id.active_personal_area:

                        break;

                }
                return true;
            }
        });

    }
}
