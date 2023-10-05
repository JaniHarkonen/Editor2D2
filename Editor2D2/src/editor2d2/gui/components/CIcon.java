package editor2d2.gui.components;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

import javax.swing.JPanel;

import editor2d2.gui.GUIComponent;

/**
 * General GUI-component that displays a BufferedImage 
 * icon using the CImage component. An onClick-event 
 * can be attatched to the CIcon that will be triggered 
 * when the JPanel containing the icon detects a mouse
 * click. CIcons also accept a tooltip that can be 
 * displayed when the user hovers the mouse over the 
 * icon JPanel.
 * 
 * See CImage for more information on CImage-component.
 * 
 * @author User
 *
 */
public class CIcon extends GUIComponent {
	
	/**
	 * Width of the icon upon rendering (in pixels).
	 */
	public int width;
	
	/**
	 * Height of the icon upon rendering (in pixels).
	 */
	public int height;
	
	/**
	 * BufferedImage that represents the icon image.
	 */
	public BufferedImage iconImage;
	
	/**
	 * Lambda-function that will be triggered when 
	 * the icon JPanel detects a mouse click. If 
	 * set to NULL, nothing happens upon mouse click.
	 */
	public Consumer<MouseEvent> onClick;
	
	/**
	 * Tooltip text that is to be displayed 
	 * underneath the cursor when the user hovers 
	 * the mouse over the icon JPanel. If set to NULL, 
	 * no tooltip will be displayed.
	 */
	public String tooltip;
	
	/**
	 * Constructs a CIcon instance and configures it to 
	 * use a given BufferedImage as the icon. The icon 
	 * will have the given width and height. Upon clicking 
	 * the icon JPanel a given onClick-function will be 
	 * run. If the onClick-function is NULL, nothing 
	 * happens upon click. If the tooltip is NULL, no 
	 * tooltip will be displayed upon hover.
	 * 
	 * @param img BufferedImage that is to represent the 
	 * icon.
	 * @param onClick Lambda-function that wil be executed 
	 * upon clicking the icon.
	 * @param width Width of the icon (in pixels).
	 * @param height Height of the icon (in pixels).
	 */
	public CIcon(BufferedImage img, Consumer<MouseEvent> onClick, int width, int height) {
		this.iconImage = img;
		this.onClick = onClick;
		this.width = width;
		this.height = height;
		this.tooltip = null;
	}
	
	/**
	 * Constructs a CIcon instance with default 
	 * configuration (32px width and 32px height). A given 
	 * BUfferedImage will be used as the icon. Upon 
	 * clicking the icon Jpanel a given onClick-function 
	 * will be run. If the onClick-function is NULL, 
	 * nothing happens upon click. By default, the icon 
	 * will not have a tooltip.
	 * 
	 * @param img BufferedImage that is to represent the 
	 * icon.
	 * @param onClick Lambda-function that wil be executed 
	 * upon clicking the icon.
	 */
	public CIcon(BufferedImage img, Consumer<MouseEvent> onClick) {
		this(img, onClick, 32, 32);
	}
	

	@Override
	protected JPanel draw() {
		Dimension dimensions = new Dimension(this.width, this.height);
		
		AffineTransform at = new AffineTransform();
		at.scale(this.width / (double) this.iconImage.getWidth(), this.height / (double) this.iconImage.getHeight());
		
		CImage icon = new CImage(at);
		icon.setImage(this.iconImage);
		
			// Create a container to set the tool tip
		JPanel container = icon.render();
		
		if( this.tooltip != null )
		container.setToolTipText(this.tooltip);
		
		container.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				onClick.accept(e);
			}
		});
		
		container.setPreferredSize(dimensions);
		container.setMaximumSize(dimensions);
		return container;
	}

}
