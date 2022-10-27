package editor2d2.gui;

import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import editor2d2.Application;
import editor2d2.gui.body.Toolbar;
import editor2d2.gui.body.assetpane.AssetPane;
import editor2d2.gui.body.layermgrpane.LayerManagerPane;
import editor2d2.gui.body.proppane.PropertiesPaneContainer;
import editor2d2.gui.body.scene.ScenePane;
import editor2d2.model.Handles;
import editor2d2.model.app.Controller;
import editor2d2.model.project.Project;
import editor2d2.model.project.scene.Scene;
import editor2d2.subservice.Subscriber;
import editor2d2.subservice.Vendor;

public class Root extends GUIComponent implements Subscriber {
	
		// The index of the currently open tab
	private int currentTabIndex;
	
		// Reference to the currently active project
	private Project targetProject;
	
	
	public Root() {
		this.currentTabIndex = -1;
		
		Controller vendor = (Controller) Application.controller.subscriptionService.get(Handles.ACTIVE_PROJECT, "Root", this);
		
		if( vendor == null )
		this.targetProject = null;
		else
		this.targetProject = vendor.getActiveProject();
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
		containerRightSide.add((new LayerManagerPane()).render()); 		// Layer manager pane
		
			// Scene-asset split
		JPanel containerTopSide = GUIUtilities.createDefaultPanel(GUIUtilities.BOX_LINE_AXIS);
		
		JSplitPane spAssets = new JSplitPane(SwingConstants.HORIZONTAL);
		spAssets.setContinuousLayout(true);
		
		containerTopSide.add((new Toolbar()).render());						// Toolbar
		
		JTabbedPane tpScenes = new JTabbedPane();							// Scene tabs
		tpScenes.add("+", new JPanel());
		
			// Creates Scene tabs for all the Scenes in the target project
			// Only renders the Scene for the currently open tab
		ArrayList<Scene> scenes = this.targetProject.getAllScenes();
		for( int i = 0; i < scenes.size(); i++ )
		{
			Scene scene = scenes.get(i);
			JPanel sp_container = new JPanel();
			
			if( this.currentTabIndex == i + 1 )
			sp_container = (new ScenePane(scene)).render();
			
			tpScenes.add(scene.getName(), sp_container);
		}
		
		tpScenes.setSelectedIndex(this.currentTabIndex);
		
		tpScenes.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				onSceneTabClick((JTabbedPane) e.getSource());
			}
		});
		
		containerTopSide.add(tpScenes);
		
		spAssets.add(containerTopSide, SwingConstants.TOP);					// Scene pane
		spAssets.add((new AssetPane()).render(), SwingConstants.BOTTOM);	// Asset pane
		spAssets.setDividerLocation(Window.DEFAULT_WINDOW_HEIGHT / 2);
		
		containerLeftSide.add(spAssets);
		container.add(spHorizontal);
		
		return container;
	}
	
	
		// Handles the Scene tab clicks
	private void onSceneTabClick(JTabbedPane source) {
		
			// If clicked on the +-tab
		if( source.getSelectedIndex() == 0 )
		{
			source.setSelectedIndex(this.currentTabIndex);
			actionCreateScene();
			return;
		}
		
		this.currentTabIndex = source.getSelectedIndex();
		update();
	}
	
		// Creates a new scene upon clicking +
	private void actionCreateScene() {
		String name = (String) JOptionPane.showInputDialog("Enter scene name:");
		
		if( name == null || name.equals("") )
		return;
		
			// Creates a new Scene and adds it to the target project
		Application.controller.createNewScene(name);
		
		update();
	}


	@Override
	public void onNotification(String handle, Vendor vendor) {
		
		switch( handle )
		{
			case Handles.ACTIVE_PROJECT:
				this.targetProject = ((Controller) vendor).getActiveProject();
				break;
			
			default: return;
		}
		
		update();
	}
}
