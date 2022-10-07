package editor2d2.gui.body.layermgrpane;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import editor2d2.Application;
import editor2d2.gui.GUIComponent;
import editor2d2.gui.GUIUtilities;
import editor2d2.model.project.layers.Layer;
import editor2d2.model.project.scene.placeables.Placeable;

public class LayerPane extends GUIComponent {
	
	private Layer<? extends Placeable> source;
	
	
	public LayerPane(Layer<? extends Placeable> source) {
		this.source = source;
	}
	
	public LayerPane() {
		this(null);
	}
	

	@Override
	protected JPanel draw() {
		JPanel container = GUIUtilities.createDefaultPanel(GUIUtilities.BOX_LINE_AXIS);
		container.add(new JLabel(source.getName()));
		
		Layer<? extends Placeable> selectedLayer = Application.controller.getLayer();
		
		if( selectedLayer != null && Application.controller.getLayer() == this.source )
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
		Application.controller.setLayer(this.source);
		update();
	}
}
