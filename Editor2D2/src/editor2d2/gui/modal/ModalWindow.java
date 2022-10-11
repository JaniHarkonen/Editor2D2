package editor2d2.gui.modal;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import editor2d2.Application;
import editor2d2.gui.GUIComponent;
import editor2d2.gui.GUIUtilities;
import editor2d2.gui.Window;
import editor2d2.gui.body.assetpane.AssetPane;
import editor2d2.gui.Handles;
import editor2d2.model.project.Asset;
import editor2d2.subservice.Subscriber;
import editor2d2.subservice.Vendor;

public class ModalWindow extends GUIComponent implements Subscriber {
	
		// Default width of the Modal Window
	public static final int DEFAULT_MODAL_WIDTH = 320;
	
		// Default height of the Modal Window
	public static final int DEFAULT_MODAL_HEIGHT = 240;
	
		// List of views currently open in the Modal Window
	private ArrayList<ModalView<? extends Asset>> views;
	
		// JDialog representing the Modal Window
	private JDialog dialog;
	
		// Reference to the Asset Pane
	private AssetPane assetPane;
	
	
	public ModalWindow(Window main) {
		this.views = new ArrayList<ModalView<? extends Asset>>();
		
		this.dialog = new JDialog(main.getFrame(), "Asset settings", false);
		this.dialog.setSize(DEFAULT_MODAL_WIDTH, DEFAULT_MODAL_HEIGHT);
		this.dialog.getContentPane().add(this.element);
		this.dialog.setVisible(false);
		
			// Clear all tabs upon close
		this.dialog.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				closeAllViews();
			}
		});
		
			// Subscribe for the Asset Pane reference
		this.assetPane = (AssetPane) Application.window.subscriptionService.get(Handles.ASSET_PANE, "ModalWindow", this);
	}
	
	
	@Override
	public void onNotification(String handle, Vendor vendor) {
		if( handle == Handles.ASSET_PANE )
		this.assetPane = (AssetPane) vendor;
	}
	
		// Adds a given Modal View to the list of views and opens it
	public void openModal(ModalView<? extends Asset> mv) {
		if( mv == null )
		return;
		
		this.views.add(mv);
		update();
		
		this.dialog.setVisible(true);
		this.dialog.setLocationRelativeTo(null);
	}
	
		// Closes the modal window and clears the views if requested
	public void closeModalWindow(boolean clearViews) {
		if( clearViews )
		closeAllViews();
		
		this.dialog.dispatchEvent(new WindowEvent(this.dialog, WindowEvent.WINDOW_CLOSING));
	}
	
		// Closes the modal window and clears the views
	public void closeModalWindow() {
		closeModalWindow(true);
	}
	
	
	@Override
	protected JPanel draw() {
		JPanel container = GUIUtilities.createDefaultPanel();
		JTabbedPane tpViews = new JTabbedPane();
		
		for( ModalView<? extends Asset> mv : this.views )
		tpViews.add(mv.getAsset().getName(), mv.render());
		
		container.add(tpViews);
		return container;
	}
	
		// Closes all views and clears the list of views
	private void closeAllViews() {
		this.views.clear();
	}
	
	
		// Returns a reference to the JDialog representing the Modal Window
	public JDialog getDialog() {
		return this.dialog;
	}
	
		// Returns a reference to the Asset Pane
	public AssetPane getAssetPane() {
		return this.assetPane;
	}
}
