package com.upsa.mimo.starshipsale.view;

import com.upsa.mimo.starshipsale.R;
import com.upsa.mimo.starshipsale.view.features.cart.CartFragment;
import com.upsa.mimo.starshipsale.view.features.feed.FeedFragment;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private CharSequence mTitle;
    private DrawerLayout drawerLayout;

    public static void launch(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(new DrawerArrowDrawable(this));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();

                FragmentManager fragmentManager = getFragmentManager();
                switch (menuItem.getItemId()) {
                    case R.id.drawer_feed:
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, FeedFragment.newInstance())
                                .commit();
                        return true;
                    case R.id.drawer_cart:
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, CartFragment.newInstance())
                                .commit();
                        return true;
                    default:
                        return false;
                }
            }
        });
        view.getMenu().performIdentifierAction(R.id.drawer_feed, 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
