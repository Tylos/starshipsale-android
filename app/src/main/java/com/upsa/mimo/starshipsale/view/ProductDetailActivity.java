package com.upsa.mimo.starshipsale.view;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.upsa.mimo.starshipsale.R;
import com.upsa.mimo.starshipsale.domain.entities.Product;
import com.upsa.mimo.starshipsale.view.features.detail.ProductDetailFragment;

public class ProductDetailActivity extends AppCompatActivity {

    private static final String EXTRAS_PRODUCT_ID = "extras.product_id";

    public static void launch(Context context, Product product) {
        Intent intent = new Intent(context, ProductDetailActivity.class);
        intent.putExtra(EXTRAS_PRODUCT_ID, product.getId());
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final long productId = getIntent().getExtras().getLong(EXTRAS_PRODUCT_ID);
        Fragment fragment = ProductDetailFragment.newInstance(productId);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment, "fragment_tag.product_detail")
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
