package com.upsa.mimo.starshipsale.view.features.cart;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.upsa.mimo.starshipsale.BuildConfig;
import com.upsa.mimo.starshipsale.R;
import com.upsa.mimo.starshipsale.api.cart.ApiCartRepository;
import com.upsa.mimo.starshipsale.api.session.SessionRepository;
import com.upsa.mimo.starshipsale.domain.entities.Product;
import com.upsa.mimo.starshipsale.view.MainActivity;

import java.io.IOException;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class CartFragment extends Fragment {
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private FrameLayout cta;
    private View emptyView;
    private ProgressBar progressBar;
    private CoordinatorLayout coordinatorLayout;
    private TextView ctaText;

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
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        cta = (FrameLayout) view.findViewById(R.id.cta);
        ctaText = (TextView) view.findViewById(R.id.cta_text);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(
                getActivity(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);


        adapter = new CartAdapter();
        recyclerView.setAdapter(adapter);

        progressBar = (ProgressBar) view.findViewById(R.id.pb_loading);
        emptyView = view.findViewById(R.id.empty_view);
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinator_layout);

        cta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Purchase fragment plx
            }
        });
        cta.setVisibility(View.GONE);
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
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        }

        @Override
        protected List<Product> doInBackground(Void... params) {
            try {
                SessionRepository sessionRepository = new SessionRepository(getActivity(), BuildConfig.SERVER_REST_URL);
                return new ApiCartRepository(BuildConfig.SERVER_REST_URL, sessionRepository.getCurrentSession()).getAll();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Product> products) {
            if (products != null) {
                if (!products.isEmpty()) {
                    progressBar.setVisibility(View.GONE);
                    emptyView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter.setProducts(products);
                    adapter.notifyDataSetChanged();
                    long total = 0;
                    for (Product product : products) {
                        try {
                            total += Long.valueOf(product.getPrice());
                        } catch (NumberFormatException e) {
                            // Skip to next
                        }

                        if (total > 0) {
                            ctaText.setText(String.format("Purchase - %d", total));
                            cta.setVisibility(View.VISIBLE);
                            cta.setVisibility(View.VISIBLE);
                            cta.setTranslationY(cta.getHeight());
                            cta.animate()
                                    .translationY(0)
                                    .setDuration(100)
                                    .start();

                        } else {
                            cta.setVisibility(View.GONE);
                        }
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    final TextView emptyViewTitle = (TextView) emptyView.findViewById(R.id.empty_view_title);
                    emptyViewTitle.setText(R.string.cart_empty_title);
                    final Button emptyViewCTA = (Button) emptyView.findViewById(R.id.empty_view_cta);
                    emptyViewCTA.setText(R.string.cart_empty_cta);
                    emptyViewCTA.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((MainActivity) getActivity()).openFeed();
                        }
                    });
                }
            } else {
                progressBar.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                final TextView emptyViewTitle = (TextView) emptyView.findViewById(R.id.empty_view_title);
                emptyViewTitle.setText(R.string.generic_error);
                final Button emptyViewCTA = (Button) emptyView.findViewById(R.id.empty_view_cta);
                emptyViewCTA.setText(R.string.generic_retry);
                emptyViewCTA.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fetchCart();
                    }
                });
            }
        }
    }
}
