package pitchLab.reference;

import sound.Sine;


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
		

	}//end xToCents
	
	
	// ---------------------------------------
	// freqToCents converts a frequency to
	// note + cents (with octave)
	// ---------------------------------------
	
	/**
	 * 
	 * @param freq
	 * @return
	 */
	public static String freqToProperNote(double freq)
	{
		if(DynmVar.userSetCents)
			return freqToCents(freq);
		else
			return freqToNote(freq);
	}
	
	/**
	 * 
	 * @param freq
	 * @return
	 */
	public static String freqToCents(double freq)
	{
		int octaveIndex = getCOctaveIndex(freq);
		int relCentsToNote = getRelCentsToNote(freq,octaveIndex);
		int noteIndex = getNoteIndex(relCentsToNote);
		
		int centsError = (int)(relCentsToNote - noteIndex*100);
		
		return Constants.SCALE[noteIndex]+ "_" +octaveIndex + " " + centsError + " Cents";
	}//end 
	
	/**
	 * 
	 * @param freq
	 * @return
	 */
	public static String freqToNote(double freq)
	{
		int octaveIndex = getCOctaveIndex(freq);
		return Constants.SCALE[getNoteIndex(getRelCentsToNote(freq,octaveIndex))] + octaveIndex;
	}
	
	/**
	 * 
	 * @param freq
	 * @return
	 */
	public static int getCOctaveIndex(double freq)
    {
		return (int)(Math.log(freq/Constants.C0)/Math.log(2));
    }
    
	/**
	 * 
	 * @param freq
	 * @return
	 */
    public static int getRelCentsToNote(double freq)
    {
    	return getRelCentsToNote(freq, getCOctaveIndex(freq));
    }
    
    /**
     * 
     * @param freq
     * @param octaveIndex
     * @return
     */
    public static int getRelCentsToNote(double freq, int octaveIndex)
    {
    	return (int)( (1200*Math.log(freq/(Constants.C0*Math.pow(2,octaveIndex))))/Math.log(2) );
    }
    
    /**
     * 
     * @param relCentsToNote
     * @return
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
	
    //  ===   Interval methods
    
   /**
    * 
    * @param freq1
    * @param freq2
    * @return
    */
    public static int calcCentsInterval(double freq1, double freq2)
    {
    	return (int)(1200*(Math.log(freq2/freq1)/Math.log(2) ) );
    }
    /**
     * 
     * @param centsInterval
     * @param intervalIndex
     * @return
     */
    public static int getCentsError(int centsInterval, int intervalIndex)
    {
    	return (int)(Math.abs(centsInterval) - intervalIndex*100);
    }
    
    /**
     * 
     * @param centsInterval
     * @return
     */
    public static int getIntervalIndex(int centsInterval)
    {
    	return Math.abs(getNoteIndex(Math.abs(centsInterval)));
    }
    
    /**
     * 
     * @param centsInterval
     * @param intervalIndex
     * @return
     */
    public static String getIntervalCents(int centsInterval, int intervalIndex)
    {	
		if(centsInterval < 0)
			return "-" + Constants.RPSCALE[intervalIndex] + " " + getCentsError(centsInterval,intervalIndex) + " Cents";
		else
			return "+" + Constants.RPSCALE[intervalIndex] + " " + getCentsError(centsInterval,intervalIndex) + " Cents";
    }
    
    /**
     * 
     * @param centsInterval
     * @param intervalIndex
     * @return
     */
    public static String getIntervalNoCents(int centsInterval, int intervalIndex)
    {		
		if(centsInterval < 0)
			return "-" + Constants.RPSCALE[intervalIndex];
		else
			return "+" + Constants.RPSCALE[intervalIndex];
    }
    
    /**
     * 
     * @param freq1
     * @param freq2
     * @return
     */
    public static String getToneInterval (double freq1, double freq2)
    {
    	return getToneInterval(calcCentsInterval(freq1,freq2));
    }
    
    /**
     * 
     * @param centsInterval
     * @return
     */
    public static String getToneInterval(int centsInterval)
    {   	
    	if(DynmVar.userSetCents)
    		return getIntervalCents(centsInterval,getIntervalIndex(centsInterval));
    	else
    		return getIntervalNoCents(centsInterval,getIntervalIndex(centsInterval));
    }

	/**
	 * 
	 */
	public static void setActiveBaseFreq()
	{
		DynmVar.setActiveBaseFreq( Sine.randFreqGenerator(Sine.minFreqBounds, 
				Sine.getRelativeOctaveFrequency(Sine.getNumberOfOctaves()-1)) );
	}
	
	/**
	 * 
	 * @param freq1
	 * @param freq2
	 * @return
	 */
	public static double getWindowCentScale(double freq1,double freq2)
	{
		return (double)calcCentsInterval(freq1, freq2)/(double)DynmVar.window_Width;
	}

	/**
	 * 
	 * @return
	 */
	public static int randXpos()
	{
		return randXpos(0, DynmVar.window_Width);
	}
	
	/**
	 * 
	 * @param xmin
	 * @param xmax
	 * @return
	 */
	public static int randXpos(int xmin, int xmax)
	{
		return (int)(Math.random()*(xmax-xmin) + xmin);
	}
	
	/**
	 * 
	 * @param freq
	 * @return
	 */
	public static int freqToX(double freq)
	{
		return (int)( ((1200*Math.log(freq/DynmVar.getActiveBaseFreq()))/Math.log(2) +50)/DynmVar.centScaling);
	}
	
	/**
	 * 
	 * @param xPos
	 * @return
	 */
	public static double frequencyFromX(int xPos)
	{
		return Math.pow(2,((DynmVar.centScaling*xPos-50.0)/1200.0))*DynmVar.getActiveBaseFreq();
	}
}
