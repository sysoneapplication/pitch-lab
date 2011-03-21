package pitchLab.modes.activePitch;

import pitchLab.pianoWindow.PianoWindow;
import pitchLab.reference.Calculations;
import pitchLab.reference.Constants;
import pitchLab.reference.DynmVar;
import sound.Sine;

/**
 * This class defines the Active Pitch methods used in PitchLab's Active Pitch mode
 *
 * @author Gavin Shriver
 * @version 0.6 April 20, 2009
 */
public class ActivePitchMethods
{
	private PianoWindow pw;
	
    /**
     * Sets the classes piano window attribute for Active Pitch mode, 
     * which in this case is only a line that represents the current 
     * note without a piano GUI.
     */
	public ActivePitchMethods(PianoWindow pw)
	{
		this.pw = pw;
	}
	
    /**
     * Determines the Active Pitch note based on the frquency calculated
     */
	public void ap_setNoteToSet()
	{
		Calculations.setActiveBaseFreq();
		DynmVar.setFirstFreq(Sine.randFreqGenerator(DynmVar.getActiveBaseFreq(), 2*DynmVar.getActiveBaseFreq()));
		DynmVar.noteToSet = Calculations.freqToProperNote(DynmVar.getFirstFreq());
	}

    /**
     * Stores the note for the Active Pitch mode
     */
	public void ap_recordNoteToSet()
	{
		pw.dataHardCopy.appendToLine(Double.toString(DynmVar.getFirstFreq()));
		pw.dataHardCopy.appendToLine(DynmVar.noteToSet);
	}

	public void ap_recordRescalePlayTime()
	{
		ap_setNoteToSet();
		ap_recordNoteToSet();
		ap_rescaleAndPlay();
		DynmVar.cycleStartTime = System.currentTimeMillis();
	}

    /**
     * In Active Pitch Mode, calculates the frequency of the note 
     * currently selected by the user in the GUI.
     * Based on the frequency calculated the X coordinate of the line in the
     * piano window is calculated and placed accordingly
     * After the piano window has been repainted and the frequency correctly 
     * calculated, the new frequency is played.
     */
	public void ap_rescaleAndPlay()
	{
		ap_setNoteToSet();
		
		if(DynmVar.mode == Constants.ACTIVE_PITCH)
			pw.setTitle("Note to Set:    " + DynmVar.noteToSet + pw.getCyclesString());
		else
			pw.setTitle("Note to Set:    " + DynmVar.noteToSet);
		
		double tempFreq = Sine.randFreqGenerator(DynmVar.getActiveBaseFreq(), 2*DynmVar.getActiveBaseFreq());
		DynmVar.dragBarX = Calculations.freqToX(tempFreq);
		
		pw.repaint();
		
		pw.contSine.play(tempFreq);
	}

    /**
     * Sets the frequency to play to the correct value based on the xPos of the 
     * GUI line in the piano window.  
     *
     * The blend notes indicates that while dragging the bar across the GUI piano 
     * window, the notes are blended together to play seemlessly as the user
     * drags the bar around.
     *
     * If PitchLab is in practice mode, then repaint the Infomation window with 
     * the currently selected note/frquency.
     *
     * @param xPos The current position of the GUI interface line in the piano window
     * @param practice The boolean that determines if the user has selected practice mode
     */
	public void blendNotes_ap(int xPos, boolean practice)
	{
		double freqToPlay = Calculations.frequencyFromX((xPos));
		if (practice)
		{
			pw.toolTip.setTip(Calculations.freqToProperNote(freqToPlay));
			pw.instruct.setCurrAnsSelected(pw.toolTip.getTip());
		}
		pw.contSine.setFrequency(freqToPlay);
	}
	
}
