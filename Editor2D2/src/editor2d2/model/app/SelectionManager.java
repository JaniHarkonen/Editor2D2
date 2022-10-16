package editor2d2.model.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import editor2d2.model.project.scene.placeable.Placeable;

public class SelectionManager {

		// List of currently selected Placeables
	private final Map<Placeable, Boolean> selection;
	
		// Reference to the Placeable that was first selected
	private Placeable firstSelection;
	
	
	public SelectionManager() {
		this.selection = new HashMap<Placeable, Boolean>();
		this.firstSelection = null;
	}
	
	
		// Sets the selection of Placeables by copying the
		// given list of Placeables
	public void setSelection(ArrayList<Placeable> selection) {
		if( selection == null )
		return;
		
		this.selection.clear();
		this.firstSelection = selection.get(0);
		
		for( int i = 0; i < selection.size(); i++ )
		this.selection.put(selection.get(i), true);
	}
	
		// Sets the selection to consist of a single Placeable
	public void setSelection(Placeable selection) {
		if( selection == null )
		return;
		
		this.selection.clear();
		this.firstSelection = selection;
		this.selection.put(selection, true);
	}
	
		// Adds a given Placeable to the selection excluding
		// it, if it's already been selected
	public void addSelection(Placeable selection) {
		this.selection.put(selection, true);
	}
	
		// Adds a given list of Placeables to the selection
		// excluding the ones that are already selected
	public void addSelection(ArrayList<Placeable> selection) {
		for( int i = 0; i < selection.size(); i++ )
		addSelection(selection.get(i));
	}
	
		// Removes a given Placeable from the selection
	public void removeSelection(Placeable selection) {
		this.selection.remove(selection);
	}
	
		// Removes a given list of Placeables from the selection
	public void removeSelection(ArrayList<Placeable> selection) {
		for( int i = 0; i < selection.size(); i++ )
		removeSelection(selection.get(i));
	}
	
		// De-selects everything
	public void deselect() {
		this.firstSelection = null;
		this.selection.clear();
	}
	
	/*********************** GETTERS **************************/
	
		// Returns a copy of the list of selected Placeables
	public ArrayList<Placeable> getSelection() {
		ArrayList<Placeable> copy = new ArrayList<Placeable>();
		copy.add(this.firstSelection);
		
		for( Map.Entry<Placeable, Boolean> en : this.selection.entrySet() )
		{
			Placeable p = en.getKey();
			
			if( p != this.firstSelection )
			copy.add(this.firstSelection);
		}
		
		return copy;
	}
	
		// Returns the first Placeable in the list of selected
		// Placeables
	public Placeable getSelectedPlaceable() {
		return this.firstSelection;
	}
	
		// Returns whether a given Placeable is included in the
		// selection
	public boolean checkSelected(Placeable p) {
		return this.selection.containsKey(p);
	}
}
