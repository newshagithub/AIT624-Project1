package fire;
import java.util.Scanner;
public class FLOADT {

	public static void main(String[] args) {

		Scanner input = new Scanner(System.in);
        System.out.print("Please enter the Dry Bulb Temp: ");
        double DRY = input.nextDouble();
        System.out.print("Please enter the Wet Bulb Temp: ");
        double WET = input.nextDouble();
        System.out.print("Please enter the Current Wind Speed: ");
        double WIND = input.nextDouble();
        System.out.print("Please enter the Last Value of teh Buildup Index : ");
        double BUO = input.nextDouble();
        System.out.print("Please enter the Current Herp State of District:  : ");
        double IHERB = input.nextDouble();
        System.out.print("Is the snow on the ground: ");
        boolean ISNOW = input.nextBoolean();
        
		// tests to see if there is Snow
				if (ISNOW == true) {
					GRASS = 0;
					TIMBER = 0;
			// if the preceding 24-hour Precipitation > .1
					if (PRECIP > .1 ) {
	}

}
