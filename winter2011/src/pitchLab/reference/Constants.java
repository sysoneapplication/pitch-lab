package pitchLab.reference;

/**
 * Sets the global constants used by PitchLab
 *
 * @author Gavin Shriver
 * @version 0.6 April 7, 2009
 * 
 */

public class Constants
{
	
	//	MODES:
	public static final int DEFAULT = 0;
	public static final int PASSIVE_PITCH = 1;
	public static final int ACTIVE_PITCH = 2;
	public static final int PASSIVE_RELATIVE = 3;
	public static final int ACTIVE_RELATIVE = 4;
	public static final int PP_PRACTICE = 11;
	public static final int AP_PRACTICE= 12;
	public static final int PR_PRACTICE = 13;
	public static final int AR_PRACTICE = 14;
	
	//	SOUND / AUDIO CONSTANTS
    public static final int CENTS_PER_OCTAVE = 1200;
	public static final int CENTS_PADDING_AT_ENDS = 50; // number of cents of "padding" (allowable tones below bottom note)
	public static final int CENT_FULL_SCALE = CENTS_PER_OCTAVE+2*CENTS_PADDING_AT_ENDS;  // used for VISIBLE scale
	public static final double C0 = 16.35; //hz (bottom octave)
	public static final String[] SCALE = {"C","C#","D","Eb","E","F","F#","G","Ab","A","Bb","B","C"};
	public static final String[] RPSCALE = {"1","2m","2M","3m","3M","p4","a4/d5","p5","6m","6M","7m",
											"7M","p8","octave + 2m","octave + 2M","octave + 3m",
											"octave + 3M","octave + p4","octave + a4/d5","octave + p5",
											"octave + 6m","octave + 6M"};
	
	//	PianoWindow Constants
	public static final int LONG_WINDOW_WIDTH = 1300; 
	public static final int SHORT_WINDOW_WIDTH = 650;
	public static final int WINDOW_HEIGHT = 400;
	public static final int DEF_NUM_TICKS = 130;
	public static final int BIG_TICK_HEIGHT = 50;
	public static final int SMALL_TICK_HEIGHT = 30;
	public static final int KEY_START_HEIGHT = 100;	// height at which stylized keyboard begins
	public static final int whtKeyHeight = WINDOW_HEIGHT - KEY_START_HEIGHT;
	public static final int BLACK_KEY_START_HEIGHT = 160;	// black keys set higher
	public static final int blkKeyHeight = WINDOW_HEIGHT - BLACK_KEY_START_HEIGHT;
	public static final int DRAG_BAR_MARGIN = 12;

}
