package editor2d2.gui.components.requirements;

/**
 * This enumeration holds the different additional 
 * requirements that can be applied to certain 
 * general RequirementFilters. For example, the 
 * RequireDoubleBetween-class requires an input to 
 * be a double between two values. However, the 
 * values in this enumeration can be used to 
 * specify whether only one or both edge values 
 * will be allowed.
 * 
 * @author User
 *
 */
public enum Require {
	/**
	 * Only the minimum value is considered. The 
	 * maximum is the maximum value allowed by 
	 * the JVM.
	 */
	MIN_ONLY,
	
	/**
	 * Only the maximum value is considered. The 
	 * minimum is the minimum value allowed by 
	 * the JVM.
	 */
	MAX_ONLY,
	
	/**
	 * Both the minimum and maximum value are 
	 * considered.
	 */
	MIN_MAX
}
