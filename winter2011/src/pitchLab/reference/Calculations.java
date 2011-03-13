package pitchLab.reference;

import sound.Sine;


/**
 * This class does all the calculations needed to create tones, pixel positions and anything
 * else requring math.
 *
 * @author Gavin Shriver
 * @version 0.6 April 7, 2009
 */

public class Calculations
{
	/**
	 * 
	 * @param x
	 * @return
	 */
	public static String xToCents(int x)
	{
		
		int noteIndex;
		int pm_pos = (int)((x*DynmVar.centScaling)/50); 
		
		if (pm_pos%2 != 0) 
			noteIndex = (pm_pos - pm_pos%2)/2;
		else
			noteIndex = pm_pos/2;
		
		int centsError = ((int)(x*DynmVar.centScaling) - 100*(noteIndex)-Constants.CENTS_PADDING_AT_ENDS);
		
		if (DynmVar.mode == Constants.PASSIVE_PITCH || DynmVar.mode == Constants.PP_PRACTICE || DynmVar.mode == Constants.AP_PRACTICE)
			return Constants.SCALE[noteIndex]+" " + centsError + " cents";
		else if (DynmVar.mode == Constants.PASSIVE_RELATIVE|| DynmVar.mode == Constants.PR_PRACTICE)
			return Constants.RPSCALE[noteIndex] + " " + centsError + " cents";
		else
			return "mode error   " + Constants.SCALE[noteIndex]+ " " + centsError + " cents";
		

	}
	
	
	// ---------------------------------------
	// freqToCents converts a frequency to
	// note + cents (with octave)
	// ---------------------------------------
	
	/**
     * Determines whether to convert a frequency to notes with cents
     * or without cents.
	 * 
	 * @param freq
	 * @return A string of the converted frequency either with or without cents.
	 */
	public static String freqToProperNote(double freq)
	{
		if(DynmVar.userSetCents)
			return freqToCents(freq);
		else
			return freqToNote(freq);
	}
	
	/**
	 * Converts a frequency to notes plus cents - and octive index
     *
	 * @param freq The current frequency
     * @return String of the converted frequency
	 */
	public static String freqToCents(double freq)
	{
		int octaveIndex = getCOctaveIndex(freq);
		int relCentsToNote = getRelCentsToNote(freq,octaveIndex);
		int noteIndex = getNoteIndex(relCentsToNote);
		
		int centsError = (int)(relCentsToNote - noteIndex*100);
		
		return Constants.SCALE[noteIndex]+ "_" +octaveIndex + " " + centsError + " Cents";
	}
	
	/**
	 * Converts a frequency to notes without cents - and octive index
	 * 
	 * @param freq
	 * @return String of the converted frequency
	 */
	public static String freqToNote(double freq)
	{
		int octaveIndex = getCOctaveIndex(freq);
		return Constants.SCALE[getNoteIndex(getRelCentsToNote(freq,octaveIndex))] + octaveIndex;
	}
	
	/**
     * Determines the current frequency octave based with logarithm
     *
	 * @param freq The current frequency
	 * @return The current octave
	 */
	public static int getCOctaveIndex(double freq)
    {
		return (int)(Math.log(freq/Constants.C0)/Math.log(2));
    }
    
	/**
     * Determines the relative cents of the current frequency
	 * 
	 * @param freq The current frequency
	 * @return The relative cents 
	 */
    public static int getRelCentsToNote(double freq)
    {
    	return getRelCentsToNote(freq, getCOctaveIndex(freq));
    }
    
    /**
     * Converts the current frequency to cents using logarithm 
     *
     * @param freq The frequency
     * @param octaveIndex The current octave the frequency is in
     *
     * @return The converted frequency into relative cents
     */
    public static int getRelCentsToNote(double freq, int octaveIndex)
    {
    	return (int)( (1200*Math.log(freq/(Constants.C0*Math.pow(2,octaveIndex))))/Math.log(2) );
    }
    
    /**
     * Converts relative cents to x-coordinate position for GUI
     *
     * @param relCentsToNote The currently calculated relative cents
     * @return The new x position
     */
    public static int getNoteIndex(int relCentsToNote)
    {
    	int pm_pos = (int)((relCentsToNote)/50);
    	
    	if(pm_pos == 0)
			return 0;
		else if (pm_pos%2 != 0 && pm_pos !=0) 
		   return (pm_pos - pm_pos%2)/2 +1;
		else
		   return pm_pos/2;
    }
	
   /**
    * Finds the frequency difference between two frequencies via logarithm
    *
    * @param freq1 The first frequency
    * @param freq2 The second frequency
    * @return The frequency 
    */
    public static int calcCentsInterval(double freq1, double freq2)
    {
    	return (int)(1200*(Math.log(freq2/freq1)/Math.log(2) ) );
    }

    /**
     * Calculates the error in the cents 
     * 
     * @param centsInterval The cent interval 
     * @param intervalIndex The interval index
     * @return Absolute value of error
     */
    public static int getCentsError(int centsInterval, int intervalIndex)
    {
    	return (int)(Math.abs(centsInterval) - intervalIndex*100);
    }
    
    /**
     * Find cent interval index based on current centsInterval:
     */
    public static int getIntervalIndex(int centsInterval)
    {
    	return Math.abs(getNoteIndex(Math.abs(centsInterval)));
    }
    
    /**
     * Determines what string to return indicating the cents interval
     *
     * @param centsInterval The cents interval
     * @param intervalIndex The interval index
     * @return String of the cents interval difference
     */
    public static String getIntervalCents(int centsInterval, int intervalIndex)
    {	
		if(centsInterval < 0)
			return "-" + Constants.RPSCALE[intervalIndex] + " " + getCentsError(centsInterval,intervalIndex) + " Cents";
		else
			return "+" + Constants.RPSCALE[intervalIndex] + " " + getCentsError(centsInterval,intervalIndex) + " Cents";
    }
    
    /**
     * Determines what string to return indicating the cents interval
     *
     * @param centsInterval The cents interval
     * @param intervalIndex The interval index
     *
     * @return String of new interval index without the cents
     */
    public static String getIntervalNoCents(int centsInterval, int intervalIndex)
    {		
		if(centsInterval < 0)
			return "-" + Constants.RPSCALE[intervalIndex];
		else
			return "+" + Constants.RPSCALE[intervalIndex];
    }
    
    /**
     * Get the tone interval between two frequencies
     * @param freq1 First frequency
     * @param freq2 Second frequency
     * @return The tone interval
     */
    public static String getToneInterval (double freq1, double freq2)
    {
    	return getToneInterval(calcCentsInterval(freq1,freq2));
    }
    
    /**
     * Depending on if the cents are being shown, return the tone interval
     *
     * @param centsInterval The current cents interval
     *
     * @return Tone interval with or without cents
     */
    public static String getToneInterval(int centsInterval)
    {   	
    	if(DynmVar.userSetCents)
    		return getIntervalCents(centsInterval,getIntervalIndex(centsInterval));
    	else
    		return getIntervalNoCents(centsInterval,getIntervalIndex(centsInterval));
    }

	/**
	 * Sets the base frequency based on the random sine generator and upper and lower limits
     * the frequency bounds.
	 */
	public static void setActiveBaseFreq()
	{
		DynmVar.setActiveBaseFreq( Sine.randFreqGenerator(Sine.minFreqBounds, 
				Sine.getRelativeOctaveFrequency(Sine.getNumberOfOctaves()-1)) );
	}
	
	/**
	 * Determine the cent scale for a given window
     * @param freq1 First frequency
     * @param freq2 Second frequency
	 * @return The calculated cents interval between two frequencies
	 */
	public static double getWindowCentScale(double freq1,double freq2)
	{
		return (double)calcCentsInterval(freq1, freq2)/(double)DynmVar.window_Width;
	}

	/**
	 * Calculate a random x-coordinate that is inside the window width
     *
	 * @return A random x-coordinate
	 */
	public static int randXpos()
	{
		return randXpos(0, DynmVar.window_Width);
	}
	
	/**
	 * Calculate a random x-coordinate that is inside the window width
     *
	 * @param xmin Minimum allowed value based on window width
	 * @param xmax Maximum allowed value based on window width
	 * @return Integer of a random x-coordinate
	 */
	public static int randXpos(int xmin, int xmax)
	{
		return (int)(Math.random()*(xmax-xmin) + xmin);
	}
	
	/**
	 * Converts a frequency to an x-coordinate 
	 * @param freq The current frequency
	 * @return The corresponding x-coordinate 
	 */
	public static int freqToX(double freq)
	{
		return (int)( ((1200*Math.log(freq/DynmVar.getActiveBaseFreq()))/Math.log(2) +50)/DynmVar.centScaling);
	}
	
	/**
	 * Converts a frequency from the current x-coordinate
     *
	 * @param xPos Current x-coordinate
     *
	 * @return The corresponding frequency
	 */
	public static double frequencyFromX(int xPos)
	{
		return Math.pow(2,((DynmVar.centScaling*xPos-50.0)/1200.0))*DynmVar.getActiveBaseFreq();
	}
}
