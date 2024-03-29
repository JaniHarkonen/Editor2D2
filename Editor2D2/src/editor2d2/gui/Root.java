package editor2d2.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;

import editor2d2.Application;
import editor2d2.gui.body.Toolbar;
import editor2d2.gui.body.assetpane.AssetPane;
import editor2d2.gui.body.layermgrpane.LayerManagerPane;
import editor2d2.gui.body.proppane.PropertiesPaneContainer;
import editor2d2.gui.body.scene.SceneTabsContainer;
import editor2d2.model.Handles;
import editor2d2.model.project.scene.Camera;
import editor2d2.subservice.Subscriber;
import editor2d2.subservice.Vendor;

/**
 * An instance of this class is the root node for all 
 * GUI-components aside from the WindowToolbar and 
 * potentially other pure Swing-components.
 * 
 * See draw-method to find all GUI-components rendered 
 * under this component (they are commented).
 * 
 * This class implements both Vendor and Subscriber. It 
 * notifies components when the horizontal and vertical 
 * splits in the application are adjusted. Root also 
 * receives project updates each time the currently 
 * active project changes, such as when creating a new 
 * project or opening a project.
 * 
 * See WindowToolbar for an explanation on pure Swing-
 * components.
 * 
 * @author User
 *
 */
public class Root extends GUIComponent implements Vendor, Subscriber {
	
	public Root() {
		Application.controller.subscriptionService.get(Handles.ACTIVE_PROJECT, "Root", this);
	}
	

	@Override
	protected JPanel draw() {
		JPanel container = GUIUtilities.createDefaultPanel();
		
		JPanel containerLeftSide = GUIUtilities.createDefaultPanel();
		JPanel containerRightSide = GUIUtilities.createDefaultPanel();
		
			// Left-right split
		JSplitPane spHorizontal = new JSplitPane(SwingConstants.VERTICAL);
		spHorizontal.setContinuousLayout(true);
		spHorizontal.setDividerLocation(Window.DEFAULT_WINDOW_WIDTH / 3 * 2);
		spHorizontal.add(containerLeftSide, JSplitPane.LEFT);
		spHorizontal.add(containerRightSide, JSplitPane.RIGHT);
		
			// Horizontal split adjustment listener
		spHorizontal.addPropertyChangeListener(
			JSplitPane.DIVIDER_LOCATION_PROPERTY,
			new PropertyChangeListener() {
				
				@Override
				public void propertyChange(PropertyChangeEvent e) {
					actionHorizontalSplitPaneAdjusted(e);
				}
			}
		);
		
			// Right pane
		containerRightSide.add((new PropertiesPaneContainer()).render());	// Properties pane
		containerRightSide.add((new LayerManagerPane()).render()); 			// Layer manager pane
		
			// Scene-asset split
		JPanel containerTopSide = GUIUtilities.createDefaultPanel(GUIUtilities.BOX_LINE_AXIS);
		
		JSplitPane spAssets = new JSplitPane(SwingConstants.HORIZONTAL);
		spAssets.setContinuousLayout(true);
		
		containerTopSide.add((new Toolbar()).render());						// Toolbar
		containerTopSide.add((new SceneTabsContainer()).render());			// Scene tabs
		
		spAssets.add(containerTopSide, SwingConstants.TOP);					// Scene pane
		spAssets.add((new AssetPane()).render(), SwingConstants.BOTTOM);	// Asset pane
		spAssets.setDividerLocation(Window.DEFAULT_WINDOW_HEIGHT / 2);
		
			// Vertical split adjustment listener
		spAssets.addPropertyChangeListener(
			JSplitPane.DIVIDER_LOCATION_PROPERTY,
			new PropertyChangeListener() {
				
				@Override
				public void propertyChange(PropertyChangeEvent e) {
					actionVerticalSplitPaneAdjusted(e);
				}
			}
		);
		
		containerLeftSide.add(spAssets);
		container.add(spHorizontal);
		
		return container;
	}


	@Override
	public void onNotification(String handle, Vendor vendor) {
		if( handle.equals(Handles.ACTIVE_PROJECT) )
		update();
	}
	
	/**
	 * Called upon adjusting the horizontal split of the 
	 * horizontal split pane. This method accepts a 
	 * PropertyChangeEvent object containing statistics 
	 * regarding the adjustment event.
	 * 
	 * @param e PropertyChangeEvent object describing the 
	 * adjustment event.
	 */
	private void actionHorizontalSplitPaneAdjusted(PropertyChangeEvent e) {
		Camera activeCamera = Application.controller.getActiveScene().getCamera();
		activeCamera.setPortDimensions((int) e.getNewValue(), activeCamera.getPortHeight());
		Application.controller.subscriptionService.register(editor2d2.gui.Handles.HORIZONTAL_SPLIT_ADJUSTED, this);
	}
	
	/**
	 * Called upon adjusting the vertical split of the 
	 * vertical split pane. This method accepts a 
	 * PropertyChangeEvent object containing statistics 
	 * regarding the adjustment event.
	 * 
	 * @param e PropertyChangeEvent object describing the 
	 * adjustment event.
	 */
	private void actionVerticalSplitPaneAdjusted(PropertyChangeEvent e) {
		Camera activeCamera = Application.controller.getActiveScene().getCamera();
		activeCamera.setPortDimensions(activeCamera.getPortWidth(), (int) e.getNewValue());
		Application.controller.subscriptionService.register(editor2d2.gui.Handles.VERTICAL_SPLIT_ADJUSTED, this);
	}
}
