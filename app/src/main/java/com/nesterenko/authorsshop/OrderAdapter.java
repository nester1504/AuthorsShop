package com.nesterenko.authorsshop;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<OrderTest> dataset;
    private OrderClickListener listener;

    public OrderAdapter(List<OrderTest> dataset){
        this.dataset = dataset;
    }

    @NonNull
    @Override
    public OrdersMyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.listitem_new_contacts, viewGroup, false);
        return new OrdersMyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

    final OrderTest orderTest = dataset.get(position);
    OrdersMyHolder vh = (OrdersMyHolder) viewHolder;
    vh.price.setText(orderTest.getPrice() +" ");
    vh.product.setText(orderTest.getProduct()+"");
    vh.imageViewProduct.setImageResource(orderTest.getImage());
    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(listener != null){
                listener.onRecyclerItemClicked(orderTest,position);
            }
        }
    });

    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    class OrdersMyHolder extends RecyclerView.ViewHolder{
        ImageView imageViewProduct;
        TextView product;
        TextView price;

        public OrdersMyHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProduct = itemView.findViewById(R.id.imageProduct);
            product = itemView.findViewById(R.id.textProduct);
            price = itemView.findViewById(R.id.textPrice);
        }
    }

    public void setItemClickListener(OrderClickListener listener){
        this.listener = listener;
    }

    interface OrderClickListener{
        void onRecyclerItemClicked(OrderTest orderTest, int position);
    }

}
