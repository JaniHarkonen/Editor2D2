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
	
		// Inverse colors whose brightness is above this threshold default to black
	public static final float DEFAULT_INVERSE_COLOR_DEFAULT_BRIGHTNESS_THRESHOLD_MIN = 0.45f;
	
		// Inverse colors whose brightness is below this threshold default to black
	public static final float DEFAULT_INVERSE_COLOR_DEFAULT_BRIGHTNESS_THRESHOLD_MAX = 0.55f;
	
	
		// Panel color
	private Color color;
	
		// Displayed value
	private String value;
	
		// Color panel width
	private int colorPanelWidth;
	
		// Color panel height
	private int colorPanelHeight;
	
	
	public ColorPreviewPanel(Color color, String value, int width, int height) {
		this.color = color;
		this.value = value;
		
		this.colorPanelWidth = width;
		this.colorPanelHeight = height;
	}
	
	public ColorPreviewPanel(Color color, String value) {
		this(color, value, DEFAULT_COLOR_PANEL_WIDTH, DEFAULT_COLOR_PANEL_HEIGHT);
	}
	
	public ColorPreviewPanel() {
		this(null, null);
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		String srcValue = this.value;
		Color srcColor = this.color;
		
		int colr = srcColor.getRed(),
			colg = srcColor.getGreen(),
			colb = srcColor.getBlue();
			
		int pw = this.colorPanelWidth,
			ph = this.colorPanelHeight;
		
			// Draw color panel
		g.setColor(srcColor);
		g.fillRect(0, 0, pw, ph);
		
			// Draw data data value
		Color invColor = Color.BLACK;
		float invColorBrightness = 1 - Color.RGBtoHSB(colr, colg, colb, null)[2];	// determine the inverse color
		
			// Prevent the inverse color from blending in with the background in edge-cases
		float	tmin = DEFAULT_INVERSE_COLOR_DEFAULT_BRIGHTNESS_THRESHOLD_MIN,
				tmax = DEFAULT_INVERSE_COLOR_DEFAULT_BRIGHTNESS_THRESHOLD_MAX;
		
		if( invColorBrightness < tmin || invColorBrightness > tmax )
		invColor = new Color(Color.HSBtoRGB(0, 0, invColorBrightness));
		
		g.setColor(invColor);
		
			// Center the value text
		FontMetrics fm = g.getFontMetrics();
		Rectangle2D textDimensions = fm.getStringBounds(srcValue, g);
		
		g.drawString(srcValue, (int) ((pw / 2) - textDimensions.getCenterX()), (int) ((ph / 2) - textDimensions.getCenterY()));
	}
	

		// Returns the width of the color panel
	public int getColorPanelWidth() {
		return this.colorPanelWidth;
	}
	
		// Returns the height of the color panel
	public int getColorPanelHeight() {
		return this.colorPanelHeight;
	}
	
		// Sets the dimensions of the color panel
	public void setColorPanelDimensions(int width, int height) {
		this.colorPanelWidth = width;
		this.colorPanelHeight = height;
	}
}
