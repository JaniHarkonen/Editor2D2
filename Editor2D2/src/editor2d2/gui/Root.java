package editor2d2.gui;

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
import editor2d2.subservice.Subscriber;
import editor2d2.subservice.Vendor;

public class Root extends GUIComponent implements Subscriber {
	
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
		
		containerLeftSide.add(spAssets);
		container.add(spHorizontal);
		
		return container;
	}


	@Override
	public void onNotification(String handle, Vendor vendor) {
		if( handle.equals(Handles.ACTIVE_PROJECT) )
		update();
	}
}
