package editor2d2.model.app.tool;

import java.util.ArrayList;

import editor2d2.model.project.scene.placeable.Placeable;

public class TSelect extends Tool {
	
		// In-scene X-coordinate of the start of the selection
	private double startX;
	
		// In-scene Y-coordinate of the start of the selection
	private double startY;
	
	
	public TSelect() {
		super();
		this.name = "Select";
		this.shortcutKey = "S";
	}
	
	
	@Override
	protected int usePrimary(ToolContext c) {
		if( c.isContinuation == false )
		{
			this.startX = c.locationX;
			this.startY = c.locationY;
			
			return USE_SUCCESSFUL;
		}
		
		return USE_FAILED;
	}
	
	@Override
	public int stop(ToolContext c) {
		if( c.order != Tool.PRIMARY_FUNCTION )
		return USE_FAILED;
		
		double 	sx = this.startX,
				sy = this.startY,
				ex = c.locationX,
				ey = c.locationY;

		if( ex < sx )
		{
			sx = ex;
			ex = this.startX;
		}
		
		if( ey < sy )
		{
			sy = ey;
			ey = this.startY;
		}
		
		ArrayList<Placeable> selection = c.targetLayer.selectPlaceables(sx, sy, ex, ey);
		
		return USE_SUCCESSFUL;
	}

}
