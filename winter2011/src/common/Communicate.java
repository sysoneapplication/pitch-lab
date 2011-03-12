package common;



import java.rmi.Remote;
import java.rmi.RemoteException;


public interface Communicate extends Remote 
{
    void makeContact(int mode,String name) throws RemoteException;
    
    void appendToLine(String toWrite) throws RemoteException;
    
    void appendNewLine(String toWrite) throws RemoteException;
    
    void setAdditionalInfo(String additionalInfo) throws RemoteException;
    
    void setTestSettings(String[] testSettings) throws RemoteException;
    
    void setClosedTime() throws RemoteException;
    
    boolean emailResults() throws RemoteException;
    
    String sayHello() throws RemoteException;
    
}



