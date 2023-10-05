package editor2d2.gui.components;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

/**
 * <b>Notice: </b>this class does NOT extend GUIComponent, 
 * instead, it is a pure Swing-component meaning it 
 * extends a Swing-component rather than implementing 
 * Swing-components through composition. General 
 * GUI-component that renders a color preview panel that 
 * consists of a rectangular JPanel that fills a rectangle 
 * using the previously selected color. A given value will 
 * also be drawn onto the rectangle (typically a data cell 
 * value, see data-module in "modules"-package for more 
 * information).
 * 
 * See GUIComponent for more information on non-Swing-
 * components. GUIComponents are the most common form of 
 * GUI-component in this application.
 * 
 * @author User
 *
 */
@SuppressWarnings("serial")
public class ColorPreviewPanel extends JPanel {
	
	/**
	 * Default color preview panel width (in pixels).
	 */
	public static final int DEFAULT_COLOR_PANEL_WIDTH = 64;
	
	/**
	 * Default color preview panel height (in pixels).
	 */
	public static final int DEFAULT_COLOR_PANEL_HEIGHT = 64;
	
	/**
	 * Inverse colors whose brightness value is above 
	 * this threshold will be rendered as black.
	 */
	public static final float DEFAULT_INVERSE_COLOR_DEFAULT_BRIGHTNESS_THRESHOLD_MIN = 0.45f;
	
	/**
	 * Inverse colors whose brightness value is below 
	 * this threshold will be rendered as black too.
	 */
	public static final float DEFAULT_INVERSE_COLOR_DEFAULT_BRIGHTNESS_THRESHOLD_MAX = 0.55f;
	
	/**
	 * Color to be displayed in the color preview panel.
	 * The last color selection.
	 */
	private Color color;
	
	/**
	 * The value that is to be overlayed on the color 
	 * preview panel.
	 */
	private String value;
	
	/**
	 * Width of the color preview panel (in pixels).
	 */
	private int colorPanelWidth;
	
	/**
	 * Height of the color preview panel (in pixels).
	 */
	private int colorPanelHeight;
	
	/**
	 * Constructs a ColorPreviewPanel instance that 
	 * displays a given color and value and has the 
	 * given width and height.
	 * 
	 * @param color Color to be displayed in the color 
	 * preview panel.
	 * @param value Value that is to be overlayed on 
	 * the color preview panel. 
	 * @param width Width of the color preview panel (in 
	 * pixels).
	 * @param height Height of the color preview panel 
	 * (in pixels).
	 */
	public ColorPreviewPanel(Color color, String value, int width, int height) {
		this.color = color;
		this.value = value;
		
		this.colorPanelWidth = width;
		this.colorPanelHeight = height;
	}
	
	/**
	 * Constructs a ColorPreivewPanel instance with the 
	 * default configuration including the default 
	 * dimensions. The ColorPreviewPanel will have a 
	 * given color and value.
	 * 
	 * @param color Color to be displayed in the color 
	 * preview panel.
	 * @param value Value that is to be overlayed on 
	 * the color preview panel. 
	 */
	public ColorPreviewPanel(Color color, String value) {
		this(color, value, DEFAULT_COLOR_PANEL_WIDTH, DEFAULT_COLOR_PANEL_HEIGHT);
	}
	
	/**
	 * Constructs a ColorPreivewPanel instance with the 
	 * default configuration including NULL color and 
	 * NULL value.
	 */
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
	
	// GETTERS AND SETTERS

	/**
	 * Returns the width of the ColorPreviewPanel
	 * (in pixels).
	 * 
	 * @return ColorPreviewPanel width (in pixels).
	 */
	public int getColorPanelWidth() {
		return this.colorPanelWidth;
	}
	
	/**
	 * Returns the height of the ColorPreivewPanel
	 * (in pixels).
	 * 
	 * @return ColorPreviewPanel height (in pixels).
	 */
	public int getColorPanelHeight() {
		return this.colorPanelHeight;
	}
	
	/**
	 * Sets the ColorPreviewPanel width and height
	 * (in pixels).
	 * 
	 * @param width New width of the ColorPreviewPanel
	 * (in pixels).
	 * @param height New height of the ColorPreviewPanel 
	 * (in pixels).
	 */
	public void setColorPanelDimensions(int width, int height) {
		this.colorPanelWidth = width;
		this.colorPanelHeight = height;
	}
}
