package editor2d2.gui.body;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import editor2d2.Application;
import editor2d2.gui.GUIComponent;
import editor2d2.gui.GUIUtilities;
import editor2d2.gui.components.CImage;
import editor2d2.model.app.Tools;
import editor2d2.model.app.tool.Tool;

public class Toolbar extends GUIComponent {
	
	public static final int DEFAULT_TOOLBAR_ITEM_WIDTH = 48;
	public static final int DEFAULT_TOOLBAR_ITEM_HEIGHT = 48;
	

	@Override
	protected JPanel draw() {
		JPanel container = GUIUtilities.createDefaultPanel();
		JPanel gridContainer = new JPanel();
		gridContainer.setBorder(BorderFactory.createTitledBorder("Tools"));
		
		gridContainer.setLayout(new GridLayout(0, 2));
		
		int containerWidth = DEFAULT_TOOLBAR_ITEM_WIDTH * 2;
		int containerHeight = DEFAULT_TOOLBAR_ITEM_HEIGHT * 3;
		
		gridContainer.setPreferredSize(new Dimension(containerWidth, containerHeight));
		gridContainer.setMaximumSize(new Dimension(containerWidth, containerHeight));
	
		for( Tool t : Tools.getAvailableTools() )
		{
				// Tool icon
			BufferedImage icon = t.getIcon();
			AffineTransform at = new AffineTransform();
			at.translate((DEFAULT_TOOLBAR_ITEM_WIDTH - icon.getWidth()) / 4 + 1, (DEFAULT_TOOLBAR_ITEM_HEIGHT - icon.getHeight()) / 4);
			
			CImage toolIcon = new CImage(at);
			toolIcon.setImage(icon);
			
				// Create a container to set the tool tip
			JPanel toolItem = toolIcon.render();
			toolItem.setToolTipText(t.getName() + " (" + t.getShortcutKey() + ")");
			
			toolItem.addMouseListener(new MouseAdapter() {
				
				@Override
				public void mousePressed(MouseEvent e) {
					if( GUIUtilities.checkLeftClick(e) )
					actionSelectTool(t);
				}
			});
			
			if( Application.controller.getSelectedTool() == t )
			toolItem.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			
			gridContainer.add(toolItem);
		}
		
		container.add(gridContainer);
		container.setMaximumSize(new Dimension(containerWidth, Integer.MAX_VALUE));
		
		return container;
	}
	
	
		// Selects a given Tool and updates the view
	private void actionSelectTool(Tool tool) {
		Application.controller.selectTool(tool);
		update();
	}
}
