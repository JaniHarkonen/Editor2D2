package editor2d2.gui.body.layermgrpane;


import javax.swing.JPanel;

import editor2d2.Application;
import editor2d2.DebugUtils;
import editor2d2.gui.GUIComponent;
import editor2d2.gui.GUIUtilities;
import editor2d2.gui.components.ClickableButton;
import editor2d2.model.project.scene.Layer;
import editor2d2.model.project.scene.Scene;
import editor2d2.modules.object.layer.InstanceLayer;

public class LayerManagerPane extends GUIComponent {
	
		// Whether a layer is being edited
	private Layer editedLayer;
	
	
	public LayerManagerPane() {
		this.editedLayer = null;
	}
	
	
		// Called by one of the child LayerPanes upon being clicked
	public void onLayerPaneClick() {
		update();
	}
	

	@Override
	protected JPanel draw() {
		JPanel container = GUIUtilities.createTitledPanel("Layers", GUIUtilities.BOX_PAGE_AXIS);
		
		if( this.editedLayer == null )
		{
		
				// Controls area
			JPanel containerControls = GUIUtilities.createDefaultPanel(GUIUtilities.BOX_LINE_AXIS);
				containerControls.add(new ClickableButton("+",   (e) -> { onAddLayer(); }));
				containerControls.add(new ClickableButton("-",   (e) -> { onDeleteLayer(); }));
				containerControls.add(new ClickableButton("...", (e) -> { onEditLayer(); }));
			container.add(containerControls);
			
				// Layer panes
			//DebugUtils.log(Application.controller.getActiveProject().getAllScenes().size(), this);
			for( Layer layer : Application.controller.getActiveProject().getScene(0).getLayers() )
			container.add((new LayerPane(this, layer)).render());
			
		}
		else
		{
				// Controls
			JPanel containerControls = GUIUtilities.createDefaultPanel(GUIUtilities.BOX_LINE_AXIS);
				containerControls.add(new ClickableButton("<", (e) -> { onBackToLayerManager(); }));
			container.add(containerControls);
			
				// Layer properties pane
			container.add((new LayerPropertiesPane(this.editedLayer)).render());
		}
		
		
		return container;
	}
	
	
		// Called upon editing a layer (...)
	private void onEditLayer() {
		this.editedLayer = Application.controller.getActiveLayer();
		update();
	}
	
		// Called upon adding a new layer (+)
	private void onAddLayer() {
		Scene scene = Application.controller.getActiveProject().getScene("small scene");
		InstanceLayer newLayer = new InstanceLayer(scene);
		newLayer.setName("Object layer " + System.currentTimeMillis());
		scene.addLayer(newLayer);
		
		update();
	}
	
		// Called upon deleting a layer (-)
	private void onDeleteLayer() {
		Layer target = Application.controller.getActiveLayer();
		
		if( target == null )
		return;
		
		Application.controller.getActiveProject().getScene("small scene").removeLayer(target);
		
		update();
	}
	
		// Called upon clicking < in the layer properties view
	private void onBackToLayerManager() {
		this.editedLayer = null;
		update();
	}
}
