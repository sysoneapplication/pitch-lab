package pitchLab.modes.passivePitch;

import pitchLab.pianoWindow.PianoWindow;
import pitchLab.reference.Calculations;
import pitchLab.reference.DynmVar;
import sound.Sine;

/**
 * This class defines the Active Pitch methods used in PitchLab's Active Pitch mode
 *
 * @author Gavin Shriver
 * @version 0.6 April 20, 2009
 */
public class PassivePitchMethods
{

	private PianoWindow pw;
	
    /**
     * Sets the classes piano window attribute for Active Pitch mode, 
     * which in this case is only a line that represents the current 
     * note without a piano GUI.
     */
	public PassivePitchMethods(PianoWindow pw)
	{
		this.pw = pw;
	}
	
    /**
     * Sets the first frequency for the Passive Pitch test
     */
	public void pp_setFreq()
	{
		DynmVar.setFirstFreq(Sine.randFreqGenerator());
	}

	public void pp_recordPlayTime()
	{
		pw.dataHardCopy.appendToLine(Double.toString(DynmVar.getFirstFreq()));
		pw.dataHardCopy.appendToLine(Calculations.freqToCents(DynmVar.getFirstFreq()));
		pw.contSine.play(DynmVar.getFirstFreq());
		DynmVar.cycleStartTime = System.currentTimeMillis();
	}

    /**
     * @param practice Determines whether or not the user has put PitchLab into practice mode
     */
	public String pp_play(boolean practice)
	{
		double f = DynmVar.getFirstFreq();
		pw.contSine.play(f);
		return practice ? Calculations.freqToCents(DynmVar.getFirstFreq()) : "";
	}
} 
