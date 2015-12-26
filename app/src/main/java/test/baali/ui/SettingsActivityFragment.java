package test.baali.ui;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

/**
 * A placeholder fragment containing a simple view.
 */
public class SettingsActivityFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener
{

    public SettingsActivityFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        bindPreferenceSummaryToValue(findPreference(getString(R.string.location_key)));
    }



 /*   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        addPreferencesFromResource(R.xml.pref_general);
//        bindPreferenceSummaryToValue(findPreference(getString(R.string.location_key)));
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }*/

    private void bindPreferenceSummaryToValue(Preference preference)
    {
        // listen for changes on preference
        preference.setOnPreferenceChangeListener(this);

        // while changed trigger the listener with the preference's current value
        onPreferenceChange(preference, PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString
                (preference.getKey(), ""));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue)
    {
        String value = newValue.toString();
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(value);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        }
        else {
            preference.setSummary(value);
        }
        return true;
    }
}
