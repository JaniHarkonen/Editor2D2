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
import editor2d2.gui.body.scenectrl.SceneControlsPane;
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
	
	private boolean drawCursorGrid;
	
	private boolean drawLayerGrid;
	
	
	public SceneView() {
		this.scene = Application.controller.getActiveScene();
		this.view = createView();
		this.isSpaceDown = false;
		this.useOrder = Tool.PRIMARY_FUNCTION;
		this.drawCursorGrid = true;
		this.drawLayerGrid = false;
		
		this.cursorCellWidth = 32;
		this.cursorCellHeight = 32;
		
		int d = Integer.MAX_VALUE / 2;
		this.sceneDragger = new DragBox(-d, -d, d * 2, d * 2);
		
		Application.controller.getHotkeyListener().subscribe("SceneView", this);
		Application.window.subscriptionService.subscribe(Handles.CURSOR_GRID_SETTINGS_CHANGED, "SceneView", this);
		Application.window.subscriptionService.subscribe(Handles.CURSOR_GRID_TOGGLED, "SceneView", this);
		Application.window.subscriptionService.subscribe(Handles.LAYER_GRID_TOGGLED, "SceneView", this);
		
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
		else
		{
			switch( handle )
			{
					// Changes were made to the cursor grid settings
				case Handles.CURSOR_GRID_SETTINGS_CHANGED:
					SceneControlsPane controls = (SceneControlsPane) vendor;
					this.cursorCellWidth = controls.getCursorCellWidth();
					this.cursorCellHeight = controls.getCursorCellHeight();
					update();
					
					break;
					
					// Cursor grid visibility was toggled
				case Handles.CURSOR_GRID_TOGGLED:
					this.drawCursorGrid = !this.drawCursorGrid;
					update();
					break;
					
					// Layer grid visibility was toggled
				case Handles.LAYER_GRID_TOGGLED:
					this.drawLayerGrid = !this.drawLayerGrid;
					update();
					break;
			}
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
				
				double onScreenOriginX = cam.getOnScreenX(0);
				double onScreenOriginY = cam.getOnScreenY(0);
				
				Rectangle2D.Double bounds_scene = new Rectangle2D.Double(
					onScreenOriginX, onScreenOriginY,
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
				
					// Render the cursor grid
				if( drawCursorGrid )
				{
					dash[0] = 1.0f;
					dash[1] = 1.0f;
					
					gg.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2.0f, dash, 2.0f));
					gg.setColor(Color.BLACK);
					
					int ox = (int) onScreenOriginX;
					int oy = (int) onScreenOriginY;
					
					double cw = cursorCellWidth * cam.getZ();
					double ch = cursorCellHeight * cam.getZ();
					
					double right = Math.min(scene.getWidth(), cbounds.right);
					double bottom = Math.min(scene.getHeight(), cbounds.bottom);
					
					if( cw > 1 && ch > 1 )
					{
						for( int i = 0; i < right; i += cw )
						gg.drawLine(ox + i, oy, ox + i, (int) cam.getOnScreenY(bottom));
						
						for( int i = 0; i < bottom; i += ch )
						gg.drawLine(ox, oy + i, (int) cam.getOnScreenX(right), oy + i);
					}
				}
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
				Application.window.unfocusAllComponents();
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
		
		container.setFocusable(true);
		return container;
	}
	
	
		// Returns the width of a cursor cell
	public int getCursorCellWidth() {
		return this.cursorCellWidth;
	}
	
		// Returns the height of a cursor cell
	public int getCursorCellHeight() {
		return this.cursorCellHeight;
	}
}
