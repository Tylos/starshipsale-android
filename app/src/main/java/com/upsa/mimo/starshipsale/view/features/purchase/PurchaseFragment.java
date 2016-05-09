package com.upsa.mimo.starshipsale.view.features.purchase;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.upsa.mimo.starshipsale.BuildConfig;
import com.upsa.mimo.starshipsale.R;
import com.upsa.mimo.starshipsale.api.purchase.ApiPurchaseRepository;

import java.io.IOException;

public class PurchaseFragment extends Fragment {
    private static final String EXTRAS_ITEM_ID = "extras.item_id";
    private static final String EXTRAS_PURCHASE_TYPE = "extras.purchase_type";

    private Type type;
    private ViewSwitcher iconSwitcher;
    private TextView statusText;

    private enum Type {
        CART,
        ITEM
    }

    public static Fragment newInstanceForItem(String itemId) {
        PurchaseFragment fragment = new PurchaseFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRAS_PURCHASE_TYPE, Type.ITEM);
        args.putString(EXTRAS_ITEM_ID, itemId);
        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment newInstanceForCart() {
        PurchaseFragment fragment = new PurchaseFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRAS_PURCHASE_TYPE, Type.CART);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = (Type) getArguments().getSerializable(EXTRAS_PURCHASE_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_purchase, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iconSwitcher = (ViewSwitcher) view.findViewById(R.id.icon_switcher);
        statusText = (TextView) view.findViewById(R.id.status_text);
        attemptPurchase();
    }

    private void attemptPurchase() {
        new PurchaseAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, type);
    }

    private class PurchaseAsyncTask extends AsyncTask<Type, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            iconSwitcher.showNext();
            statusText.setText(getString(R.string.purchase_text));
        }

        @Override
        protected Boolean doInBackground(Type... params) {
            try {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Type type = params[0];
                final ApiPurchaseRepository apiPurchaseRepository =
                        new ApiPurchaseRepository(getActivity(), BuildConfig.SERVER_REST_URL);
                if (type.equals(Type.ITEM)) {
                    apiPurchaseRepository.purchase(getArguments().getString(EXTRAS_ITEM_ID));
                } else {
                    apiPurchaseRepository.purchaseCart();
                }
                return Boolean.TRUE;
            } catch (IOException e) {
                e.printStackTrace();
                return Boolean.FALSE;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                iconSwitcher.showPrevious();
                statusText.setText(getString(R.string.purchase_ok));
            } else {
                iconSwitcher.setVisibility(View.GONE);
                statusText.setText(getString(R.string.generic_error));
            }

            statusText.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getFragmentManager().popBackStack();
                }
            }, 1000);
        }
    }

}
