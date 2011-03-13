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
 * Generates the Interval Window which are the ticks at the bottom of the piano window.
 *
 * @author Gavin Shriver
 * @version 0.6 April 7, 2009
 */
public class IntervalPane extends JPanel
{

	private static final long serialVersionUID = 1L;

    /**
     * Sets the interval pane size 
     *
     * @param preferredSize The size to set the interval pane
     */
	public IntervalPane(Dimension preferredSize)
	{
		IntervalPane.this.setPreferredSize(preferredSize);
		IntervalPane.this.setBorder(BorderFactory.createLineBorder(Color.black));
	}
    
    /**
     * Set the interval pane width and height
     *
     * @param width The width of the interval pane in pixels
     * @param height The height of the interval pane in pixels
     */
	public IntervalPane(int width, int height)
	{
		IntervalPane.this.setPreferredSize(new Dimension(width, height));
		IntervalPane.this.setBorder(BorderFactory.createLineBorder(Color.black));
	}
	
    /**
     * Generate the interval pane by drawing all the necessary ticks.
     * Every 10th tick is made taller then the ticks in between as shown in 
     * the piano window.
     */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		g.setColor(Color.DARK_GRAY);
		for (int i=0; i < Constants.DEF_NUM_TICKS; i++)
		{
			int x = i*DynmVar.tickSpacing;
			if ((i+5)%10 != 0)
				g.drawLine(x,Constants.WINDOW_HEIGHT,x, Constants.WINDOW_HEIGHT - Constants.SMALL_TICK_HEIGHT);
			else
				g.drawLine(x,Constants.WINDOW_HEIGHT,x, Constants.WINDOW_HEIGHT - Constants.BIG_TICK_HEIGHT);
		}
	}
	
}
