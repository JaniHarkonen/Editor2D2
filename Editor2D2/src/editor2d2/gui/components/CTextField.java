package editor2d2.gui.components;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

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
	
		// DocumentListener that is used to track changes to
		// the text field
	private DocumentListener textFieldChangeListener;
	
		// Reference to the RequirementFilter that can be used
		// to validate the input
	private RequirementFilter<? extends Object> requirements;
	
	public CTextField(String title, RequirementFilter<? extends Object> requirements) {
		this.container = null;
		this.title = title;
		this.titleLabel = new JLabel(this.title);
		this.textField = new JTextField();
		this.orientation = BoxLayout.LINE_AXIS;
		this.maxWidth = Integer.MAX_VALUE;
		this.requirements = requirements;
		this.textFieldChangeListener = null;
	}
	
	public CTextField(String title) {
		this(title, null);
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
		
			// Attatch a ReuqirementFilter if there are requirements
			// to the input of the field
		if( this.requirements != null )
		{
				// Only attatch once
			if( !this.requirements.getAttatched() )
			{
				this.textFieldChangeListener = new DocumentListener() {

					@Override
					public void insertUpdate(DocumentEvent e) {
						validateInput();
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						validateInput();
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						validateInput();
					}
				};
				
				this.textField.getDocument().addDocumentListener(this.textFieldChangeListener);
				this.requirements.setAttatched(true);
			}
			
			validateInput();
		}
		
		return container;
	}
	
	protected void validateInput() {
		this.requirements.updateInput(this.textField.getText());
		
		if( !this.requirements.checkValid() )
		this.textField.setBackground(Color.RED);
		else
		this.textField.setBackground(Color.WHITE);
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
	
		// Returns a reference to the RequirementFilter
	public RequirementFilter<? extends Object> getRequirementFilter() {
		return this.requirements;
	}
	
		// Sets the RequirementFilter that will impose requirements
		// on the input and the output of the text field
	public void setRequirementFilter(RequirementFilter<? extends Object> requirements) {
		this.textField.getDocument().removeDocumentListener(this.textFieldChangeListener);
		requirements.setAttatched(false);
		this.requirements = requirements;
	}
}
