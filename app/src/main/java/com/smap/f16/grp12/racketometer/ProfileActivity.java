package com.smap.f16.grp12.racketometer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.smap.f16.grp12.racketometer.utils.SharedPreferenceHelper;

/**
 * A {@link ProfileActivity} that presents a users profile settings.
 */
public class ProfileActivity extends BaseActivity {
    private final static String INSTANCE_NAME
            = "com.smap.f16.grp12.racketometer.ProfileActivity.INSTANCE_NAME";
    private final static String INSTANCE_CLUB
            = "com.smap.f16.grp12.racketometer.ProfileActivity.INSTANCE_CLUB";

    private EditText txtName;
    private EditText txtClub;
    private Button btnSave;
    private SharedPreferenceHelper preferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        preferenceHelper = new SharedPreferenceHelper(this);

        initUiReferences();
        initSaveClickHandler();
        initDataFromSharedPreferences();
        initDateFromSavedInstance(savedInstanceState);
    }

    /**
     * Get temporary input values.
     * @param savedInstanceState The instance state.
     */
    private void initDateFromSavedInstance(Bundle savedInstanceState) {
        if(savedInstanceState == null) {
            return;
        }
        setName(savedInstanceState.getString(INSTANCE_NAME));
        setClub(savedInstanceState.getString(INSTANCE_CLUB));
    }

    /**
     * Save temporary input values.
     * @param outState The leaving state.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(INSTANCE_NAME, getName());
        outState.putString(INSTANCE_CLUB, getClub());
    }

    /**
     * Init text fields with data from shared preferences.
     */
    private void initDataFromSharedPreferences() {
        setName(preferenceHelper.getName());
        setClub(preferenceHelper.getClub());
    }

    /**
     * Save field values to shared preferences.
     */
    private void saveDateToSharedPreferences() {
        preferenceHelper.setName(getName());
        preferenceHelper.setClub(getClub());
    }

    /**
     * Initialize save click handler.
     */
    private void initSaveClickHandler() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDateToSharedPreferences();
                Toast.makeText(ProfileActivity.this, "Saved", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Initialize references to the view.
     */
    private void initUiReferences() {
        txtName = (EditText) findViewById(R.id.txt_name);
        txtClub = (EditText) findViewById(R.id.txt_club);
        btnSave = (Button) findViewById(R.id.btn_save);
    }

    /**
     * Set name {@link EditText} value.
     * @param value The value.
     */
    private void setName(String value) {
        txtName.setText(value);
    }

    /**
     * Set club {@link EditText} value.
     * @param value The value.
     */
    private void setClub(String value) {
        txtClub.setText(value);
    }

    /**
     * Get value from name {@link EditText} field.
     * @return The value.
     */
    private String getName() {
        return txtName.getText().toString();
    }

    /**
     * Get value from club {@link EditText} field.
     * @return The value.
     */
    private String getClub() {
        return txtClub.getText().toString();
    }
}
