package com.upsa.mimo.starshipsale.view.features.feed;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.upsa.mimo.starshipsale.BuildConfig;
import com.upsa.mimo.starshipsale.R;
import com.upsa.mimo.starshipsale.api.cart.ApiCartRepository;
import com.upsa.mimo.starshipsale.api.product.ApiProductRepository;
import com.upsa.mimo.starshipsale.api.session.SessionRepository;
import com.upsa.mimo.starshipsale.domain.entities.Product;
import com.upsa.mimo.starshipsale.view.features.detail.ProductDetailActivity;

import java.io.IOException;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class FeedFragment extends Fragment {
    public static final int SPAN_COUNT = 2;
    private RecyclerView recyclerView;
    private FeedAdapter adapter;
    private View emptyView;
    private ProgressBar progressBar;
    private CoordinatorLayout coordinatorLayout;

    public static FeedFragment newInstance() {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public FeedFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        progressBar = (ProgressBar) view.findViewById(R.id.pb_loading);
        emptyView = view.findViewById(R.id.empty_view);
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinator_layout);
        final GridLayoutManager layoutManager = new GridLayoutManager(
                getActivity(),
                SPAN_COUNT,
                StaggeredGridLayoutManager.VERTICAL,
                false);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter.getItemViewType(position) == FeedAdapter.TYPE_FEATURED) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });

        recyclerView.setLayoutManager(layoutManager);

        adapter = new FeedAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setProductClickListener(new FeedAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(Product product) {
                ProductDetailActivity.launch(getActivity(), product);
            }

            @Override
            public void onAddToCartClick(Product product) {
                new CartAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, product);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchProducts();
    }

    public void fetchProducts() {
        new FeedAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private class FeedAsyncTask extends AsyncTask<Void, Void, List<Product>> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        }

        @Override
        protected List<Product> doInBackground(Void... params) {
            try {
                final SessionRepository sessionRepository = new SessionRepository(getActivity(), BuildConfig.SERVER_REST_URL);
                return new ApiProductRepository(BuildConfig.SERVER_REST_URL, sessionRepository.getCurrentSession()).getAll();
            } catch (IOException e) {
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
                } else {
                    progressBar.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    final TextView emptyViewTitle = (TextView) emptyView.findViewById(R.id.empty_view_title);
                    emptyViewTitle.setText(R.string.feed_empty_results);
                    final Button emptyViewCTA = (Button) emptyView.findViewById(R.id.empty_view_cta);
                    emptyViewCTA.setText(R.string.generic_retry);
                    emptyViewCTA.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fetchProducts();
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
                        fetchProducts();
                    }
                });
            }
        }
    }

    private class CartAsyncTask extends AsyncTask<Product, Void, Product> {

        @Override
        protected Product doInBackground(Product... params) {
            final SessionRepository sessionRepository = new SessionRepository(getActivity(), BuildConfig.SERVER_REST_URL);
            final ApiCartRepository apiCartRepository = new ApiCartRepository(BuildConfig.SERVER_REST_URL, sessionRepository.getCurrentSession());
            Product product = params[0];
            try {
                if (!product.isAddedToCart()) {
                    return apiCartRepository.addToCart(product.getId());
                } else {
                    return apiCartRepository.removeFromCart(product.getId());
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Product product) {
            super.onPostExecute(product);
            if (product != null) {
                adapter.update(product);
            } else {
                Snackbar.make(coordinatorLayout, "Could not add to cart", Snackbar.LENGTH_SHORT).show();
            }
        }
    }
}
