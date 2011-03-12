package server;
//
//  PitchLabServer.java
//  PitchLabServer
//
//  Created by Gavin Shriver on 5/22/09.
//  Copyright (c) 2009 __MyCompanyName__. All rights reserved.
//

/*
 * TODO: 
 * 	- fix 'stop server' so that the server *actually* stops
 * 	- display server IP if possible after successful connect
 * 
 * 
 */


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import common.Communicate;


public class PitchLabServer implements Communicate 
{
	private FileUtil hardCopy;
	private String filePath = FileConsts.DEFAULT_RESULTS_PATH;


	private Registry registry;
	private Communicate stub;
	private int port = FileConsts.DEFAULT_PORT;
	
	public PitchLabServer(int port, String filePath) 
	{
		this.filePath = filePath;
		this.port = port;
	}
	
	/**
	 * starts and/or restarts server
	 */
	public void startServer()
	{
		try
		{
			System.out.println();
			System.out.println("Creating Local Registery...");
			java.rmi.registry.LocateRegistry.createRegistry(port);

			System.out.println("Exporting Remote Object...");
			stub = (Communicate) UnicastRemoteObject.exportObject(this, port);
			
			System.out.println("Fetching Registry...");
			registry = LocateRegistry.getRegistry();
			
			System.out.println("Binding Registry...");
			registry.rebind("Communicate", stub);
			
			System.out.println("...");
			System.out.println("Server IP Address: " + getIPAddress());
			System.out.println("Server Host Name: " + getHostName());
			System.out.println("Server port: " + port);
			System.out.println("Results Directory: " + filePath);
			System.out.println("Server ready!");
		} 
		catch (RemoteException e)
		{
			System.out.println(" !!! ----------- ");
			System.out.println("Server exception:\t" + e.toString());
			e.printStackTrace();
		}
		
	}
	
	/**
	 * shuts down server
	 */
	public void killServer()
	{
		try
		{
			System.out.println("");
			System.out.println("Killing Server...");
			System.out.println("unbinding communicate...");
			registry.unbind("Communicate");
			
			System.out.println("Server disconnected!");
		} 
		catch (Exception e)
		{
			System.out.println(" !!! ----------- ");
			System.out.println("Server exception:\t" + e.toString());
			e.printStackTrace();
		}
	}
	
	public String getIPAddress()
	{
		//FIXME: OUTPUT INCORRECT
		try 
		{
		    InetAddress addr = InetAddress.getLocalHost();
		    byte[] ipAddr = addr.getAddress();
		    String ip = new String(ipAddr);
		    return ip;
		    } 
		catch (UnknownHostException e) 
		{
			return "Unable to get host IP";
		}
	}
	
	public String getHostName()
	{
		try 
		{
		    InetAddress addr = InetAddress.getLocalHost();
		    return addr.getHostName();
		} catch (UnknownHostException e)
		{
			return "Unable to get host name";
		}
	}
	
	public void makeContact(int mode, String name)
    {
    	hardCopy = new FileUtil(mode, name, filePath);
    }
    
    public  void setAdditionalInfo(String additionalInfo)
    {
    	hardCopy.setAdditionalInfo( additionalInfo );
    }
    
    public void setTestSettings(String[] testSettings)
    {
    	hardCopy.setTestSettings(testSettings);
    }
    
    public void appendToLine(String toWrite)
    {
    	hardCopy.appendToLine( toWrite );
    }
    
    public void appendNewLine(String toWrite)
    {
    	hardCopy.appendNewLine( toWrite );
    }
	
    public void setClosedTime()
    {
    	hardCopy.setClosedTime();
    }
    
	public boolean emailResults()
	{
		return hardCopy.emailResults();
	}
	
	public String sayHello()
	{
		return "hello, first contact made";
	}
	
	
	public String getFilePath()
	{
		return filePath;
	}
	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}
	public int getPort()
	{
		return port;
	}
	public void setPort(int port)
	{
		this.port = port;
	}
	
    public static void main (String args[]) 
	{
    	new PitchLabServer(FileConsts.DEFAULT_PORT,FileConsts.DEFAULT_RESULTS_PATH);
		
    }
	
	
}
