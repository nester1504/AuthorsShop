package com.nesterenko.authorsshop;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MyAnnouncementFragment extends Fragment implements ProductAdapter.ProductClickListener {
    private List<Product> list_product;
    private ProductAdapter adapter;
    private DatabaseReference reference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_my_announcement, container, false);
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
        String login = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = database.getReference("Users/" + login + "/product").getRef();

        list_product = new ArrayList<>();

        updateList();

        RecyclerView recyclerView = rootView.findViewById(R.id.orders);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        adapter = new ProductAdapter(list_product);
        adapter.setItemClickListener((ProductAdapter.ProductClickListener) this);
        recyclerView.setAdapter(adapter);


    }

    private void updateList() {
        reference.addChildEventListener(new ChildEventListener() {
            int counter = 0;

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Product prod = dataSnapshot.getValue(Product.class);
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

}
