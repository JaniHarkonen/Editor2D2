package editor2d2.gui;


import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import editor2d2.Application;
import editor2d2.gui.components.requirements.RequireStringName;
import editor2d2.gui.fsysdialog.FileSystemDialogResponse;
import editor2d2.gui.fsysdialog.FileSystemDialogSettings;
import editor2d2.gui.metadata.MetaDataModal;
import editor2d2.gui.modal.ModalView;
import editor2d2.gui.modal.ModalWindow;
import editor2d2.model.app.HotkeyListener;
import editor2d2.model.project.Asset;
import editor2d2.model.project.Project;
import editor2d2.model.project.loader.ProjectLoader;
import editor2d2.model.project.scene.Scene;
import editor2d2.model.project.writer.ProjectWriter;
import editor2d2.modules.AbstractFactories;
import editor2d2.modules.FactoryService;
import editor2d2.subservice.Subscriber;
import editor2d2.subservice.Vendor;

public class WindowToolbar extends JMenuBar implements Subscriber {
	
	private static final long serialVersionUID = -1768214741403462691L;


	public WindowToolbar() {
		generate();
		
		Application.controller.getHotkeyListener().subscribe("WindowToolbar", this);
	}
	
		// Regenerates the toolbar by removing its contents and calling "generate"
		// again
	public void regenerate() {
		removeAll();
		generate();
		revalidate();
		repaint();
	}
	
	@Override
	public void onNotification(String handle, Vendor vendor) {
		if( handle == Handles.MODAL )
		regenerate();
		else if( HotkeyListener.didKeyUpdate(handle) )
		{
			HotkeyListener hl = Application.controller.getHotkeyListener();
			
			if( HotkeyListener.isSequenceHeld(hl, KeyEvent.VK_CONTROL, 'S') )
			actionOnSaveProject();
			else if( HotkeyListener.isSequenceHeld(hl, KeyEvent.VK_CONTROL, 'O') )
			actionOnLoadProject();
			else if( HotkeyListener.isSequenceHeld(hl, KeyEvent.VK_CONTROL, 'N') )
			actionOnNewProject();
		}
	}
	
		// Generates the toolbar
	@SuppressWarnings("serial")
	private void generate() {
		
			// Project menu
		JMenuItem itemNewProject = new JMenuItem(new AbstractAction("New project (Ctrl+N)") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				actionOnNewProject();
			}
		});

		JMenuItem itemOpenProject = new JMenuItem(new AbstractAction("Open project (Ctrl+O)") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				actionOnLoadProject();
			}
		});
		
		JMenuItem itemSaveProject = new JMenuItem(new AbstractAction("Save project (Ctrl+S)") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				actionOnSaveProject();
			}
		});
		
		JMenuItem itemSaveProjectAs = new JMenuItem(new AbstractAction("Save project as... (Ctrl+Shift+S)") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				actionOnSaveProjectAs();
			}
		});
		
		JMenu menuProject = new JMenu("Project");
		menuProject.add(itemNewProject);
		menuProject.add(itemOpenProject);
		menuProject.add(itemSaveProject);
		menuProject.add(itemSaveProjectAs);
		menuProject.add(new JMenuItem("Compile map"));
		
			// Asset menu
		JMenu menuAsset = new JMenu("Asset");
		Window host = (Window) Application.window.subscriptionService.get(Handles.MODAL, "WindowToolbar", this);
		
		if( host != null )
		{
			ModalWindow modal = host.getModalWindow();
			String[] ctypes = FactoryService.getClassTypes();
			
				// Populate Asset menu
			for( String type : ctypes )
			{
				AbstractFactories<? extends Asset> factories = FactoryService.getFactories(type);
				String title = "Create " + type + " (Ctrl+Shift+" + factories.getAssetCreationShortcut() + ")";
				
				menuAsset.add(createAssetMenuItem(title, factories.createModal(modal, true)));
			}
		}
		
			// Scene settings
		JMenu settingsScene = new JMenu("Scene");
		
				// Scene - Meta data
			JMenuItem itemSceneMetaData = new JMenuItem(new AbstractAction("Meta data") {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					actionMetaData();
				}
			});
		
				// Scene - Rename scene
			JMenuItem itemRenameScene = new JMenuItem(new AbstractAction("Rename scene") {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					actionOnRenameScene();
				}
			});
			
				// Scene - Delete scene
			JMenuItem itemDeleteScene = new JMenuItem(new AbstractAction("Delete scene") {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					actionOnDeleteScene();
				}
			});
			
		settingsScene.add(itemSceneMetaData);
		settingsScene.add(itemRenameScene);
		settingsScene.add(itemDeleteScene);
		
		add(menuProject);
		add(menuAsset);
		add(settingsScene);
	}
	
	
		// Creates a clickable menu item for the "Asset"-menu
	@SuppressWarnings("serial")
	private JMenuItem createAssetMenuItem(String title, ModalView<? extends Asset> mv) {
		
		return new JMenuItem(new AbstractAction(title) {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				regenerate();
				Application.window.popup(mv);
			}
		});
	}
	
		// Creates a new Project and opens it in the editor
	private void actionOnNewProject() {
		int result = JOptionPane.showConfirmDialog(
			null,
			"All unsaved changes will be lost.\n"
		  + "Are you sure you want to continue?",
		    "New project",
		    JOptionPane.OK_CANCEL_OPTION
		);
		
		if( result != JOptionPane.OK_OPTION )
		return;
		
		Project p = new Project();
		Application.controller.openProject(p);
	}
	
		// Loads a Project from an external file and opens it
		// in editor
	private void actionOnLoadProject() {
		FileSystemDialogSettings settings = new FileSystemDialogSettings();
		FileSystemDialogResponse res = Application.window.showOpenDialog(settings);
		
		if( !res.isApproved )
		return;
		
		Project p = (new ProjectLoader()).loadProject(res.filepaths[0]);
		
		if( p == null )
		{
			JOptionPane.showMessageDialog(null, "Error: File doesn't exist!", "Error loading", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		Application.controller.openProject(p);
	}
	
		// Saves the currently open Project into the file it was last
		// saved in
	private void actionOnSaveProject() {
		Project activeProject = Application.controller.getActiveProject();
		String path = activeProject.getFilepath();
		
		if( path != null )
		(new ProjectWriter()).writeProject(path, Application.controller.getActiveProject());
		else
		actionOnSaveProjectAs();
	}
	
	private void actionOnSaveProjectAs() {
		FileSystemDialogSettings settings = new FileSystemDialogSettings();
		FileSystemDialogResponse res = Application.window.showSaveDialog(settings);
		
		if( !res.isApproved )
		return;
		
		Application.controller.getActiveProject().setFilepath(res.filepaths[0].getPath());
		actionOnSaveProject();
	}
	
	private void actionOnRenameScene() {
		Scene scene = Application.controller.getActiveScene();
		String newName = JOptionPane.showInputDialog(null, "Enter scene name", scene.getName());
		RequireStringName rfName = new RequireStringName();
		
		rfName.updateInput(newName);
		
		if( !rfName.checkValid() )
		return;
		
		Application.controller.renameActiveScene(newName);
	}
	
	private void actionOnDeleteScene() {
		Application.controller.deleteActiveScene();
	}
	
	private void actionMetaData() {
		if( Application.controller.getActiveScene() == null )
		return;
		
		Window window = Application.window;
		window.popup("Scene meta data", new MetaDataModal(window.getModalWindow()));
	}
}
