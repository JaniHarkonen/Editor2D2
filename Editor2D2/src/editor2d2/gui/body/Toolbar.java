package editor2d2.gui.body;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
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
import editor2d2.model.Handles;
import editor2d2.model.app.HotkeyListener;
import editor2d2.model.app.Tools;
import editor2d2.model.app.tool.Tool;
import editor2d2.subservice.Subscriber;
import editor2d2.subservice.Vendor;

/**
 * This class renders the toolbar on the left side of 
 * the SceneView. The Toolbar will list all the Tools 
 * available in the application as outlined in the 
 * Tools-class. When a Tool is selected it can be 
 * used by the SceneView-component.
 * <br/><br/>
 * 
 * This class subscribes to the HotkeyListener as the 
 * Tools can also be accessed via hotkeys.
 * <br/><br/>
 * 
 * See Tool for more information on the functioning of 
 * Tools.
 * <br/><br/>
 * 
 * See Tools for the listing of all the Tools available 
 * in the application.
 * <br/><br/>
 * 
 * See SceneView for more information on using Tools in 
 * the Scene.
 * 
 * @author User
 *
 */
public class Toolbar extends GUIComponent implements Subscriber {
	
	/**
	 * The width of a toolbar item (in pixels).
	 */
	public static final int DEFAULT_TOOLBAR_ITEM_WIDTH = 48;
	
	/**
	 * The height of a toolbar item (in pixels).
	 */
	public static final int DEFAULT_TOOLBAR_ITEM_HEIGHT = 48;
	
	/**
	 * Constructs a Toolbar instance and subscribes it 
	 * to the hotkey listener as well as to the Controller.
	 * The Toolbar receives updates regarding the currently 
	 * selected Tool via the Controller.
	 */
	public Toolbar() {
		Application.controller.getHotkeyListener().subscribe("Toolbar", this);
		Application.controller.subscriptionService.subscribe(Handles.SELECTED_TOOL, "Toolbar", this);
	}
	
	
	@Override
	public void onNotification(String handle, Vendor vendor) {
		if( HotkeyListener.didKeyUpdate(handle) )
		{
			for( Tool t : Tools.getAvailableTools() )
			{
				int keyCode = KeyEvent.getExtendedKeyCodeForChar(t.getShortcutKey().toLowerCase().charAt(0));
				
				if( HotkeyListener.isSequenceHeld(vendor, keyCode) )
				{
					Application.controller.selectTool(t);
					update();
				}
			}
		}
		else if( handle.equals(Handles.SELECTED_TOOL) )
		update();
	}
	

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
			at.translate((DEFAULT_TOOLBAR_ITEM_WIDTH - icon.getWidth()) / 4, (DEFAULT_TOOLBAR_ITEM_HEIGHT - icon.getHeight()) / 4);
			
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
	
	/**
	 * Called upon clicking a tool item in the 
	 * Toolbar. Selects a given Tool and updates 
	 * the view.
	 * 
	 * @param tool Reference to the Tool that is 
	 * to be selected.
	 */
	private void actionSelectTool(Tool tool) {
		Application.controller.selectTool(tool);
		update();
	}
}
