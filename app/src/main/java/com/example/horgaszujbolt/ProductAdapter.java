package com.example.horgaszujbolt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>  implements Filterable {
    private ArrayList<Product> pProductsData  = new ArrayList<>();
    private ArrayList<Product> pProductsDataAll  = new ArrayList<>();
    private Context pContext;
    private int lastPosition = -1;

    ProductAdapter(Context context, ArrayList<Product> productsData){
        this.pProductsData = productsData;
        this.pProductsDataAll = productsData;
        this.pContext = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(pContext).inflate(R.layout.product_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ProductAdapter.ViewHolder holder, int position) {
        Product current = pProductsData.get(position);
        
        holder.bindTo(current);

        if(holder.getAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(pContext, R.anim.slide_new);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {return pProductsData.size();}

    @Override
    public Filter getFilter() {
        return productFilter;
    }
    private Filter productFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Product> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if(charSequence == null || charSequence.length() == 0) {
                results.count = pProductsDataAll.size();
                results.values = pProductsDataAll;
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for(Product product : pProductsDataAll) {
                    if(product.getLeiras().toLowerCase().contains(filterPattern)){
                        filteredList.add(product);
                    }
                }

                results.count = filteredList.size();
                results.values = filteredList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            pProductsDataAll = (ArrayList)filterResults.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView pTitle;
        private TextView pName;
        private TextView pPrice;
        private ImageView pImage;

        public ViewHolder(View itemView) {
            super(itemView);

             pName = itemView.findViewById(R.id.productName);
             pPrice = itemView.findViewById(R.id.productPrice);
             pImage = itemView.findViewById(R.id.productImage);

        }

        public void bindTo(Product current) {
            pName.setText(current.getLeiras());
            pPrice.setText(current.getAr());

            Glide.with(pContext).load(current.getKepResource()).into(pImage);
        }
    }
}

