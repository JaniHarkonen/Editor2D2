package editor2d2.gui.modal.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import editor2d2.gui.GUIUtilities;
import editor2d2.gui.components.CTextField;
import editor2d2.gui.modal.ModalWindow;
import editor2d2.model.project.assets.EObject;
import editor2d2.model.project.assets.ObjectProperty;

public class ObjectModal extends ModalView<EObject> {
	
		// Default width text field
	private CTextField txtWidth;
	
		// Default height text field
	private CTextField txtHeight;
	
		// Default rotation text field
	private CTextField txtRotation;
	
	
	public ObjectModal(ModalWindow host, boolean useFactorySettings) {
		super(host, useFactorySettings);
		
		this.txtWidth = new CTextField("Width:");
		this.txtHeight = new CTextField("Height:");
		this.txtRotation = new CTextField("Rotation°:");
	}
	
	public ObjectModal(ModalWindow host) {
		super(host);
	}
	

	@Override
	protected JPanel draw() {
		JPanel modal = GUIUtilities.createDefaultPanel();
		
			// Sprite field
		CTextField txtSprite = new CTextField("Sprite:");
		
			// Dimensions
		JPanel containerDimensions = GUIUtilities.createTitledPanel("Dimensions", GUIUtilities.BOX_LINE_AXIS);
		
				// Default width field
			this.txtWidth.setText(""+source.getWidth());
			
				// Default height field
			this.txtHeight.setText(""+this.source.getHeight());
			
		containerDimensions.add(this.txtWidth.render());
		containerDimensions.add(this.txtHeight.render());
		
			// Rotation field
		this.txtRotation.setText(""+this.source.getRotation());
		
			// Properties
		JPanel containerProperties = GUIUtilities.createTitledPanel("Properties", GUIUtilities.BOX_PAGE_AXIS);
		JPanel containerPropertiesTitles = GUIUtilities.createDefaultPanel(GUIUtilities.BOX_LINE_AXIS);
		containerPropertiesTitles.add(new JLabel("Name:"));
		containerPropertiesTitles.add(new JLabel("Value:"));
		containerPropertiesTitles.add(new JLabel("Compile:"));
		
		containerProperties.add(containerPropertiesTitles);
		
		ArrayList<ObjectProperty> objProps = this.source.getProperties();
		
		for( ObjectProperty op : objProps )
		containerProperties.add(createPropertyField(op));
		
				// Add property button
			JButton btnAddProp = new JButton("+");
			btnAddProp.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					actionAddProperty();
				}
			});
			
				// Remove selected properties button
			JButton btnRemoveProp = new JButton("-");
			btnRemoveProp.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					actionRemoveProperties();
				}
			});
		
		containerProperties.add(btnAddProp);
		containerProperties.add(new JButton("-"));
		
		modal.add(txtSprite.render());
		modal.add(containerDimensions);
		modal.add(this.txtRotation.render());
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


	@Override
	public void setFactorySettings() {
		EObject source = new EObject();
		source.setIdentifier("c");
		source.setName("Object " + System.currentTimeMillis());
		source.setWidth(0);
		source.setHeight(0);
		source.setRotation(0);
		
		this.source = source;
	}
	
	@Override
	public void saveChanges(boolean doChecks) {
		super.saveChanges(doChecks);
		
		String w = this.txtWidth.getText();
		String h = this.txtHeight.getText();
		String rot = this.txtRotation.getText();
		
		if( doChecks && (w.equals("") || h.equals("") || rot.equals("")) )
		return;
		
		this.source.setWidth(Double.parseDouble(w));
		this.source.setHeight(Double.parseDouble(h));
		this.source.setRotation(Double.parseDouble(rot));
	}

	@Override
	protected void actionCreate() {
		saveChanges(true);
		finalizeCreation();
		
		this.host.closeModalWindow();
	}
	
		// Adds a new property panel to a given container upon
		// clicking "+"
	private void actionAddProperty( ) {
		this.source.addProperty(new ObjectProperty("!x", "&x", true));
		saveChanges(false);
		update();
	}
	
		// Removes the selected properties from the object upon
		// clicking "-"
	private void actionRemoveProperties() {
		
		saveChanges(false);
		update();
	}
}
