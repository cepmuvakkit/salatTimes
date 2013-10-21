package com.cepmuvakkit.times;


public class CONSTANT {

	public static final boolean DEBUG = false;

	public static final long RESTART_DELAY = 1000; // 1 second
	public static final long POST_NOTIFICATION_DELAY = 5000; // 5 seconds

	public static final String[][] CALCULATION_METHOD_COUNTRY_CODES = new String[][]{

		/** METHOD_EGYPT_SURVEY:	Africa, Syria, Iraq, Lebanon, Malaysia, Parts of the USA **/
		new String[]{
				// Africa
				"AGO", "BDI", "BEN", "BFA", "BWA", "CAF", "CIV", "CMR", "COG", "COM", "CPV", "DJI", "DZA", "EGY", "ERI", "ESH", "ETH", "GAB", "GHA", "GIN", "GMB", "GNB", "GNQ", "KEN", "LBR", "LBY", "LSO", "MAR", "MDG", "MLI", "MOZ", "MRT", "MUS", "MWI", "MYT", "NAM", "NER", "NGA", "REU", "RWA", "SDN", "SEN", "SLE", "SOM", "STP", "SWZ", "SYC", "TCD", "TGO", "TUN", "TZA", "UGA", "ZAF", "ZAR", "ZWB", "ZWE",
				// Syria, Iraq, Lebanon, Malaysia
				"IRQ", "LBN", "MYS", "SYR"
		},

		/** METHOD_KARACHI_SHAF:		____ **/
		new String[]{},

		/** METHOD_KARACHI_HANAF:	Pakistan, Bangladesh, India, Afghanistan, Parts of Europe **/
		new String[]{"AFG", "BGD", "IND", "PAK"},

		/** METHOD_NORTH_AMERICA:	Parts of the USA, Canada, Parts of the UK **/
		new String[]{"USA", "CAN"},

		/** METHOD_MUSLIM_LEAGUE:	Europe, The Far East, Parts of the USA **/
		new String[]{
				// Europe
				"AND", "AUT", "BEL", "DNK", "FIN", "FRA", "DEU", "GIB", "IRL", "ITA", "LIE", "LUX", "MCO", "NLD", "NOR", "PRT", "SMR", "ESP", "SWE", "CHE", "GBR", "VAT",
				// Far East
				"CHN", "JPN", "KOR", "PRK", "TWN"
		},

		/** METHOD_UMM_ALQURRA:		The Arabian Peninsula **/
		new String[]{"BHR", "KWT", "OMN", "QAT", "SAU", "YEM"},

		/** METHOD_FIXED_ISHAA:		___ **/
		new String[]{}

	};
//	public static final short DEFAULT_CALCULATION_METHOD = 0; // Diyanet işleri

	public static final short FAJR_EW = 0, FAJR = 1,SUNRISE_EW = 2,SUNRISE = 3,DHUHR_EW = 4,DHUHR = 5, ASR_EW = 6, ASR= 7, MAGHRIB_EW = 8, MAGHRIB = 9, ISHAA_EW = 10,ISHAA = 11,NEXT_FAJR_EW = 12,NEXT_FAJR = 13; // Notification Times
	public static final short IMSAK = 0, GUNES = 1, OGLE = 2, IKINDI = 3, AKSAM = 4, YATSI = 5, SONRAKI_IMSAK = 6; // Notification Times

	//public static int[] TIME_NAMES = new int[]{R.string.fajr, R.string.sunrise, R.string.dhuhr, R.string.asr, R.string.maghrib, R.string.ishaa, R.string.next_fajr};
	public static int[] TIME_NAMES = new int[] {   R.string.fajr_ew,
			R.string.fajr, R.string.sunrise_ew, R.string.sunrise,
			R.string.dhuhr_ew, R.string.dhuhr, R.string.asr_ew, R.string.asr,
			R.string.maghrib_ew, R.string.maghrib, R.string.ishaa_ew,
			R.string.ishaa, R.string.next_fajr_ew, R.string.next_fajr };

	
	/*public static int[] TIME_NAMES = new int[]{
		R.string.fajrEW,
		R.string.fajr,
		R.string.sunrinseEW
		R.string.sunrise, 
		R.string.dhuhrEW,
		R.string.dhuhr,
		R.string.asrEW,
		R.string.asr,
		R.string.maghribEW,
		R.string.maghrib,
		R.string.ishaaEW,
		R.string.ishaa,
		R.string.next_fajrEW,
		R.string.next_fajr};
*/
	public static final short NOTIFICATION_NONE = 0, NOTIFICATION_DEFAULT = 1, NOTIFICATION_PLAY = 2, NOTIFICATION_CUSTOM = 3; // Notification Methods

	public static final short DEFAULT_TIME_FORMAT = 0; // AM/PM

	public static final short DEFAULT_ROUNDING_TYPE = 2; // Special

	private CONSTANT() {
		// Private constructor to enforce un-instantiability.
	}
}