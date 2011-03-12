package pitchLab.modes.passivePitch;

import pitchLab.pianoWindow.PianoWindow;
import pitchLab.reference.Calculations;
import pitchLab.reference.DynmVar;
import sound.Sine;

public class PassivePitchMethods
{

	private PianoWindow pw;
	
	public PassivePitchMethods(PianoWindow pw)
	{
		this.pw = pw;
	}
	
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

	public String pp_play(boolean practice)
	{
		double f = DynmVar.getFirstFreq();
		pw.contSine.play(f);
		return practice ? Calculations.freqToCents(DynmVar.getFirstFreq()) : "";
	}
} 
