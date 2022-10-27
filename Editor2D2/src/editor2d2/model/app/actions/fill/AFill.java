package editor2d2.model.app.actions.fill;

import java.util.ArrayList;

import editor2d2.common.grid.FloodFiller;
import editor2d2.common.grid.Grid;
import editor2d2.common.grid.Gridable;
import editor2d2.common.grid.NullCell;
import editor2d2.model.app.actions.Action;
import editor2d2.model.app.actions.ActionContext;
import editor2d2.model.project.scene.Layer;
import editor2d2.model.project.scene.placeable.Placeable;
import editor2d2.modules.data.placeable.DataCell;
import editor2d2.modules.image.placeable.Tile;

public class AFill extends Action {
	
	private class PlaceableFloodFiller extends FloodFiller<Placeable> {
		
			// Layer that whose Grid is to be filled
		private Layer targetLayer;
		
		
		public PlaceableFloodFiller(Layer targetLayer) {
			super(targetLayer.getObjectGrid());
			this.targetLayer = targetLayer;
		}
		
		
		@Override
		protected boolean isFree(Placeable p, int cx, int cy) {
			Gridable g = this.targetGrid.get(cx, cy);
			
			if( g instanceof NullCell )
			return false;
			
			Placeable at = (Placeable) this.targetGrid.get(cx, cy);
			
			if( at == null )
			return true;
			
				// Replace Placeables dervide from a different Asset
			if( !at.getAsset().getIdentifier().equals(p.getAsset().getIdentifier()) )
			{
				replacedPlaceables.add(at);
				return true;
			}
			
			return false;
		}
		
		@Override
		protected boolean fillAt(Placeable p, int cx, int cy) {
			Placeable duplicate = p.duplicate();
			boolean successful = super.fillAt(duplicate, cx, cy);
			
			if( successful )
			placements.add(duplicate);
			
			return successful;
		}
		
		@Override
		protected void place(Placeable p, int cx, int cy) {
			this.targetLayer.attemptPlace(cx, cy, p);
		}
	}
	
		// List of Placeables replaced by the fill
	private ArrayList<Placeable> replacedPlaceables;
	
		// List of Placeables that were placed by the fill
	private ArrayList<Placeable> placements;
	
		// Layer that the fill targets
	private Layer targetLayer;
	
	
	public AFill() {
		this.targetLayer = null;
		this.replacedPlaceables = new ArrayList<Placeable>();
		this.placements = new ArrayList<Placeable>();
	}
	

	@Override
	public void undo() {
		for( Placeable p : this.placements )
		p.delete();
		
		for( Placeable p : this.replacedPlaceables )
		p.changeLayer(this.targetLayer);
	}

	@Override
	public void redo() {
		for( Placeable p : this.replacedPlaceables )
		p.delete();
		
		for( Placeable p : this.placements )
		p.changeLayer(this.targetLayer);
	}

	@Override
	public void performImpl(ActionContext c) {
		AFillContext ac = (AFillContext) c;
		
		if(
			!(ac.placeable instanceof Tile) &&
			!(ac.placeable instanceof DataCell)
		)
		return;
		
		this.targetLayer = ac.target;
		
		Grid ogrid = this.targetLayer.getObjectGrid();
		int cw = ogrid.getCellWidth(),
			ch = ogrid.getCellHeight();
		
			// Fill the tiles using flood fill
		this.replacedPlaceables = new ArrayList<Placeable>();
		PlaceableFloodFiller flooder = new PlaceableFloodFiller(this.targetLayer);
		flooder.fillTarget(ac.placeable, (int) (ac.locationX / cw), (int) (ac.locationY / ch));
	}
}
