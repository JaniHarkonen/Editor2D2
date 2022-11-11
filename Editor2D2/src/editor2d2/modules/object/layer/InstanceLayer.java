package editor2d2.modules.object.layer;

import java.util.ArrayList;

import editor2d2.common.Bounds;
import editor2d2.common.grid.Gridable;
import editor2d2.common.grid.NullCell;
import editor2d2.model.project.Asset;
import editor2d2.model.project.scene.Layer;
import editor2d2.model.project.scene.Scene;
import editor2d2.model.project.scene.placeable.Placeable;
import editor2d2.modules.object.asset.EObject;
import editor2d2.modules.object.placeable.Instance;

public class InstanceLayer extends Layer {
	
		// Width of a grid cell in pixels
	public static int OBJECT_LAYER_GRID_CELL_WIDTH = 1920;
	
		// Height of a grid cell in pixels
	public static int OBJECT_LAYER_GRID_CELL_HEIGHT = 1080;
	
	
	public InstanceLayer(Scene scene) {
		super(scene, OBJECT_LAYER_GRID_CELL_WIDTH, OBJECT_LAYER_GRID_CELL_HEIGHT);
	}
	

		// Places an object to the given coordinates in the layer
		// and places it into the object grid
	@Override
	public Placeable place(int x, int y, Placeable p) {
		return place((double) x, y, p);
	}
	
	@Override
	public Placeable place(double x, double y, Placeable p) {
		Instance inst = (Instance) p;
		int cw = this.objectGrid.getCellWidth(),
			ch = this.objectGrid.getCellHeight();
		
		int cx = (int) (x / cw),
			cy = (int) (y / ch);
		
		Gridable cell = this.objectGrid.get(cx, cy);
		
			// Out of bounds
		if( cell instanceof NullCell )
		return null;
		
			// No objects exist in the cell, create a new object list
			// to represent the cell
		if( cell == null )
		{
			cell = new ObjectArray();
			this.objectGrid.put(cx, cy, cell);
		}
		
		inst.changeLayer(this);
		inst.setCellPosition(cx, cy);
		inst.setOffsets(x - cx * cw, y - cy * ch);
		
		((ObjectArray) cell).add(inst);
		
		return null;
	}
	
	@Override
	public Instance delete(int cx, int cy) {
		return null;
	}
	
	@Override
	public Instance delete(double x, double y) {
		Gridable g = this.objectGrid.get(x, y);
		
		if( g == null || g instanceof NullCell )
		return null;
		
		ObjectArray oa = (ObjectArray) g;
		
		for( Instance inst : oa.getAllInstances() )
		{
			Bounds instBounds = inst.getBounds();
			
			if( x < instBounds.left || y < instBounds.top || x > instBounds.right || y > instBounds.bottom )
			continue;
			
			inst.delete();
			return inst;
		}
		
		return null;
	}
	
	@Override
	public void deleteByAsset(Asset asset) {
		int cw = getObjecGridRowLength(),
			ch = getObjectGridColumnLength();
	
		for( int cx = 0; cx < cw; cx++ )
		for( int cy = 0; cy < ch; cy++ )
		{
			ObjectArray oa = (ObjectArray) this.objectGrid.getFast(cx, cy);
			
			if( oa == null )
			continue;
			
			ArrayList<Instance> instances = oa.getAllInstances();
			
			for( int i = 0; i < instances.size(); i++ )
			{
				Instance inst = instances.get(i);
				if( inst.getAsset() != asset )
				continue;
				
				inst.delete();
				i--;
			}
			
		}
	}
	
	@Override
	public ArrayList<Placeable> selectPlaceables(int cx1, int cy1, int cx2, int cy2) {
		ArrayList<Placeable> selection = new ArrayList<Placeable>();
		
		for( int x = cx1; x < cx2; x++ )
		for( int y = cy1; y < cy2; y++ )
		{
			ObjectArray oa = (ObjectArray) this.objectGrid.get(x, y);
			
			if( oa == null )
			continue;
				
			for( Placeable p : oa.objects )
			if( p != null )
			selection.add(p);
		}
		
		return selection;
	}
	
	@Override
	public ArrayList<Placeable> selectPlaceables(double x1, double y1, double x2, double y2) {
		ArrayList<Placeable> gridSelection = super.selectPlaceables(x1, y1, x2, y2);
		
			// Grid cells of an InstanceLayer are vast because the Instances
			// do not necessarily respect cellular coordinates. Therefore the
			// Placeables returned by the super-method may fall outside of the
			// selected area. Another sweep must be made.
		int s = gridSelection.size();
		int offset = 0;
		for( int i = 0; i < s; i++ )
		{
			Placeable p = gridSelection.get(i - offset);
			Bounds pb = p.getBounds();
			
			if( pb.right <= x1 || pb.bottom <= y1 || pb.left > x2 || pb.top > y2 )
			{
				gridSelection.remove(i - offset);
				offset++;
			}
		}
		
		return gridSelection;
	}
	

	@Override
	public Asset getReferencedAsset() {
		return new EObject();
	}
	
	
	@Override
	protected boolean filterCheck(Gridable p) {
		return p instanceof Instance;
	}
}
