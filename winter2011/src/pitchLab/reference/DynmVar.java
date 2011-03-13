package pitchLab.reference;

/**
 * This class is a static class purely for variables shared among other classes. 
 *
 * @author Gavin Shriver
 * @version 0.6 April 7, 2009
 */

public class DynmVar
{
	//	PIANO WINDOW SETTINGS
	// ---	drawing settings:
	public static int window_Width = 1300;
	public static int tickSpacing;// = (int)(window_Width/Constants.DEF_NUM_TICKS);
	public static int keyWidth;//  = tickSpacing*10;
	// --- 	sound related:
	public static double centScaling;//s = (double)Constants.CENT_FULL_SCALE/(double)window_Width;
	
    /**
     * Sets the window width
     *
     * @param window_Width The window width to set in pixels
     */
	public static void setWindow_Width(int window_Width)
	{
		DynmVar.window_Width = window_Width;
		calcVars();
	}

    /**
     * Calculates the adjustments necessary based on a a windows width.
     * Adjustments include the tick spacing and cent scaling.
     */
	private static void calcVars()
	{
		tickSpacing = (int)(window_Width/Constants.DEF_NUM_TICKS);
		keyWidth  = tickSpacing*10;
		// --- 	sound related:
		centScaling = (double)Constants.CENT_FULL_SCALE/(double)window_Width;
	}
	
	//	GENERAL TEST SETTINGS: mode, sync, setCents
	public static int mode = 0;
	public static boolean syncResults = true;
	public static boolean userSetCents = true;
	public static boolean upAndDown_RS = true;
	
	//	RELATIVE SETTINGS 
	public static int minRelCentInterval = 100;	
	public static int maxRelCentInterval = 1200;
	
	//  Octave intervals (not the multipliers)
	public static double ARScaleMinOctaveInterval = 0.5;
	public static double ARScaleMaxOctaveInterval = 2.0;
	
	public static void setDefaults()
	{
		DynmVar.userSetCents = true;
		DynmVar.upAndDown_RS = true;
		DynmVar.window_Width = 1300;
		DynmVar.ARScaleMinOctaveInterval = 0.5;
		DynmVar.ARScaleMaxOctaveInterval = 2.0;
	}
	
	//	IN TEST VARIABLES
	public static long cycleStartTime;
	public static int cycles;
	public static int count = 0;
	
    public static int dragBarX  = 150;  // starting x cord of 'drag-able' bar changed by mouse listeners
	public static int dragFromX = 0; 
	public static int dragBarGrabOffset = 0;
	
	// ---	active mode param
	public static String noteToSet;
	
	public static double[] variableBank = new double[5];
							//	variableBank[0] = freq1
							//	variableBank[1] = freq2
							//	variableBank[2] = freq3
							//	variableBank[3] = activeBaseFreq
							//	variableBank[4] = ARCentScale

	public static double getFirstFreq()
	{
		return variableBank[0];
	}
	public static void setFirstFreq(double freq)
	{
		variableBank[0] = freq;
	}
	public static double getSecondFreq()
	{
		return variableBank[1];
	}
	public static void setSecondFreq(double freq)
	{
		variableBank[1] = freq;
	}
	public static double getThirdFreq()
	{
		return variableBank[2];
	}
	public static void setThirdFreq(double freq)
	{
		variableBank[2] = freq;
	}
	public static double getActiveBaseFreq()
	{
		return variableBank[3];
	}
	public static void setActiveBaseFreq(double freq)
	{
		variableBank[3] = freq;
	}
	public static double getARCentScale()
	{
		return variableBank[4];
	}
	public static void setARCentScale(double centScale)
	{
		variableBank[4] = centScale;
	}
}
