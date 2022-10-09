package editor2d2.gui.body;

import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import editor2d2.gui.GUIComponent;
import editor2d2.gui.GUIUtilities;
import editor2d2.gui.components.ClickableButton;
import editor2d2.model.project.Asset;
import editor2d2.model.project.scene.placeable.Placeable;
import editor2d2.modules.data.asset.Data;
import editor2d2.modules.data.proppane.DataCellPropertiesPane;
import editor2d2.modules.image.asset.Image;
import editor2d2.modules.image.proppane.TilePropertiesPane;
import editor2d2.modules.object.asset.EObject;
import editor2d2.modules.object.proppane.InstancePropertiesPane;

public abstract class PropertiesPane extends GUIComponent {
	
		// Source Placeable whose properties the pane is representing
	protected Placeable source;
	
	
	protected PropertiesPane(Placeable source) {
		this.source = source;
	}
	
	
	public static PropertiesPane createPropertiesPane(Placeable source) {
		if( source == null )
		return null;
		
		Asset srcAsset = source.getAsset();
		
		if( srcAsset instanceof Image )
		return new TilePropertiesPane(source);
		
		if( srcAsset instanceof EObject )
		return new InstancePropertiesPane(source);
		
		if( srcAsset instanceof Data )
		return new DataCellPropertiesPane(source);
		
		return null;
	}
	
	
	/**
	 * Called upon clicking the "Apply"-button. Applies the current
	 * configuration to the source Placeable.
	 * <br />
	 * By default, does nothing.
	 * <br /><br />
	 * CAN BE OVERRIDDEN!
	 * 
	 * @param ae ActionEvent-object that was created as a result of
	 * the button being clicked. (Most likely wont be used)
	 */
	public void actionApply(ActionEvent ae) { }
	
	
	/**
	 * Creates a JPanel of a default properties pane wrapping a given body
	 * within. This can be used in the draw-methods of the subclasses
	 * to include the default features of a properties pane. These features
	 * include: <br /><br />
	 * - a top bar with a title <br />
	 * - the name of the asset the Placeable is based on <br />
	 * - controls for applying changes ("Apply"-button)
	 * 
	 * @param title Title of the properties pane.
	 * 
	 * @param body JPanel representing the body of the properties pane. That
	 * is, the part that actually differs.
	 * 
	 * @return Returns a reference to the generated JPanel representing the
	 * properties pane. 
	 */
	protected JPanel createDefaultPropertiesPaneContainer(String title, JPanel body) {
		JPanel container = GUIUtilities.createDefaultPanel();
		container.add(new JLabel(title));
		container.add(new JLabel("Asset: " + this.source.getAsset().getName()));
		
		container.add(body);
		container.add(new ClickableButton("Apply", (e) -> { actionApply(e); }));
		
		return container;
	}
}
