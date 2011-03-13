package pitchLab.modes.passiveRelative;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import pitchLab.pianoWindow.GToolTip;
import pitchLab.pianoWindow.PianoWindow;
import pitchLab.reference.Calculations;
import pitchLab.reference.DynmVar;

/**
 * This class defines the Passive Pitch mode used in PitchLab
 * 
 * [XXX] This class contains 95% of the same codes as activeRelativeControl,
 *       maybe make a RelativeControl Class then extend it accordingly for
 *       the Active and Passive modes?
 *
 * @author Gavin Shriver
 * @version 0.6 April 20, 2009
 */
public 	class PassiveRelativeControl extends pitchLab.pianoWindow.PianoWindowListener
{ 
	private PianoWindow pw;
	private int state = 0;
	private PassiveRelativeMethods prm;

    /**
     * Sets the configuration for PitchLab such that Active Relative mode is 
     * enabled and its methods are utilized.
     * Unlike the Active Pitch Control, there is not practice boolean in this 
     * method.
     *
     * @param pw The GUI piano window interface
     */
	public PassiveRelativeControl(PianoWindow pw)
	{
		this.pw = pw;
		this.pw.toolTip = new GToolTip(pw);
		this.pw.setTitle("Press the \"Enter\" key to begin.");
		this.prm = new PassiveRelativeMethods(pw);
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
        
        if (DynmVar.dragBarX-1 <= x && x <= DynmVar.dragBarX+1 && state == 2)
		{
        	state = 3; //sets can drag
			DynmVar.dragFromX = x - DynmVar.dragBarX;  // how far from left   
			
			pw.toolTip.setTip(Calculations.xToCents(e.getX()));
			pw.toolTip.setLocation(e);
			pw.toolTip.setVisible(true);
			
        } 
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
            
            int xp = e.getX();
            xp = Math.max(xp, 0);
            xp = Math.min(xp, DynmVar.window_Width);
            
            //--- Don't move the line off the screen sides
            DynmVar.dragBarX = Math.max(DynmVar.dragBarX, 0);
            DynmVar.dragBarX = Math.min(DynmVar.dragBarX, DynmVar.window_Width);
            
        	pw.toolTip.setTip(Calculations.xToCents(xp));
        	pw.toolTip.setLocation(e);

        	pw.repaint();
    		
        }
		
	}
	
    /**
     * Mouse up event. Adjusts state accordingly.
     */
	public void mouseReleased(MouseEvent e)
	{
		if (state == 3)
		{
			state = 2;
			pw.toolTip.setVisible(false);
		}
		
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
     * Also the cases are based on key pressed entires as well - for numbers 1, 2 and the Enter.
     */
	public void keyPressed(KeyEvent e)
	{
		switch (e.getKeyCode())
		{
			case KeyEvent.VK_ENTER:
			{
				switch(state)
				{
					case 0:
					{
						state = 1; //busy
						pw.setTitle("Testing....");
						cycleAndPlay();
						state = 4; //waits for user to play things etc
						break;
					}
					case 2:
					{
						state = 1; //set state working
						long now = System.currentTimeMillis();	// get the time! (elapsed)
						int x = DynmVar.dragBarX;
						
						if(DynmVar.syncResults)
						{
							pw.dataHardCopy.appendToLine(Calculations.xToCents(x));
							pw.timeStamp(now - DynmVar.cycleStartTime);
						}
						pw.toolTip.setVisible(false);
			            
						pw.repaint();
						
						if (DynmVar.count < DynmVar.cycles)
						{
							DynmVar.count++;
							pw.setTitle("Testing." + pw.getCyclesString());
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
			case KeyEvent.VK_1:
			{
				if(!pw.contSine.getPlaying() && state != 5)
				{
					pw.contSine.play(DynmVar.getFirstFreq());
					if (state == 4)
						state++;
				}	
				break;
			}
			case KeyEvent.VK_2:
			{
				if(!pw.contSine.getPlaying() && state != 4)
				{
					pw.contSine.play(DynmVar.getSecondFreq());
				}
				break;
			}
		}
	}


    /**
     * The key up event that triggers the playing of the selected tone.
     *
     * If '1' is pressed, then play the first tone
     * If '2' is pressed, then play the second.
     *
     */
	public void keyReleased(KeyEvent e)
	{
		switch(e.getKeyCode())
		{
			case KeyEvent.VK_1:
			{
				if(pw.contSine.getPlaying(DynmVar.getFirstFreq()))
				{
					pw.contSine.stop();
				}
				break;
			} 
			case KeyEvent.VK_2:
			{
				if(pw.contSine.getPlaying(DynmVar.getSecondFreq()))
				{
					pw.contSine.stop();
					if(state == 5)
						state = 2;
				}
				break;
			}
		}
	}
	

    /**
     * Create, set and record intervals for the Passive Relative test.
     */
	private void cycleAndPlay()
	{
		prm.pr_makeIntervalToSet();
		if(DynmVar.syncResults)
			prm.pr_recordIntervalToSet();
		DynmVar.cycleStartTime = System.currentTimeMillis();
	}
	
	
	
	//Ignored Events:
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseMoved(MouseEvent e){}
	public void keyTyped(KeyEvent e){}
	public void mouseClicked(MouseEvent e){}

	
}
