package editor2d2.gui;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import editor2d2.gui.body.AssetPane;
import editor2d2.gui.body.ScenePane;
import editor2d2.gui.body.Toolbar;
import editor2d2.gui.body.layermgrpane.LayerManagerPane;
import editor2d2.gui.body.proppanes.TilePropertiesPane;

public class Root extends GUIComponent {
	
		// The index of the previously open tab
	private int previousTabIndex;
	
	
	public Root() {
		this.previousTabIndex = -1;
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
		containerRightSide.add((new TilePropertiesPane()).render());	// Placeable properties
		containerRightSide.add((new LayerManagerPane()).render()); 		// Layer manager pane
		
			// Scene-asset split
		JPanel containerTopSide = GUIUtilities.createDefaultPanel(GUIUtilities.BOX_X_AXIS);
		
		JSplitPane spAssets = new JSplitPane(SwingConstants.HORIZONTAL);
		spAssets.setContinuousLayout(true);
		
		containerTopSide.add((new Toolbar()).render());						// Toolbar
		
		JTabbedPane tpScenes = new JTabbedPane();							// Scene tabs
		tpScenes.add("+", new JPanel());
		tpScenes.add("Placehold", (new ScenePane()).render());
		tpScenes.add("Another one", (new ScenePane()).render());
		
		tpScenes.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				JTabbedPane source = (JTabbedPane) e.getSource();
				
					// If clicked on the +
				if( source.getSelectedIndex() == 0 )
				{
					//source.setSelectedIndex(previousTabIndex);
					actionCreateScene();
					return;
				}
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
	
	
		// Creates a new scene upon clicking +
	private void actionCreateScene() {
		String name = (String) JOptionPane.showInputDialog("Enter scene name:");
		
		if( name == null || name.equals("") )
		return;
		
		// CREATE SCENE, SWITCH TO ITS TAB AND UPDATE THE COMPONENT
		update();
	}
}
