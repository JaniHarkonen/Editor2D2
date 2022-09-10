package editor2d2.gui.modal.views;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import editor2d2.gui.GUIUtilities;
import editor2d2.gui.components.CTextField;
import editor2d2.model.project.assets.EObject;
import editor2d2.model.project.assets.ObjectProperty;

public class ObjectModal extends ModalView<EObject> {

	@Override
	protected JPanel draw() {
		JPanel modal = GUIUtilities.createDefaultPanel();
		
			// Sprite field
		CTextField txtSprite = new CTextField("Sprite:");
		
			// Dimensions
		JPanel containerDimensions = GUIUtilities.createTitledPanel("Dimensions", GUIUtilities.BOX_LINE_AXIS);
		
				// Default width field
			CTextField txtWidth = new CTextField("Width:");
			
				// Default height field
			CTextField txtHeight = new CTextField("Height:");
			
		containerDimensions.add(txtWidth.render());
		containerDimensions.add(txtHeight.render());
		
			// Rotation field
		CTextField txtRotation = new CTextField("Rotation°:");
		
			// Properties
		JPanel containerProperties = GUIUtilities.createTitledPanel("Properties", GUIUtilities.BOX_PAGE_AXIS);
		JPanel containerPropertiesTitles = GUIUtilities.createDefaultPanel(GUIUtilities.BOX_LINE_AXIS);
		containerPropertiesTitles.add(new JLabel("Name:"));
		containerPropertiesTitles.add(new JLabel("Value:"));
		containerPropertiesTitles.add(new JLabel("Compile:"));
		
		containerProperties.add(containerPropertiesTitles);
		containerProperties.add(createPropertyField(new ObjectProperty("!x", "&x", true)));
		containerProperties.add(new JButton("+"));
		containerProperties.add(new JButton("-"));
		
		modal.add(txtSprite.render());
		modal.add(containerDimensions);
		modal.add(txtRotation.render());
		modal.add(containerProperties);
		
		return this.createDefaultModalView(modal);
	}
	

		// Creates a JPanel containing the settings for a property
	private JPanel createPropertyField(ObjectProperty objectProperty) {
		JPanel container = GUIUtilities.createDefaultPanel(GUIUtilities.BOX_LINE_AXIS);
		
			// Selection checkbox
		JCheckBox cbIsSelected = new JCheckBox();
		
			// Property name field
		CTextField txtPropName = new CTextField("");
		txtPropName.setText(objectProperty.name);
		
			// Value field
		CTextField txtPropValue = new CTextField("");
		txtPropValue.setText(objectProperty.value);
		
			// Compilation checkbox
		JCheckBox cbIsCompiled = new JCheckBox();
		cbIsCompiled.setSelected(objectProperty.isCompiled);
		
		container.add(cbIsSelected);
		container.add(txtPropName.render());
		container.add(txtPropValue.render());
		container.add(cbIsCompiled);
		
		return container;
	}
}
