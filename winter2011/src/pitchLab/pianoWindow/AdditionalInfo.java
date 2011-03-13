package pitchLab.pianoWindow;

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

/**
 * Generate the window that asks for the user additional info such as their name, birthday
 * and gender.
 *
 * @author Gavin Shriver
 * @version 0.6 April 7, 2009
 *
 * [XXX]
 *	Is this class essentially useless?
 *	IE: should its implementations be replaced with JOptionPane?
 */

public class AdditionalInfo extends JDialog implements ActionListener, WindowListener 
{

	private static final long serialVersionUID = 1L;

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
	
    /**
     * This method does most of the formatting for the 'Subject Info' box.
     * Sets the labels, format and positioning of all the input fields along
     * with registering event listeners to the buttons.
     *
     */
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
	

    /**
     * Populates the information automatically if available
     */
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
	


    /**
     * Populates the info in the 'Subject Info' pane automatically when called
     */
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
    
    /**
     * Returns the user's name
     */
    public String getName()
    {
    	return this.name;
    }
    /**
     * Returns the user's additional info.
     */
    public String getAdditionalInfo()
    {
    	return this.additionalInfo;
    }
	

    /**
     * Initialze the additional info box
     */
	public static void main(String args[])
	{
		AdditionalInfo box = new AdditionalInfo(null, true);
		box.setVisible(true);
		
		System.out.println(box.getName());
		System.out.println(box.getAdditionalInfo());
		box.dispose();
	}


    /**
     * Alert the user to confirm exiting/closing of the additional info box.
     */
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

    // Ignored events
	public void windowActivated(WindowEvent e){}
	public void windowClosed(WindowEvent e){}
	public void windowDeactivated(WindowEvent e){}
	public void windowDeiconified(WindowEvent e){}
	public void windowIconified(WindowEvent e){}
	public void windowOpened(WindowEvent e){}
}
