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

/**
 * This class contains all the functionalities of the 
 * toolbar displayed on the top edge of the master 
 * window. The toolbar contains the following features: 
 * - ability to create a new project
 * - ability to open an existing project
 * - ability to save the project (also save as)
 * - ability to compile a map
 * 
 * - ability to create any available asset
 * 
 * - set the metadata of a scene
 * - rename a scene
 * - delete a scene
 * 
 * This class also implements Subscriber as it 
 * subscribes to the HotkeyListener (which extends 
 * SubscriptionService) available through
 * Application.controller. HotkeyListener will 
 * provide WindowToolbar updates on hotkey presses so 
 * that appropriate actions can be triggered.
 * 
 * This class is a pure Swing-component meaning it 
 * extends a Swing-class JMenuBar rather than 
 * implementing it through composition as in most 
 * GUI-components.
 * 
 * @author User
 *
 */
public class WindowToolbar extends JMenuBar implements Subscriber {
	
		// Mostly unnecessary
	private static final long serialVersionUID = -1768214741403462691L;


	/**
	 * Constructs a WindowToolbar instance with the 
	 * default settings. WindowToolbar is immediately 
	 * subscribed to the HotkeyListener found in 
	 * Application.controller to listen for hotkey 
	 * presses which will be used to trigger actions 
	 * outlined below.
	 */
	public WindowToolbar() {
		generate();
		this.setFocusable(false);
		
		Application.controller.getHotkeyListener().subscribe("WindowToolbar", this);
	}
	
	/**
	 * Re-renders the toolbar by clearing its contents
	 * and repainting it.
	 */
	public void regenerate() {
		removeAll();
		generate();
		revalidate();
		repaint();
	}
	
	@Override
	public void onNotification(String handle, Vendor vendor) {
		if( handle == Handles.MODAL )	// ModalWindow updated
		regenerate();
		else if( HotkeyListener.didKeyUpdate(handle) )
		{
			HotkeyListener hl = Application.controller.getHotkeyListener();
			
			if( HotkeyListener.isSequenceHeld(hl, KeyEvent.VK_CONTROL, 'S') )
			actionOnSaveProject();	// Handle CTRL + S (save project)
			else if( HotkeyListener.isSequenceHeld(hl, KeyEvent.VK_CONTROL, 'O') )
			actionOnLoadProject();	// Handle CTRL + O (open project)
			else if( HotkeyListener.isSequenceHeld(hl, KeyEvent.VK_CONTROL, 'N') )
			actionOnNewProject();	// Handle CTRL + N (new project)
		}
	}
	
	/**
	 * Generates the toolbar itself by calling Swing's
	 * add-method on this object as it is inherited from 
	 * JMenuBar. The toolbar will be populated with 
	 * JMenus that contain dropdowns created using 
	 * JMenuItems. Each JMenuItem is coupled with the 
	 * appropriate ActionListener that triggers the 
	 * corresponding action. 
	 * 
	 * Some menu items have to be created dynamically 
	 * as Editor2D2 is meant to be a skeleton of sorts 
	 * that more specialized map editors will be built 
	 * upon. This means that the number of modules (types 
	 * of assets) is variable, thus, FactoryService is 
	 * utilized to get Asset specifications.
	 * 
	 * See FactoryService for more information on modules 
	 * and different factories.
	 */
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
	
	/**
	 * Creates a JMenuItem representing an asset in the 
	 * "Asset"-menu. Upon clicking the menu item a given 
	 * ModalView will be popped up using the ModalWindow 
	 * instance from Application.window. The ModalWindow 
	 * will have a given title.
	 * 
	 * @param title Title that the ModalWindow should 
	 * have when the menu item is clicked.
	 * @param mv ModalView to be displayed in the 
	 * ModalWindow.
	 * 
	 * @return Returns a JMenuItem representing the 
	 * asset menu item.
	 */
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

	/**
	 * Triggered upon clicking "New project". First a 
	 * confirmation yes/no dialog window will be 
	 * displayed to confirm the creation of a new project. 
	 * If the prompt is OK'd a new project will be 
	 * created and the application will de-facto reset. 
	 */
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
	
	/**
	 * Triggered upon clicking "Open project". A file 
	 * system dialog window will be displayed to 
	 * choose the project file that is to be opened. 
	 * If no project file is chosen or the prompt is 
	 * cancelled, nothing will happen. If the project 
	 * file is found, it will be read, parsed and 
	 * opened in the editor using the ProjectLoader.
	 * 
	 * See ProjectLoader for more information on 
	 * loading and parsing projects.
	 */
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
	
	/**
	 * Triggered upon clicking "Save project". A file 
	 * system dialog window will be displayed to 
	 * choose the path and the name of the file that 
	 * the project is to be saved to. If no file is 
	 * chosen or the prompt is cancelled, nothing 
	 * will happen and the project will remain 
	 * unsaved. If a valid file is chosen, the project 
	 * will be written into it using a ProjectWriter.
	 * If the project has previously been saved in a 
	 * file already, the file system dialog window 
	 * wont be brought up and, instead, the project 
	 * will be saved directly to the previous file.
	 * 
	 * See ProjectWriter for more information on 
	 * saving and writing projects into files.
	 */
	private void actionOnSaveProject() {
		Project activeProject = Application.controller.getActiveProject();
		String path = activeProject.getFilepath();
		
		if( path != null )
		(new ProjectWriter()).writeProject(path, Application.controller.getActiveProject());
		else
		actionOnSaveProjectAs();
	}
	
	/**
	 * Triggered upon clicking "Save project as". A 
	 * file system dialog window will be displayed 
	 * to choose the path and the name of the file 
	 * that the project is to be saved to. If no 
	 * file is chosen or the prompt is cancelled, 
	 * nothing will happen and the project will 
	 * remain unsaved. If a valid file is chosen, 
	 * the project will be written into it using a 
	 * ProjectWriter.
	 * 
	 * See ProjectWriter for more information on 
	 * saving and writing projects into files.
	 */
	private void actionOnSaveProjectAs() {
		FileSystemDialogSettings settings = new FileSystemDialogSettings();
		FileSystemDialogResponse res = Application.window.showSaveDialog(settings);
		
		if( !res.isApproved )
		return;
		
		Application.controller.getActiveProject().setFilepath(res.filepaths[0].getPath());
		actionOnSaveProject();
	}
	
	/**
	 * Triggered upon clicking "Rename scene". An 
	 * input dialog window will be displayed where 
	 * the new scene name can be entered. If the 
	 * input cannot be validated, nothing happens.
	 * If a valid input is entered, the scene will 
	 * be renamed.
	 */
	private void actionOnRenameScene() {
		Scene scene = Application.controller.getActiveScene();
		String newName = JOptionPane.showInputDialog(null, "Enter scene name", scene.getName());
		RequireStringName rfName = new RequireStringName();
		
		rfName.updateInput(newName);
		
		if( !rfName.checkValid() )
		return;
		
		Application.controller.renameActiveScene(newName);
	}
	
	/**
	 * Triggered upon clicking "Delete scene". The 
	 * scene that is currently active (open and 
	 * visible) in the editor will be removed. 
	 */
	private void actionOnDeleteScene() {
		Application.controller.deleteActiveScene();
	}
	
	/**
	 * Triggered upon clicking "Meta data". A 
	 * ModalWindow will be popped up displaying 
	 * MetaDataModal instance. The MetaDataModal 
	 * contains the meta data string of the active 
	 * scene.
	 */
	private void actionMetaData() {
		if( Application.controller.getActiveScene() == null )
		return;
		
		Window window = Application.window;
		window.popup("Scene meta data", new MetaDataModal(window.getModalWindow()));
	}
}
