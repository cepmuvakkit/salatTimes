package com.cepmuvakkit.times.test;

/*
 * To compile and run this source code you have to select text file encoding UTF-8
 * otherwise you can see so many "Invalid Character"  errors at the source code.
 * In Eclipse IDE right click on the Project --> Properties--> Resource--> 
 * Text File Encoding--> Other-->UTF-8 
 */
import java.text.DecimalFormat;

import com.cepmuvakkit.times.posAlgo.AstroLib;
import com.cepmuvakkit.times.posAlgo.Ecliptic;
import com.cepmuvakkit.times.posAlgo.Equatorial;
import com.cepmuvakkit.times.posAlgo.Horizontal;
import com.cepmuvakkit.times.posAlgo.LunarPosition;
import com.cepmuvakkit.times.posAlgo.SolarPosition;
import com.cepmuvakkit.times.posAlgo.SunMoonPosition;

public class FirstVisibility {

	static SolarPosition spa;
	LunarPosition lunar;
	private static DecimalFormat twoDigitFormat;

	public static void main(String[] args) {
		int temperature = 10, pressure = 1010;
		twoDigitFormat = new DecimalFormat("#0.00000");
		int altitude = 0;
		
		double longitude = -105.863, latitude = -36.86566, timezone = 0;//(0)
		//double longitude = -112.19, latitude = -4.38, timezone = 0;//(2)
		//double longitude = -86.3, latitude = -69.34, timezone = 0;//(1)
		//double longitude = -177.337, latitude = -30.1355, timezone = 0;
		//double longitude = 91.3729, latitude = -23.862, timezone = 0;
		SolarPosition solar = new SolarPosition();
		LunarPosition lunar = new LunarPosition();
		Ecliptic moonPosEc, sunPosEc;
		Equatorial moonPosEq, sunEq;
		double jd;
		jd = AstroLib.calculateJulianDay(2012, 10, 16, 1, 20, 28, 0);
		//jd = AstroLib.calculateJulianDay(2012, 10, 16, 6, 0, 0, 0);
		//jd = AstroLib.calculateJulianDay(2012, 10, 16, 12, 0, 0, 0);

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

		sunEq = solar.calculateSunEquatorialCoordinates(jd, ΔT);
		System.out.println("SUN RA α     :  " + twoDigitFormat.format(sunEq.α));
		System.out.println("SUN Dec δ    :  " + twoDigitFormat.format(sunEq.δ));
		moonPosEq = lunar.calculateMoonEquatorialCoordinates(jd, ΔT);
		System.out.println("Moon RA α    :  "
				+ twoDigitFormat.format(moonPosEq.α));
		System.out.println("Moon Dec δ   :  "
				+ twoDigitFormat.format(moonPosEq.δ));

		double LongDiff, tau_Sun = 8.32 / (1440.0); // 8.32 min [cy]
		moonPosEc = lunar.calculateMoonEclipticCoordinates(jd, ΔT);
		sunPosEc = solar.calculateSunEclipticCoordinatesAstronomic(
				jd - tau_Sun, ΔT);
		LongDiff = moonPosEc.λ - sunPosEc.λ;
		double elongation = Math.sqrt(LongDiff * LongDiff + moonPosEc.β
				* moonPosEc.β);
		// In case of Small angles of elongation lattitude is
		// taken into root mean square due to accuracy
		// double Δinkm=149598000.0*Δ;
		double paralax = lunar.getHorizontalParallax(moonPosEc.Δ);

		System.out.println("Hor.Paralax  :  " + twoDigitFormat.format(paralax));

		System.out.println("Distant(Elon):  "
				+ twoDigitFormat.format(elongation));

		Horizontal horizontalMoon = moonPosEq.Equ2Topocentric(longitude,
				latitude, altitude, jd, ΔT);
		System.out.println("Moon Alt     :  "
				+ twoDigitFormat.format(horizontalMoon.h));
		
		double elevationCorrected = horizontalMoon.h
				+ AstroLib.getAtmosphericRefraction(horizontalMoon.h)
				* AstroLib
						.getWeatherCorrectionCoefficent(temperature, pressure);
		System.out
				.println("Moon Alt(Atm):  "
						+ twoDigitFormat.format(elevationCorrected));

	}
}
