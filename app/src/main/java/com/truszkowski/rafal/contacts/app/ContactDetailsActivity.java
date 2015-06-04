package com.truszkowski.rafal.contacts.app;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


/**
 * An activity representing a single Contact detail screen.
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link ContactDetailsFragment}.
 */
public class ContactDetailsActivity extends AppCompatActivity {

    public Menu menu;
    public boolean isStarMenuChecked = false;
    private boolean isCurrentlyEditingDetails = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(ContactDetailsFragment.CONTACT_NAME,
                    getIntent().getStringExtra(ContactDetailsFragment.CONTACT_NAME));
            ContactDetailsFragment fragment = new ContactDetailsFragment();
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
            // toggle the icon
            isStarMenuChecked = !isStarMenuChecked;
            item.setIcon(getAppropriateStarIcon());
            return true;
        } else if (id == R.id.edit) {
            // toggle the icon
            isCurrentlyEditingDetails = !isCurrentlyEditingDetails;
            item.setIcon(getAppropriateEditIcon());
        }
        return super.onOptionsItemSelected(item);

    }

    private Drawable getAppropriateEditIcon() {
        Drawable icon;
        // Resources.getDrawable(int) is deprecated in API 22. The most reliable way to obtain drawables is now Context.getDrawable(int).
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (isCurrentlyEditingDetails) {
                icon = getResources().getDrawable(R.mipmap.ic_save);
                enableEditTextFields(true);
                Toast.makeText(getApplicationContext(), "After you edit the information, click the check mark to save.",
                        Toast.LENGTH_LONG).show();
            } else {
                icon = getResources().getDrawable(R.mipmap.ic_menu_edit);
                enableEditTextFields(false);
            }
        } else {
            if (isCurrentlyEditingDetails) {
                icon = this.getDrawable(R.mipmap.ic_save);
                enableEditTextFields(true);
                Toast.makeText(getApplicationContext(), "After you edit the information, click the check mark to save.",
                        Toast.LENGTH_LONG).show();
            } else {
                icon = this.getDrawable(R.mipmap.ic_menu_edit);
                enableEditTextFields(false);
            }
        }
        return icon;
    }

    private Drawable getAppropriateStarIcon() {
        Drawable icon;
        // Resources.getDrawable(int) is deprecated in API 22. The most reliable way to obtain drawables is now Context.getDrawable(int).
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
        return icon;
    }

    /**
     * The menu option menu allows editing the contact entries.
     * This function enables or disables the editing ability depending on the state.
     *
     * @param enableFields - whether or not to enable the EditText fields
     */
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
