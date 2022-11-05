package editor2d2.gui.body.layermgrpane;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;
import javax.swing.JPanel;

import editor2d2.Application;
import editor2d2.gui.GUIComponent;
import editor2d2.gui.GUIUtilities;
import editor2d2.gui.components.CIcon;
import editor2d2.model.Handles;
import editor2d2.model.project.scene.Layer;
import editor2d2.subservice.Vendor;

public class LayerPane extends GUIComponent implements Vendor {
	
		// Manager pane that the LayerPane belongs to
	private LayerManagerPane manager;
	
		// Reference to the source Layer that the LayerPane
		// represents
	private Layer source;
	
		// Reference to the Layer visibility icon (visibility ON)
	private BufferedImage imageToggleLayerVisibilityOn;
	
		// Reference to the Layer visibility icon (visibility OFF)
	private BufferedImage imageToggleLayerVisibilityOff;
	
	
	public LayerPane(LayerManagerPane manager, Layer source) {
		this.source = source;
		this.manager = manager;
		this.imageToggleLayerVisibilityOn = Application.resources.getGraphic("icon-toggle-layer-visibility-on");
		this.imageToggleLayerVisibilityOff = Application.resources.getGraphic("icon-toggle-layer-visibility-off");
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
		
		BufferedImage img = this.imageToggleLayerVisibilityOff;
		
		if( this.source.checkVisible() )
		img = this.imageToggleLayerVisibilityOn;
		
		CIcon iconToggleLayerVisibility = new CIcon(img, (e) -> { onToggleLayerVisibility(); });
		
		iconToggleLayerVisibility.width = 16;
		iconToggleLayerVisibility.height = 16;
		container.add(iconToggleLayerVisibility.render());
		
		return container;
	}
	
	
		// Called upon clicking the layer pane
	private void onClick() {
		Application.controller.selectLayer(this.source);
		this.manager.onLayerPaneClick();
	}
	
		// Toggles the visibility of the Layer
	private void onToggleLayerVisibility() {
		this.source.toggleVisibility();
		Application.controller.subscriptionService.register(Handles.LAYER_VISIBILITY, this);
		update();
	}
}
