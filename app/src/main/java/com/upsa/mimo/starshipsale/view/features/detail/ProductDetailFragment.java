package com.upsa.mimo.starshipsale.view.features.detail;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import com.squareup.picasso.Picasso;
import com.upsa.mimo.starshipsale.BuildConfig;
import com.upsa.mimo.starshipsale.R;
import com.upsa.mimo.starshipsale.api.product.ApiProductRepository;
import com.upsa.mimo.starshipsale.domain.entities.Product;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

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


        //testOpenReveal();
        //testCloseReveal();
        mExtendedFab.setVisibility(View.GONE);
        mFab.setVisibility(View.VISIBLE);

        mFab.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            public boolean mSet;

            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (!mSet) {
                    mSet = true;
                    mFab.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            final int[] fabXY = new int[2];
                            mFab.getLocationOnScreen(fabXY);

                            final int[] ctaXY = new int[2];

                            final Point extendedFabCenter = center(mExtendedFab);
                            final Point fabCenter = center(mFab);

                            mExtendedFab.getLocationOnScreen(ctaXY);
                            mFab.animate()
                                    .translationX(-(fabCenter.x - extendedFabCenter.x))
                                    .translationY(-(fabCenter.y - extendedFabCenter.y));

                        }
                    }, 1000);
                }
            }
        });



        /*final CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mFab.getLayoutParams();
        layoutParams.setBehavior(
        new CoordinatorLayout.Behavior<FloatingActionButton>() {

            @Override
            public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
                return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
            }

            @Override
            public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
                super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);


                NestedScrollView nestedScrollView = (NestedScrollView) target;
                final int scrollY = nestedScrollView.getScrollY();
                final int contentHeight = nestedScrollView.getChildAt(0).getHeight();


                if (reachedEnd(nestedScrollView, scrollY, contentHeight) && mExtendedFab.getVisibility() == View.GONE) {
                    animateToBottomBar(child, mExtendedFab);
                } else {
                    animateToFab(child, mExtendedFab);
                }
            }

            private boolean reachedEnd(NestedScrollView nestedScrollView, int scrollY, int contentHeight) {
                return scrollY == contentHeight - nestedScrollView.getHeight();
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            private void animateToBottomBar(final FloatingActionButton fab, final View bottomFabBar) {
//                final int[] fabXY = new int[2];
//                fab.getLocationOnScreen(fabXY);
//
//                final int[] ctaXY = new int[2];
//                cta.getLocationOnScreen(ctaXY);
//
//                final Animator circularReveal = ViewAnimationUtils.createCircularReveal(
//                        cta,
//                        cta.getWidth() / 2,
//                        cta.getHeight() / 2,
//                        fab.getWidth() / 2,
//                        cta.getWidth());
//
//                circularReveal
//                        .addListener(new AnimatorListenerAdapter() {
//
//                            @Override
//                            public void onAnimationStart(Animator animation) {
//                                cta.setVisibility(View.VISIBLE);
//                            }
//                        });
//                circularReveal.start();


//                fab.animate()
//                        .translationX(fabXY[0] - (ctaXY[0] / 2) + (fab.getWidth() / 2))
//                        .translationY(fabXY[1] - (ctaXY[1] / 2) + (fab.getHeight() / 2))
//                        .withEndAction(new Runnable() {
//                            @Override
//                            public void run() {
//
//                            }
//                        });


            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            private void animateToFab(final FloatingActionButton fab, final View bottomFabBar) {
//                final int[] fabXY = new int[2];
//                fab.getLocationOnScreen(fabXY);
//
//                final int[] ctaXY = new int[2];
//                cta.getLocationOnScreen(ctaXY);
//
//                final Animator circularReveal = ViewAnimationUtils.createCircularReveal(
//                        cta,
//                        cta.getWidth() / 2,
//                        cta.getHeight() / 2,
//                        cta.getWidth(),
//                        fab.getWidth() / 2);
//
//                circularReveal.addListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        cta.setVisibility(View.GONE);
//                    }
//                });
//
//                circularReveal.start();

//                fab.animate()
//                        .translationX(fabXY[0] - (ctaXY[0] / 2) + (fab.getWidth() / 2))
//                        .translationY(fabXY[1] - (ctaXY[1] / 2) + (fab.getHeight() / 2))
//                        .withEndAction(new Runnable() {
//                            @Override
//                            public void run() {
//                                circularReveal.start();
//                            }
//                        });


                }
            }
        });*/
    }

    private Point center(View view) {
        return new Point((int) view.getX() + (view.getWidth() / 2), (int) view.getY() + (view.getHeight() / 2));
    }

    private void testCloseReveal() {
        mExtendedFab.setVisibility(View.VISIBLE);
        mFab.setVisibility(View.GONE);

        mFab.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            public boolean mSet;

            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (!mSet) {
                    mSet = true;
                    mFab.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            final Animator animator = closeWithReveal(mExtendedFab, mFab);
                            animator.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    animation.setupEndValues();
                                }
                            });
                            animator.start();
                        }
                    }, 1000);
                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private Animator closeWithReveal(View mExtendedFab, FloatingActionButton mFab) {
        return ViewAnimationUtils.createCircularReveal(
                mExtendedFab,
                mExtendedFab.getWidth() / 2,
                mExtendedFab.getHeight() / 2,
                mExtendedFab.getWidth(),
                mFab.getWidth() / 2);
    }

    private void testOpenReveal() {
        mExtendedFab.setVisibility(View.VISIBLE);
        mFab.setVisibility(View.GONE);

        mFab.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            public boolean mSet;

            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (!mSet) {
                    mSet = true;
                    mFab.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            final Animator animator = openWithReveal(mExtendedFab, mFab);
                            animator.start();
                        }
                    }, 1000);
                }
        }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private Animator openWithReveal(View extendedFab, FloatingActionButton fab) {
        return ViewAnimationUtils.createCircularReveal(
                extendedFab,
                extendedFab.getWidth() / 2,
                extendedFab.getHeight() / 2,
                fab.getWidth() / 2,
                extendedFab.getWidth());
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

                mDeepLinkTrackingObject = initDeepLinkObject(product.getName(),product.getModel(),
                        BuildConfig.SERVER_REST_URL + "/p/" + product.getId());
                startDeepLinkingEvent(mDeepLinkTrackingObject);

            } else {
                // TODO empty view
            }
        }
    }

    private void renderProduct(Product product) {
        mName.setText(product.getName());
        mPrice.setText(product.getPrice());
        mModel.setText(String.format("Model\t %s", product.getModel()));
        mManufacturer.setText(String.format("Manufacturer\t %s", product.getManufacturer()));
        mClass.setText(String.format("Starship Class\t %s", product.getStarshipClass()));
        mPassengers.setText(String.format("Passengers\t %s", product.getPassengers()));
        mCrew.setText(String.format("Crew\t %s", product.getCrew()));
        mCargo.setText(String.format("Cargo capacity\t %s", product.getCargoCapacity()));
        mHyperdriveRating.setText(String.format("Hyperdrive rating\t %s", product.getHyperDriveRating()));

        Picasso.with(getActivity()).
                load(product.getImage())
                .fit()
                .into(mImage);
    }

}
