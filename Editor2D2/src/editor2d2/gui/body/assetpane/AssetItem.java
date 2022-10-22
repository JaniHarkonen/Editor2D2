package editor2d2.gui.body.assetpane;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import editor2d2.gui.GUIComponent;
import editor2d2.gui.GUIUtilities;
import editor2d2.model.project.Asset;

public abstract class AssetItem extends GUIComponent {
	
		// Reference to the hosting AssetPane
	protected AssetPane host;
	
		// Source Asset the item is based on
	protected Asset source;
	
		// Whether the mouse is hovering over the AssetItem
	protected boolean isMouseOver;
	
		// Reference to the container JPanel
	protected JPanel container;
	
	
	protected AssetItem(AssetPane host, Asset source) {
		this.host = host;
		this.source = source;
		this.isMouseOver = false;
		this.container = GUIUtilities.createDefaultPanel();
		this.container.add(drawIcon());
		this.container.add(new JLabel(this.source.getName()));
		
		this.container.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				if( e.getButton() == GUIUtilities.MB_LEFT )
				{
					//Application.controller.selectAsset(source);
					host.selectAsset(source);
				}
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				isMouseOver = true;
				update();
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				isMouseOver = false;
				update();
			}
		});
		
		this.container.setPreferredSize(new Dimension(128, 128));
	}
	

	@Override
	protected JPanel draw() {
		if( this.isMouseOver )
		this.container.setBorder(BorderFactory.createEtchedBorder());
		else
		this.container.setBorder(null);
		
		if( this.host.checkSelected(this.source) )
		this.container.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
		
		return this.container;
	}
	
	protected abstract JPanel drawIcon();
}
