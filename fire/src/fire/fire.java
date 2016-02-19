package fire;
import java.lang.Math;
public class fire {
	
	//	For input, the program requires dry-bulb and wet- bulb readings, 
	//a yes or no decision regarding snow on the ground, 
	//the preceding 24-hour precipitation, the current windspeed, 
	//yesterday's buildup index, and current herbaceous stage of vegetation. 
	//These are the same variables required by the tabular method.
	//IF (expression) negative,zero,positive

// declaring and initializing subroutins for calculating //
	public static double DangerCal(double DRY, double WET, boolean ISNOW, double PRECIP, double WIND, double BUO, double IHERB){
		double DF,FFM,ADFM,GRASS,TIMBER,FLOAD, BUI;
		DF = 0;
		FFM = 99;
		ADFM = 99;
		FLOAD = 0;
		double Ai [] = {-0.185900,-0.859000, -0.059660,-0.077373};
		double Bi [] = {30.0,19.2,13.8,22.5};
		double Ci [] = {4.5, 12.5, 27.5};
		double Di [] = {16.00, 10.0, 7.0, 5.0, 4.0, 3.0};
// tests to see if there is Snow
		if (ISNOW == true) {
			GRASS = 0;
			TIMBER = 0;
// if the preceding 24-hour Precipitation > 0
			if (PRECIP > .1 ) {
// calculates Buildup Index
				BUO = -50 * Math.log(1-(1-Math.exp(-BUO/50))*Math.exp(-1.175*(PRECIP-.1))); 
					if (BUO < 0) {
						BUO = 0;
					} else
						return BUO;
				} else 
					return BUO;  
			}
			else {
// calculates Fine Fuel Moisture
				double DIF = DRY - WET;
				for (int i=0; i<3; i++)
				if ((FFM - Ci [i]) <= 0) {
					FFM = Bi[i]*Math.exp(Ai[i]*DIF);
				}
				else {
					i = 4;
					FFM = Bi[i]*Math.exp(Ai[i]*DIF);
				}		
	}
// calculates Drying Factor for the day
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
				FFM = FFM + (IHERB - 1)*5;
			}
			if (PRECIP > 1 ) {
				BUO = -50 * Math.log(1-(1-Math.exp(-BUO/50))*Math.exp(-1.175*(PRECIP-.1))); 
				if (BUO < 0) 
					BUO = 0.0;
				}else {
					BUO = BUO + DF;
					ADFM = 0.9*FFM + 0.5 + 9.5 * Math.exp(-BUO/50); // calculates Adjusted Fuel Moist
				}
			if (ADFM >= 33){
				if (FFM >= 33){
					GRASS = 1;
					TIMBER = 1;
					return FFM;
				}else {
					TIMBER = 1;
					if (WIND < 14) {
					GRASS = 0.01312 * (WIND + 6) * Math.pow((33 - FFM),1.65) - 3; // calculates Grass and Timber
					}else {
					GRASS = 0.00918 * (WIND + 14.4) * Math.pow((33 - FFM),1.65) - 3;
					}
				}
			}else {
				if (WIND < 14) {
					TIMBER = 0.01312 * (WIND + 6) * Math.pow((33 - ADFM),1.65) - 3;
				}else {
					TIMBER = 0.00918 * (WIND + 14.4) * Math.pow((33 - ADFM),1.65) - 3;
					if (GRASS > 99) {
						GRASS = 99;
						if ( TIMBER > 99){
							TIMBER = 99;						
						} 
						if (TIMBER > 0 && BUO > 0)  {
							FLOAD = 1.75 * Math.log(TIMBER) + 0.32 * Math.log(BUO) - 1.640;  // calculates Fire Load Rating only if TIMBER & BUO are greater than 0
							if (FLOAD < 0) {
								FLOAD = 0;
								return FLOAD;
							}else {
								FLOAD = Math.pow(10, FLOAD);
								return FLOAD;
							}
							}
						
					}
				}
			} return FLOAD;
	}			
}
	
