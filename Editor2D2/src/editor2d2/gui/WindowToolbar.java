package editor2d2.gui;


import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import editor2d2.Application;
import editor2d2.gui.modal.ModalView;
import editor2d2.gui.modal.ModalWindow;
import editor2d2.model.project.Asset;
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
	private void generate() {
		
			// Project menu
		JMenu menuProject = new JMenu("Project");
		menuProject.add(new JMenuItem("New project"));
		menuProject.add(new JMenuItem("Open project"));
		menuProject.add(new JMenuItem("Save project"));
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
}
