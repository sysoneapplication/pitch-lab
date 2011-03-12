package common;


import java.awt.*;

import javax.swing.*; 
import javax.swing.JLabel;
import javax.swing.JPanel;


/** Description of EntryBox
 * 
 * @author Gavin Shriver
 * @version 0.6 April 27, 2009
 *
 *
 * Copyright 2011 Vladimir Chaloupka. All rights reserved.
 *
 */
public class EntryBox extends JPanel
{
	private static final long serialVersionUID = 1L;

	public static final int INT = 0;
	public static final int DOUBLE = 1;
	public static final int STRING = 2;
	public static final int PASS = 3;
		
	private int mode;
		
	private int fieldSize = 20;
	private JTextField inputfield;
	private JPasswordField passfield;
	
	
	//String
    /**
     * Set parameters of EntryBox with <code>String</code> initial value
     */
	public EntryBox(int fieldSize, int mode, String text, String initialValue)
	{		
		setMode(mode);
		setFieldSize(fieldSize);
		windowMaker(text);
		setTo(initialValue);
	}
    /**
     * Set parameters of EntryBox with <code>String</code> initial value and 
     * a boolean to set whether the EntryBox is enabled
     */
	public EntryBox(int fieldSize, int mode, String text, String initialValue,boolean enabled)
	{		
		setMode(mode);
		setFieldSize(fieldSize);
		windowMaker(text);
		setTo(initialValue);
		setEnabled(enabled);
	}
	
	
	//Number
    /**
     * Set parameters of EntryBox with <code>Number</code> initial value
     */
	public EntryBox(int fieldSize, int mode, String text, Number initialValue)
	{		
		setMode(mode);
		setFieldSize(fieldSize);
		windowMaker(text);
		setTo(initialValue);
	}
    /**
     * Set parameters of EntryBox with <code>String</code> initial value and 
     * a boolean to set whether the EntryBox is enabled
     */
	public EntryBox(int fieldSize, int mode, String text, Number initialValue, boolean enabled)
	{		
		setMode(mode);
		setFieldSize(fieldSize);
		windowMaker(text);
		setTo(initialValue);
		setEnabled(enabled);
	}
	
	//char[]	
    /**
     * Set parameters of EntryBox with <code>char[]</code> initial value
     */
	public EntryBox(int fieldSize, int mode, String text, char[] initialValue)
	{		
		setMode(mode);
		setFieldSize(fieldSize);
		windowMaker(text);
		setTo(initialValue);
	}
    /**
     * Set parameters of EntryBox with <code>char[]</code> initial value and 
     * a boolean to set whether the EntryBox is enabled
     */
	public EntryBox(int fieldSize, int mode, String text, char[] initialValue, boolean enabled)
	{		
		setMode(mode);
		setFieldSize(fieldSize);
		windowMaker(text);
		setTo(initialValue);
		setEnabled(enabled);
	}
	
	//no defaults:
    /**
     * Sets parameters of EntryBox without defaults
     */
	public EntryBox(int fieldSize, int mode, String text)
	{		
		setMode(mode);
		setFieldSize(fieldSize);
		windowMaker(text);
	}
    /**
     * Sets parameters of EntryBox without defaults and a boolean to set 
     * whether the EntryBox is enabled
     */
	public EntryBox(int fieldSize, int mode, String text, boolean enabled)
	{		
		setMode(mode);
		setFieldSize(fieldSize);
		windowMaker(text);
		setEnabled(enabled);

	}
    /**
     * Sets parameters of EntryBox 
     */
	public EntryBox(int fieldSize, int mode)
	{		
		setMode(mode);
		setFieldSize(fieldSize);
		windowMaker(null);
	}
    /**
     * Sets parameters of EntryBox with a boolean to set whether the EntryBox 
     * is enabled
     */
	public EntryBox(int fieldSize, int mode, boolean enabled)
	{		
		setMode(mode);
		setFieldSize(fieldSize);
		windowMaker(null);
		setEnabled(enabled);
	}
	
    /**
     * 
     */
	private void windowMaker(String text)
	 {
		this.setLayout(new BorderLayout());
		
		 if (text != null)
			 this.add(new JLabel(text), BorderLayout.LINE_START);
			 
		 if(mode != PASS)
		 {
			 inputfield = new JTextField(fieldSize);
			 this.add(inputfield, BorderLayout.LINE_END);
		 }
		 else if(mode == PASS)
		 {
			 passfield = new JPasswordField(fieldSize);
			 this.add(passfield, BorderLayout.LINE_END);
		 }
	 }
	
	
	
    /**
     * Set current EntryBox object field size
     * @param fieldSize The <code>int</code> field size to set
     */
	public void setFieldSize(int fieldSize)
	{
		this.fieldSize = fieldSize;
	}
	
    /**
     * Set current EntryBox object field mode
     *
     * @param mode The <code>int</code> mode to set
     */
	public void setMode(int mode)
	{
		this.mode = mode;
	}
	
    /**
     * Set current EntryBox object editable boolean
     * @param mode The <code>boolean</code> mode for editable state
     */
	public void setEditable(boolean editable)
	{
		inputfield.setEditable(editable);
	}
	
    /**
     * Set current EntryBox to show input field or password field
     * @param mode The <code>boolean</code> mode for editable state
     */
	public void setEnabled(boolean enabled)
	{
		if (mode != PASS)
			inputfield.setEnabled(enabled);
		if (mode == PASS)
			passfield.setEnabled(enabled);
	}
	
	
    /**
     * Set value of the password field
     * @param value the <code>char[]</code> to set the pass field
     */
	public void setTo(char[] value)
	{		
		passfield.setText(value.toString());
	}
	
    /**
     * Set value of the input field
     * @param value the <code>String</code> to set the input field
     */
	public void setTo(String value)
	{
		inputfield.setText(value);
	}
	
    /**
     * Set number value of the input field
     * @param value the <code>Number</code> to set the input field
     */
	public void setTo(Number value)
	{
		inputfield.setText(value.toString());
	}
	
    /**
     * Based on the mode that is set by constructors and depending on
     * the type of the mode value a different value is returned. 
     * <p>
     * The type of mode values are <code>int</code>, <code>double</code>,
     * <code>String</code> and <code>Pass</code> 
     * <p>
     * Returns <code>null</code> if mode type does not match.
     */
	public Object getValue()
	{
		if (mode == INT)
			return (int)Integer.valueOf(inputfield.getText());
		else if (mode == DOUBLE)
			return (double)Double.valueOf(inputfield.getText());
		else if (mode == STRING)
			return (String)inputfield.getText();
		else if (mode == PASS)
			return (char[])passfield.getPassword();
		else
			return null;
	}



}





















