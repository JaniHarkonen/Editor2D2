package editor2d2.gui.body.assetpane;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
import editor2d2.model.app.SelectionManager;
import editor2d2.model.project.Asset;
import editor2d2.model.project.Project;
import editor2d2.modules.FactoryService;
import editor2d2.modules.data.asset.Data;
import editor2d2.subservice.Vendor;

public class AssetPane extends GUIComponent implements Vendor {
	
		// Reference to the project the asset pane is representing
	private Project source;
	
		// Reference to the SelectionManager that handles the selection
		// of Assets
	private SelectionManager<Asset> assetSelectionManager;
	
	
	public AssetPane(Project source) {
		this.source = source;
		this.assetSelectionManager = new SelectionManager<Asset>();
		
		Application.window.subscriptionService.register(Handles.ASSET_PANE, this);
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
	

	@SuppressWarnings("serial")
	@Override
	protected JPanel draw() {
		JPanel container = GUIUtilities.createDefaultPanel();
		container.add(new JLabel("Assets"));
		container.add(new JLabel("======================================================="));
		
			// Draw AssetItems
		for( Asset a : this.source.getAllAssets() )
		container.add((new AssetItem(this, a)).render());
		
			// Create Asset creation sub-menu
		JMenu menuCreate = new JMenu("Create");
		menuCreate.add(new JMenuItem("New folder..."));
		
			// Populate
		for( String type : FactoryService.getClassTypes() )
		{
			String title = GUIUtilities.getFirstLetterUppercase(type);
			ModalWindow modal = Application.window.getModalWindow();
			ModalView<? extends Asset> mv = FactoryService.getFactories(type).createModal(modal, true);
			
			menuCreate.add(createAssetMenuItem(title, mv));
		}
		
			// Create right-click context menu
		JPopupMenu pmAssets = new JPopupMenu();
		pmAssets.add(menuCreate);
		
		JMenuItem itemEdit = new JMenuItem(new AbstractAction("Edit") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				actionEdit();
				update();
			}
		});
		
		pmAssets.add(itemEdit);
		
		JMenuItem itemRename = new JMenuItem(new AbstractAction("Rename") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				actionRename();
				update();
			}
		});
		
		pmAssets.add(itemRename);
		
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
	
		// Opens a ModalWindow for the selected Asset
	@SuppressWarnings("unchecked")
	private void actionEdit() {
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
		
		for( Asset a : this.assetSelectionManager.getSelection() )
		Application.controller.removeAsset(a);
		
		update();
	}
}
