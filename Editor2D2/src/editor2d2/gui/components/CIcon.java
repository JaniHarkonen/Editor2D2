package editor2d2.gui.components;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

import javax.swing.JPanel;

import editor2d2.gui.GUIComponent;

public class CIcon extends GUIComponent {
	
	public int width;
	
	public int height;
	
	public BufferedImage iconImage;
	
	public Consumer<MouseEvent> onClick;
	
	public String tooltip;
	
	
	public CIcon(BufferedImage img, Consumer<MouseEvent> onClick, int width, int height) {
		this.iconImage = img;
		this.onClick = onClick;
		this.width = width;
		this.height = height;
		this.tooltip = null;
	}
	
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
