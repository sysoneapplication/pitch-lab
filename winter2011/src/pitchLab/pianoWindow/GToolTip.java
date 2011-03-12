package pitchLab.pianoWindow;

import java.awt.Color;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JWindow;

public class GToolTip extends JWindow
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel tooltip;
	private int xFudge = 10;
	private int yFudge = 4;

	public GToolTip()
	{
		tooltip = new JLabel();
		setup();
	}
	public GToolTip(String tip)
	{
		tooltip = new JLabel();
		this.setTip(tip);
		setup();
	}
	public GToolTip(JFrame frame)
	{
		super(frame);
		tooltip = new JLabel();
		setup();
	}
	public GToolTip(JFrame frame, String tip)
	{
		super(frame);
		tooltip = new JLabel();
		this.setTip(tip);
		setup();
	}
	private void setup()
	{
		this.add(tooltip);
		this.pack();
		this.setBackground(new Color(226,226,149));
		setTextColor(Color.BLUE);
	}
	
	public void setLocation(MouseEvent e)
	{
		this.setLocation(e.getXOnScreen()+getXFudge(),e.getYOnScreen()+getYFudge() );
	}
	public void setLocationOffsets(int xFudge, int yFudge)
	{
		this.setXFudge(xFudge);
		this.setYFudge(yFudge);
	}
	public void setXFudge(int xFudge)
	{
		this.xFudge = xFudge;
	}
	public int getXFudge()
	{
		return this.xFudge;
	}
	public void setYFudge(int yFudge)
	{
		this.yFudge = yFudge;
	}
	public int getYFudge()
	{
		return this.yFudge;
	}
	
	public void setTextColor(Color color)
	{
		this.tooltip.setForeground(color);
	}
	public Color getTextColor()
	{
		return this.tooltip.getForeground();
	}
	
	public void setTip(String tip)
	{
		this.tooltip.setText(tip);
		this.pack();
	}
	public String getTip()
	{
		return this.tooltip.getText();
	}
	
	
}
