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
	
	
	protected AssetItem(AssetPane host, Asset source, String overrideName) {
		this.host = host;
		this.source = source;
		this.isMouseOver = false;
		this.container = GUIUtilities.createDefaultPanel();
		
		this.container.add(drawIcon());
		this.container.add(new JLabel(overrideName));
		this.container.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if( !GUIUtilities.checkLeftClick(e) )
				return;
				
				if( e.getClickCount() == 1 )
				actionSelect(e);
				
				if( e.getClickCount() == 2 )
				actionPrimaryFunction(e);
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
	
	protected AssetItem(AssetPane host, Asset source) {
		this(host, source, source.getName());
	}
	

	@Override
	protected JPanel draw() {
		if( this.isMouseOver )
		this.container.setBorder(BorderFactory.createEtchedBorder());
		else
		this.container.setBorder(BorderFactory.createEmptyBorder());
		
		if( this.host.checkSelected(this.source) )
		this.container.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		return this.container;
	}
	
		// Selects the Asset represented by the AssetItem and
		// updates the Controller and the AssetPane
	protected void actionSelect(MouseEvent e) {
		AssetPane host = this.host;
		Asset src = this.source;
		
		if( e.isControlDown() )
		{
			if( host.checkSelected(src) )
			host.deselectAsset(src);
			else
			host.addSelection(src);
		}
		else
		host.selectAsset(src);
		//Application.controller.selectAsset(this.source);
	}
	
		// Opens the editing ModalView for the Asset upon
		// double-click
	protected void actionPrimaryFunction(MouseEvent e) {
		this.host.actionEdit();
	}
	
		// Draws the AssetItem icon inside the draw-method
	protected abstract JPanel drawIcon();
}
