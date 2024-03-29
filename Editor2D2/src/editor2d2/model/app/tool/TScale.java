package editor2d2.model.app.tool;

import java.util.ArrayList;

import editor2d2.model.app.actions.scale.AScale;
import editor2d2.model.app.actions.scale.AScaleContext;
import editor2d2.model.project.scene.placeable.Placeable;
import editor2d2.modules.object.placeable.Instance;

public class TScale extends Tool {
	
		// Last reported mouse X-coordinate
	private double previousX;
	
		// Duplicates of the initial selection
	private ArrayList<Placeable> initialSelection;
	

	public TScale() {
		super();
		this.name = "Scale";
		this.description = "Scale selection.\n"
						 + "Left-click: scale with mouse\n"
						 + "Left-click + CTRL: scale horizonally only\n"
						 + "Left-click + SHIFT: scale vertically only";
		this.shortcutKey = "D";
		this.initialSelection = new ArrayList<Placeable>();
	}
	
	
	@Override
	protected int usePrimary(ToolContext c) {
		if( !c.isContinuation )
		{
			this.previousX = c.locationX;
			this.initialSelection = new ArrayList<Placeable>();
			
				// Duplicate each selected Placeable and store
				// to enable rollback when undoing the Action
			for( Placeable p : c.selection )
			{
				if( p instanceof Instance )
				this.initialSelection.add(p.duplicate());
			}
		}
		
		AScaleContext ac = new AScaleContext(c);
		ac.horizontalScaleIncrement = c.locationX - this.previousX;
		ac.verticalScaleIncrement = c.locationX - this.previousX;
		ac.initialSelection = this.initialSelection;
		
		this.previousX = c.locationX;
		(new AScale()).performImpl(ac);
		
		return USE_SUCCESSFUL;
	}
	
	@Override
	public int use(ToolContext c) {
		if( c.selection == null || c.selection.size() <= 0 )
		return USE_FAILED;
		
		if( !c.isContinuation )
		{
			this.previousX = c.locationX;
			this.initialSelection = new ArrayList<Placeable>();
			
				// Duplicate each selected Placeable and store
				// to enable rollback when undoing the Action
			for( Placeable p : c.selection )
			{
				if( p instanceof Instance )
				this.initialSelection.add(p.duplicate());
			}
		}
		
		double dx = c.locationX - this.previousX;
		
		AScaleContext ac = new AScaleContext(c);
		
		if( c.order == Tool.PRIMARY_FUNCTION )
		{
			ac.horizontalScaleIncrement = dx;
			ac.verticalScaleIncrement = dx;
		}
		else if( c.order == Tool.TERTIARY_FUNCTION )
		ac.horizontalScaleIncrement = dx;
		else if( c.order == 4 )
		ac.verticalScaleIncrement = dx;
		
		ac.initialSelection = this.initialSelection;
		
		this.previousX = c.locationX;
		(new AScale()).performImpl(ac);
		
		return USE_SUCCESSFUL;
	}
	
	@Override
	public int stop(ToolContext c) {
		if( c.selection.size() <= 0 )
		return USE_FAILED;
		
		AScaleContext ac = new AScaleContext(c);
		ac.initialSelection = this.initialSelection;
		
		(new AScale()).perform(ac);
		
		return USE_SUCCESSFUL;
	}
}
