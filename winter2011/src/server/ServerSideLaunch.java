package server;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;

import common.EntryBox;
import common.GUIMethods;


public class ServerSideLaunch extends JDialog implements ActionListener, WindowListener 
{
	/*
	 * TODO:
	 * 	need create window/pane that asks for file a directory to use to save files
	 * 
	 * 	create window that asks for port number to use
	 *  
	 *  leave the window up once server is started, if possible add dialog box to show output of server,
	 *  
	 *  gray out directory and port and gray out directory 
	 *  
	 *  start and stop button // CHANGE THE TEXT AND THE FUNCTION OF THE BUTTON ONCE STARTED
	 *	
	 *	WAYY down the road:  add checkbox 'send email to inform' blah blah
	 *  
	 */
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//
	//	VARIABLES
	//
	private static final String	WINDOW_TITLE = "Pitch Lab Server";
	private static final String PORT_PROMPT = "Port Number: ";
	private static final String DIRECTORY_PROMPT = "File Path: ";//"Pick where to save result files: ";
	private static final Dimension WINDOW_DIMENSION = new Dimension(700, 500); //in pixels
	
	private boolean started = false;
	
	
	//
	//	COMPONENTS
	//
	private JButton startServer = new JButton("Start");
	private JButton pickDirectory = new JButton("Pick Directory");
	private EntryBox port = new EntryBox(6, EntryBox.INT, PORT_PROMPT, FileConsts.DEFAULT_PORT);
	private EntryBox path = new EntryBox(30, EntryBox.STRING, DIRECTORY_PROMPT, FileConsts.DEFAULT_RESULTS_PATH);
	private ConsolePanel console;
	private PitchLabServer server;
	
	
	//
	//	CONSTRUCTOR
	//
	public ServerSideLaunch()
	{
		this.setTitle(WINDOW_TITLE);
		this.setSize(WINDOW_DIMENSION);
		this.setResizable(false);
		this.
		getContentPane().setLayout(new BorderLayout());
		
		console = new ConsolePanel();
		server = new PitchLabServer(getPort(),getFilePath());
		
		startServer.addActionListener(this);
		pickDirectory.addActionListener(this);
		
		JComponent[] settings = {port,path,pickDirectory};//GUIMethods.flowMaker(path,pickDirectory)};
		
		this.add(GUIMethods.makeBag(settings, "Settings", false),BorderLayout.NORTH);
		this.add(console,BorderLayout.CENTER);
		this.add(GUIMethods.flowMaker(startServer, null),BorderLayout.SOUTH);
		
		pack();		
		setLocationRelativeTo(null);
		
	}
	
	
	//
	//	SET FILE DIRECTORY DIALOG
	//
	public String askFilePath()
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
	    if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) 
	    	return fileChooser.getSelectedFile().getAbsolutePath();
	    else
	    	return null;			
	}

	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == startServer)
		{
			if(started)
			{
				server.killServer();
				started = false;
				startServer.setText("Start");
			}
			else if (!started)
			{
				//start server
				server.setFilePath(getFilePath());
				server.setPort(getPort());
				server.startServer();
				started = true;
				startServer.setText("Stop");
			}
			
			
		}
		else if (e.getSource() == pickDirectory)
		{
			setFilePath(askFilePath());
		}
		
	}
	
	public int getPort()
	{
		return (Integer)port.getValue();
	}
	public void setFilePath(String filePath)
	{
		if(filePath != null)
			path.setTo(filePath);
		
	}
	public String getFilePath()
	{
		return (String)path.getValue();
	}
	
	//
	//	IGNORE THESE EVENTS 
	//
	public void windowActivated(WindowEvent e){}
	public void windowClosed(WindowEvent e){}
	public void windowClosing(WindowEvent e){}
	public void windowDeactivated(WindowEvent e){}
	public void windowDeiconified(WindowEvent e){}
	public void windowIconified(WindowEvent e){}
	public void windowOpened(WindowEvent e){}

	
	//
	//	MAIN
	//
	public static void main(String args[])
	{
		ServerSideLaunch ssl = new ServerSideLaunch();
		ssl.setVisible(true);
	}
}
