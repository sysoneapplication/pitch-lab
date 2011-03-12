package pitchLab.pianoWindow;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import pitchLab.reference.Constants;
import pitchLab.reference.DynmVar;

public class PianoPane extends JPanel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PianoPane(Dimension preferredSize)
	{
		PianoPane.this.setPreferredSize(preferredSize);
		PianoPane.this.setBorder(BorderFactory.createLineBorder(Color.black));
	}
	public PianoPane(int width, int height)
	{
		PianoPane.this.setPreferredSize(new Dimension(width, height));
		PianoPane.this.setBorder(BorderFactory.createLineBorder(Color.black));
	}
	
	public void paintComponent(Graphics g)
	{
		// - variable initialization 
		super.paintComponent(g);
		
		int[] keyIntW = {0,2,4,5,7,9,11,12}; //white key intervals
		int[] keyIntB = {1,3,6,8,10}; //black key intervals
		
		
		if (DynmVar.mode == Constants.PASSIVE_PITCH || DynmVar.mode == Constants.PP_PRACTICE)
		{
			for (int i=0; i < 8; i++)
			{
				whiteKeyAt(keyIntW[i]*DynmVar.keyWidth, keyIntW[i], g, Constants.SCALE);
			}//end for
		}
		else if (DynmVar.mode == Constants.PASSIVE_RELATIVE  || DynmVar.mode == Constants.PR_PRACTICE)
		{
			for (int i=0; i < 13; i++)
			{
				whiteKeyAt(i*DynmVar.keyWidth, i, g, Constants.RPSCALE);
			}//end for
		} 
		
		//Draws Black Keys
		for(int i=0; i < 5; i++)
		{
			blackKeyAt( DynmVar.keyWidth*keyIntB[i], g );
		}//end for				
		
		//Draws Ticks
		g.setColor(Color.lightGray);
		for (int i=0; i < Constants.DEF_NUM_TICKS; i++)
		{
			int x = i*DynmVar.tickSpacing;
			if ((i+5)%10 != 0)
				g.drawLine(x,Constants.WINDOW_HEIGHT,x, Constants.WINDOW_HEIGHT - Constants.SMALL_TICK_HEIGHT);
			else
				g.drawLine(x,Constants.WINDOW_HEIGHT,x, Constants.WINDOW_HEIGHT - Constants.BIG_TICK_HEIGHT);
		}

		
	}//END paint
	
	// ---------------------------------------
	// whiteKeysAt - draws white keys
	// ---------------------------------------
	public void whiteKeyAt(int xPos, int numL, Graphics g, String[] lables)
	{
		Graphics2D g2 = (Graphics2D)g;
		BasicStroke bs = new BasicStroke(2);
		
		int[] xLinePoints = {xPos, xPos, xPos + DynmVar.keyWidth, xPos + DynmVar.keyWidth};
		int[] yLinePoints = {0, Constants.whtKeyHeight, Constants.whtKeyHeight, 0};
		
		g2.setColor(Color.black);
		g2.setStroke(bs);
		g2.drawPolyline(xLinePoints, yLinePoints, 4);
		
		g.setColor(Color.black);
		if (DynmVar.mode == Constants.PASSIVE_RELATIVE)
			g.drawString(lables[numL], xPos + DynmVar.keyWidth/2 - 10, Constants.blkKeyHeight +30);
		else
			g.drawString(lables[numL], xPos + DynmVar.keyWidth/2 - 10, Constants.blkKeyHeight);
			
	}//END whiteKeysAt	
	
	// ---------------------------------------
	// blackKeysAt - draws black keys
	// ---------------------------------------
	public void blackKeyAt(int xPos, Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		BasicStroke bs = new BasicStroke(2);
		
		int[] xLinePoints = {xPos, xPos, xPos + DynmVar.keyWidth, xPos + DynmVar.keyWidth};
		int[] yLinePoints = {0, Constants.blkKeyHeight, Constants.blkKeyHeight, 0};
		
		g.setColor(Color.darkGray);
		g.fillRect(xPos, 0, DynmVar.keyWidth, Constants.blkKeyHeight);
		
		g2.setColor(Color.black);
		g2.setStroke(bs);
		g2.drawPolyline(xLinePoints, yLinePoints, 4);
		
	}//END blackKeysAt	
}