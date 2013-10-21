package com.cepmuvakkit.times.settings;

import com.cepmuvakkit.times.posAlgo.HigherLatitude;
import com.cepmuvakkit.times.posAlgo.Methods;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Settings implements Methods, HigherLatitude {
	private static Settings instance = new Settings();
	private String customCity,countryName;
	private boolean isHanafiMathab=false;
	private double longitude, latitude, timezone;
	private int calculationMethodsIndex, estMethodofFajr, estMethodofIsha,
			temperature, pressure, altitude;
	
	public Settings() {
	}
	public static Settings getInstance() {
		return instance;
	}

	public int getCalculationMethodsIndex() {
		return calculationMethodsIndex;
	}

	public void setCalculationMethodsIndex(int calculationMethodsIndex) {
		this.calculationMethodsIndex = calculationMethodsIndex;
	}

	public byte[] getEstMethods() {
		byte[] estMethod = { (byte) estMethodofFajr, (byte) estMethodofIsha };
		return estMethod;
	}

	public static void setInstance(Settings instance) {
		Settings.instance = instance;
	}


	public void setCustomCity(String customCity) {
		this.customCity = customCity;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public double getTimezone() {
		return timezone;
	}

	public void setTimezone(double timezone) {
		this.timezone = timezone;
	}

	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public int getPressure() {
		return pressure;
	}

	public void setPressure(int pressure) {
		this.pressure = pressure;
	}

	public int getAltitude() {
		return altitude;
	}

	public void setAltitude(int altitude) {
		this.altitude = altitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public boolean isHanafiMathab() {
		return isHanafiMathab;
	}
	public void setHanafiMathab(boolean isHanafiMathab) {
		this.isHanafiMathab = isHanafiMathab;
	}

	public static void load(SharedPreferences pref) {
		instance.countryName = pref.getString("country", "Türkiye");
		instance.customCity = pref.getString("customCity", "DefCustomCity");
		instance.latitude = Double.parseDouble(pref.getString("latitude", "39.938"));
		instance.longitude = Double.parseDouble(pref.getString("longitude", "32.848"));
		instance.timezone = Double.parseDouble(pref.getString("timezone", "3.0"));
		instance.temperature = Integer.parseInt(pref.getString("temperature","10"));
		instance.pressure = Integer	.parseInt(pref.getString("pressure", "1010"));
		instance.altitude = Integer.parseInt(pref.getString("altitude", "0"));
		instance.calculationMethodsIndex = Integer.parseInt(pref.getString("calculationMethodsIndex", TURKISH_RELIGOUS + ""));
		instance.estMethodofFajr = Integer.parseInt(pref.getString("estMethodofFajr", NO_ESTIMATION + ""));
		instance.estMethodofIsha = Integer.parseInt(pref.getString("estMethodofIsha", NO_ESTIMATION + ""));
		instance.isHanafiMathab = pref.getString("isHanafiMathab", "0").equals("1");
		
	
	}

	
	public static void save(SharedPreferences preferences) {

		Editor editor = preferences.edit();
		editor.putString("country", instance.countryName);
		editor.putString("customCity", instance.customCity);
		editor.putString("latitude", instance.latitude + "");
		editor.putString("longitude", instance.longitude + "");
		editor.putString("timezone", instance.timezone + "");
		editor.putString("temperature", instance.temperature + "");
		editor.putString("pressure", instance.pressure + "");
		editor.putString("altitude", instance.altitude + "");
		//editor.putString("calculationMethodsIndex",	instance.calculationMethodsIndex + "");
		//editor.putString("isHanafiMathab", instance.isHanafiMathab?"1":"0"+"");
	    //editor.putString("estMethodofFajr", instance.estMethodofFajr + "");
	    //editor.putString("estMethodofIsha", instance.estMethodofIsha + "");
		

		editor.commit();
	}
	public String getCustomCity() {
		// TODO Auto-generated method stub
		return customCity;
	}

}
