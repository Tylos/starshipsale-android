package com.upsa.mimo.starshipsale.view.features.cart;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.upsa.mimo.starshipsale.R;
import com.upsa.mimo.starshipsale.domain.entities.Product;

import java.text.NumberFormat;

public class CartItemViewHolder  extends RecyclerView.ViewHolder {
    private TextView name;
    private ImageView image;
    private TextView price;

    public CartItemViewHolder(View layoutView) {
        super(layoutView);
        name = (TextView) layoutView.findViewById(R.id.product_name);
        image = (ImageView) layoutView.findViewById(R.id.product_image);
        price = (TextView) layoutView.findViewById(R.id.product_price);
    }

    public void bind(Product product) {
        renderProduct(product);
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
        Picasso.with(image.getContext()).
                load(product.getImage())
                .fit()
                .into(image);
    }
}