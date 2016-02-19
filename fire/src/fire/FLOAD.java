package fire;
import java.lang.Math;
public class FLOAD {
	
	//	For input, the program requires dry-bulb and wet- bulb readings, 
	//a yes or no decision regarding snow on the ground, 
	//the preceding 24-hour precipitation, the current windspeed, 
	//yesterday's buildup index, and current herbaceous stage of vegetation. 
	//These are the same variables required by the tabular method.
	//IF (expression) negative,zero,positive

	
	
	public static double CalBUI(double BUO, double IHERB, double PRECIP){
		double BUI;
	// calculates Buildup Index
				BUI = -50 * Math.log(1-(1-Math.exp(-BUO/50))*Math.exp(-1.175*(PRECIP-.1))); 
					if (BUI > 0) {
					return BUI;
					}else {
							BUI = 0;
					}
		return BUI;
	}
	// calculates Fine Fuel Moisture	
		public static double CalFFM(double DRY, double WET){
			double FFM = 99;
			double Ai [] = {30.0,19.2,13.8,22.5};
			double Bi [] = {-0.185900,-0.859000, -0.059660,-0.077373};
			double Ci [] = {4.5, 12.5, 27.5};	
				double DIF = DRY - WET;
				for (int i=0; i<3; i++)
				if ((FFM - Ci [i]) <= 0) {
					FFM = Ai[i]*Math.exp(Bi[i])*DIF;
				}
				else {
					i = 4;
					FFM = Ai[i]*Math.exp(Bi[i])*DIF;
				}	
				return FFM;
		}
		// calculates Drying Factor for the day		
		public static double CalDF(double DRY, double WET, boolean ISNOW, double PRECIP, double WIND, double BUO, double IHERB){
			double DF,FFM;
			DF = 0;
			FFM = 99;
			double Di [] = {16.00, 10.0, 7.0, 5.0, 4.0, 3.0};
		// loop to 
			for (int i=0; i<6; i++)
			if (FFM - Di [i] <= 0) {
				DF = 7;
			}else {
				DF = i - 1; // test to see if the Fine Moisture is 1 or less
			}
			if (FFM < 1) {
				FFM = 1;
				}	
			else {
				FFM = FFM + (IHERB - 1) * 5;
			}
			return DF;
		}
		
		public static double CalADFM(double WIND){
		double FFM,ADFM,BUO;
		FFM = 99;
		ADFM = 99;
		BUO = 0;
		// calculates Adjusted Fuel Moist
		ADFM = 0.9*FFM + 0.5 + 9.5 * Math.exp(-BUO/50); 
		return ADFM;
	}
		public static double GRASS(double WIND){
		double FFM,GRASS;
		FFM = 99;
		// calculates Fine Fuel Spread 
		if (WIND < 14) {
					GRASS = 0.01312 * (WIND + 6) * Math.pow((33 - FFM),1.65) - 3;
					}else {
					GRASS = 0.00918 * (WIND + 14.4) * Math.pow((33 - FFM),1.65) - 3;
					}
			return GRASS;
		}
		// calculates Timber Spread Index
					
		public static double CalTIMBER(double WIND, double BUO, double IHERB){
		double ADFM,TIMBER;
		ADFM = 99;
		if (WIND < 14) {
			TIMBER = 0.01312 * (WIND + 6) * Math.pow((33 - ADFM),1.65) - 3;
		}else {
			TIMBER = 0.00918 * (WIND + 14.4) * Math.pow((33 - ADFM),1.65) - 3;
		}
		return TIMBER;
		}
		
		// calculates Fire Load Index (Man-Hour Base)
		// calculates Fire Load Rating only if TIMBER & BUO are greater than 0
		public static double CalDF(double BUO){
		double TIMBER,FLOAD;
		FLOAD = 0;
		TIMBER = 0;
		FLOAD = 1.75 * Math.log(TIMBER) + 0.32 * Math.log(BUO) - 1.640;  
			if (FLOAD < 0) {
				FLOAD = 0;
				return FLOAD;
			}else {
				FLOAD = Math.pow(10, FLOAD);
				return FLOAD;
			}
							}
			
	}			

	
