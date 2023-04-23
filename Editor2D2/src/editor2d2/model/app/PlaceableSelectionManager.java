package editor2d2.model.app;

import java.util.ArrayList;
import java.util.Set;

import editor2d2.model.project.scene.placeable.Placeable;

/**
 * This class can be used to track the Placeables that 
 * have currently been selected. 
 * PlaceableSelectionManager is a wrapper that provides 
 * methods for the selection, de-selection, additive 
 * selection and subtractive selection of Placeables as 
 * well as for creating a clipboard for selections.
 * <br/><br/>
 * 
 * Once the selection has been modified, the impacted 
 * Placeables can be updated in the editor to display 
 * them as highlighted by calling the updatePlaceables-
 * method. <b>Notice: </b>this only impacts the 
 * isSelected-flag of the Placeables, however, their 
 * visual representation is not altered in this method.
 * <br/><br/>
 * 
 * This class extends the SelectionManager-class. See 
 * SelectionManager for the more abstract super class.
 * 
 * @author User
 *
 */
public class PlaceableSelectionManager extends SelectionManager<Placeable> {
	
	/**
	 * ArrayList that functions as the clipboard for 
	 * the Placeables that have been copied via CTRL+C 
	 * or cut via CTRL+X.
	 */
	private ArrayList<Placeable> clipboard;
	
	/**
	 * Constructs a PlceableSelectionManager instance 
	 * with an empty ArrayList as the clipboard.
	 */
	public PlaceableSelectionManager() {
		this.clipboard = new ArrayList<Placeable>();
	}
	
	
	@Override
	public void setSelection(ArrayList<Placeable> selection) {
		if( selection == null )
		return;
		
		updatePlaceables(this.selection.keySet(), false);
		super.setSelection(selection);
		updatePlaceables(this.selection.keySet(), true);
	}
	
	@Override
	public void setSelection(Placeable selection) {
		if( selection == null )
		return;
		
		updatePlaceables(this.selection.keySet(), false);
		
		super.setSelection(selection);
		selection.setSelected(true);
	}
	
	@Override
	public void addSelection(Placeable selection) {
		super.addSelection(selection);
		selection.setSelected(true);
	}
	
	@Override
	public void removeSelection(Placeable selection) {
		super.removeSelection(selection);
		selection.setSelected(false);
	}
	
	@Override
	public void deselect() {
		updatePlaceables(this.selection.keySet(), false);
		super.deselect();
	}
	
	/**
	 * Copies the current selection of the instance 
	 * to the clipboard and returns whether the 
	 * operation was successful.
	 * <br/><br/>
	 * 
	 * <b>False</b>, if the selection contained no 
	 * Placeables.
	 * <br/>
	 * <b>True</b>, if Placeables were added to the 
	 * clipboard.
	 * 
	 * @return Whether the current selection was 
	 * successfully copied to the clipboard. 
	 */
	public boolean copyToClipboard() {
		this.clipboard = getSelection();
		
		if( this.clipboard.size() > 0 )
		return true;
		
		this.clipboard = new ArrayList<Placeable>();
		return false;
	}
	
	/**
	 * Returns the ArrayList containing the selection 
	 * of Placeables currently stored in the 
	 * clipboard.
	 * <br/><br/>
	 * 
	 * <b>Notice: </b>this method returns a reference 
	 * to the ArrayList, however, the ArrayList 
	 * should NOT be changed outside of this class.
	 * 
	 * @return Reference to the ArrayList containing 
	 * the Placeables in the clipboard.
	 */
	public ArrayList<Placeable> getClipboardSelection() {
		return this.clipboard;
	}
	
	/**
	 * Updates the isSelected-flag of all of the 
	 * Placeable instances currently selected by the 
	 * PlaceableSelectionManager to a given value.
	 * The selection has to be provided as a Set as 
	 * selection is derived from the key Set of the 
	 * HashMap stored in the selection-field. See
	 * SelectionManager for more information on the 
	 * selection-field.
	 * <br/><br/>
	 * 
	 * <b>Notice: </b>this only impacts the 
	 * isSelected-flag of the Placeables, however, their 
	 * visual representation is not altered in this 
	 * method.
	 * <br/><br/>
	 * 
	 * See Placeable for more information on the 
	 * isSelected-flag.
	 * 
	 * @param selection A Set of Placeables that are to 
	 * be updated (typically the key Set of the 
	 * selection-HashMap).
	 * @param isSelected Whether the Placeables should 
	 * be set as selected or unselected.
	 */
	private void updatePlaceables(Set<Placeable> selection, boolean isSelected) {
		for( Placeable p : selection )
		p.setSelected(isSelected);
	}
}
