package com.cepmuvakkit.times.settings;

import com.cepmuvakkit.times.R;
import com.cepmuvakkit.times.Schedule;
import com.cepmuvakkit.times.VARIABLE;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.DialogPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.widget.Toast;

public class SalatTimesPreferenceActivity extends PreferenceActivity {
	// Check /res/xml/preferences.xml file for this preference
	private ListPreference calculationMethodList;
	private ListPreference estimationMethodListFajr;
	private ListPreference estimationMethodListIsha;
	private CheckBoxPreference useEstAlwaysFajrCheckBox;
	private CheckBoxPreference useEstAlwaysIshaCheckBox;
	private EditTextPreference fixedMinText;
	private CheckBoxPreference applytoAllCheckBox;
	private SwitchPreference onlineCalc;
	private PreferenceScreen dawn_dusk_angle,times_screen;
	// private SwitchPreference onlineCalc;
	//private SeekBarPreference   offsetFajr,offsetSunrise,offsetDhur,offsetAsr,offsetMagrib,offsetIsha;

	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.salat_times_preferences);

		// setValueIndex()
		onlineCalc = (SwitchPreference) findPreference("enable_internal_calc");
		calculationMethodList = (ListPreference) findPreference("calculationMethodsIndex");
		
		dawn_dusk_angle= (PreferenceScreen)findPreference("dawn_dusk_angle");
		times_screen= (PreferenceScreen)findPreference("times_screen");
		dawn_dusk_angle.setEnabled(false);
		
	    int index=Integer.parseInt(VARIABLE.settings.getString("calculationMethodsIndex",0+""));
		if (index== 0) times_screen.setEnabled(false);
		calculationMethodList.setOnPreferenceChangeListener(
				new Preference.OnPreferenceChangeListener() {
					public boolean onPreferenceChange(Preference preference,Object newValue)
					{	final String val = newValue.toString();
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
		
		
		  findPreference("reset_times").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
	      {
	        public boolean onPreferenceClick(Preference paramAnonymousPreference)
	        {
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
						if (index == 0) {
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

	
	public void resetTimes() throws Throwable {
		
	/*	SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
	    PreferenceScreen localPreferenceScreen = (PreferenceScreen)findPreference("times_screen");
        localPreferenceScreen.getDialog().dismiss();
        localEditor.putInt("offsetFajr", 0);
        localEditor.commit();*/
        
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
