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
import editor2d2.gui.components.requirements.RequireStringName;
import editor2d2.gui.modal.ModalView;
import editor2d2.gui.modal.ModalWindow;
import editor2d2.model.app.Controller;
import editor2d2.model.app.HotkeyListener;
import editor2d2.model.app.SelectionManager;
import editor2d2.model.project.Asset;
import editor2d2.model.project.Folder;
import editor2d2.modules.AbstractFactories;
import editor2d2.modules.FactoryService;
import editor2d2.subservice.Subscriber;
import editor2d2.subservice.Vendor;

/**
 * AssetPane is a major GUI-component that lists all the 
 * Assets available in the currently active Project. 
 * Assets are displayed using AssetItems for Assets and 
 * FolderItems for Folders. Folders can be navigated by 
 * double-clicking them whereas double-clicking assets 
 * will pop them up in a ModalWindow for editing. A 
 * drop down menu can be brought up by right-clicking an 
 * AssetItem or the AssetPane itself. The menu contains 
 * buttons for creating, editing, renaming and deleting 
 * Assets and/or Folders.
 * <br/><br/>
 * 
 * This class implements both the Vendor- and Subscriber-
 * interfaces as it sends and receives information on 
 * updates on actions in the application and the 
 * AssetPane.
 * <br/><br/>
 * 
 * See AssetItem and FolderItem for more information on 
 * how Assets and Folders of the active Project are 
 * handled by the AssetPane.
 * 
 * <br/><br/>
 * See ModalWindow for more information on displaying 
 * popup windows.
 * 
 * @author User
 *
 */
public class AssetPane extends GUIComponent implements Vendor, Subscriber {
	
	/**
	 * The Folder that is currently open and whose Assets 
	 * are now being displayed in the AssetPane. 
	 * <br/><br/>
	 * 
	 * By default, this is the root Folder.
	 */
	private Folder openFolder;
	
	/**
	 * The SelectionManager used to keep track of currently 
	 * selected Assets and/or Folders.
	 */
	private SelectionManager<Asset> assetSelectionManager;
	
	/**
	 * JScrollPane that can is used to browse the 
	 * AssetPane's view.
	 */
	private JScrollPane scrollPane;
	
	/**
	 * Current vertical position of the JScrollPane. 
	 * This has to be stored in a field so as to keep 
	 * track of the value even when the AssetPane is 
	 * re-rendered.
	 */
	private int scrollPanePosition;
	
	/**
	 * The drop down menu that can be brought up by 
	 * right-clicking the AssetPane.
	 */
	private JPopupMenu pmAssets;
	
	/**
	 * Constructs an AssetPane instance by 
	 * instantiating a SelectionManager to keep track 
	 * of all the currently selected Assets/Folders.
	 * <br/><br/>
	 * The AssetPane is also subscribed to listen for 
	 * changes in the currently open Folder as well 
	 * as registered as the Vendor of actions in the 
	 * AssetPane.
	 * <br/><br/>
	 * 
	 * This class also subscribes to the 
	 * HotkeyListener as many of the actions available 
	 * through the drop down menu can also be triggered 
	 * via hotkeys.
	 * <br/><br/>
	 * 
	 * See HotkeyListener for more information on 
	 * handling hotkey presses.
	 */
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
		
			// Subscribe for ModalWindow
		Application.window.subscriptionService.subscribe(Handles.MODAL, "AssetPane", this);
	}
	
	/**
	 * Returns whether a given Asset is selected in the 
	 * AssetPane.
	 * 
	 * @param a Reference to the Asset that is to be 
	 * checked.
	 * 
	 * @return Whether the Asset is currently selected.
	 */
	public boolean checkSelected(Asset a) {
		return this.assetSelectionManager.checkSelected(a);
	}
	
	/**
	 * Selects a given Asset in the AssetPane.
	 * 
	 * @param a Reference to the Asset that is to be 
	 * selected.
	 */
	public void selectAsset(Asset a) {
		this.assetSelectionManager.setSelection(a);
		updateWithState();
	}
	
	/**
	 * Performs an additive selection on a given 
	 * Asset. If the Asset has already been selected, 
	 * nothing happens.
	 * 
	 * @param a Reference to the Asset that is to be 
	 * additively selected.
	 */
	public void addSelection(Asset a) {
		this.assetSelectionManager.addSelection(a);
		updateWithState();
	}
	
	/**
	 * De-selects a given asset from the current 
	 * selection. If the Asset is not selected, 
	 * nothing happens.
	 * 
	 * @param a Reference to the Asset that is to be 
	 * de-selected.
	 */
	public void deselectAsset(Asset a) {
		this.assetSelectionManager.removeSelection(a);
		updateWithState();
	}
	
	@Override
	public void onNotification(String handle, Vendor vendor) {
		switch( handle )
		{
			case Handles.MODAL:
				update();
				break;
		
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
	
	/**
	 * Saves the position of the JScrollBar that is 
	 * used to browse the AssetPane and updates the 
	 * component by calling the update-method.
	 * <br/><br/>
	 * 
	 * See the update-method of GUIComponent for 
	 * more information on updating GUI-components.
	 */
	public void updateWithState() {
		this.scrollPanePosition = this.scrollPane.getVerticalScrollBar().getValue() - 4;
		update();
	}
	
	/**
	 * Opens a drop down menu at the mouse position 
	 * that allows the user to create, edit, rename 
	 * and delete Assets and/or Folders. The method 
	 * takes in a MouseEvent object that contains 
	 * relevant information on the mouse-click 
	 * event that triggers this menu, including the 
	 * mouse position.
	 * 
	 * @param e MouseEvent object that contains 
	 * relevant information for opening the drop 
	 * down menu, such as the mouse position on 
	 * screen.
	 */
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
	
	/**
	 * A utility method that creates a menu item for the 
	 * "Create"-submenu found in the right-click drop down 
	 * menu and assigns the menu item a given title. The 
	 * menu item is used to create an Asset by using the 
	 * given ModalView. When the menu item is clicked the 
	 * ModalView is popped up in the ModalWindow. 
	 * 
	 * @param title Title of the menu item.
	 * @param mv Reference to the ModalView that will be 
	 * popped up in the ModalWindow to create the Asset.
	 * 
	 * @return Refrence to the created JMenuItem 
	 * representing the menu item.
	 */
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
	
	/**
	 * Called upon clicking the new folder creation 
	 * button in the "Create"-submenu found in the 
	 * right-click drop down menu. This method 
	 * brings up a JOptionPane-dialog box that 
	 * allows the user to input a name for the 
	 * folder that is to be created. If the folder 
	 * name is valid, it will be created and added 
	 * to the currently active Project.
	 */
	private void actionNewFolder() {
		String folderName = (String) JOptionPane.showInputDialog("Enter folder name:");
		
		if( folderName == null || folderName.equals("") )
		return;
		
		Folder newFolder = new Folder();
		newFolder.setName(folderName);
		
		Application.controller.addNewAsset(newFolder);
		updateWithState();
	}
	
	/**
	 * Called upon clicking the "Edit"-button found 
	 * in the right-click drop down menu. The currently 
	 * selected Asset will be popped up in the 
	 * ModalWindow for editing. If multiple Assets 
	 * are currently selected, the first selection will 
	 * only be opened.
	 */
	@SuppressWarnings("unchecked")
	public void actionEdit() {
		Asset asset = this.assetSelectionManager.getSelectedItem();
		
		if( asset == null || asset instanceof Folder )
		return;
		
		ModalWindow mw = Application.window.getModalWindow();
		ModalView<Asset> mv = (ModalView<Asset>) FactoryService.getFactories(asset.getAssetClassName()).createModal(mw, false);
		mv.setAsset(asset);
		mv.setEdited(true);
		
		Application.window.popup(mv);
	}
	
	/**
	 * Called upon clicking the "Rename"-button found 
	 * in the right-click drop down menu. A 
	 * JOptionPane-dialog box that allows the user to 
	 * input a new name for the selected Asset will 
	 * be displayed. If the entered Asset name is 
	 * valid, the Asset will be renamed. If the 
	 * prompt is cancelled, nothing happens. If 
	 * multiple Assets are currently selected, the 
	 * first selected Asset will be renamed.
	 */
	private void actionRename() {
		Asset asset = this.assetSelectionManager.getSelectedItem();
		
		if( asset == null )
		return;
		
		String newName = (String) JOptionPane.showInputDialog("Enter the new name:", asset.getName());
		RequireStringName rfName = new RequireStringName();
		rfName.updateInput(newName);
		
		if( !rfName.checkValid() )
		return;
		
			// Creates a new Scene and adds it to the target project
		asset.setName(newName);
		updateWithState();
	}
	
	/**
	 * A utility method for deleting an ArrayList of 
	 * Assets from the currently active Project.
	 * 
	 * @param assetList Reference to the ArrayList 
	 * of Assets that are to be removed from the 
	 * Project.
	 */
	private void deleteMultipleAssets(ArrayList<Asset> assetList) {
		for( Asset a : assetList )
		Application.controller.removeAsset(a);
	}
	
	/**
	 * Called upon clicking the "Delete"-button found 
	 * in the right-click drop down menu. A 
	 * JOptionPane-dialog box will be displayed to 
	 * confirm the deletion of the selected item(s).
	 * If the user chooses YES, the selected item(s) 
	 * will be deleted from the currently active 
	 * Project. If the prompt is cancelled, nothing 
	 * happens. If multiple items are selected, ALL 
	 * selected items will be removed.
	 */
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
		updateWithState();
	}
}
