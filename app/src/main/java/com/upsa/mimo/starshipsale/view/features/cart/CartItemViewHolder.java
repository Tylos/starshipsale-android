package com.upsa.mimo.starshipsale.view.features.cart;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.upsa.mimo.starshipsale.R;
import com.upsa.mimo.starshipsale.domain.entities.Product;

public class CartItemViewHolder  extends RecyclerView.ViewHolder {
    private TextView mName;
    private ImageView mImage;
    private TextView mPrice;

    public CartItemViewHolder(View layoutView) {
        super(layoutView);
        mName = (TextView) layoutView.findViewById(R.id.product_name);
        mImage = (ImageView) layoutView.findViewById(R.id.product_image);
        mPrice = (TextView) layoutView.findViewById(R.id.product_price);
    }

    public void bind(Product product) {
        renderProduct(product);
    }

    private void renderProduct(Product product) {
        mName.setText(product.getName());
        mPrice.setText(product.getPrice() + " €");
    }
}
