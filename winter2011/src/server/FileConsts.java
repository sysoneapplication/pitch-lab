package server;

public class FileConsts
{
	public static final String DEFAULT_RESULTS_PATH = "/Library/WebServer/Documents/pitchLabResults/"; //default use for mac server in lab
	public static final int DEFAULT_PORT = 1099;
	
	public static final String FILE_HEADINGS = 
		"Time from tone start(ns), " +
		"Release xPos, " +
		"Release xPos Cents, " +
		"Note Played";
	public static final String FILE_HEADING_PASSIVE_PITCH = 
		"Frequency Played (hz), " +
		"Note + Error Played, " +
		"Subject's Note + Error, " +
		"Time Elapsed (ms)";
	public static final String FILE_HEADING_ACTIVE_PITCH = 
		"Frequency to Set (hz), " +
		"Note To Set (as displayed), " +
		"Frequency Set By User (hz), " +
		"Note + Error Set, " +
		"Time Elapsed (ms)";
	public static final String FILE_HEADING_PASSIVE_RELATIVE = 
		"First Frequency Played, " +
		"Second Frequency Played, " +
		"(( calculated step )), " +
		"User Selected Step, " +
		"Time Elapsed (ms)";
	public static final String FILE_HEADING_ACTIVE_RELATIVE = 
		"First frequency Played (hz), " +
		"Frequency to set (hz), " +
		"Step To Set (as displayed), " +
		"User set Frequency, " +
		"Calculated Step, " +
		"Time Elapsed (ms)";

	public static final String[] MODE_NAME = 
	{
		"default",						//0
		"Passive_Pitch_Test",			//1
		"Active_Pitch_Test",			//2
		"Passive_Relative_Test",		//3
		"Active_Relative_Test",			//4
		"Passive_Pitch_Training",		//5
		"Active_Pitch_Training", 		//6
		"Passive_Relative_Training",	//7
		"Active_Relative_Training"		//8
		};
	
	/*
	public static final int DEFAULT 			= 0;
	public static final int PASSIVE_PITCH 		= 1;
	public static final int ACTIVE_PITCH 		= 2;
	public static final int PASSIVE_RELATIVE 	= 3;
	public static final int ACTIVE_RELATIVE 	= 4;
	public static final int PP_PRACTICE 		= 11;
	public static final int AP_PRACTICE			= 12;
	public static final int PR_PRACTICE 		= 13;
	public static final int AR_PRACTICE 		= 14;*/
}
