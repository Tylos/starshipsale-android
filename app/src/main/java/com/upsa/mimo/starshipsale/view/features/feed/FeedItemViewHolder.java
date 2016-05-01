package com.upsa.mimo.starshipsale.view.features.feed;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.upsa.mimo.starshipsale.R;
import com.upsa.mimo.starshipsale.domain.entities.Product;

import java.text.NumberFormat;

class FeedItemViewHolder extends RecyclerView.ViewHolder {

    private Context itemViewContext;
    private FeedAdapter.OnProductClickListener mProductClickListener;
    private TextView mName;
    private ImageView mImage;
    private TextView mPrice;
    private ImageView mFavorite;
    private ImageView mAddedToCart;
    private Product mBoundProduct;

    public FeedItemViewHolder(View itemView, FeedAdapter.OnProductClickListener productClickListener) {
        super(itemView);
        itemViewContext = itemView.getContext();
        findViews(itemView);
        mProductClickListener = productClickListener;
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
        String formattedText = null;
        try {
            final Double aDouble = Double.valueOf(product.getPrice());
            NumberFormat numberFormatter = NumberFormat.getInstance();
            numberFormatter.setMaximumFractionDigits(0);
            formattedText = numberFormatter.format(aDouble);
        } catch (NumberFormatException exception) {
            formattedText = product.getPrice();
        } finally {
            mPrice.setText(formattedText);
        }
        mFavorite.setActivated(product.isFavorite());
        mAddedToCart.setActivated(product.isAddedToCart());
        Picasso.with(itemViewContext).
                load(product.getImage())
                .fit()
                .into(mImage);
    }
}
