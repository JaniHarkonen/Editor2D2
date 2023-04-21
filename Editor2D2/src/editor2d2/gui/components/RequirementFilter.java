package editor2d2.gui.components;

/**
 * Sometimes it is useful to validate the input of a 
 * GUI-component, such as a text field. CTextFields 
 * come with a built-in functionality to have their 
 * input tested and returned via a RequirementFilter.
 * 
 * RequirementFilter takes in string-type input and 
 * attempts to validate it using the validateInput-
 * method. If the input is valid, the isValid-flag 
 * will be set to true. When the input is valid, it 
 * can be returned using the getValue-method. In 
 * case of invalid input, the method will return a 
 * preset default value.
 * 
 * Each time a GUI-component that the 
 * RequirementFilter is attached changes its value, 
 * the GUI-component will update the 
 * RequirementFilter using the updateInput-method.
 * 
 * Each RequirementFilter must override and 
 * implement its own validateInput-method. This makes 
 * the RequirementFilter very versatile allowing 
 * any type of filters to be created. 
 * RequirementFilters should either be stored in the 
 * "gui.components.requirements"-package, if they are 
 * general in nature, or within the same package as 
 * the GUI-component utilizing them if they are 
 * specific to that component. 
 * 
 * @author User
 *
 * @param <T> Determines the type of the input that 
 * will be returned when calling the getValue-method,
 * the type of input produced by the input field.
 * 
 * See getValue-method for more information on 
 * getting validated input through the 
 * RequirementFilter.
 */
public abstract class RequirementFilter<T> {

	/**
	 * Whether the latest input that the 
	 * RequirementFilter was updated with passed the 
	 * validity check.
	 */
	protected boolean isValid;
	
	/**
	 * Latest input the RequirementFilter was updated 
	 * with.
	 */
	protected String input;
	
	/**
	 * Default value that will be returned if the 
	 * input is invalid.
	 */
	protected T defaultValue;
	
	/**
	 * Returns the last valid value that the 
	 * RequirementFilter was updated with.
	 */
	protected T value;
	
	/**
	 * Returns whether this instance of 
	 * RequirementFilter has been attached to a 
	 * GUI-component processing input.
	 */
	protected boolean isAttatched;
	
	/**
	 * Constructs an instance of RequirementFilter with 
	 * default settings.
	 */
	public RequirementFilter() {
		this.isValid = false;
		this.input = null;
		this.defaultValue = null;
		this.value = null;
		this.isAttatched = false;
	}
	
	/**
	 * Each RequirementFilter subclass should implement 
	 * its own validateInput-method. This method is 
	 * responsible for validating the current input 
	 * which will be stored in the value-field if the 
	 * input is valid. If the input is invalid, value 
	 * wont be updated. This method returns whether 
	 * the latest input the RequirementFilter was 
	 * updated with was valid.
	 * 
	 * When validating the input, it has to be 
	 * converted to the appropriate type defined by 
	 * the template parameter T. See JavaDoc of this 
	 * class (RequirementFilter) for more information.
	 * 
	 * @return Returns whether the last input the 
	 * RequirementFilter was updated with was valid.
	 */
	protected abstract boolean validateInput();
	
	/**
	 * Updates the RequirementFilter input with a 
	 * given value passed in String-form. The input 
	 * will be stored in the input-field and 
	 * validated.
	 * 
	 * @param updatedInput A String representing the 
	 * updated input.
	 */
	public void updateInput(String updatedInput) {
		this.input = updatedInput;
		this.isValid = validateInput();
	}
	
	/**
	 * Returns whether the previous input the 
	 * RequirementFilter was updated with passed 
	 * the validity check.
	 * 
	 * @return Returns whether the last input was 
	 * valid.
	 */
	public boolean checkValid() {
		return this.isValid;
	}
	
	/**
	 * Validates the latest input passed into the 
	 * RequirementFilter and returns it converted 
	 * to the appropriate type if the input was 
	 * valid. If the input was invalid a preset
	 * default value will be returned.
	 * 
	 * @return Returns the last valid value or the 
	 * preset default value.
	 */
	public T getValue() {
		if( checkValid() )
		return this.value;
		
		return this.defaultValue;
	}
	
	// GETTERS AND SETTERS
	
	/**
	 * Sets whether this RequirementFilter has been 
	 * attached to a GUI-component that processes 
	 * and validates input.
	 * 
	 * @param isAttatched Whether the 
	 * RequirementFilter has been attached to a GUI-
	 * component.
	 */
	public void setAttatched(boolean isAttatched) {
		this.isAttatched = isAttatched;
	}
	
	/**
	 * Returns whether this RequirementFilter has 
	 * been attached to a GUI-component that 
	 * processes and validates input.
	 * 
	 * @return Returns whether this 
	 * RequirementFilter is attached to a GUI-
	 * component.
	 */
	public boolean getAttatched() {
		return this.isAttatched;
	}
}
