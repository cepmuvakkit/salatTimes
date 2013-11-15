package com.cepmuvakkit.times.settings;

import com.cepmuvakkit.times.CONSTANT;
import com.cepmuvakkit.times.R;
import com.cepmuvakkit.times.VARIABLE;
import com.cepmuvakkit.times.posAlgo.Methods;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.widget.Toast;

public class SalatTimesPreferenceActivity extends PreferenceActivity implements
		Methods {
	private ListPreference calculationMethodList;
	private ListPreference estimationMethodListFajr;
	private ListPreference estimationMethodListIsha;
	private CheckBoxPreference useEstAlwaysFajrCheckBox;
	private CheckBoxPreference useEstAlwaysIshaCheckBox;
	private EditTextPreference fixedMinText, baseLatitudeText;
	private CheckBoxPreference applytoAllCheckBox;
	private SwitchPreference onlineCalc;
	private PreferenceScreen dawn_dusk_angle, times_screen;
	private final int REQUEST_CODE_PICK_FILE = 2;
	private Intent fileExploreIntent;
	private static int timeIndexFile;

	// private SwitchPreference onlineCalc;

	// private SeekBarPreference
	// offsetFajr,offsetSunrise,offsetDhur,offsetAsr,offsetMagrib,offsetIsha;

	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.salat_times_preferences);

		// setValueIndex()
		//onlineCalc = (SwitchPreference) findPreference("enable_internal_calc");
		calculationMethodList = (ListPreference) findPreference("calculationMethodsIndex");

		dawn_dusk_angle = (PreferenceScreen) findPreference("dawn_dusk_angle");
		times_screen = (PreferenceScreen) findPreference("times_screen");
		dawn_dusk_angle.setEnabled(false);
		// Log.i("integer error",
		// VARIABLE.settings.getString("calculationMethodsIndex",
		// TURKISH_RELIGOUS + "")+"");
		int index = Integer.parseInt(VARIABLE.settings.getString(
				"calculationMethodsIndex", CONSTANT.DEFAULT_CALCULATION_METHOD
						+ ""));
		if (index == 0)
			times_screen.setEnabled(false);
		calculationMethodList
				.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						final String val = newValue.toString();
						int index = calculationMethodList.findIndexOfValue(val);
						if (index == 6) {
							dawn_dusk_angle.setEnabled(true);
						} else {
							dawn_dusk_angle.setEnabled(false);
						}

						if (index == 0) {
							times_screen.setEnabled(false);
						} else {
							times_screen.setEnabled(true);
						}
						return true;

					}
				});

		findPreference("reset_times").setOnPreferenceClickListener(
				new Preference.OnPreferenceClickListener() {
					public boolean onPreferenceClick(
							Preference paramAnonymousPreference) {
						try {
							SalatTimesPreferenceActivity.this.resetTimes();
						} catch (Throwable e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
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
		baseLatitudeText = (EditTextPreference) findPreference("baseLatitude");

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
						if (index == 1)
							baseLatitudeText.setEnabled(true);
						if (index == 8)
							fixedMinText.setEnabled(true);

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
						if (index == 0) {
							useEstAlwaysIshaCheckBox.setEnabled(false);
							applytoAllCheckBox.setEnabled(false);
						} else {
							useEstAlwaysIshaCheckBox.setEnabled(true);
							applytoAllCheckBox.setEnabled(true);
						}
						if (index == 1)
							baseLatitudeText.setEnabled(true);

						if (index == 8)
							fixedMinText.setEnabled(true);

						return true;
					}
				});

		fileExploreIntent = new Intent(
				com.cepmuvakkit.times.filepick.FilePickActivity.INTENT_ACTION_SELECT_FILE,
				null, this,
				com.cepmuvakkit.times.filepick.FilePickActivity.class);
		((ListPreference) findPreference("notificationMethod0"))
				.setOnPreferenceChangeListener(

				new Preference.OnPreferenceChangeListener() {
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						final String val = newValue.toString();
						int index = Integer.valueOf(val);
						timeIndexFile = 0;
						if (index == 3) {
							startActivityForResult(fileExploreIntent,
									REQUEST_CODE_PICK_FILE);
						}
						return true;

					}
				}

				);
		((ListPreference) findPreference("notificationMethod1"))
				.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						final String val = newValue.toString();
						int index = Integer.valueOf(val);
						timeIndexFile = 1;
						if (index == 3) {
							startActivityForResult(fileExploreIntent,
									REQUEST_CODE_PICK_FILE);
						}
						return true;

					}
				});
		((ListPreference) findPreference("notificationMethod2"))
				.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						final String val = newValue.toString();
						int index = Integer.valueOf(val);
						timeIndexFile = 2;
						if (index == 3) {
							startActivityForResult(fileExploreIntent,
									REQUEST_CODE_PICK_FILE);
						}
						return true;

					}
				});
		((ListPreference) findPreference("notificationMethod3"))
				.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						final String val = newValue.toString();
						int index = Integer.valueOf(val);
						timeIndexFile = 3;
						if (index == 3) {
							startActivityForResult(fileExploreIntent,
									REQUEST_CODE_PICK_FILE);
						}
						return true;

					}
				});
		((ListPreference) findPreference("notificationMethod4"))
				.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						final String val = newValue.toString();
						int index = Integer.valueOf(val);
						timeIndexFile = 4;
						if (index == 3) {
							startActivityForResult(fileExploreIntent,
									REQUEST_CODE_PICK_FILE);
						}
						return true;

					}
				});
		((ListPreference) findPreference("notificationMethod5"))
				.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						final String val = newValue.toString();
						int index = Integer.valueOf(val);
						timeIndexFile = 5;
						if (index == 3) {
							startActivityForResult(fileExploreIntent,
									REQUEST_CODE_PICK_FILE);
						}
						return true;

					}
				});
		((ListPreference) findPreference("notificationMethod6"))
				.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						final String val = newValue.toString();
						int index = Integer.valueOf(val);
						timeIndexFile = 6;
						if (index == 3) {
							startActivityForResult(fileExploreIntent,
									REQUEST_CODE_PICK_FILE);
						}
						return true;

					}
				});
		((ListPreference) findPreference("notificationMethod7"))
				.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						final String val = newValue.toString();
						int index = Integer.valueOf(val);
						timeIndexFile = 7;
						if (index == 3) {
							startActivityForResult(fileExploreIntent,
									REQUEST_CODE_PICK_FILE);
						}
						return true;

					}
				});
		((ListPreference) findPreference("notificationMethod8"))
				.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						final String val = newValue.toString();
						int index = Integer.valueOf(val);
						timeIndexFile = 8;
						if (index == 3) {
							startActivityForResult(fileExploreIntent,
									REQUEST_CODE_PICK_FILE);
						}
						return true;

					}
				});
		((ListPreference) findPreference("notificationMethod9"))
				.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						final String val = newValue.toString();
						int index = Integer.valueOf(val);
						timeIndexFile = 9;
						if (index == 3) {
							startActivityForResult(fileExploreIntent,
									REQUEST_CODE_PICK_FILE);
						}
						return true;

					}
				});
		((ListPreference) findPreference("notificationMethod10"))
				.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						final String val = newValue.toString();
						int index = Integer.valueOf(val);
						timeIndexFile = 10;
						if (index == 3) {
							startActivityForResult(fileExploreIntent,
									REQUEST_CODE_PICK_FILE);
						}
						return true;

					}
				});
		((ListPreference) findPreference("notificationMethod11"))
				.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						final String val = newValue.toString();
						int index = Integer.valueOf(val);
						timeIndexFile = 11;
						if (index == 3) {
							startActivityForResult(fileExploreIntent,
									REQUEST_CODE_PICK_FILE);
						}
						return true;

					}
				});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == REQUEST_CODE_PICK_FILE) {
			if (resultCode == RESULT_OK) {
				String newFile = data
						.getStringExtra(com.cepmuvakkit.times.filepick.FilePickActivity.returnFileParameter);
				Editor editor = VARIABLE.settings.edit();
				editor.putString("notificationCustomFile" + timeIndexFile,
						newFile);
				editor.commit();

				Toast.makeText(this,
						"Received FILE path from file browser:\n" + newFile,
						Toast.LENGTH_LONG).show();

			} else {// if(resultCode == this.RESULT_OK) {
				Toast.makeText(this, "Received NO result from file browser",
						Toast.LENGTH_LONG).show();
			}// END } else {//if(resultCode == this.RESULT_OK) {
		}// if (requestCode == REQUEST_CODE_PICK_FILE) {

		super.onActivityResult(requestCode, resultCode, data);
	}

	public void resetTimes() throws Throwable {

		times_screen.getDialog().dismiss();
		Editor editor = VARIABLE.settings.edit();
		editor.putInt("offsetFajr", 0);
		editor.putInt("offsetSunrise", 0);
		editor.putInt("offsetDhur", 0);
		editor.putInt("offsetAsr", 0);
		editor.putInt("offsetMagrib", 0);
		editor.putInt("offsetIsha", 0);

		editor.commit();
		Toast.makeText(getApplicationContext(), "Offset values are Cleared", 0)
				.show();

		this.finish();

	}
}
