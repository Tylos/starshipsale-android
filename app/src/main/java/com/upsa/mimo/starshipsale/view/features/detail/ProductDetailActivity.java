package com.upsa.mimo.starshipsale.view.features.detail;

import com.upsa.mimo.starshipsale.R;
import com.upsa.mimo.starshipsale.domain.entities.Product;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.upsa.mimo.starshipsale.R;
import com.upsa.mimo.starshipsale.domain.entities.Product;

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

        String productId = getDeepLinkingProductId(getIntent());
        if (productId == null
                && getIntent() != null
                && getIntent().getExtras() != null
                && getIntent().getExtras().containsKey(EXTRAS_PRODUCT_ID)) {
            productId = getIntent().getExtras().getString(EXTRAS_PRODUCT_ID);
        }

        if(productId == null) {
            finish();
        }

        Fragment fragment = ProductDetailFragment.newInstance(productId);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment, "fragment_tag.product_detail")
                .commit();
    }

    protected String getDeepLinkingProductId(Intent intent) {
        String action = intent.getAction();
        String dataString = intent.getDataString();
        String productId = null;
        if (Intent.ACTION_VIEW.equals(action) && dataString != null) {
             productId = dataString.substring(dataString.lastIndexOf("/") + 1);
        }
        return productId;
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
