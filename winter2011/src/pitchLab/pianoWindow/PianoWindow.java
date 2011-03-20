package pitchLab.pianoWindow;

import java.io.*;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


import pitchLab.instructWindow.InstructionWindow;
import pitchLab.modes.activePitch.ActivePitchControl;
import pitchLab.modes.activeRelative.ARCPractice;
import pitchLab.modes.activeRelative.ActiveRelativeControl;
import pitchLab.modes.passivePitch.PassivePitchControl;
import pitchLab.modes.passiveRelative.PRCPractice;
import pitchLab.modes.passiveRelative.PassiveRelativeControl;
import pitchLab.reference.Calculations;
import pitchLab.reference.Constants;
import pitchLab.reference.DynmVar;

import common.FileUtilClient;

import sound.*;
//import sound.Sine;
//import sound.SineContinuous;

import jm.util.*;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;



/**
 * Generates the Piano Window used during PitchLab tests
 *
 * @author Gavin Shriver
 * @version 0.6 April 7, 2009
 * 
 *
 * TODO:
 * Fix file writing... not outputting the correct "user selected" in active mode
 *  
 * TODO:
 * Passive relative training has issue with timer.. its off.. not producing milliseconds
 *
 */

public class PianoWindow  extends JFrame implements WindowListener
{

    /**
     * Gives a margin on the bar that the user drags across the piano so 
     * the user doesn't have to click the exact pixel but instead 
     * has a bit of leeway. 
     *
     * @param mouseX The x-coordinate of the the mouse with respect to the Piano Window
     */
	public static boolean isMouseWithinDragBarMargin(int mouseX)
	{
		return DynmVar.dragBarX - Constants.DRAG_BAR_MARGIN <= mouseX && mouseX <= DynmVar.dragBarX + Constants.DRAG_BAR_MARGIN;
	}
	
	private static final long serialVersionUID = 1L;

	public FileUtilClient dataHardCopy;
	//private static final String HOST = "acoustix.mine.nu";
	//private Registry registry;// = LocateRegistry.getRegistry(HOST);
	//private Communicate stub;// = (Communicate) registry.lookup("Communicate");
	
	private boolean dragBarVisible = true;
	
	private Sine sineWave = new Sine();
	public SineContinuous contSine = new SineContinuous();

	public Instruments instrumentName = new Instruments();

	
	private PianoWindowListener listener;	
	public InstructionWindow instruct;
	public GToolTip toolTip;

	
    /**
     * Open the Piano Window and set its visibility to true
     */
	public static void main (String args[])
	{	
		DynmVar.syncResults = false;
		PianoWindow drawing = new PianoWindow(Constants.AR_PRACTICE, 14);
		drawing.setVisible(true);
	}
	
	
    /**
     * Initializes the piano window with appropriate settings
     * based on the mode PitchLab is currently in
     *
     * @param mode The mode PitchLab is currently in
     * @param cycles The number of test cycles that will be performed
     */
	public PianoWindow(int mode, int cycles)
	{
		System.out.println("Open Piano Window");

		DynmVar.mode = mode;
		DynmVar.cycles = cycles;
		
	    
		if (mode > 10)
			DynmVar.syncResults = false;
		
		initializeVariables(); 
		initializeWindow();
		initializeListeners();
			
		this.setLocationRelativeTo(null);
		
		this.getContentPane();
		
		this.setResizable(false);
	
		this.setVisible(true);

		
		if(mode > 10)
		{
			instruct.setLocation(this.getLocationOnScreen().x+this.getWidth() +20, 
					this.getLocationOnScreen().y- (int)(instruct.getHeight()-this.getHeight())/2);
			instruct.setVisible(true);
		}
		
		this.toFront();
		this.requestFocus();

		//repaint();
		
	}	

    //	BEGIN constructor helpers
	
    /**
     * Lazyloads an instrument based on a String which is taken from 
     * the 'Drop Down' menu in the main PitchLab window.
     */
    public void lazyLoadInstrument(boolean random) 
    {
		try{
			Object CurrentInstrument = Class.forName("sound." + DynmVar.instrument).newInstance();
//			Object CurrentInstrument = Class.forName("sound.Sine").newInstance();
            System.out.println(DynmVar.instrument + " Instrument Loaded");
            
		} catch ( ClassNotFoundException ex ){
			System.err.println(ex + " Instrument Not Found");
		} catch( InstantiationException ex ){
		      System.err.println( ex + " Interpreter class must be concrete.");
	    }
	    catch( IllegalAccessException ex ){
	      System.err.println( ex + " Interpreter class must have a no-arg constructor.");
	    }
    }

    /**
     * Checks if the user has requested his or her results to be submitted
     * and if so sets up the data to do so.
     */
	private void initializeVariables()
	{
	
		// --- FileUtil init.
		if(DynmVar.syncResults)
		{
			AdditionalInfo userInfo = new AdditionalInfo(null, true);
			userInfo.setVisible(true);
			
			dataHardCopy = new FileUtilClient(DynmVar.mode, userInfo.getName());
			dataHardCopy.setAdditionalInfo(userInfo.getAdditionalInfo());
			dataHardCopy.setTestSettings( makeTestSettingsList() );
		
			userInfo.dispose();
		}
	}
	
    /**
     * Sets the title of the Piano Window before the intial test has started.
     */
	private void initializeWindow()
	{
		this.setTitle(" Press the \"Enter\" key to begin. ");
		//this.getContentPane().setBackground(Color.white);
		this.pack();
	}
	
    /**
     * Initializes the piano window event listeners and adjusts them 
     * accordingly based on what mode PitchLab is in
     */
	private void initializeListeners()
	{		
		//--- Window Listeners (on close)
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(this);
		
		
		this.setSize(DynmVar.window_Width,Constants.WINDOW_HEIGHT);

		//--- Add the mouse listeners.
		switch(DynmVar.mode)
		{
			case Constants.PASSIVE_PITCH:
				//initialize controller:
				listener = new PassivePitchControl(this, false);
				this.add(new PianoPane(DynmVar.window_Width,Constants.WINDOW_HEIGHT));
				this.pack();
				break;
				
			case Constants.ACTIVE_PITCH:
				//initialize controller:
				listener = new ActivePitchControl(this, false);
				this.setSize(DynmVar.window_Width,Constants.WINDOW_HEIGHT);
				break;
				
			case Constants.PASSIVE_RELATIVE:
				//initialize controller:
				listener = new PassiveRelativeControl(this);
				this.add(new IntervalPane(DynmVar.window_Width,Constants.WINDOW_HEIGHT));
				this.pack();
				break;
				
			case Constants.ACTIVE_RELATIVE:
				//initialize controller:
				listener = new ActiveRelativeControl(this);
				this.setSize(DynmVar.window_Width,Constants.WINDOW_HEIGHT);
				
				break;
				
			case Constants.PP_PRACTICE:
				//initialize controller:
				listener = new PassivePitchControl(this, true);
				this.add(new PianoPane(DynmVar.window_Width,Constants.WINDOW_HEIGHT));
				this.pack();
				break;
				
			case Constants.AP_PRACTICE:
				//initialize controller:
				listener = new ActivePitchControl(this, true);
				this.setSize(DynmVar.window_Width,Constants.WINDOW_HEIGHT);
				break;
				
			case Constants.PR_PRACTICE:
				//initialize controller:
				listener = new PRCPractice(this);
				this.add(new PianoPane(DynmVar.window_Width,Constants.WINDOW_HEIGHT));
				this.pack();
				break;
				
			case Constants.AR_PRACTICE:
				//initialize controller:
				listener = new ARCPractice(this);
				this.setSize(DynmVar.window_Width,Constants.WINDOW_HEIGHT);
				break;
		}
		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);
		this.addKeyListener(listener);
        
	}
	

	private String[] makeTestSettingsList()
	{
		String[] testSettings = {
					"Piano Window Settings: , ",
					"Number of test cycles: , " + DynmVar.cycles,
					"User Sets Cents bool: , " + DynmVar.userSetCents,
					"Relative Pitch Up && Down bool: , " + DynmVar.upAndDown_RS,
					"Standard Settings: , ",
					"Number of Random Tones: , " + Sine.getNumRandTones(),
					"Minimum Octave (from C0 = 16.35hz): , " + Sine.getMinOctave(),
					"Number of Octaves: , " + Sine.getNumberOfOctaves(),
					"Minimum Random Tone Time (s): , " + Sine.getMinToneTime(),
					"Maximum Random Tone Time (s): , " + Sine.getMaxToneTime(),
					"Minimum Random Rest Time (s): , " + Sine.getMinRestTime(),
					"Maximum Random Rest Time (s): , " + Sine.getMaxRestTime(),
					"Finite Randoms bool: , " + Sine.getFiniteTimes(),
					"Finite Tone Time (s): , " + Sine.getFiniteToneTime(),
					"Finite Rest Time (s): , " + Sine.getFiniteRestTime(),
					
					"Advanced Sound Settings (Finite Sine): , ",
					"Finite Sine Sample Rate (samp/sec): , " + Sine.getSampleRate(),
					"Finite Sine Sample Size (bits): , " + Sine.getSampleSizeInBits(),
					"Finite Sine Line Buffer Size (bytes): , " + Sine.getLineBufferSize(),
					"Finite Sine Basic Amplitude (%): , " + Sine.getAmplitude(),
					"Finite Sine Use Dynamic Amplitudes bool: , " + Sine.getUseDynAmp(),

					"Advanced Sound Settings (Continuous Sine): , ",
					"Continuous Sine Sample Rate (samp/sec): , " + SineContinuous.getSampleRate(),
					"Continuous Sine Sample Size (bits): , " + SineContinuous.getSampleSizeInBits(),
					"Continuous Sine Line Buffer Size (bytes): , " + SineContinuous.getLineBufferSize(),
					"Continuous Sine Basic Amplitude (%): , " + SineContinuous.getAmplitude(),
					"Continuous Sine Use Dynamic Amplitudes bool: , " + SineContinuous.getUseDynAmp()
					};
		return testSettings;
		
	}
	
    // BEGIN Window Events
	
    /**
     * Set the Piano Window close event
     *
     * @param e The event that triggers when the piano window closes
     */
	public void windowClosing(WindowEvent e)
	{
		exiting();
	}
	
    /**
     * Called when the Piano Window closes and prompts the user with an
     * alert box asking to confirm exiting the test.
     */
	public void exiting()
	{
		int exitAnswer = JOptionPane.showConfirmDialog(null, 
				" Are you sure you want to Exit? ", "Exit?", 
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		Play.stopMidi();
		if (exitAnswer == JOptionPane.YES_OPTION)
			exit();
	}
	
    /**
     * Called after the user has confirmed Exiting the PitchLab test.
     * Ends any sine wave thats playing, resets the count and if the user
     * has selected to submit results, the test results are emailed.
     */
	public void exit()
	{
		contSine.stop();
		contSine.end();
		DynmVar.count = 0; 
		if(DynmVar.syncResults)
		{
			dataHardCopy.setClosedTime();
			dataHardCopy.emailResults();
		}
		this.dispose();
	}
	
	

	
    // BEGIN random methods

    /**
     * Timestamp in milliseconds that is added to the time array used by all PitchLab 
     * modes.
     *
     * @param time The time in milliseconds
     */
	public void timeStamp(long time)
	{		
		dataHardCopy.appendNewLine(Long.toString(time));
    }
    
    /**
     * Plays random sine waves, called at the beginning of each test cycle.
     */
	public void playRands()
	{
		sineWave.play();
	}
    
    /**
     * Returns a string that indicates the total number of test cycles completed so far.
     * This string is shown at the top of the Piano Window and updated after each test.
     */
	public String getCyclesString()
	{
	    return "  ---  Completed "+DynmVar.count+" of "+ DynmVar.cycles +" cycles.";
	}
    

    //	BEGIN GUI METHODS
    
    /**
     * Draws the Piano Window and sets whether the draggable bar should be shown
     * and where it should it placed.
     *
     */
	public void paint(Graphics g)
	{
		// - variable initialization 
		super.paint(g);
		
		//---this is the drag-able mouse bar
		if(dragBarVisible)
		{
			g.setColor(Color.black);
			g.drawLine((int)DynmVar.dragBarX, 0 , (int)DynmVar.dragBarX, Constants.WINDOW_HEIGHT);
		}
		
	}

    /**
     * Sets the visibility boolean of the draggable bar in the Piano Window
     *
     * @param dragBarVisible A boolean indicating whether or not to show the draggable bar
     */
	public void setDragBarVisible(boolean dragBarVisible)
	{
		this.dragBarVisible = dragBarVisible;
	}
    /**
     * Returns the draggable bar visibility state
     */
	public boolean getDragBarVisible()
	{
		return this.dragBarVisible;
	}
	
	//Ignore these events
	public void windowClosed(WindowEvent e) {}		
	public void windowIconified(WindowEvent e) {}	
	public void windowDeiconified(WindowEvent e) {}	
	public void windowActivated(WindowEvent e) {}	
	public void windowDeactivated(WindowEvent e) {}	
	public void windowOpened(WindowEvent e) {}		
	
}
