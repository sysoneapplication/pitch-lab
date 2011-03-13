package pitchLab.modes.activeRelative;

import pitchLab.pianoWindow.PianoWindow;
import pitchLab.reference.Calculations;
import pitchLab.reference.Constants;
import pitchLab.reference.DynmVar;
import sound.Sine;

public class ActiveRelativeMethods
{
	
	/**
	 * Blends the notes together such that a smooth transition of notes is played
     * as the user drags the bar accross the GUI piano window.
     *
	 * @param xPos The x coordinate of the bar in the piano window
	 * @param firstTone The first random tone that will be played in the active relative test
	 * @param pw The GUI piano window
	 */
	public static void blendNotes_arp(PianoWindow pw, int xPos, boolean firstTone)
	{
		double freqToPlay = arFrequencyFromX((xPos));
		pw.toolTip.setTip((Calculations.getToneInterval(DynmVar.getFirstFreq(), freqToPlay)));
		pw.instruct.setCurrAnsSelected(pw.toolTip.getTip());
		if(!firstTone)
			pw.contSine.setFrequency(freqToPlay);
	}
	
	/**
	 * Calls appropriate function to blend notes in the Active Relative test
	 * @param pw
	 * @param xPos
	 */
	public static void blendNotes_ar(PianoWindow pw, int xPos)
	{
		//contSine.setFrequency(Calculations.arFrequencyFromX(xPos));
		pw.contSine.setFrequency(arFrequencyFromX(xPos));
	}
	
	/**
	 * Caclulates and sets the two random frequencies that will be played for the Relative test.
	 */
	public static void ar_setIntervalToSet()
	{
		setARCentScale();
		int firstX;
		int secondX;
		do
		{
			//find the location of the FIRST tone in the interval
			firstX = Calculations.randXpos();
						
			//find the location of the SECOND tone in the interval
			if (DynmVar.upAndDown_RS)
				secondX = Calculations.randXpos();
			else //for only increasing variables
				secondX = Calculations.randXpos(firstX, DynmVar.window_Width);

		}while(!acceptableInterval(firstX, secondX)); //test acceptability of the tone interval...
		
		//System.out.println("interval is: " +(secondX - firstX));
		
		DynmVar.setFirstFreq(arFrequencyFromX(firstX));
		DynmVar.setSecondFreq(arFrequencyFromX(secondX));
		
		DynmVar.noteToSet = Calculations.getToneInterval(DynmVar.getFirstFreq(), DynmVar.getSecondFreq());
	}
	
	/** 
	 * Saves the first and second frequency that will be played in the Active Relative test.
     *
	 * @param pw The GUI piano window
	 */
	public static void ar_recordIntervalToSet(PianoWindow pw)
	{
		pw.dataHardCopy.appendToLine(Double.toString(DynmVar.getFirstFreq()));
		pw.dataHardCopy.appendToLine(Double.toString(DynmVar.getSecondFreq()));
		pw.dataHardCopy.appendToLine(DynmVar.noteToSet);
	}
	
	/**
     * Sets the piano window text to the currently selected note that will be set
     * if the user hits enter.
	 * 
	 * @param pw The GUI piano window
	 */
	public static void ar_rescaleAndPlay(PianoWindow pw)
	{
		//pw.setTitle("Interval to Set:    " + DynmVar.noteToSet);
		if(DynmVar.mode == Constants.ACTIVE_RELATIVE)
			pw.setTitle("Interval to Set:  " + DynmVar.noteToSet + pw.getCyclesString());
		else
			pw.setTitle("Interval to Set:  " + DynmVar.noteToSet);
		
		DynmVar.dragBarX = arFreqToX(DynmVar.getFirstFreq());
		pw.repaint();
		//contSine.play(DynmVar.getFirstFreq());
	}
	
	/**
	 * 
	 * @param pw The GUI piano window
	 */
	public static void ar_rescalePlayTime(PianoWindow pw)
	{
		ar_recordIntervalToSet(pw);
		ar_rescaleAndPlay(pw);
		DynmVar.cycleStartTime = System.currentTimeMillis();
	}
	
	/**
     * Returns an octave multiplier based on the minimum octave interval
     */
	public static double getARScaleMinOctaveMultiplier()
	{
		return Math.pow(2.0, DynmVar.ARScaleMinOctaveInterval);
	}
	
	/**
     * Returns an octave multiplier based on the maximum octave interval
	 */
	public static double getARScaleMaxOctaveMultiplier()
	{
		return Math.pow(2.0, DynmVar.ARScaleMaxOctaveInterval);
	}
	
	/**
	 * Sets the cents scale
	 */
	public static void setARCentScale()
	{
		setARActiveBaseFreq();
		DynmVar.setARCentScale(Calculations.getWindowCentScale(DynmVar.getActiveBaseFreq(),getARtopFreq()));
	}
	
	/**
	 * Sets the active base frequency based on the number of octaves and the maximum
     * octave interval set.
	 */
	public static void setARActiveBaseFreq()
	{
		DynmVar.setActiveBaseFreq( Sine.randFreqGenerator(Sine.minFreqBounds, 
				Sine.getRelativeOctaveFrequency(Sine.getNumberOfOctaves()-DynmVar.ARScaleMaxOctaveInterval)) );
	}
	
	/**
	 * Returns a Sine frequency based on octave settings.
	 * 
	 */
	public static double getARtopFreq()
	{
		return Sine.randFreqGenerator(DynmVar.getActiveBaseFreq()*getARScaleMinOctaveMultiplier(), DynmVar.getActiveBaseFreq()*getARScaleMaxOctaveMultiplier());
	}
	
	/**
	 * 
	 * @param xPos
	 * @return
	 */
	public static double arFrequencyFromX(int xPos)
	{
		return Math.pow(2,((DynmVar.getARCentScale()*xPos-50.0)/1200.0))*DynmVar.getActiveBaseFreq();
	}
	
	/**
	 * Returns the <code>int</code> x coordinate value of the bar based on the 
     * current frequency
	 * @param freq The current frequency
	 * @return
	 */
	public static int arFreqToX(double freq)
	{
		return (int)( ((1200*Math.log(freq/DynmVar.getActiveBaseFreq()))/Math.log(2) +50)/DynmVar.getARCentScale());
	}
	
	/**
	 * Returns an interval based on the first and second bar positions
	 * @param firstX The initial x-coordinate of the bar in the piano window
	 * @param secondX The final x-cordinate of the bar in the piano window
	 */
	public static boolean acceptableInterval(int firstX,int secondX)
	{
		int interval = Math.abs(Calculations.calcCentsInterval(arFrequencyFromX(firstX),arFrequencyFromX(secondX)));
		return (DynmVar.minRelCentInterval <= interval && interval <= DynmVar.maxRelCentInterval);
	}
	
}
