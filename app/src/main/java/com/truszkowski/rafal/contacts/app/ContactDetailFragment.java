package com.truszkowski.rafal.contacts.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * A fragment representing a single Contact detail screen.
 * This fragment is either contained in a {@link ContactListActivity}
 * in two-pane mode (on tablets) or a {@link ContactDetailActivity}
 * on handsets.
 */
public class ContactDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String CONTACT_NAME = "item_id";


    /**
     * The dummy content this fragment is presenting.
     */
    private ContactList.Contact mContact;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ContactDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(CONTACT_NAME)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mContact = ContactList.MAP.get(getArguments().getString(CONTACT_NAME));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact_detail, container, false);
        // Show the dummy content as text in a TextView.
        if (mContact != null) {
            new GetContactImage().execute();
            new GetMoreContactDetails().execute(mContact.detailsURL);
            ((TextView) rootView.findViewById(R.id.name)).setText(mContact.name);
            ((TextView) rootView.findViewById(R.id.company)).setText(mContact.company);
            try {
                ((TextView) rootView.findViewById(R.id.workPhone)).setText(mContact.phone.getString("work"));
                ((TextView) rootView.findViewById(R.id.homePhone)).setText(mContact.phone.getString("home"));
                ((TextView) rootView.findViewById(R.id.mobilePhone)).setText(mContact.phone.getString("mobile"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return rootView;
    }

    class GetContactImage extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            URL myFileUrl;
            try {
                myFileUrl = new URL(mContact.smallImageURL);
                HttpURLConnection conn;
                try {
                    conn = (HttpURLConnection) myFileUrl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    return BitmapFactory.decodeStream(is);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                ((ImageView) getActivity().findViewById(R.id.image)).setImageBitmap(bitmap);
            }
        }
    }

    class GetMoreContactDetails extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {
            // AndroidHttpClient class is deprecated in API level 22. It is advised to use URLConnection instead.
            // ref: http://developer.android.com/reference/android/net/http/AndroidHttpClient.html
            URL url = null;
            try {
                url = new URL(strings[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            URLConnection urlConnection;
            InputStream in = null;
            try {
                assert url != null;
                urlConnection = url.openConnection();
                in = new BufferedInputStream(urlConnection.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert in != null;
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder();
            String line;
            try {
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                return new JSONObject(total.toString());
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject jsonContactDetails) {
            super.onPostExecute(jsonContactDetails);
        }
    }
}
