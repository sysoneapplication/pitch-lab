package server;

//
//  FileUtil.java
//  
//
//  Created by Gavin Shriver on 4/9/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;

import pitchLab.reference.Constants;

public class FileUtil 
{
	
	/****************************************************************************************
	 *	BEGIN Defining Variables
	 ****************************************************************************************/
	
	

	
	public String fileName;
	public String filePath;
	//public String filePath = "/Users/vladimirchaloupka/PitchLab_Server/Results/";
	//public String filePath = "/Library/WebServer/Documents/pitchLabResults/";
	private String openedTime;
	private String closedTime;
	private String userName;
	private String additionalInfo;
	private String[] testSettings; 
	//public MailUtil mailFile = new MailUtil();

	private int mode;


	/****************************************************************************************
	 *	END Defining Variables
	 *	BEGIN Main
	 ****************************************************************************************/
	
	
	public static void main(String args[])
	{
		new FileUtil(0, "someName", FileConsts.DEFAULT_RESULTS_PATH);
	}
	
	/****************************************************************************************
	 *	END Main
	 *	BEGIN Constructors
	 ****************************************************************************************/
		
	public FileUtil(int mode, String userName, String filePath)
	{
		setMode(mode);
		setUserName(userName);
		setOpenedTime();
		fileConstruct();
		this.filePath = filePath;
		
		
//		mailFile.setSubject("PitchLab "+ MODE_NAME[mode] + " session on " + openedTime);
//		mailFile.setFileLocation(filePath);
		
		appendNewLine( "PitchLab "+ FileConsts.MODE_NAME[mode] + " session on " + openedTime );
		
	}
	
	/****************************************************************************************
	 *	END Constructors
	 *	BEGIN get/set Methods
	 ****************************************************************************************/	
	public void setMode(int mode)
	{
		this.mode = mode;
	}
	public int getMode()
	{
		return mode;
	}
	
	public void setOpenedTime()
	{
		this.openedTime = getDateTime();
	}
	
	public void setClosedTime()
	{
		this.closedTime = getDateTime();
	}
	
	public void setAdditionalInfo(String additionalInfo)
	{
		this.additionalInfo = additionalInfo;
	}
	public String getAdditionalInfo()
	{
		return this.additionalInfo;
	}
	
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	public String getUserName()
	{
		return this.userName;
	}
	
	public String[] getTestSettings()
	{
		return testSettings;
	}

	public void setTestSettings( String[] testSettings )
	{
		this.testSettings = testSettings;
	}
	
	/****************************************************************************************
	 *	END get/set Methods
	 *	BEGIN initialization file methods
	 ****************************************************************************************/	
	
	// ---------------------------------------
	// Constructs the file name, and then
	// actually creates the file.
	// ---------------------------------------	
	private void fileConstruct()
	{
		File file = null;
		PrintWriter pw = null;
		boolean fileExists = true;
	
		try 
		{
	
			//--- tests to see if file exists in default directory
			for (int i=0 ; fileExists == true; i++) 
			{
				//if (i != 0)
					this.fileName = ((userName+"-"+FileConsts.MODE_NAME[mode]+Integer.toString(i)+".csv"));
				file= new File(filePath + fileName);
				fileExists = file.exists();
				//this.filePath=file.getAbsolutePath(); //--- writes the absolute file path

			}//end for
				
			
			//--- actually makes the new file
			pw = new PrintWriter(new FileWriter(file));
			
			//--- write the title headings to the *.csv File
			switch(mode)
			{
			
				case Constants.PASSIVE_PITCH:
				case Constants.PP_PRACTICE:
					pw.println(FileConsts.FILE_HEADING_PASSIVE_PITCH);
					break;
					
				case Constants.ACTIVE_PITCH:
				case Constants.AP_PRACTICE:
					pw.println(FileConsts.FILE_HEADING_ACTIVE_PITCH);
					break;
					
				case Constants.PASSIVE_RELATIVE:
				case Constants.PR_PRACTICE:
					pw.println(FileConsts.FILE_HEADING_PASSIVE_RELATIVE);
					break;
					
				case Constants.ACTIVE_RELATIVE:
				case Constants.AR_PRACTICE:
					pw.println(FileConsts.FILE_HEADING_ACTIVE_RELATIVE);
					break;
					
				default:
					pw.println(FileConsts.FILE_HEADINGS);
					
			}
			pw.flush();
			
		}//END try
		catch (IOException ex)
		{
			System.out.println("ERROR: File read error!");
			ex.printStackTrace();
		}//END catch
		finally 
		{
			if (pw != null)	//Close the PrintWriter
				pw.close();
		}//END finally
		
	}//END fileConstruct
	
	


	// ---------------------------------------
	// Constructs the message sent in the email
	// --------------------------------------
	private String makeMessage()
	{
		if (additionalInfo.equals(null))
			additionalInfo += " ";
		return ( "Enclosed is a *.csv file continigng data for a " + FileConsts.MODE_NAME[mode] 
		+ " session started on " + openedTime + " and closed on " + closedTime + ". \n" 
		+ " " + "\n \n"
		+ "Additional Info: " + additionalInfo );
	}
	
	
	// ---------------------------------------
	// returns Time & Date String
	// ---------------------------------------
	private String getDateTime() 
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	} // end getDateTime()
	
	
	/****************************************************************************************
	 *	END initialization file methods
	 *	BEGIN file data methods 
	 ****************************************************************************************/	
	
	
	// ---------------------------------------
	// Adds new column to the csv file with comma
	// ---------------------------------------
	public void appendToLine(String toWrite)
	{
		PrintWriter pw = null;
		try
		{
			//pw = new PrintWriter(new FileWriter(fileName, true));
			pw = new PrintWriter(new FileWriter(filePath + fileName, true));
			pw.print(toWrite + ", ");
			
		}//END try
		catch (IOException ex)
		{
			System.out.println("ERROR: File read error!");
			System.exit(0);
		}//END catch
		finally 
		{
			if (pw != null)	//Close the PrintWriter
				pw.close();
		}//END finally
	}//END appendToLine
	
	
	
	// ---------------------------------------
	// Writes the end of a line, the next input
	// is then started on a new row
	// ---------------------------------------
	public void appendNewLine(String toWrite)
	{
		PrintWriter pw = null;
		try
		{
			//pw = new PrintWriter(new FileWriter(fileName, true));
			pw = new PrintWriter(new FileWriter(filePath+ fileName, true));
			pw.println(toWrite);
			
		}//END try
		catch (IOException ex)
		{
			System.out.println("ERROR: File read error!");
			System.exit(0);
		}//END catch
		finally 
		{
			if (pw != null)	//Close the PrintWriter
				pw.close();
		}//END finally
	}//END appendNewLine
	
	// ---------------------------------------
	// Writes a new line for each element in
	// a string array
	// ---------------------------------------
	public void appendNewLine(String[] toWrite)
	{
		for (int i = 0; i < toWrite.length ; i++)
		{
			appendNewLine(toWrite[i]);
		}
	}
	
	// ---------------------------------------
	// Emails the test results
	// ---------------------------------------
	public boolean emailResults()
	{
//		mailFile.setMessage(makeMessage());
		appendNewLine(" ");		//spacer
		appendNewLine(" ");		//spacer
		appendNewLine(makeMessage());
		appendNewLine(" ");		//spacer
		appendNewLine(" ");		//spacer
		if(!testSettings.equals(null))
			appendNewLine(testSettings);

		
		return true;
		
		//return mailFile.sendPost();
	}//END emailResults


	
	
}