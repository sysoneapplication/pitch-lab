package pitchLab.modes.passivePitch;

import java.awt.Cursor;
import java.awt.MouseInfo;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import pitchLab.instructWindow.InstructionWindow;
import pitchLab.pianoWindow.GToolTip;
import pitchLab.pianoWindow.PianoWindow;
import pitchLab.reference.Calculations;
import pitchLab.reference.DynmVar;


public class PassivePitchControl extends pitchLab.pianoWindow.PianoWindowListener
{
	/*
	 * SWITCH STATEMENT state MODES
	 * case 0:	haven't started yet
	 * case 1: 	Working/Busy/Random Tones Playing
	 * case 2:	single note playing, user needs to drag bar to location, enter confirms choice
	 * case 3:	means user can drag the bar! (ie: pressed in the correct location)
	 * case 4:	dead case, before user can 'accidently press enter' -- must touch drag bar first
	 */
	private PianoWindow pw;
	private PassivePitchMethods ppm;
	private int state = 0;
	private boolean practiceMode = false;
	
	public PassivePitchControl(PianoWindow pw, boolean practice)
	{
		this.pw = pw;
		this.practiceMode = practice;
		if (practice)
			this.pw.instruct = new InstructionWindow(this.pw);
		this.pw.toolTip = new GToolTip(pw);
		this.pw.setTitle("Press the \"Enter\" key to begin.");
		this.ppm = new PassivePitchMethods(pw);
	}
	
	public void mousePressed(MouseEvent e)
	{
        int x = e.getX();   // Save the x coord of the click	
        
        if (state >= 2 && PianoWindow.isMouseWithinDragBarMargin(x))
		{
        	if(this.practiceMode && state == 4)
        		pw.instruct.setCanSetAnswer(true, false);
            state = 3;
            if (this.practiceMode)
            	pw.instruct.setInstructionPlace(2,true);
			DynmVar.dragFromX = x - DynmVar.dragBarX; 
			
			pw.toolTip.setTip(Calculations.xToCents(x));
			pw.toolTip.setLocation(e);
			pw.toolTip.setVisible(true);

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
                        
        	pw.toolTip.setTip(Calculations.xToCents(xp));
        	pw.toolTip.setLocation(e);
        	
        	if (this.practiceMode)
            	pw.instruct.setCurrAnsSelected(pw.toolTip.getTip());

    		pw.repaint();

		}
		
	}
	
	public void mouseReleased(MouseEvent e)
	{
		if (state == 3)
		{
			state = 2;
			if (this.practiceMode)
				pw.instruct.setInstructionPlace(1,true);
			pw.setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
	}
	

	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			switch(state)
			{
				//
				//	CASE WHEN TEST HASN'T STARTED YET
				//
				case 0:	//haven't started yet!
					state = 1; //working
					if (this.practiceMode)
						pw.instruct.setInstructionPlace(3,true);
					pw.setCursor(new Cursor(Cursor.WAIT_CURSOR));
					pw.setTitle("Testing....");
					pw.repaint(); // - why? does this need to happen?
					cycleAndPlay();
					state = 4; // - continuous tone is playing
					if (PianoWindow.isMouseWithinDragBarMargin(MouseInfo.getPointerInfo().getLocation().x - pw.getX()))
						pw.setCursor(new Cursor(Cursor.HAND_CURSOR));
					else
						pw.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					break;
					
				//
				//	CASE WHEN TEST TONE PLAYING AND USER IS ABLE TO GRAB BAR
				//	- when enter pressed her, user has confirmed answer
				//
				case 2:
					state = 1; // - Set state to busy 
					pw.setCursor(new Cursor(Cursor.WAIT_CURSOR));
					
					if(DynmVar.syncResults)
					{
						//First Write the time Elapsed
						pw.timeStamp(System.currentTimeMillis() - DynmVar.cycleStartTime);
						//Now write drop location
						pw.dataHardCopy.appendToLine(Calculations.xToCents(DynmVar.dragBarX));		
					}

					pw.contSine.stop();	//stop continuous wave
					pw.toolTip.setVisible(false);
					pw.repaint();
					
					if (this.practiceMode)
					{
						pw.instruct.nextRound(Calculations.xToCents(DynmVar.dragBarX),3);
						cycleAndPlay();
					}
					else if (DynmVar.count < DynmVar.cycles)
					{
						DynmVar.count++;
						pw.setTitle("Testing." + pw.getCyclesString());
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
			}//END SWITCH
		}
		
	}
	
	//
	//	CYCLE AND PLAY
	//
	private void cycleAndPlay()
	{
		pw.playRands();	//play random cycle
		if (this.practiceMode)
		{
			pw.instruct.setInstructionPlace(1,true);
			ppm.pp_setFreq(); 
			pw.instruct.setCurrAnsToSet(ppm.pp_play(this.practiceMode));
		}
		else
		{
			ppm.pp_setFreq();
			if (DynmVar.syncResults)
				ppm.pp_recordPlayTime();
			else
				ppm.pp_play(this.practiceMode);
		}
	}


	//Ignored Events:
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){}
	public void mouseClicked(MouseEvent e){}
	
}
