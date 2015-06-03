package com.truszkowski.rafal.contacts.app;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * A list fragment representing a list of Contacts. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link ContactDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class ContactListFragment extends ListFragment {

    public static final String SOLSTICE_CONTACTS_JSON_URL = "https://solstice.applauncher.com/external/contacts.json";

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        void onItemSelected(String id);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String id) {
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ContactListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // clear the list of contacts every time the fragment is created and refetch the data from server.
        ContactList.LIST.clear();
        ContactList.MAP.clear();
        new GetContacts().execute();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        mCallbacks.onItemSelected(ContactList.LIST.get(position).name);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }


    class GetContacts extends AsyncTask<Void, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(Void... voids) {
            // AndroidHttpClient class is deprecated in API level 22. It is advised to use URLConnection instead.
            // ref: http://developer.android.com/reference/android/net/http/AndroidHttpClient.html
            URL url = null;
            try {
                url = new URL(SOLSTICE_CONTACTS_JSON_URL);
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
                return new JSONArray(total.toString());
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONArray contactsJSON) {
            super.onPostExecute(contactsJSON);

            JSONArray jArray = null;
            try {
                jArray = new JSONArray(contactsJSON.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // get each JSONObject and store it in the array as each contact
            assert jArray != null;
            int length = jArray.length();
            for (int i = 0; i < length; i++) {
                try {
                    JSONObject jsonContact = jArray.getJSONObject(i);
                    String name = jsonContact.getString("name");
                    String employeeId = jsonContact.getString("employeeId");
                    String company = jsonContact.getString("company");
                    String detailsURL = jsonContact.getString("detailsURL");
                    String smallImageURL = jsonContact.getString("smallImageURL");
                    String birthDate = jsonContact.getString("birthdate");
                    JSONObject phone = jsonContact.getJSONObject("phone");
                    ContactList.Contact contact = new ContactList.Contact(name, employeeId, company, detailsURL, smallImageURL, birthDate, phone);
                    Log.d("TAG", "contact number " + (i + 1));
                    ContactList.LIST.add(contact);
                    ContactList.MAP.put(name, contact);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            setListAdapter(new ArrayAdapter<ContactList.Contact>(
                    getActivity(),
                    android.R.layout.simple_list_item_activated_1,
                    android.R.id.text1,
                    ContactList.LIST));
        }
    }
}
