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

public class LayerPropertiesPane extends GUIComponent {

	@Override
	protected JPanel draw() {
		JPanel container = GUIUtilities.createDefaultPanel();
		
			// Layer name field
		CTextField txtName = new CTextField("Name:");
		txtName.orientation = BoxLayout.PAGE_AXIS;
		
			// Type area
		JPanel containerType = GUIUtilities.createDefaultPanel();
		
			JLabel labTypeTitle = new JLabel("Type:");
		
			String[] typeChoices = { "Tile", "Object", "Data" };
			JComboBox dmType = new JComboBox(typeChoices);
			
		containerType.add(labTypeTitle);
		containerType.add(dmType);
		
			// Opacity area
		JPanel containerOpacity = GUIUtilities.createDefaultPanel();
		
				// Opacity slider
			JSlider sldOpacity = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
			
				// Opacity field
			CTextField txtOpacity = new CTextField();
			JLabel labOpacityTitle = new JLabel("Opacity:");
			
		containerOpacity.add(labOpacityTitle);
		containerOpacity.add(sldOpacity);
		containerOpacity.add(txtOpacity.render());
		
			// Visibility checkbox 
		JPanel containerVisibility = GUIUtilities.createDefaultPanel();
		
			JCheckBox cbIsVisible = new JCheckBox();
			JLabel labVisibilityTitle = new JLabel("Visibility:");
			cbIsVisible.setSelected(true);
			
		containerVisibility.add(labVisibilityTitle);
		containerVisibility.add(cbIsVisible);
		
		container.add(txtName.render());
		container.add(containerType);
		container.add(containerOpacity);
		container.add(containerVisibility);
		
		return container;
	}
}
