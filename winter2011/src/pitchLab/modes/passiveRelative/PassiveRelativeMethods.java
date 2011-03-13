package pitchLab.modes.passiveRelative;

import pitchLab.pianoWindow.PianoWindow;
import pitchLab.reference.Calculations;
import pitchLab.reference.DynmVar;
import sound.Sine;

/**
 * This class defines the Passive Relative methods used in PitchLab's Passive Relative mode
 *
 * @author Gavin Shriver
 * @version 0.6 April 20, 2009
 */
public class PassiveRelativeMethods
{
	
	private PianoWindow pw;
	
    /**
     * Sets the Passive Relative piano window 
     *
     * @param pw The GUI piano window
     */
	public PassiveRelativeMethods(PianoWindow pw)
	{
		this.pw = pw;
	}
	
	/**
	 * Calculates and sets the two random frequencies that will be played for the relative test.
	 */
	public void pr_makeIntervalToSet()
	{
		DynmVar.setFirstFreq(Sine.randFreqGenerator());
		if (!DynmVar.upAndDown_RS)
			DynmVar.setSecondFreq(Sine.randFreqGenerator(DynmVar.getFirstFreq(),DynmVar.getFirstFreq()*2));		//generate second freq w/in bounds of f1 + 1200c
		else
			DynmVar.setSecondFreq(Sine.randFreqGenerator(DynmVar.getFirstFreq()/2, DynmVar.getFirstFreq()*2));	//generate second freq w/in bounds of f1 +- 1200c
	}
	
	/** 
	 * Saves the first and second frequency that will be played in the Passive Relative test
     * along with the tone interval.
     *
     */
	public void pr_recordIntervalToSet()
	{
		pw.dataHardCopy.appendToLine(Double.toString(DynmVar.getFirstFreq()));				//write first frequency
		pw.dataHardCopy.appendToLine(Double.toString(DynmVar.getSecondFreq()));				//write second frequency 
		pw.dataHardCopy.appendToLine(Calculations.getToneInterval(DynmVar.getFirstFreq(), DynmVar.getSecondFreq()));  //write "calculated" note +- cents interval 
	}
}
