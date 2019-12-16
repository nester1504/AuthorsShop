package com.nesterenko.authorsshop;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<Product> dataset;
    private ProductClickListener listener;

    public ProductAdapter(List<Product> dataset) {
        this.dataset = dataset;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.listitem_new_contacts, viewGroup, false);
        return new OrdersMyHolderProduct(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        final Product product = dataset.get(position);
        OrdersMyHolderProduct vh = (OrdersMyHolderProduct) viewHolder;
        vh.price.setText(product.getPrice() + " ");
        vh.product.setText(product.getHeading() + "");
        //Изменить!!!!!
        vh.imageViewProduct.setImageResource(R.drawable.bracer_1);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onRecyclerItemClicked(product, position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    class OrdersMyHolderProduct extends RecyclerView.ViewHolder {
        ImageView imageViewProduct;
        TextView product;
        TextView price;

        public OrdersMyHolderProduct(@NonNull View itemView) {
            super(itemView);
            imageViewProduct = itemView.findViewById(R.id.imageProduct);
            product = itemView.findViewById(R.id.textProduct);
            price = itemView.findViewById(R.id.textPrice);
        }
    }

    public void setItemClickListener(ProductClickListener listener) {
        this.listener = listener;
    }

    interface ProductClickListener {
        void onRecyclerItemClicked(Product product, int position);
    }
}