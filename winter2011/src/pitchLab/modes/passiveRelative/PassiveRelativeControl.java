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

public 	class PassiveRelativeControl extends pitchLab.pianoWindow.PianoWindowListener
{ 
	private PianoWindow pw;
	private int state = 0;
	private PassiveRelativeMethods prm;
	/**
	 * SWITCH STATEMENT state MODES
	 * case 0:	haven't started yet
	 * case 1: 	Working/Busy/Random Tones Playing
	 * case 2:	single note playing, user needs to drag bar to location, enter confirms choice
	 * case 3:	means user can drag the bar! (ie: pressed in the correct location)
	 * case 4:  user hasn't heard first tone yet
	 * case 5: 	user hasn't heard second tone yet
	 * case 6:	
	 */
	public PassiveRelativeControl(PianoWindow pw)
	{
		this.pw = pw;
		this.pw.toolTip = new GToolTip(pw);
		this.pw.setTitle("Press the \"Enter\" key to begin.");
		this.prm = new PassiveRelativeMethods(pw);
	}
	
	//
    // 		MOUSE PRESSED
	//
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
	
    //
    // 		MOUSE DRAGGED
    //
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
	
	//
    // 		MOUSE RELEASED
	//
	public void mouseReleased(MouseEvent e)
	{
		if (state == 3)
		{
			state = 2;
			pw.toolTip.setVisible(false);
		}
		
	}
	
   
	public void keyPressed(KeyEvent e)
	{
		switch (e.getKeyCode())
		{
			case KeyEvent.VK_ENTER:
			{
				switch(state)
				{
					case 0://hasen't started yet
					{
						state = 1; //busy
						pw.setTitle("Testing....");
						cycleAndPlay();
						state = 4; //waits for user to play things etc
						break;
					}//end case
					case 2: //basically means user solitifies answer
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
					}// end case 2
				} //end switch
				break;
			} // end case ENTER
			case KeyEvent.VK_1:	// to play first relative note
			{
				if(!pw.contSine.getPlaying() && state != 5)
				{
					pw.contSine.play(DynmVar.getFirstFreq());
					if (state == 4)
						state++;
				}	
				break;
			}
			case KeyEvent.VK_2: // to play second relative note
			{
				if(!pw.contSine.getPlaying() && state != 4)
				{
					pw.contSine.play(DynmVar.getSecondFreq());
				}
				break;
			}//end case
		}//end switch
	}//end keyPressed

	
	public void keyReleased(KeyEvent e)
	{
		switch(e.getKeyCode())
		{
			case KeyEvent.VK_1:	// to play first relative note
			{
				if(pw.contSine.getPlaying(DynmVar.getFirstFreq()))
				{
					pw.contSine.stop();
				}
				break;
			} //end case block
			case KeyEvent.VK_2: // to play second relative note
			{
				if(pw.contSine.getPlaying(DynmVar.getSecondFreq()))
				{
					pw.contSine.stop();
					if(state == 5)
						state = 2;
				}
				break;
			}// end case block
		}//end switch statement
	} //end key release
	
	//
	//	CYCLE AND PLAY
	//
	private void cycleAndPlay()
	{
		prm.pr_makeIntervalToSet();
		if(DynmVar.syncResults)
			prm.pr_recordIntervalToSet();
		DynmVar.cycleStartTime = System.currentTimeMillis();
	}
	
	
	
    // 		IGNORED EVENTS
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseMoved(MouseEvent e){}
	public void keyTyped(KeyEvent e){}
	public void mouseClicked(MouseEvent e){}

	
}
