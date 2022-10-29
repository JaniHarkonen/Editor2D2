package editor2d2.gui.components.requirements;

import editor2d2.gui.components.RequirementFilter;

public class RequireDoubleBetween extends RequirementFilter<Double> {
	
	private double minimum;
	private double maximum;
	
	public RequireDoubleBetween(Double min, Double max) {
		this.minimum = -Double.MIN_VALUE;
		this.maximum = Double.MAX_VALUE;
		
		if( min != null )
		this.minimum = min;
		
		if( max != null )
		this.maximum = max;
		
		this.defaultValue = min;
	}

	@Override
	protected boolean validateInput() {
		
		if( this.input == null || this.input.equals("") )
		return false;
		
		double n = 0;
		
		try
		{
			n = Double.parseDouble(this.input);
		}
		catch (NumberFormatException e)
		{
			return false;
		}
		
		if( n < this.minimum || n > this.maximum )
		return false;
		
		return true;
	}
}
