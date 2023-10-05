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
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

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
import editor2d2.subservice.SubscriptionService;
import editor2d2.subservice.Vendor;

/**
 * This class is a major GUI-component that renders the currently 
 * active Scene. For the most part, this class only renders a 
 * JPanel that contains the Scene viewed through the Scene's 
 * Camera. 
 * <br/><br/>
 * 
 * SceneView also handles the usage of Tools each time 
 * the user clicks the Scene. Each Tool has different orders of 
 * functionality, some of which are listed in the Tool-class 
 * (such as Tool.PRIMARY_FUNCTION). This class uses hotkeys to 
 * determine the order of functionality when a Tool is used. 
 * Here is a mapping of hotkey combinations to their respective 
 * orders of functionality: <br/><br/>
 * <b>LEFT-CLICK</b> - Primary function <br/>
 * <b>RIGHT-CLICK</b> - Secondary function <br/>
 * <b>CTRL + LEFT-CLICK</b> - Tertiary function <br/>
 * <b>SHIFT + LEFT-CLICK</b> - Quaternary function
 * <br/><br/>
 * 
 * The SceneView also handles the following actions: <br/><br/>
 * <b>SPACE + LEFT-MOUSE DRAG</b> - Pans the Scene's Camera inside 
 * the view according to the mouse <br/>
 * <b>DELETE</b> - Deletes the selected Placeables <br/>
 * <b>CTRL + Z</b> - Undoes the latest Action. <br/>
 * <b>CTRL + Y</b> - Re-does the previously undone Action. <br/>
 * <b>CTRL + D</b> - De-selects the selected Placeables. <br/>
 * <b>CTRL + C</b> - Copies the selected Placeables to the 
 * clipboard. <br/>
 * <b>CTRL + X</b> - Copies the selected Placeables to the 
 * clipboard and deletes the from the Scene. <br/>
 * <b>CTRL + V</b> - Pastes the Placeables in the clipboard to the 
 * Scene.
 * <br/><br/>
 * 
 * Unlike other GUI-components, the SceneView will be rendered 
 * in the constructor using a separate method, createView. This 
 * method has been implemented only to reduce the amount of code 
 * in the constructor. The scene must be established in the 
 * constructor in order to avoid excessive drawing in the draw-
 * method.
 * 
 * <b>NOTES: </b>This class should most likely be split further 
 * into multiple components as it has quite a few 
 * responsibilities and is subscribed to many of Controller's 
 * handles.
 * 
 * See Camera for more information on rendering.
 * <br/><br/>
 * 
 * See Tool for more information on using tools in the editor.
 * 
 * 
 * @author User
 *
 */
public class SceneView extends GUIComponent implements Subscriber, Vendor {
	
	/**
	 * The Scene that will be rendered by the SceneView.
	 */
	private final Scene scene;
	
	/**
	 * JPanel that the Scene will be renderd onto.
	 */
	private JPanel view;
	
	/**
	 * DragBox instance that will be used to pan the Camera 
	 * inside the view.
	 */
	private DragBox sceneDragger;
	
	/**
	 * Whether the space-key is being held down.
	 */
	private boolean isSpaceDown;
	
	/**
	 * The order of functionality that should be used 
	 * when using the currently selected Tool.
	 */
	private int useOrder;
	
	/**
	 * The width of a cursor grid cell (in pixels).
	 */
	private int cursorCellWidth;
	
	/**
	 * The height of a cursor grid cell (in pixels).
	 */
	private int cursorCellHeight;
	
	/**
	 * Whether the cursor grid should be rendered.
	 */
	private boolean drawCursorGrid;
	
	/**
	 * Whether the Layer grid should be rendered.
	 */
	private boolean drawLayerGrid;
	
	/**
	 * The BufferedImage that draws the overlay of 
	 * the Scene. This overlay will contain things 
	 * such as highlighting selected Placeables.
	 */
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
		
		SubscriptionService subsrvController = Application.controller.subscriptionService;
		SubscriptionService subsrvWindow = Application.window.subscriptionService;
		
		Application.controller.getHotkeyListener().subscribe("SceneView", this);
		
		subsrvWindow.subscribe(Handles.CURSOR_GRID_SETTINGS_CHANGED, "SceneView", this);
		subsrvWindow.subscribe(Handles.CURSOR_GRID_TOGGLED, "SceneView", this);
		subsrvWindow.subscribe(Handles.LAYER_GRID_TOGGLED, "SceneView", this);
		subsrvWindow.subscribe(Handles.CAMERA_RETURNED_TO_ORIGIN, "SceneView", this);
		subsrvWindow.subscribe(Handles.HORIZONTAL_SPLIT_ADJUSTED, "SceneView", this);
		
		subsrvController.subscribe(editor2d2.model.Handles.LAYER_VISIBILITY, "SceneView", this);
		subsrvController.subscribe(editor2d2.model.Handles.LAYER_DELETED, "SceneView", this);
		subsrvController.subscribe(editor2d2.model.Handles.LAYER_REORDER, "SceneView", this);
		
		subsrvWindow.register(Handles.SCENE_VIEW, this);
	}
	
	
	@Override
	public void onNotification(String handle, Vendor vendor) {
		boolean skipUpdate = false;
		Controller controller = Application.controller;
		
		if( HotkeyListener.didKeyUpdate(handle) )
		{
			HotkeyListener hl = (HotkeyListener) vendor;
			Layer activeLayer = controller.getActiveLayer();
			int toolUseOrder = Tool.NO_FUNCTION;
			
			skipUpdate = true;
			
				// Determine the order of Tool functionality
			if( HotkeyListener.isSequenceHeld(hl, KeyEvent.VK_CONTROL) )
			toolUseOrder = Tool.TERTIARY_FUNCTION;
			else if( HotkeyListener.isSequenceHeld(hl, KeyEvent.VK_SHIFT) )
			toolUseOrder = Tool.QUATERNARY_FUNCTION;
			else
			toolUseOrder = Tool.PRIMARY_FUNCTION;
			
				// Delete Placebles upon pressing Delete key
			if( HotkeyListener.isSequenceHeld(hl, KeyEvent.VK_DELETE) )
			{
				SelectionManager<Placeable> mngr = controller.placeableSelectionManager;
				ADeleteManyContext ac = new ADeleteManyContext(controller, activeLayer);
				(new ADeleteMany()).perform(ac);
				
				mngr.deselect();
				
				skipUpdate = false;
			}
			
				// Undo
			if( HotkeyListener.isSequenceHeld(hl, KeyEvent.VK_CONTROL, 'Z') )
			{
				controller.undoAction();
				skipUpdate = false;
			}
			
				// Redo
			if( HotkeyListener.isSequenceHeld(hl, KeyEvent.VK_CONTROL, 'Y') )
			{
				controller.redoAction();
				skipUpdate = false;
			}
			
				// Deselect all
			if( HotkeyListener.isSequenceHeld(hl, KeyEvent.VK_CONTROL, 'D') )
			{
				controller.placeableSelectionManager.deselect();
				skipUpdate = false;
			}
			
				// Copy selection
			if( HotkeyListener.isSequenceHeld(hl, KeyEvent.VK_CONTROL, 'C') )
			{
				controller.placeableSelectionManager.copyToClipboard();
				skipUpdate = false;
			}
			
				// Cut selection
			if( HotkeyListener.isSequenceHeld(hl, KeyEvent.VK_CONTROL, 'X') )
			{
				if( !controller.placeableSelectionManager.copyToClipboard() )
				return;
				
				ADeleteManyContext ac = new ADeleteManyContext(controller, activeLayer);
				(new ADeleteMany()).perform(ac);
				
				skipUpdate = false;
			}
			
				// Paste selection
			if( HotkeyListener.isSequenceHeld(hl, KeyEvent.VK_CONTROL, 'V') )
			{
				ArrayList<Placeable> clipboardSelection = controller.placeableSelectionManager.getClipboardSelection();
				
				if( clipboardSelection == null || clipboardSelection.size() <= 0 )
				return;
				
				APasteContext ac = new APasteContext(controller, activeLayer);
				
				ac.selection = clipboardSelection;
				controller.selectTool(Tools.getAvailableTools()[1]);
				(new APaste()).perform(ac);
				
				skipUpdate = false;
			}
			
				// Pan-mode
			this.isSpaceDown = HotkeyListener.isSequenceHeld(hl, KeyEvent.VK_SPACE);
			
			if( toolUseOrder >= 0 )
			{
				this.useOrder = toolUseOrder;
				skipUpdate = false;
			}
			
			if( this.isSpaceDown )
			skipUpdate = false;
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
					break;
				
					// Layer visibility was toggled
				case editor2d2.model.Handles.LAYER_VISIBILITY:
				case editor2d2.model.Handles.LAYER_DELETED:
				case editor2d2.model.Handles.LAYER_REORDER:
				case Handles.CAMERA_RETURNED_TO_ORIGIN:
				case Handles.HORIZONTAL_SPLIT_ADJUSTED:
				case Handles.VERTICAL_SPLIT_ADJUSTED:
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
	
	/**
	 * Uses the currently selected Tool on the active Layer in the 
	 * Scene. This method can also be used to trigger the stop 
	 * action of the selected Tool. The Tool will be used at a 
	 * given world position in the Scene using a given order of 
	 * functionality. Whether this use is a continuation of a 
	 * previous use must also be passed. <i>isContinuation</i> is 
	 * used by Tools that are to be used in succession, such as 
	 * when placing Placeables via CTRL + SHIFT.
	 * 
	 * @param x The world X-coordinate where the Tool is to be 
	 * used.
	 * @param y The world Y-coordinate where the Tool is to be 
	 * used.
	 * @param isContinuation Whether this is a continuation of a 
	 * previous useTool-call.
	 * @param order The order of functionality that is to be 
	 * used.
	 * @param stop Whether the Tool use is to be stopped. FALSE 
	 * is the normal use.
	 */
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
	
	/**
	 * Uses the currently selected Tool on the active Layer in the 
	 * Scene. The Tool will be used at a given world position in 
	 * the Scene using a given order of functionality. Whether 
	 * this use is a continuation of a previous use must also be 
	 * passed. <i>isContinuation</i> is used by Tools that are to 
	 * be used in succession, such as when placing Placeables via 
	 * CTRL + SHIFT.
	 * 
	 * @param x The world X-coordinate where the Tool is to be 
	 * used.
	 * @param y The world Y-coordinate where the Tool is to be 
	 * used.
	 * @param isContinuation Whether this is a continuation of a 
	 * previous useTool-call.
	 * @param order The order of functionality that is to be 
	 * used.
	 */
	private void useTool(double x, double y, boolean isContinuation, int order) {
		useTool(x, y, isContinuation, order, false);
	}
	
	/**
	 * Creates the JPanel that will contain the Scene rendition 
	 * and renders the currently active Scene onto it. This 
	 * method first renders the Scene according to the Scene's 
	 * Camera by calling its render-method. Next, the bounds 
	 * of the Scene will be rendered if they are visible to the 
	 * Camera. Finally, the cursor grid and Layer grid will be 
	 * rendered in that order. The Scene overlay will also be 
	 * rendered on top of everything (the overlay will draw the 
	 * selection rectangle).
	 * <br/><br/>
	 * 
	 * MouseListeners will also be added to the created JPanel 
	 * that will be used to listen for mouse clicks and 
	 * releases. Whenever the left or right mouse button is 
	 * pressed down, the currently selected Tool will be used.
	 * When the left or right mouse button is released, the 
	 * use of the currently selected Tool is stopped.
	 * <br/><br/>
	 * 
	 * A MouseMotionListener as well as a 
	 * MouseWheelWheelListener will be added to listen mouse 
	 * drags and zooms via the mouse wheel. The 
	 * MouseMotionListner will be used to listen to mouse 
	 * drag events to allow for a successive use of Tools as 
	 * well as panning of the Scene Camera.
	 * <br/><br/>
	 * 
	 * See the render-method of Camera-class for more 
	 * information on rendering of Scenes.
	 * 
	 * @return Reference to the JPanel that contains the 
	 * rendition of the active Scene.
	 */
	private JPanel createView() {
		Camera cam = this.scene.getCamera();
		
		@SuppressWarnings("serial")
		JPanel container = new JPanel() {
			
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				Graphics2D gg = (Graphics2D) g;
				Bounds cbounds = cam.getBounds();
				
					// Render the Scene
				cam.render(gg);
				
					// Render the Scene bounds
				double onScreenOriginX = cam.getOnScreenX(0);
				double onScreenOriginY = cam.getOnScreenY(0);
				
				double viewPortRight = cam.getOnScreenX(Math.min(scene.getWidth(), cbounds.right));
				double viewPortBottom = cam.getOnScreenY(Math.min(scene.getHeight(), cbounds.bottom));
				
				float[] dash = { 6.0f, 6.0f };
				gg.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2.0f, dash, 0.0f));
				gg.setColor(Color.BLACK);
				
					// Top bound
				double x1 = Math.max(0, onScreenOriginX);
				double y1 = Math.max(0, onScreenOriginY);
				Line2D.Double sceneBound = new Line2D.Double(x1, y1, viewPortRight, y1);
				
				if( onScreenOriginY >= 0 )
				gg.draw(sceneBound);
				
					// Left bound
				if( onScreenOriginX >= 0 )
				{
					sceneBound.x1 = x1;
					sceneBound.y1 = y1;
					sceneBound.x2 = x1;
					sceneBound.y2 = viewPortBottom;
					gg.draw(sceneBound);
				}
				
					// Bottom bound
				if( viewPortBottom < cam.getPortHeight() )
				{
					sceneBound.x1 = x1;
					sceneBound.y1 = viewPortBottom;
					sceneBound.x2 = viewPortRight;
					sceneBound.y2 = viewPortBottom;
					gg.draw(sceneBound);
				}
				
					// Right bound
				if( viewPortRight < cam.getPortWidth() )
				{
					sceneBound.x1 = viewPortRight;
					sceneBound.y1 = y1;
					sceneBound.x2 = viewPortRight;
					sceneBound.y2 = viewPortBottom;
					gg.draw(sceneBound);
				}
				
				double cam_z = cam.getZ();
				
				Layer activeLayer = Application.controller.getActiveLayer();
				
					// Render grids
				if( drawCursorGrid || drawLayerGrid )
				{
					dash[0] = 1.0f;
					dash[1] = 1.0f;
					
					gg.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2.0f, dash, 2.0f));
					gg.setColor(Color.BLACK);
					
					int ox = (int) Math.max(onScreenOriginX % cursorCellWidth, onScreenOriginX);
					int oy = (int) Math.max(onScreenOriginY % cursorCellHeight, onScreenOriginY);
					
					double cw = cursorCellWidth * cam_z;
					double ch = cursorCellHeight * cam_z;
					
						// Render cursor grid
					if( drawCursorGrid )
					drawGrid(gg, ox, oy, (int) viewPortRight, (int) viewPortBottom, (int) cw, (int) ch);
					
						// Render Layer grid
					if( drawLayerGrid )
					{
						if( activeLayer != null )
						{
							cw = activeLayer.getObjectGrid().getCellWidth();
							ch = activeLayer.getObjectGrid().getCellHeight();
							
							gg.setColor(Color.RED);
							dash[0] = 5.0f;
							dash[1] = 5.0f;
							gg.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2.0f, dash, 2.0f));
							drawGrid(gg, ox, oy, (int) viewPortRight, (int) viewPortBottom, (int) cw, (int) ch);
						}
					}
				}
				
					// Render overlay
				if( overlay != null )
				{
					gg.drawImage(
						overlay, 
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
				if( isSpaceDown )
				return;
				
				int toolUseOrder = Tool.NO_FUNCTION;
				
				if( GUIUtilities.checkLeftClick(e) )
				toolUseOrder = useOrder;
				else if( GUIUtilities.checkRightClick(e) )
				toolUseOrder = Tool.SECONDARY_FUNCTION;
				
				useTool(cam.getInSceneX(e.getX()), cam.getInSceneY(e.getY()), false, toolUseOrder);
				Application.window.unfocusAllComponents();
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				int toolUseOrder = Tool.NO_FUNCTION;
				
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
				if( !isSpaceDown )
				{
					int toolUseOrder = Tool.NO_FUNCTION;
					
					if( SwingUtilities.isLeftMouseButton(e) )
					toolUseOrder = useOrder;
					else if( SwingUtilities.isRightMouseButton(e) )
					toolUseOrder = Tool.SECONDARY_FUNCTION;
					
					if( toolUseOrder > 0 )
					useTool(cam.getInSceneX(e.getX()), cam.getInSceneY(e.getY()), true, toolUseOrder);
				}
				else if( SwingUtilities.isLeftMouseButton(e) )
				{
						// Handle Scene dragging (uses SwingUtilities as getButton returns non-zero only
						// on the first click)
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
	
	/**
	 * A helper method that draws a grid in a given 
	 * Graphics2D context. The grid will be placed 
	 * within a specified area and each cell will 
	 * have the given dimensions (in pixels).
	 * 
	 * @param g Graphics2D object that specifies the 
	 * context in which the grid is to be drawn.
	 * @param x1 The X-coordinate where the grid is to 
	 * begin.
	 * @param y1 The Y-coordinate where the grid is to 
	 * begin.
	 * @param x2 The X-coordinate where the grid is to 
	 * end.
	 * @param y2 The X-coordinate where the grid is to 
	 * end.
	 * @param cw The width of a grid cell (in pixels).
	 * @param ch The height of a grid cell (in pixels).
	 */
	private void drawGrid(Graphics2D g, int x1, int y1, int x2, int y2, int cw, int ch) {
		if( cw > 1 && ch > 1 )
		{
			for( int i = x1; i < x2; i += cw )
			g.drawLine(i, y1, i, y2);
			
			for( int i = y1; i < y2; i += ch )
			g.drawLine(x1, i, x2, i);
		}
	}
	
	/**
	 * A helper method to draw a string in a given Graphics2D
	 * context. The string will be drawn using the drawString-
	 * method of the Graphics2D-class and will appear at a 
	 * given position. The amount of separation between the 
	 * lines (line spacing) must be specified.
	 * <br/><br/>
	 * 
	 * The lines in the string must be separated using a new 
	 * line character (\n).
	 * 
	 * @param str The string that is to be drawn.
	 * @param x The X-coordinate where the string is to be 
	 * drawn.
	 * @param y The Y-coordinate where the string is to be 
	 * drawn.
	 * @param sep Amount of separation (in pixels) between the 
	 * lines.
	 * @param g Reference to the Graphics2D object that the 
	 * string is to be drawn in.
	 */
	private void drawStringNewline(String str, int x, int y, int sep, Graphics2D g) {
		String[] split = str.split("\n");
		int s = split.length;
		
		for( int i = 0; i < s; i++ )
		g.drawString(split[i], x, y + i * sep);
	}
	
	// GETTERS AND SETTERS
	
	/**
	 * Returns the width of a cursor cell (in pixels).
	 * 
	 * @return Width of a cursor cell.
	 */
	public int getCursorCellWidth() {
		return this.cursorCellWidth;
	}
	
	/**
	 * Returns the height of a cursor cell (in pixels).
	 * 
	 * @return Height of a cursor cell.
	 */
	public int getCursorCellHeight() {
		return this.cursorCellHeight;
	}
	
	/**
	 * Sets the overlay BufferedImage that will be 
	 * rendered over all of the components of the Scene.
	 * 
	 * @param overlay Reference to the BufferedImage 
	 * containing the overlay that is to be rendered.
	 */
	public void setOverlay(BufferedImage overlay) {
		this.overlay = overlay;
		update();
	}
}
