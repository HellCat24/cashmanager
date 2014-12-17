package cashmanager.helo.com.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import cashmanager.helo.com.R;

/**
 * Created by Mazhukin Oleh on 10.11.2014.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}
