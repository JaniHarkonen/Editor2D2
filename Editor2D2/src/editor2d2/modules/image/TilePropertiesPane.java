package editor2d2.modules.image;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import editor2d2.gui.GUIUtilities;
import editor2d2.gui.body.PropertiesPane;
import editor2d2.gui.components.CTextField;
import editor2d2.model.project.scene.placeable.Placeable;

public class TilePropertiesPane extends PropertiesPane {
	
		// Cellular X-coordinate of the selection
	private int selectionCellX;
	
		// Cellular Y-coordinate of the selection
	private int selectionCellY;
	
		// Width of the selection in pixels
	private int selectionWidth;
	
		// Height of the selection in pixels
	private int selectionHeight;
	
		// Text field for the cellular X-coordinate of the selection
	private CTextField txtSelectionCellX;
	
		// Text field for the cellular Y-coordinate of the selection
	private CTextField txtSelectionCellY;
	
		// Text field for the width of a selection cell
	private CTextField txtSelectionWidth;
	
		// Text field for the height of a selection cell
	private CTextField txtSelectionHeight;
	
	
	public TilePropertiesPane(Placeable source) {
		super(source);
		
		BufferedImage img = ((Tile) source).getImage().getImage();
		
		this.selectionCellX = 0;
		this.selectionCellY = 0;
		this.selectionWidth = img.getWidth();
		this.selectionHeight = img.getHeight();
		
		this.txtSelectionCellX = new CTextField("X: ");
		this.txtSelectionCellY = new CTextField("Y: ");
		this.txtSelectionWidth = new CTextField("Width: ");
		this.txtSelectionHeight = new CTextField("Height: ");
	}
	
	
		// Applies changes as set in the properties pane
	@Override
	public void actionApply(ActionEvent ae) {
		int cx = Integer.parseInt(this.txtSelectionCellX.getText());
		int cy = Integer.parseInt(this.txtSelectionCellY.getText());
		int w  = Integer.parseInt(this.txtSelectionWidth.getText());
		int h  = Integer.parseInt(this.txtSelectionHeight.getText());
		
		this.selectionCellX = clampSelectionCellX(cx);
		this.selectionCellY = clampSelectionCellY(cy);
		this.selectionWidth = w;
		this.selectionHeight = h;
		
		((Tile) source).setDrawArea(cx * w, cy * h, w, h);
		
		update();
	}
	

	@Override
	protected JPanel draw() {
		JPanel container = GUIUtilities.createDefaultPanel();
		
			// Palette
		container.add(new JLabel("Palette: "));
		
		Image src = ((Tile) this.source).getImage();
		BufferedImage img = src.getImage();
		
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
					selectionCellX = clampSelectionCellX(e.getX() / selectionWidth);
					selectionCellY = clampSelectionCellY(e.getY() / selectionHeight);
					
						// Auto-apply changes
					actionApply(null);
				}
			}
		});
		
		container.add(palette);
		
			// Palette selection cell controls
		JPanel paletteCellContainer = GUIUtilities.createDefaultPanel(GUIUtilities.BOX_LINE_AXIS);
			this.txtSelectionCellX.setText(""+this.selectionCellX);
			this.txtSelectionCellY.setText(""+this.selectionCellY);
			this.txtSelectionWidth.setText(""+this.selectionWidth);
			this.txtSelectionHeight.setText(""+this.selectionHeight);
			
			paletteCellContainer.add(this.txtSelectionCellX.render());
			paletteCellContainer.add(this.txtSelectionCellY.render());
			paletteCellContainer.add(this.txtSelectionWidth.render());
			paletteCellContainer.add(this.txtSelectionHeight.render());
			paletteCellContainer.setBorder(BorderFactory.createTitledBorder("Palette cell"));
		
		container.add(paletteCellContainer);
		
		return this.createDefaultPropertiesPaneContainer("Tile properties", container);
	}
	
		// Clamps the cellular X-coordinate of the selection
	private int clampSelectionCellX(int cx) {
		Image src = ((Tile) source).getImage();
		
		return Math.min(src.getWidth(selectionWidth) - 1, Math.max(0, cx));
	}
	
		// Clamps the cellular Y-coordinate of the selection
	private int clampSelectionCellY(int cy) {
		Image src = ((Tile) source).getImage();
		
		return Math.min(src.getHeight(selectionHeight) - 1, Math.max(0, cy));
	}
}
