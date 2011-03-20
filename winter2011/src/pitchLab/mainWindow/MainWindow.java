package pitchLab.mainWindow;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.JComboBox;

import common.EntryBox;
import common.GUIMethods;

import pitchLab.pianoWindow.PianoWindow;
import pitchLab.reference.Constants;
import pitchLab.reference.DynmVar;

import server.ServerSideLaunch;
import sound.Sine;
import sound.SineContinuous;

/**
 * The MainWindow class produces the main GUI window for the PitchLab application.
 *
 * @author Gavin Shriver
 * @version 0.6 April 9, 2009
 *
 */

public class MainWindow extends JPanel implements ActionListener, ItemListener
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/********************************************************************************
	 * Begin Variables
	 ********************************************************************************/

	
	private EntryBox minOctave = new EntryBox(6, EntryBox.INT, "Lowest Octave (starting at C0 = 16.35hz): ", Sine.getMinOctave());
	private EntryBox numberOfOctaves = new EntryBox(6, EntryBox.INT, "Number Of Octaves: ", Sine.getNumberOfOctaves());
	private EntryBox minToneSeperation = new EntryBox(6, EntryBox.DOUBLE, "Minimum Frequency difference (Hz):", Sine.getMinFreqSeperation());
	
	//ACTIVE Relative mode crap
	private EntryBox ARScaleMinOctaveInterval = new EntryBox(6, EntryBox.DOUBLE, "AR window scaling, minimum interval (octaves):", DynmVar.ARScaleMinOctaveInterval);
	private EntryBox ARScaleMaxOctaveInterval = new EntryBox(6, EntryBox.DOUBLE, "AR window scaling, maximum interval (octaves):", DynmVar.ARScaleMaxOctaveInterval);

	//RANDOM TONE SETUP // FILLER SETUP
	private EntryBox numRandTones = new EntryBox(6, EntryBox.INT, "Number of random tones: ", Sine.getNumRandTones());
	private EntryBox minToneTime = new EntryBox(6, EntryBox.DOUBLE, "Minimum length of tone (seconds): ", Sine.getMinToneTime());
	private EntryBox maxToneTime = new EntryBox(6, EntryBox.DOUBLE, "Maximum length of tone (seconds): ", Sine.getMaxToneTime());
	private EntryBox minRestTime = new EntryBox(6, EntryBox.DOUBLE, "Minimum seperation between tones (seconds): ", Sine.getMinRestTime());
	private EntryBox maxRestTime = new EntryBox(6, EntryBox.DOUBLE, "Maximum seperation between tones (seconds): ", Sine.getMaxRestTime());
	
	//finite value
	private Checkbox finiteRands = new Checkbox("Use Finte Tone Times: ", Sine.getFiniteTimes());
	private EntryBox finiteToneTime = new EntryBox(6, EntryBox.DOUBLE, "Random note length (seconds): ", Sine.getFiniteToneTime(),		false);
	private EntryBox finiteRestTime = new EntryBox(6, EntryBox.DOUBLE, "Seperation between Random notes (seconds): ",Sine.getFiniteRestTime(), false);	
	
	
	
	//---------------------------------------------
	// - Mail Options Panel
	//---------------------------------------------
//	--- TODO : (mail stuff)
//	private EntryBox emailAddy = new EntryBox(20, EntryBox.STRING, "Email: ");	
//	private JList emailList= new JList(new String[] {});
//	private JButton addEmail = new JButton("Add");
//	private JButton removeEmail = new JButton("Remove");
//	private ArrayList<String> emails = new ArrayList<String>();

	
	//---------------------------------------------
	// - ADVANCED SOUND SYSTEM SETTINGS
	//---------------------------------------------
	private EntryBox sine_sampleRate = new EntryBox(6, EntryBox.INT, "Sample Rate (48k max): ", Sine.getSampleRate());	
	private EntryBox sine_sampleSizeInBits = new EntryBox(6, EntryBox.INT, "Sample Size (bits): ",Sine.getSampleSizeInBits());	
	private EntryBox sine_lineBufferSize = new EntryBox(6, EntryBox.INT, "Line Buffer Size (bytes): ",Sine.getLineBufferSize());	
	private EntryBox sine_amplitude = new EntryBox(6, EntryBox.DOUBLE, "Amplitude (%): ", Sine.getAmplitude());	
	private Checkbox sine_useDynAmp = new Checkbox("Use Dynamic Amplitudes", Sine.getUseDynAmp());

	
	private EntryBox sineC_sampleRate = new EntryBox(6, EntryBox.INT, "Sample Rate (48k max): ", SineContinuous.getSampleRate());	
	private EntryBox sineC_sampleSizeInBits = new EntryBox(6, EntryBox.INT, "Sample Size (bits): ", SineContinuous.getSampleSizeInBits());	
	private EntryBox sineC_lineBufferSize = new EntryBox(6, EntryBox.INT, "Line Buffer Size (bytes): ", SineContinuous.getLineBufferSize());
	private EntryBox sineC_amplitude = new EntryBox(6, EntryBox.DOUBLE, "Amplitude (%): ", SineContinuous.getAmplitude());	
	private Checkbox sineC_useDynAmp = new Checkbox("Use Dynamic Amplitudes", SineContinuous.getUseDynAmp());
	
	private JButton runAsServer = new JButton("Run As Server");
	
	//---------------------------------------------
	// - FOR PAGE ONE: (MAIN PAGE)
	//---------------------------------------------
	private JButton startButton = new JButton("Start");
	private JButton exitButton = new JButton("Exit");
	
	private JRadioButton active_pitch = new JRadioButton("Active Pitch");
	private JRadioButton passive_pitch = new JRadioButton("Passive Pitch");
	private JRadioButton active_relative = new JRadioButton("Active Relative Pitch");
	private JRadioButton passive_relative = new JRadioButton("Passive Relative Pitch");
	private ButtonGroup modeGroup = new ButtonGroup();
	
    private JComboBox dropdown = new JComboBox(Constants.AVAILABLE_INSTRUMENTS);
    
	private static Checkbox practiceMode = new Checkbox("Practice Mode", false);
	private static Checkbox userSetsCents = new Checkbox("Use Notes +/- Cents ?", false);
	private Checkbox upAndDown_RS = new Checkbox("Identify increasing AND decreasing intervals ?", DynmVar.upAndDown_RS);
	
	private EntryBox numberOfCycles = new EntryBox(6, EntryBox.INT, "Number of Test Cycles: ", 55);
	
	private JRadioButton syncResults = new JRadioButton("I would like to submit my test results.");
	private JRadioButton noSyncResults = new JRadioButton("Do not record data of any kind.");
	private ButtonGroup syncResultsGroup = new ButtonGroup();
	private String privacyPolicy = 	"Only information specific to this test will \n"+
									"be gathered and securely transmitted to our \n"+
									"server. Only personal data deliberately \n"+
									"submitted by the user will be recorded. We  \n" +
									"gather no information about your computer: \n" +
									"hardware, software, operating system, etc.. \n" +
									"This program does not, and is incapable of \n"+
									"reading or writing files to or from your \n" +
									"local hard disks or virtual memory. Data \n"+
									"recovered from this test will be used \n" +
									"purely for scientific research. ";
									
	
	
	private JRadioButton shortWindow = new JRadioButton("Short (low resolution computers)");
	private JRadioButton longWindow = new JRadioButton("Long (high resolution computers)");
	private ButtonGroup windowWidth = new ButtonGroup();
	//---------------------------------------------
	// - FOR PAGE TWO: (SETTINGS PAGE)
	//---------------------------------------------
	private JButton applyButton = new JButton("Apply");
	private JButton resetButton = new JButton("Reset");
	private JButton defaultsButton = new JButton("Load Defaults");

	JTabbedPane tabbedPane = new JTabbedPane(); ///MAIN TABBED PANE (W/ LISTENER)
	JTabbedPane settingsTabs = new JTabbedPane();
	
	/********************************************************************************
	 * END Variables
	 ********************************************************************************/
	
    /** 
     * This GUI window is a JPanel with a two-tabbed interface: Main and Settings.
     * The class defines the layout of the window along with the text, fields and options
     * available when launching PitchLab.
     * 
     * The class also binds all the event handlers for interacting with buttons. 
     *
     * For future reference - this is where one should change the UI for PitchLab.
     */
	
    public MainWindow() 
	{
        super(new GridLayout(1, 1));
		JComponent mainPage = makeMainPanel();
		

		// --- MAIN Panel
		mainPage.setPreferredSize(new Dimension(420, 480));
        tabbedPane.addTab("Main", null, mainPage, "Select your test, and be tested...");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        
		// --- Settings Panel
		JComponent settings = makeSettingsPanel();
		settings.setPreferredSize(new Dimension(420, 470));
        tabbedPane.addTab("Settings", null, settings, "Change Settings...");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
		
		
        //Add the tabbed pane to this panel.
        add(tabbedPane);
        
        //The following line enables to use scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        
        setToDefaults();
    }
    
	
	/********************************************************************************
	 * END Constructors
	 * Begin (MY) Helpers
	 ********************************************************************************/

    /**
     * Returns the GUI panel for PitchLab for the Main Tab window.
     * Attaches the action listeners to the GUI for user interactivity and 
     * applies the buttons and labels to the panel itself.
     */
    private JComponent makeMainPanel()
    {
    	startButton.addActionListener(this);
    	exitButton.addActionListener(this);
    	
    	passive_pitch.addActionListener(this);
    	active_pitch.addActionListener(this);
    	passive_relative.addActionListener(this);
    	active_relative.addActionListener(this);
    	practiceMode.addItemListener(this);
    	
    	modeGroup.add(active_pitch);
    	modeGroup.add(passive_pitch);
    	modeGroup.add(active_relative);
    	modeGroup.add(passive_relative);
    	    	
    	syncResultsGroup.add(syncResults);
    	syncResultsGroup.add(noSyncResults);
    	JLabel aboutSync = new JLabel("       (view Privacy Policy)");
    	aboutSync.setForeground(Color.BLUE);
    	aboutSync.addMouseListener(new MouseAdapter() 
    		{ 
            	public void mousePressed(MouseEvent me) 
            	{ 
            		JOptionPane.showMessageDialog(null, privacyPolicy,"Privacy Policy",JOptionPane.PLAIN_MESSAGE);
            	} 
            });
    	
    	
    	windowWidth.add(shortWindow);
    	windowWidth.add(longWindow);
    	
    	JPanel panel = new JPanel();
    	panel.setLayout(new BorderLayout());
    	panel.add(new JLabel("<html><b></b></html>"),BorderLayout.NORTH);
    	
    	/*JPanel buttPan = new JPanel(new FlowLayout());
    	buttPan.add(startButton);
    	buttPan.add(exitButton);*/
    	panel.add(GUIMethods.flowMaker(startButton, exitButton) ,BorderLayout.SOUTH);
  
    	JPanel buttPan2 = new JPanel(new GridLayout(0,1));
    	buttPan2.add(new JLabel(""));
    	buttPan2.add(new JLabel("<html><b>Select Test Type:</b></html>"));
    	buttPan2.add(passive_pitch);
    	buttPan2.add(active_pitch);
    	buttPan2.add(passive_relative);
    	buttPan2.add(active_relative);
    	buttPan2.add(new JLabel(""));
    	buttPan2.add(new JLabel("<html><b>Select Instrument Type:</b></html>")); 
    	buttPan2.add(dropdown);
     	buttPan2.add(new JLabel(""));
    	buttPan2.add(new JLabel("<html> <b>Test Options:</b></html>"));
    	buttPan2.add(practiceMode);
    	buttPan2.add(userSetsCents);
    	buttPan2.add(upAndDown_RS);
    	buttPan2.add(numberOfCycles);
    	buttPan2.add(new JLabel(""));
    	buttPan2.add(new JLabel("<html> <b> Test Window Size:</b></html>"));
    	buttPan2.add(longWindow);
    	buttPan2.add(shortWindow);
    	buttPan2.add(new JLabel(""));
    	buttPan2.add(new JLabel("<html> <b>Results Submission:</b></html>"));
    	buttPan2.add(syncResults);
    	buttPan2.add(aboutSync);
    	buttPan2.add(noSyncResults);
    	buttPan2.add(new JLabel(""));

    	
    	panel.add(buttPan2, BorderLayout.CENTER);
   
    	return panel;
    }
    
	
    /**
     * Returns the GUI panel for PitchLab for Settings Tab window.
     * Attaches the action listeners to the GUI for user interactivity and 
     * applies the buttons and labels to the panel itself.
     */
	//----------------------------------- Settings Pannel
	private JComponent makeSettingsPanel()
	{
		// --- makes panels
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		
		settingsTabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		// --- end make panels
		
		
		// --- Makes Buttons:
		// actions for checkboxes
		finiteRands.addItemListener(this);
		
		// actions for button clicked
		applyButton.addActionListener(this);
		resetButton.addActionListener(this);
		defaultsButton.addActionListener(this);
		dropdown.addActionListener(this);
		
		// adds buttons to button panel
		buttonPanel.add(applyButton);
		buttonPanel.add(resetButton);
		buttonPanel.add(defaultsButton);
		
		//adds button panel to settings panel
		panel.add(buttonPanel, BorderLayout.SOUTH);
		// --- end Makes Buttons
		
		// TODO (mail stuff)
		// --- Makes and Adds Tabs
		//makes setting pages
		JComponent testSettings = createTestSettings();
//		JComponent mailSettings = createMailSettings();
		JComponent advancedSoundSettings = createAdvancedSoundSettings();
//		JComponent advancedSettings = new createAdvancedSettings();
		
		// --- adds pages to tabs 
		settingsTabs.addTab("General", null, testSettings, "Change Test Settings");
		settingsTabs.setMnemonicAt(0, KeyEvent.VK_1);
//		settingsTabs.addTab("Mail", null, mailSettings, "Change Mail & SMTP Settings");
//		settingsTabs.setMnemonicAt(2, KeyEvent.VK_3);
		settingsTabs.addTab("Advanced Sound", null, advancedSoundSettings, "Change advanced sound parameters (expert only)");
		settingsTabs.setMnemonicAt(1, KeyEvent.VK_3);

		
		
		// --- adds tabed pane to settings panel
		panel.add(settingsTabs, BorderLayout.CENTER);

		intitializeAll();
		return panel;
	}	

    /**
     * Returns the GUI panel for PitchLab for the Advanced Settings tab window.
     * Attaches the action listeners to the GUI for user interactivity and 
     * applies the buttons and labels to the panel itself.
     */
	protected JComponent createAdvancedSoundSettings()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,1));
		runAsServer.addActionListener(this);
		
		panel.add(new JLabel(""));
		panel.add(new JLabel("                      DISCRETE SINE SETTINGS: "));
		panel.add(sine_sampleRate);
		panel.add(sine_sampleSizeInBits);
		panel.add(sine_lineBufferSize);
		panel.add(sine_amplitude);
		panel.add(sine_useDynAmp);
		panel.add(new JLabel(""));
		panel.add(new JLabel("                      CONTINUOUS SINE SETTINGS: "));
		panel.add(sineC_sampleRate);
		panel.add(sineC_sampleSizeInBits);
		panel.add(sineC_lineBufferSize);
		panel.add(sineC_amplitude);
		panel.add(sineC_useDynAmp);
		panel.add(new JLabel(""));
		panel.add(new JLabel(""));
		panel.add(new JLabel(""));
		panel.add(new JLabel(""));
		panel.add(runAsServer);
		
		
		return panel;
	}
	

	
	/*
	//----------------------------------- Mail Settings Panel
	 * this is unimplemented for favor of web distribution and "live" communication
	 * but I will leave the methods in the case of a completely stand-alone app.
	 * 		// TODO (mail stuff)
	protected JComponent createMailSettings()
	{
		// --- makes panels
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2,1));
		
		JPanel smtpSettingsPanel = new JPanel();
		smtpSettingsPanel.setLayout(new GridLayout(0,1));
		
		JPanel mailListPanel = new JPanel();
		mailListPanel.setLayout(new BorderLayout());
		
		// --- adding smtp fields to panel
		smtpSettingsPanel.add(new JLabel("                                 SMTP Mail Settings"));
		smtpSettingsPanel.add(new JLabel("Add additional recipients to the test results"));
		smtpSettingsPanel.add(new  JLabel("by adding to the box below: "));
		
		panel.add(smtpSettingsPanel);

		
		
		//---Adds email entry box to panel
		//makes subPanel to add button and textfield
		
		JPanel subPanel = new JPanel();
		subPanel.setLayout(new GridLayout(0,1));
		
		//adds listeners to buttons
//		addEmail.addActionListener(this);
//		removeEmail.addActionListener(this);
		
		//adds to subPanel
//		subPanel.add(addEmail);
//		subPanel.add(removeEmail);

		//adds pieces to mailList 
//		mailListPanel.add(emailAddy, BorderLayout.NORTH);
//		mailListPanel.add(emailList, BorderLayout.CENTER);
//		mailListPanel.add(subPanel, BorderLayout.EAST);
		
		panel.add(mailListPanel);
		
		setBoxGrays();
		return panel;
	}//END createMailSettings
	
	*/
	
	//----------------------------------- create Test Settings Panel
	private JComponent createTestSettings()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,1));
		
		panel.add(new JLabel("General Settings: "));
		panel.add(minOctave);
		panel.add(numberOfOctaves);
		panel.add(minToneSeperation);
		panel.add(ARScaleMaxOctaveInterval);
		panel.add(ARScaleMinOctaveInterval);
		panel.add(new JLabel("Random Tone Generation Settings: "));
		panel.add(numRandTones);
		panel.add(minToneTime);
		panel.add(maxToneTime);
		panel.add(minRestTime);
		panel.add(maxRestTime);
		panel.add(finiteRands);
		panel.add(finiteToneTime);
		panel.add(finiteRestTime);
			
		return panel;
	}//END createTestSettings();
	
	
	
	/********************************************************************************
	 * END (MY) Helpers
	 * Begin universal functions (setting resetting defaults)
	 ********************************************************************************/
	private void intitializeAll()
	{
		//TODO mail stuff
//		emails.clear();
//		addArrayToEmails(MailUtil.emailList);
//		emailList.setListData(emails.toArray());
		setBoxGrays();
	}
	
	
	private void setSettings()
	{		
		//PianoWindow.setUpAndDown_RS(upAndDown_RS.getState());
		//PianoWindow.setARScaleMinOctaveInterval((Double)ARScaleMinOctaveInterval.getValue());
		//PianoWindow.setARScaleMaxOctaveInterval((Double)ARScaleMaxOctaveInterval.getValue());
		
		DynmVar.upAndDown_RS = upAndDown_RS.getState();
		DynmVar.ARScaleMinOctaveInterval = ((Double)ARScaleMinOctaveInterval.getValue());
		DynmVar.ARScaleMaxOctaveInterval = ((Double)ARScaleMaxOctaveInterval.getValue());
		
		//        Standard SETTINGS
		// - octave info;
		Sine.setNumRandTones((Integer)numRandTones.getValue());
		Sine.setMinOctave(((Integer)minOctave.getValue()));
		Sine.setNumberOfOctaves((Integer)numberOfOctaves.getValue());
		Sine.setMinFreqSeperation((Double)minToneSeperation.getValue());
		
		// - tone times
		Sine.setFiniteToneTime((Double)finiteToneTime.getValue());
		Sine.setMinRestTime((Double)minRestTime.getValue());
		Sine.setMaxToneTime((Double)maxToneTime.getValue());
		
		// - rest times
		Sine.setFiniteRestTime((Double)finiteRestTime.getValue());
		Sine.setMinRestTime((Double)minRestTime.getValue());
		Sine.setMaxRestTime((Double)maxRestTime.getValue());
		Sine.setFiniteTimes(finiteRands.getState());
		
		//            MAIL SETTINGS
//		MailUtil.setEmailList(getEmailsInArray()); TODO (mail stuff)

		
		
		//             ADVANCED SOUND SETTINGS		
		Sine.setSampleRate((Integer)sine_sampleRate.getValue());
		Sine.setSampleSizeInBits((Integer)sine_sampleSizeInBits.getValue());
		Sine.setLineBufferSize((Integer)sine_lineBufferSize.getValue());
		Sine.setAmplitude((Double)sine_amplitude.getValue());
		Sine.setUseDynAmp(sine_useDynAmp.getState());
		
		SineContinuous.setSampleRate((Integer)sineC_sampleRate.getValue());
		SineContinuous.setSampleSizeInBits((Integer)sineC_sampleSizeInBits.getValue());
		SineContinuous.setLineBufferSize((Integer)sineC_lineBufferSize.getValue());
		SineContinuous.setAmplitude((Double)sineC_amplitude.getValue());
		SineContinuous.setUseDynAmp(sineC_useDynAmp.getState());
	}

	
    /**
     * Sets the settings back to the defaults.
     */
	private void setToDefaults()
	{
	//	MailUtil.setDefaults();
		Sine.setDefaults();
		SineContinuous.setDefaults();
		DynmVar.setDefaults();
		dropdown.setEnabled(true);
		
		resetSettings();
	}

	
    /**
     * Resets the settings.
     */
	private void resetSettings()
	{
		//        MAIN WINDOW SETTINGS
		numberOfCycles.setTo(55);
		userSetsCents.setState(false);
		upAndDown_RS.setState(DynmVar.upAndDown_RS);
		windowWidth.setSelected(shortWindow.getModel(), true );
		syncResultsGroup.setSelected(syncResults.getModel(), true);
		modeGroup.setSelected(passive_pitch.getModel(), true);

		//        STANDARD SETTINGS
		numRandTones.setTo(Sine.getNumRandTones());
		minOctave.setTo(Sine.getMinOctave());
		numberOfOctaves.setTo(Sine.getNumberOfOctaves());
		
		ARScaleMinOctaveInterval.setTo(DynmVar.ARScaleMinOctaveInterval);
		ARScaleMaxOctaveInterval.setTo(DynmVar.ARScaleMaxOctaveInterval);
		
		minToneTime.setTo(Sine.getMinToneTime());
		maxToneTime.setTo(Sine.getMaxToneTime());
		minRestTime.setTo(Sine.getMinRestTime());
		maxRestTime.setTo(Sine.getMaxRestTime());
		finiteRands.setState(Sine.getFiniteTimes());
		finiteToneTime.setTo(Sine.getFiniteToneTime());
		finiteRestTime.setTo(Sine.getFiniteRestTime());
		
		// TODO (mark mail)
		//      MAIL SETTINGS
//		emails.clear();
//		addArrayToEmails(MailUtil.emailList);
//		emailList.setListData(emails.toArray());
		
		
		//      ADVANCED SOUND SETTINGS
		sine_sampleRate.setTo(Sine.getSampleRate());
		sine_sampleSizeInBits.setTo(Sine.getSampleSizeInBits());
		sine_lineBufferSize.setTo(Sine.getLineBufferSize());
		sine_amplitude.setTo(Sine.getAmplitude());
		sine_useDynAmp.setState(Sine.getUseDynAmp());

		sineC_sampleRate.setTo(SineContinuous.getSampleRate());
		sineC_sampleSizeInBits.setTo(SineContinuous.getSampleSizeInBits());
		sineC_lineBufferSize.setTo(SineContinuous.getLineBufferSize());
		sineC_amplitude.setTo(SineContinuous.getAmplitude());
		sineC_useDynAmp.setState(SineContinuous.getUseDynAmp());
	}

	
	

	
	
	/********************************************************************************
	 * END Global Getter/Setters
	 * Begin Local Getter/Setters
	 ********************************************************************************/
	
	/*	private void addArrayToEmails(String[] emailList)
	{
		for (int i =0; i < emailList.length; i++)
		{
			emails.add(emailList[i]);
		}
	}
	
	private String[] getEmailsInArray()
	{
		String[] emailz = new String[emails.size()];
		
		for (int i =0; i < emails.size(); i++)
		{
			emailz[i]= (String)emails.get(i);
 		}
		return emailz;
	}	*/
	
    /**
     * Set the window width based on 'Test Window Size' settings from the Main window 
     * Long Window is defined for 'High resolution screens'
     * Short Window is defined for 'Low resolution screens'
     */
	private void setWindowWidth()
	{
		if( windowWidth.getSelection() == longWindow.getModel())
			DynmVar.setWindow_Width(Constants.LONG_WINDOW_WIDTH);
		else
			DynmVar.setWindow_Width(Constants.SHORT_WINDOW_WIDTH);
		
	}
	
    /**
     * Gathers the settings and opens the Piano Window to begin a PitchLab test.
     * Settings include whether or not to sync the results and window width.
     */
	private void starter()
	{
        System.out.println("Starting PitchLab Test");
		DynmVar.upAndDown_RS = (upAndDown_RS.getState());
		DynmVar.userSetCents = (userSetsCents.getState());
		
		if (syncResultsGroup.getSelection() == syncResults.getModel())
			DynmVar.syncResults = (true);
		else if (syncResultsGroup.getSelection() == noSyncResults.getModel())
			DynmVar.syncResults = (false);
		
		setWindowWidth();
		
		SwingUtilities.invokeLater(new Runnable()
		{
            public void run()
            {
				new PianoWindow(getMode(), (Integer)numberOfCycles.getValue());
            }
        });
	}
	
	
	private void setBoxGrays()
	{
		if(finiteRands.getState())
		{
			finiteToneTime.setEnabled(true);
			finiteRestTime.setEnabled(true);
		}
		else
		{
			finiteToneTime.setEnabled(false);
			finiteRestTime.setEnabled(false);
		}

		if(modeGroup.getSelection() == passive_pitch.getModel()) {
			dropdown.setEnabled(true);
		} else {
			dropdown.setEnabled(false);
			DynmVar.instrument = "Sine";
		}
			
		 if (modeGroup.getSelection() == active_relative.getModel() || modeGroup.getSelection() == active_pitch.getModel())
			 userSetsCents.setEnabled(true);
		 else
			 userSetsCents.setEnabled(false);
		 
		 if (modeGroup.getSelection() == passive_relative.getModel() || modeGroup.getSelection() == active_relative.getModel())
			 upAndDown_RS.setEnabled(true);
		 else 
			 upAndDown_RS.setEnabled(false);
	}
	
    /**
     * Gathers the selected value from the 'Select Test Type' radio buttons.
     */
	private int getMode()
	{
		int mode = 0;
		if (modeGroup.getSelection()== passive_pitch.getModel())
			mode = Constants.PASSIVE_PITCH;
		else if (modeGroup.getSelection()== active_pitch.getModel())
			mode = Constants.ACTIVE_PITCH;
		else if (modeGroup.getSelection()== passive_relative.getModel())
			mode = Constants.PASSIVE_RELATIVE;
		else if (modeGroup.getSelection()== active_relative.getModel())
			mode = Constants.ACTIVE_RELATIVE;
		
		if(practiceMode.getState())
			mode+=10;
		
		return mode;
	}
	
    // END Local Getter/ Setters
    // Begin Event Handlers
	
    /**
     * Sets the BoxGrays and number of cycles to be performed in the Piano Window.
     * The number of cycles is hard-coded here and has two different values: 
     * 10 if PitchLab is in practice mode and 55 if PitchLab is not in practice mode.
     *
     * @param e An <code>ItemEvent</code> that contains the Practice Mode boolean. 
     *          If the boolean is true, PitchLab will adjust itself to accommodate 
     *          the user preferences.  
     *
     */
    public void itemStateChanged(ItemEvent e) 
    {
       setBoxGrays();
       
       if (practiceMode == e.getSource())
       {
	       if(practiceMode.getState())
	       {
	    	   numberOfCycles.setTo(10);
	    	   numberOfCycles.setEnabled(false);
	       }
	       else
	       {
	    	   numberOfCycles.setTo(55);
	    	   numberOfCycles.setEnabled(true);
	       }
       }
       
    }
	
	/**
	 * Sets the action that will be performed based on the event parameter.
     * 
     * @param e The type of action that governs what the method will do.
	 */
	public void actionPerformed(ActionEvent e) 
	{		
		
        if(dropdown == e.getSource())
        {
            DynmVar.instrument = (String)dropdown.getSelectedItem();
        } 
        else if (applyButton == e.getSource())
		{
			setSettings();
		}
		else if(resetButton == e.getSource()) 
		{
			resetSettings();
		}
		else if(defaultsButton == e.getSource()) 
		{
			setToDefaults();
		}
		// TODO  (mail stuff)
/*		else if (addEmail == e.getSource())
		{
			emails.add((String)emailAddy.getValue());
			emailList.setListData(emails.toArray());
			emailAddy.setTo("");

		}
		else if (removeEmail == e.getSource())
		{
			emails.remove(emailList.getSelectedIndex());
			emails.trimToSize();
			emailList.setListData(emails.toArray());
		}
		else if (browseButton == e.getSource())
		{
			setFilePath();
		}*/
		
		else if (startButton == e.getSource())
		{
			starter();
		}
		else if (exitButton == e.getSource())
		{
			System.exit(0);
		}
		else if (runAsServer == e.getSource())
		{
			switchToServer();
		}
		else
			setBoxGrays();
		
	}
	
	private void switchToServer()
	{
		//FIXME: MARKER, NEED TO KILL MAIN WINDOW 
		ServerSideLaunch server = new ServerSideLaunch();
		server.setVisible(true);
	}
	
	/********************************************************************************
	 * END Event Handlers
	 * Begin MAIN
	 ********************************************************************************/

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from
     * the event dispatch thread.
     */
    private static void createAndShowGUI() 
	{
        //Create and set up the window.
        JFrame frame = new JFrame("Pitch Lab Starter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Add content to the window.
        frame.add(new MainWindow(), BorderLayout.CENTER);
        
        //Display the window.
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    /**
     * Create and run the application GUI.
     * Globally disables bold fonts from the UI as well.
     */
    public static void main(String[] args) 
    {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() 
        {
            public void run() 
            {
                //Turn off metal's use of bold fonts
            	UIManager.put("swing.boldMetal", Boolean.FALSE);
            	createAndShowGUI();
            }
        });
    }
}
