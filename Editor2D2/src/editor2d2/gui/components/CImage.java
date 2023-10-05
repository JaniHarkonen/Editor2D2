package editor2d2.gui.components;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import editor2d2.gui.GUIComponent;
import editor2d2.modules.image.asset.Image;

/**
 * General GUI-component that contains a JPanel displaying 
 * a given BUfferedImage. An AffineTransform can be applied 
 * to the BufferedImage to change its position, scaling or 
 * dimensions.
 * 
 * See AffineTransform for more information on 
 * BufferedImage transformations.
 * 
 * @author User
 *
 */
public class CImage extends GUIComponent {
	
	/**
	 * Container JPanel that draws the BufferedImage.
	 */
	public JPanel container;	// Not sure why this has to be stored in a field
	
	/**
	 * BufferedImage that is to be displayed.
	 */
	public BufferedImage image;
	
	/**
	 * AffineTransform that will be applied to the 
	 * BufferedImage. If NULL, no transformations will be 
	 * applied.
	 */
	public AffineTransform affineTransform;
	
	/**
	 * Constructs a CImage instance with a given 
	 * AffineTransform that will be applied to the 
	 * BufferedImage that is drawn during rendering.
	 * If the AffineTransform is NULL, no transformation 
	 * will be applied to the BufferedImage.
	 * 
	 * @param at AffineTransform to apply to the 
	 * BufferedImage upon rendering or NULL for no 
	 * transformation (default).
	 */
	public CImage(AffineTransform at) {
		this.container = null;
		this.affineTransform = at;
	}
	
	/**
	 * Constructs a CImage instance with no additional 
	 * AffineTransformation applied to it.
	 */
	public CImage() {
		this(null);
	}
	
	/********************************************************************************/
	
	@Override
	@SuppressWarnings("serial")
	protected JPanel draw() {
		this.container = new JPanel() {
			
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D gg = (Graphics2D) g;
				
					// Draw at (0, 0) if no transform is present
				if( affineTransform == null )
				gg.drawImage(image, 0, 0, null);
				else
				gg.drawImage(image, affineTransform, null);
			}
		};
		
		double sx = 1;
		double sy = 1;
		
		if( this.affineTransform != null )
		{
			sx = this.affineTransform.getScaleX();
			sy = this.affineTransform.getScaleY();
		}
		
		int sizeWidth = (int) (this.image.getWidth() * sx);
		int sizeHeight = (int) (this.image.getHeight() * sy);
		
		this.container.setPreferredSize(new Dimension(sizeWidth, sizeHeight));
		return this.container;
	}
	
	// GETTERS AND SETTERS
	
	/**
	 * Returns a reference to the BufferedImage that will 
	 * be rendered onto the container JPanel.
	 * 
	 * @return Reference to the BufferedImage that will 
	 * be rendered.
	 */
	public BufferedImage getImage() {
		return this.image;
	}
	
	/**
	 * Returns a reference to the AffineTransform that will 
	 * be applied to the BufferedImage during rendering.
	 * 
	 * @return Reference to the AffineTransform applied 
	 * during rendering.
	 */
	public AffineTransform getAffineTransform() {
		return this.affineTransform;
	}
	
	/**
	 * Sets the BufferedImage that will be rendered onto 
	 * the container JPanel.
	 * 
	 * @param image Reference to the BufferedImage that is 
	 * to be rendered.
	 */
	public void setImage(BufferedImage image) {
		if( image == null )
		return;
		
		this.image = image;
	}
	
	/**
	 * Sets the BufferedImage that will be rendered onto 
	 * the container JPanel based on a given Image-asset.
	 * 
	 * @param image Image-asset whose BufferedImage should 
	 * be used upon rendering.
	 */
	public void setImage(Image image) {
		setImage(image.getImage());
	}
}
