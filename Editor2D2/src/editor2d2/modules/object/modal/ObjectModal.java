package editor2d2.modules.object.modal;

import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import editor2d2.gui.GUIUtilities;
import editor2d2.gui.components.CTextField;
import editor2d2.gui.components.ClickableButton;
import editor2d2.gui.modal.ModalView;
import editor2d2.gui.modal.ModalWindow;
import editor2d2.model.project.Asset;
import editor2d2.modules.object.asset.EObject;
import editor2d2.modules.object.asset.ObjectProperty;

public class ObjectModal extends ModalView<EObject> {

	
		// Default width text field
	private CTextField txtWidth;
	
		// Default height text field
	private CTextField txtHeight;
	
		// Default rotation text field
	private CTextField txtRotation;
	
		// Reference to the checkboxes next to properties
		// fields
	private ArrayList<PropertyField> propertyFields;
	
	
	public ObjectModal(ModalWindow host, boolean useFactorySettings) {
		super(host, useFactorySettings);
		
		this.txtWidth = new CTextField("Width:");
		this.txtHeight = new CTextField("Height:");
		this.txtRotation = new CTextField("Rotation°:");
		this.propertyFields = new ArrayList<PropertyField>();
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
		
			// Create property fields
		this.propertyFields = new ArrayList<PropertyField>();
		for( ObjectProperty op : this.source.getPropertyManager().getAllProperties() )
		{
			PropertyField pf = new PropertyField(op);
			containerProperties.add(pf.render());
			this.propertyFields.add(pf);
		}
		
			// Property controls
		containerProperties.add(new ClickableButton("+", (e) -> { actionAddProperty(); }));
		containerProperties.add(new ClickableButton("-", (e) -> { actionRemoveProperties(); }));
		
		modal.add(txtSprite.render());
		modal.add(containerDimensions);
		modal.add(this.txtRotation.render());
		modal.add(containerProperties);
		
		return this.createDefaultModalView(modal);
	}


	@Override
	public void setFactorySettings() {
		EObject source = new EObject();
		long currms = System.currentTimeMillis();
		
		source.setIdentifier("OBJ" + currms);
		source.setName("Object " + currms);
		source.setWidth(32);
		source.setHeight(32);
		
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
		
			// Save changes to property fields
		this.source.getPropertyManager().removeAllProperties();
		
		for( PropertyField pf : this.propertyFields )
		this.source.getPropertyManager().addProperty(new ObjectProperty(pf.getName(), pf.getValue(), pf.checkCompiled()));
	}
	
		// Adds a new property panel to a given container upon
		// clicking "+"
	private void actionAddProperty( ) {
		saveChanges(false);
		this.source.getPropertyManager().addProperty(new ObjectProperty("", "", true));
		update();
	}
	
		// Removes the selected properties from the object upon
		// clicking "-"
	private void actionRemoveProperties() {
		saveChanges(false);
		
		for( int i = 0; i < this.propertyFields.size(); i++ )
		{
			if( !this.propertyFields.get(i).getSelected() )
			continue;
			
			this.source.getPropertyManager().removeProperty(i);
			this.propertyFields.remove(i);
			i--;
		}
		
		update();
	}

	
	@Override
	public Asset getReferencedAsset() {
		return this.source;
	}
}
