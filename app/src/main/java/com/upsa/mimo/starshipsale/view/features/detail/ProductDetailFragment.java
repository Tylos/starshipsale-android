package com.upsa.mimo.starshipsale.view.features.detail;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.upsa.mimo.starshipsale.BuildConfig;
import com.upsa.mimo.starshipsale.R;
import com.upsa.mimo.starshipsale.api.product.ApiProductRepository;
import com.upsa.mimo.starshipsale.domain.entities.Product;

import java.io.IOException;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProductDetailFragment extends Fragment {

    private static final String ARGUMENTS_PRODUCT_ID = "arguments.product_id";

    private CoordinatorLayout mCoordinatorLayout;
    private FloatingActionButton mFab;
    private View mExtendedFab;
    private TextView mName;
    private ImageView mImage;
    private TextView mPrice;
    private TextView mDescription;
    private TextView mModel;
    private TextView mManufacturer;
    private TextView mClass;
    private TextView mPassengers;
    private TextView mCrew;
    private TextView mCargo;
    private TextView mHyperdriveRating;

    public ProductDetailFragment() {
    }

    public static android.app.Fragment newInstance(String productId) {
        Fragment fragment = new ProductDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENTS_PRODUCT_ID, productId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_detail, container, false);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCoordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinator_layout);
        mFab = (FloatingActionButton) view.findViewById(R.id.fab);
        mExtendedFab = view.findViewById(R.id.extended_fab);

        mName = (TextView) view.findViewById(R.id.product_name);
        mImage = (ImageView) view.findViewById(R.id.product_image);
        mPrice = (TextView) view.findViewById(R.id.product_price);
        mDescription = (TextView) view.findViewById(R.id.product_description);
        mModel = (TextView) view.findViewById(R.id.product_model);
        mManufacturer = (TextView) view.findViewById(R.id.product_manufacturer);
        mClass = (TextView) view.findViewById(R.id.product_class);
        mPassengers = (TextView) view.findViewById(R.id.product_passengers);
        mCrew = (TextView) view.findViewById(R.id.product_crew);
        mCargo = (TextView) view.findViewById(R.id.product_cargo);
        mHyperdriveRating = (TextView) view.findViewById(R.id.product_hyperdrive_rating);
    }


    @Override
    public void onStart() {
        super.onStart();
        new DetailAsyncTask().executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR,
                getArguments().getString(ARGUMENTS_PRODUCT_ID));
    }

    private class DetailAsyncTask extends AsyncTask<String, Void, Product> {

        @Override
        protected Product doInBackground(String... params) {
            try {
                return new ApiProductRepository(BuildConfig.SERVER_REST_URL).getById(params[0]);
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Product product) {
            if (product != null) {
                renderProduct(product);
            } else {
                // TODO empty view
            }
        }
    }

    private void renderProduct(Product product) {
        mName.setText(product.getName());
        mPrice.setText(product.getPrice());
        mModel.setText(product.getModel());
        mManufacturer.setText(product.getManufacturer());
        mClass.setText(product.getStarshipClass());
        mPassengers.setText(product.getPassengers());
        mCrew.setText(product.getCrew());
        mCargo.setText(product.getCargoCapacity());
        mHyperdriveRating.setText(product.getHyperDriveRating());

        Picasso.with(getActivity()).
                load(product.getImage())
                .fit()
                .into(mImage);
    }

}
