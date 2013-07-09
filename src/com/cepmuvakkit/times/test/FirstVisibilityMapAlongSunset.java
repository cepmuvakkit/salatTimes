package com.cepmuvakkit.times.test;

/*
 * To compile and run this source code you have to select text file encoding UTF-8
 * otherwise you can see so many "Invalid Character"  errors at the source code.
 * In Eclipse IDE right click on the Project --> Properties--> Resource--> 
 * Text File Encoding--> Other-->UTF-8 
 */
import java.text.DecimalFormat;
import com.cepmuvakkit.times.posAlgo.AstroLib;
import com.cepmuvakkit.times.posAlgo.Equatorial;
import com.cepmuvakkit.times.posAlgo.LunarPosition;
import com.cepmuvakkit.times.posAlgo.SolarPosition;


public class FirstVisibilityMapAlongSunset {

	static SolarPosition spa;
	LunarPosition lunar;
	private static DecimalFormat twoDigitFormat;

	public static void main(String[] args) {

		double jd;
		twoDigitFormat = new DecimalFormat("#0.00000");
		double[] latlong = new double[2];
		jd = AstroLib.calculateJulianDay(2012, 10, 16, 1, 20, 28, 0);
		System.out
				.println("--------------------------------------------------------------------------------");
		System.out.println("Day          :"
				+ AstroLib.fromJulianToCalendarStr(jd));
		double ΔT = AstroLib.calculateTimeDifference(jd);
		ΔT = 60;// Diyanet için Delta zamanında düzeltme
		System.out.println("ΔT           :  " + twoDigitFormat.format(ΔT));
		System.out.println("JD.FOR(ET)   :  " + twoDigitFormat.format(jd));
		System.out
				.println("--------------------------------------------------------------------------------");
		SolarPosition solar = new SolarPosition();
		Equatorial sunEq;
		double tau;//the Greenwich Hour Angle of the sun
		sunEq = solar.calculateSunEquatorialCoordinates(jd, ΔT);
		//tau=solar.sun_GHA(jd,sunEq.α, ΔT);
		 tau=solar.computeGHA( 16,  10, 2012, 1.34);
		for (int i = -180; i <= 180; i++) {
		latlong=findSunSetBorderLatLong(tau,sunEq.δ,i);
		System.out.println(" (" + twoDigitFormat.format(latlong[0])+","+twoDigitFormat.format(solar.limitDegrees180pm(latlong[1]))+")");
		}
	}

	public static double[] findSunSetBorderLatLong(double tau, double δ,int i) {
		double K = Math.PI / 180.0; // to convert degrees into arc angle
		double tanLat, arctanLat = 0,longitude;
		double[] latlong = new double[2];
		longitude = i + tau;
		tanLat = -Math.cos(longitude * K) / Math.tan(δ * K);
		arctanLat = Math.atan(tanLat) / K;
		latlong[0]=arctanLat;
		latlong[1]=longitude;
		return latlong;
	}


	/**
	 * Returns the Greenwich Hour Angle of the sun on a specific date and time.
	 * 
	 * If we consider as great circles of reference the celestial equator and
	 * the hour circle (meridian) through Greenwich , the coordinates of the
	 * star are its declination and Greenwich Hour Angle (G.H.A.)
	 * 
	 * The G.H.A. of a star is the angle between the meridian of Greenwich and
	 * the meridian of the star measured westward from from the Greenwich
	 * meridian 0 to 360 degrees.
	 * 
	 * @param day
	 *            Day of the month (1-31).
	 * @param month
	 *            Month of year (1-12, where January is 1 ).
	 * @param year
	 *            four digit year.
	 * @param decimalHours
	 *            The hour, minute and seconds, in a decimal notation ( 10:30:00
	 *            == 10.5 ).
	 * @return Greenwich Hour Angle of the sun on a specific date and time.
	 */
	private static double computeGHA(int day, int month, int year, double decimalHours) {
		long n;
		double K = Math.PI / 180.0;
		double x, xx, p;

		n = 365 * year + day + 31 * month - 46;
		if (month < 3) {
			n = n + (int) ((year - 1) / 4.0);
		} else {
			n = n - (int) (0.4 * month + 2.3) + (int) (year / 4.0);
		}

		p = decimalHours / 24.0;
		x = (p + n - 7.22449E5) * 0.98564734 + 279.306;
		x = x * K;
		xx = -104.55 * Math.sin(x) - 429.266 * Math.cos(x) + 595.63
				* Math.sin(2.0 * x) - 2.283 * Math.cos(2.0 * x);
		xx = xx + 4.6 * Math.sin(3.0 * x) + 18.7333 * Math.cos(3.0 * x);
		xx = xx - 13.2 * Math.sin(4.0 * x) - Math.cos(5.0 * x)
				- Math.sin(5.0 * x) / 3.0 + 0.5 * Math.sin(6.0 * x) + 0.231;
		xx = xx / 240.0 + 360.0 * (p + 0.5);
		if (xx > 360) {
			xx = xx - 360.0;
		}
		return xx;
	}

}
