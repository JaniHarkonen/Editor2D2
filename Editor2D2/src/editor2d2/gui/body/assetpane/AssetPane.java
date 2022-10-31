package editor2d2.gui.body.assetpane;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import editor2d2.Application;
import editor2d2.gui.GUIComponent;
import editor2d2.gui.GUIUtilities;
import editor2d2.gui.Handles;
import editor2d2.gui.modal.ModalView;
import editor2d2.gui.modal.ModalWindow;
import editor2d2.model.app.Controller;
import editor2d2.model.app.HotkeyListener;
import editor2d2.model.app.SelectionManager;
import editor2d2.model.project.Asset;
import editor2d2.model.project.Folder;
import editor2d2.model.project.Project;
import editor2d2.modules.AbstractFactories;
import editor2d2.modules.FactoryService;
import editor2d2.subservice.Subscriber;
import editor2d2.subservice.Vendor;

public class AssetPane extends GUIComponent implements Vendor, Subscriber {
	
		// Reference to the Folder whose Assets are being displayed
	private Folder openFolder;
	
		// Reference to the SelectionManager that handles the selection
		// of Assets
	private SelectionManager<Asset> assetSelectionManager;
	
		// Reference to the JScrollPane that can be used to browse the
		// AssetPane
	private JScrollPane scrollPane;
	
		// Current JScrollPane position
	private int scrollPanePosition;
	
		// Reference to the Asset popup menu
	private JPopupMenu pmAssets;
	
	
	public AssetPane() {
		this.assetSelectionManager = new SelectionManager<Asset>();
		this.pmAssets = null;
		
			// Register this AssetPane
		Application.window.subscriptionService.register(Handles.ASSET_PANE, this);
		
		this.scrollPanePosition = 0;
		
			// Get or subscribe for the open Folder reference
		String openFolderHandle = editor2d2.model.Handles.OPEN_FOLDER;
		Controller vendor = (Controller) Application.controller.subscriptionService.get(openFolderHandle, "AssetPane", this); 
		this.openFolder = vendor.getOpenFolder();
		
			// Subscribe for keyboard input
		Application.controller.getHotkeyListener().subscribe("AssetPane", this);
	}
	
	
		// Returns whether a given Asset is selected
	public boolean checkSelected(Asset a) {
		return this.assetSelectionManager.checkSelected(a);
	}
	
		// Selects a given Asset
	public void selectAsset(Asset a) {
		this.assetSelectionManager.setSelection(a);
		updateWithState();
	}
	
		// Adds a given Asset to the selection
	public void addSelection(Asset a) {
		this.assetSelectionManager.addSelection(a);
		updateWithState();
	}
	
		// De-selects a given Asset from the selection
	public void deselectAsset(Asset a) {
		this.assetSelectionManager.removeSelection(a);
		updateWithState();
	}
	
	@Override
	public void onNotification(String handle, Vendor vendor) {
		switch( handle )
		{
			case editor2d2.model.Handles.OPEN_FOLDER:
				this.openFolder = ((Controller) vendor).getOpenFolder();
				updateWithState();
				break;
			
			case HotkeyListener.KEY_UPDATED_HANDLE:
				HotkeyListener hl = (HotkeyListener) vendor;
				ModalWindow mw = Application.window.getModalWindow();
				
				for( String assetClass : FactoryService.getClassTypes() )
				{
					AbstractFactories<? extends Asset> factories = FactoryService.getFactories(assetClass);
					char shortcut = factories.getAssetCreationShortcut();
					if( HotkeyListener.isSequenceHeld(hl, KeyEvent.VK_CONTROL, KeyEvent.VK_SHIFT, shortcut) )
					Application.window.popup(factories.createModal(mw, true));
				}
				
				break;
		}
	}
	
		// Saves the position of the scroll bar and updates the
		// component
	public void updateWithState() {
		this.scrollPanePosition = this.scrollPane.getVerticalScrollBar().getValue() - 4;
		update();
	}
	
		// Opens the Asset popup menu at a location provided
		// by a MouseEvent
	public void openPopup(MouseEvent e) {
		this.pmAssets.show(e.getComponent(), e.getX(), e.getY());
	}


	@SuppressWarnings("serial")
	@Override
	protected JPanel draw() {
		JPanel container = new JPanel();
		container.setBorder(BorderFactory.createTitledBorder("Assets"));
		
		GridLayout loGrid = new GridLayout(0, 3);
		loGrid.setHgap(16);
		loGrid.setVgap(16);
		container.setLayout(loGrid);
		
			// Create the "up one folder"-Folder
		Folder parentFolder = this.openFolder.getParentFolder();
		
		if( parentFolder != null )
		container.add((new FolderItem(this, parentFolder, "..").render()));
		
			// Render Assets and sub-folders
		for( Asset a : this.openFolder.getAllAssets() )
		{
			if( a instanceof Folder )
			container.add((new FolderItem(this, a)).render());
			else
			container.add(FactoryService.getFactories(a.getAssetClassName()).createAssetItem(this, a).render());
		}
			
			// Create right-click context menu
		this.pmAssets = new JPopupMenu();
		
				// Context menu - Create
			JMenu menuCreate = new JMenu("Create");
			
					// Context menu - Create - New folder...
				JMenuItem itemNewFolder = new JMenuItem(new AbstractAction("New folder...") {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						actionNewFolder();
						updateWithState();
					}
				});
				
				menuCreate.add(itemNewFolder);
				
					// Context menu - Create - Populate Assets
				for( String type : FactoryService.getClassTypes() )
				{
					String title = GUIUtilities.getFirstLetterUppercase(type);
					ModalWindow modal = Application.window.getModalWindow();
					ModalView<? extends Asset> mv = FactoryService.getFactories(type).createModal(modal, true);
					
					menuCreate.add(createAssetMenuItem(title, mv));
				}
			
			this.pmAssets.add(menuCreate);
			
				// Context menu - Edit
			JMenuItem itemEdit = new JMenuItem(new AbstractAction("Edit") {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					actionEdit();
				}
			});
			
			this.pmAssets.add(itemEdit);
			
				// Context menu - Rename
			JMenuItem itemRename = new JMenuItem(new AbstractAction("Rename") {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					actionRename();
				}
			});
			
			this.pmAssets.add(itemRename);
			
				// Context menu - Delete
			JMenuItem itemDelete = new JMenuItem(new AbstractAction("Delete") {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					actionDelete();
				}
			});
			
			this.pmAssets.add(itemDelete);
		
			// Listen for right-click
		container.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				
				if( GUIUtilities.checkRightClick(e) )
				openPopup(e);
				else if( GUIUtilities.checkLeftClick(e) )
				{
					assetSelectionManager.deselect();
					updateWithState();
				}
				else
				{
					final int MB_BACK = 4;
					
					if( e.getButton() == MB_BACK )
					Application.controller.openFolder(openFolder.getParentFolder());
				}
			}
		});
		
			// Create the scroll pane
		this.scrollPane = new JScrollPane(container);
		this.scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
			// Adjust scroll pane position and configure
			// scroll speed
		JScrollBar verticalScrollBar = this.scrollPane.getVerticalScrollBar();
		verticalScrollBar.setUnitIncrement(16);
		verticalScrollBar.setValue(this.scrollPanePosition);
		
			// Final container for the scroll pane
		JPanel containerContainer = GUIUtilities.createDefaultPanel();
		containerContainer.add(this.scrollPane);
		
		return containerContainer;
	}
	
	
		// Creates a clickable menu item for the "Asset"-menu
	@SuppressWarnings("serial")
	private JMenuItem createAssetMenuItem(String title, ModalView<? extends Asset> mv) {
		
		return new JMenuItem(new AbstractAction(title) {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Application.window.popup(mv);
				updateWithState();
			}
		});
	}
	
		// Shows an input dialog box for creating a new Folder
	private void actionNewFolder() {
		String folderName = (String) JOptionPane.showInputDialog("Enter folder name:");
		
		if( folderName == null || folderName.equals("") )
		return;
		
		Folder newFolder = new Folder();
		newFolder.setName(folderName);
		
		Application.controller.addNewAsset(newFolder);
		updateWithState();
	}
	
		// Opens a ModalWindow for the selected Asset
	@SuppressWarnings("unchecked")
	public void actionEdit() {
		Asset asset = this.assetSelectionManager.getSelectedItem();
		
		if( asset == null )
		return;
		
		ModalWindow mw = Application.window.getModalWindow();
		ModalView<Asset> mv = (ModalView<Asset>) FactoryService.getFactories(asset.getAssetClassName()).createModal(mw, false);
		mv.setAsset(asset);
		mv.setEdited(true);
		
		Application.window.popup(mv);
	}
	
		// Shows an input dialog box for renaming the selected Asset
	private void actionRename() {
		Asset asset = this.assetSelectionManager.getSelectedItem();
		
		if( asset == null )
		return;
		
		String newName = (String) JOptionPane.showInputDialog("Enter the new name:", asset.getName());
		
		if( newName == null || newName.equals("") )
		return;
		
			// Creates a new Scene and adds it to the target project
		asset.setName(newName);
		updateWithState();
	}
	
		// Deletes all Assets in a given list
	private void deleteMultipleAssets(ArrayList<Asset> assetList) {
		for( Asset a : assetList )
		Application.controller.removeAsset(a);
	}
	
		// Deletes the currently selected Assets from the Project
	private void actionDelete() {
		if( this.assetSelectionManager.getSelection().size() <= 0 )
		return;
		
		String title = "Deletion confirmation";
		String message = "Are you sure you want to remove the selected asset(s)?";
		int type = JOptionPane.YES_NO_OPTION;
		
		int result = JOptionPane.showConfirmDialog(null, message, title, type);
		
		if( result == JOptionPane.NO_OPTION )
		return;
		
		deleteMultipleAssets(this.assetSelectionManager.getSelection());
		Project project = Application.controller.getActiveProject();
		Application.controller.getActiveProject().getRootFolder().printFolder();
		for( Asset a : project.getAllAssets() )
		System.out.println(a.getName());
		updateWithState();
	}
}
