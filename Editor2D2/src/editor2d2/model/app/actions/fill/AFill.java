package editor2d2.model.app.actions.fill;

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
			
			return !at.getAsset().getIdentifier().equals(p.getAsset().getIdentifier());
		}
		
		@Override
		protected boolean fillAt(Placeable p, int cx, int cy) {
			return super.fillAt(p.duplicate(), cx, cy);
		}
		
		@Override
		protected void place(Placeable p, int cx, int cy) {
			this.targetLayer.attemptPlace(cx, cy, p);
		}
	}
	
	
	

	@Override
	public void undo() {
		
	}

	@Override
	public void redo() {
		
	}

	@Override
	public void performImpl(ActionContext c) {
		AFillContext ac = (AFillContext) c;
		
		if(
			!(ac.placeable instanceof Tile) &&
			!(ac.placeable instanceof DataCell)
		)
		return;
		
		Grid ogrid = ac.target.getObjectGrid();
		int cw = ogrid.getCellWidth(),
			ch = ogrid.getCellHeight();
		
			// Fill the tiles using flood fill
		PlaceableFloodFiller flooder = new PlaceableFloodFiller(ac.target);
		flooder.fillTarget(ac.placeable, (int) (ac.locationX / cw), (int) (ac.locationY / ch));
	}
}
