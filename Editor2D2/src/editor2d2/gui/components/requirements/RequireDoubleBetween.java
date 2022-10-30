package editor2d2.gui.components.requirements;

import editor2d2.gui.components.RequirementFilter;

public class RequireDoubleBetween extends RequirementFilter<Double> {
	
	private double minimum;
	private double maximum;
	
	public RequireDoubleBetween(Require flag, double val1, double val2) {
		this.minimum = Integer.MIN_VALUE;
		this.maximum = Integer.MAX_VALUE;
		
		switch( flag )
		{
			case MIN_ONLY:
				this.minimum = val1;
				break;
			case MAX_ONLY:
				this.maximum = val1;
				break;
			case MIN_MAX:
				this.minimum = val1;
				this.maximum = val2;
				break;
		}
		
		this.defaultValue = val1;
	}
	
	public RequireDoubleBetween(double min, double max) {
		this(Require.MIN_MAX, min, max);
	}
	
	public RequireDoubleBetween(Require flag, double minOrMax) {
		this(flag, minOrMax, 0);
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
		
		this.value = n;
		return true;
	}
}
