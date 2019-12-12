package com.nesterenko.authorsshop;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements OrderAdapter.OrderClickListener {
    OrderAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View rootView = getView();
        List<OrderTest> dataset = getSampleDataSet();
        adapter = new OrderAdapter(dataset);
        adapter.setItemClickListener(this);
        RecyclerView orderList = rootView.findViewById(R.id.orders);
        orderList.setAdapter(adapter);
        orderList.setLayoutManager(new GridLayoutManager(getContext(), 2));

    }

    private List<OrderTest> getSampleDataSet() {
        List<OrderTest> dataSet = new ArrayList<>();
        dataSet.add(new OrderTest(5000, "Брошка", R.drawable.brooch_1));
        dataSet.add(new OrderTest(10000, "Браслет", R.drawable.bracer_1));
        dataSet.add(new OrderTest(15000, "Браслет из бисера", R.drawable.bracer_2));
        dataSet.add(new OrderTest(5000, "Серьги", R.drawable.earring_1));
        dataSet.add(new OrderTest(5000, "Брошка", R.drawable.brooch_1));
        dataSet.add(new OrderTest(5000, "Брошка", R.drawable.brooch_1));
        dataSet.add(new OrderTest(5000, "Брошка", R.drawable.brooch_1));
        dataSet.add(new OrderTest(5000, "Брошка", R.drawable.brooch_1));
        dataSet.add(new OrderTest(5000, "Брошка", R.drawable.brooch_1));
        dataSet.add(new OrderTest(5000, "Брошка", R.drawable.brooch_1));
        dataSet.add(new OrderTest(5000, "Брошка", R.drawable.brooch_1));
        dataSet.add(new OrderTest(5000, "Брошка", R.drawable.brooch_1));
        dataSet.add(new OrderTest(5000, "Брошка", R.drawable.brooch_1));
        dataSet.add(new OrderTest(5000, "Брошка", R.drawable.brooch_1));
        dataSet.add(new OrderTest(5000, "Брошка", R.drawable.brooch_1));
        dataSet.add(new OrderTest(5000, "Брошка", R.drawable.brooch_1));
        dataSet.add(new OrderTest(5000, "Брошка", R.drawable.brooch_1));
        dataSet.add(new OrderTest(5000, "Брошка", R.drawable.brooch_1));
        dataSet.add(new OrderTest(5000, "Брошка", R.drawable.brooch_1));
        dataSet.add(new OrderTest(5000, "Брошка", R.drawable.brooch_1));
        dataSet.add(new OrderTest(5000, "Брошка", R.drawable.brooch_1));
        dataSet.add(new OrderTest(5000, "Брошка", R.drawable.brooch_1));
        dataSet.add(new OrderTest(5000, "Брошка", R.drawable.brooch_1));
        dataSet.add(new OrderTest(5000, "Брошка", R.drawable.brooch_1));
        return dataSet;
    }

    @Override
    public void onRecyclerItemClicked(OrderTest orderTest, int position) {
        Intent intent = new Intent(getContext(), AnnouncementActivity.class);
        startActivity(intent);
        //Toast.makeText(getContext(),"" +position,Toast.LENGTH_SHORT).show();
    }
}
