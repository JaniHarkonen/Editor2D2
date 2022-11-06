package editor2d2.model.app.tool;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import editor2d2.Application;
import editor2d2.DebugUtils;
import editor2d2.common.Bounds;
import editor2d2.model.app.actions.move.AMove;
import editor2d2.model.app.actions.move.AMoveContext;
import editor2d2.model.project.scene.placeable.Placeable;

public class TMove extends Tool {
	
		// List of points representing the difference between
		// the location the Tool was used at and the selected
		// Placeables
	private ArrayList<Point.Double> offsets;
	

	public TMove() {
		super();
		this.name = "Move";
		this.shortcutKey = "M";
		this.icon = Application.resources.getGraphic("icon-tool-move");
		this.offsets = null;
	}
	
	
	@Override
	protected int usePrimary(ToolContext c) {
		if( !c.isContinuation )
		{
				// Calculates the offsets between the Placeables and
				// the location the Tool is being used at
			this.offsets = new ArrayList<Point.Double>();
			
			for( Placeable p : c.selection )
			{
				double cx = p.getX() - c.locationX;
				double cy = p.getY() - c.locationY;
				
				DebugUtils.log(p, this);
				
				this.offsets.add(new Point.Double(cx, cy));
			}
		}
		
			// Draw the selected Placeables onto a BufferedImage that will then
			// be overlaid over the SceneView
		BufferedImage overlay = new BufferedImage(640, 480, BufferedImage.TYPE_INT_ARGB);
		Graphics2D gg = (Graphics2D) overlay.getGraphics();
		double camZ = c.controller.getActiveScene().getCamera().getZ();
		Composite prevComp = gg.getComposite();
		
			// Transparent
		gg.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
		
		double 	mx = c.locationX * camZ,
				my = c.locationY * camZ;
		
		for( int i = 0; i < this.offsets.size(); i++ )
		{
			Placeable p = c.selection.get(i);
			Point.Double off = this.offsets.get(i);
			Bounds bounds = p.getBounds();
			
			double 	width = (bounds.right - bounds.left) * camZ,
					height = (bounds.bottom - bounds.top) * camZ;
			
			p.drawPlaceable(gg, mx + (off.x * camZ), my + (off.y * camZ), width, height);
		}
		
		c.sceneView.setOverlay(overlay);
		gg.setComposite(prevComp);
		
		return USE_SUCCESSFUL;
	}
	
	@Override
	public int stop(ToolContext c) {
		if( c.order != Tool.PRIMARY_FUNCTION )
		return USE_FAILED;
		
		AMoveContext ac = new AMoveContext(c);
		ac.targetLayer = c.targetLayer;
		ac.offsets = this.offsets;
		ac.locationX = c.locationX;
		ac.locationY = c.locationY;
		
		(new AMove()).perform(ac);
		c.sceneView.setOverlay(null);
		
		return USE_SUCCESSFUL;
	}
}
