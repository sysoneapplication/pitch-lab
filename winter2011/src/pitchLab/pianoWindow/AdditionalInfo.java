package pitchLab.pianoWindow;
//
//  CustomDialog.java
//  (this is version 0.6)  
//
//	NOTES:	
//		is this class essentially useless ?
//		ie: should its implementations be  
//		replaced with JOptionPane ?
//
//  Created by Gavin Shriver on 4/7/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.


import javax.swing.JDialog; 
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.MaskFormatter;

import common.EntryBox;
import common.GUIMethods;


import java.awt.event.ActionEvent;
import java.awt.*;
import java.text.ParseException;

public class AdditionalInfo extends JDialog implements ActionListener, WindowListener 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/****************************************************************************************
	 *	BEGIN Defining Variables 
	 ****************************************************************************************/	
	
	private JButton button = new JButton("OK");
	
	private EntryBox userName = new EntryBox(20, EntryBox.STRING, "Name: ");
	
	private JRadioButton male = new JRadioButton("male");
	private JRadioButton female = new JRadioButton("female");
	private ButtonGroup gender = new ButtonGroup();
	
	private JTextArea additionalNotes = new JTextArea();
	private JFormattedTextField userBirthDate;
	
	private String name = "";
	private String additionalInfo = "";
	
	private JFrame masterFrame;
	
	/****************************************************************************************
	 *	END Variables
	 *	BEGIN Constructors
	 ****************************************************************************************/
	
	public AdditionalInfo(JFrame frame, boolean modal) 
	{
		super(frame, modal);
		this.masterFrame = frame;
				
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(this);
		this.setTitle("Subject Info");
		
		MaskFormatter format;
		try 
		{
			format = new MaskFormatter("##/##/####");
		//	usrBirthDate.setFormatter(format);
			userBirthDate = new JFormattedTextField(format);
		} catch (ParseException e) 
		{
			e.printStackTrace();
		}
		
		userBirthDate.setToolTipText("Set date of birth in MM/DD/YYYY format.");
		userBirthDate.setColumns(10);
		userBirthDate.setHorizontalAlignment(JFormattedTextField.CENTER);
		
		getContentPane().setLayout(new BorderLayout());
				
		button.addActionListener(this);
		gender.add(male);
		gender.add(female);
		
		additionalNotes.setLineWrap(true);
		additionalNotes.setWrapStyleWord(true);
		JScrollPane addnotesScroll = new JScrollPane(additionalNotes, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		addnotesScroll.setPreferredSize(new Dimension(410,120));
		
		JPanel panel = new JPanel(new GridLayout(2,1));
		
		
		JComponent[] userInputs = {userName,GUIMethods.flowMaker(new JLabel("Birth Date:"),userBirthDate),GUIMethods.flowMaker(male,female)};
		JComponent[] addNotes = {addnotesScroll};
		
		
		
		panel.add(GUIMethods.makeBag(userInputs,"User Information",false));        
        panel.add(GUIMethods.makeBag(addNotes,"Additional Notes", true));
       
		this.add(GUIMethods.flowMaker(button, null),BorderLayout.SOUTH);
		this.add(panel,BorderLayout.CENTER);
		pack();		
		setLocationRelativeTo(frame);
		

    }
	
	/****************************************************************************************
	 *	END Constructors
	 *	BEGIN Action Events
	 ****************************************************************************************/	

    public void actionPerformed(ActionEvent e) 
	{
		if (button == e.getSource())
		{
			populateInfo();
			this.setVisible(false);
		}
		else
			System.out.println("error occured");
	}

    

	
	/****************************************************************************************
	 *	END Action Events
	 *	BEGIN getter/setters
	 ****************************************************************************************/	
	
	private void populateInfo()
	{
		String tmp = " ";
		this.name = (String)userName.getValue();
		
		tmp += " \n Name: " + name;
		tmp += " \n Birth Date: " + userBirthDate.getValue();
		
		if (gender.getSelection() == male.getModel())
			tmp += " \n Gender: male";
		else if (gender.getSelection() == female.getModel())
			tmp += " \n Gender: female";
		else
			tmp += " \n Gender: not selected";
		
		tmp += " \n Additional Notes: \n " + additionalNotes.getText(); 
		
		this.additionalInfo = tmp;
	}
    
    public String getName()
    {
    	return this.name;
    }
    public String getAdditionalInfo()
    {
    	return this.additionalInfo;
    }
	
	/****************************************************************************************
	 *	END getter/setters
	 *	BEGIN main
	 ****************************************************************************************/	
	public static void main(String args[])
	{
		AdditionalInfo box = new AdditionalInfo(null, true);
		box.setVisible(true);
		
		System.out.println(box.getName());
		System.out.println(box.getAdditionalInfo());
		box.dispose();
	}


	@Override
	public void windowClosing(WindowEvent e)
	{
		int exitAnswer = JOptionPane.showConfirmDialog(null, " Are you sure you want to Exit? ", "Exit?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

		if (exitAnswer == JOptionPane.YES_OPTION)
		{
			dispose();
			masterFrame.dispose();
		}
		
	}

	//
	//	IGNORE THESE:
	//
	public void windowActivated(WindowEvent e){}
	public void windowClosed(WindowEvent e){}
	public void windowDeactivated(WindowEvent e){}
	public void windowDeiconified(WindowEvent e){}
	public void windowIconified(WindowEvent e){}
	public void windowOpened(WindowEvent e){}

    
}