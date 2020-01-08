package es.floppp.monumentaltreesgva.ui.preferences;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;

import es.floppp.monumentaltreesgva.R;


public class PreferencesFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
                setPreferencesFromResource(R.xml.preferences, rootKey);
        }
}