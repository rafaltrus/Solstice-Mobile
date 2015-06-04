package com.truszkowski.rafal.contacts.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A fragment representing a single Contact detail screen.
 * This fragment is contained in a {@link ContactDetailsActivity}.
 */
public class ContactDetailsFragment extends Fragment {
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
    public ContactDetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(CONTACT_NAME)) {
            mContact = ContactList.MAP.get(getArguments().getString(CONTACT_NAME));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact_detail, container, false);
        rootView.findViewById(R.id.contactDetails).setVisibility(View.GONE);

        // Fetch more contact details
        new GetContactImage().execute(mContact.smallImageURL);
        // Display the contact data previously fetched
        if (mContact != null) {
            new GetMoreContactDetails(rootView).execute(mContact.detailsURL);
            ((EditText) rootView.findViewById(R.id.name)).setText(mContact.name);
            ((EditText) rootView.findViewById(R.id.company)).setText(mContact.company);
            try {
                ((TextView) rootView.findViewById(R.id.workPhone)).setText(mContact.phone.getString("work"));
                ((TextView) rootView.findViewById(R.id.homePhone)).setText(mContact.phone.getString("home"));
                ((TextView) rootView.findViewById(R.id.mobilePhone)).setText(mContact.phone.getString("mobile"));
                ((TextView) rootView.findViewById(R.id.birthday)).setText(getDate(Integer.parseInt(mContact.birthDate), "MMMM dd, yyyy"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        rootView.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        return rootView;
    }

    /**
     * Return date in specified format.
     *
     * @param milliSeconds Date in milliseconds
     * @param dateFormat   Date format
     * @return String representing date in specified format
     */
    private String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    /**
     * This asynchronous task downloads and sets the profile picture for a current contact.
     */
    class GetContactImage extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            // AndroidHttpClient class is deprecated in API level 22. It is advised to use URLConnection instead.
            // ref: http://developer.android.com/reference/android/net/http/AndroidHttpClient.html
            URL myFileUrl;
            try {
                myFileUrl = new URL(strings[0]);
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

    /**
     * This asynchronous task downloads and sets the remaining contact details for a current contact.
     */
    class GetMoreContactDetails extends AsyncTask<String, Void, JSONObject> {

        private View rootView;

        public GetMoreContactDetails(View rootView) {
            this.rootView = rootView;
        }

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
            try {
                ((TextView) getActivity().findViewById(R.id.email)).setText(jsonContactDetails.getString("email"));
                JSONObject jsonAddress = jsonContactDetails.getJSONObject("address");
                ((TextView) getActivity().findViewById(R.id.street)).setText(jsonAddress.getString("street"));
                ((TextView) getActivity().findViewById(R.id.city_state_and_zipcode)).setText(jsonAddress.getString("city") + ", " + jsonAddress.getString("city") + " " + jsonAddress.getString("zip"));
                ((TextView) getActivity().findViewById(R.id.country)).setText(jsonAddress.getString("country"));

                ((ContactDetailsActivity) getActivity()).menu.findItem(R.id.favorite).setIcon(getAppropriateStarIcon(jsonContactDetails));
                rootView.findViewById(R.id.progressBar).setVisibility(View.GONE);
                ((ContactDetailsActivity) getActivity()).getSupportActionBar().show();
                rootView.findViewById(R.id.contactDetails).setVisibility(View.VISIBLE);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private Drawable getAppropriateStarIcon(JSONObject jsonContactDetails) throws JSONException {
            // Resources.getDrawable(int) is deprecated in API 22. The most reliable way to obtain drawables is now Context.getDrawable(int).
            Drawable icon;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
                if (jsonContactDetails.getBoolean("favorite")) {
                    icon = getResources().getDrawable(R.mipmap.ic_star_pressed);
                    ((ContactDetailsActivity) getActivity()).isStarMenuChecked = true;
                } else {
                    icon = getResources().getDrawable(R.mipmap.ic_star_normal);
                    ((ContactDetailsActivity) getActivity()).isStarMenuChecked = false;
                }
            } else {
                if (jsonContactDetails.getBoolean("favorite")) {
                    icon = getActivity().getDrawable(R.mipmap.ic_star_pressed);
                    ((ContactDetailsActivity) getActivity()).isStarMenuChecked = true;
                } else {
                    icon = getActivity().getDrawable(R.mipmap.ic_star_normal);
                    ((ContactDetailsActivity) getActivity()).isStarMenuChecked = false;
                }
            }
            return icon;
        }

    }
}
