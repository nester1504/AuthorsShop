package com.nesterenko.authorsshop;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment implements ProductAdapter.ProductClickListener {

    private List<Product> list_product;
    private ProductAdapter adapter;
    private DatabaseReference reference;
    private RecyclerView recyclerView;
    private boolean isScrolling = false;
    private int currentItems, totalItems, scrollOutItems;
    private LinearLayoutManager manager;
    int counter = 15;



    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_main, container, false);
    }


    @Override
    public void onRecyclerItemClicked(Product product, int position) {
        Intent intent = new Intent(getContext(), AnnouncementActivity.class);
        intent.putExtra(Product.class.getSimpleName(), product);
        startActivity(intent);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View rootView = getView();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("product")
                .getRef();


        list_product = new ArrayList<>();

        updateList();

        assert rootView != null;
        recyclerView = rootView.findViewById(R.id.orders);




        adapter = new ProductAdapter(list_product);
        adapter.setItemClickListener(this);
        recyclerView.setAdapter(adapter);

        manager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(manager);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                isScrolling = true;
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = manager.getChildCount();
                totalItems = manager.getItemCount();
                scrollOutItems = manager.findFirstVisibleItemPosition();

                if(isScrolling && (currentItems + scrollOutItems == totalItems)){
                    isScrolling = false;
                    fetchData();
                }


            }
        });

    }

    private void fetchData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                counter = counter+15;
            }
        },5000);
    }

    private void updateList() {

        reference.limitToFirst(counter).addChildEventListener(new ChildEventListener() {
            //orderByChild("heading").equalTo(counter)
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Product prod = dataSnapshot.getValue(Product.class);
                assert prod != null;
                prod.setKey(dataSnapshot.getKey());
                list_product.add(0, prod);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Product product = dataSnapshot.getValue(Product.class);
                int index = getItemIndex(product);
                list_product.set(index, product);
                adapter.notifyItemChanged(index);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Product product = dataSnapshot.getValue(Product.class);
                int index = getItemIndex(product);
                list_product.remove(index);
                adapter.notifyItemRemoved(index);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private int getItemIndex(Product product) {
        int index = -1;
        for (int i = 0; i < list_product.size(); i++) {
            if (list_product.get(i).key.equals(product.key)) {
                index = i;
                break;
            }
        }
        return index;
    }

    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_toolbar_search, menu);
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Поиск");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return true;
            }
        });

    }

    private void search(String str) {

        ArrayList<Product> myList = new ArrayList<>();
        for (Product object : list_product) {
            if (object.getHeading().toLowerCase().contains(str.toLowerCase())) {
                myList.add(object);
            }
        }
        ProductAdapter productAdapter = new ProductAdapter(myList);
        recyclerView.setAdapter(productAdapter);
        productAdapter.setItemClickListener(this);

    }


}

