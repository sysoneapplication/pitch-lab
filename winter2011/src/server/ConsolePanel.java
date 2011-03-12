package server;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ConsolePanel extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea textArea;
	private TextAreaOutputStream textOutputStream;
	private JScrollPane scrollPane;
	private Dimension preferredSize = new Dimension(690,250);
	
	public ConsolePanel()
	{
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setPreferredSize(preferredSize);
		textOutputStream = TextAreaOutputStream.getInstance(textArea);
		this.add(scrollPane);
	}
}
