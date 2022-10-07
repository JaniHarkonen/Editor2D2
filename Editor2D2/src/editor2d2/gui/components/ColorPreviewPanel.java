package editor2d2.gui.components;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ColorPreviewPanel extends JPanel {
	
		// Default color panel width
	public static final int DEFAULT_COLOR_PANEL_WIDTH = 64;
	
		// Default color panel height
	public static final int DEFAULT_COLOR_PANEL_HEIGHT = 64;
	
		// Panel color
	private Color color;
	
		// Displayed value
	private String value;
	
	
	public ColorPreviewPanel(Color color, String value) {
		this.color = color;
		this.value = value;
	}
	
	public ColorPreviewPanel() {
		this(null, null);
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		String srcValue = this.value;
		Color srcColor = this.color;
		
		int colr = srcColor.getRed(),
			colg = srcColor.getGreen(),
			colb = srcColor.getBlue();
			
		int pw = DEFAULT_COLOR_PANEL_WIDTH,
			ph = DEFAULT_COLOR_PANEL_HEIGHT;
		
			// Draw color panel
		g.setColor(srcColor);
		g.fillRect(0, 0, pw, ph);
		
			// Draw data data value
		Color invColor = Color.BLACK;
		float invColorBrightness = 1 - Color.RGBtoHSB(colr, colg, colb, null)[2];	// determine the inverse color
		
			// Prevent the inverse color from blending in edge-cases
		if( invColorBrightness < 0.45 || invColorBrightness > 0.55 )
		invColor = new Color(Color.HSBtoRGB(0, 0, invColorBrightness));
		
		g.setColor(invColor);
		
			// Center the value text
		FontMetrics fm = g.getFontMetrics();
		Rectangle2D textDimensions = fm.getStringBounds(srcValue, g);
		
		g.drawString(srcValue, (int) ((pw / 2) - textDimensions.getCenterX()), (int) ((ph / 2) - textDimensions.getCenterY()));
	}
}
