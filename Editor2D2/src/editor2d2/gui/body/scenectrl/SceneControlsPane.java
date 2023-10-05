package editor2d2.gui.body.scenectrl;

import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import editor2d2.Application;
import editor2d2.gui.GUIComponent;
import editor2d2.gui.GUIUtilities;
import editor2d2.gui.Handles;
import editor2d2.gui.body.scene.SceneView;
import editor2d2.gui.components.CIcon;
import editor2d2.gui.components.CTextField;
import editor2d2.gui.components.requirements.Require;
import editor2d2.gui.components.requirements.RequireIntegerBetween;
import editor2d2.model.project.scene.Scene;
import editor2d2.subservice.Subscriber;
import editor2d2.subservice.Vendor;

/**
 * This GUI-component contains the Scene controls. It 
 * can be used to control the currently active Scene. 
 * This component contains the button for snapping 
 * the Scene Camera back to the origin point (0,0), 
 * button for resizing the Scene, buttons for toggling 
 * the visibility of the Layer and cursor grids as 
 * well as the cursor grid settings.
 * <br/><br/>
 * 
 * The Layer grid is the one that the Layer uses to 
 * store the Placeables placed onto it. See Layer for 
 * more information on the Layer grid. The cursor 
 * grid is the one used by the editor to snap the 
 * cursor position into a defined grid. This is 
 * can be useful when placing Placeables onto Layers 
 * that dont respect the Layer grid.
 * <br/><br/>
 * 
 * This class implements both the Subscriber- and the 
 * Vendor-interface as it receives updates to the 
 * Project and vends updates to components when 
 * controlling the Scene.
 * 
 * @author User
 *
 */
public class SceneControlsPane extends GUIComponent implements Vendor, Subscriber {
	
	/**
	 * Width of a cursor cell (in pixels).
	 * <br/><br/>
	 * 
	 * See the class explanation for more 
	 * information on the cursor grid.
	 */
	private int cursorCellWidth;
	
	/**
	 * Height of hte cursor cell (in pixels).
	 * <br/><br/>
	 * 
	 * See the class explanation for more 
	 * information on the cursor grid.
	 */
	private int cursorCellHeight;
	
	/**
	 * CTextField component that is used to alter 
	 * the cursor grid width (in pixels).
	 * <br/><br/>
	 * 
	 * See the class explanation for more 
	 * information on the cursor grid.
	 */
	private CTextField txtCursorGridWidth;
	
	/**
	 * CTextField component that is used to alter 
	 * the cursor grid height (in pixels).
	 * <br/><br/>
	 * 
	 * See the class explanation for more 
	 * information on the cursor grid.
	 */
	private CTextField txtCursorGridHeight;
	
	/**
	 * Constructs a SceneControlsPane instance with 
	 * the default, zeroed out, cursor grid settings 
	 * and subscribes the SceneControlsPane to the 
	 * SceneView component. The SceneControlsPane is 
	 * also registered to provide updates regarding 
	 * cursor grid changes.
	 */
	public SceneControlsPane() {
		this.cursorCellWidth = 0;
		this.cursorCellHeight = 0;
		
			// Subscribe for changes in the SceneView
		Vendor v = Application.window.subscriptionService.get(Handles.SCENE_VIEW, "SceneControlsPane", this);
		getCursorGridSettings(v);
		
		this.txtCursorGridWidth = new CTextField(
			"Cell width: ",
			new RequireIntegerAndNotify(1, Handles.CURSOR_GRID_SETTINGS_CHANGED, this)
		);
		
		this.txtCursorGridHeight = new CTextField(
			"Cell height: ",
			new RequireIntegerAndNotify(1, Handles.CURSOR_GRID_SETTINGS_CHANGED, this)
		);
	}
	

	@Override
	public void onNotification(String handle, Vendor vendor) {
		if( handle.equals(Handles.SCENE_VIEW) )
		getCursorGridSettings(vendor);
	}
	
	/**
	 * When cursor grid settings are received from the 
	 * SceneView instance via the 
	 * onNotification-method, the Vendor (SceneView) 
	 * can be passed onto this method where the cursor 
	 * grid settings are extracted and stored in the 
	 * SceneControlsPane's state.
	 * 
	 * <b>Warning: </b>despite looking like it this 
	 * method is NOT a getter as it returns no values.
	 * 
	 * @param vendor
	 */
	private void getCursorGridSettings(Vendor vendor) {
		if( vendor == null )
		return;
		
		SceneView sv = (SceneView) vendor;
		this.cursorCellWidth = sv.getCursorCellWidth();
		this.cursorCellHeight = sv.getCursorCellHeight();
	}
	
	
	@Override
	protected JPanel draw() {
		JPanel container = GUIUtilities.createDefaultPanel(GUIUtilities.BOX_LINE_AXIS);
		
			// Icon - Resize Scene
		JPanel resizeContainer = GUIUtilities.createDefaultPanel(GUIUtilities.BOX_LINE_AXIS);
		
				// Icon - Return camera to origin
			CIcon iconReturnToOrigin = new CIcon(
				Application.resources.getGraphic("icon-return-to-origin"),
				(e) -> {
					actionReturnToOrigin();
				},
				24, 24
			);
			
			iconReturnToOrigin.tooltip = "Return camera to origin";
			
				// Icon - Resize Scene
			CIcon iconResizeScene = new CIcon(
				Application.resources.getGraphic("icon-resize-scene"),
				(e) -> {
					actionResizeScene();
				},
				24, 24
			);
			
			iconResizeScene.tooltip = "Resize scene";
			
		resizeContainer.add(iconReturnToOrigin.render());
		addEmptySpace(resizeContainer, 2);
		resizeContainer.add(iconResizeScene.render());
		resizeContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
		
			
		JPanel gridControlsContainer = GUIUtilities.createDefaultPanel(GUIUtilities.BOX_LINE_AXIS);
				// Icon - Toggle Layer grid
			CIcon iconToggleLayerGrid = new CIcon(
				Application.resources.getGraphic("icon-toggle-layer-grid"),
				(e) -> {
					actionToggleLayerGrid();
				},
				24, 24
			);
			
			iconToggleLayerGrid.tooltip = "Toggle layer grid visibility";
		
				// Icon - Toggle cursor grid
			CIcon iconToggleCursorGrid = new CIcon(
				Application.resources.getGraphic("icon-toggle-cursor-grid"),
				(e) -> {
					actionToggleCursorGrid();
				},
				24, 24
			);
		
			iconToggleCursorGrid.tooltip = "Toggle cursor grid visibility";
			
			gridControlsContainer.add(iconToggleLayerGrid.render());
			addEmptySpace(gridControlsContainer, 2);
			gridControlsContainer.add(iconToggleCursorGrid.render());
			addEmptySpace(gridControlsContainer, 2);
			gridControlsContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
			
				// Cursor grid settings
			this.txtCursorGridWidth.setText(""+this.cursorCellWidth);
			this.txtCursorGridWidth.textField.addKeyListener(new KeyAdapter() {

				@Override
				public void keyPressed(KeyEvent e) {
					if( e.getKeyCode() == KeyEvent.VK_ENTER )
					Application.window.unfocusAllComponents();
				}
			});
			this.txtCursorGridHeight.setText(""+this.cursorCellHeight);
			
			gridControlsContainer.add(this.txtCursorGridWidth.render());
			addEmptySpace(gridControlsContainer, 4);
			gridControlsContainer.add(this.txtCursorGridHeight.render());
		
		container.add(resizeContainer);
		container.add(gridControlsContainer);
		return container;
	}

	/**
	 * Called upon clicking the Scene resize-button.
	 * A JOptionPane-dialog box is opened where the 
	 * user can input the new width and height of the 
	 * Scene (in pixels). If the new Scene dimensions 
	 * are valid, the Scene is resized.
	 * <br/><br/>
	 * 
	 * <b>Warning:</b> Resizing the Scene may lead to 
	 * unexpected behavior when one of the dimensions 
	 * gets smaller. When this happens the Placeables 
	 * left outside of the Scene will be removed.
	 */
	private void actionResizeScene() {
		JPanel container = GUIUtilities.createDefaultPanel();
		Scene scene = Application.controller.getActiveScene();
		
		if( scene == null )
		return;
		
			// Add Scene dimensions input fields
		CTextField txtNewWidth = new CTextField("New width: ", new RequireIntegerBetween(Require.MIN_ONLY, 1));
		txtNewWidth.orientation = GUIUtilities.BOX_PAGE_AXIS;
		txtNewWidth.setText(""+scene.getWidth());
		CTextField txtNewHeight = new CTextField("New height: ", new RequireIntegerBetween(Require.MIN_ONLY, 1));
		txtNewHeight.orientation = GUIUtilities.BOX_PAGE_AXIS;
		txtNewHeight.setText(""+scene.getHeight());
		
		container.add(txtNewWidth.render());
		addEmptySpace(container);
		container.add(txtNewHeight.render());

			// Display the input dialog
		int result = JOptionPane.showConfirmDialog(
			null,
			container, 
			"Enter scene dimensions",
			JOptionPane.OK_CANCEL_OPTION
		);
		
		if( result != JOptionPane.OK_OPTION )
		return;
		
			// Validate inputs and resize the Scene
		RequireIntegerBetween rfWidth = (RequireIntegerBetween) txtNewWidth.getRequirementFilter();
		RequireIntegerBetween rfHeight = (RequireIntegerBetween) txtNewHeight.getRequirementFilter();
		
		if( !rfWidth.checkValid() || !rfHeight.checkValid() )
		return;
		
		int newWidth = rfWidth.getValue(),
			newHeight = rfHeight.getValue();
		
			// Confirm the resize when the action may result in the deletion
			// of Placeables left outside of the new bounds of the Scene
		if( newWidth < scene.getWidth() || newHeight < scene.getHeight() )
		{
			result = JOptionPane.showConfirmDialog(null,
					  "Making the scene smaller causes objects left out of "
					+ "bounds to be removed! \n\n"
					+ "Are you sure you want to continue?",
					"Resize confirmation",
					JOptionPane.YES_NO_OPTION);
			
			if( result != JOptionPane.YES_OPTION )
			return;
		}
		
		scene.setDimensions(newWidth, newHeight);
	}
	
	/**
	 * Called upon clicking the Layer grid's 
	 * visibility toggle button. Toggle's the 
	 * visibility of the Layer grid in the 
	 * SceneView.
	 */
	private void actionToggleLayerGrid() {
		Application.window.subscriptionService.register(Handles.LAYER_GRID_TOGGLED, this);
	}
	
	/**
	 * Called upon clicking the cursor grid's 
	 * visibility toggle button. Toggle's the 
	 * visibility of the cursor grid in the 
	 * SceneView.
	 */
	private void actionToggleCursorGrid() {
		Application.window.subscriptionService.register(Handles.CURSOR_GRID_TOGGLED, this);
	}
	
	/**
	 * Called upon clicking the return to origin 
	 * button. Resets the position of the Scene's 
	 * Camera by setting its position to (0, 0, 1).
	 */
	private void actionReturnToOrigin() {
		Scene activeScene = Application.controller.getActiveScene();
		
		if( activeScene == null )
		return;
		
		activeScene.getCamera().setPosition(0, 0, 1);
		
		Application.window.subscriptionService.register(Handles.CAMERA_RETURNED_TO_ORIGIN, this);
	}
	
	// GETTERS AND SETTERS
	
	/**
	 * Returns the width of a cursor grid cell 
	 * (in pixels).
	 * 
	 * @return Cursor grid cell width.
	 */
	public int getCursorCellWidth() {
		return (int) this.txtCursorGridWidth.getRequirementFilter().getValue();
	}
	
	/**
	 * Returns the height of a cursor grid cell
	 * (in pixels).
	 * 
	 * @return Cursor grid cell height.
	 */
	public int getCursorCellHeight() {
		return (int) this.txtCursorGridHeight.getRequirementFilter().getValue();
	}
}
