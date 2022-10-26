package editor2d2.model.app.tool;

import editor2d2.model.app.actions.scale.AScale;
import editor2d2.model.app.actions.scale.AScaleContext;

public class TScale extends Tool {
	
		// Last reported mouse X-coordinate
	private double previousX;
	

	public TScale() {
		super();
		this.name = "Scale";
		this.shortcutKey = "D";
	}
	
	
	@Override
	protected int usePrimary(ToolContext c) {
		
		if( !c.isContinuation )
		this.previousX = c.locationX;
		
		AScaleContext ac = new AScaleContext(c);
		ac.scaleIncrement = c.locationX - this.previousX;
		this.previousX = c.locationX;
		
		(new AScale()).performImpl(ac);
		
		return USE_SUCCESSFUL;
	}
	
	@Override
	public int stop(ToolContext c) {
		if( c.controller.placeableSelectionManager.getSelection().size() <= 0 )
		return USE_FAILED;
		
		(new AScale()).perform(new AScaleContext(c));
		
		return USE_SUCCESSFUL;
	}
}
