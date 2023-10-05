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
import editor2d2.gui.Handles;
import editor2d2.gui.Window;
import editor2d2.gui.body.assetpane.AssetPane;
import editor2d2.model.project.Asset;
import editor2d2.subservice.Subscriber;
import editor2d2.subservice.Vendor;

/**
 * This class is used to display various modals, ModalViews.
 * The Window creates a single ModalWindow instance that is 
 * to be used by the GUI-components to display modals.
 * ModalWindow uses Swing's JDialog-class to function as 
 * the window. This JDialog will be attached to the master 
 * window.
 * <br/><br/>
 * 
 * The modal window can be popped up by calling the 
 * openModal-method and closed with the closeModal-method. 
 * openModal allows the opening of multiple ModalViews. 
 * When all ModalViews are closed the window itself will 
 * close as well.
 * <br/><br/>
 * 
 * This class is also a Subscriber as it is subscribed to 
 * the AssetPane.
 * 
 * @author User
 *
 */
public class ModalWindow extends GUIComponent implements Subscriber {
	
	/**
	 * Default width of the modal window (in pixels).
	 */
	public static final int DEFAULT_MODAL_WIDTH = 480;
	
	/**
	 * Default height of the modal window (in pixels).
	 */
	public static final int DEFAULT_MODAL_HEIGHT = 360;
	
	/**
	 * The ArrayList that will be used to store the 
	 * references to the ModalViews that are available 
	 * in the modal window.
	 */
	private ArrayList<ModalView<? extends Asset>> views;
	
	/**
	 * JDialog instance that functions as the modal 
	 * window.
	 */
	private JDialog dialog;
	
	/**
	 * A reference to the AssetPane. This is needed as 
	 * the AssetPane can be used to edit Assets and, 
	 * thus, bring up the modal window for the Assets.
	 */
	private AssetPane assetPane;
	
	/**
	 * Constructs a ModalWindow instance and attaches it 
	 * to a given Window (the master window). The 
	 * ModalWindow will also have a given title.
	 * 
	 * @param main Reference to the Window instance 
	 * functioning as the master window.
	 * @param title Title of the ModalWindow.
	 */
	public ModalWindow(Window main, String title) {
		this.views = new ArrayList<ModalView<? extends Asset>>();
		
		this.dialog = new JDialog(main.getFrame(), title, false);
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
	
	/**
	 * Constructs a ModalWindow instance and attaches it 
	 * to a given Window (the master window).
	 * 
	 * @param main Reference to the Window instance 
	 * functioning as the master window.
	 */
	public ModalWindow(Window main) {
		this(main, "Asset settings");
	}
	
	
	@Override
	public void onNotification(String handle, Vendor vendor) {
		if( handle == Handles.ASSET_PANE )
		this.assetPane = (AssetPane) vendor;
	}
	
	/**
	 * Opens a given ModalView in the ModalWindow. If the 
	 * ModalWindow is closed, it will be popped up.
	 * 
	 * @param mv Reference to the ModalView instance that 
	 * is to be displayed in the ModalWindow.
	 */
	public void openModal(ModalView<? extends Asset> mv) {
		if( mv == null )
		return;
		
		this.views.add(mv);
		update();
		
		this.dialog.setVisible(true);
		this.dialog.setLocationRelativeTo(null);
	}

	/**
	 * Opens a given ModalView in the ModalWindow. If the 
	 * ModalWindow is closed, it will be popped up. The 
	 * ModalWindow will also have a given title.
	 * <br/><br/>
	 * 
	 * When multiple ModalViews are opened, they can all 
	 * be found in the tabs listed in the ModalWindow.
	 * 
	 * @param title Title of the ModalWindow. 
	 * @param mv Reference to the ModalView instance that 
	 * is to be displayed in the ModalWindow.
	 */
	public void openModal(String title, ModalView<? extends Asset> mv) {
		if( mv == null )
		return;
		
		this.dialog.setTitle(title);
		
		this.views = new ArrayList<ModalView<? extends Asset>>();
		this.views.add(mv);
		update();
		
		this.dialog.setVisible(true);
		this.dialog.setLocationRelativeTo(null);
	}
	
	/**
	 * Closes the ModalWindow and clears the ModalViews 
	 * if requested. If the ModalViews are requested to 
	 * be cleared, they will no longer be displayed when 
	 * the ModalWindow is opened again via the 
	 * openModal-method.
	 * <br/><br/>
	 * 
	 * See the openModal-method for more information on 
	 * opening ModalViews in the ModalWindow.
	 * 
	 * @param clearViews Whether the ModalViews currently 
	 * available in the ModalWindow must be cleared so as 
	 * not to be visible next time the modal window is 
	 * opened.
	 */
	public void closeModalWindow(boolean clearViews) {
		if( clearViews )
		closeAllViews();
		
		this.dialog.dispatchEvent(new WindowEvent(this.dialog, WindowEvent.WINDOW_CLOSING));
	}
	
	/**
	 * Closes the ModalWindow and clears the ModalViews 
	 * currently available in the window.
	 * <br/><br/>
	 * 
	 * To avoid clearing the ModalViews upon closing the 
	 * window, see the closeModalWindow(boolean)-method.
	 */
	public void closeModalWindow() {
		closeModalWindow(true);
	}
	
	/**
	 * Closes a given ModalView currently available in 
	 * the ModalWindow. If the given ModalView is not 
	 * open, nothing happens. If the ModalView is the 
	 * last view open in the window, the window will 
	 * also be closed.
	 * 
	 * @param mv Reference to the ModalView that is to 
	 * be closed.
	 */
	public void closeModalWindow(ModalView<? extends Asset> mv) {
		if( mv == null )
		return;
		
		if( this.views.size() <= 1 )
		closeModalWindow();
		else
		{
			for( int i = 0; i < this.views.size(); i++ )
			{
				ModalView<? extends Asset> view = this.views.get(i);
				if( view != mv )
				continue;
				
				this.views.remove(i);
				update();
				return;
			}
		}
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
	
	/**
	 * Clears the list of ModalViews.
	 */
	private void closeAllViews() {
		this.views.clear();
	}
	
	// GETTERS AND SETTERS
	
	/**
	 * Returns a reference to the JDialog instance 
	 * functioning as the modal window.
	 * 
	 * @return Reference to the modal window 
	 * JDialog instance.
	 */
	public JDialog getDialog() {
		return this.dialog;
	}
	
		// Returns a reference to the Asset Pane
	/**
	 * Returns a reference to the AssetPane that 
	 * the ModalWindow is subscribed to.
	 * 
	 * @return Reference to the AssetPane.
	 */
	public AssetPane getAssetPane() {
		return this.assetPane;
	}
}
