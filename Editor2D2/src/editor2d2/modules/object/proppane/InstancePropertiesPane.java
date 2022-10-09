package editor2d2.modules.object.proppane;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import editor2d2.gui.GUIUtilities;
import editor2d2.gui.body.PropertiesPane;
import editor2d2.gui.components.CTextField;
import editor2d2.model.project.scene.placeable.Placeable;

public class InstancePropertiesPane extends PropertiesPane {
	
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
	
	
	public InstancePropertiesPane(Placeable source) {
		super(source);
		
		this.txtX = new CTextField("X: ");
		this.txtY = new CTextField("Y: ");
		this.txtWidth = new CTextField("Width: ");
		this.txtHeight = new CTextField("Height: ");
		this.txtRotation = new CTextField("Rotation: ");
	}
	

	@Override
	protected JPanel draw() {
		JPanel container = GUIUtilities.createDefaultPanel();
		
			// Position controls
		JPanel containerPosition = GUIUtilities.createDefaultPanel(GUIUtilities.BOX_LINE_AXIS);
		
			containerPosition.add(this.txtX.render());
			containerPosition.add(this.txtY.render());
		
		containerPosition.setBorder(BorderFactory.createTitledBorder("Position"));
		container.add(containerPosition);
		
			// Dimension controls
		JPanel containerDimensions = GUIUtilities.createDefaultPanel(GUIUtilities.BOX_LINE_AXIS);
		
			containerDimensions.add(this.txtWidth.render());
			containerDimensions.add(this.txtHeight.render());
			
		containerDimensions.setBorder(BorderFactory.createTitledBorder("Dimensions"));
		container.add(containerDimensions);
		
			// Orientation controls
		JPanel containerOrientation = GUIUtilities.createDefaultPanel(GUIUtilities.BOX_LINE_AXIS);
		
			containerOrientation.add(this.txtRotation.render());
			
		containerOrientation.setBorder(BorderFactory.createTitledBorder("Orientation"));
		container.add(containerOrientation);
		
		return this.createDefaultPropertiesPaneContainer("Instance properties", container);
	}
}
