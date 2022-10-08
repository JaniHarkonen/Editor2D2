package editor2d2.gui.body.layermgrpane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import editor2d2.Application;
import editor2d2.gui.GUIComponent;
import editor2d2.gui.GUIUtilities;
import editor2d2.model.project.Scene;
import editor2d2.model.project.layers.Layer;
import editor2d2.model.project.layers.ObjectLayer;

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
		
				// Control - create new layer button
			JButton btnAddLayer = new JButton("+");
			btnAddLayer.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					onAddLayer();
				}
			});
			containerControls.add(btnAddLayer);
			
				// Control - delete layer button
			JButton btnDeleteLayer = new JButton("-");
			btnDeleteLayer.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					onDeleteLayer();
				}
			});
			containerControls.add(btnDeleteLayer);
		
				// Control - edit layer button
			JButton btnEditLayer = new JButton("...");
			btnEditLayer.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					onEditLayer();
				}
			});
			containerControls.add(btnEditLayer);
			
		container.add(containerControls);
		
			// Layer panes
		for( Layer layer : Application.controller.getProject().getScene("small scene").getLayers() )
		container.add((new LayerPane(layer)).render());
		
		}
		else
		{
				// Controls
			JPanel containerControls = GUIUtilities.createDefaultPanel(GUIUtilities.BOX_LINE_AXIS);
			
					// Control - back button
				JButton btnBackToLayerManager = new JButton("<");
				btnBackToLayerManager.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						onBackToLayerManager();
					}
				});
				containerControls.add(btnBackToLayerManager);
				
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
