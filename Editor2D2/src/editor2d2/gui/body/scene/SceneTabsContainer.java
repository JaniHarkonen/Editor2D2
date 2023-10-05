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
import editor2d2.gui.body.scenectrl.SceneControlsPane;
import editor2d2.gui.components.requirements.RequireStringName;
import editor2d2.model.Handles;
import editor2d2.model.app.HotkeyListener;
import editor2d2.model.project.scene.Scene;
import editor2d2.subservice.Subscriber;
import editor2d2.subservice.Vendor;

/**
 * This class is a major GUI-component that renders the currently 
 * active Scene using ScenePane-components. The ScenePanes are 
 * rendered in their respective tabs using the Swing-component 
 * JTabbedPane.
 * <br/><br/>
 * 
 * This class also implements Subscriber as it is subscribed to 
 * the hotkey listener as well as the application Controller. The 
 * Controller is used to listen for active Scene changes.
 * <br/><br/>
 * 
 * See the ScenePane-class for more information on rendering 
 * Scenes.
 * <br/><br/>
 * 
 * See HotkeyListener for more information on listening to 
 * hotkey presses.
 * 
 * @author User
 *
 */
public class SceneTabsContainer extends GUIComponent implements Subscriber {
	
	/**
	 * The index of the currently open Scene tab.
	 */
	private int currentTabIndex;
	
	/**
	 * JTabbedPane used to render the Scene tabs as well as the 
	 * currently active Scene.
	 */
	private JTabbedPane tpScenes;
	
	/**
	 * Constructs a SceneTabsContainer instance with the default 
	 * settings and subscribes it to the HotkeyListener and the 
	 * application Controller.
	 */
	public SceneTabsContainer() {
		this.currentTabIndex = -1;
		this.tpScenes = null;
		
		Application.controller.getHotkeyListener().subscribe("SceneTabsContainer", this);
		Application.controller.subscriptionService.subscribe(Handles.ACTIVE_SCENE, "SceneTabsContainer", this);
	}
	
	@Override
	public void onNotification(String handle, Vendor vendor) {
		boolean skipUpdate = true;
		int sceneCount = Application.controller.getActiveProject().getAllScenes().size();
		
			// Hotkey press
		if( HotkeyListener.didKeyUpdate(handle) )
		{
			HotkeyListener hl = (HotkeyListener) vendor;
			
			for( int i = 1; i < 10; i++ )
			{
					// Choose tab based on the numeric key pressed (CTRL + 1)
				if( HotkeyListener.isSequenceHeld(hl, KeyEvent.VK_CONTROL, 49 + i - 1) && i - 1 < sceneCount )
				{
					this.currentTabIndex = i;
					this.tpScenes.setSelectedIndex(this.currentTabIndex);
					Application.controller.openScene(i - 1);
					return;
				}
			}
		}
		else if( handle.equals(Handles.ACTIVE_SCENE) )	// Active Scene changed
		{
			this.currentTabIndex = Application.controller.getActiveSceneIndex() + 1;
			
			if( this.currentTabIndex <= 0 )
			this.currentTabIndex = -1;
			
			skipUpdate = false;
		}
		
		if( !skipUpdate )
		update();
	}

	@Override
	protected JPanel draw() {
		JPanel container = GUIUtilities.createDefaultPanel();
		
		this.tpScenes = new JTabbedPane();
		this.tpScenes.add("+", new JPanel());
		container.add(tpScenes);
		
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
		
		container.add((new SceneControlsPane()).render());
		
		return container;
	}
	
	/**
	 * Called upon clicking a Scene tab. Changes the currently 
	 * active Scene to the selected one. If the user clicked 
	 * on the "plus"-tab (+), a new Scene will be created by 
	 * calling the actionCreateScene-method.
	 * <br/><br/>
	 * 
	 * See actionCreateScene-method for more information on 
	 * creating adding Scene tabs in the SceneTabsContainer.
	 * 
	 * @param source Reference to the JTabbedPane instance 
	 * that registered the tab click.
	 */
	private void actionSceneTabClick(JTabbedPane source) {
		
			// If clicked on the +-tab
		if( source.getSelectedIndex() == 0 )
		{
			source.setSelectedIndex(this.currentTabIndex);
			actionCreateScene();
			Application.controller.openScene(this.currentTabIndex - 1);
			
			return;
		}
		
		this.currentTabIndex = source.getSelectedIndex();
		Application.controller.openScene(source.getSelectedIndex() - 1);
	}
	
	/**
	 * Called upon clicking the "plus"-tab (+). Creates a 
	 * new Scene by popping up an input dialog box where 
	 * the name of the new Scene can be entered. If name is 
	 * valid, the Scene is created and added to the 
	 * currently active Project.
	 */
	private void actionCreateScene() {
		String name = (String) JOptionPane.showInputDialog("Enter scene name:");
		RequireStringName rfName = new RequireStringName();
		rfName.updateInput(name);
		
		if( !rfName.checkValid() )
		return;
		
			// Creates a new Scene and adds it to the target project
		Application.controller.createNewScene(name);
	}
}
