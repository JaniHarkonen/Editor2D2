package editor2d2.gui.body.layermgrpane;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import editor2d2.gui.GUIComponent;
import editor2d2.gui.GUIUtilities;
import editor2d2.gui.components.CTextField;
import editor2d2.gui.components.ClickableButton;
import editor2d2.model.project.layers.Layer;

public class LayerPropertiesPane extends GUIComponent {
	
		// Reference to the source layer that the pane is representing
	private Layer source;
	
		// Layer name text field
	private CTextField txtName;
	
		// Layer opacity text field
	private CTextField txtOpacity;
	
	
	public LayerPropertiesPane(Layer source) {
		this.source = source;
		
		this.txtName = new CTextField("Name: ");
		this.txtName.orientation = BoxLayout.PAGE_AXIS;
		
		this.txtOpacity = new CTextField();
	}
	
	public LayerPropertiesPane() {
		this(null);
	}
	

	@Override
	protected JPanel draw() {
		JPanel container = GUIUtilities.createDefaultPanel();
		
			// Layer name field
		this.txtName.setText(this.source.getName());
		
			// Type area
		JPanel containerType = GUIUtilities.createDefaultPanel();
		
			JLabel labTypeTitle = new JLabel("Type:");
		
			String[] typeChoices = { "Tile", "Object", "Data" };
			JComboBox dmType = new JComboBox(typeChoices);
			
		containerType.add(labTypeTitle);
		containerType.add(dmType);
		
			// Opacity area
		double opacity = this.source.getOpacity();
		JPanel containerOpacity = GUIUtilities.createDefaultPanel();
		
				// Opacity slider
			JSlider sldOpacity = new JSlider(JSlider.HORIZONTAL, 0, 100, (int) (100 * opacity));
			
				// Opacity field
			this.txtOpacity.setText(""+(opacity * 255));
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
		double opacity = (Double.parseDouble(this.txtOpacity.getText()) / 255);
		
		this.source.setName(name);
		this.source.setOpacity(opacity);
	}
}
