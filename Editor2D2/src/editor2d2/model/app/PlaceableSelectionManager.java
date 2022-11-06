package editor2d2.model.app;

import java.util.ArrayList;
import java.util.Set;

import editor2d2.model.project.scene.placeable.Placeable;

public class PlaceableSelectionManager extends SelectionManager<Placeable> {
	
	private ArrayList<Placeable> clipboard;
	
	
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
	
	public void copyToClipboard() {
		this.clipboard = this.getSelection();
	}
	
	public ArrayList<Placeable> getClipboardSelection() {
		return this.clipboard;
	}
	
	
	private void updatePlaceables(Set<Placeable> selection, boolean isSelected) {
		for( Placeable p : selection )
		p.setSelected(isSelected);
	}
}
