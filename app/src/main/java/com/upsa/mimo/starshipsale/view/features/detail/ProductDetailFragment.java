package com.upsa.mimo.starshipsale.view.features.detail;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

import com.upsa.mimo.starshipsale.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProductDetailFragment extends Fragment {

    private CoordinatorLayout mCoordinatorLayout;
    private FloatingActionButton mFab;
    private View mExtendedFab;

    public ProductDetailFragment() {
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


}
