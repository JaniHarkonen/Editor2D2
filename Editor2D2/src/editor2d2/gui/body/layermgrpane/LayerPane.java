package editor2d2.gui.body.layermgrpane;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import editor2d2.Application;
import editor2d2.gui.GUIComponent;
import editor2d2.gui.GUIUtilities;
import editor2d2.model.project.scene.Layer;

public class LayerPane extends GUIComponent {
	
		// Manager pane that the LayerPane belongs to
	private LayerManagerPane manager;
	
		// Reference to the source Layer that the LayerPane
		// represents
	private Layer source;
	
	
	public LayerPane(LayerManagerPane manager, Layer source) {
		this.source = source;
		this.manager = manager;
	}
	
	public LayerPane(LayerManagerPane manager) {
		this(manager, null);
	}
	

	@Override
	protected JPanel draw() {
		JPanel container = GUIUtilities.createDefaultPanel(GUIUtilities.BOX_LINE_AXIS);
		container.add(new JLabel(source.getName()));
		
		Layer selectedLayer = Application.controller.getActiveLayer();
		
		if( selectedLayer != null && selectedLayer == this.source )
		container.setBackground(Color.RED);
		
			// Handle selection
		container.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				onClick();
			}
		});
		
		return container;
	}
	
	
		// Called upon clicking the layer pane
	private void onClick() {
		Application.controller.selectLayer(this.source);
		this.manager.onLayerPaneClick();
	}
}
