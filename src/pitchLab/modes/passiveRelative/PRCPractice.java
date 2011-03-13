package pitchLab.modes.passiveRelative;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import pitchLab.instructWindow.InstructionWindow;
import pitchLab.pianoWindow.GToolTip;
import pitchLab.pianoWindow.PianoWindow;
import pitchLab.reference.Calculations;
import pitchLab.reference.DynmVar;

public class PRCPractice extends pitchLab.pianoWindow.PianoWindowListener
{ 
	private PianoWindow pw;
	private int state = 0;
	private PassiveRelativeMethods prm;
	/*
	 * SWITCH STATEMENT state MODES
	 * case 0:	haven't started yet
	 * case 1: 	Working/Busy/Random Tones Playing
	 * case 2:	single note playing, user needs to drag bar to location, enter confirms choice
	 * case 3:	means user can drag the bar! (ie: pressed in the correct location)
	 * case 4:  user hasn't heard first tone yet
	 * case 5: 	user hasn't heard second tone yet
	 */
	
	public PRCPractice(PianoWindow pw)
	{
		this.pw = pw;
		this.pw.instruct = new InstructionWindow(this.pw);
		this.pw.toolTip = new GToolTip(this.pw);
		this.prm = new PassiveRelativeMethods(pw);
	}
    
    // 		MOUSE CLICKED
	public void mouseClicked(MouseEvent e){}


    // 		MOUSE PRESSED
	public void mousePressed(MouseEvent e)
	{
        int x = e.getX();   // Save the x coord of the click	
        
        if (DynmVar.dragBarX-1 <= x && x <= DynmVar.dragBarX+1 && state == 2)
		{
            state = 1; //set busy
			DynmVar.dragFromX = x - DynmVar.dragBarX;  // how far from left   
			
			pw.toolTip.setLocation(e);
			pw.toolTip.setTip(Calculations.xToCents(e.getX()));
			pw.toolTip.setVisible(true);
		
			pw.instruct.setInstructionPlace(4, true);
			state = 3; // set can drag bar 
        } 

	}
	

    // 		MOUSE DRAGGED
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
            
            pw.toolTip.setLocation(e);
        	pw.toolTip.setTip(Calculations.xToCents(xp));
        	pw.instruct.setCurrAnsSelected(pw.toolTip.getTip());

        	pw.repaint();
    		
        }
		
	}
	
    // 		MOUSE RELEASED
	public void mouseReleased(MouseEvent e)
	{
		if (state ==3)
		{
			state = 2;
			pw.instruct.setInstructionPlace(3, true);
		}
	}
	
	
	public void keyPressed(KeyEvent e)
	{
		switch(e.getKeyCode())
		{
			case KeyEvent.VK_ENTER:
			{
				switch(state)
				{
					case 0: //hasent started yet
					{
						state = 1; //busy/working
						pw.setTitle("Testing....");
						cycleAndPlay();
						state = 4; //state user hasn't heard first note yet
		    			
						break;
					}
					case 2:	// next cycle // user sets answer 
					{
						state = 1; //set busy
						pw.toolTip.setVisible(false);
						pw.repaint();
						pw.instruct.nextRound(Calculations.xToCents(DynmVar.dragBarX),1);
						cycleAndPlay();
						state = 4; //set first tone hasn't been listened to
						break;
					}
				}
				
				break;
			}
			case KeyEvent.VK_1:
			{
				if(!pw.contSine.getPlaying())
				{
					pw.contSine.play(DynmVar.getFirstFreq());
					if (state == 4)
					{
						state++;
						pw.instruct.setInstructionPlace(2, true);
					}
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
	
	public void keyReleased(KeyEvent e)
	{
		switch (e.getKeyCode())
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
					{
		    			pw.instruct.setCurrAnsToSet(Calculations.getToneInterval(DynmVar.getFirstFreq(), DynmVar.getSecondFreq()));
		    			pw.instruct.setInstructionPlace(3, false);
		    			pw.instruct.setCanSetAnswer(true, true);
		    			state = 2;
					}
				}
				
				break;
			}
		}
		
	}
	
	
	//
	//	CYCLE AND PLAY
	//
	private void cycleAndPlay()
	{
		prm.pr_makeIntervalToSet();
		pw.instruct.setInstructionPlace(1, true);
	}
   
    // 		IGNORED EVENTS
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseMoved(MouseEvent e){}
	public void keyTyped(KeyEvent e){}
	
}
