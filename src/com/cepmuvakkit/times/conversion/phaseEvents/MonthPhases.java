/*
 * To compile and run this source code you have to select text file encoding UTF-8
 * otherwise you can see so many "Invalid Character"  errors at the source code.
 * In Eclipse IDE right click on the Project --> Properties--> Resource--> 
 * Text File Encoding--> Other-->UTF-8 
 */
package com.cepmuvakkit.times.conversion.phaseEvents;



import com.cepmuvakkit.times.posAlgo.AstroLib;
import com.cepmuvakkit.times.posAlgo.Ecliptic;
import com.cepmuvakkit.times.posAlgo.LunarPosition;

public class MonthPhases {

	//private double jd;
	private boolean[] isFound;
	private double synmonth = 29.530588861;
	private double[] moonPhasesJd;
	private String[] eclipses;
	private double tMoonPhase, tCrescent; // Calculated time for the New Moon
											// and the Crescent Moon in
											// JulianDays UTC
	final double dt = 7; // Step (1 week)
	final double dtc = 3.0; // Step (3 days)
	final double acc = (0.5 / 1440.0); // Desired Accuracy (0.5 min)

	// private short NewMoon=0, NewCrescent=8, FirstQuarter=90, FullMoon=180,
	// LastQuarter=270;

	enum Phases {

		NewMoon(0), FirstQuarter(90), FullMoon(180), LastQuarter(270);
		private int angle;

		private Phases(int angle) {
			this.angle = angle;
		}

		public int getAngle() {
			return angle;
		}
	};

	public MonthPhases(double jd) {
	//	String eclipse;
		double t0, t1;
		double D0, D1;
		moonPhasesJd=new double[5];
		eclipses=new String[2];
		MoonPhases phases = new MoonPhases();
		t0 = jd-synmonth;
		t1 = t0 + dt;
		tMoonPhase = t0;
		// Check 13 months

		// Correct for difference of ephemeris time and universal time
		double ΔT = AstroLib.calculateTimeDifference(t1);
		// Search for phases
		int i=0;
		for (Phases phase : Phases.values()) {

			isFound = new boolean[1];
			isFound[0] = false;
			// Bracket desired phase event
			D0 = phases.searchPhaseEvent(t0, ΔT, phase.getAngle());
			D1 = phases.searchPhaseEvent(t1, ΔT, phase.getAngle());
			
			while ((D0 * D1 > 0.0) || (D1 < D0)) {
				t0 = t1;
				D0 = D1;
				t1 += dt; // increase 1 week
				D1 = phases.searchPhaseEvent(t1, ΔT, phase.getAngle());// Finds
																		// correct
																		// week
																		// for
																		// iteration
			}
			// Iterate time of Moon phase
			tMoonPhase = AstroLib.Pegasus(phases, t0, t1, ΔT, acc, isFound,
					phase.getAngle());
			moonPhasesJd[i++]=tMoonPhase;
			//moonPhasesJd
		
			// Eclipse check
			if (phase == Phases.NewMoon) {
				eclipses[0] = SolarEclipseFlag(tMoonPhase, ΔT);
			// System.out.print(AstroLib.fromJulianToCalendar(tMoonPhase));
			} else if (phase == Phases.FullMoon) {
				eclipses[1] = LunarEclipseFlag(tMoonPhase, ΔT);
			} 
			
//			else {
//				eclipse = " "; // First or Last Quarter
//			}
////			System.out.print(AstroLib.fromJulianToCalendarStr(tMoonPhase)
//					+ eclipse);
//			System.out.print(tMoonPhase
//					+ eclipse);
			
		
			;
			if ((phase == Phases.NewMoon) && isFound[0]) {
				int crescentElongationAngle = 8;
				tCrescent = AstroLib.Pegasus(phases, tMoonPhase, tMoonPhase
						+ dtc, ΔT, acc, isFound, crescentElongationAngle);
				// System.out.print(isFound[0]);
				//moonPhasesJd[i++]=AstroLib.convertJulian2Gregorian(tCrescent);
				moonPhasesJd[i++]=tCrescent;
				//System.out.print(AstroLib.fromJulianToCalendarStr(tCrescent));
			}
			// Move search interval by one week
			t0 = tMoonPhase;
			t1 = t0 + dt;
		}

	}
	

	/**
	 * SolarEclipseFlag: Returns a 2 char. string indicating the possibility of
	 * a solar eclipse
	 * 
	 * @param tNewMoon
	 *            Julian Day when the New Moon occurs
	 * @param ΔT
	 *            parameter delta-T (ΔT)
	 * @return Eclipse flag
	 */
	String  SolarEclipseFlag(double tNewMoon, double ΔT) {
		Ecliptic moonPos;
		LunarPosition lunar = new LunarPosition();
		moonPos = lunar.calculateMoonEclipticCoordinates(tNewMoon, ΔT);
		double b = Math.toRadians(Math.abs(moonPos.β));// b Ecliptic latitude of
														// the Moon in [rad]

		if (b > 0.027586) {
			return ""; // No solar eclipse possible
		}
		if (b < 0.015223) {
			return "central_eclipse"; // Central eclipse certain
		}
		if (b < 0.018209) {
			return " possible_eclipse"; // Possible central eclipse
		}
		if (b < 0.024594) {
			return "partial_eclipse"; // Partial solar eclipse certain
		}
		return "possible_partial_eclipse"; // Possible partial solar eclipse
	}

	/**
	 * LunarEclipseFlag: Returns a 2 char. string indicating the possibility of
	 * a lunar eclipse a solar eclipse
	 * 
	 * @param tFullMoon
	 *            Julian Day when the Full Moon happen
	 * @param ΔT
	 *            parameter delta-T (ΔT)
	 * @return Eclipse flag
	 */
	String LunarEclipseFlag(double tFullMoon, double ΔT) {
		Ecliptic moonPos;
		LunarPosition lunar = new LunarPosition();
		moonPos = lunar.calculateMoonEclipticCoordinates(tFullMoon, ΔT);
		double b = Math.toRadians(Math.abs(moonPos.β));// b Ecliptic latitude of
														// the Moon in [rad]

		if (b > 0.028134) {
			return "R.string.blank"; // No lunar eclipse possible
		}
		if (b < 0.006351) {
			return "R.string.lunar_eclipse"; // Total lunar eclipse certain
		}
		if (b < 0.009376) {
			return "R.string.possible_lunar_eclipse"; // Possible total eclipse
		}
		if (b < 0.015533) {
			return "R.string.partial_lunar_eclipse"; // Partial lunar eclipse certain
		}
		if (b < 0.018568) {
			return "R.string.possible_partial_lunar_eclipse"; // Possible partial eclipse
		}
		if (b < 0.025089) {
			return "R.string.penumbral_eclipse"; // Penumbral lunar eclipse certain
		}
		return "R.string.possible_penumbral_eclipse"; // Possible penumbral lunar eclipse
	}
	
	public double[] getMoonPhasesJd() {
	
		return moonPhasesJd;
	}
	public String[] getEclipses() {
		return eclipses;
	}
	public int getLunation ()
	{
		final double LunatBase = 1948083.1284733997;
		int Lunation = (int) Math.floor((moonPhasesJd[0] + 7 - LunatBase) /synmonth);
		return Lunation;
	}
	
	}

