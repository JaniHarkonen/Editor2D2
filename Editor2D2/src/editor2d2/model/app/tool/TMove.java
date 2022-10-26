package editor2d2.model.app.tool;

import editor2d2.Application;
import editor2d2.model.app.actions.move.AMove;
import editor2d2.model.app.actions.move.AMoveContext;

public class TMove extends Tool {
	
	private double startX;
	
	private double startY;
	

	public TMove() {
		super();
		this.name = "Move";
		this.shortcutKey = "M";
		this.icon = Application.resources.getGraphic("icon-tool-move");
	}
	
	
	@Override
	protected int usePrimary(ToolContext c) {
		if( !c.isContinuation )
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
		
		double	sx = this.startX,
				sy = this.startY,
				ex = c.locationX,
				ey = c.locationY;
		
		AMoveContext ac = new AMoveContext(c);
		ac.startX = sx;
		ac.startY = sy;
		ac.endX = ex;
		ac.endY = ey;
		
		(new AMove()).perform(ac);
		
		return USE_SUCCESSFUL;
	}
}
