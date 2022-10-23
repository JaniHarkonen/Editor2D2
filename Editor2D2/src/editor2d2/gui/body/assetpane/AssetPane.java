package editor2d2.gui.body.assetpane;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import editor2d2.Application;
import editor2d2.gui.GUIComponent;
import editor2d2.gui.GUIUtilities;
import editor2d2.gui.Handles;
import editor2d2.gui.modal.ModalView;
import editor2d2.gui.modal.ModalWindow;
import editor2d2.model.app.Controller;
import editor2d2.model.app.SelectionManager;
import editor2d2.model.project.Asset;
import editor2d2.model.project.Folder;
import editor2d2.modules.FactoryService;
import editor2d2.subservice.Subscriber;
import editor2d2.subservice.Vendor;

public class AssetPane extends GUIComponent implements Vendor, Subscriber {
	
		// Reference to the Folder whose Assets are being displayed
	private Folder openFolder;
	
		// Reference to the SelectionManager that handles the selection
		// of Assets
	private SelectionManager<Asset> assetSelectionManager;
	
	
	public AssetPane() {
		this.assetSelectionManager = new SelectionManager<Asset>();
		
			// Register this AssetPane
		Application.window.subscriptionService.register(Handles.ASSET_PANE, this);
		
			// Get or subscribe for the open Folder reference
		String openFolderHandle = editor2d2.model.Handles.OPEN_FOLDER;
		Controller vendor = (Controller) Application.controller.subscriptionService.get(openFolderHandle, "AssetPane", this); 
		this.openFolder = vendor.getOpenFolder();
	}
	
	
		// Returns whether a given Asset is selected
	public boolean checkSelected(Asset a) {
		return this.assetSelectionManager.checkSelected(a);
	}
	
		// Selects a given Asset
	public void selectAsset(Asset a) {
		this.assetSelectionManager.setSelection(a);
		update();
	}
	
		// Adds a given Asset to the selection
	public void addSelection(Asset a) {
		this.assetSelectionManager.addSelection(a);
		update();
	}
	
		// De-selects a given Asset from the selection
	public void deselectAsset(Asset a) {
		this.assetSelectionManager.removeSelection(a);
		update();
	}
	
	@Override
	public void onNotification(String handle, Vendor vendor) {
		if( handle.equals(editor2d2.model.Handles.OPEN_FOLDER) )
		{
			this.openFolder = ((Controller) vendor).getOpenFolder();
			update();
		}
	}


	@SuppressWarnings("serial")
	@Override
	protected JPanel draw() {
		JPanel container = GUIUtilities.createDefaultPanel();
		container.add(new JLabel("Assets"));
		container.add(new JLabel("======================================================="));
		
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
		JPopupMenu pmAssets = new JPopupMenu();
		
				// Context menu - Create
			JMenu menuCreate = new JMenu("Create");
			
					// Context menu - Create - New folder...
				JMenuItem itemNewFolder = new JMenuItem(new AbstractAction("New folder...") {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						actionNewFolder();
						update();
					}
				});
				menuCreate.add(itemNewFolder);
				
				menuCreate.add(itemNewFolder);
				
					// Context menu - Create - Populate
				for( String type : FactoryService.getClassTypes() )
				{
					String title = GUIUtilities.getFirstLetterUppercase(type);
					ModalWindow modal = Application.window.getModalWindow();
					ModalView<? extends Asset> mv = FactoryService.getFactories(type).createModal(modal, true);
					
					menuCreate.add(createAssetMenuItem(title, mv));
				}
			
			pmAssets.add(menuCreate);
			
				// Context menu - Edit
			JMenuItem itemEdit = new JMenuItem(new AbstractAction("Edit") {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					actionEdit();
					update();
				}
			});
			
			pmAssets.add(itemEdit);
			
				// Context menu - Rename
			JMenuItem itemRename = new JMenuItem(new AbstractAction("Rename") {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					actionRename();
					update();
				}
			});
			
			pmAssets.add(itemRename);
			
				// Context menu - Delete
			JMenuItem itemDelete = new JMenuItem(new AbstractAction("Delete") {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					actionDelete();
					update();
				}
			});
			
			pmAssets.add(itemDelete);
		
			// Listen for right-click
		container.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				if( GUIUtilities.checkRightClick(e) )
				pmAssets.show(e.getComponent(), e.getX(), e.getY());
			}
		});
		
		JScrollPane scroller = new JScrollPane(container);
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroller.getVerticalScrollBar().setUnitIncrement(16);
		
		JPanel containerContainer = GUIUtilities.createDefaultPanel();
		containerContainer.add(scroller);
		
		return containerContainer;
	}
	
		// Creates a clickable menu item for the "Asset"-menu
	@SuppressWarnings("serial")
	private JMenuItem createAssetMenuItem(String title, ModalView<? extends Asset> mv) {
		
		return new JMenuItem(new AbstractAction(title) {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Application.window.popup(mv);
				update();
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
		update();
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
		update();
	}
	
		// Deletes all Assets in a given list
	private void deleteMultipleAssets(ArrayList<Asset> assetList) {
		for( Asset a : assetList )
		{
			if( a instanceof Folder )
			{
				deleteMultipleAssets(((Folder) a).getAllAssets());
				continue;
			}
			
			Application.controller.removeAsset(a);
		}
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
		update();
	}
}
