package editor2d2.gui.body.layermgrpane;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import editor2d2.Application;
import editor2d2.gui.GUIComponent;
import editor2d2.gui.GUIUtilities;
import editor2d2.gui.Handles;
import editor2d2.gui.body.scene.SceneView;
import editor2d2.gui.components.CTextField;
import editor2d2.gui.components.ClickableButton;
import editor2d2.model.project.scene.Layer;
import editor2d2.modules.FactoryService;
import editor2d2.subservice.Subscriber;
import editor2d2.subservice.Vendor;

public class LayerPropertiesPane extends GUIComponent implements Subscriber {
	
		// Reference to the source layer that the pane is representing
	private Layer source;
	
		// Layer name text field
	private CTextField txtName;
	
		// Layer opacity text field
	private CTextField txtOpacity;
	
		// Whether a new Layer is being created
	private boolean isNew;
	
		// Hosting LayerPropertiesPane
	private LayerManagerPane host;
	
	
	public LayerPropertiesPane(LayerManagerPane host, Layer source, boolean isNew) {
		this.source = source;
		this.host = host;
		
		this.txtName = new CTextField("Name: ");
		this.txtName.orientation = BoxLayout.PAGE_AXIS;
		
		this.txtOpacity = new CTextField();
		this.isNew = isNew;
	}
	
	public LayerPropertiesPane() {
		this(null, null, true);
	}
	
	
	@Override
	public void onNotification(String handle, Vendor vendor) { }
	

	@Override
	protected JPanel draw() {
		JPanel container = GUIUtilities.createDefaultPanel();
		
			// Layer name field
		this.txtName.setText(this.source.getName());
		
			// Type area
		JPanel containerType = GUIUtilities.createDefaultPanel();
		
			JLabel labTypeTitle = new JLabel("Type:");
			
			String[] typeChoices = FactoryService.getClassTypes();
			
			for( int i = 0; i < typeChoices.length; i++ )
			typeChoices[i] = FactoryService.getPlaceableClass(typeChoices[i]);
			
			GUIUtilities.convertFirstLetterUppercase(typeChoices);
			
			JComboBox<String> dmType = new JComboBox<String>(typeChoices);
			String selectedLayerType = FactoryService.getPlaceableClass(this.source.getReferencedAsset().getAssetClassName());
			dmType.setSelectedItem(GUIUtilities.getFirstLetterUppercase(selectedLayerType));
			
		containerType.add(labTypeTitle);
		containerType.add(dmType);
		
			// Opacity area
		double opacity = this.source.getOpacity();
		JPanel containerOpacity = GUIUtilities.createDefaultPanel();
		
		final Layer otherSource = this.source;
		final LayerPropertiesPane self = this;
		
				// Opacity slider
			JSlider sldOpacity = new JSlider(JSlider.HORIZONTAL, 0, 100, (int) (100 * opacity));
			sldOpacity.addChangeListener(new ChangeListener() {
				
				@Override
				public void stateChanged(ChangeEvent e) {
					JSlider source = (JSlider) e.getSource();
					
					Vendor v = Application.window.subscriptionService.get(Handles.SCENE_VIEW, "LayerPropertiesPane", self);
					SceneView sv = (SceneView) v;
					sv.update();
					
					double sldValue = source.getValue();
					otherSource.setOpacity(sldValue / 100d);
					
					if( !source.getValueIsAdjusting() )
					{
						txtOpacity.setText("" + Layer.opacityPercentageTo255(sldValue));
						update();
					}
				}
			});
			
				// Opacity field
			this.txtOpacity.setText(""+(Layer.opacityPercentageTo255(opacity)));
			JLabel labOpacityTitle = new JLabel("Opacity:");
			
		containerOpacity.add(labOpacityTitle);
		containerOpacity.add(sldOpacity);
		containerOpacity.add(this.txtOpacity.render());
		
			// Visibility checkbox 
		JPanel containerVisibility = GUIUtilities.createDefaultPanel();
		
			JCheckBox cbIsVisible = new JCheckBox();
			JLabel labVisibilityTitle = new JLabel("Visibility:");
			cbIsVisible.setSelected(true);
			
		containerVisibility.add(labVisibilityTitle);
		containerVisibility.add(cbIsVisible);
		
		container.add(this.txtName.render());
		container.add(containerType);
		container.add(containerOpacity);
		container.add(containerVisibility);
		
			// Controls
		container.add(new ClickableButton("Apply", (e) -> { onApply(); }));
		
		return container;
	}
	
	
		// Called upon clicking "Apply", applies changes to the source layer
	private void onApply() {
		String name = this.txtName.getText();
		double opacity = Layer.opacity255ToPercentage(Double.parseDouble(this.txtOpacity.getText()));
		
		this.source.setName(name);
		this.source.setOpacity(opacity);
		
		if( this.isNew )
		this.source.getScene().addLayer(this.source);
		
		this.host.closeProperties();
	}
}
