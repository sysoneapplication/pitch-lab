package pitchLab.instructWindow;


import java.awt.BorderLayout;

import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import pitchLab.pianoWindow.PianoWindow;
import pitchLab.reference.Constants;
import pitchLab.reference.DynmVar;

import java.awt.event.ActionEvent;
import java.util.Scanner;

import jm.util.*;


/**
 * Generates the Instruction Window that loads when Practice Mode is enabled
 *
 * @author Gavin Shriver
 * @version 0.6 April 20, 2009
 */
public class InstructionWindow extends JDialog implements ActionListener, ItemListener 
{
	private static final long serialVersionUID = 1L;
	//private PianoWindow pianoWindow;
	private JPanel mainPan = null;
    private JButton endPractice = null;

    private boolean canSetAnswer = false; //boolean for constant bold of "to answer select _" 
    private int instructionPlace = 0;
    private JTextPane instructionBox = new JTextPane();
    
	private Checkbox showCurrentSel = new Checkbox("Show current selection.", true);
	private JTextField ansToSet = new JTextField(21); //Displays what TO set, or previous TO set.
	private JTextField ansSelected = new JTextField(21); //Displays current Selection or previousSelection

	private String currAnsToSet = "waiting..."; // stores current value to set
	private String currAnsSelected = "waiting..."; //stores currently selected value (this must be updated by pianoWindow)
	private String prevAnsToSet = "none"; //stores previous value to be set
	private String prevAnsSelected = "none"; //stores previously user set answer
    
    private int numCompleted = 0; //number of completed test cycles

	private JTextField roundsCompleted = new JTextField(21); //text field to display number of rounds completed
    
	private StyledDocument doc;

    


   /**
    * Sets up the Instruction window
    */
    public InstructionWindow() 
	{
    	super();
		//super(frame, modal);
		setupWindow();
		
    }
       
   /**
    * Sets up the Instruction window with the Piano
    */
    public InstructionWindow(PianoWindow frame) 
	{
    	super(frame);
		setupWindow();
    }
 
    /**
     * Called by InstructionWindow to build the GUI interface for the
     * Instruction Window that loads in Practic Mode.
     *
     * The window is JPanel and all of its characteristics are constructed 
     * here. The Panel looked different based on whether the practice mode 
     * is in Passive Pitch, Active Pitch, Passive Relative or Active Relative.
     */
    private void setupWindow()
    {

		//    -  setting up pieces & settings:
		
		//=== window settings
		this.setTitle("Instructions");
		
		
		//==== main panel
		mainPan = new JPanel(new BorderLayout());
		JPanel centerPan = new JPanel();
		
		//==== sudo-JLabels
		roundsCompleted.setEditable(false);
		roundsCompleted.setBorder(null);
		roundsCompleted.setDisabledTextColor(Color.black);
		roundsCompleted.setBackground(mainPan.getBackground());
		ansToSet.setEditable(false);
		ansToSet.setBorder(null);
		ansToSet.setDisabledTextColor(Color.black);
		ansToSet.setBackground(mainPan.getBackground());
		ansSelected.setEditable(false);
		ansSelected.setBorder(null);
		ansSelected.setDisabledTextColor(Color.black);
		ansSelected.setBackground(mainPan.getBackground());
		updateFields();	
		
		//==== instructionSet JTextPane
		instructionBox.setEnabled(false);
		instructionBox.setBorder(null);
		instructionBox.setDisabledTextColor(Color.black);
		instructionBox.setMargin(new Insets(0, 0, 0, 0));
		instructionBox.setBackground(mainPan.getBackground());
		doc = instructionBox.getStyledDocument();
		
		addStylesToDocument(doc);		
		
		//==== checkbox
		showCurrentSel.addItemListener(this);
				
		switch (DynmVar.mode)
		{
			case Constants.PP_PRACTICE:
			{
				mainPan.add(new JLabel("<html><b> Passive Pitch Perception </b></html>"),BorderLayout.NORTH);
				//drawInstructions(IWConsts.PP_INSTRUCTIONS,IWConsts.PP_INST_BOLDS,IWConsts.PP_INST_ITALIC);
				redrawInstructions();
				instructionBox.setPreferredSize(IWConsts.PP_INST_BOX);
				this.setPreferredSize(IWConsts.PP_WINDOW);
				break;
			}
			case Constants.AP_PRACTICE:
			{
				mainPan.add(new JLabel("<html><b> Active Pitch Perception </b></html>"),BorderLayout.NORTH);
				//drawInstructions(IWConsts.AP_INSTRUCTIONS,IWConsts.AP_INST_BOLDS,IWConsts.AP_INST_ITALIC);
				redrawInstructions();
				instructionBox.setPreferredSize(IWConsts.AP_INST_BOX);
				this.setPreferredSize(IWConsts.AP_WINDOW);
				break;
			}
			case Constants.PR_PRACTICE:
			{
				mainPan.add(new JLabel("<html><b> Passive Relative Pitch Perception </b></html>"),BorderLayout.NORTH);
				//drawInstructions(IWConsts.PR_INSTRUCTIONS,IWConsts.PR_INST_BOLDS,IWConsts.PR_INST_ITALIC);
				redrawInstructions();
				instructionBox.setPreferredSize(IWConsts.PR_INST_BOX);
				this.setPreferredSize(IWConsts.PR_WINDOW);
				break;
			}
			case Constants.AR_PRACTICE:
			{
				mainPan.add(new JLabel("<html><b> Active Relative Pitch Perception </b></html>"),BorderLayout.NORTH);
				//drawInstructions(IWConsts.AR_INSTRUCTIONS,IWConsts.AR_INST_BOLDS,IWConsts.AR_INST_ITALIC);
				redrawInstructions();
				instructionBox.setPreferredSize(IWConsts.AR_INST_BOX);
				this.setPreferredSize(IWConsts.AR_WINDOW);
				break;
			}
			default:
			{
				
			}
		}
		
		//spacing & paddings
    	mainPan.add(new JLabel("  "),BorderLayout.EAST);
    	mainPan.add(new JLabel("  "),BorderLayout.WEST);
		
		
		//add junk in grid bags:    	
    	JComponent[] insts = {instructionBox};
    	JComponent[] info = {roundsCompleted, ansToSet, ansSelected, componentMaker(showCurrentSel)};

    	centerPan.add(makeBag(insts,"Instructions",true));
    	centerPan.add(makeBag(info,"Info",false));
		mainPan.add(centerPan, BorderLayout.CENTER);
    	
		endPractice = new JButton("End Practice");
		endPractice.addActionListener(this);
		mainPan.add(endPractice, BorderLayout.SOUTH);	
				
		this.add(mainPan);
		this.pack();
		
		//this.setVisible(true);
    }
    
    /**
     * The instructions in the Instruction Window are set differently based on
     * the mode PitchLab is in: Passive/Active Pitch or Passive/Active Relative.
     */
    private void redrawInstructions()
    {
    	switch (DynmVar.mode)
		{
			case Constants.PP_PRACTICE:
			{
				drawInstructions(IWConsts.PP_INSTRUCTIONS,IWConsts.PP_INST_BOLDS,IWConsts.PP_INST_ITALIC, IWConsts.PP_CAN_SET_ANS_PLACE);
				break;
			}
			case Constants.AP_PRACTICE:
			{
				drawInstructions(IWConsts.AP_INSTRUCTIONS,IWConsts.AP_INST_BOLDS,IWConsts.AP_INST_ITALIC, IWConsts.AP_CAN_SET_ANS_PLACE);
				break;
			}
			case Constants.PR_PRACTICE:
			{
				drawInstructions(IWConsts.PR_INSTRUCTIONS,IWConsts.PR_INST_BOLDS,IWConsts.PR_INST_ITALIC, IWConsts.PR_CAN_SET_ANS_PLACE);
				break;
			}
			case Constants.AR_PRACTICE:
			{
				drawInstructions(IWConsts.AR_INSTRUCTIONS,IWConsts.AR_INST_BOLDS,IWConsts.AR_INST_ITALIC, IWConsts.AR_CAN_SET_ANS_PLACE);
				break;
			}
		}
    }
    
    /**
     * The instructions in the Instruction Window are set differently based on
     * the mode PitchLab is in: Passive/Active Pitch or Passive/Active Relative.
     * This method builds the intial text instructions into the instruction window 
     * panel by looping through each part of the panel and inserting the desired text
     * with its desired font-style.
     *
     * @param instructionSet The main instruction string
     *
     * @param instructionSetBolds The instructions that are desired to be in bold font
     *
     * @param instructionSetItalics The instructions that are desired to be italicized.
     *
     * @param canSetAnswerPlace The area of the panel where a user can set answer.
     */
    private void drawInstructions(String[] instructionSet, int[] instructionSetBolds, int[] instructionSetItalics, int canSetAnswerPlace)
    {
    	int italicIndex = 0;
    	try
    	{
        	//removes current content
    		doc.remove(0, doc.getLength()); 
        	
        	//loops to add content
	    	for (int i = 0; i < instructionSet.length; i++)
			{
				if (i == instructionSetBolds[instructionPlace])
				{
	                doc.insertString(doc.getLength(), instructionSet[i], doc.getStyle(IWConsts.STYLES[2]));
				}
				else if(i == instructionSetItalics[italicIndex])
				{
					doc.insertString(doc.getLength(), "    "+instructionSet[i], doc.getStyle(IWConsts.STYLES[1]));
					
					if (italicIndex < instructionSetItalics.length-1)
						italicIndex++;
				}
				else if (i == canSetAnswerPlace && canSetAnswer)
				{
					doc.insertString(doc.getLength(), instructionSet[i], doc.getStyle(IWConsts.STYLES[2]));
				}
				else
					doc.insertString(doc.getLength(), instructionSet[i], doc.getStyle(IWConsts.STYLES[0]));
				if(i < instructionSet.length-1)
					doc.insertString(doc.getLength(), "\n", doc.getStyle(IWConsts.STYLES[0]));
			}
    	}
    	catch (BadLocationException ble) 
    	{
            System.err.println("Couldn't insert initial text into text pane.");
    	}
    }
    
    /**
     * Returns the Instruction Window panel after building it with a grid layout.
     *
     * @param bagHeader The titled border of the Information Window
     *
     * @param center If true the window will adjust its dimensions to fit in the center.
     */
    private JComponent makeBag(JComponent[] compons,String bagHeader, boolean center)  
	{
    	JPanel panel = new JPanel();
	    GridBagLayout gridbag = new GridBagLayout();
	    
	    panel.setLayout(gridbag);
	    panel.setBorder(
				BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(bagHeader),
                BorderFactory.createEmptyBorder(7,7,7,7)));
        
        GridBagConstraints c = new GridBagConstraints();

        if(center)
        {
        	c.gridwidth = GridBagConstraints.CENTER;
 	        c.anchor = GridBagConstraints.CENTER;
 	        c.weightx = 6.0;
        }
        else
        {
	        c.gridwidth = GridBagConstraints.REMAINDER;
	        c.anchor = GridBagConstraints.EAST;
	        c.weightx = 6.0;
        }
        
        for (int i = 0; i < compons.length; i++) 
		{
        	panel.add(compons[i],c);
        }
        
        return panel;
    }
    
    /**
     * Returns JPanel
     */
    private JComponent componentMaker(Component comp)
    {
    	JPanel panel = new JPanel();
    	panel.add(comp);
    	return panel;
    }
    
    /**
     * Set the styles used for bold, italic font-styles in the information window.
     *
     * @param doc The document the styles are attached to
     */
    protected void addStylesToDocument(StyledDocument doc) 
    {
        //Initialize some styles.
        Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

        Style regular = doc.addStyle("regular", def);
        StyleConstants.setFontFamily(def, "SansSerif");

        Style s = doc.addStyle("small_italic", regular);
        StyleConstants.setItalic(s, true);
        StyleConstants.setFontSize(s, 10);

        s = doc.addStyle("bold", regular);
        StyleConstants.setBold(s, true);

    
    }
	 //****************************************************************************************
	 //*	END GUI setup 
	 //*	BEGIN action methods
	 //****************************************************************************************/	

    
    /**
     * Closes the Piano Window and Information Window when the user ends the practice 
     * mode.  Prints out informatino into the termianl on a successful closing.
     *
     * @param e The event that indicates 'endPractice' to close the windows. If the event 
     *          is not endPractice then an error has occured.
     */
    public void actionPerformed(ActionEvent e) 
	{
		if(endPractice == e.getSource()) 
		{
			System.out.println("User ended practice session.");
			Play.stopMidi();
			((PianoWindow) this.getOwner()).exiting();
		}
		else
			System.err.println("method 'actionPerformed' encountered unrecognized/unhandeled action");

    }
    
    /**
     * Updates the Information Window fields (I believe the 'Show Current Selection'
     * in the Information Window is the event that triggers this action).
     *
     * The 'Fields' are the the two fields at the bottom of the Information Window that 
     * indicate the 'Value to set' and 'Current Selection'
     *
     * @param e The event that indicates whether to error out or update the window fields.
     */
	public void itemStateChanged(ItemEvent e)
	{
		if(showCurrentSel == e.getSource())
			updateFields();
		else
			System.err.println("method 'itemStateChange' encountered unrecognized/unhandeled item");
	}
    
	/****************************************************************************************
	 *	END action methods
	 *	BEGIN get/set methods
	 ****************************************************************************************/	
	
    
   /*public void incrementInstructionPlace()
    {
    	instructionPlace++;
    	switch (DynmVar.mode)
		{
			case Constants.PP_PRACTICE:
			{
				if (instructionPlace >= IWConsts.PP_INST_BOLDS.length)
					setInstructionPlace(1);
				break;
			}
			case Constants.AP_PRACTICE:
			{
				if (instructionPlace >= IWConsts.AP_INST_BOLDS.length)
					setInstructionPlace(1);
				break;
			}
			case Constants.PR_PRACTICE:
			{
				if (instructionPlace >= IWConsts.PR_INST_BOLDS.length)
					setInstructionPlace(1);
				break;
			}
			case Constants.AR_PRACTICE:
			{
				if (instructionPlace >= IWConsts.AR_INST_BOLDS.length)
					setInstructionPlace(1);
				break;
			}
		}
    	redrawInstructions();
    	this.pack();
    }*/
    
    /**
     * Move onto the next round after a user has selected a pitch
     *
     * @param ansJustSelected The pitch the user chose in the previous round
     *
     * @param place The instruction text that will be set.
     */
    public void nextRound(String ansJustSelected, int place)
    {
    	setPrevAnsSelected(ansJustSelected);  	
    	setCanSetAnswer(false, false);
    	setInstructionPlace(place, true);
    	incrementNumRounds();
    	updateFields();
    	
    }
    /*public void nextRound(String ansJustSelected)
    {
    	setPrevAnsSelected(ansJustSelected);  	
    	incrementInstructionPlace();
    	incrementNumRounds();
    	updateFields();
    	
    }*/
    
    /**
     * Updates the Round, the Answer, and the Selected answer
     */
    public void updateFields()
    {
    	updateRounds();
    	updateAnsToSet();
    	updateAnsSelected();
    }

    /**
     * Sets the text of the number of rounds completed.
     */
    public void updateRounds()
    {
    	roundsCompleted.setText(IWConsts.NUM_COMPLETED + Integer.toString(numCompleted));
    }
    /**
     * If a previous answer exists set the answer text to that answer, otherwise choose
     * the current answer.
     */
    public void updateAnsToSet()
    {
    	if(!showCurrentSel.getState())
    		ansToSet.setText(IWConsts.DISP_PREV_TO_SET + getPrevAnsToSet());
    	else
    		ansToSet.setText(IWConsts.DISP_CUR_TO_SET + getCurrAnsToSet());
    }
    /**
     * If a previous selected answer exists set the selection text to that selection, 
     * otherwise choose the current user selection.
     */
    public void updateAnsSelected()
    {
    	if(!showCurrentSel.getState())
    		ansSelected.setText(IWConsts.DISP_PREV_SEL + getPrevAnsSelected());
    	else
    		ansSelected.setText(IWConsts.DISP_CUR_SEL + getCurrAnsSelected());
    }
    
    
	/*
	 *	END advanced get/set
	 *	BEGIN get/set methods
	 ****************************************************************************************/	
    
    /**
     * Sets the Infomation Window canSetAnswer 
     *
     * @param canSetAnswer 
     * @param redraw If true the instructions get redrawn
     */
    public void setCanSetAnswer(boolean canSetAnswer, boolean redraw)
    {
    	this.canSetAnswer = canSetAnswer;
    	
    	if(redraw)
    		redrawInstructions();
    }
    /**
     * Returns the Information Window's current canSetAnswer boolean
     */
    public boolean getCanSetAnswer()
    {
    	return this.canSetAnswer;
    }
    /**
     * Keeps track of the number of rounds completed by the user
     */
    public void incrementNumRounds()
    {
    	numCompleted++;
    	//updateRounds();
    }
    /**
     * Determines where the instructions will be placed.
     *
     * @param instructionPlace
     * @param redraw If true, the instructions will be redrawn
     */
	public void setInstructionPlace(int instructionPlace, boolean redraw) 
	{
		this.instructionPlace = instructionPlace;
		if(redraw)
			redrawInstructions();
		
	}

    /**
     * Returns Instruction Place
     */
	public int getInstructionPlace() 
	{
		return this.instructionPlace;
	}

    /**
     * Sets currently selected answer and updates the updates the GUI to represent
     * the value.
     *
     * @param currAnsSelected The current pitch being selected
     */
	public void setCurrAnsSelected(String currAnsSelected)
	{
		this.currAnsSelected = currAnsSelected;
		updateAnsSelected();
	}

    /**
     * Returns the currently selected answer
     */
	public String getCurrAnsSelected()
	{
		return currAnsSelected;
	}

    /**
     * Sets the previously selected answer to the currently selected answer
     */
	public void setCurrAnsToSet(String currAnsToSet)
	{
		setPrevAnsToSet(this.currAnsToSet);
		this.currAnsToSet = currAnsToSet;
		updateAnsToSet();
	}

    /**
     * Returns what the current answer to set is
     */
	public String getCurrAnsToSet()
	{
		return currAnsToSet;
		
	}
    /**
     * Sets the previous answer that was set
     */
	public void setPrevAnsToSet(String prevAnsToSet)
	{
		this.prevAnsToSet = prevAnsToSet;
	}
    
    /**
     * Returns the previous answer to set
     */
	public String getPrevAnsToSet()
	{
		return prevAnsToSet;
	}

    /**
     * Sets the previous answer that was selected
     *
     * @param prevAnsSelected The previous answer that was selected by the user
     */
	public void setPrevAnsSelected(String prevAnsSelected)
	{
		this.prevAnsSelected = prevAnsSelected;
	}

    /**
     * Returns the previously selected answer
     */
	public String getPrevAnsSelected()
	{
		return prevAnsSelected;
	}
	
    /**
     * Returns the boolean that is dictated by the checkbox in the Information Window.
     * The checkbox when checked shows the user's currently selected pitch, and when
     * unchecked hides the value of the user's currently selected pitch.
     */
	public boolean getShowCurrSelection()
	{
		return showCurrentSel.getState();
	}
	
	 //
	 //	END get/set methods
	 //	BEGIN MAIN
	 //
		
	
    /**
     * Generates the Instruction Windows that are present when a user uses PitchLab
     * in Practice Mode. 
     * The Instruction Window has text which is set based on what mode the user has 
     * chosen: Passive Pitch, Active Pitch, Passive Relative or Active Relative.
     * The window text is styled appropriately with bold and italic fonts based on 
     * which section the the text is in and at what point. 
     * The instructions become bold based on where the user is in the PitchLab test.
     * 
     *
     * There is also a boolean input checkbox that governs whether to show the user's
     * currently selected pitch or to hide it.  When unchecked the currently selected
     * pitch value is hidden and simulates PitchLab when not in practice mode.
     */
	public static void main(String[] args)
	{
		InstructionWindow pp = new InstructionWindow();
		InstructionWindow ap = new InstructionWindow();
		InstructionWindow pr = new InstructionWindow();
		InstructionWindow ar = new InstructionWindow();
		
		Scanner scan = new Scanner(System.in);
		String s1[] = {"q","w","e","r","t","y","u","i","o"};
		//String s2[] = {"a","s","d","f","g","h","j","k","l"};
		//String s3[] = {"z","x","c","v","b","n","m",",","."};
		int zzz;
		int n = 0;
		do
		{
			zzz = scan.nextInt();
			switch (zzz)
			{
				case 1:
				{
			//		pp.nextRound(s1[n], s2[n], s3[n]);
			//		ap.nextRound(s1[n], s2[n], s3[n]);
			//		pr.nextRound(s1[n], s2[n], s3[n]);
			//		ar.nextRound(s1[n], s2[n], s3[n]);
					if (n<=s1.length)
						n++;
					else
						n=0;
				}
				case 2:
				{
					/* 
					 * incrementInstructionPlace()  is depreciated, should be changed to
					 * setInstructionPlace(int ip)
					 */
					//pp.incrementInstructionPlace();
					//ap.incrementInstructionPlace();
					//pr.incrementInstructionPlace();
					//ar.incrementInstructionPlace();
				}
			}
		}while(zzz >0);
		
		
	}
    
}
