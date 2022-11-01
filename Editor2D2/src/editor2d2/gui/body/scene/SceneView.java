package editor2d2.gui.body.scene;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import editor2d2.Application;
import editor2d2.common.Bounds;
import editor2d2.common.dragbox.DragBox;
import editor2d2.common.dragbox.DragBoxPoll;
import editor2d2.common.grid.Grid;
import editor2d2.gui.GUIComponent;
import editor2d2.gui.GUIUtilities;
import editor2d2.gui.Handles;
import editor2d2.model.app.HotkeyListener;
import editor2d2.model.app.SelectionManager;
import editor2d2.model.app.actions.deletemany.ADeleteMany;
import editor2d2.model.app.actions.deletemany.ADeleteManyContext;
import editor2d2.model.app.tool.Tool;
import editor2d2.model.app.tool.ToolContext;
import editor2d2.model.project.scene.Camera;
import editor2d2.model.project.scene.Layer;
import editor2d2.model.project.scene.Scene;
import editor2d2.model.project.scene.placeable.Placeable;
import editor2d2.subservice.Subscriber;
import editor2d2.subservice.Vendor;

public class SceneView extends GUIComponent implements Subscriber, Vendor {
	
		// Reference to the Scene that this view will render
	private final Scene scene;
	
		// The JPanel that the scene will be rendered in
	private JPanel view;
	
		// Reference to the DragBox that will be used to drag the Scene view
	private DragBox sceneDragger;
	
		// Whether space bar is down
	private boolean isSpaceDown;
	
		// Whether the tertiary functionalities of the Tools should be used
	private int useOrder;
	
	private int cursorCellWidth;
	
	private int cursorCellHeight;
	
	
	public SceneView(Scene scene) {
		this.scene = scene;
		this.view = createView();
		this.isSpaceDown = false;
		this.useOrder = Tool.PRIMARY_FUNCTION;
		
		this.cursorCellWidth = 32;
		this.cursorCellHeight = 32;
		
		int d = Integer.MAX_VALUE / 2;
		this.sceneDragger = new DragBox(-d, -d, d * 2, d * 2);
		
		Application.controller.getHotkeyListener().subscribe("SceneView", this);
		Application.window.subscriptionService.register(Handles.SCENE_VIEW, this);
	}
	
	
	@Override
	public void onNotification(String handle, Vendor vendor) {
		if( HotkeyListener.didKeyUpdate(handle) )
		{
			boolean skipUpdate = false;
			HotkeyListener hl = (HotkeyListener) vendor;
			
				// Determine the order of Tool functionality
			if( HotkeyListener.isSequenceHeld(hl, KeyEvent.VK_CONTROL) )
			this.useOrder = Tool.TERTIARY_FUNCTION;
			else if( HotkeyListener.isSequenceHeld(hl, KeyEvent.VK_ALT) )
			this.useOrder = 4;
			else
			this.useOrder = Tool.PRIMARY_FUNCTION;
			
				// Delete Placebles upon pressing Delete key
			if( HotkeyListener.isSequenceHeld(hl, KeyEvent.VK_DELETE) )
			{
				SelectionManager<Placeable> mngr = Application.controller.placeableSelectionManager;
				Layer l = Application.controller.getActiveLayer();
				ADeleteManyContext ac = new ADeleteManyContext(Application.controller, l);
				(new ADeleteMany()).perform(ac);
				
				mngr.deselect();
			}
			
				// Undo
			if( HotkeyListener.isSequenceHeld(hl, KeyEvent.VK_CONTROL, 'Z') )
			Application.controller.undoAction();
			
				// Redo
			if( HotkeyListener.isSequenceHeld(hl, KeyEvent.VK_CONTROL, 'Y') )
			Application.controller.redoAction();
			
				// Pan-mode
			this.isSpaceDown = HotkeyListener.isSequenceHeld(hl, KeyEvent.VK_SPACE);
			
			if( !skipUpdate )
			update();
		}
	}
	

	@Override
	protected JPanel draw() {
		JPanel container = GUIUtilities.createDefaultPanel();
		container.add(this.view);
		
		if( this.isSpaceDown )
		container.setCursor(new Cursor(Cursor.MOVE_CURSOR));
		else
		container.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		
		return container;
	}
	
		// Uses the currently selected Tool specifying whether
		// the use is continuation of a prior use as well as the
		// order of function
	private void useTool(double x, double y, boolean isContinuation, int order, boolean stop) {
		ToolContext tc = new ToolContext();
		tc.isContinuation = isContinuation;
		tc.locationX = Grid.snapToGrid(x, this.cursorCellWidth);
		tc.locationY = Grid.snapToGrid(y, this.cursorCellHeight);
		tc.order = order;
		
		int outcome = Application.controller.useTool(tc, stop);
		
		if( Tool.checkSuccessfulUse(outcome) )
		update();
	}
	
	private void useTool(double x, double y, boolean isContinuation, int order) {
		useTool(x, y, isContinuation, order, false);
	}
	
		// Creates the JPanel that will render the Scene by creating an
		// anonymous class extending JPanel
	private JPanel createView() {
		Camera cam = this.scene.getCamera();
		
		@SuppressWarnings("serial")
		JPanel container = new JPanel() {
			
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				Graphics2D gg = (Graphics2D) g;
				
					// Render the Scene
				cam.render(gg);
				
					// Render the Scene bounds
				float[] dash = { 6.0f, 6.0f };
				gg.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2.0f, dash, 0.0f));
				gg.setColor(Color.BLACK);
				
				double cam_z = cam.getZ();
				
				Rectangle2D.Double bounds_scene = new Rectangle2D.Double(
					cam.getOnScreenX(0), cam.getOnScreenY(0),
					scene.getWidth() * cam_z, scene.getHeight() * cam_z
				);
				
				gg.draw(bounds_scene);
				
					// Render the Camera bounds
				Bounds cbounds = cam.getBounds();
				
				gg.setColor(Color.RED);
				Rectangle2D.Double bounds_cam = new Rectangle2D.Double(
					cam.getOnScreenX(cbounds.left), cam.getOnScreenY(cbounds.top),
					cam.getOnScreenX(cbounds.right), cam.getOnScreenY(cbounds.bottom)
				);
					
				gg.draw(bounds_cam);
				
					// Render the placement grid of the layer that is currently active
				/*dash[0] = 1.0f;
				dash[1] = 1.0f;
				
				gg.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2.0f, dash, 0.0f));
				
				for( int i = 0; i < scene.getWidth(); i += scene.getLayers().get(0).getObjectGrid().getCellWidth() )
				gg.drawLine(i, 0, i, scene.getHeight());
				
				for( int i = 0; i < scene.getHeight(); i += scene.getLayers().get(0).getObjectGrid().getCellHeight() )
				gg.drawLine(0, i,  scene.getWidth(), i);
				*/
			}
		};
		
			// Register mouse clicks
		container.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				int toolUseOrder = -1;
				
				if( GUIUtilities.checkLeftClick(e) )
				toolUseOrder = useOrder;
				else if( GUIUtilities.checkRightClick(e) )
				toolUseOrder = Tool.SECONDARY_FUNCTION;
				
				useTool(cam.getInSceneX(e.getX()), cam.getInSceneY(e.getY()), false, toolUseOrder);
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				int toolUseOrder = -1;
				
				if( GUIUtilities.checkLeftClick(e) && isSpaceDown )
				sceneDragger.stopDragging();
				else if( GUIUtilities.checkLeftClick(e) )
				toolUseOrder = useOrder;
				else if( GUIUtilities.checkRightClick(e) )
				toolUseOrder = Tool.SECONDARY_FUNCTION;
				
				useTool(cam.getInSceneX(e.getX()), cam.getInSceneY(e.getY()), false, toolUseOrder, true);
			}
		});
		
			// Register mouse wheel
		container.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				cam.shift(0, 0, -e.getWheelRotation() * 0.1);
				update();
			}
		});
		
			// Register mouse movements
		container.addMouseMotionListener(new MouseMotionAdapter() {
			
			@Override
			public void mouseDragged(MouseEvent e) {
				
				int toolUseOrder = -1;
				
				if( SwingUtilities.isLeftMouseButton(e) && !isSpaceDown )
				toolUseOrder = useOrder;
				else if( SwingUtilities.isRightMouseButton(e) )
				toolUseOrder = Tool.SECONDARY_FUNCTION;
				
				if( toolUseOrder > 0 )
				useTool(cam.getInSceneX(e.getX()), cam.getInSceneY(e.getY()), true, toolUseOrder);
				
					// Handle Scene dragging (uses SwingUtilities as getButton returns non-zero only
					// on the first click)
				else if( SwingUtilities.isLeftMouseButton(e) && isSpaceDown )
				{
					if( !sceneDragger.checkDragging() )
					sceneDragger.startDragging(e.getX(), e.getY(), 1);
					else
					{
						DragBoxPoll dgpoll = sceneDragger.poll(e.getX(), e.getY(), 1);
						cam.shift(dgpoll.x / cam.getZ(), dgpoll.y / cam.getZ(), 0);
						update();
					}
				}
			}
		});
		
		return container;
	}
}
