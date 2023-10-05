package editor2d2.model.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * SelectionManagers are class that can be used to keep 
 * track of instances of a certain type. This class 
 * provides the basic functionalities for selection, 
 * de-selection, additive selection and subtractive 
 * selection. SelectionManager also holds a reference 
 * to the first selected item so that classes needing 
 * only a single selected instance can call the 
 * getSelectedItem-method without having to extract an 
 * item from the selection-HashMap.
 * <br/><br/>
 * 
 * A HashMap is used to store the current selection so 
 * that the selection cannot contain more than one 
 * reference to the same item.
 * 
 * @author User
 *
 * @param <T> Type of the instances that are to be 
 * selected by the SelectionManager.
 */
public class SelectionManager<T> {

	/**
	 * HashMap that holds all the currently selected 
	 * items. Using a Map makes it impossible for the 
	 * same item to exist multiple times in the 
	 * selection as this is not intended.
	 */
	protected Map<T, Boolean> selection;
	
	/**
	 * Reference to the "first" item in the selection 
	 * HashMap. This can be used whenever only a 
	 * single item is needed from the selection 
	 * HashMap without having to access its entry 
	 * Set.
	 */
	protected T firstSelection;
	
	/**
	 * Constructs a SelectionManager instance with an 
	 * empty selection HashMap.
	 */
	public SelectionManager() {
		this.selection = new HashMap<T, Boolean>();
		this.firstSelection = null;
	}
	
	/**
	 * Sets the selection of the SelectionManager by 
	 * copying the references to items stored in the 
	 * given ArrayList. The first item in the list 
	 * will be set as the firstSelection, however, 
	 * if the list is empty the first selection will 
	 * be set to NULL.
	 * <br/><br/>
	 * 
	 * <b>Notice: </b>this method does NOT accept a 
	 * NULL selection as the selection HashMap 
	 * should never be NULL.
	 * 
	 * @param selection ArrayList containing 
	 * references to all the items that are to be 
	 * selected.
	 */
	public void setSelection(ArrayList<T> selection) {
		if( selection == null )
		return;
		
		this.selection = new HashMap<T, Boolean>();
		
		if( selection.size() > 0 )
		this.firstSelection = selection.get(0);
		else
		this.firstSelection = null;
		
		for( int i = 0; i < selection.size(); i++ )
		this.selection.put(selection.get(i), true);
	}
	
	/**
	 * Sets the selection of the SelectionManager to 
	 * only encompass a single item. The item will 
	 * also be selected as the first selection.
	 * <br/><br/>
	 * 
	 * <b>Notice: </b>the given selection should not 
	 * be NULL. If you want to de-select the current 
	 * selection, see the deselect-method for more 
	 * information.
	 * 
	 * @param selection Reference to the item that 
	 * should constitute the new selection. Must not 
	 * be NULL.
	 */
	public void setSelection(T selection) {
		if( selection == null )
		return;
		
		this.selection = new HashMap<T, Boolean>();
		this.firstSelection = selection;
		this.selection.put(selection, true);
	}
	
	/**
	 * Performs an additive selection by adding a given 
	 * item to the selection. If the item is already 
	 * selected, nothing happens as the selection 
	 * HashMap can not hold more than one reference to 
	 * the same item.
	 * 
	 * @param selection Reference to the item that 
	 * should be added to the selection if it's not 
	 * already selected.
	 */
	public void addSelection(T selection) {
		this.selection.put(selection, true);
	}
	
	/**
	 * Performs an additive selection by adding items 
	 * stored in a given ArrayList to the selection. If 
	 * any item is already selected, it wont be added 
	 * to the selection as the selection HashMap can 
	 * not hold more than one reference to the same 
	 * item.
	 * 
	 * @param selection Reference to the ArrayList 
	 * containing all the items that are to be added 
	 * to the selection.
	 */
	public void addSelection(ArrayList<T> selection) {
		for( int i = 0; i < selection.size(); i++ )
		addSelection(selection.get(i));
	}
	
	/**
	 * Performs a subtractive selection by removing a 
	 * given item from the selection. If the item is 
	 * not selected, nothing happens.
	 * 
	 * @param selection Reference to the item that 
	 * should be removed from the selection.
	 */
	public void removeSelection(T selection) {
		if( selection == this.firstSelection )
		this.firstSelection = null;
		
		this.selection.remove(selection);
	}
	
	/**
	 * Performs a subtractive selection by removing 
	 * the items stored in a given ArrayList from 
	 * the selection. If any item is not selected, 
	 * nothing happens to it.
	 * 
	 * @param selection Reference to the ArrayList 
	 * containing all the items that are to be 
	 * removed from the selection.
	 */
	public void removeSelection(ArrayList<T> selection) {
		for( int i = 0; i < selection.size(); i++ )
		removeSelection(selection.get(i));
		
		if( this.selection.size() > 0 )
		this.firstSelection = getSelection().get(0);
	}
	
	/**
	 * De-selects the entire selection by removing 
	 * all the items from the selection.
	 */
	public void deselect() {
		this.firstSelection = null;
		this.selection = new HashMap<T, Boolean>();
	}
	
	// GETTERS AND SETTERS
	
	/**
	 * Returns an ArrayList containing a reference 
	 * to each currently selected item. If no item 
	 * is selected, an empty ArrayList is returned.
	 * <br/><br/>
	 * 
	 * <b>Notice: </b>this method iterates over 
	 * each reference in the selection HashMap 
	 * which takes time. If you only need to get a 
	 * single selected item, but dont care which 
	 * one, call the getSelectedItem-method to 
	 * return the first selected item.
	 * 
	 * @return Reference to an ArrayList containing 
	 * a reference to each selected item.
	 */
	public ArrayList<T> getSelection() {
		ArrayList<T> copy = new ArrayList<T>();
		
		if( this.firstSelection != null )
		copy.add(this.firstSelection);
		
		for( Map.Entry<T, Boolean> en : this.selection.entrySet() )
		{
			T p = en.getKey();
			
			if( p != this.firstSelection )
			copy.add(p);
		}
		
		return copy;
	}
	
	/**
	 * Returns the first selected item. Use this 
	 * method whenever only one item needs to 
	 * be returned from the selection rather 
	 * than calling the getSelection-method as 
	 * getSelection iterates over the entire 
	 * selection HashMap. 
	 * 
	 * See getSelection-method for more 
	 * information on getting the entire 
	 * selection.
	 * 
	 * @return Reference to the first selected 
	 * item.
	 */
	public T getSelectedItem() {
		return this.firstSelection;
	}
	
	/**
	 * Checks whether a given item is found in 
	 * the selection HashMap.
	 * 
	 * @param p Reference to the item that is 
	 * to be checked.
	 * 
	 * @return Whether the given item is found 
	 * in the selection.
	 */
	public boolean checkSelected(T p) {
		return this.selection.containsKey(p);
	}
}
