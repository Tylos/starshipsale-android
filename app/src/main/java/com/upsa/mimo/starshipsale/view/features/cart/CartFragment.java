package com.upsa.mimo.starshipsale.view.features.cart;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.upsa.mimo.starshipsale.R;
import com.upsa.mimo.starshipsale.api.cart.ApiCartRepository;
import com.upsa.mimo.starshipsale.domain.entities.Product;

import java.io.IOException;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class CartFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private CartAdapter mAdapter;
    private TextView mCTA;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static CartFragment newInstance() {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public CartFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cart, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mCTA = (TextView) view.findViewById(R.id.extended_fab);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(
                getActivity(), LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new CartAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mCTA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Purchase fragment plx
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchCart();
    }

    public void fetchCart() {
        new CartAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private class CartAsyncTask extends AsyncTask<Void, Void, List<Product>> {

        @Override
        protected List<Product> doInBackground(Void... params) {
            try {
                return new ApiCartRepository("http://startshipsale.herokuapp.com/api/").getAll();
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Product> products) {
            if (products != null) {
                mAdapter.setProducts(products);
                mAdapter.notifyDataSetChanged();
                long total = 0;
                for (Product product : products) {
                    try {
                        total += Long.valueOf(product.getPrice());
                    } catch (NumberFormatException e) {
                        // Skip to next
                    }

                    if (total > 0) {
                        mCTA.setText(String.format("Purchase - %d", total));
                        mCTA.setVisibility(View.VISIBLE);
                    } else {
                        mCTA.setVisibility(View.GONE);
                    }
                }
            } else {
                // TODO empty view
            }
        }
    }
}
