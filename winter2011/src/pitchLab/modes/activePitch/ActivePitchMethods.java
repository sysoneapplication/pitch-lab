package pitchLab.modes.activePitch;

import pitchLab.pianoWindow.PianoWindow;
import pitchLab.reference.Calculations;
import pitchLab.reference.Constants;
import pitchLab.reference.DynmVar;
import sound.Sine;


public class ActivePitchMethods
{
	private PianoWindow pw;
	
	public ActivePitchMethods(PianoWindow pw)
	{
		this.pw = pw;
	}
	
	public void ap_setNoteToSet()
	{
		Calculations.setActiveBaseFreq();
		DynmVar.setFirstFreq(Sine.randFreqGenerator(DynmVar.getActiveBaseFreq(), 2*DynmVar.getActiveBaseFreq()));
		DynmVar.noteToSet = Calculations.freqToProperNote(DynmVar.getFirstFreq());
	}
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
	public void ap_rescaleAndPlay()
	{
		ap_setNoteToSet();
		
		if(DynmVar.mode == Constants.ACTIVE_PITCH)
			pw.setTitle("Note to Set:    " + DynmVar.noteToSet + pw.getCyclesString());
		else
			pw.setTitle("Note to Set:    " + DynmVar.noteToSet);
		
		//get freq and set xPosition to that note
		double tempFreq = Sine.randFreqGenerator(DynmVar.getActiveBaseFreq(), 2*DynmVar.getActiveBaseFreq());
		DynmVar.dragBarX = Calculations.freqToX(tempFreq);
		
		pw.repaint();
		
		pw.contSine.play(tempFreq); // then begin playing that note
	}

	//
	//	Blend note methods
	//
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
