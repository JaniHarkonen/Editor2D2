package editor2d2.gui.body.layermgrpane;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JPanel;

import editor2d2.Application;
import editor2d2.gui.GUIComponent;
import editor2d2.gui.GUIUtilities;
import editor2d2.gui.components.ClickableButton;
import editor2d2.model.Handles;
import editor2d2.model.app.HotkeyListener;
import editor2d2.model.project.scene.Layer;
import editor2d2.model.project.scene.Scene;
import editor2d2.modules.image.layer.TileLayer;
import editor2d2.subservice.Subscriber;
import editor2d2.subservice.Vendor;

/**
 * This class is a major GUI-component that renders the pane where 
 * the user can manage Layers of the currently active Scene. Here 
 * Layer visibility can be toggled and Layers can be added, removed,
 * edited or moved up or down in the drawing order (Layers higher 
 * up in the list are rendered first). When a Layer is edited, or a 
 * new one is added, its properties are displayed in a separate 
 * container under this component.
 * <br/><br/>
 * 
 * The Layer list itself is rendered as a column of LayerPanes that 
 * can be used to toggle the Layer visibilty or to select the 
 * underlying Layer.
 * <br/><br/>
 * 
 * This class implements both Vendor and Subscriber as it updates 
 * the Scene view when the Layers of the Scene are edited. This 
 * class also subscribes to the HotkeyListener as hotkeys can be 
 * used to create new Layers via CTRL + N.
 * <br/><br/>
 * 
 * See LayerPropertiesPane for more information on displaying the 
 * properties of Layers upon adding or editing them.
 * <br/><br/>
 * 
 * See LayerPane for more information on rendering Layers in the 
 * Layer list.
 * 
 * @author User
 *
 */
public class LayerManagerPane extends GUIComponent implements Vendor, Subscriber {

	/**
	 * LayerPropertiesPane containing the settings of the Layer 
	 * that is currently being edited in this manager pane. 
	 * When NULL, the manager pane only displays a list of all 
	 * available Layers.
	 */
	private LayerPropertiesPane editedLayerPropertiesPane;
	
	/**
	 * Constructs a LayerManagerPane instance with default settings 
	 * and subscribes it to the HotkeyListener as well as the 
	 * Controller to listen to changes in the active Scene and its 
	 * Layers.
	 */
	public LayerManagerPane() {
		this.editedLayerPropertiesPane = null;
		
		Application.controller.getHotkeyListener().subscribe("LayerManagerPane", this);
		Application.controller.subscriptionService.subscribe(Handles.ACTIVE_SCENE, "LayerManagerPane", this);
		Application.controller.subscriptionService.subscribe(Handles.LAYER_REORDER, "LayerManagerPane", this);
	}
	
	
	/**
	 * Called upon clicking a LayerPane that represents a Layer 
	 * in the list view. Simply updates the graphical 
	 * representation of the component via GUIComponent.update.
	 * <br/><br/>
	 * 
	 * See the update-method of GUIComponent for more 
	 * information on updating the graphical representation.
	 */
	public void onLayerPaneClick() {
		update();
	}
	
	@Override
	public void onNotification(String handle, Vendor vendor) {
		if( HotkeyListener.didKeyUpdate(handle) )
		{
			HotkeyListener hl = (HotkeyListener) vendor;
			
				// Add a new layer (CTRL + N)
			if( HotkeyListener.isSequenceHeld(hl, KeyEvent.VK_CONTROL, KeyEvent.VK_SHIFT, 'N') )
			onAddLayer();
		}
		else
		{
			switch( handle )
			{
					// Active Scene changed
				case Handles.ACTIVE_SCENE:
					// Layers of the active Scne were reordered
				case Handles.LAYER_REORDER:
					update();
					break;
			}
		}
	}
	

	@Override
	protected JPanel draw() {
		JPanel container = GUIUtilities.createTitledPanel("Layers", GUIUtilities.BOX_PAGE_AXIS);
		
		if( this.editedLayerPropertiesPane == null )
		{
				// Layer panes
			Scene activeScene = Application.controller.getActiveScene();
			
			if( activeScene != null )
			{
				for( Layer layer : activeScene.getLayers() )
				container.add((new LayerPane(this, layer)).render());
			}
		
				// Controls area
			JPanel containerControls = GUIUtilities.createDefaultPanel(GUIUtilities.BOX_LINE_AXIS);
				containerControls.add(new ClickableButton("+",   (e) -> { onAddLayer(); }));
				containerControls.add(new ClickableButton("-",   (e) -> { onDeleteLayer(); }));
				containerControls.add(new ClickableButton("...", (e) -> { onEditLayer(); }));
				containerControls.add(new ClickableButton("^", (e) -> { onMoveLayerUp(); }));
				containerControls.add(new ClickableButton("V", (e) -> { onMoveLayerDown(); }));
			container.add(containerControls);
		}
		else
		{
				// Controls
			JPanel	containerControls = GUIUtilities.createDefaultPanel(GUIUtilities.BOX_LINE_AXIS);
					containerControls.add(new ClickableButton("Back", (e) -> { onBackToLayerManager(); }));
					containerControls.setMaximumSize(new Dimension(Integer.MAX_VALUE, 16));
			container.add(containerControls);
			
			addEmptySpace(container, 2);
			
				// Layer properties pane
			container.add(this.editedLayerPropertiesPane.render());
		}
		
		return container;
	}
	
	/**
	 * Called upon editing a Layer. Creates a LayerPropertiesPane 
	 * instance for the currently active (selected) Layer. This 
	 * LayerPropertiesPane instance will be rendered in the draw-
	 * method containing the settings for the selected Layer.
	 * <br/><br/>
	 * 
	 * This LayerManagerPane will be passed into the 
	 * LayerPropertiesPane as the host.
	 */
	private void onEditLayer() {
		Layer layer = Application.controller.getActiveLayer();
		
		if( layer == null )
		return;
		
		this.editedLayerPropertiesPane = new LayerPropertiesPane(this, layer, false);
		update();
	}
	
	/**
	 * Called upon adding a new Layer. Creates a new Layer 
	 * (by default TileLayer) and creates a LayerPropertiesPane 
	 * instance for it which will be displayed in this manager 
	 * pane upon rendering via the draw-method.
	 * <br/><br/>
	 * 
	 * This LayerPropertiesPane will be passed into the 
	 * LayerPropertiesPane as the host.
	 */
	private void onAddLayer() {
		Scene scene = Application.controller.getActiveScene();
		TileLayer newLayer = new TileLayer(scene);
		newLayer.setName("Layer " + System.currentTimeMillis());
		
		this.editedLayerPropertiesPane = new LayerPropertiesPane(this, newLayer, true);
		update();
	}
	
	/**
	 * Called upon deleting an existing Layer. Requests the 
	 * Controller to delete the currently active Layer from the 
	 * currently active Scene.
	 */
	private void onDeleteLayer() {
		Application.controller.removeActiveLayer();
		update();
	}
	
	/**
	 * Called upon clicking the backward navigation button (<) 
	 * in the LayerPropertiesPane (if it is visible). 
	 */
	private void onBackToLayerManager() {
		closeProperties();
	}
	
	/**
	 * Called upon moving a Layer down a level. Moves the 
	 * currently active Layer of the currently active Scene 
	 * down in the Layer list and, thus, in the rendering 
	 * order. Layers lower on the list will be rendered last
	 * (on top of all the others).
	 * <br/><br/>
	 * 
	 * Simply finds the Layer in the Scene's ArrayList of 
	 * Layers and switches its position with the next Layer 
	 * in the list. If the end of the Layer list is reached, 
	 * nothing happens.
	 */
	private void onMoveLayerDown() {
		Scene activeScene = Application.controller.getActiveScene();
		Layer activeLayer = Application.controller.getActiveLayer();
		ArrayList<Layer> layers = activeScene.getLayers();
		
		for( int i = 0; i < layers.size(); i++ )
		{
			Layer l = layers.get(i);
			
			if( l == activeLayer )
			{
				if( i == layers.size() - 1 )
				break;
				
				layers.set(i, layers.get(i + 1));
				layers.set(i + 1, l);
				
				break;
			}
		}
		
		Application.controller.subscriptionService.register(Handles.LAYER_REORDER, this);
	}
	
	/**
	 * Called upon moving a Layer up a level. Moves the 
	 * currently active Layer of the currently active Scene 
	 * up in the Layer list and, thus, in the rendering 
	 * order. Layers higher on the list will be rendered first
	 * (underneath all the others).
	 * <br/><br/>
	 * 
	 * Simply finds the Layer in the Scene's ArrayList of 
	 * Layers and switches its position with the previous Layer 
	 * in the list. If the beginning of the Layer list is 
	 * reached, nothing happens.
	 */
	private void onMoveLayerUp() {
		Scene activeScene = Application.controller.getActiveScene();
		Layer activeLayer = Application.controller.getActiveLayer();
		ArrayList<Layer> layers = activeScene.getLayers();
		
		for( int i = 0; i < layers.size(); i++ )
		{
			Layer l = layers.get(i);
			
			if( l == activeLayer )
			{
				if( i == 0 )
				break;
				
				layers.set(i, layers.get(i - 1));
				layers.set(i - 1, l);
				
				break;
			}
		}
		
		Application.controller.subscriptionService.register(Handles.LAYER_REORDER, this);
	}
	
	/**
	 * Closes the LayerPropertiesPane view by simply 
	 * setting it to NULL. When the properties pane is 
	 * set to NULL, the manager pane will revert to the 
	 * default Layer list view.
	 */
	public void closeProperties() {
		this.editedLayerPropertiesPane = null;
		update();
	}
}
