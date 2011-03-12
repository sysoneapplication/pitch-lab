package pitchLab.modes.activeRelative;

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

public class ARCPractice extends pitchLab.pianoWindow.PianoWindowListener
{ 
	PianoWindow pw;
	private boolean firstTone = false;
	private int state = 0;
	/*
	 * SWITCH STATEMENT state MODES
	 * case 0:	haven't started yet
	 * case 1: 	Working/Busy/Random Tones Playing
	 * case 2:	single note playing, user needs to drag bar to location, enter confirms choice
	 * case 3:	means user can drag the bar! (ie: pressed in the correct location)
	 * case 4:  user hasn't heard first tone yet
	 * case 5: 	user hasn't heard second tone yet
	 * case 6:	user hasn't made bar appear yet
	 */
	
	public ARCPractice(PianoWindow pw)
	{
		this.pw = pw;
		this.pw.instruct = new InstructionWindow(pw);
		this.pw.toolTip = new GToolTip(pw);
	}


	@Override
	public void mousePressed(MouseEvent e)
	{
        int x = e.getX();   // Save the x coord of the click	
        
        if(!pw.getDragBarVisible() && state == 6)
        {
        	DynmVar.dragBarX = x; //e.getX() - DynmVar.dragFromX;
        	pw.setDragBarVisible(true);
        	pw.instruct.setCanSetAnswer(true, true);
        	pw.repaint();
        	state = 2;
        }
        
        if (DynmVar.dragBarX-1 <= x && x <= DynmVar.dragBarX+1 && state == 2 )
		{
            state = 3;
			DynmVar.dragFromX = x - DynmVar.dragBarX;  // how far from left 
			//pw.instruct.setInstructionPlace(4,true);
			pw.toolTip.setLocation(e);
			pw.toolTip.setVisible(pw.instruct.getShowCurrSelection());
        }
        
        
	}
	
	@Override
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
            
            ActiveRelativeMethods.blendNotes_arp(pw, xp, firstTone);
        	pw.toolTip.setLocation(e);

        	pw.repaint();
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
		if (state == 3)
		{
			state = 2;
			pw.toolTip.setVisible(false);
			pw.instruct.setInstructionPlace(3, true);
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e)
	{
		switch (e.getKeyCode())
		{
			case KeyEvent.VK_ENTER:
			{
				switch (state)
				{
					case 0: //havent started yet
						state = 1; // state is busy
						pw.instruct.setInstructionPlace(1, true);
						cycleAndPlay();
						state = 4; //set state waiting for them
				    	break;
					case 2:  //end of cycle etc
					{
						state = 1;  //set working
						int x = DynmVar.dragBarX;
						pw.instruct.nextRound(Calculations.getToneInterval(DynmVar.getFirstFreq(),ActiveRelativeMethods.arFrequencyFromX(x)),1);
						pw.repaint();
						cycleAndPlay();
						state = 4; //set back to idle 
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
					{
						state++;
						pw.instruct.setInstructionPlace(2, true);
					}
				}					
				break;
			}
			case KeyEvent.VK_2: //play second tone (user selected)
			{
				if(!pw.contSine.getPlaying() && state != 4)
				{
					pw.contSine.play(ActiveRelativeMethods.arFrequencyFromX(DynmVar.dragBarX));
					if(state == 5)
					{
						state++;
						pw.instruct.setInstructionPlace(3, true);
					}
				}
				break;
			}//end case
		}//end key switch
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		switch (e.getKeyCode())
		{
			case KeyEvent.VK_1: //play first tone
			{
				if(pw.contSine.getPlaying(DynmVar.getFirstFreq()))
				{
					pw.contSine.stop();
					firstTone = false;
				}
				break;
			}//end case
			case KeyEvent.VK_2: //play second tone
			{
				if(!firstTone)
				{
					pw.contSine.stop();
				}
				break;
			}//end case
			
		}//end switch
	}
	
	
	
	//
	//	CYCLE AND PLAY
	//
	private void cycleAndPlay()
	{
		pw.setDragBarVisible(false);
		pw.instruct.setCanSetAnswer(false, false);
		pw.repaint();
		ActiveRelativeMethods.ar_setIntervalToSet();
		pw.instruct.setCurrAnsToSet(DynmVar.noteToSet);
		pw.instruct.setCurrAnsSelected(Calculations.getToneInterval(DynmVar.getFirstFreq(),DynmVar.getFirstFreq()));
		ActiveRelativeMethods.ar_rescaleAndPlay(pw);
		//FIXME: turn off bar here.
		
	}
	
	
	//ignore these events
	public void keyTyped(KeyEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseMoved(MouseEvent e){}
	public void mouseClicked(MouseEvent e){}

	
}
