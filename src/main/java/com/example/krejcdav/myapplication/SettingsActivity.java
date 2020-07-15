package com.example.krejcdav.myapplication;

import android.content.DialogInterface;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Load settings fragment
        getFragmentManager().beginTransaction().replace(android.R.id.content,new SettingsFragment()).commit();
    }

    public static class SettingsFragment extends PreferenceFragment {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            // Configure On Click Database operations
            configureRemoveLastPreference();
            configureRemoveAllPreference();
            configureInsertRandomDataPreference();

            //  Configure updating preference summaries
            bindSummaryValue(findPreference(getString(R.string.pref_tram_price)));
            bindSummaryValue(findPreference(getString(R.string.pref_bus_price)));
        }

        private void configureRemoveLastPreference() {
        final DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        Preference removeLastPreference = findPreference(getString(R.string.pref_remove_last));
        removeLastPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.AlertDialogCustom);
                builder.setCancelable(true);
                builder.setTitle("Confirmation");
                builder.setMessage("Do you wish to remove the last record from database?");

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        databaseHelper.removeLastRow();
                        toastMessage("Record removed.");
                    }
                });
                builder.show();
                return true;
            }
        });
    }
        private void configureRemoveAllPreference() {
        final DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        Preference removeAllPreference = findPreference(getString(R.string.pref_remove_all));
        removeAllPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.AlertDialogCustom);
                builder.setCancelable(true);
                builder.setTitle("Confirmation");
                builder.setMessage("Do you wish to remove all records from database?");

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        databaseHelper.removeAllRows();
                        toastMessage("Records removed.");
                    }
                });
                builder.show();
                return true;
            }
        });
    }
        private void configureInsertRandomDataPreference() {
            final DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
            Preference insertRandomData = findPreference(getString(R.string.pref_insert_random));
            insertRandomData.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.AlertDialogCustom);
                    builder.setCancelable(true);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you wish to add random records to database?");

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            databaseHelper.insertRandomRows(5);
                            toastMessage("Records added.");
                        }
                    });
                    builder.show();
                    return true;
                }
            });
        }
        private void toastMessage(String message) {
            Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 200);
            toast.show();
        }
        private static void bindSummaryValue(Preference preference){
            preference.setOnPreferenceChangeListener(listener);
            listener.onPreferenceChange(
                    preference,
                    PreferenceManager.getDefaultSharedPreferences(
                            preference.getContext()).getString(
                            preference.getKey(),
                            "")
            );
        }
        private static Preference.OnPreferenceChangeListener listener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String newString = newValue.toString();
                if (preference instanceof EditTextPreference)
                    preference.setSummary(newString + " " + preference.getContext().getString(R.string.currency));
                return true;
            }
    };
    }

}