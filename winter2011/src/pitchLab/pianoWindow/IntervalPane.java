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

public class IntervalPane extends JPanel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IntervalPane(Dimension preferredSize)
	{
		IntervalPane.this.setPreferredSize(preferredSize);
		IntervalPane.this.setBorder(BorderFactory.createLineBorder(Color.black));
	}
	public IntervalPane(int width, int height)
	{
		IntervalPane.this.setPreferredSize(new Dimension(width, height));
		IntervalPane.this.setBorder(BorderFactory.createLineBorder(Color.black));
	}
	
	public void paintComponent(Graphics g)
	{
		// - variable initialization 
		super.paintComponent(g);
		
		//Draws Ticks
		g.setColor(Color.DARK_GRAY);
		for (int i=0; i < Constants.DEF_NUM_TICKS; i++)
		{
			int x = i*DynmVar.tickSpacing;
			if ((i+5)%10 != 0)
				g.drawLine(x,Constants.WINDOW_HEIGHT,x, Constants.WINDOW_HEIGHT - Constants.SMALL_TICK_HEIGHT);
			else
				g.drawLine(x,Constants.WINDOW_HEIGHT,x, Constants.WINDOW_HEIGHT - Constants.BIG_TICK_HEIGHT);
		}

		
	}//END paint
	
}