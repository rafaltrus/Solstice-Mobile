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
import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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

            //TODO: cannot parse the image on main thread! use asynctask
            URL myFileUrl;
            try {
                myFileUrl = new URL(mContact.smallImageURL);
                HttpURLConnection conn;
                try {
                    conn = (HttpURLConnection) myFileUrl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    ((ImageView) rootView.findViewById(R.id.image)).setImageBitmap(BitmapFactory.decodeStream(is));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }

        return rootView;
    }

    class ContactImage extends AsyncTask<String, Void, Bitmap> {


        @Override
        protected Bitmap doInBackground(String... strings) {
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
        }
    }
}
