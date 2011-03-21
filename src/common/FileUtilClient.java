package common;

/** 
 * The FileUtilClient class contains the information
 * and methods required to connect to a client via an 
 * IP address.
 * [TODO] this class assumes HOST IP address 
 * 
 * @author Gavin Shriver
 * @version 0.6 April 27, 2009
 *
 *
 * Copyright 2011 Vladimir Chaloupka. All rights reserved.
 *
 */

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class FileUtilClient
{
	private static final String HOST ="128.95.101.226"; //the acoustics lab's g4's IP, (running the pitchlab server)
	private Communicate stub;
	
    /** 
     * Attempt to communicate with client via a hard-coded IP address.
     * If there is an error in establishing a connection, a stack trace
     * is printed in the prompt.
     */
	public FileUtilClient() 
	{
		
		try 
		{
		    Registry registry = LocateRegistry.getRegistry(HOST);
		    stub = (Communicate) registry.lookup("Communicate");
		   
		    String response = stub.sayHello();
		    System.out.println("response: " + response);
		} catch (Exception e) {
		    System.err.println("Client exception: " + e.toString());
		    e.printStackTrace();
		}
	}

    /**
     * Attempt to communicate with a client via hard-coded IP address.
     * If there is an error in establishing a connection, a stack trace 
     * is printed in the prompt.
     *
     * @param mode integer passed onto makeContact method if connection is made
     * @param name name passed onto the makeContact method if connection is made
     *
     */
	public FileUtilClient(int mode,String name) 
	{
		
		try 
		{
		    Registry registry = LocateRegistry.getRegistry(HOST);
		    stub = (Communicate) registry.lookup("Communicate");
		    
		    //hand shake (of sorts)
		    String response = stub.sayHello();
		    System.out.println("response: " + response);
		    
		    stub.makeContact( mode, name );
		    
		} 
		catch (Exception e) {
		    System.err.println("Client exception: " + e.toString());
		    e.printStackTrace();
		}
	}

	
    /** 
     * Append to line of connection. <br/>
     * [TODO] Auto-generate catch block
     *
     * @param toWrite The string to append 
     * 
     */
    public void appendToLine(String toWrite)
    {
    	try
		{
			stub.appendToLine( toWrite );
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
    }
    
    /** 
     * Append a new line to connection 
     * [TODO] Auto-generate catch block
     *
     * @param toWrite The string to append
     */
    public void appendNewLine(String toWrite)
    {
    	try
		{
			stub.appendNewLine( toWrite );
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
    }
    
    /**
     * Set additional info to the connection
     * [TODO] Auto-generate catch block
     *
     * @param additionalInfo The string to set
     */
    public void setAdditionalInfo(String additionalInfo)
    {
    	try
		{
			stub.setAdditionalInfo( additionalInfo );
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
    }
    
    /**
     * Set the test settings to the connection
     * [TODO] Auto-generated catch block
     *
     * @param testSettings The string array to set
     *
     */
    public void setTestSettings(String[] testSettings)
    {
    	try
		{
			stub.setTestSettings( testSettings );
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
    }
    
    /**
     * Set the connection close time setting
     */
    public void setClosedTime()
    {
    	try
		{
			stub.setClosedTime();
		}
		catch (RemoteException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * Set the connection email results
     */
    public boolean emailResults()
    {
    	try
		{
			return stub.emailResults();
		}
		catch (RemoteException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
    }
	
	
    /**
     * Instantiate the FileUtilClient
     *
     * @param args The array of arguements
     */
    public static void main(String[] args)
	{
    	new FileUtilClient();
    }	
	
	
}
