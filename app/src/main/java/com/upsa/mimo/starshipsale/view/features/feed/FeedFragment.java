package com.upsa.mimo.starshipsale.view.features.feed;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.upsa.mimo.starshipsale.R;
import com.upsa.mimo.starshipsale.api.cart.ApiCartRepository;
import com.upsa.mimo.starshipsale.api.product.ApiProductRepository;
import com.upsa.mimo.starshipsale.domain.entities.Product;
import com.upsa.mimo.starshipsale.view.MainActivity;
import com.upsa.mimo.starshipsale.view.features.detail.ProductDetailActivity;

import java.io.IOException;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class FeedFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    public static final int SPAN_COUNT = 2;
    private RecyclerView mRecyclerView;
    private ProductAdapter mAdapter;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FeedFragment newInstance(int sectionNumber) {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
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
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        final GridLayoutManager layoutManager = new GridLayoutManager(
                getActivity(),
                SPAN_COUNT,
                StaggeredGridLayoutManager.VERTICAL,
                false);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mAdapter.getItemViewType(position) == ProductAdapter.TYPE_FEATURED) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });

        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new ProductAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setProductClickListener(new ProductAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(Product product) {
                ProductDetailActivity.launch(getActivity(), product);
            }

            @Override
            public void onFavoriteClick(Product product) {
                // TODO Mark as addToCart
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    public void fetchProducts() {
        new FeedAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private class FeedAsyncTask extends AsyncTask<Void, Void, List<Product>> {

        @Override
        protected List<Product> doInBackground(Void... params) {
            try {
                return new ApiProductRepository("http://startshipsale.herokuapp.com/api/").getAll();
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Product> products) {
            if (products != null) {
                mAdapter.setProducts(products);
                mAdapter.notifyDataSetChanged();
            } else {
                // TODO empty view
            }
        }
    }

    private class CartAsyncTask extends AsyncTask<Product, Void, Product> {

        @Override
        protected Product doInBackground(Product... params) {
            final ApiCartRepository apiCartRepository = new ApiCartRepository("http://startshipsale.herokuapp.com/api/");
            Product product = params[0];
            try {
                if (!product.isFavorite()) {
                    return apiCartRepository.addToCart(product.getId());
                } else {
                    return apiCartRepository.removeFromCart(product.getId());
                }
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Product product) {
            super.onPostExecute(product);
            if (product != null) {
                mAdapter.update(product);
            } else {
                //TODO ERROR
            }
        }
    }
}
