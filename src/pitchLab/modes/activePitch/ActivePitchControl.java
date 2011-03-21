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

/**
 * This class defines the Active Pitch mode used in PitchLab
 *
 * @author Gavin Shriver
 * @version 0.6 April 20, 2009
 */

public class ActivePitchControl extends pitchLab.pianoWindow.PianoWindowListener
{ 
	PianoWindow pw;
	ActivePitchMethods apm;
	boolean practiceMode = false;
	
	int state = 0;
	
    /**
     * Sets the configuration for PitchLab such that Active Pitch mode is 
     * enabled and its methods are utilized.
     *
     * @param pw The GUI piano window interface
     * @param practice The boolean that determines if the user has selected practice mode
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

	
    /**
     * When the user triggeres a mouse down event on the piano window,
     * the x position is stored into memory.
     * If the event occures within a set margin, then the user has 
     * successfully grabbed the line in the piano window in order to 
     * change the frequency and position of the line.
     *
     * If the user has selected practice mode, the information window
     * is updated with information calculated based on this mouse click 
     * event.
     *
     * While the line is being dragged across the piano window the cursor
     * changes to the Resize cursor to visually indicate dragging.
     *
     * @param e The mouse click event 
     */
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
	
    /**
     * If the user is within the bounds of grabbing the line to interact with 
     * the piano window, then the cursor will change to a Hand indicating
     * the ability to grab the line.
     */
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
	
    /**
     * Mouse move event
     */
	public void mouseMoved(MouseEvent e)
	{
		adjustInteractiveCursor(e);
	}
	
    /**
     * Mouse enter event
     */
	public void mouseEntered(MouseEvent e)
	{
		adjustInteractiveCursor(e);
	}
	
    /**
     * Mouse leave event
     */
	public void mouseExited(MouseEvent e)
	{
		adjustInteractiveCursor(e);
	}
	
    /**
     * Mouse drag event which updates the x-coordinate of the line in memory
     * and by how much the line has been dragged.
     *
     * If practice mode is turned on, updates to the information window are
     * made accordingly.
     */
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
	
	
    /**
     * Mouse up event. Adjusts cursor accordingly.
     */
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
	
    /**
     * Key press event which switches based on the current state, the states are indicated
     * as follows:
     *
     * case 0:	The PitchLab test hasnt started yet
     * case 1: 	Working/Busy/Random Tones Playing
     * case 2:	Single note playing, user needs to drag bar to location, enter confirms choice
     * case 3:	Means user can drag the bar! (ie: pressed in the correct location)
     * case 4:	Dead case, before user can 'accidently press enter' -- must touch drag bar first
     *
     */
	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			switch (state)
			{
				case 0: 
					state = 1;
					pw.setCursor(new Cursor(Cursor.WAIT_CURSOR));
					pw.setCursor(new Cursor(Cursor.WAIT_CURSOR));
					cycleAndPlay();
					state = 4;
					if (PianoWindow.isMouseWithinDragBarMargin(MouseInfo.getPointerInfo().getLocation().x - pw.getX()))
						pw.setCursor(new Cursor(Cursor.HAND_CURSOR));
					else
						pw.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					break;
					
				case 2:
					state = 1;
					pw.setCursor(new Cursor(Cursor.WAIT_CURSOR));
					
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
			}
		}
	}
	
	
    /**
     * Cycle and play the random notes heard at the beginning of each test.
     */
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
