package com.cepmuvakkit.times.settings;

import com.cepmuvakkit.times.R;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class SalatTimesPreferenceActivity extends PreferenceActivity {

	private ListPreference calculationMethodList;
	private EditTextPreference dawnAngleText;
	private EditTextPreference duskAngleText;
	private ListPreference estimationMethodListFajr;
	private ListPreference estimationMethodListIsha;
	private CheckBoxPreference useEstAlwaysFajrCheckBox;
	private CheckBoxPreference useEstAlwaysIshaCheckBox;
	private EditTextPreference fixedMinText;
	private CheckBoxPreference applytoAllCheckBox;

	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.salat_times_preferences);
		
		//setValueIndex()

		calculationMethodList = (ListPreference) findPreference("calculationMethodsIndex");
		dawnAngleText = (EditTextPreference) findPreference("dawnAngle");
		duskAngleText = (EditTextPreference) findPreference("duskAngle");
		calculationMethodList
				.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						final String val = newValue.toString();
						int index = calculationMethodList.findIndexOfValue(val);
						if (index == 6) {
							dawnAngleText.setEnabled(true);
							duskAngleText.setEnabled(true);
						} else {
							dawnAngleText.setEnabled(false);
							duskAngleText.setEnabled(false);
						}
						return true;
					}
				});
		estimationMethodListFajr = (ListPreference) findPreference("estMethodofFajr");
		estimationMethodListIsha = (ListPreference) findPreference("estMethodofIsha");
		useEstAlwaysFajrCheckBox = (CheckBoxPreference) findPreference("useEstAlwaysFajr");
		useEstAlwaysIshaCheckBox = (CheckBoxPreference) findPreference("useEstAlwaysIsha");
		applytoAllCheckBox = (CheckBoxPreference) findPreference("applytoAll");

		fixedMinText = (EditTextPreference) findPreference("fixedMin");

		estimationMethodListFajr
				.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						final String val = newValue.toString();
						int index = estimationMethodListFajr
								.findIndexOfValue(val);
						if (index == 0) {
							useEstAlwaysFajrCheckBox.setEnabled(false);
							applytoAllCheckBox.setEnabled(false);
						}

						else {
							useEstAlwaysFajrCheckBox.setEnabled(true);
							applytoAllCheckBox.setEnabled(true);
						}

						if (index == 8)
							fixedMinText.setEnabled(true);
						else
							fixedMinText.setEnabled(false);

						return true;
					}
				});

		estimationMethodListIsha
				.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						final String val = newValue.toString();
						int index = estimationMethodListIsha
								.findIndexOfValue(val);
						if 
						(index == 0) {
							useEstAlwaysIshaCheckBox.setEnabled(false);
							applytoAllCheckBox.setEnabled(false);
						} else {
							useEstAlwaysIshaCheckBox.setEnabled(true);
							applytoAllCheckBox.setEnabled(true);
						}

						if (index == 8)
							fixedMinText.setEnabled(true);
						else
							fixedMinText.setEnabled(false);
						return true;
					}
				});

	}

}
