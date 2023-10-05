package editor2d2.common.grid;

import java.util.ArrayList;

/**
 * Each class whose instances are to be placed in a Grid
 * implement this marker interface. Some Gridables may 
 * function as collections where multiple objects are 
 * stacked in a single cell. These Gridables may implement 
 * the getCollection-method that will return the underlying 
 * ArrayList containing the objects.
 * 
 * @author User
 *
 */
public interface Gridable {
	
	/**
	 * Returns an ArrayList of all the objects stored in 
	 * this Gridable.
	 * 
	 * Only to be implemented by Gridables that stack.
	 * 
	 * @return Returns the ArrayList containing the objects 
	 * stored in this Gridable.
	 */
	default ArrayList<? extends Object> getCollection() {
		return null;
	}
}
