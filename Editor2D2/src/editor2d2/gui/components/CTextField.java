package editor2d2.gui.components;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import editor2d2.gui.GUIComponent;

public class CTextField extends GUIComponent {
	
		// JPanel containing the elements of the component
	public JPanel container;
	
		// Orientation of the BoxLayout the container is using
	public int orientation;
	
		// Maximum allowed width of the text field
	public int maxWidth;
	
		// JLabel containing the title of the text field
	public JLabel titleLabel;
	
		// Title of the text field
	public String title;
	
		// Reference to the underlying text field
	public JTextField textField;
	
	
	public CTextField(String title) {
		this.container = null;
		this.title = title;
		this.titleLabel = new JLabel(this.title);
		this.textField = new JTextField();
		this.orientation = BoxLayout.LINE_AXIS;
		this.maxWidth = Integer.MAX_VALUE;
	}
	
	public CTextField() {
		this("");
	}
	
	/********************************************************************************/
	
	@Override
	protected JPanel draw() {
		container = new JPanel();
		container.setLayout(new BoxLayout(container, orientation));
		
		textField.setMaximumSize(new Dimension(maxWidth, textField.getPreferredSize().height));
		
		container.add(titleLabel);
		container.add(textField);
		return container;
	}
	
	/***************************** GETTERS & SETTERS ********************************/
	
		// Returns the text currently typed into the text field
	public String getText() {
		return textField.getText();
	}
	
		// Sets the text of the text field
	public void setText(String t) {
		this.textField.setText(t);
	}
}
