package editor2d2.gui.components;

public abstract class RequirementFilter<T> {

	protected boolean isValid;
	
	protected String input;
	
	protected T defaultValue;
	
	protected T value;
	
	protected boolean isAttatched;
	
	
	public RequirementFilter() {
		this.isValid = false;
		this.input = null;
		this.defaultValue = null;
		this.value = null;
		this.isAttatched = false;
	}
	
	
	protected abstract boolean validateInput();
	
	public void updateInput(String updatedInput) {
		this.input = updatedInput;
		this.isValid = validateInput();
	}
	
	public boolean checkValid() {
		return this.isValid;
	}
	
	public T getValue() {
		if( checkValid() )
		return this.value;
		
		return this.defaultValue;
	}
	
	public void setAttatched(boolean isAttatched) {
		this.isAttatched = isAttatched;
	}
	
	public boolean getAttatched() {
		return this.isAttatched;
	}
}
