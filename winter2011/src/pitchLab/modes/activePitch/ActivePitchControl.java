package pitchLab.modes.activePitch;

import java.awt.Cursor;
import java.awt.MouseInfo;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import pitchLab.instructWindow.InstructionWindow;
import pitchLab.pianoWindow.GToolTip;
import pitchLab.pianoWindow.PianoWindow;
import pitchLab.reference.Calculations;
import pitchLab.reference.DynmVar;

public class ActivePitchControl extends pitchLab.pianoWindow.PianoWindowListener
{ 
	PianoWindow pw;
	ActivePitchMethods apm;
	boolean practiceMode = false;
	
	int state = 0;
	/*
	 * SWITCH STATEMENT state MODES
	 * case 0:	haven't started yet
	 * case 1: 	Working/Busy/Random Tones Playing
	 * case 2:	single note playing, user needs to drag bar to location, enter confirms choice
	 * case 3:	means user can drag the bar! (ie: pressed in the correct location)
	 * case 4:	dead case, before user can 'accidently press enter' -- must touch drag bar first
	 */
	
	public ActivePitchControl(PianoWindow pw, boolean practice)
	{
		this.pw = pw;
		this.practiceMode = practice;
		
		if (this.practiceMode)
		{
			this.pw.instruct = new InstructionWindow(pw);
			this.pw.toolTip = new GToolTip(pw);
		}

		this.apm = new ActivePitchMethods(pw);

	}

	
	public void mousePressed(MouseEvent e)
	{
        int x = e.getX();   // Save the x coord of the click	
        if (state >= 2 && PianoWindow.isMouseWithinDragBarMargin(x))
		{
        	
        	if (this.practiceMode)
        	{
            	pw.instruct.setCanSetAnswer(true, false);
            	pw.instruct.setInstructionPlace(2, true);
            	
            	pw.toolTip.setTip(Calculations.xToCents(x));
            	pw.toolTip.setLocation(e);
            	pw.toolTip.setVisible(true); //pw.instruct.getShowCurrSelection()); //sets toolTip visible if nessasary 
        	}
        	
			DynmVar.dragFromX = x - DynmVar.dragBarX;  // how far from left

			state = 3;
			pw.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
			
			pw.repaint();

		} 
	}
	
	public void adjustInteractiveCursor(MouseEvent e)
	{
		if (state >= 2 && state != 3)
		{
			if (PianoWindow.isMouseWithinDragBarMargin(e.getX()))
				pw.setCursor(new Cursor(Cursor.HAND_CURSOR));
			else
				pw.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
		else if (state == 1)
			pw.setCursor(new Cursor(Cursor.WAIT_CURSOR));
	}
	
	public void mouseMoved(MouseEvent e)
	{
		adjustInteractiveCursor(e);
	}
	
	public void mouseEntered(MouseEvent e)
	{
		adjustInteractiveCursor(e);
	}
	
	public void mouseExited(MouseEvent e)
	{
		adjustInteractiveCursor(e);
	}
	
	public void mouseDragged(MouseEvent e)
	{
	    if (state == 3) 
		{   // True only if button was pressed on the line        	
        	//--- line pos from mouse and original click displacement
            DynmVar.dragBarX = e.getX() - DynmVar.dragFromX;
            //--- Don't move the line off the screen sides
            DynmVar.dragBarX = Math.max(DynmVar.dragBarX, 0);
            DynmVar.dragBarX = Math.min(DynmVar.dragBarX, DynmVar.window_Width);

            int xp = DynmVar.dragBarX;
            xp = Math.max(xp, 0);
            xp = Math.min(xp, DynmVar.window_Width);
            
            if (this.practiceMode)
            {
                pw.toolTip.setLocation(e);
        		pw.instruct.setCurrAnsSelected(Calculations.freqToProperNote(pw.contSine.getFrequency()));		
            }
            apm.blendNotes_ap(xp, this.practiceMode);
            
            pw.repaint();
            
        }
		
	}
	
	
	public void mouseReleased(MouseEvent e)
	{
		if (state == 3)
		{
			if (this.practiceMode)
				pw.instruct.setInstructionPlace(1, true);

			state = 2;
			pw.setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
	}
	
	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			switch (state)
			{
				case 0: //thing has not yet started
					state = 1;  // set busy
					pw.setCursor(new Cursor(Cursor.WAIT_CURSOR));
					cycleAndPlay();
					state = 4;
					if (PianoWindow.isMouseWithinDragBarMargin(MouseInfo.getPointerInfo().getLocation().x - pw.getX()))
						pw.setCursor(new Cursor(Cursor.HAND_CURSOR));
					else
						pw.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					break;
					
				case 2:
					state = 1;  //set busy
					pw.setCursor(new Cursor(Cursor.WAIT_CURSOR));
					
					long now = System.currentTimeMillis();
					int x = DynmVar.dragBarX;

					if (this.practiceMode)
					{
			    		pw.toolTip.setVisible(false);
			    		pw.instruct.nextRound(Calculations.freqToCents(Calculations.frequencyFromX(x)), 1);
					}
					else
					{
						if(DynmVar.syncResults)
						{
							pw.dataHardCopy.appendToLine(Double.toString(Calculations.frequencyFromX(x)));	//writes stop frequency
							pw.dataHardCopy.appendToLine(Calculations.freqToCents(Calculations.frequencyFromX(x)));
							pw.timeStamp(now - DynmVar.cycleStartTime);
						}
					}
									
					pw.contSine.stop();	//stop continuous wave

		    		pw.repaint();
		    		
					if (DynmVar.count < DynmVar.cycles || this.practiceMode)
					{
						DynmVar.count++;						
						cycleAndPlay();							
					}
					else
						pw.exit();

					state = 4;
					if (PianoWindow.isMouseWithinDragBarMargin(MouseInfo.getPointerInfo().getLocation().x - pw.getX()))
						pw.setCursor(new Cursor(Cursor.HAND_CURSOR));
					else
						pw.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					break;
			}//end switch
		}//end if
	}//end method
	
	
	//
	//	CYCLE AND PLAY
	//
	private void cycleAndPlay()
	{
		if (this.practiceMode)
			pw.instruct.setInstructionPlace(1, true);
		pw.playRands();
		if (this.practiceMode)
		{
			apm.ap_rescaleAndPlay();	
			pw.instruct.setCurrAnsToSet(DynmVar.noteToSet);
			pw.instruct.setCurrAnsSelected(Calculations.freqToProperNote(pw.contSine.getFrequency()));		
		}
		else if (DynmVar.syncResults)			
			apm.ap_recordRescalePlayTime();
		else
			apm.ap_rescaleAndPlay();	
	}
	
	
	//Ignored Events:
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){}
	public void mouseClicked(MouseEvent e){}
	
}