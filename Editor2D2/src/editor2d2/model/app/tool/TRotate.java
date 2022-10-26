package editor2d2.model.app.tool;

import java.util.ArrayList;

import editor2d2.Application;
import editor2d2.model.app.actions.rotate.ARotate;
import editor2d2.model.app.actions.rotate.ARotateContext;
import editor2d2.model.project.scene.placeable.Placeable;
import editor2d2.modules.object.placeable.Instance;

public class TRotate extends Tool {
	
	private ArrayList<Placeable> initialSelection;
	

	public TRotate() {
		super();
		this.name = "Rotate";
		this.shortcutKey = "R";
		this.icon = Application.resources.getGraphic("icon-tool-rotate");
	}
	
	
	@Override
	protected int usePrimary(ToolContext c) {
		if( !c.isContinuation )
		{
			this.initialSelection = new ArrayList<Placeable>();
			
			for( Placeable p : c.selection )
			{
				if( p instanceof Instance )
				this.initialSelection.add(p.duplicate());
			}
		}
		
		ARotateContext ac = new ARotateContext(c);
		ac.initialSelection = this.initialSelection;
		
		(new ARotate()).performImpl(ac);
		return USE_SUCCESSFUL;
	}
	
	@Override
	public int stop(ToolContext c) {
		if( c.controller.placeableSelectionManager.getSelection().size() <= 0 )
		return USE_FAILED;
		
		ARotateContext ac = new ARotateContext(c);
		ac.initialSelection = this.initialSelection;
		
		(new ARotate()).perform(ac);
		
		return USE_SUCCESSFUL;
	}
}
