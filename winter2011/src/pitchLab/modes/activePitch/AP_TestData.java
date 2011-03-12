package pitchLab.modes.activePitch;
/*
 * 	This class is meant to be a sub class to store test results in an array
 */
public class AP_TestData
{
		
	protected long startTime;
	protected long stopTime;
	protected int timeElapsed;
	protected int frequencyToChoose;
	protected String noteDisplayedToChoose;
	protected int frequencyChoosen;
	
	public String toCSVString()
	{
		//FIXME: generate return
		return "";
	}
	
	
	
	public long getStartTime()
	{
		return startTime;
	}
	public void setStartTime(long startTime)
	{
		this.startTime = startTime;
	}
	public long getStopTime()
	{
		return stopTime;
	}
	public void setStopTime(long stopTime)
	{
		this.stopTime = stopTime;
	}
	public int getTimeElapsed()
	{
		return timeElapsed;
	}
	public void setTimeElapsed(int timeElapsed)
	{
		this.timeElapsed = timeElapsed;
	}
	public int getFrequencyToChoose()
	{
		return frequencyToChoose;
	}
	public void setFrequencyToChoose(int frequencyToChoose)
	{
		this.frequencyToChoose = frequencyToChoose;
	}
	public String getNoteDisplayedToChoose()
	{
		return noteDisplayedToChoose;
	}
	public void setNoteDisplayedToChoose(String noteDisplayedToChoose)
	{
		this.noteDisplayedToChoose = noteDisplayedToChoose;
	}
	public int getFrequencyChoosen()
	{
		return frequencyChoosen;
	}
	public void setFrequencyChoosen(int frequencyChoosen)
	{
		this.frequencyChoosen = frequencyChoosen;
	}
	
	
}
