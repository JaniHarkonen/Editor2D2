package editor2d2.gui.body.layermgrpane;


import javax.swing.JPanel;

import editor2d2.Application;
import editor2d2.gui.GUIComponent;
import editor2d2.gui.GUIUtilities;
import editor2d2.gui.components.ClickableButton;
import editor2d2.model.project.scene.Layer;
import editor2d2.model.project.scene.Scene;
import editor2d2.modules.object.layer.ObjectLayer;

public class LayerManagerPane extends GUIComponent {
	
		// Whether a layer is being edited
	private Layer editedLayer;
	
	
	public LayerManagerPane() {
		this.editedLayer = null;
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
			for( Layer layer : Application.controller.getProject().getScene("small scene").getLayers() )
			container.add((new LayerPane(layer)).render());
			
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
		this.editedLayer = Application.controller.getLayer();
		update();
	}
	
		// Called upon adding a new layer (+)
	private void onAddLayer() {
		Scene scene = Application.controller.getProject().getScene("small scene");
		ObjectLayer newLayer = new ObjectLayer(scene);
		newLayer.setName("Object layer " + System.currentTimeMillis());
		scene.addLayer(newLayer);
		
		update();
	}
	
		// Called upon deleting a layer (-)
	private void onDeleteLayer() {
		Layer target = Application.controller.getLayer();
		
		if( target == null )
		return;
		
		Application.controller.getProject().getScene("small scene").removeLayer(target);
		
		update();
	}
	
		// Called upon clicking < in the layer properties view
	private void onBackToLayerManager() {
		this.editedLayer = null;
		update();
	}
}
