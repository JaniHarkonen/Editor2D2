package editor2d2.gui.body.proppanes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;
import javax.swing.JPanel;

import editor2d2.gui.GUIUtilities;
import editor2d2.model.project.assets.Asset;
import editor2d2.model.project.assets.Image;

public class TilePropertiesPane extends PropertiesPane {
	
		// Cellular X-coordinate of the selection
	private int selectionCellX = 0;
	
		// Cellular Y-coordinate of the selection
	private int selectionCellY = 0;
	
		// Width of the selection in pixels
	private int selectionWidth = 32;
	
		// Height of the selection in pixels
	private int selectionHeight = 32;
	
	
	public TilePropertiesPane(Asset source) {
		this.source = source;
	}
	

	@Override
	protected JPanel draw() {
		JPanel container = GUIUtilities.createDefaultPanel();
		container.add(new JLabel("tile properties"));					// Caption bar
		container.add(new JLabel("Asset: " + this.source.getName()));	// Name of the asset
		
			// Palette
		container.add(new JLabel("Palette: "));
		
		BufferedImage img = ((Image) source).getImage();
		
		@SuppressWarnings("serial")
		JPanel palette = new JPanel() {
			
			@Override
			protected void paintComponent(Graphics g) {
				g.drawImage(img, 0, 0, null);
				
				Graphics2D gg = (Graphics2D) g;
				Stroke stroke = gg.getStroke();
				
				float[] dash = { 6.0f, 6.0f };
				gg.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2.0f, dash, 0.0f));
				gg.setColor(Color.RED);
				
				for( int x = 0; x < img.getWidth(); x += selectionWidth )
				gg.drawLine(x, 0, x, img.getHeight());
				
				for( int y = 0; y < img.getHeight(); y += selectionHeight )
				gg.drawLine(0, y, img.getWidth(), y);
				
				gg.setStroke(stroke);
				gg.drawRect(selectionCellX * selectionWidth, selectionCellY * selectionHeight, selectionWidth, selectionHeight);
			}
		};
		
		palette.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				if( e.getButton() == 1 )
				{
					selectionCellX = e.getX() / selectionWidth;
					selectionCellY = e.getY() / selectionHeight;
					
					update();
				}
			}
		});
		
		container.add(palette);
		
		return container;
	}
}
