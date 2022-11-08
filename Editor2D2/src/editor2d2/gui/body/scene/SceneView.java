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
import java.awt.image.BufferedImage;

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
import editor2d2.model.app.Controller;
import editor2d2.model.app.HotkeyListener;
import editor2d2.model.app.SelectionManager;
import editor2d2.model.app.Tools;
import editor2d2.model.app.actions.deletemany.ADeleteMany;
import editor2d2.model.app.actions.deletemany.ADeleteManyContext;
import editor2d2.model.app.actions.paste.APaste;
import editor2d2.model.app.actions.paste.APasteContext;
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
	
	private BufferedImage overlay;
	
	
	public SceneView() {
		this.overlay = null;
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
		Application.controller.subscriptionService.subscribe(editor2d2.model.Handles.LAYER_VISIBILITY, "SceneView", this);
		Application.controller.subscriptionService.subscribe(editor2d2.model.Handles.LAYER_DELETED, "SceneView", this);
		
		Application.window.subscriptionService.register(Handles.SCENE_VIEW, this);
	}
	
	
	@Override
	public void onNotification(String handle, Vendor vendor) {
		boolean skipUpdate = false;
		Controller controller = Application.controller;
		
		if( HotkeyListener.didKeyUpdate(handle) )
		{
			HotkeyListener hl = (HotkeyListener) vendor;
			Layer activeLayer = controller.getActiveLayer();
			
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
				SelectionManager<Placeable> mngr = controller.placeableSelectionManager;
				ADeleteManyContext ac = new ADeleteManyContext(controller, activeLayer);
				(new ADeleteMany()).perform(ac);
				
				mngr.deselect();
			}
			
				// Undo
			if( HotkeyListener.isSequenceHeld(hl, KeyEvent.VK_CONTROL, 'Z') )
			controller.undoAction();
			
				// Redo
			if( HotkeyListener.isSequenceHeld(hl, KeyEvent.VK_CONTROL, 'Y') )
			controller.redoAction();
			
				// Deselect all
			if( HotkeyListener.isSequenceHeld(hl, KeyEvent.VK_CONTROL, 'D') )
			controller.placeableSelectionManager.deselect();
			
				// Copy selection
			if( HotkeyListener.isSequenceHeld(hl, KeyEvent.VK_CONTROL, 'C') )
			controller.placeableSelectionManager.copyToClipboard();
			
				// Cut selection
			if( HotkeyListener.isSequenceHeld(hl, KeyEvent.VK_CONTROL, 'X') )
			{
				controller.placeableSelectionManager.copyToClipboard();
				ADeleteManyContext ac = new ADeleteManyContext(controller, activeLayer);
				(new ADeleteMany()).perform(ac);
			}
			
				// Paste selection
			if( HotkeyListener.isSequenceHeld(hl, KeyEvent.VK_CONTROL, 'V') )
			{
				APasteContext ac = new APasteContext(controller, activeLayer);
				ac.selection = controller.placeableSelectionManager.getClipboardSelection();
				(new APaste()).perform(ac);
				controller.selectTool(Tools.getAvailableTools()[1]);
			}
			
				// Pan-mode
			this.isSpaceDown = HotkeyListener.isSequenceHeld(hl, KeyEvent.VK_SPACE);
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
					break;
					
					// Cursor grid visibility was toggled
				case Handles.CURSOR_GRID_TOGGLED:
					this.drawCursorGrid = !this.drawCursorGrid;
					break;
					
					// Layer grid visibility was toggled
				case Handles.LAYER_GRID_TOGGLED:
					this.drawLayerGrid = !this.drawLayerGrid;
					update();
					break;
				
					// Layer visibility was toggled
				case editor2d2.model.Handles.LAYER_VISIBILITY:
				case editor2d2.model.Handles.LAYER_DELETED:
					break;
					
				default: skipUpdate = true; break;
			}
		}
		
		if( !skipUpdate )
		update();
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
		tc.sceneView = this;
		
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
				Rectangle2D.Double bounds_cam = new Rectangle2D.Double(
					cam.getOnScreenX(cbounds.left), cam.getOnScreenY(cbounds.top),
					cam.getOnScreenX(cbounds.right), cam.getOnScreenY(cbounds.bottom)
				);
				
				gg.setColor(Color.RED);
				gg.draw(bounds_cam);
				
				Layer activeLayer = Application.controller.getActiveLayer();
				
					// Render grids
				if( drawCursorGrid || drawLayerGrid )
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
					
						// Render cursor grid
					if( drawCursorGrid )
					drawGrid(gg, ox, oy, (int) cam.getOnScreenX(right), (int) cam.getOnScreenY(bottom), (int) cw, (int) ch);
					
						// Render Layer grid
					if( drawLayerGrid )
					{
						if( activeLayer != null )
						{
							cw = activeLayer.getObjectGrid().getCellWidth() * cam.getZ();
							ch = activeLayer.getObjectGrid().getCellHeight() * cam.getZ();
							
							gg.setColor(Color.RED);
							dash[0] = 5.0f;
							dash[1] = 5.0f;
							gg.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2.0f, dash, 2.0f));
							drawGrid(gg, ox, oy, (int) cam.getOnScreenX(right), (int) cam.getOnScreenY(bottom), (int) cw, (int) ch);
						}
					}
				}
				
				if( overlay != null )
				{
					gg.drawImage(overlay,
						(int) onScreenOriginX,
						(int) onScreenOriginY,
						null
					);
				}
				
				Tool selectedTool = Application.controller.getSelectedTool();
				
				g.setColor(Color.BLACK);
				
				if( selectedTool != null )
				drawStringNewline(selectedTool.getDescription(), 8, 16, 16, gg);
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
				overlay = null;
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
	
		// Draws a grid in a given context with given bounds and
		// cell dimensions
	private void drawGrid(Graphics2D g, int x1, int y1, int x2, int y2, int cw, int ch) {
		if( cw > 1 && ch > 1 )
		{
			for( int i = x1; i < x2; i += cw )
			g.drawLine(i, y1, i, y2);
			
			for( int i = y1; i < y2; i += ch )
			g.drawLine(x1, i, x2, i);
		}
	}
	
		// Draws a string containing newline characters
	private void drawStringNewline(String str, int x, int y, int sep, Graphics2D g) {
		String[] split = str.split("\n");
		int s = split.length;
		for( int i = 0; i < s; i++ )
		g.drawString(split[i], x, y + i * sep);
	}
	
	
		// Returns the width of a cursor cell
	public int getCursorCellWidth() {
		return this.cursorCellWidth;
	}
	
		// Returns the height of a cursor cell
	public int getCursorCellHeight() {
		return this.cursorCellHeight;
	}
	
		// Sets the drawable overlay
	public void setOverlay(BufferedImage overlay) {
		this.overlay = overlay;
		update();
	}
}
