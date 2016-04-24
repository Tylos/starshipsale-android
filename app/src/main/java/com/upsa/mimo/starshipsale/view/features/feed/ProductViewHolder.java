package com.upsa.mimo.starshipsale.view.features.feed;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.upsa.mimo.starshipsale.R;
import com.upsa.mimo.starshipsale.domain.entities.Product;

class ProductViewHolder extends RecyclerView.ViewHolder {

    private ProductAdapter.OnProductClickListener mProductClickListener;
    private TextView mName;
    private ImageView mImage;
    private TextView mPrice;
    private ImageView mFavorite;
    private ImageView mAddedToCart;
    private Product mBoundProduct;

    public ProductViewHolder(View itemView, ProductAdapter.OnProductClickListener productClickListener) {
        super(itemView);
        mProductClickListener = productClickListener;
        findViews(itemView);
        attachListeners(itemView);
    }

    private void findViews(View itemView) {
        mName = (TextView) itemView.findViewById(R.id.product_name);
        mImage = (ImageView) itemView.findViewById(R.id.product_image);
        mPrice = (TextView) itemView.findViewById(R.id.product_price);
        mFavorite = (ImageView) itemView.findViewById(R.id.product_favorite);
        mAddedToCart = (ImageView) itemView.findViewById(R.id.product_cart);
    }

    private void attachListeners(View itemView) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProductClickListener.onProductClick(mBoundProduct);
            }
        });
        mFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProductClickListener.onFavoriteClick(mBoundProduct);
            }
        });

        mAddedToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProductClickListener.onAddToCartClick(mBoundProduct);
            }
        });
    }

    public void bind(Product product) {
        mBoundProduct = product;
        renderProduct(mBoundProduct);
    }

    private void renderProduct(Product product) {
        mName.setText(product.getName());
        mPrice.setText(product.getPrice() + " €");
        mFavorite.setActivated(product.isFavorite());
        mAddedToCart.setActivated(product.isAddedToCart());
    }
}
