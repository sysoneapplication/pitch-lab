package pitchLab.modes.activeRelative;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import pitchLab.pianoWindow.PianoWindow;
import pitchLab.reference.Calculations;
import pitchLab.reference.DynmVar;

/**
 * This class defines the Passive Pitch mode used in PitchLab
 * 
 * [XXX] This class contains 95% of the same codes as ActivePitchControl,
 *       maybe make a PitchControl Class then extend it accordingly for
 *       the Active and Passive modes?
 *
 * @author Gavin Shriver
 * @version 0.6 April 20, 2009
 */
public class ActiveRelativeControl extends pitchLab.pianoWindow.PianoWindowListener
{ 
	
	private PianoWindow pw;
	private boolean firstTone = false;
	private int state = 0;

    /**
     * Sets the configuration for PitchLab such that Active Relative mode is 
     * enabled and its methods are utilized.
     * Unlike the Active Pitch Control, there is not practice boolean in this 
     * method.
     *
     * @param pw The GUI piano window interface
     */
	public ActiveRelativeControl(PianoWindow pw)
	{
		this.pw = pw; 
	}

    /**
     * When the user triggeres a mouse down event on the piano window,
     * the x position is stored into memory and the dragged distance
     * is calculated.
     *
     * [TODO] check if the user's cursor is in the set margins?
     *
     * While the line is being dragged across the piano window the cursor
     * changes to the Resize cursor to visually indicate dragging.
     *
     * @param e The mouse click event 
     */
	public void mousePressed(MouseEvent e)
	{
        int x = e.getX();   // Save the x coord of the click	
        
        if(!pw.getDragBarVisible() && state == 6)
        {
        	DynmVar.dragBarX = x; //e.getX() - DynmVar.dragFromX;
        	pw.setDragBarVisible(true);
        	pw.repaint();
        	state = 2;
        }
        
        if (DynmVar.dragBarX-10 <= x && x <= DynmVar.dragBarX+10 && state == 2 )
		{
            state = 3;
			DynmVar.dragFromX = x - DynmVar.dragBarX;  // how far from left   
        } 
	}
	
    /**
     * Mouse drag event which updates the x-coordinate of the line in memory
     * and by how much the line has been dragged.
     * Repaints the GUI piano window to visiually represent where the on the
     * window the user's bar is.
     */
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
            
            if(!firstTone) //To prevent the blending when first tone being played
            	ActiveRelativeMethods.blendNotes_ar(pw,xp);

        	pw.repaint();
		}
	}
	
    /**
     * Mouse up event adjusts the state accordingly.
     */
	public void mouseReleased(MouseEvent e)
	{
		if (state == 3)
			state = 2;
		
	}

    /**
     * Key press event which switches based on the current state, the states are indicated
     * as follows:
     *
	 * case 0:	The PitchLab test hasn't started yet
	 * case 1: 	Working/Busy/Random Tones Playing
	 * case 2:	Single note playing, user needs to drag bar to location, enter confirms choice
	 * case 3:	Means user can drag the bar! (ie: pressed in the correct location)
	 * case 4:  User hasn't heard first tone yet
	 * case 5: 	User hasn't heard second tone yet
	 * case 6:	User hasn't made bar appear yet
     *
     */
	public void keyPressed(KeyEvent e)
	{
		switch (e.getKeyCode())
		{
			case KeyEvent.VK_ENTER:
			{
				switch (state)
				{
					case 0:
						state = 1; 
						cycleAndPlay();
						state = 4;
				    	break;
					case 2:
					{
						state = 1;
						long now = System.currentTimeMillis();
						int x = DynmVar.dragBarX;
					
						if(DynmVar.syncResults)
						{
							pw.dataHardCopy.appendToLine(Double.toString(ActiveRelativeMethods.arFrequencyFromX(x)));	//writes stop frequency
							pw.dataHardCopy.appendToLine(Calculations.getToneInterval(DynmVar.getFirstFreq(), ActiveRelativeMethods.arFrequencyFromX(x)));
							pw.timeStamp(now - DynmVar.cycleStartTime);
						}
						
		        		pw.repaint();
						
						if (DynmVar.count < DynmVar.cycles)
						{
							DynmVar.count++;
							pw.setTitle("Testing... Completed "+DynmVar.count+" of "+ DynmVar.cycles +" cycles.");
							cycleAndPlay();
							state = 4;
						}
						else
							pw.exit();
						
						
						break;
					}
				}
				break;
			}
			case KeyEvent.VK_1: //play first tone
			{
				if(!pw.contSine.getPlaying() && state != 5)
				{
					pw.contSine.play(DynmVar.getFirstFreq());				
					firstTone = true;
					if(state == 4)
						state ++;
				}					
				break;
			}
			case KeyEvent.VK_2: //play second tone (user selected)
			{
				if(!pw.contSine.getPlaying() && state != 4)
				{
					pw.contSine.play(ActiveRelativeMethods.arFrequencyFromX(DynmVar.dragBarX));
					if(state == 5)
						state ++;
				}
				
				break;
			}
		}
	}

    /**
     * The key up event that triggers the playing of the selected tone.
     *
     * If the '1' (or the first key) is pressed, then play the first tone
     * otherwise play the second.
     *
     */
	public void keyReleased(KeyEvent e)
	{
		switch (e.getKeyCode())
		{
			case KeyEvent.VK_1:
			{
				if(pw.contSine.getPlaying(DynmVar.getFirstFreq()))
				{
					pw.contSine.stop();
					//SineContinuous.setAmplitude(0);
					firstTone = false;
				}
				break;
			}//end case
			case KeyEvent.VK_2:
			{
				if(!firstTone)
				{
					pw.contSine.stop();
					//if(state == 5)
					//	state = 2;
				}
				break;
			}
			
		}
	}
	
	
	
	
    /**
     * Hides the drag bar intiially and sets the two frequencies that
     * will be played.
     */
	private void cycleAndPlay()
	{
		pw.setDragBarVisible(false);
		pw.repaint();
		ActiveRelativeMethods.ar_setIntervalToSet();
		if(DynmVar.syncResults)
			ActiveRelativeMethods.ar_rescalePlayTime(pw);
		else
			ActiveRelativeMethods.ar_rescaleAndPlay(pw);	
	}
	
	//Ignored Events:
	public void keyTyped(KeyEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseMoved(MouseEvent e){}
	public void mouseClicked(MouseEvent e){}
	
}
