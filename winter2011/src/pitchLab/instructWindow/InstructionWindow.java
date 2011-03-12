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

public class InstructionWindow extends JDialog implements ActionListener, ItemListener 
{
	/**
	 * 
	 */
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

    

   // public InstructionWindow(JFrame frame, boolean modal, int mode) 
    public InstructionWindow() 
	{
    	super();
		//super(frame, modal);
		setupWindow();
		
    }
       
    public InstructionWindow(PianoWindow frame) 
	{
    	super(frame);
		setupWindow();
    }
 
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
    
    //************************************
    //
    //************************************
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
    
    //************************************
    //
    //************************************
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
    
    private JComponent componentMaker(Component comp)
    {
    	JPanel panel = new JPanel();
    	panel.add(comp);
    	return panel;
    }
    
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
	/****************************************************************************************
	 *	END GUI setup 
	 *	BEGIN action methods
	 ****************************************************************************************/	
    public void actionPerformed(ActionEvent e) 
	{
		if(endPractice == e.getSource()) 
		{
			System.out.println("User ended practice session.");
			((PianoWindow) this.getOwner()).exiting();
		}
		else
			System.err.println("method 'actionPerformed' encountered unrecognized/unhandeled action");

    }
    
	
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
    
    public void updateFields()
    {
    	updateRounds();
    	updateAnsToSet();
    	updateAnsSelected();
    }
    public void updateRounds()
    {
    	roundsCompleted.setText(IWConsts.NUM_COMPLETED + Integer.toString(numCompleted));
    }
    public void updateAnsToSet()
    {
    	if(!showCurrentSel.getState())
    		ansToSet.setText(IWConsts.DISP_PREV_TO_SET + getPrevAnsToSet());
    	else
    		ansToSet.setText(IWConsts.DISP_CUR_TO_SET + getCurrAnsToSet());
    }
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
     * 
     * @param canSetAnswer
     * @param redraw
     */
    public void setCanSetAnswer(boolean canSetAnswer, boolean redraw)
    {
    	this.canSetAnswer = canSetAnswer;
    	
    	if(redraw)
    		redrawInstructions();
    }
    public boolean getCanSetAnswer()
    {
    	return this.canSetAnswer;
    }
    public void incrementNumRounds()
    {
    	numCompleted++;
    	//updateRounds();
    }
    /**
     * 
     * @param instructionPlace
     * @param redraw
     */
	public void setInstructionPlace(int instructionPlace, boolean redraw) 
	{
		this.instructionPlace = instructionPlace;
		if(redraw)
			redrawInstructions();
		
	}

	public int getInstructionPlace() 
	{
		return this.instructionPlace;
	}

	public void setCurrAnsSelected(String currAnsSelected)
	{
		this.currAnsSelected = currAnsSelected;
		updateAnsSelected();
	}

	public String getCurrAnsSelected()
	{
		return currAnsSelected;
	}

	public void setCurrAnsToSet(String currAnsToSet)
	{
		setPrevAnsToSet(this.currAnsToSet);
		this.currAnsToSet = currAnsToSet;
		updateAnsToSet();
	}

	public String getCurrAnsToSet()
	{
		return currAnsToSet;
		
	}
	public void setPrevAnsToSet(String prevAnsToSet)
	{
		this.prevAnsToSet = prevAnsToSet;
	}

	public String getPrevAnsToSet()
	{
		return prevAnsToSet;
	}

	public void setPrevAnsSelected(String prevAnsSelected)
	{
		this.prevAnsSelected = prevAnsSelected;
	}

	public String getPrevAnsSelected()
	{
		return prevAnsSelected;
	}
	
	public boolean getShowCurrSelection()
	{
		return showCurrentSel.getState();
	}
	
	/****************************************************************************************
	 *	END get/set methods
	 *	BEGIN MAIN
	 ****************************************************************************************/	
		
	
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