package editor2d2.gui.components.requirements;

import editor2d2.gui.components.RequirementFilter;

/**
 * This RequirementFilter forces an input to be 
 * between two values. The values must be doubles.
 * Additional settings provided by the Require-
 * enumeration can be used to only set a minimum, 
 * maximum or both.
 * 
 * See Require-enumeration for more information 
 * on additional settings in general 
 * RequirementFilters.
 * 
 * @author User
 *
 */
public class RequireDoubleBetween extends RequirementFilter<Double> {
	
	/**
	 * Minimum valid double.
	 */
	private double minimum;
	
	/**
	 * Maximum valid double.
	 */
	private double maximum;
	
	/**
	 * Constructs a RequireDoubleBetween requirement 
	 * filter that considers values between a given 
	 * maximum value and minimum value to be valid. 
	 * Additional settings can be provided using 
	 * values from the Require-enumeration.
	 * 
	 * If Require.MIN_ONLY or Require.MAX_ONLY are 
	 * used, however, the second double value passed 
	 * into the method will be ignored. It is 
	 * advisable to use the 
	 * RequireDoubleBetween(Require, double) 
	 * constructor if only the minimum or only the 
	 * maximum need to be considered to avoid 
	 * confusion and redundancy.
	 * 
	 * If Require.MIN_MAX is used, it is preferable 
	 * to use the 
	 * RequireDoubleBetween(double, double) 
	 * constructor instead.
	 * 
	 * This method should in general be avoided as 
	 * the other constructors are often clearer and 
	 * more natural.
	 * 
	 * By default, this filter returns the first 
	 * threshold value.
	 * 
	 * @param flag Require-enumeration value that 
	 * determines how the threshold values should 
	 * be interpreted.
	 * @param val1 First threshold value (may be the 
	 * minimum or maximum depending on the Require-
	 * flag).
	 * @param val2 Second threshold value (may be the 
	 * minimum or maximum depending on the Require-
	 * flag).
	 */
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
	
	/**
	 * Constructs a RequireDoubleBetween instance 
	 * that determines the validity of an input 
	 * based on a minimum and maximum value. The 
	 * input is considered valid when it lies 
	 * between the two values.
	 * 
	 * By default, this filter returns the first 
	 * threshold value.
	 * 
	 * @param min Minimum valid value.
	 * @param max Maximum valid value.
	 */
	public RequireDoubleBetween(double min, double max) {
		this(Require.MIN_MAX, min, max);
	}
	
	/**
	 * Constructs a RequireDoubleBetween instance 
	 * that determines the validity of an input 
	 * based on a minimum or maximum value. A 
	 * Require-enumeration value must be passed 
	 * onto the method to determine whether the 
	 * threshold value passed is a minimum or 
	 * maximum value. <b>Notice: </b>this method 
	 * only deals with cases when there is only 
	 * a single threshold value. See the 
	 * RequireDoubleBetween(double, double) 
	 * constructor if you want to have two 
	 * threshold values, the minimum AND the 
	 * maximum.
	 * 
	 * By default, this filter returns the first 
	 * threshold value.
	 * 
	 * @param flag Require-enumeration value 
	 * that determines whether the threshold 
	 * value is a minimum value or a maximum 
	 * value.
	 * @param minOrMax The minimum or maximum 
	 * value depending on the Reqire-flag.
	 */
	public RequireDoubleBetween(Require flag, double minOrMax) {
		this(flag, minOrMax, 0);
	}

	@Override
	protected boolean validateInput() {
		if( this.input == null || this.input.equals("") )
		return false;
		
		double n = 0;
		
			// Attempt to parse a double
		try
		{
			n = Double.parseDouble(this.input);
		}
		catch (NumberFormatException e)
		{
			return false;
		}
		
			// Threshold check
		if( n < this.minimum || n > this.maximum )
		return false;
		
		this.value = n;
		return true;
	}
}
