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

/**
 * This class represents a clickable Layer pane. When clicked 
 * the Layer represented by this class will be selected via 
 * the application Controller. The LayerPane also contains a 
 * visibility toggle that can be used to display/hide Layers 
 * of the Scene displayed in the SceneView.
 * <br/><br/>
 * 
 * The Vendor-interface is implemented to notify other 
 * GUI-components of changes in the visibility of the 
 * underlying Layer.
 * 
 * @author User
 *
 */
public class LayerPane extends GUIComponent implements Vendor {
	
	/**
	 * The hosting LayerManagerPane that this Layer pane is 
	 * rendered in.
	 */
	private LayerManagerPane manager;
	
	/**
	 * The source Layer that is being represented by this 
	 * LayerPane.
	 */
	private Layer source;
	
	/**
	 * BufferedImage that represents the visibility toggle 
	 * button in the LayerPane. Setting: ON.
	 */
	private BufferedImage imageToggleLayerVisibilityOn;

	/**
	 * BufferedImage that represents the visibility toggle 
	 * button in the LayerPane. Setting: OFF.
	 */
	private BufferedImage imageToggleLayerVisibilityOff;
	
	/**
	 * Constructs a LayerPane instance with a reference 
	 * to a given host LayerManagerPane and a given Layer as 
	 * the source.
	 * <br/><br/>
	 * 
	 * <b>Notice: </b>each LayerPane MUST have a hosting 
	 * LayerManagerPane where it is listed.
	 * 
	 * @param manager Reference to the host LayerManagerPane 
	 * that renders this LayerPane.
	 * @param source Reference to the source Layer that is 
	 * being represented by this LayerPane.
	 */
	public LayerPane(LayerManagerPane manager, Layer source) {
		this.source = source;
		this.manager = manager;
		this.imageToggleLayerVisibilityOn = Application.resources.getGraphic("icon-toggle-layer-visibility-on");
		this.imageToggleLayerVisibilityOff = Application.resources.getGraphic("icon-toggle-layer-visibility-off");
	}
	
	/**
	 * Constructs a LayerPane instance with the default 
	 * settings (NULL represented Layer) and a given host 
	 * LayerManagerPane that the LayerPane is rendered 
	 * inside of.
	 * @param manager Reference to the host LayerManagerPane 
	 * that renders this LayerPane.
	 */
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
	
	/**
	 * Called upon clicking the Layer. Simply selects the 
	 * underlying Layer represented by this LayerPane via 
	 * the applicatino Controller. The host 
	 * LayerManagerPane will also be notified and 
	 * re-rendered.
	 */
	private void onClick() {
		Application.controller.selectLayer(this.source);
		this.manager.onLayerPaneClick();
	}
	
	/**
	 * Called upon toggling the Layer visibility. Simply 
	 * toggles the visibility and registers the change via 
	 * the application Controller's SubscriptionService so 
	 * that the SceneView will be updated.
	 */
	private void onToggleLayerVisibility() {
		this.source.toggleVisibility();
		Application.controller.subscriptionService.register(Handles.LAYER_VISIBILITY, this);
		update();
	}
}
