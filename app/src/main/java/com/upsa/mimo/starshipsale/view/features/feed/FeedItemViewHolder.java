package com.upsa.mimo.starshipsale.view.features.feed;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.upsa.mimo.starshipsale.R;
import com.upsa.mimo.starshipsale.domain.entities.Product;

import java.text.NumberFormat;

class FeedItemViewHolder extends RecyclerView.ViewHolder {

    private FeedAdapter.OnProductClickListener productClickListener;
    private TextView name;
    private ImageView image;
    private TextView price;
    private ImageView addedToCart;
    private Product boundProduct;

    public FeedItemViewHolder(View itemView, FeedAdapter.OnProductClickListener productClickListener) {
        super(itemView);
        findViews(itemView);
        this.productClickListener = productClickListener;
        attachListeners(itemView);
    }

    private void findViews(View itemView) {
        name = (TextView) itemView.findViewById(R.id.product_name);
        image = (ImageView) itemView.findViewById(R.id.product_image);
        price = (TextView) itemView.findViewById(R.id.product_price);
        addedToCart = (ImageView) itemView.findViewById(R.id.product_cart);
    }

    private void attachListeners(View itemView) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productClickListener.onProductClick(boundProduct);
            }
        });

        addedToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productClickListener.onAddToCartClick(boundProduct);
            }
        });
    }

    public void bind(Product product) {
        boundProduct = product;
        renderProduct(boundProduct);
    }

    private void renderProduct(Product product) {
        name.setText(product.getName());
        String formattedText = null;
        try {
            final Double aDouble = Double.valueOf(product.getPrice());
            NumberFormat numberFormatter = NumberFormat.getInstance();
            numberFormatter.setMaximumFractionDigits(0);
            formattedText = numberFormatter.format(aDouble);
        } catch (NumberFormatException exception) {
            formattedText = product.getPrice();
        } finally {
            price.setText(formattedText);
        }
        addedToCart.setActivated(product.isAddedToCart());
        Picasso.with(image.getContext()).
                load(product.getImage())
                .fit()
                .into(image);
    }
}
