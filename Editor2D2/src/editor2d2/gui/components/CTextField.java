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

/**
 * This is a general GUI-component that renders an input 
 * field using Swing-components. This component can be 
 * coupled with a RequirementFilter that will be used to 
 * validate the input within. When the input is invalid 
 * the field will be colored red, otherwise it is colored 
 * white.
 * 
 * The text field can also be coupled with a caption that 
 * will be either displayed above the field or next to the 
 * field depending on the orientation of the BoxLayout.
 * 
 * See RequirementFilter for more information on input 
 * requirements.
 * 
 * @author User
 *
 */
public class CTextField extends GUIComponent {
	
	/**
	 * JPanel containing the GUI-elements of the component.
	 */
	public JPanel container;
	
	/**
	 * Orientation of the BoxLayout the container JPanel 
	 * is using.
	 */
	public int orientation;
	
	/**
	 * Maximum allowed width of the text field (in pixels).
	 */
	public int maxWidth;
	
	/**
	 * JLabel containing the caption of the text field.
	 */
	public JLabel titleLabel;
	
	/**
	 * Caption of the text field. Can also be NULL, in 
	 * which case the field will have no caption.
	 */
	public String title;
	
	/**
	 * The JTextField that represents the text field in 
	 * the container JPanel.
	 */
	public JTextField textField;
	
	/**
	 * DocumentListener that is used to track the changes 
	 * in the input of the text field. It will validate 
	 * the input each time the input changes.
	 */
	private DocumentListener textFieldChangeListener;

	/**
	 * RequirementFilter used to validate and check the 
	 * input of the text field.
	 * 
	 * See RequirementFilter for more information on 
	 * input validation and checking.
	 */
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
	
	/**
	 * Constructs a CTextField instance with a given title 
	 * as its caption.
	 * 
	 * @param title Caption that the text field should 
	 * have.
	 */
	public CTextField(String title) {
		this(title, null);
	}
	
	/**
	 * Constructs a CTextField instance with default 
	 * settings and no title.
	 */
	public CTextField() {
		this("");
	}
	
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
	
	/**
	 * Called by the DocumentListener each time the text 
	 * field input changes to validate the input of the 
	 * field. This method is only called if the text 
	 * field is coupled with a RequirementFilter.
	 */
	protected void validateInput() {
		this.requirements.updateInput(this.textField.getText());
		
		if( !this.requirements.checkValid() )
		this.textField.setBackground(Color.RED);
		else
		this.textField.setBackground(Color.WHITE);
	}
	
	// GETTERS AND SETTERS
	
	/**
	 * Returns the current text input in the text field.
	 * 
	 * @return Returns the text field input.
	 */
	public String getText() {
		return textField.getText();
	}

	/**
	 * Sets the input of the text field. The input will 
	 * immediately be validated by the RequirementFilter 
	 * if there is one.
	 * 
	 * @param t New input of the text field.
	 */
	public void setText(String t) {
		this.textField.setText(t);
		
		if( this.requirements != null )
		validateInput();
	}

	/**
	 * Returns a reference to the RequirementFilter 
	 * coupled to the text field that validates the 
	 * input.
	 * 
	 * @return Returns a reference to the 
	 * RequirementFilter of the text field.
	 */
	public RequirementFilter<? extends Object> getRequirementFilter() {
		return this.requirements;
	}

	/**
	 * Sets the RequirementFilter object used by the 
	 * text field to validate its input.
	 * 
	 * @param requirements Reference to the 
	 * RequirementFilter that will be used to 
	 * validate input.
	 */
	public void setRequirementFilter(RequirementFilter<? extends Object> requirements) {
		this.textField.getDocument().removeDocumentListener(this.textFieldChangeListener);
		requirements.setAttatched(false);
		this.requirements = requirements;
	}
}
