package editor2d2.gui.components.requirements;

import editor2d2.gui.components.RequirementFilter;

/**
 * This RequirementFilter forces an input to be 
 * between two values. The values must be integers.
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
public class RequireIntegerBetween extends RequirementFilter<Integer> {
	
	/**
	 * Minimum valid integer.
	 */
	private int minimum;
	
	/**
	 * Maximum valid integer.
	 */
	private int maximum;
	
	/**
	 * Constructs a RequireIntegerBetween requirement 
	 * filter that considers values between a given 
	 * maximum value and minimum value to be valid. 
	 * Additional settings can be provided using 
	 * values from the Require-enumeration.
	 * 
	 * If Require.MIN_ONLY or Require.MAX_ONLY are 
	 * used, however, the second integer value passed 
	 * into the method will be ignored. It is 
	 * advisable to use the 
	 * RequireIntegerBetween(Require, int) 
	 * constructor if only the minimum or only the 
	 * maximum need to be considered to avoid 
	 * confusion and redundancy.
	 * 
	 * If Require.MIN_MAX is used, it is preferable 
	 * to use the RequireIntegerBetween(int, int) 
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
	public RequireIntegerBetween(Require flag, int val1, int val2) {
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
	 * Constructs a RequireIntegerBetween instance 
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
	public RequireIntegerBetween(int min, int max) {
		this(Require.MIN_MAX, min, max);
	}
	
	/**
	 * Constructs a RequireIntegerBetween instance 
	 * that determines the validity of an input 
	 * based on a minimum or maximum value. A 
	 * Require-enumeration value must be passed 
	 * onto the method to determine whether the 
	 * threshold value passed is a minimum or 
	 * maximum value. <b>Notice: </b>this method 
	 * only deals with cases when there is only 
	 * a single threshold value. See the 
	 * RequireIntegerBetween(int, int) 
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
	public RequireIntegerBetween(Require flag, int minOrMax) {
		this(flag, minOrMax, 0);
	}

	@Override
	protected boolean validateInput() {
		
		if( this.input == null || this.input.equals("") )
		return false;
		
		int n = 0;
		
			// Attempt to parse an integer
		try
		{
			n = Integer.parseInt(this.input);
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
