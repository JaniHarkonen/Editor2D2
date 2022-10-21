package editor2d2.model.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectionManager<T> {

		// List of currently selected items
	private final Map<T, Boolean> selection;
	
		// Reference to the item that was first selected
	private T firstSelection;
	
	
	public SelectionManager() {
		this.selection = new HashMap<T, Boolean>();
		this.firstSelection = null;
	}
	
	
		// Sets the selection of items by copying the
		// given list of items
	public void setSelection(ArrayList<T> selection) {
		if( selection == null )
		return;
		
		this.selection.clear();
		this.firstSelection = selection.get(0);
		
		for( int i = 0; i < selection.size(); i++ )
		this.selection.put(selection.get(i), true);
	}
	
		// Sets the selection to consist of a single item
	public void setSelection(T selection) {
		if( selection == null )
		return;
		
		this.selection.clear();
		this.firstSelection = selection;
		this.selection.put(selection, true);
	}
	
		// Adds a given item to the selection excluding
		// it, if it's already been selected
	public void addSelection(T selection) {
		this.selection.put(selection, true);
	}
	
		// Adds a given list of items to the selection
		// excluding the ones that are already selected
	public void addSelection(ArrayList<T> selection) {
		for( int i = 0; i < selection.size(); i++ )
		addSelection(selection.get(i));
	}
	
		// Removes a given item from the selection
	public void removeSelection(T selection) {
		this.selection.remove(selection);
	}
	
		// Removes a given list of items from the selection
	public void removeSelection(ArrayList<T> selection) {
		for( int i = 0; i < selection.size(); i++ )
		removeSelection(selection.get(i));
	}
	
		// De-selects everything
	public void deselect() {
		this.firstSelection = null;
		this.selection.clear();
	}
	
	/*********************** GETTERS **************************/
	
		// Returns a copy of the list of selected items
	public ArrayList<T> getSelection() {
		ArrayList<T> copy = new ArrayList<T>();
		
		if( this.firstSelection != null )
		copy.add(this.firstSelection);
		
		for( Map.Entry<T, Boolean> en : this.selection.entrySet() )
		{
			T p = en.getKey();
			
			if( p != this.firstSelection )
			copy.add(this.firstSelection);
		}
		
		return copy;
	}
	
		// Returns the first item in the list of selected
		// items
	public T getSelectedItem() {
		return this.firstSelection;
	}
	
		// Returns whether a given item is included in the
		// selection
	public boolean checkSelected(T p) {
		return this.selection.containsKey(p);
	}
}
