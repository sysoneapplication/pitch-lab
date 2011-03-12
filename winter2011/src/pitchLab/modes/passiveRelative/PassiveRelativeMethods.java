package pitchLab.modes.passiveRelative;

import pitchLab.pianoWindow.PianoWindow;
import pitchLab.reference.Calculations;
import pitchLab.reference.DynmVar;
import sound.Sine;

public class PassiveRelativeMethods
{
	
	private PianoWindow pw;
	
	public PassiveRelativeMethods(PianoWindow pw)
	{
		this.pw = pw;
	}
	
	public void pr_makeIntervalToSet()
	{
		DynmVar.setFirstFreq(Sine.randFreqGenerator());
		if (!DynmVar.upAndDown_RS)
			DynmVar.setSecondFreq(Sine.randFreqGenerator(DynmVar.getFirstFreq(),DynmVar.getFirstFreq()*2));		//generate second freq w/in bounds of f1 + 1200c
		else
			DynmVar.setSecondFreq(Sine.randFreqGenerator(DynmVar.getFirstFreq()/2, DynmVar.getFirstFreq()*2));	//generate second freq w/in bounds of f1 +- 1200c
	}
	
	public void pr_recordIntervalToSet()
	{
		pw.dataHardCopy.appendToLine(Double.toString(DynmVar.getFirstFreq()));				//write first frequency
		pw.dataHardCopy.appendToLine(Double.toString(DynmVar.getSecondFreq()));				//write second frequency 
		pw.dataHardCopy.appendToLine(Calculations.getToneInterval(DynmVar.getFirstFreq(), DynmVar.getSecondFreq()));  //write "calculated" note +- cents interval 
	}
}
