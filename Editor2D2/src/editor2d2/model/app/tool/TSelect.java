package editor2d2.model.app.tool;

import java.util.ArrayList;

import editor2d2.Application;
import editor2d2.model.Handles;
import editor2d2.model.app.actions.select.ASelect;
import editor2d2.model.app.actions.select.ASelectContext;
import editor2d2.model.project.scene.placeable.Placeable;

public class TSelect extends Tool {
	
		// In-scene X-coordinate of the start of the selection
	private double startX;
	
		// In-scene Y-coordinate of the start of the selection
	private double startY;
	
		// List of initially selected Placeables
	private ArrayList<Placeable> initialSelection;
	
	
	public TSelect() {
		super();
		this.name = "Select";
		this.description = "Selects objects.\nLeft-click: select\nLeft-click + CTRL: add selection\nLeft-click + ALT: subtract selection";
		this.shortcutKey = "S";
		this.icon = Application.resources.getGraphic("icon-tool-select");
		this.initialSelection = null;
	}
	
	@Override
	public int use(ToolContext c) {
		int outcome = super.use(c);
		
		if( outcome == USE_UNSPECIFIED )
		return usePrimary(c);
		
		return USE_FAILED;
	}
	
	
	@Override
	protected int usePrimary(ToolContext c) {
		if( !c.isContinuation )
		{
			this.startX = c.locationX;
			this.startY = c.locationY;
			this.initialSelection = c.selection;
			
			return USE_SUCCESSFUL;
		}
		
		return USE_FAILED;
	}
	
	@Override
	protected int useTertiary(ToolContext c) {
		return usePrimary(c);
	}
	
	@Override
	public int stop(ToolContext c) {
		if( c.order == Tool.SECONDARY_FUNCTION || c.targetLayer == null )
		return USE_FAILED;
		
		int type = ASelect.TYPE_NORMAL;
		
		if( c.order == Tool.TERTIARY_FUNCTION )
		type = ASelect.TYPE_ADD;
		else if( c.order == 4 )
		type = ASelect.TYPE_SUBTRACT;
		
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
		
			// Pass the selection area coordinates into the ActionContext
		ASelectContext ac = new ASelectContext(c);
		ac.initialSelection = this.initialSelection;
		ac.type = type;
		ac.startX = sx;
		ac.startY = sy;
		ac.endX = ex;
		ac.endY = ey;
		
		(new ASelect()).perform(ac);
		Application.controller.subscriptionService.register(Handles.SELECTED_PLACEABLE, Application.controller);
		
		return USE_SUCCESSFUL;
	}

}
