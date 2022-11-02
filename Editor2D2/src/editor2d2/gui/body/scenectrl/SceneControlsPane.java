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

public class SceneControlsPane extends GUIComponent implements Vendor, Subscriber {
	
	private int cursorCellWidth;
	
	private int cursorCellHeight;
	
	private CTextField txtCursorGridWidth;
	
	private CTextField txtCursorGridHeight;
	
	
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
		JPanel resizeContainer = GUIUtilities.createDefaultPanel();
		
			CIcon iconResizeScene = new CIcon(
				Application.resources.getGraphic("icon-resize-scene"),
				(e) -> {
					actionResizeScene();
				},
				24, 24
			);
			
			iconResizeScene.tooltip = "Resize scene";
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

	
	private void actionResizeScene() {
		JPanel container = GUIUtilities.createDefaultPanel();
		Scene scene = Application.controller.getActiveScene();
		
		if( scene == null )
		return;
		
		CTextField txtNewWidth = new CTextField("New width: ", new RequireIntegerBetween(Require.MIN_ONLY, 1));
		txtNewWidth.orientation = GUIUtilities.BOX_PAGE_AXIS;
		txtNewWidth.setText(""+scene.getWidth());
		CTextField txtNewHeight = new CTextField("New height: ", new RequireIntegerBetween(Require.MIN_ONLY, 1));
		txtNewHeight.orientation = GUIUtilities.BOX_PAGE_AXIS;
		txtNewHeight.setText(""+scene.getHeight());
		
		container.add(txtNewWidth.render());
		addEmptySpace(container);
		container.add(txtNewHeight.render());

		int result = JOptionPane.showConfirmDialog(
			null,
			container, 
			"Enter scene dimensions",
			JOptionPane.OK_CANCEL_OPTION
		);
		
		if( result == JOptionPane.OK_OPTION )
		{
			RequireIntegerBetween rfWidth = (RequireIntegerBetween) txtNewWidth.getRequirementFilter();
			RequireIntegerBetween rfHeight = (RequireIntegerBetween) txtNewHeight.getRequirementFilter();
			
			if( !rfWidth.checkValid() || !rfHeight.checkValid() )
			return;
			
			
		}
	}
	
	private void actionToggleLayerGrid() {
		Application.window.subscriptionService.register(Handles.LAYER_GRID_TOGGLED, this);
	}
	
	private void actionToggleCursorGrid() {
		Application.window.subscriptionService.register(Handles.CURSOR_GRID_TOGGLED, this);
	}
	
	
	public int getCursorCellWidth() {
		return (int) this.txtCursorGridWidth.getRequirementFilter().getValue();
	}
	
	public int getCursorCellHeight() {
		return (int) this.txtCursorGridHeight.getRequirementFilter().getValue();
	}
}
