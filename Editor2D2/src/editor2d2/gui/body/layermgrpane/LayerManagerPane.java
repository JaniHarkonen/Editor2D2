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

public class LayerManagerPane extends GUIComponent implements Vendor, Subscriber {

	private LayerPropertiesPane editedLayerPropertiesPane;
	
	
	public LayerManagerPane() {
		this.editedLayerPropertiesPane = null;
		
		Application.controller.getHotkeyListener().subscribe("LayerManagerPane", this);
		Application.controller.subscriptionService.subscribe(Handles.ACTIVE_SCENE, "LayerManagerPane", this);
		Application.controller.subscriptionService.subscribe(Handles.LAYER_REORDER, "LayerManagerPane", this);
	}
	
	
		// Called by one of the child LayerPanes upon being clicked
	public void onLayerPaneClick() {
		update();
	}
	
	@Override
	public void onNotification(String handle, Vendor vendor) {
		if( HotkeyListener.didKeyUpdate(handle) )
		{
			HotkeyListener hl = (HotkeyListener) vendor;
			
			if( HotkeyListener.isSequenceHeld(hl, KeyEvent.VK_CONTROL, KeyEvent.VK_SHIFT, 'N') )
			onAddLayer();
		}
		else
		{
			switch( handle )
			{
				case Handles.ACTIVE_SCENE:
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
	
	
		// Called upon editing a layer (...)
	private void onEditLayer() {
		Layer layer = Application.controller.getActiveLayer();
		
		if( layer == null )
		return;
		
		this.editedLayerPropertiesPane = new LayerPropertiesPane(this, layer, false);
		update();
	}
	
		// Called upon adding a new layer (+)
	private void onAddLayer() {
		Scene scene = Application.controller.getActiveScene();
		TileLayer newLayer = new TileLayer(scene);
		newLayer.setName("Layer " + System.currentTimeMillis());
		
		this.editedLayerPropertiesPane = new LayerPropertiesPane(this, newLayer, true);
		update();
	}
	
		// Called upon deleting a layer (-)
	private void onDeleteLayer() {
		Application.controller.removeActiveLayer();
		update();
	}
	
		// Called upon clicking < in the layer properties view
	private void onBackToLayerManager() {
		this.editedLayerPropertiesPane = null;
		update();
	}
	
		// Moves the selected Layer down a level in rendition order
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
	
		// Moves the selected Layer up a level in rendition order
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
	
	
		// Closes the LayerPropertiesPane
	public void closeProperties() {
		this.editedLayerPropertiesPane = null;
		update();
	}
}
