package com.upsa.mimo.starshipsale.view.features.feed;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.upsa.mimo.starshipsale.R;
import com.upsa.mimo.starshipsale.domain.entities.Product;

import java.util.ArrayList;
import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedItemViewHolder> {

    public static final int TYPE_FEATURED = 1;
    private static final int TYPE_DEFAULT = 2;

    private List<Product> products = new ArrayList<>();
    private OnProductClickListener productClickListener = new NullProductClickListener();

    private static class NullProductClickListener implements OnProductClickListener {

        @Override
        public void onProductClick(Product product) {
        }

        @Override
        public void onFavoriteClick(Product product) {
        }

        @Override
        public void onAddToCartClick(Product product) {
        }
    }

    @Override
    public FeedItemViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View layoutView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new FeedItemViewHolder(layoutView, productClickListener);
    }

    @Override
    public void onBindViewHolder(FeedItemViewHolder productViewHolder, int i) {
        productViewHolder.bind(products.get(i));
    }

    @Override
    public void onViewRecycled(FeedItemViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void update(Product product) {
        final int index = products.indexOf(product);
        products.set(index, product);
        notifyItemChanged(index);
    }

    @Override
    public int getItemViewType(int position) {
        final Product product = products.get(position);
        return product.isFeatured() ? TYPE_FEATURED : TYPE_DEFAULT;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void setProductClickListener(OnProductClickListener productClickListener) {
        this.productClickListener = productClickListener;
    }

    public interface OnProductClickListener {

        void onProductClick(Product product);

        void onFavoriteClick(Product product);

        void onAddToCartClick(Product product);
    }
}
