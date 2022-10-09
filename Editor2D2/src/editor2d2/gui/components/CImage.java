package editor2d2.gui.components;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import editor2d2.gui.GUIComponent;
import editor2d2.modules.image.Image;

public class CImage extends GUIComponent {
	
		// JPanel containing the elements of the component
	public JPanel container;
	
		// Reference to the Buffered Image that will be drawn by the component
	public BufferedImage image;
	
		// Affine Transform that can be used to apply transforms to the component
	public AffineTransform affineTransform;
	
	
	public CImage(AffineTransform at) {
		this.container = null;
		this.affineTransform = at;
	}
	
	public CImage() {
		this(null);
	}
	
	/********************************************************************************/
	
	@Override
	@SuppressWarnings("serial")
	protected JPanel draw() {
		container = new JPanel() {
			
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D gg = (Graphics2D) g;
				
					// Draw at (0, 0) if no transform is present
				if( affineTransform == null )
				gg.drawImage(image, 0, 0, null);
				else
				gg.drawImage(image, affineTransform, null);
			}
		};
		
		return container;
	}
	
	/***************************** GETTERS & SETTERS ********************************/
	
		// Sets the image that will be drawn by the component
		// (BufferedImage)
	public void setImage(BufferedImage image) {
		if( image == null )
		return;
		
		this.image = image;
	}
	
		// Sets the image that will be drawn by the component
		// (Image - Asset)
	public void setImage(Image image) {
		setImage(image.getImage());
	}
}
