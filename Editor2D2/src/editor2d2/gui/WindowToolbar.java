package editor2d2.gui;


import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class WindowToolbar {
	
		// Renders and returns the toolbar that can be then attached to the frame
	public JMenuBar get() {
		JMenuBar toolbar = new JMenuBar();
		
			// Project menu
		JMenu menuProject = new JMenu("Project");
		menuProject.add(new JMenuItem("New project"));
		menuProject.add(new JMenuItem("Open project"));
		menuProject.add(new JMenuItem("Save project"));
		menuProject.add(new JMenuItem("Save project as..."));
		menuProject.add(new JMenuItem("Compile map"));
		
			// Asset menu
		JMenu menuAsset = new JMenu("Asset");
		menuAsset.add(new JMenuItem("Create image"));
		menuAsset.add(new JMenuItem("Create object"));
		menuAsset.add(new JMenuItem("Create data"));
		
			// Meta data settings
		JMenu settingsMetaData = new JMenu("Meta data");
		
		toolbar.add(menuProject);
		toolbar.add(menuAsset);
		toolbar.add(settingsMetaData);
		return toolbar;
	}
}
