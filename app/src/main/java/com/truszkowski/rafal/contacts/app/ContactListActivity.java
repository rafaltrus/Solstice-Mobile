package com.truszkowski.rafal.contacts.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


/**
 * An activity representing a list of Contacts. The activity presents a list of items,
 * which when touched,lead to a {@link ContactDetailsActivity} representing
 * item details.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ContactListFragment} and the item details
 * (if present) is a {@link ContactDetailsFragment}.
 * <p/>
 * This activity also implements the required
 * {@link ContactListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class ContactListActivity extends FragmentActivity
        implements ContactListFragment.Callbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
    }

    /**
     * Callback method from {@link ContactListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {
        Intent detailIntent = new Intent(this, ContactDetailsActivity.class);
        detailIntent.putExtra(ContactDetailsFragment.CONTACT_NAME, id);
            startActivity(detailIntent);
    }
}