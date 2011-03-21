package pitchLab.instructWindow;

import java.awt.Dimension;

/**
 * The IWConsts generates the Instruction Window constants for the user interface.
 *
 * @author Gavin Shriver
 * @version 0.6 April 9, 2009
 *
 */
public class IWConsts
{
	public static final String DISP_CUR_TO_SET = 	"Value to set:       ";
	public static final String DISP_PREV_TO_SET = 	"Previous solution:  ";
	public static final String DISP_CUR_SEL = 		"Current selection:  ";
	public static final String DISP_PREV_SEL = 	"Previous selection: ";
    public static final String NUM_COMPLETED = "Number of rounds completed:";
	
	public static final String[] STYLES = {"regular", "small_italic", "bold"};
	
	
	
	//
	//	SHARED TEXT
	//
	private static final String PRESS_ENTER_TO_START =
		"0) Press the \"Enter\" key to start.";
	
	private static final String MAY_RELEASE_MOUSE = 
		"(if you release your mouse the bar will stay where it is, "+
		"simply press and hold the bar to drag it and adjust your answer.)";
		//"(You may release the bar at any time)",
	
	private static final String PRESS_ENTER_TO_FINALIZE =
		"-) When ready press the \"Enter\" key to finalize your answer and move to next round.";
	
	private static final String RANDOM_TONES_PLAY =
		"(random tones will play then the process will repeat)";
	
	private static final String LISTEN_TO_FIRST_TONE =
		"1) Press and hold the \"1\" key to listen to the first tone in the interval.";
	
	private static final String LISTEN_TO_SECOND_TONE =
		"2) When ready, release the \"1\" key then hold the \"2\" key to listen to the second tone.";
	
	private static final int BOX_ADJUST = 245;
	
	//
	//	PASSIVE PITCH CONSTANTS 
	//
    public static final Dimension PP_INST_BOX = new Dimension(250,250);
	public static final Dimension PP_WINDOW = new Dimension(305,PP_INST_BOX.height + BOX_ADJUST);
	public static final int[] PP_INST_BOLDS = {0,1,3,5}; //the index of the instruction lines which will be 'bold' when appropriate.
	public static final int[] PP_INST_ITALIC = {2,4,6}; //the index of the instruction lines which will be 'small_italic' when appropriate.
	public static final int PP_CAN_SET_ANS_PLACE = 5;
    public static final String[] PP_INSTRUCTIONS = 	
    {
			//0:	BOLD
    		PRESS_ENTER_TO_START,
    		//1:	BOLD
			"1) Listen to the continueous tone played, When ready"+
			" click and hold the black bar to answer.",
			//2:	ITAL
			"(Left-click and hold the black bar with your "+	
			"mouse. This will allow you to move it.)",
			//3:	BOLD
			"2) Drag the bar to the note being played.",
			//4:	ITAL
			MAY_RELEASE_MOUSE,
			//5:	SPEC BOLD
			PRESS_ENTER_TO_FINALIZE,
			//6:	ITAL
			RANDOM_TONES_PLAY
    };
   
    
    //
    //	ACTIVE PITCH CONSTATNS
    //
    public static final Dimension AP_INST_BOX = new Dimension(250,270);
    public static final Dimension AP_WINDOW = new Dimension(305,AP_INST_BOX.height + BOX_ADJUST);
	public static final int[] AP_INST_BOLDS = {0,1,3}; //the index of the instruction lines which will be 'bold' when appropriate.
	public static final int[] AP_INST_ITALIC = {2,4,5,7}; //the index of the instruction lines which will be 'small_italic' when appropriate.
	public static final int AP_CAN_SET_ANS_PLACE = 6;
	public static final String[] AP_INSTRUCTIONS = 	
    {
			//0:	BOLD
			PRESS_ENTER_TO_START,
			//1:	BOLD
			"1) Click and hold the black bar to answer.",
			//2:	ITAL
			"(Left-click and hold the black bar with your mouse. "+	
			"This will allow you to move it. As you slide the bar "+
			"the current tone being played will change.)",
			//3:	BOLD
			"2) Drag the bar until you find the requested note",
			//4:	ITAL
			"(The requested note will be displayed in the window's title.)",
			//5:	ITAL
			MAY_RELEASE_MOUSE,
			//6:	SPEC BOLD
			PRESS_ENTER_TO_FINALIZE,
			//7:	ITAL
			RANDOM_TONES_PLAY
	};
    
	
	//
	//	PASSIVE RELATIVE CONSTATNS
	//
    public static final Dimension PR_INST_BOX = new Dimension(250,330);
	public static final Dimension PR_WINDOW = new Dimension(305,PR_INST_BOX.height + BOX_ADJUST);
	public static final int[] PR_INST_BOLDS = {0,1,2,4,6}; //the index of the instruction lines which will be 'bold' when appropriate.
	public static final int[] PR_INST_ITALIC = {3,5,7}; //the index of the instruction lines which will be 'small_italic' when appropriate.
	public static final int PR_CAN_SET_ANS_PLACE = 8;
    public static final String[] PR_INSTRUCTIONS = 	
    {
    		//0: 	BOLD
    		PRESS_ENTER_TO_START,
			//1:	BOLD
			LISTEN_TO_FIRST_TONE,
			//2:	BOLD
			LISTEN_TO_SECOND_TONE,
			//3: 	ITAL
			"(this is the second tone in the interval. you may repeat "+
			"listening to the interval as many times as you like.)",
			//4:	BOLD
			"3) Click and hold the black bar to answer.", 	
			//5:	ITAL
			"(Left-click and hold the black bar with your mouse. "+							
			"This will allow you to move it.)",	
			//6:	BOLD
			"4) Drag the bar to the interval played to select your answer",
			//7:	ITAL
			MAY_RELEASE_MOUSE,
			//8:	SPEC BOLD
			PRESS_ENTER_TO_FINALIZE
	};
    
    
    //
    //	ACTIVE RELATIVE CONSTANTS
    //
    public static final Dimension AR_INST_BOX = new Dimension(250,335);
    public static final Dimension AR_WINDOW = new Dimension(305,AR_INST_BOX.height + BOX_ADJUST);
	public static final int[] AR_INST_BOLDS = {0,1,2,4}; //the index of the instruction lines which will be 'bold' when appropriate.
	public static final int[] AR_INST_ITALIC = {3,5,6}; //the index of the instruction lines which will be 'small_italic' when appropriate.
	public static final int AR_CAN_SET_ANS_PLACE = 7;
    public static final String[] AR_INSTRUCTIONS = 	
    {
    		//0: 	BOLD
    		PRESS_ENTER_TO_START,	
    		//1:	BOLD
    		LISTEN_TO_FIRST_TONE,	
			//2:	BOLD
			LISTEN_TO_SECOND_TONE,
			//3:	ITAL
			"(this will be the second tone in the interval which "+
			"you will set, but always starts at the same pitch as "+
			"the first tone)",
			//4:	BOLD
			"3) Press and hold the mouse button anywhere in the window to make the bar "+//"appear, then drag the bar to answer.",
			"appear then drag the bar to make the requested interval displayed in the window's title.",
			//5:	ITAL
			"(you may repeat listening to the first and second tones as needed)",
			//6:	ITAL
			MAY_RELEASE_MOUSE, //"(The requested interval is displayed in the window's title)",	
			//7:	SPEC BOLD
			PRESS_ENTER_TO_FINALIZE,
			//8:	NORM
			"Note: Unless specified in the main window, only "+
			"increasing intervals should be set."
	};
	
    
    
    
    
   
}
