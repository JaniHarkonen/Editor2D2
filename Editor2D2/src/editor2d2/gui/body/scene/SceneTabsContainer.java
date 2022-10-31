package editor2d2.gui.body.scene;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import editor2d2.Application;
import editor2d2.gui.GUIComponent;
import editor2d2.gui.GUIUtilities;
import editor2d2.model.app.HotkeyListener;
import editor2d2.model.project.scene.Scene;
import editor2d2.subservice.Subscriber;
import editor2d2.subservice.Vendor;

public class SceneTabsContainer extends GUIComponent implements Subscriber {
	
	private int currentTabIndex;
	
	private JTabbedPane tpScenes;
	
	
	public SceneTabsContainer() {
		this.currentTabIndex = -1;
		this.tpScenes = null;
		
		Application.controller.getHotkeyListener().subscribe("SceneTabsContainer", this);
	}
	
	
	@Override
	public void onNotification(String handle, Vendor vendor) {
		if( !HotkeyListener.didKeyUpdate(handle) )
		return;
		
		HotkeyListener hl = (HotkeyListener) vendor;
		boolean skipUpdate = false;
		int sceneCount = Application.controller.getActiveProject().getAllScenes().size();
		
		for( int i = 1; i < 10; i++ )
		if( HotkeyListener.isSequenceHeld(hl, KeyEvent.VK_CONTROL, 49 + i - 1) && i - 1 < sceneCount )
		{
			this.currentTabIndex = i;
			this.tpScenes.setSelectedIndex(this.currentTabIndex);
			break;
		}
		
		if( !skipUpdate )
		update();
	}
	

	@Override
	protected JPanel draw() {
		JPanel container = GUIUtilities.createDefaultPanel();
		
		this.tpScenes = new JTabbedPane();
		this.tpScenes.add("+", new JPanel());
		
			// Creates Scene tabs for all the Scenes in the target project
			// Only renders the Scene for the currently open tab
		ArrayList<Scene> scenes = Application.controller.getActiveProject().getAllScenes();
		
		for( int i = 0; i < scenes.size(); i++ )
		{
			Scene scene = scenes.get(i);
			JPanel sp_container = new JPanel();
			
			if( this.currentTabIndex == i + 1 )
			sp_container = (new ScenePane(scene)).render();
			
			this.tpScenes.add(scene.getName(), sp_container);
		}
		
		this.tpScenes.setSelectedIndex(this.currentTabIndex);
		
		this.tpScenes.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				actionSceneTabClick((JTabbedPane) e.getSource());
			}
		});
		
		container.add(tpScenes);
		
		return container;
	}
	
		// Handles the Scene tab clicks
	private void actionSceneTabClick(JTabbedPane source) {
		
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
}
