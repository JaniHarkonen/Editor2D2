package editor2d2.common.grid;

import java.util.ArrayList;

/**
 * Each class whose instances are to be placed in a Grid
 * implement this marker interface.
 * @author User
 *
 */
public interface Gridable {
	
	default ArrayList<? extends Object> getCollection() {
		return null;
	}
}
