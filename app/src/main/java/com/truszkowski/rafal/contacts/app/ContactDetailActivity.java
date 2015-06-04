package com.truszkowski.rafal.contacts.app;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


/**
 * An activity representing a single Contact detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ContactListActivity}.
 * <p/>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link ContactDetailFragment}.
 */
public class ContactDetailActivity extends ActionBarActivity {

    public Menu menu;
    private boolean isStarMenuChecked = false;
    private boolean isCurrentlyEdittingDetails = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(ContactDetailFragment.CONTACT_NAME,
                    getIntent().getStringExtra(ContactDetailFragment.CONTACT_NAME));
            ContactDetailFragment fragment = new ContactDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contact_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, ContactListActivity.class));
            return true;
        } else if (id == R.id.favorite) {
            isStarMenuChecked = !item.isChecked();
            // Resources.getDrawable(int) is deprecated in API 22. The most reliable way to obtain drawables is now Context.getDrawable(int).
            Drawable icon;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
                if (isStarMenuChecked) {
                    icon = getResources().getDrawable(R.mipmap.ic_star_pressed);
                } else {
                    icon = getResources().getDrawable(R.mipmap.ic_star_normal);
                }
            } else {
                if (isStarMenuChecked) {
                    icon = this.getDrawable(R.mipmap.ic_star_pressed);
                } else {
                    icon = this.getDrawable(R.mipmap.ic_star_normal);
                }
            }

            item.setIcon(icon);

            item.setChecked(isStarMenuChecked);
            return true;
        } else if (id == R.id.edit) {
            isCurrentlyEdittingDetails = !item.isChecked();
            Drawable icon;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
                if (isCurrentlyEdittingDetails) {
                    icon = getResources().getDrawable(R.mipmap.ic_save);
                    enableEditTextFields(true);
                    Toast.makeText(getApplicationContext(), "After you edit the information, click the check mark to save.",
                            Toast.LENGTH_LONG).show();
                } else {
                    icon = getResources().getDrawable(R.mipmap.ic_menu_edit);
                    enableEditTextFields(false);
                }
            } else {
                if (isCurrentlyEdittingDetails) {
                    icon = this.getDrawable(R.mipmap.ic_save);
                    enableEditTextFields(true);
                    Toast.makeText(getApplicationContext(), "After you edit the information, click the check mark to save.",
                            Toast.LENGTH_LONG).show();
                } else {
                    icon = this.getDrawable(R.mipmap.ic_menu_edit);
                    enableEditTextFields(false);
                }
            }

            item.setIcon(icon);
            item.setChecked(isCurrentlyEdittingDetails);
        }

        return super.onOptionsItemSelected(item);

    }

    private void enableEditTextFields(boolean enableFields) {
        findViewById(R.id.name).setEnabled(enableFields);
        findViewById(R.id.company).setEnabled(enableFields);
        findViewById(R.id.homePhone).setEnabled(enableFields);
        findViewById(R.id.workPhone).setEnabled(enableFields);
        findViewById(R.id.mobilePhone).setEnabled(enableFields);
        findViewById(R.id.street).setEnabled(enableFields);
        findViewById(R.id.city_state_and_zipcode).setEnabled(enableFields);
        findViewById(R.id.country).setEnabled(enableFields);
        findViewById(R.id.email).setEnabled(enableFields);
        findViewById(R.id.birthday).setEnabled(enableFields);
        if (enableFields) {
            findViewById(R.id.name).setBackgroundColor(Color.parseColor("#D6D6D6"));
            findViewById(R.id.company).setBackgroundColor(Color.parseColor("#D6D6D6"));
            findViewById(R.id.homePhone).setBackgroundColor(Color.parseColor("#D6D6D6"));
            findViewById(R.id.workPhone).setBackgroundColor(Color.parseColor("#D6D6D6"));
            findViewById(R.id.mobilePhone).setBackgroundColor(Color.parseColor("#D6D6D6"));
            findViewById(R.id.street).setBackgroundColor(Color.parseColor("#D6D6D6"));
            findViewById(R.id.city_state_and_zipcode).setBackgroundColor(Color.parseColor("#D6D6D6"));
            findViewById(R.id.country).setBackgroundColor(Color.parseColor("#D6D6D6"));
            findViewById(R.id.email).setBackgroundColor(Color.parseColor("#D6D6D6"));
            findViewById(R.id.birthday).setBackgroundColor(Color.parseColor("#D6D6D6"));
        } else {
            findViewById(R.id.name).setBackgroundColor(0);
            findViewById(R.id.company).setBackgroundColor(0);
            findViewById(R.id.homePhone).setBackgroundColor(0);
            findViewById(R.id.workPhone).setBackgroundColor(0);
            findViewById(R.id.mobilePhone).setBackgroundColor(0);
            findViewById(R.id.street).setBackgroundColor(0);
            findViewById(R.id.city_state_and_zipcode).setBackgroundColor(0);
            findViewById(R.id.country).setBackgroundColor(0);
            findViewById(R.id.email).setBackgroundColor(0);
            findViewById(R.id.birthday).setBackgroundColor(0);
        }
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem checkable = menu.findItem(R.id.favorite);
        checkable.setChecked(isStarMenuChecked);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        this.menu = menu;
        inflater.inflate(R.menu.contact_details_menu, menu);
        return true;
    }
}
