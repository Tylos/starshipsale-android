package com.upsa.mimo.starshipsale.view.features.detail;

import android.annotation.TargetApi;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;
import com.upsa.mimo.starshipsale.BuildConfig;
import com.upsa.mimo.starshipsale.R;
import com.upsa.mimo.starshipsale.api.product.ApiProductRepository;
import com.upsa.mimo.starshipsale.api.session.SessionRepository;
import com.upsa.mimo.starshipsale.domain.entities.Product;
import com.upsa.mimo.starshipsale.view.features.purchase.PurchaseFragment;

import java.io.IOException;
import java.text.NumberFormat;

public class ProductDetailFragment extends Fragment {

    private static final String ARGUMENTS_PRODUCT_ID = "arguments.product_id";

    private CoordinatorLayout coordinatorLayout;
    private FloatingActionButton fab;
    private TextView name;
    private ImageView image;
    private TextView price;
    private TextView description;
    private TextView model;
    private TextView manufacturer;
    private TextView shipClass;
    private TextView passengers;
    private TextView crew;
    private TextView cargo;
    private TextView hyperdriveRating;

    private GoogleApiClient mClient;
    private Thing mDeepLinkTrackingObject;

    public ProductDetailFragment() {
    }

    public static Fragment newInstance(String productId) {
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mClient = new GoogleApiClient.Builder(getContext()).addApi(AppIndex.API).build();
    }
    
    @Override
    public void onStart() {
        super.onStart();
        new DetailAsyncTask().executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR,
                getArguments().getString(ARGUMENTS_PRODUCT_ID));
    }

    @Override
    public void onStop() {
        if(mDeepLinkTrackingObject != null) {
            stopDeepLinkingEvent(mDeepLinkTrackingObject);
        }
        super.onStop();
    }

    private void startDeepLinkingEvent(Thing object){
        mClient.connect();
        AppIndex.AppIndexApi.start(mClient, getDeepLinkAction(object));
    }

    private void stopDeepLinkingEvent(Thing object){
        AppIndex.AppIndexApi.end(mClient, getDeepLinkAction(object));
        mClient.disconnect();
    }

    private Thing initDeepLinkObject(String title, String description, String url){
        return new Thing.Builder()
                .setName(title)
                .setDescription(description)
                .setUrl(Uri.parse(url))
                .build();
    }

    public Action getDeepLinkAction(Thing object) {
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinator_layout);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        name = (TextView) view.findViewById(R.id.product_name);
        image = (ImageView) view.findViewById(R.id.product_image);
        price = (TextView) view.findViewById(R.id.product_price);
        description = (TextView) view.findViewById(R.id.product_description);
        model = (TextView) view.findViewById(R.id.product_model);
        manufacturer = (TextView) view.findViewById(R.id.product_manufacturer);
        shipClass = (TextView) view.findViewById(R.id.product_class);
        passengers = (TextView) view.findViewById(R.id.product_passengers);
        crew = (TextView) view.findViewById(R.id.product_crew);
        cargo = (TextView) view.findViewById(R.id.product_cargo);
        hyperdriveRating = (TextView) view.findViewById(R.id.product_hyperdrive_rating);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragment_container, PurchaseFragment.newInstanceForCart())
                        .addToBackStack("purchase_cart")
                        .commit();
            }
        });
    }

    private class DetailAsyncTask extends AsyncTask<String, Void, Product> {

        @Override
        protected Product doInBackground(String... params) {
            try {
                final SessionRepository sessionRepository = new SessionRepository(getActivity(), BuildConfig.SERVER_REST_URL);
                return new ApiProductRepository(BuildConfig.SERVER_REST_URL, sessionRepository.getCurrentSession()).getById(params[0]);
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Product product) {
            if (product != null) {
                renderProduct(product);

                mDeepLinkTrackingObject = initDeepLinkObject(product.getName(),product.getModel(),
                        BuildConfig.SERVER_REST_URL + "/p/" + product.getId());
                startDeepLinkingEvent(mDeepLinkTrackingObject);

            } else {
                Snackbar.make(coordinatorLayout, R.string.generic_error, Snackbar.LENGTH_SHORT)
                        .setCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar snackbar, int event) {
                                super.onDismissed(snackbar, event);
                                getActivity().finish();
                            }
                        })
                        .show();
            }
        }
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
        model.setText(product.getModel());
        manufacturer.setText(product.getManufacturer());
        shipClass.setText(product.getStarshipClass());
        passengers.setText(product.getPassengers());
        crew.setText(product.getCrew());
        cargo.setText(product.getCargoCapacity());
        hyperdriveRating.setText(product.getHyperDriveRating());

        Picasso.with(getActivity()).
                load(product.getImage())
                .fit()
                .into(image);
    }

}
