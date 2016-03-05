package edu.towson.cis.ait642.amirhormozaki.project1;
import java.util.Scanner;
import java.lang.Math;


/**
 * this is the re-engineering of a Fire Danger Legacy System
 * the original source code was in Fortran 
 * and developed by William A.Main (1969)
 * which was used to calculate National Fire Danger Rating Indexes. 
 * Fuel moisture, buildup index, and drying factor are also available.
*/ 

public class FLOAD{

	private static Scanner input;
	
	/**
	 * Calculates the BUI: Current Buildup Index
	 * @param BUO the yesterday's buildup index
	 * @param PRECIP the preceding 24-hour precipitation
	 * @return the double
	 */
	private static double calBuildupIndex(double BUO, double PRECIP){
		double BUI = 0;
		// if the preceding 24-hour Precipitation > 0.1 
		// BUI must be adjusted by adding the DF(HOW?) after correction **
				if (PRECIP > .1 ) {
				BUI = -50 * Math.log(1-(1-Math.E*(-BUO/50))*Math.exp(-1.175*(PRECIP - 0.1))); 
						if (BUI > 0) {
							return BUI;
						}else {
							BUI = 0;
						}
					}
		return BUI;
	}
	
	private static final double Ai [] = {30.0,19.2,13.8,22.5}; // table values
	
	private static final double Bi [] = {-0.185900,-0.859000, -0.059660,-0.077373}; // table values
	
	private static final double Ci [] = {4.5, 12.5, 27.5}; // table value for range of (dry-wet)
	
	private static final double Di [] = {16.00, 10.0, 7.0, 5.0, 4.0, 3.0}; // table values
					
	/**
	 * Calculates FFM: Fine Fuel Moisture ( NO SNOW)
	 * @param DRY the dry-bulb reading
	 * @param WET the wet-bulb reading
	 * @return the double
	 */
	private static double calFineFuelMoisture(double DRY, double WET){
		double FFM = 99;
		double DIF = DRY - WET;
			for (int i=0; i<2; i++)
				if ((DIF - Ci [i]) <= 0) {
					FFM = Ai[i]*Math.exp(Bi[i])*DIF;
				}else {
					i = 3;
					FFM = Ai[i]*Math.exp(Bi[i])*DIF;
				}	
				return FFM;
		}
	
	/**
	 * Calculates DF (Drying Factor for the day).
	 * @param IHERB the current herbaceous stage of vegetation
	 * @return the double
	 */
	private static double calDryingFactor(double IHERB){
		double DF,FFM;
		DF = 1;
		FFM = 99;
			for (int i=0; i<6; i++)
				if (FFM - Di [i] > 0) {
					DF = Di[i] - 1;
				}else {
					DF = 7; 
					}
			return DF;
		}
	
	/**
	 * @param BUO the yesterday's buildup index
	 * @return the double
	 */
	private static double calAdjustedFuelMoist(double BUO){
		double ADFM, FFM;
		FFM = 99;
		ADFM = 99;
		ADFM = 0.9*FFM + 0.5 + 9.5 * Math.exp(-BUO/50); // *line 15
		return ADFM;
	}
	
	/**
	 * Calculates GRASS: Fine Fuel Spread
	 * @param WIND the current windspeed
	 * @return the double
	 */
	private static double calGrass(double WIND){
		double FFM,GRASS;
		FFM = 99;
		if (WIND < 14) {
			GRASS = 0.01312 * (WIND + 6) * Math.pow((33 - FFM), 1.65) - 3;
		}else {
			GRASS = 0.00918 * (WIND + 14.4) * Math.pow((33 - FFM), 1.65) - 3;
					}
		return GRASS;
		}
		
	/**
	 * Calculates TIMBER: Timber Spread Index
	 * @param WIND the current windspeed
	 * @param ADFM the Adjusted Fuel Moist
	 * @return the double
	 */			
	private static double calTimber(double WIND, double ADFM){
		double TIMBER;
		ADFM = 99;
		if (WIND < 14) {
			TIMBER = 0.01312 * (WIND + 6) * Math.pow((33 - ADFM), 1.65) - 3;
		}else {
			TIMBER = 0.00918 * (WIND + 14.4) * Math.pow((33 - ADFM), 1.65) - 3;
		}
	return TIMBER;
	}
		
	
	/**
	 * Calculates FLOAD: Fire Load Index (Man-Hour Base)
	 * @return the double
	 */
	// calculates FLOAD only if TIMBER & BUI are greater than 0
	private static double calFload(){
		double TIMBER,FLOAD, BUI;
		FLOAD = 0;
		TIMBER = 0;
		BUI = 0;
		if (TIMBER > 0 && BUI > 0) {
			FLOAD = 1.75 * Math.log10(TIMBER) + 0.32 * Math.log10(BUI) - 1.640;  
			if (FLOAD < 0) {
				FLOAD = 0;
			}else {
				FLOAD = Math.pow(10, FLOAD);		
			}
		}return FLOAD;	
	}

	/**
	 * The main method.
	 * @param args the arguments
	 */
	
	public static void main(String[] args) {
		
		input = new Scanner(System.in);
        System.out.print("Please enter the Dry Bulb Temp: ");
        double DRY = input.nextDouble();
        System.out.print("Please enter the Wet Bulb Temp: ");
        double WET = input.nextDouble();
        System.out.print("Please enter the Current Wind Speed: ");
        double WIND = input.nextDouble();
        System.out.print("Please enter the last value of the Buildup Index : ");
        double BUO = input.nextDouble();
        System.out.print("Please enter the Current Herp State of District ('1' for Cured, '2' for Transition, '3' for Green): ");
        double IHERB = input.nextDouble();
        System.out.print("Please enter 0 for 'NO' snow and 1 for snow on the ground: ");
        int ISNOW = input.nextInt();
        System.out.print("Please enter the past 24 hours precipitation in inches and hundredths : ");
        double PRECIP = input.nextDouble();    
        
        /**
    	 * @return the int
    	 */
        // tests to see if there is Snow
    		if (ISNOW == 1) {		
    			double GRASS = 0;
    			double TIMBER = 0;
    			double FLOAD = 0;
    			//adjusts the BUI
    			double BUI = calBuildupIndex(PRECIP, BUO); 
    			System.out.printf("Grass Spread Index is: %s\n", GRASS);
    			System.out.printf("Timber Spread Index is: %s\n", TIMBER);
    			System.out.printf("Fire Load Raiting is: %s\n", FLOAD);
    			System.out.printf("Build Up Index is: %s\n", BUI);
    		// if there is no snow
    		}else {
    			
    			System.out.printf("\n ------- RESULTS ------- \n");
    			   		
    			double ffm = ffm(DRY, WET, IHERB);
				double bui = bui(BUO, IHERB, PRECIP);
				double adfm = calAdjustedFuelMoist(BUO);
    			double grass = calGrass(WIND);
    			double timber = calTimber(WIND, BUO);
    			double fload = calFload();
           
    			System.out.printf("Build Up Index: %s\n", bui);
    			System.out.printf("Fine Fuel Moisture: %s\n", ffm);	
    			System.out.printf("Adjusted Fuel Moisture: %s\n", adfm);
    			System.out.printf("Grass Spread Index: %s\n", grass);
    			System.out.printf("Timber Spread Index: %s\n", timber);
    			System.out.printf("Fire Load Rating: %s\n", fload);        
    		}
  	
		}

	private static double bui(double BUO, double IHERB, double PRECIP) {
		double df = calDryingFactor(IHERB);
		double bui = calBuildupIndex(PRECIP, BUO);
		if (PRECIP > 0.1) {
			bui = bui + df;
		}
		return bui;
	}

	private static double ffm(double DRY, double WET, double IHERB) {
		double ffm = calFineFuelMoisture(DRY, WET);
		if (ffm < 1) {
			ffm = 1;
		} else {
			ffm = ffm + ((IHERB - 1) * 5);
		}
		return ffm;
	}
}

