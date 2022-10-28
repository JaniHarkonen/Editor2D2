package editor2d2.modules.object.proppane;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import editor2d2.Application;
import editor2d2.gui.GUIUtilities;
import editor2d2.gui.Handles;
import editor2d2.gui.body.proppane.PropertiesPane;
import editor2d2.gui.body.scene.SceneView;
import editor2d2.gui.components.CTextField;
import editor2d2.model.project.Asset;
import editor2d2.model.project.scene.placeable.Placeable;
import editor2d2.modules.object.asset.ObjectProperty;
import editor2d2.modules.object.modal.PropertyField;
import editor2d2.modules.object.placeable.Instance;
import editor2d2.subservice.Subscriber;
import editor2d2.subservice.Vendor;

public class InstancePropertiesPane extends PropertiesPane implements Subscriber {
	
		// Text field for the X-coordinate of the instance
	private CTextField txtX;
	
		// Text field for the Y-coordinate of the instance
	private CTextField txtY;
	
		// Text field for the width of the instance
	private CTextField txtWidth;
	
		// Text field for the height of the instance
	private CTextField txtHeight;
	
		// Text field for the rotation of the instance
	private CTextField txtRotation;
	
		// List of PropertyFields representing the properties
		// of the Instance
	private ArrayList<PropertyField> propertyFields;
	
	
	public InstancePropertiesPane(Placeable source) {
		super(source);
		
		this.txtX = new CTextField("X: ");
		this.txtY = new CTextField("Y: ");
		this.txtWidth = new CTextField("Width: ");
		this.txtHeight = new CTextField("Height: ");
		this.txtRotation = new CTextField("Rotation: ");
		this.propertyFields = new ArrayList<PropertyField>();
	}
	
	
	@Override
	public Asset getReferencedAsset() {
		return this.source.getAsset();
	}
	
	@Override
	public void actionApply(ActionEvent ae) {
		double	/*x = Double.parseDouble(this.txtX.getText()),
				y = Double.parseDouble(this.txtY.getText()),*/
				w = Double.parseDouble(this.txtWidth.getText()),
				h = Double.parseDouble(this.txtHeight.getText()),
				rot = Double.parseDouble(this.txtRotation.getText());
		
		Instance src = (Instance) this.source;
		src.setDimensions(w, h);
		src.setRotation(rot);
		
		for( int i = 0; i < this.propertyFields.size(); i++ )
		{
			ObjectProperty op = src.getPropertyManager().getProperty(i);
			PropertyField pf = this.propertyFields.get(i);
			op.name = pf.getName();
			op.value = pf.getValue();
			op.isCompiled = pf.checkCompiled();
		}
		
		update();
		
		Vendor v = Application.window.subscriptionService.get(Handles.SCENE_VIEW, "InstancePropertiesPane", this);
		
		if( v != null )
		((SceneView) v).update();
	}
	

	@Override
	protected JPanel draw() {
		JPanel container = GUIUtilities.createDefaultPanel();
		Instance src = (Instance) this.source;
		
			// Position controls
		JPanel containerPosition = GUIUtilities.createDefaultPanel(GUIUtilities.BOX_LINE_AXIS);
		
			containerPosition.add(this.txtX.render());
			containerPosition.add(this.txtY.render());
		
		containerPosition.setBorder(BorderFactory.createTitledBorder("Position"));
		container.add(containerPosition);
		
			// Dimension controls
		JPanel containerDimensions = GUIUtilities.createDefaultPanel(GUIUtilities.BOX_LINE_AXIS);
		
			this.txtWidth.setText(""+src.getWidth());
			containerDimensions.add(this.txtWidth.render());
			
			this.txtHeight.setText(""+src.getHeight());
			containerDimensions.add(this.txtHeight.render());
			
		containerDimensions.setBorder(BorderFactory.createTitledBorder("Dimensions"));
		container.add(containerDimensions);
		
			// Orientation controls
		JPanel containerOrientation = GUIUtilities.createDefaultPanel(GUIUtilities.BOX_LINE_AXIS);
		
			this.txtRotation.setText(""+src.getRotation());
			containerOrientation.add(this.txtRotation.render());
			
		containerOrientation.setBorder(BorderFactory.createTitledBorder("Orientation"));
		container.add(containerOrientation);
		
			// Properties
		ArrayList<ObjectProperty> props = src.getPropertyManager().getAllProperties(); 
		if( props.size() > 0 )
		{
			JPanel propertiesContainer = GUIUtilities.createDefaultPanel();
			this.propertyFields = new ArrayList<PropertyField>();
			
				for( ObjectProperty op : props )
				{
					PropertyField pf = new PropertyField(op, false);
					this.propertyFields.add(pf);
					propertiesContainer.add(pf.render());
				}
			
			propertiesContainer.setBorder(BorderFactory.createTitledBorder("Properties"));
			container.add(propertiesContainer);
		}
		
		return this.createDefaultPropertiesPaneContainer("Instance properties", container);
	}


	@Override
	public void onNotification(String handle, Vendor vendor) { }
}
