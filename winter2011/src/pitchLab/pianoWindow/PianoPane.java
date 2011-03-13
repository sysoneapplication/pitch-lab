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

/**
 * Builds the Piano Window GUI interface from scratch.
 *
 * [TODO] Instead of building a piano from scratch, possible to load a
 *        piano image and use it as a background - better UI and prettier.
 *
 * @author Gavin Shriver
 * @version 0.6 April 7, 2009
 *
 */
public class PianoPane extends JPanel
{
	private static final long serialVersionUID = 1L;

    /**
     * Sets the size of a piano pane
     *
     * @param preferredSize The size of the piano pane
     */
	public PianoPane(Dimension preferredSize)
	{
		PianoPane.this.setPreferredSize(preferredSize);
		PianoPane.this.setBorder(BorderFactory.createLineBorder(Color.black));
	}
    /**
     * Sets the width and height of the piano pane
     *
     * @param width The desired width of the piano pane in pixels
     * @param height The desired height of the piano pane in pixels
     */
	public PianoPane(int width, int height)
	{
		PianoPane.this.setPreferredSize(new Dimension(width, height));
		PianoPane.this.setBorder(BorderFactory.createLineBorder(Color.black));
	}
	
    /**
     * Paints the black and white keys in the piano pane in the correct
     * intervals to mimic a piano. 
     * Also draws the gray ticks at the bottom of the piano window pane.
     */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		int[] keyIntW = {0,2,4,5,7,9,11,12}; //white key intervals
		int[] keyIntB = {1,3,6,8,10}; //black key intervals
		
		
		if (DynmVar.mode == Constants.PASSIVE_PITCH || DynmVar.mode == Constants.PP_PRACTICE)
		{
			for (int i=0; i < 8; i++)
			{
				whiteKeyAt(keyIntW[i]*DynmVar.keyWidth, keyIntW[i], g, Constants.SCALE);
			}
		}
		else if (DynmVar.mode == Constants.PASSIVE_RELATIVE  || DynmVar.mode == Constants.PR_PRACTICE)
		{
			for (int i=0; i < 13; i++)
			{
				whiteKeyAt(i*DynmVar.keyWidth, i, g, Constants.RPSCALE);
			}
		} 
		
		for(int i=0; i < 5; i++)
		{
			blackKeyAt( DynmVar.keyWidth*keyIntB[i], g );
		}
		
		g.setColor(Color.lightGray);
		for (int i=0; i < Constants.DEF_NUM_TICKS; i++)
		{
			int x = i*DynmVar.tickSpacing;
			if ((i+5)%10 != 0)
				g.drawLine(x,Constants.WINDOW_HEIGHT,x, Constants.WINDOW_HEIGHT - Constants.SMALL_TICK_HEIGHT);
			else
				g.drawLine(x,Constants.WINDOW_HEIGHT,x, Constants.WINDOW_HEIGHT - Constants.BIG_TICK_HEIGHT);
		}

		
	}
	
    /**
     * This is the method that draws a white key on the piano pane
     *
     * @param xPos The position of the white key to be generated
     * @param numL The number of the white key being generated
     * @param g Graphics need to make the key
     * @param lables The label to put on key (possible misspelling in the variable)
     */
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
			
	}
	
    /**
     * This is the method that draws a black key on the piano pane.
     * These keys do not get labeled.
     *
     * @param xPos The position of the black key to be generated
     * @param g Graphics need to make the key
     */
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
		
	}
}
