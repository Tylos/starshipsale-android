package com.upsa.mimo.starshipsale.view;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.upsa.mimo.starshipsale.R;
import com.upsa.mimo.starshipsale.view.features.cart.CartFragment;
import com.upsa.mimo.starshipsale.view.features.feed.FeedFragment;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int ADD_ACCOUNT_REQUEST_CODE = 42;
    private CharSequence mTitle;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    public static void launch(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureView();
        openFeed();
        requireLogin();
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

    private void requireLogin() {
        final AccountManager accountManager = AccountManager.get(getApplicationContext());
        final Account[] starshipSaleAccounts = accountManager.getAccountsByType(getString(R.string.account_type));
        if (starshipSaleAccounts.length > 0) {
            setCurrentAccount(starshipSaleAccounts[0]);
        } else {
            accountManager.addAccount(
                    getString(R.string.account_type),
                    null,
                    null,
                    null,
                    this,
                    new AccountManagerCallback<Bundle>() {
                        @Override
                        public void run(AccountManagerFuture<Bundle> future) {
                            try {
                                final Bundle result = future.getResult();
                                if (isSuccess(result)) {
                                    final String accountName = result.getString(AccountManager.KEY_ACCOUNT_NAME);
                                    Toast.makeText(MainActivity.this,
                                            "Add account: " + accountName,
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    final String errorMessage = result.getString(AccountManager.KEY_ERROR_MESSAGE);
                                    Toast.makeText(MainActivity.this,
                                            "Error: " + errorMessage,
                                            Toast.LENGTH_LONG).show();
                                }
                            } catch (OperationCanceledException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (AuthenticatorException e) {
                                e.printStackTrace();
                            }
                        }

                        private boolean isSuccess(Bundle result) {
                            return result.containsKey(AccountManager.KEY_ACCOUNT_NAME)
                                    && result.containsKey(AccountManager.KEY_ACCOUNT_TYPE);
                        }
                    },
                    null
            );

        }
    }

    private void setCurrentAccount(Account currentAccount) {
        Toast.makeText(MainActivity.this,
                "Log in as: " + currentAccount.name,
                Toast.LENGTH_LONG).show();
    }

    private void configureView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(new DrawerArrowDrawable(this));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();

                FragmentManager fragmentManager = getSupportFragmentManager();
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
    }

    public void openFeed() {
        navigationView.getMenu().performIdentifierAction(R.id.drawer_feed, 0);
    }
}
