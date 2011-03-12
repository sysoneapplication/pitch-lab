package pitchLab.pianoWindow;
//
//  PianoWindow.java
//  (this is version 0.6)  
//	
//
//  Created by Gavin Shriver on 4/7/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

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

import sound.Sine;
import sound.SineContinuous;


import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;


/*
 *
 *  
 *  TODO:
 * 	fix file writing... not outputting the correct "user selected"
 *  in active mode
 *  
 *  TODO:
 *  passive relative training has issue with timer.. its off.. not producing miliseconds
 * 
 */


public class PianoWindow  extends JFrame implements WindowListener
{

	public static boolean isMouseWithinDragBarMargin(int mouseX)
	{
		return DynmVar.dragBarX - Constants.DRAG_BAR_MARGIN <= mouseX && mouseX <= DynmVar.dragBarX + Constants.DRAG_BAR_MARGIN;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/****************************************************************************************
	 *	BEGIN Defining Variables 
	 ****************************************************************************************/	
	
	// ------------------------------ MOUSE ACTION ------------------------------ //
	
	// ------------------------------ OUTPUT DEPENDENCIES ------------------------------//
	public FileUtilClient dataHardCopy;
	//private static final String HOST = "acoustix.mine.nu";
	//private Registry registry;// = LocateRegistry.getRegistry(HOST);
	//private Communicate stub;// = (Communicate) registry.lookup("Communicate");
	
	private boolean dragBarVisible = true;
	
	// --- additional classes to call:
	private Sine sineWave = new Sine();
	public SineContinuous contSine = new SineContinuous();
	
	private PianoWindowListener listener;	
	public InstructionWindow instruct;
	public GToolTip toolTip;

	/****************************************************************************************
	 *	END Defining Public Variables 
	 *	BEGIN Main
	 ****************************************************************************************/				
	
	public static void main (String args[])
	{
		DynmVar.syncResults = false;
		PianoWindow drawing = new PianoWindow(Constants.AR_PRACTICE, 14);
		drawing.setVisible(true);
		
	}//end main
	
	
	/****************************************************************************************
	 *	END Main
	 *	BEGIN Constructors
	 ****************************************************************************************/	
	// ---------------------------------------
	//  initializes the piano window
	// with settings appropriate for the mode
	// ---------------------------------------

	public PianoWindow(int mode, int cycles)
	{
			
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

	/****************************************************************************************
	 *	END Constructors
	 *	BEGIN constructor helpers
	 ****************************************************************************************/
	
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
	
	private void initializeWindow()
	{
		this.setTitle(" Press the \"Enter\" key to begin. ");
		//this.getContentPane().setBackground(Color.white);
		this.pack();
	}
	
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
	
	/****************************************************************************************
	 *	END constructor helpers
	 *	BEGIN Window Events
	 ****************************************************************************************/
	
	// ---------------------------------------
	// On attempted window close
	// ---------------------------------------
	public void windowClosing(WindowEvent e)
	{
		exiting();
	}//end windowClosing
	
	public void exiting()
	{
		int exitAnswer = JOptionPane.showConfirmDialog(null, 
				" Are you sure you want to Exit? ", "Exit?", 
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

		if (exitAnswer == JOptionPane.YES_OPTION)
			exit();
	}
	
	public void exit()
	{
		//tieing up loose ends
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
	
	

	
	/****************************************************************************************
	 *	END Window Events
	 *	BEGIN random methods
	 ****************************************************************************************/	


	// ---------------------------------------
	// Stamps the times by adding them to 
	// the appropriate time arrays
	// used by all modes
	// ---------------------------------------
	public void timeStamp(long time) //ms
	{		
		dataHardCopy.appendNewLine(Long.toString(time));//in ms
    }//end timeStamper
    
	
	// ---------------------------------------
	// playRands plays the random tones! 
	// ---------------------------------------
	public void playRands()
	{
		sineWave.play();
	}
    
	
	//
	//	CYCLES STRING
	//
	  public String getCyclesString()
	  {
		  return "  ---  Completed "+DynmVar.count+" of "+ DynmVar.cycles +" cycles.";
	  }
    
	/****************************************************************************************
	 *	END random methods
	 *	BEGIN GUI METHODS
	 ****************************************************************************************/
    
	// ---------------------------------------
	// paint draws the piano graphics
	// ---------------------------------------
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
		
	}//END paint

	public void setDragBarVisible(boolean dragBarVisible)
	{
		this.dragBarVisible = dragBarVisible;
	}
	public boolean getDragBarVisible()
	{
		return this.dragBarVisible;
	}
	/****************************************************************************************
	 *	END GUI methods
	 *	BEGIN ignored things
	 ****************************************************************************************/	
	
	//Ignore these events
	public void windowClosed(WindowEvent e) {}		//Ignore these events
	public void windowIconified(WindowEvent e) {}		//Ignore these events
	public void windowDeiconified(WindowEvent e) {}		//Ignore these events
	public void windowActivated(WindowEvent e) {}		//Ignore these events
	public void windowDeactivated(WindowEvent e) {}		//Ignore these events
	public void windowOpened(WindowEvent e) {}		//Ignore these events
	
}