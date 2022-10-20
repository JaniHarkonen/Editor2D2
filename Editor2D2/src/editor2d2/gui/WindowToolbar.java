package editor2d2.gui;


import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import editor2d2.Application;
import editor2d2.gui.fsysdialog.FileSystemDialogResponse;
import editor2d2.gui.fsysdialog.FileSystemDialogSettings;
import editor2d2.gui.modal.ModalView;
import editor2d2.gui.modal.ModalWindow;
import editor2d2.model.project.Asset;
import editor2d2.model.project.Project;
import editor2d2.model.project.loader.ProjectLoader;
import editor2d2.model.project.writer.ProjectWriter;
import editor2d2.modules.FactoryService;
import editor2d2.subservice.Subscriber;
import editor2d2.subservice.Vendor;

public class WindowToolbar extends JMenuBar implements Subscriber {
	
	private static final long serialVersionUID = -1768214741403462691L;


	public WindowToolbar() {
		generate();
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
	}
	
		// Generates the toolbar
	@SuppressWarnings("serial")
	private void generate() {
		
			// Project menu
		JMenuItem itemNewProject = new JMenuItem(new AbstractAction("New project") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				actionOnNewProject();
			}
		});

		JMenuItem itemOpenProject = new JMenuItem(new AbstractAction("Open project") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				actionOnLoadProject();
			}
		});
		
		JMenuItem itemSaveProject = new JMenuItem(new AbstractAction("Save project") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				actionOnSaveProject();
			}
		});
		
		JMenu menuProject = new JMenu("Project");
		menuProject.add(itemNewProject);
		menuProject.add(itemOpenProject);
		menuProject.add(itemSaveProject);
		menuProject.add(new JMenuItem("Save project as..."));
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
			menuAsset.add(createAssetMenuItem("Create " + type, FactoryService.getFactories(type).createModal(modal, true)));
		}
		
			// Meta data settings
		JMenu settingsMetaData = new JMenu("Meta data");
		
		add(menuProject);
		add(menuAsset);
		add(settingsMetaData);
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
		Application.controller.openProject(p);
	}
	
		// Saves the currently open Project into the file it was last
		// saved in
	private void actionOnSaveProject() {
		(new ProjectWriter()).writeProject("C:\\Users\\User\\Desktop\\SHASHASHA.txt", Application.controller.getActiveProject());
	}
}
