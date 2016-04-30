package com.upsa.mimo.starshipsale.view.features.login;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ViewSwitcher;

import com.upsa.mimo.starshipsale.R;
import com.upsa.mimo.starshipsale.api.session.SessionRepository;
import com.upsa.mimo.starshipsale.domain.entities.Session;
import com.upsa.mimo.starshipsale.view.MainActivity;

import java.io.IOException;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginActivityFragment extends Fragment {

    private EditText passwordInput;
    private EditText emailInput;
    private View view;
    private ViewSwitcher viewSwitcher;

    public LoginActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.view = view;
        super.onViewCreated(view, savedInstanceState);
        emailInput = (EditText) view.findViewById(R.id.email_input);
        passwordInput = (EditText) view.findViewById(R.id.password_input);
        viewSwitcher = (ViewSwitcher) view.findViewById(R.id.cta_view_switcher);

        view.findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoginAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                        new Pair<>(
                                emailInput.getText().toString(),
                                passwordInput.getText().toString()
                        ));
            }
        });
    }

    private class LoginAsyncTask extends AsyncTask<Pair<String, String>, Void, Session> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            viewSwitcher.showNext();
        }

        @Override
        protected Session doInBackground(Pair<String, String>... params) {
            try {
                return new SessionRepository("http://startshipsale.herokuapp.com/api/")
                        .login(
                                params[0].first,
                                params[0].second);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Session session) {
            super.onPostExecute(session);
            if (session != null) {
                MainActivity.launch(getActivity());
                getActivity().finish();
            } else {
                Snackbar.make(view, R.string.generic_error, Snackbar.LENGTH_SHORT).show();
            }
            viewSwitcher.showPrevious();
        }
    }
}
