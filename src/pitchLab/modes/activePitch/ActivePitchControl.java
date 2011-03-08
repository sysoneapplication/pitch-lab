package pitchLab.modes.activePitch;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import pitchLab.pianoWindow.PianoWindow;
import pitchLab.reference.Calculations;
import pitchLab.reference.DynmVar;

public class ActivePitchControl implements MouseMotionListener, MouseListener, KeyListener
{ 
	PianoWindow pw;
	ActivePitchMethods apm;
	
	int state = 0;
	/*
	 * SWITCH STATEMENT state MODES
	 * case 0:	haven't started yet
	 * case 1: 	Working/Busy/Random Tones Playing
	 * case 2:	single note playing, user needs to drag bar to location, enter confirms choice
	 * case 3:	means user can drag the bar! (ie: pressed in the correct location)
	 * case 4:	dead case, before user can 'accidently press enter' -- must touch drag bar first
	 */
	
	public ActivePitchControl(PianoWindow pw)
	{
		this.pw = pw;
		this.apm = new ActivePitchMethods(pw);
	}

	
	public void mousePressed(MouseEvent e)
	{
        int x = e.getX();   // Save the x coord of the click	

        if (DynmVar.dragBarX-1 <= x && x <= DynmVar.dragBarX+1 && state >= 2)
		{
        	state = 3;
			DynmVar.dragFromX = x - DynmVar.dragBarX;  // how far from left   
        } 
	}
	
	
	public void mouseDragged(MouseEvent e)
	{
	    if (state == 3) 
		{   // True only if button was pressed on the line        	
        	//--- line pos from mouse and original click displacement
            DynmVar.dragBarX = e.getX() - DynmVar.dragFromX;
            
            int xp = e.getX();
            xp = Math.max(xp, 0);
            xp = Math.min(xp, DynmVar.window_Width);
            
            //--- Don't move the line off the screen sides
            DynmVar.dragBarX = Math.max(DynmVar.dragBarX, 0);
            DynmVar.dragBarX = Math.min(DynmVar.dragBarX, DynmVar.window_Width);
            apm.blendNotes_ap(xp);
            
            pw.repaint();
            
        }
		
	}
	
	
	public void mouseReleased(MouseEvent e)
	{
		if (state == 3)
			state = 2;
	}
	
	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			switch (state)
			{
				case 0: //thing has not yet started
					state = 1;  // set busy
					cycleAndPlay();
					state = 4;
					break;
					
				case 2:
					state = 1;  //set busy
					long now = System.currentTimeMillis();
					int x = DynmVar.dragBarX;
				
					if(DynmVar.syncResults)
					{
						pw.dataHardCopy.appendToLine(Double.toString(Calculations.frequencyFromX(x)));	//writes stop frequency
						pw.dataHardCopy.appendToLine(Calculations.freqToCents(Calculations.frequencyFromX(x)));
						pw.timeStamp(now - DynmVar.cycleStartTime);
					}
					
					pw.contSine.stop();	//stop continuous wave

		    		pw.repaint();
		    		
					if (DynmVar.count < DynmVar.cycles)
					{
						DynmVar.count++;						
						cycleAndPlay();							
					}
					else
						pw.exit();

					state = 4;
					break;
			}//end switch
		}//end if
	}//end method
	
	
	//
	//	CYCLE AND PLAY
	//
	private void cycleAndPlay()
	{
		pw.playRands();
		if(DynmVar.syncResults)
			apm.ap_recordRescalePlayTime();
		else
			apm.ap_rescaleAndPlay();	
	}
	
	
	//Ignored Events:
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseMoved(MouseEvent e){}
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){}
	public void mouseClicked(MouseEvent e){}
	
}